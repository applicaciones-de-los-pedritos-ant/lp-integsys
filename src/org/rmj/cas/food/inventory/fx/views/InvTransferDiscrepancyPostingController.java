package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.appdriver.constants.UserRight;
import org.rmj.cas.inventory.base.InvTransferDiscrepancy;
import org.rmj.lp.parameter.agent.XMBranch;

public class InvTransferDiscrepancyPostingController implements Initializable {

    @FXML
    private AnchorPane dataPane;
    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private AnchorPane anchorField;
    @FXML
    private TableView table;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private TextField txtField50;
    @FXML
    private Label lblHeader;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField99;
    @FXML
    private TextField txtField51;
    @FXML
    private TextArea txtField05;
    @FXML
    private TextField txtDetail01;
    @FXML
    private TextField txtDetail02;
    @FXML
    private TextArea txtDetail03;
    @FXML
    private TextField txtDetail05;
    @FXML
    private TextField txtDetail06;
    @FXML
    private Label Label12;
    @FXML
    private TextArea txtDetail04;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnPost;

    private final String pxeModuleName = "InvTransferDiscrepancyPostingController";
    private static GRider poGRider;
    private InvTransferDiscrepancy poTrans;
    private int pnEditMode = -1;
    private boolean pbLoaded = false;

    private final String pxeDateFormat = "MM/dd/yyyy";
    private final String pxeDateFormatMsg = "Date format must be MM/dd/yyyy (e.g. 12/25/1945)";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();

    private TableModel model;
    private ObservableList<TableModel> data = FXCollections.observableArrayList();

    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;

    private String psDestina = "";
    private String psTransNox = "";
    private String psOldRec = "";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvTransferDiscrepancy(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(1230);

        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnPost.setOnAction(this::cmdButton_Click);

        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtField99.focusedProperty().addListener(txtField_Focus);

        txtDetail04.focusedProperty().addListener(txtDetailArea_Focus);

        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        pnEditMode = EditMode.UNKNOWN;
        clearFields();
        initGrid();

        pbLoaded = true;
    }

    public void setGRider(GRider foGRider) {
        this.poGRider = foGRider;
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField50.setText("");

        txtDetail01.setText("");
        txtDetail02.setText("");
        txtDetail03.setText("");
        txtDetail04.setText("");
        txtDetail04.setEditable(false);
        txtDetail05.setText("0.00");
        txtDetail06.setText("0");
        Label12.setText("0.00");
        

        pnRow = -1;
        pnOldRow = -1;
        pnIndex = 51;
        setTranStat("-1");

        psOldRec = "";
        psDestina = "";
        psTransNox = "";
        data.clear();
    }

    
    private void initGrid() {
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Brand");
        TableColumn index05 = new TableColumn("Measure");
        TableColumn index06 = new TableColumn("Unit Price");
        TableColumn index07 = new TableColumn("Qty");

        index01.setPrefWidth(30);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(120);
        index04.setPrefWidth(130);
        index05.setPrefWidth(65);
        index06.setPrefWidth(60);
        index07.setPrefWidth(55);
        index07.setStyle("-fx-alignment: CENTER-RIGHT;");

        index01.setSortable(false);
        index01.setResizable(false);
        index02.setSortable(false);
        index02.setResizable(false);
        index03.setSortable(false);
        index03.setResizable(false);
        index04.setSortable(false);
        index04.setResizable(false);
        index05.setSortable(false);
        index05.setResizable(false);
        index06.setSortable(false);
        index06.setResizable(false);
        index07.setSortable(false);
        index07.setResizable(false);

        table.getColumns().clear();
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        table.getColumns().add(index07);

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index07"));

        /*making column's position uninterchangebale*/
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
    }

