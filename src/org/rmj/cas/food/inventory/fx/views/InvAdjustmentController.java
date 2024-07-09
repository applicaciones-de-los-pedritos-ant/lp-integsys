package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.Date;
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
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.cas.inventory.base.InvAdjustment;

/**
 * FXML Controller class
 *
 * @author jovan
 */
public class InvAdjustmentController implements Initializable {

    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private AnchorPane anchorField;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtDetail02;
    @FXML
    private TextField txtDetail03;
    @FXML
    private TextField txtDetail04;
    @FXML
    private TextField txtDetail80;
    @FXML
    private TextField txtDetail05;
    @FXML
    private TextField txtDetail06;
    @FXML
    private TextField txtDetail07;
    @FXML
    private TableView table;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private TextField txtField50;
    @FXML
    private Button btnNew;
    @FXML
    private Button btnSave;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnConfirm;
    @FXML
    private Button btnDel;
    @FXML
    private Button btnBrowse;
    @FXML
    private TableView tableDetail;
    @FXML
    private TextArea txtField04;
    @FXML
    private Button btnVoid;
    @FXML
    private AnchorPane dataPane;
    @FXML
    private Button btnUpdate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvAdjustment(poGRider, poGRider.getBranchCode(), false);
        poTrans.setCallBack(poCallBack);

        btnCancel.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnDel.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnConfirm.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnVoid.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);

        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtArea_Focus);
        txtField50.focusedProperty().addListener(txtField_Focus);

        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);
        txtDetail80.focusedProperty().addListener(txtDetail_Focus);

        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtFieldArea_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail06.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail07.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail80.setOnKeyPressed(this::txtDetail_KeyPressed);

        pnEditMode = EditMode.UNKNOWN;
        clearFields();

        initGrid();
        initLisView();
        initButton(pnEditMode);

        pbLoaded = true;

    }

    private void initGrid() {
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Brand");
        TableColumn index05 = new TableColumn("Expiry Date");
        TableColumn index06 = new TableColumn("QOH");
        TableColumn index07 = new TableColumn("CQty");
        TableColumn index08 = new TableColumn("DQty");

        index01.setPrefWidth(30);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(140);
        index04.setPrefWidth(150);
        index05.setPrefWidth(90);
        index05.setStyle("-fx-alignment: CENTER;");
        index06.setPrefWidth(65);
        index06.setStyle("-fx-alignment: CENTER-RIGHT;");
        index07.setPrefWidth(65);
        index07.setStyle("-fx-alignment: CENTER-RIGHT;");
        index08.setPrefWidth(65);
        index08.setStyle("-fx-alignment: CENTER-RIGHT;");

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
        index08.setSortable(false);
        index08.setResizable(false);

        table.getColumns().clear();
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        table.getColumns().add(index07);
        table.getColumns().add(index08);

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index08"));

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
        /*Set data source to table*/
        table.setItems(data);
    }

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
        tableDetail.setItems(getRecordData(pnRow));
        if (!pbFound) {
            addDetailData(pnlRow);
        }
        setDetailInfo(pnRow);
    }

    public void setGRider(GRider foGRider) {
        this.poGRider = foGRider;
    }
    private final String pxeModuleName = "Inventory Adjustment Controller";
    private static GRider poGRider;
    private InvAdjustment poTrans;
    private double pnCrdtTotl = 0;
    private double pnDbtTotl = 0;
    private double pnValTotl = 0;

    private boolean pbFound;
    private int pnlRow = 0;

    TableColumn index01 = new TableColumn("No.");
    TableColumn index02 = new TableColumn("Expiration");
    TableColumn index03 = new TableColumn("ActualQty");
    TableColumn index04 = new TableColumn("Quantity");

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

    private String psOldRec = "";
    private String psTransNox = "";
    private String psdTransact = "";

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
                case 2:
                    /*dTransact*/
                    if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)) {
                        poTrans.setMaster("dTransact", SQLUtil.toDate(txtField.getText(), pxeDateFormat));
                        txtField.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster("dTransact")));
                    } else {
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                        poTrans.setMaster("dTransact", CommonUtils.toDate(pxeDateDefault));
                        txtField.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster("dTransact")));
                    }
                    return;
                case 1:
                    /*sTransNox*/
                    break;
                case 50:
                    if (lsValue.equals("") || lsValue.equals("%")) {
                        txtField.setText("");
                    }
                    break;

