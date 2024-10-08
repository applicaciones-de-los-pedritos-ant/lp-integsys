/**
 * Maynard Valencia
 *
 * @since 2024-08-22
 */
package org.rmj.cas.food.inventory.fx.views.child;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;
import static org.rmj.cas.food.inventory.fx.views.FoodInventoryFX.xsRequestFormat;
import org.rmj.cas.inventory.base.Inventory;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMTerm;
import org.rmj.purchasing.agent.PurchaseOrders;

public class InvStockRequestPOController implements Initializable {

    private static GRider poGRider;
    private final String pxeModuleName = "InvStockRequestPOController";

    private PurchaseOrders poTrans = null;
    private boolean pbCancelled;
    private String psValue;
    private int pnRow;
    private int pnIndex = -1;
    private String psOldRec = "";

    private boolean pbLoaded = false;

    @FXML
    private TextField txtField01, txtField02, txtField03,
            txtField04, txtField05, txtField06, txtField07;
    @FXML
    private TextArea txtField08;
    @FXML
    private Button btnOk, btnCancel, btnExit;
    @FXML
    private TableView table;
    @FXML
    private TableColumn index01, index02, index03, index04,
            index05, index06, index07, index08, index09;
    @FXML
    private Label Label12;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        pbCancelled = true;
        psValue = "";

        btnExit.setOnAction(this::cmdButton_Click);
        btnOk.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);

        initGrid();
        loadRecord();
        loadDetail2Grid();

        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtArea_Focus);

        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);

        pbLoaded = true;
    }

    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnExit":
            case "btnCancel":

                CommonUtils.closeStage(btnExit);
                break;
            case "btnOk":
                if (poTrans.saveTransaction()) {

                    pbCancelled = false;
                    CommonUtils.closeStage(btnOk);
                    break;
                } else {
                    poTrans.ShowMessageFX();
                    return;
                }

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
        }
    }

    private void initGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;");
        index05.setStyle("-fx-alignment: CENTER-LEFT;");
        index06.setStyle("-fx-alignment: CENTER-RIGHT;");
        index07.setStyle("-fx-alignment: CENTER-RIGHT;");
        index08.setStyle("-fx-alignment: CENTER-RIGHT;");
        index09.setStyle("-fx-alignment: CENTER-RIGHT;");

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index08"));
        index09.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.inventory.base.views.TableModel, String>("index09"));

        table.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) table.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        table.setItems(data);

        index08.setCellFactory(TextFieldTableCell.forTableColumn());
        index08.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModel, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<TableModel, String> event) {
                TableModel tableModel = event.getRowValue();
                tableModel.setIndex08(event.getNewValue());
                if (poGRider.getUserLevel() >= 2) {
                    poTrans.setDetail(pnRow, "nUnitPrce", Double.valueOf(tableModel.getIndex08()));
                } else {
                    poTrans.setDetail(pnRow, "nUnitPrce", 1.0);
                }

                loadDetail2Grid();

            }
        });
    }

    public void loadDetail2Grid() {
        data.clear();
        int lnCtr;
        if (poTrans == null) {
            return;
        }

        int lnRow = poTrans.ItemCount();
        Inventory loInventory;
        for (lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++) {

            loInventory = poTrans.GetInventory((String) poTrans.getDetail(lnCtr, "sStockIDx"), true, false);

            data.add(new TableModel(String.valueOf(lnCtr + 1),
                    (String) loInventory.getMaster("sBarCodex"),
                    (String) poTrans.getDetail(lnCtr, "sBrandNme"),
                    (String) loInventory.getMaster("sDescript"),
                    loInventory.getMeasureMent((String) loInventory.getMaster("sMeasurID")),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQtyOnHnd").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()), "0.00"),
                    String.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce")),
                    CommonUtils.NumberFormat(((Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()))
                            * Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString())), "#,##0.00"),
                    (String) poTrans.getDetail(lnCtr, "sOrderNox")));
        }
        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()) {
            table.getSelectionModel().select(lnRow - 1);
            table.getFocusModel().focus(lnRow - 1);

            pnRow = table.getSelectionModel().getSelectedIndex();
        }
//        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nTranTotl").toString()), "#,##0.00"));
        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()), "#,##0.00"));

    }

    private void loadRecord() {
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField02.setText(xsRequestFormat((Date) poTrans.getMaster("dTransact")));

        XMBranch loBranch = null;
        loBranch = poTrans.GetBranch((String) poTrans.getMaster(2), true);
        if (loBranch != null) {
            txtField03.setText((String) loBranch.getMaster("sBranchNm"));
        }

        loBranch = poTrans.GetBranch((String) poTrans.getMaster(5), true);
        if (loBranch != null) {
            txtField04.setText((String) loBranch.getMaster("sBranchNm"));
        }

        txtField06.setText((String) poTrans.getMaster("sReferNox"));
        txtField08.setText((String) poTrans.getMaster("sRemarksx"));
        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()), "#,##0.00"));
        psOldRec = (String) poTrans.getMaster("sTransNox");

    }

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
    }

    private Stage getStage() {
        return (Stage) btnOk.getScene().getWindow();
    }

    private ObservableList<TableModel> data = FXCollections.observableArrayList();

    public String getValue() {
        return psValue;
    }

    public boolean isCancelled() {
        return pbCancelled;
    }

    public void setPurchaseOrders(PurchaseOrders foRS) {
        this.poTrans = foRS;
    }

    public void setGRider(GRider foGRider) {
        this.poGRider = foGRider;
    }
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
        } else {
            pnIndex = -1;
            txtField.selectAll();
        }
    };
    final ChangeListener<? super Boolean> txtArea_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextArea txtField = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {

                case 8:
                    /*sRemarksx*/
                    if (lsValue.length() > 126) {
                        lsValue = lsValue.substring(0, 126);
                    }
                    poTrans.setMaster("sRemarksx", CommonUtils.TitleCase(lsValue));
                    txtField.setText((String) poTrans.getMaster("sRemarksx"));
                    break;
            }
        } else {
            pnIndex = -1;
            txtField.selectAll();
        }
    };

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));

        switch (event.getCode()) {
            case F3:
                switch (lnIndex) {

                    case 5:
                        /*sSupplier*/
                        if (poTrans.SearchMaster(6, txtField.getText(), false) == true) {

                            JSONObject loSupplier = poTrans.GetSupplier((String) poTrans.getMaster(6), true);
                            if (loSupplier != null) {
                                txtField.setText((String) loSupplier.get("sClientNm"));
                            }
                        } else {
                            txtField.setText("");
                        }
                        break;
                    case 7:
                        /*sTermCode*/
                        if (poTrans.SearchMaster(8, txtField.getText(), false) == true) {
                            XMTerm loTerm = poTrans.GetTerm((String) poTrans.getMaster(8), true);

                            if (loTerm != null) {
                                txtField.setText((String) loTerm.getMaster("sDescript"));
                            }
                        } else {
                            txtField.setText("");
                        }
                        break;

                }
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
    }

}
