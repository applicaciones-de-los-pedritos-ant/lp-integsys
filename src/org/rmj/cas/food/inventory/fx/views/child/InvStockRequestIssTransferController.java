/**
 * Maynard Valencia
 *
 * @since 2024-08-21
 */
package org.rmj.cas.food.inventory.fx.views.child;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.appdriver.constants.UserRight;
import static org.rmj.cas.food.inventory.fx.views.FoodInventoryFX.xsRequestFormat;
import org.rmj.cas.inventory.base.InvMaster;
import org.rmj.cas.inventory.base.InvTransfer;
import org.rmj.lp.parameter.agent.XMBranch;

public class InvStockRequestIssTransferController implements Initializable {

    private static GRider poGRider;
    private final String pxeModuleName = "InvStockRequestIssTransferController";

    private InvTransfer poTrans = null;
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
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transaction?") == true) {
                        
                            if (poTrans.closeTransaction(psOldRec)) {
                                if (printTransfer()) {
                                    
                                } else {
                                    return;
                                }
                            } else {
                                if  (poTrans.getErrMsg().isEmpty()){
                                ShowMessageFX.Warning(poTrans.getMessage() , pxeModuleName, "Unable to confirm transaction.");
                                }else {
                                ShowMessageFX.Error(poTrans.getErrMsg(),pxeModuleName, "Unable to confirm transaction.");
                                }
                        }
                    }
                    pbCancelled = false;
                    CommonUtils.closeStage(btnOk);
                    break;
                } else {
                    if (!poTrans.getErrMsg().equals("")) {
                        ShowMessageFX.Error(poTrans.getErrMsg(), pxeModuleName, "Please inform MIS Department.");
                    } else {
                        ShowMessageFX.Warning(poTrans.getMessage(), pxeModuleName, "Please verify your entry.");
                    }
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

//        index08.setCellFactory(TextFieldTableCell.forTableColumn());
//        index08.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<TableModel, String>>() {
//            @Override
//            public void handle(TableColumn.CellEditEvent<TableModel, String> event) {
//                TableModel tableModel = event.getRowValue();
//                tableModel.setIndex08(event.getNewValue());
//                poTrans.setDetail(pnRow, "nQuantity", Double.valueOf(tableModel.setIndex08()));
//                loadDetail2Grid();
//
//            }
//        });
    }

    public void loadDetail2Grid() {
        data.clear();
        int lnCtr;
        if (poTrans == null) {
            return;
        }

        int lnRow = poTrans.ItemCount();
        for (lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++) {
            data.add(new TableModel(String.valueOf(lnCtr + 1),
                    (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"),
                    (String) poTrans.getDetailOthers(lnCtr, "sDescript"),
                    (String) poTrans.getDetailOthers(lnCtr, "sBrandNme"),
                    (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetailOthers(lnCtr, "nQtyOnHnd").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()), "0.00"),
                    "0.00",
                    "0.00",
                    (String) poTrans.getDetail(lnCtr, "sOrderNox")));
            System.out.println(poTrans.getDetailOthers(lnCtr, "sBrandNme"));
        }
        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()) {
            table.getSelectionModel().select(lnRow - 1);
            table.getFocusModel().focus(lnRow - 1);

            pnRow = table.getSelectionModel().getSelectedIndex();
        }