//                case 51:
//                    if(CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
//                         txtField.setText(SQLUtil.dateFormat(SQLUtil.toDate(txtField.getText(), SQLUtil.FORMAT_LONG_DATE),pxeDateFormat));
//                    }else{
//                        txtField.setText(FoodInventoryFX.xsRequestFormat(CommonUtils.toDate(pxeDateDefault)));
//                    }
//                   break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                    return;
            }
            pnIndex = lnIndex;
        } else {
            switch (lnIndex) {
                case 2:
                    /*dTransact*/
                    txtField.setText(SQLUtil.dateFormat(poTrans.getMaster("dTransact"), pxeDateFormat));
                    txtField.selectAll();
                    break;
//                case 51:
//                    try{
//                        txtField.setText(FoodInventoryFX.xsRequestFormat(lsValue));
//                    }catch(ParseException e){
//                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
//                    }
//                    txtField.selectAll();
//                    break;
                default:
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        }
    };

    final ChangeListener<? super Boolean> txtDetail_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtDetail = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();

        if (pnRow < 0) {
            return;
        }
        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 7:
                    /*dExpiryDt*/
                    if (CommonUtils.isDate(txtDetail.getText(), pxeDateFormat)) {
                        poTrans.setDetail(pnRow, "dExpiryDt", SQLUtil.toDate(txtDetail.getText(), pxeDateFormat));
                        txtDetail.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
                    } else {
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                        poTrans.setDetail(pnRow, "dExpiryDt", CommonUtils.toDate(pxeDateDefault));
                        txtDetail.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
                    }
                    return;
                case 3:
                    /*Barcode Search*/
                    break;
                case 80:/*sDescript Search*/
                    break;
                case 4:/*Credit Qty*/
 /*This must be numeric*/
                    double x = 0;

                    try {
                        x = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        x = 0;
                    }
                    if (Double.valueOf(poTrans.getDetail(pnRow, "nDebitQty").toString()) > 0.00) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Debit Qty must be zero");
                        x = 0;
                        txtDetail.setText("0");
                    }
                    poTrans.setDetail(pnRow, "nCredtQty", x);
                    break;
                case 5:/*Debit Qty*/
 /*This must be numeric*/
                    double y = 0;

                    try {
                        y = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        y = 0;
                    }

                    if ((Double.valueOf(poTrans.getDetail(pnRow, "nCredtQty").toString())) > 0.00) {
                        ShowMessageFX.Warning(null, pxeModuleName, "Credit Qty must be zero");
                        y = 0;
                        txtDetail.setText("0");
                    }
                    poTrans.setDetail(pnRow, "nDebitQty", y);
                    break;
                case 6:/*Qty On Hand*/
                case 10:/*Inv Cost*/
                    break;

            }
            pnOldRow = table.getSelectionModel().getSelectedIndex();
            pnIndex = lnIndex;
        } else {
            switch (lnIndex) {
                case 7:
                    /*dExpiryDt*/
                    txtDetail.setText(SQLUtil.dateFormat(poTrans.getDetail(pnRow, "dExpiryDt"), pxeDateFormat));
                    txtDetail.selectAll();
                    break;
                default:
            }
            pnIndex = -1;
            txtDetail.selectAll();
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
                case 4:
                    /*sRemarksx*/
                    if (lsValue.length() > 256) {
                        lsValue = lsValue.substring(0, 512);
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

    private ObservableList getRecordData(int fnRow) {
        ObservableList dataDetail = FXCollections.observableArrayList();
        ResultSet loRS = null;
        loRS = poTrans.getExpiration((String) poTrans.getDetail(fnRow, "sStockIDx"));
        double lnQuantity = 0;
        pnlRow = 0;
        pbFound = false;

        try {
            dataDetail.clear();
            loRS.first();
            for (int rowCount = 0; rowCount <= MiscUtil.RecordCount(loRS) - 1; rowCount++) {
                if (FoodInventoryFX.xsRequestFormat(loRS.getDate("dExpiryDt")).equals(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(fnRow, "dExpiryDt")))) {
                    if (!pbFound) {
                        pbFound = true;
                    }
                    lnQuantity = loRS.getDouble("nQtyOnHnd") + Double.valueOf(poTrans.getDetail(fnRow, "nCredtQty").toString()) - Double.valueOf(poTrans.getDetail(fnRow, "nDebitQty").toString());
                } else {
                    lnQuantity = 0;
                }
                dataDetail.add(new TableModel(String.valueOf(rowCount + 1),
                        String.valueOf(FoodInventoryFX.xsRequestFormat(loRS.getDate("dExpiryDt"))),
                        String.valueOf(loRS.getDouble("nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                ));
                pnlRow++;
                loRS.next();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dataDetail;
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {
            case "btnNew":
                if (poTrans.newTransaction()) {
                    clearFields();
                    loadRecord();
                    txtField50.setText("");
                    pnEditMode = EditMode.ADDNEW;
                }
                break;

            case "btnConfirm":
                if (!psOldRec.equals("")) {
                    if (!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)) {
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to confirm this transaction?") == true) {
                        if (poTrans.closeTransaction(psOldRec)) {
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction CONFIRMED successfully.");
                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                        }
                    }

                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to confirm!");
                }
                break;
            case "btnVoid":
                if (!psOldRec.equals("")) {
                    if (!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)) {
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to void this transaction?") == true) {
                        if (poTrans.voidTransaction(psOldRec)) {
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction VOIDED successfully.");
                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                        }
                    }

                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to void!");
                }
                break;

            case "btnClose":
            case "btnExit":
                unloadForm();
                return;

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                    break;
                } else {
                    return;
                }

            case "btnSearch":
                switch (pnIndex) {
                    case 3:
                        if (poTrans.SearchDetail(pnRow, 3, "%", false, false)) {
                            txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                            txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                            txtDetail02.setText(poTrans.getDetailOthers(pnRow, "nQtyOnHnd").toString());
                        } else {
                            txtDetail03.setText("");
                            txtDetail80.setText("");
                            txtDetail02.setText("0.00");
                        }
                        break;
                    case 80:
                        if (poTrans.SearchDetail(pnRow, 4, "%", true, false)) {
                            txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                            txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                            txtDetail02.setText(poTrans.getDetailOthers(pnRow, "nQtyOnHnd").toString());
                        } else {
                            txtDetail03.setText("");
                            txtDetail80.setText("");
                            txtDetail02.setText("0.00");
                        }
                        break;
                }
                return;

            case "btnSave":
                if (!isEntryOk()) {
                    return;
                }
                if (poTrans.saveTransaction()) {
                    ShowMessageFX.Information(null, pxeModuleName, "Transaction saved successfuly.");
                    clearFields();
                    initGrid();
                    pnEditMode = EditMode.UNKNOWN;
                    initButton(pnEditMode);
                    break;
                } else {
                    if (!poTrans.getErrMsg().equals("")) {
                        ShowMessageFX.Error(poTrans.getErrMsg(), pxeModuleName, "Please inform MIS Department.");
                    } else {
                        ShowMessageFX.Warning(poTrans.getMessage(), pxeModuleName, "Please verify your entry.");
                    }
                    return;
                }
            case "btnBrowse":
                switch (pnIndex) {
                    case 50:
                        /*sTransNox*/
                        if (poTrans.BrowseRecord(txtField50.getText(), true) == true) {
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                            break;
                        } else if (!txtField50.getText().equals(psTransNox)) {
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                            break;
                        } else {
                            txtField50.setText(psTransNox);
                        }

                        return;
                    default:
                        ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                }

                return;
            case "btnDel":
                int lnIndex = table.getSelectionModel().getFocusedIndex();
                if (table.getSelectionModel().getSelectedItem() == null) {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select item to remove!");
                    break;
                }
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true) {
                    poTrans.deleteDetail(lnIndex);
                    loadDetail();

                }
                break;

            case "btnUpdate":
                if (!psOldRec.equals("")) {
                    if ("0".equals((String) poTrans.getMaster("cTranStat"))) {
                        if (poTrans.updateRecord()) {
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Unable to update transaction.");
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Unable to update transaction...");
                    }
                }
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        initButton(pnEditMode);
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) java.sql.Date.valueOf(LocalDate.now())));
        txtField04.setText("");
        txtField50.setText("");

        txtDetail03.setText("");
        txtDetail04.setText("");
        txtDetail05.setText("0.00");
        txtDetail02.setText("0");
        txtDetail06.setText("0.00");
        txtDetail07.setText(FoodInventoryFX.xsRequestFormat((Date) java.sql.Date.valueOf(LocalDate.now())));
        txtDetail80.setText("");

        pnRow = -1;
        pnOldRow = -1;
        pnIndex = 50;
        setTranStat("-1");
        psOldRec = "";
        psTransNox = "";
        psdTransact = "";
        data.clear();
        pnCrdtTotl = 0;
        pnDbtTotl = 0;
        pnValTotl = 0;
        pbFound = false;
        pnlRow = 0;
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnDel.setVisible(lbShow);

        txtField50.setDisable(lbShow);

        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnConfirm.setVisible(!lbShow);
        btnVoid.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);

//        txtField01.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);

        txtDetail03.setDisable(!lbShow);
        txtDetail04.setDisable(!lbShow);
        txtDetail05.setDisable(!lbShow);
        txtDetail06.setDisable(!lbShow);
        txtDetail07.setDisable(!lbShow);
        txtDetail80.setDisable(!lbShow);

        if (lbShow) {
            txtField02.requestFocus();
        } else {
            txtField50.requestFocus();
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
//        if (event.getCode() == ENTER || event.getCode() == F3){
        if (event.getCode() == F3) {
            switch (lnIndex) {
                case 50:
                    /*sTransNox*/
                    if (event.getCode() == F3) {
                        lsValue = txtField.getText() + "%";
                    }
                    if (poTrans.BrowseRecord(lsValue, true) == true) {
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                        break;
                    } else if (!txtField50.getText().equals(psTransNox)) {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    } else {
                        txtField50.setText(psTransNox);
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

    private void initLisView() {

        index01.setPrefWidth(30);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index02.setStyle("-fx-alignment: CENTER;");
        index03.setPrefWidth(65);
        index03.setStyle("-fx-alignment: CENTER;");
        index04.setPrefWidth(65);
        index04.setStyle("-fx-alignment: CENTER;");

        index01.setSortable(false);
        index01.setResizable(false);
        index02.setSortable(true);
        index02.setResizable(false);
        index03.setSortable(false);
        index03.setResizable(false);
        index04.setSortable(false);
        index04.setResizable(false);

        tableDetail.getColumns().clear();
        tableDetail.getColumns().add(index01);
        tableDetail.getColumns().add(index02);
        tableDetail.getColumns().add(index03);
        tableDetail.getColumns().add(index04);

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));

    }

    private void txtFieldArea_KeyPressed(KeyEvent event) {
//        if (event.getCode() == ENTER || event.getCode() == DOWN) {
        if (event.getCode() == DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    private void txtDetail_KeyPressed(KeyEvent event) {
        TextField txtDetail = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText() + "%";

        if (event.getCode() == F3) {
            switch (lnIndex) {
                case 3:
                    if (poTrans.SearchDetail(pnRow, 3, lsValue, false, false)) {
                        txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                        txtDetail06.setText(String.valueOf(poTrans.getDetail(pnRow, "nInvCostx")));
                        txtDetail02.setText(poTrans.getDetailOthers(pnRow, "nQtyOnHnd").toString());
                    } else {
                        txtDetail03.setText("");
                        txtDetail80.setText("");
                        txtDetail06.setText("0.00");
                        txtDetail02.setText("0.00");
                    }
                    break;

                case 80:
                    if (poTrans.SearchDetail(pnRow, 3, lsValue, true, false)) {
                        txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                        txtDetail06.setText(String.valueOf(poTrans.getDetail(pnRow, "nInvCostx")));
                        txtDetail02.setText(poTrans.getDetailOthers(pnRow, "nQtyOnHnd").toString());

                        pnRow = table.getSelectionModel().getSelectedIndex();
                        tableDetail.setItems(getRecordData(pnRow));
                        if (!pbFound) {
                            addDetailData(pnlRow);
                        }
                        setDetailInfo(pnRow);
                    } else {
                        txtDetail03.setText("");
                        txtDetail80.setText("");
                        txtDetail02.setText("0.00");
                        txtDetail06.setText("0.00");
                    }

                    break;

            }
        }

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtDetail);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtDetail);
        }
    }

    private void unloadForm() {
//        VBox myBox = (VBox) VBoxForm.getParent();
//        myBox.getChildren().clear();
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }

    private void loadRecord() {
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField50.setText((String) poTrans.getMaster("sTransNox"));
        psTransNox = txtField50.getText();
        txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster("dTransact")));
        try {

            psdTransact = FoodInventoryFX.xsRequestFormat(CommonUtils.toDate(poTrans.getMaster("dTransact").toString()));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        txtField04.setText((String) poTrans.getMaster("sRemarksx"));
        setTranStat((String) poTrans.getMaster("cTranStat"));

        pnRow = 0;
        pnOldRow = 0;
        loadDetail();

        psOldRec = txtField01.getText();
    }

    private void loadDetail() {
        int lnCtr;
        pnlRow = poTrans.ItemCount();

        pnValTotl = 0;
        pnCrdtTotl = 0;
        pnDbtTotl = 0;

        data.clear();
        /*ADD THE DETAIL*/
        for (lnCtr = 0; lnCtr <= pnlRow - 1; lnCtr++) {
            data.add(new TableModel(String.valueOf(lnCtr + 1),
                    (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"),
                    (String) poTrans.getDetailOthers(lnCtr, "sDescript"),
                    (String) poTrans.getDetail(lnCtr, "sBrandNme"),
                    FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(lnCtr, "dExpiryDt")),
                    String.valueOf(poTrans.getDetailOthers(lnCtr, "nQtyOnHnd")),
                    String.valueOf(poTrans.getDetail(lnCtr, "nCredtQty")),
                    String.valueOf(poTrans.getDetail(lnCtr, "nDebitQty")),
                    "",
                    ""));
            System.out.println("sBrandNme = " + poTrans.getDetailOthers(lnCtr, "sBrandNme"));

            pnValTotl = pnValTotl + Double.valueOf(poTrans.getDetailOthers(lnCtr, "nQtyOnHnd").toString());
            pnCrdtTotl = pnCrdtTotl + Double.valueOf(poTrans.getDetail(lnCtr, "nCredtQty").toString());
            pnDbtTotl = pnDbtTotl + Double.valueOf(poTrans.getDetail(lnCtr, "nDebitQty").toString());
        }

        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()) {
            table.getSelectionModel().select(pnlRow - 1);
            table.getFocusModel().focus(pnlRow - 1);
            pnRow = table.getSelectionModel().getSelectedIndex();
            setDetailInfo(pnRow);
        }
    }

    public boolean isEntryOk() {
        if (pnCrdtTotl != 0 && pnDbtTotl != 0) {
            if (pnCrdtTotl != pnDbtTotl) {
                ShowMessageFX.Warning(null, pxeModuleName, "Qty on credit and debit must be equal to QTY on hand!");
                return false;
            }
        }

        return true;
    }

    private void setDetailInfo(int fnRow) {
        if (!poTrans.getDetail(fnRow, "sStockIDx").equals("")) {
            txtDetail07.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(fnRow, "dExpiryDt")));
            txtDetail02.setText(String.valueOf(poTrans.getDetailOthers(fnRow, "nQtyOnHnd")));
            txtDetail03.setText((String) poTrans.getDetailOthers(fnRow, "sBarCodex"));
            txtDetail80.setText((String) poTrans.getDetailOthers(fnRow, "sDescript"));
            txtDetail04.setText(String.valueOf(poTrans.getDetail(fnRow, "nCredtQty")));
            txtDetail05.setText(String.valueOf(poTrans.getDetail(fnRow, "nDebitQty")));
            txtDetail06.setText(String.valueOf(poTrans.getDetail(fnRow, "nInvCostx")));
            txtDetail03.requestFocus();
        } else {
            txtDetail02.setText("0");
            txtDetail03.setText("");
            txtDetail04.setText("0");
            txtDetail05.setText("0");
            txtDetail06.setText("0.00");
            txtDetail07.setText(FoodInventoryFX.xsRequestFormat((Date) java.sql.Date.valueOf(LocalDate.now())));
            txtDetail80.setText("");
            txtDetail03.requestFocus();
        }
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

    IMasterDetail poCallBack = new IMasterDetail() {
        @Override
        public void MasterRetreive(int fnIndex) {
            getMaster(fnIndex);
        }

        @Override
        public void DetailRetreive(int fnIndex) {
            switch (fnIndex) {
                case 4:
                    txtDetail04.setText(String.valueOf(poTrans.getDetail(pnRow, "nCredtQty")));
                    if (!poTrans.getDetail(poTrans.ItemCount() - 1, "sStockIDx").toString().isEmpty()) {
                        if (Double.valueOf(poTrans.getDetail(pnRow, "nCredtQty").toString()) != 0.00) {
                            poTrans.addDetail();
                        }
                    }
                    loadDetail();

                    break;
                case 5:
                    txtDetail05.setText(String.valueOf(poTrans.getDetail(pnRow, "nDebitQty")));
                    if (!poTrans.getDetail(poTrans.ItemCount() - 1, "sStockIDx").toString().isEmpty()) {
                        if (Double.valueOf(poTrans.getDetail(pnRow, "nDebitQty").toString()) != 0.00) {
                            poTrans.addDetail();
                        }
                    }
                    loadDetail();

                    break;
                case 7:
                    /*get the value from the class*/
                    txtDetail07.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
                    loadDetail();
                    break;
            }
        }
    };

    private void addDetailData(int fnRow) {
        if (poTrans.getDetail(pnRow, "sStockIDx").equals("")) {
            return;
        }
        TableModel newData = new TableModel();
        newData.setIndex01(String.valueOf(fnRow + 1));
        newData.setIndex02(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
        newData.setIndex03("0");

        if (Double.valueOf(poTrans.getDetail(pnRow, "nCredtQty").toString()) > 0.00) {
            newData.setIndex04(String.valueOf(poTrans.getDetail(pnRow, "nCredtQty")));
        } else if (Double.valueOf(poTrans.getDetail(pnRow, "nDebitQty").toString()) > 0.00) {
            ShowMessageFX.Warning(null, pxeModuleName, "Cannot debit an empty inventory!");
            poTrans.setDetail(pnRow, "nDebitQty", 0.00);
            return;
        } else {
            newData.setIndex04("0");
        }
        newData.setIndex05("");
        newData.setIndex06("");
        newData.setIndex07("");
        newData.setIndex08("");
        newData.setIndex09("");
        newData.setIndex10("");
        tableDetail.getItems().add(newData);

        index02.setSortType(TableColumn.SortType.ASCENDING);
        tableDetail.getSortOrder().add(index02);
        tableDetail.sort();

    }

    private void getMaster(int fnIndex) {
        switch (fnIndex) {
            case 1:
                txtField01.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster("dTransact")));
                break;
            case 3:
                txtField02.setText(CommonUtils.TitleCase((String) poTrans.getMaster("sRemarksx")));
                break;
        }
    }

}