    private void unloadForm() {

        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnClose":
            case "btnExit":
                unloadForm();
                return;

            case "btnBrowse":
                switch (pnIndex) {
                    case 50:
                        /*sTransNox*/
                        if (poTrans.BrowseAcceptance(txtField50.getText(), true) == true) {
                            txtField99.setText(FoodInventoryFX.xsRequestFormat(poGRider.getServerDate()));
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                            break;
                        } else if (!txtField50.getText().equals(psTransNox)) {
                            clearFields();
                            break;
                        } else {
                            txtField50.setText(psTransNox);
                        }
                        return;

                    case 51:
                        /*sDestination*/
                        if (poTrans.BrowseAcceptance(txtField51.getText() + "%", false) == true) {
                            txtField99.setText(FoodInventoryFX.xsRequestFormat(poGRider.getServerDate()));
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }
                        if (!txtField51.getText().equals(psDestina)) {
                            clearFields();
                            break;
                        } else {
                            txtField51.setText(psDestina);
                        }
                        return;

                    default:
                        ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                        txtField51.requestFocus();
                }
                return;
            case "btnPost":
                if (psOldRec.equals("")) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to post!");
                    return;
                }
                if (!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN) && !poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_CLOSED)) {
                    ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                    return;
                }
                if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to post this transaction?") == true) {
//                    if (pnDetTotl != pnExpTotl) {
////                        if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to post this transaction?") == true) {
                            if (poGRider.getUserLevel() <= UserRight.ENCODER) {
                                JSONObject loJSON = showFXDialog.getApproval(poGRider);
                                if (loJSON == null) {
                                    ShowMessageFX.Warning("Approval failed.", pxeModuleName, "Unable to post transaction");
                                    return;
                                }
                                if ((int) loJSON.get("nUserLevl") <= UserRight.ENCODER) {
                                    ShowMessageFX.Warning("User account has no right to approve.", pxeModuleName, "Unable to post transaction");
                                    return;
                                }
                            }