//        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nTranTotl").toString()), "#,##0.00"));
        Label12.setText("0.00");

    }

    private void loadRecord() {
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField02.setText(xsRequestFormat((Date) poTrans.getMaster("dTransact")));

        XMBranch loBranch = poTrans.GetBranch((String) poTrans.getMaster(4), true);
        if (loBranch != null) {
            txtField03.setText((String) loBranch.getMaster("sBranchNm"));
        }

        txtField04.setText((String) poTrans.getMaster("sOrderNox"));
        txtField05.setText(poTrans.getMaster("nDiscount").toString());
        txtField06.setText((String) poTrans.getMaster("sTruckIDx"));
        txtField07.setText((String) poTrans.getMaster("nFreightx").toString());
        txtField08.setText((String) poTrans.getMaster("sRemarksx"));
        Label12.setText("0.00");
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

    public void setInvTransfer(InvTransfer foRS) {
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
            switch (lnIndex) {
                case 5:
                    /*nDiscount*/

                    double x = 0;
                    try {
                        /*this must be numeric*/
                        x = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        x = 0;
                        txtField.setText("0.0");
                    }
                    if (x > 100.00) {
                        x = 100.0;
                    }
                    poTrans.setMaster("nDiscount", x);
                    txtField.setText(poTrans.getMaster("nDiscount").toString());
                    break;
                case 7:
                    /*nFreightx*/

                    double y = 0;
                    try {
                        /*this must be numeric*/
                        y = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        y = 0;
                        txtField.setText("0.0");
                    }
                    if (y > 100.00) {
                        y = 100.0;
                    }
                    poTrans.setMaster("nFreightx", y);
                    txtField.setText(poTrans.getMaster("nFreightx").toString());
                    break;
            }
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

   private boolean printTransfer() {
        JSONArray json_arr = new JSONArray();
        json_arr.clear();

        for (int lnCtr = 0; lnCtr <= poTrans.ItemCount() - 1; lnCtr++) {
            JSONObject json_obj = new JSONObject();
            json_obj.put("sField01", (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"));
            json_obj.put("sField02", (String) poTrans.getDetailOthers(lnCtr, "sDescript"));
            json_obj.put("sField03", (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"));
            json_obj.put("sField04", (String) poTrans.getDetailOthers(lnCtr, "sBrandNme"));
            json_obj.put("lField01", (Double) poTrans.getDetail(lnCtr, "nQuantity"));
            json_arr.add(json_obj);
        }

        String lsSQL = "SELECT sBranchNm FROM Branch WHERE sBranchCD = " + SQLUtil.toSQL((String) poTrans.getMaster("sDestinat"));
        ResultSet loRS = poGRider.executeQuery(lsSQL);

        try {
            if (loRS.next()) {
                lsSQL = loRS.getString("sBranchNm");
            } else {
                lsSQL = (String) poTrans.getMaster("sDestinat");
            }

            //Create the parameter
            Map<String, Object> params = new HashMap<>();
            params.put("sReportNm", "Inventory Transfer");
            params.put("sBranchNm", poGRider.getBranchName());
            params.put("sBranchCd", poGRider.getBranchCode());
            params.put("sDestinat", lsSQL);
            params.put("sTransNox", poTrans.getMaster("sTransNox").toString().substring(1));
            params.put("sReportDt", CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
            params.put("sPrintdBy", System.getProperty("user.name"));
            params.put("xRemarksx", poTrans.getMaster("sRemarksx"));

            lsSQL = "SELECT sClientNm FROM Client_Master WHERE sClientID IN ("
                    + "SELECT sEmployNo FROM xxxSysUser WHERE sUserIDxx = " + SQLUtil.toSQL(poGRider.getUserID()) + ")";
            loRS = poGRider.executeQuery(lsSQL);

            if (loRS.next()) {
                params.put("sPrepared", loRS.getString("sClientNm"));
            } else {
                params.put("sPrepared", "");
            }

            InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
            JsonDataSource jrjson = new JsonDataSource(stream);

            JasperPrint _jrprint = JasperFillManager.fillReport("d:/GGC_Java_Systems/reports/InvTransferPrint.jasper", params, jrjson);
            JasperViewer jv = new JasperViewer(_jrprint, false);
            jv.setVisible(true);
            jv.setAlwaysOnTop(true);
        } catch (JRException | UnsupportedEncodingException | SQLException ex) {
            Logger.getLogger(InvStockRequestIssTransferController.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }
}