////                        } else {
////                            return;
////                        }
//                    }
                        if (poTrans.postTransaction(psOldRec, SQLUtil.toDate(txtField99.getText(), pxeDateFormat))) {
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction POSTED successfully.");
                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                    
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }

    }

    private void loadRecord() {

        txtField50.setText((String) poTrans.getMaster("sTransNox"));
        psTransNox = txtField50.getText();

        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField02.setText(SQLUtil.dateFormat((Date) poTrans.getMaster("dTransact"), pxeDateFormat));

        XMBranch loBranch = poTrans.GetBranch((String) poTrans.getMaster(2), true);
        if (loBranch != null) {
            txtField03.setText((String) loBranch.getMaster("sBranchNm"));
            txtField51.setText((String) loBranch.getMaster("sBranchNm"));
        }

        txtField04.setText((String) poTrans.getMaster("sSourceNo"));
        txtField05.setText((String) poTrans.getMaster("sRemarksx"));

        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nTranTotl").toString()), "#,##0.00"));
        txtDetail04.setEditable(true);
        pnRow = 0;
        pnOldRow = 0;
        loadDetail();
        setTranStat((String) poTrans.getMaster("cTranStat"));
        psOldRec = txtField01.getText();
    }

    private void setTranStat(String fsValue) {
        switch (fsValue) {
            case "0":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/open.png"));
                break;
            case "1":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/closed.png"));
                break;
            case "2":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/posted.png"));
                break;
            case "3":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/cancelled.png"));
                break;
            case "4":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/void.png"));
                break;
            default:
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/unknown.png"));
        }
    }

    private void loadDetail() {
        int lnCtr;
        int lnRow = poTrans.ItemCount();

        data.clear();
        /*ADD THE DETAIL*/
        for (lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++) {
            data.add(new TableModel(String.valueOf(lnCtr + 1),
                    (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"),
                    (String) poTrans.getDetailOthers(lnCtr, "sDescript"),
                    (String) poTrans.getDetailOthers(lnCtr, "sBrandNme"),
                    (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nInvCostx").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()), "0.00"),
                    "",
                    "",
                    ""));
            System.out.println(poTrans.getDetailOthers(lnCtr, "sBrandNme"));
        }
        initGrid();
        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()) {
            table.getSelectionModel().select(lnRow - 1);
            table.getFocusModel().focus(lnRow - 1);

            pnRow = table.getSelectionModel().getSelectedIndex();

            setDetailInfo(pnRow);
        }
        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nTranTotl").toString()), "#,##0.00"));
    }

    private void setDetailInfo(int fnRow) {
        if (fnRow >= 0) {
            txtDetail01.setText((String) poTrans.getDetailOthers(fnRow, "sBarCodex"));
            txtDetail02.setText((String) poTrans.getDetailOthers(fnRow, "sDescript"));
            txtDetail03.setText((String) poTrans.getDetail(fnRow, "sRemarksx"));
            txtDetail04.setText((String) poTrans.getDetail(fnRow, "sNotesxxx"));
            txtDetail05.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(fnRow, "nQuantity").toString()), "0.00"));
            txtDetail06.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(fnRow, "nInvCostx").toString()), "0.00"));
        }
    }

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
        if (pnRow < 0) {
            return;
        }

        setDetailInfo(pnRow);

        txtDetail01.requestFocus();
        txtDetail01.selectAll();

    }

    IMasterDetail poCallBack = new IMasterDetail() {
        @Override
        public void MasterRetreive(int fnIndex) {
        }

        @Override
        public void DetailRetreive(int fnIndex) {
            switch (fnIndex) {

            }
        }
    };

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if ( event.getCode() == F3) {
            switch (lnIndex) {
                case 50:
                    /*sTransNox*/
                    if (event.getCode() == F3) {
                        lsValue = txtField.getText();
                    }
                    if (poTrans.BrowseAcceptance(lsValue, true) == true) {
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                        break;
                    } else if (!txtField50.getText().equals(psTransNox)) {
                        clearFields();
                        break;
                    } else {
                        txtField50.setText(psTransNox);
                    }
                    return;

                case 51:
                    /*psDestina*/
                    if (event.getCode() == F3) {
                        lsValue = txtField.getText() + "%";
                    }
                    if (poTrans.BrowseAcceptance(lsValue, false) == true) {
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                        break;
                    } else {
                        ShowMessageFX.Warning(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department");
                    }

                    if (!txtField51.getText().equals(psDestina)) {
                        clearFields();
                        break;
                    } else {
                        txtField51.setText(psDestina);
                    }

                    return;
            }
        }

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
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
                case 50:
                    /*sTransNox*/
                    if (lsValue.equals("") || lsValue.equals("%")) {
                        txtField.setText("");
                    }
                    break;
                case 51:
                    /*sDestination*/
                    if (lsValue.equals("") || lsValue.equals("%")) {
                        txtField.setText("");
                    }
                    break;
                case 99:
                    /*dReceived*/
                    if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)) {
                        txtField.setText(SQLUtil.dateFormat(SQLUtil.toDate(txtField.getText(), pxeDateFormat), pxeDateFormat));
                    } else {
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                        txtField.setText(FoodInventoryFX.xsRequestFormat(CommonUtils.toDate(pxeDateDefault)));
                    }
                    return;
            }
            pnOldRow = table.getSelectionModel().getSelectedIndex();
            pnIndex = lnIndex;
        } else {
            switch (lnIndex) {
                case 99:
                    /*dReceived*/
                    txtField.setText(SQLUtil.dateFormat(SQLUtil.toDate(txtField.getText(), pxeDateFormat), pxeDateFormat));
                    txtField.selectAll();
                    break;
                default:
            }
            pnIndex = -1;
            txtField.selectAll();
        }

    };

    final ChangeListener<? super Boolean> txtDetailArea_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextArea txtDetail = (TextArea) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();

        if (lsValue == null) {
            return;
        }
        if (pnRow < 0) {
            return;
        }
        if (txtField01.getText().isEmpty()) {
            return;
        }

        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 04:
                    /*sNotesxxx*/
                    if (lsValue.length() > 126) {
                        lsValue = lsValue.substring(0, 126);
                    }

                    poTrans.setDetail(pnRow, "sNotesxxx", CommonUtils.TitleCase(lsValue));
            }
        } else {
            pnIndex = -1;
            txtDetail.selectAll();
        }
    };

}
