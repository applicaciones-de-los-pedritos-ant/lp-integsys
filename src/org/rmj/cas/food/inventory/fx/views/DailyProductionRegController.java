package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
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
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.cas.inventory.production.base.DailyProduction;

public class DailyProductionRegController implements Initializable {

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
    private TextArea txtField03;
    @FXML
    private TextField txtDetail03;
    @FXML
    private TextField txtDetail80;
    @FXML
    private TextField txtDetail05, txtDetail08;
    @FXML
    private TextField txtDetail04;
    @FXML
    private TableView table;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private Button btnVoid;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnPrint;
    @FXML
    private Button btnBrowse;
    @FXML
    private TextField txtField50;
    @FXML
    private TextField txtField51;
    @FXML
    private AnchorPane dataPane;
    @FXML
    private TableView table1;
    @FXML
    private TextField txtDetail06;
    @FXML
    private TextField txtDetailOther01, txtDetailOther02,
            txtDetailOther03, txtDetailOther04,
            txtDetailOther05, txtDetailOther06;
    @FXML
    private Label lblHeader;
    @FXML
    private TableView tableData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new DailyProduction(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(12340);

        btnVoid.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);

        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtFieldArea_KeyPressed);

        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail06.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail80.setOnKeyPressed(this::txtDetail_KeyPressed);

        txtDetailOther01.setOnKeyPressed(this::txtDetailOther_KeyPressed);
        txtDetailOther02.setOnKeyPressed(this::txtDetailOther_KeyPressed);
        txtDetailOther03.setOnKeyPressed(this::txtDetailOther_KeyPressed);
        txtDetailOther04.setOnKeyPressed(this::txtDetailOther_KeyPressed);
        txtDetailOther05.setOnKeyPressed(this::txtDetailOther_KeyPressed);
        txtDetailOther06.setOnKeyPressed(this::txtDetailOther_KeyPressed);

        pnEditMode = EditMode.UNKNOWN;
        clearFields();

        initGrid();
        initRawData();
        initLisView();
        pbLoaded = true;
    }

    private void initGrid() {
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Measure");
        TableColumn index05 = new TableColumn("Goal Qty");
        TableColumn index06 = new TableColumn("Order Qty");
        TableColumn index07 = new TableColumn("Qty");

        index01.setPrefWidth(40);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(150);
        index04.setPrefWidth(85);
        index05.setPrefWidth(65);
        index05.setStyle("-fx-alignment: CENTER;");
        index06.setPrefWidth(65);
        index06.setStyle("-fx-alignment: CENTER;");
        index07.setPrefWidth(65);
        index07.setStyle("-fx-alignment: CENTER;");

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
        /*Set data source to table*/
        table.setItems(data);
    }

    private void initLisView() {
        index01.setPrefWidth(30);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(130);
        index02.setStyle("-fx-alignment: CENTER;");
        index03.setPrefWidth(90);
        index03.setStyle("-fx-alignment: CENTER;");
        index04.setPrefWidth(75);
        index04.setStyle("-fx-alignment: CENTER;");
        index05.setPrefWidth(75);
        index05.setStyle("-fx-alignment: CENTER;");

        index01.setSortable(false);
        index01.setResizable(false);
        index02.setSortable(true);
        index02.setResizable(false);
        index03.setSortable(false);
        index03.setResizable(false);
        index04.setSortable(false);
        index04.setResizable(false);
        index05.setSortable(false);
        index05.setResizable(false);

        tableData.getColumns().clear();
        tableData.getColumns().add(index01);
        tableData.getColumns().add(index02);
        tableData.getColumns().add(index03);
        tableData.getColumns().add(index04);
        tableData.getColumns().add(index05);

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index05"));
    }

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
        p_bTableFocus = true;
        p_bRawFocus = false;
        if (pnRow < 0) {
            return;
        }
        setDetailInfo(pnRow);
        txtDetail80.requestFocus();
        txtDetail80.selectAll();
    }

    public void setGRider(GRider foGRider) {
        this.poGRider = foGRider;
    }
    private final String pxeModuleName = "DailyProductionRegController";
    private static GRider poGRider;
    private DailyProduction poTrans;
    private int pnlRow = 0;
    private boolean pbFound;

    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    private boolean p_bTableFocus = false;
    private boolean p_bRawFocus = false;

    private final String pxeDateFormat = "MM/dd/yyyy";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();

    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private ObservableList<RawTable> rawData = FXCollections.observableArrayList();

    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;
    private int pnRawdata = -1;

    private String psOldRec = "";
    private String psTransNox = "";
    private String psdTransact = "";

    TableColumn index01 = new TableColumn("No.");
    TableColumn index02 = new TableColumn("Expiration");
    TableColumn index03 = new TableColumn("OnHand");
    TableColumn index04 = new TableColumn("Out");
    TableColumn index05 = new TableColumn("Rem");

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();

        switch (lsButton) {

            case "btnClose":
            case "btnExit":
                unloadForm();
                return;

            case "btnPrint":
                if (!psOldRec.equals("")) {
//                    ShowMessageFX.Information(null, pxeModuleName, "This feature is coming soon!.");
//
////                if(!psOldRec.equals("")){
////                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transaction?")==true){
//////                        if (poTrans.printTransaction(psOldRec))
////                        ShowMessageFX.Information(null, pxeModuleName, "Transaction printed successfully.");
////                    clearFields();
////                        initGrid();
////                    pnEditMode = EditMode.UNKNOWN;
////                    break;
////                    }else
////                    return;

                    if (poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)
                            || poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_CANCELLED)
                            || poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_VOID)) {
                        ShowMessageFX.Information(null, pxeModuleName, "Unable to Print OPEN/VOID/CANCELLED Transaction.");
                        return;
                    }
                    if (!poTrans.printTransfer()) {
                        ShowMessageFX.Information(null, pxeModuleName, "Unable to print Transaction");
                    }

                    return;
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
                }
                break;

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
                    case 51:
                        /*dTransact*/

                        if (CommonUtils.isDate(txtField51.getText(), pxeDateFormat)) {
                            String lsValue = SQLUtil.dateFormat(SQLUtil.toDate(txtField51.getText(), pxeDateFormat), "yyyy-MM-dd");
                            if (poTrans.BrowseRecord(lsValue, false) == true) {
                                loadRecord();
                                pnEditMode = poTrans.getEditMode();
                                break;
                            }
                        }
                        if (!txtField51.getText().equals(psdTransact)) {
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                            break;
                        } else {
                            txtField51.setText(psdTransact);
                        }

                        return;
                    default:
                        ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                        txtField51.requestFocus();
                }

                return;
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
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                        }
                    } else {
                        return;
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to void!");
                }
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField50.setText("");
        txtField51.setText(FoodInventoryFX.xsRequestFormat((Date) java.sql.Date.valueOf(LocalDate.now())));

        txtDetail03.setText("");
        txtDetail04.setText("0.0");
        txtDetail05.setText("0.0");
        txtDetail80.setText("");

        txtDetailOther01.setText("");
        txtDetailOther02.setText("");
        txtDetailOther03.setText("");
        txtDetailOther04.setText(FoodInventoryFX.xsRequestFormat((Date) java.sql.Date.valueOf(LocalDate.now())));
        txtDetailOther05.setText("0.0");
        txtDetailOther06.setText("0.0");

        pnRow = -1;
        pnRawdata = -1;
        pnOldRow = -1;
        pnIndex = 50;
        pnRawdata = -1;
        setTranStat("-1");
        pbFound = false;
        pnlRow = 0;
        psOldRec = "";
        psTransNox = "";
        psdTransact = "";
        p_bRawFocus = false;
        p_bTableFocus = false;
        data.clear();
        rawData.clear();
        tableData.setItems(loadEmptyData());
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
//        if (event.getCode() == ENTER || event.getCode() == F3) {
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

                case 51:
                    /*dTransact*/
                    if (CommonUtils.isDate(lsValue, pxeDateFormat)) {
                        String ldValue = SQLUtil.dateFormat(SQLUtil.toDate(lsValue, pxeDateFormat), "yyyy-MM-dd");
                        if (poTrans.BrowseRecord(ldValue, false) == true) {
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }
                    }
                    if (!txtField51.getText().equals(psdTransact)) {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    } else {
                        txtField51.setText(psdTransact);
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

    private void txtFieldArea_KeyPressed(KeyEvent event) {
        if (event.getCode() == ENTER || event.getCode() == DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == KeyCode.UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    private void txtDetail_KeyPressed(KeyEvent event) {
        TextField txtDetail = (TextField) event.getSource();

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtDetail);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtDetail);
        }
    }

    private void txtDetailOther_KeyPressed(KeyEvent event) {
        TextField txtDetailOther = (TextField) event.getSource();

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtDetailOther);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtDetailOther);
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
            txtField51.setText(FoodInventoryFX.xsRequestFormat(CommonUtils.toDate(poTrans.getMaster("dTransact").toString())));
            psdTransact = FoodInventoryFX.xsRequestFormat(CommonUtils.toDate(poTrans.getMaster("dTransact").toString()));
        } catch (NullPointerException e) {
            System.out.println(e);
        }

        txtField03.setText((String) poTrans.getMaster("sRemarksx"));
        setTranStat((String) poTrans.getMaster("cTranStat"));

        pnRow = 0;
        pnRawdata = 0;
        pnOldRow = 0;
        loadDetail();
        loadRawDetail();
        tableData.setItems(loadEmptyData());

        psOldRec = txtField01.getText();
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
                    (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"),
                    String.valueOf(poTrans.getDetail(lnCtr, "nGoalQtyx")),
                    String.valueOf(poTrans.getDetail(lnCtr, "nOrderQty")),
                    String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                    "",
                    "",
                    ""));
        }

        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()) {
            table.getSelectionModel().select(lnRow - 1);
            table.getFocusModel().focus(lnRow - 1);

            pnRow = table.getSelectionModel().getSelectedIndex();
            setDetailInfo(pnRow);
        }
    }

    /**
     * since 06-11-21 this method will load raw detail per detail
     */
    private void loadRawDetail() {
        int lnCtr;
        int lnRow = poTrans.InvCount();

        rawData.clear();
        /*ADD THE DETAIL*/
        for (lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++) {
            rawData.add(new RawTable(String.valueOf(lnCtr + 1),
                    (String) poTrans.getInvOthers(lnCtr, "sBarCodex"),
                    (String) poTrans.getInvOthers(lnCtr, "sDescript"),
                    (String) poTrans.getInvOthers(lnCtr, "sBrandNme"),
                    (String) poTrans.getInvOthers(lnCtr, "sMeasurNm"),
                    String.valueOf(poTrans.getInv(lnCtr, "nQtyReqrd")),
                    String.valueOf(poTrans.getInv(lnCtr, "nQtyUsedx"))
            ));
            System.out.println("nQtyUsedx = " + poTrans.getInv(lnCtr, "nQtyUsedx"));
        }

        /*FOCUS ON FIRST ROW*/
        if (!rawData.isEmpty()) {
            table1.getSelectionModel().select(lnRow - 1);
            table1.getFocusModel().focus(lnRow - 1);

            pnRawdata = table1.getSelectionModel().getSelectedIndex();
            setDetailOther(pnRawdata);
        }
    }

    private void setDetailInfo(int fnRow) {
        if (fnRow == -1) {
            return;
        }
        if (!poTrans.getDetail(fnRow, "sStockIDx").equals("")) {
            txtDetail03.setText((String) poTrans.getDetailOthers(pnRow, "sBarCodex"));
            txtDetail80.setText((String) poTrans.getDetailOthers(pnRow, "sDescript"));
            txtDetail06.setText(SQLUtil.dateFormat(poTrans.getDetail(pnRow, "dExpiryDt"), pxeDateFormat));
            txtDetail04.setText(String.valueOf(poTrans.getDetail(pnRow, "nQuantity")));
            txtDetail05.setText(String.valueOf(poTrans.getDetail(pnRow, "nGoalQtyx")));
            txtDetail08.setText(String.valueOf(poTrans.getDetail(pnRow, "nOrderQty")));
        } else {
            txtDetail03.setText("");
            txtDetail04.setText("0");
            txtDetail05.setText("0");
            txtDetail08.setText("0");
            txtDetail06.setText(FoodInventoryFX.xsRequestFormat((Date) java.sql.Date.valueOf(LocalDate.now())));
            txtDetail80.setText("");
        }
    }

    private void setDetailOther(int fnRow) {
        if (fnRow == -1) {
            return;
        }
        if (!poTrans.getInv(fnRow, "sStockIDx").equals("")) {
            txtDetailOther02.setText((String) poTrans.getInvOthers(pnRawdata, "sBarCodex"));
            txtDetailOther01.setText((String) poTrans.getInvOthers(pnRawdata, "sDescript"));
            txtDetailOther03.setText((String) poTrans.getInvOthers(pnRawdata, "sMeasurNm"));
            txtDetailOther04.setText(SQLUtil.dateFormat(poTrans.getInv(pnRawdata, "dExpiryDt"), pxeDateFormat));
            txtDetailOther05.setText(String.valueOf(poTrans.getInv(pnRawdata, "nQtyReqrd")));
            txtDetailOther06.setText(String.valueOf(poTrans.getInv(pnRawdata, "nQtyUsedx")));
        } else {
            txtDetailOther01.setText("");
            txtDetailOther02.setText("");
            txtDetailOther03.setText("");
            txtDetailOther04.setText(FoodInventoryFX.xsRequestFormat((Date) java.sql.Date.valueOf(LocalDate.now())));
            txtDetailOther05.setText("0.0");
            txtDetailOther06.setText("0.0");
        }
    }

    /**
     * for handling of editable table view and passing of parameters to setters.
     */
    //    /**
    private void initRawData() {
        TableColumn index01 = new TableColumn("No.");

        TableColumn<RawTable, String> index02 = new TableColumn<RawTable, String>("Barcode");
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable, String>("index02"));
//        index02.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<RawTable, String> index03 = new TableColumn<RawTable, String>("Description");
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable, String>("index03"));
//        index03.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<RawTable, String> index04 = new TableColumn<RawTable, String>("Brand");
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable, String>("index04"));
//        index04.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<RawTable, String> index05 = new TableColumn<RawTable, String>("Measure");
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable, String>("index05"));
//        index05.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<RawTable, String> index06 = new TableColumn<RawTable, String>("RcQty");
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable, String>("index06"));
//        index06.setCellFactory(TextFieldTableCell.forTableColumn());

        TableColumn<RawTable, String> index07 = new TableColumn<RawTable, String>("UsQty");
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable, String>("index07"));
//        index07.setCellFactory(TextFieldTableCell.forTableColumn());

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable, String>("index01"));

        index01.setPrefWidth(30);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(180);
        index04.setPrefWidth(150);
        index05.setPrefWidth(60);
        index05.setStyle("-fx-alignment: CENTER;");
        index06.setPrefWidth(40);
        index06.setStyle("-fx-alignment: CENTER;");
        index07.setPrefWidth(40);
        index07.setStyle("-fx-alignment: CENTER;");

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

        index01.setEditable(false);
        index02.setEditable(false);
        index03.setEditable(false);
        index04.setEditable(false);
        index05.setEditable(false);
        index06.setEditable(false);
        index07.setEditable(false);

        table1.getColumns().clear();
        table1.getColumns().add(index01);
        table1.getColumns().add(index02);
        table1.getColumns().add(index03);
        table1.getColumns().add(index04);
        table1.getColumns().add(index05);
        table1.getColumns().add(index06);
        table1.getColumns().add(index07);

        /*making column's position uninterchangebale*/
        table1.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) table1.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        table1.getColumns().clear();
        table1.getColumns().add(index01);
        table1.getColumns().add(index02);
        table1.getColumns().add(index03);
        table1.getColumns().add(index04);
        table1.getColumns().add(index05);
        table1.getColumns().add(index06);
        table1.getColumns().add(index07);

        table1.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        /*Set data source to table*/
        table1.setItems(rawData);
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
                case 6:
                    txtDetail06.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
                    break;
                case 4:
                    txtDetail04.setText(String.valueOf(poTrans.getDetail(pnRow, "nQuantity")));

                    loadDetail();
                    if (!poTrans.getDetail(poTrans.ItemCount() - 1, "sStockIDx").toString().isEmpty()
                            && Double.valueOf(String.valueOf(poTrans.getDetail(poTrans.ItemCount() - 1, fnIndex))) > 0.00) {
                        poTrans.addDetail();
                        pnRow = poTrans.ItemCount() - 1;
                    }

                    loadDetail();

                    if (!txtDetail03.getText().isEmpty()) {
                        txtDetail04.requestFocus();
                        txtDetail04.selectAll();
                    } else {
                        txtDetail03.requestFocus();
                        txtDetail03.selectAll();
                    }
            }
        }
    };

    private void getMaster(int fnIndex) {
        switch (fnIndex) {
            case 2:
                /*get the value from the class*/
                txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster("dTransact")));
                break;

        }
    }

    @FXML
    private void tableRaw_Clicked(MouseEvent event) {
        pnRawdata = table1.getSelectionModel().getSelectedIndex();
        p_bTableFocus = false;
        p_bRawFocus = true;
        if (pnRawdata < 0) {
            return;
        }
        setDetailOther(pnRawdata);

        tableData.setItems(getRecordData(pnRawdata));
        txtDetailOther04.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getInv(pnRawdata, "dExpiryDt")));
    }

    private void addDetailData(int fnRow) {
        if (poTrans.getInv(pnRawdata, "sStockIDx").equals("")) {
            return;
        }

        TableModel newData = new TableModel();
        newData.setIndex01(String.valueOf(fnRow + 1));
        newData.setIndex02(FoodInventoryFX.xsRequestFormat((Date) poTrans.getInv(pnRawdata, "dExpiryDt")));
        newData.setIndex03("0");
        newData.setIndex04(String.valueOf(poTrans.getInv(pnRawdata, "nQtyUsedx")));
        newData.setIndex05("");
        newData.setIndex06("");
        newData.setIndex07("");
        newData.setIndex08("");
        newData.setIndex09("");
        newData.setIndex10("");
        tableData.getItems().add(newData);

        index02.setSortType(TableColumn.SortType.ASCENDING);
        tableData.getSortOrder().add(index02);
        tableData.sort();
    }

    @FXML
    private void tableData_Clicked(MouseEvent event) {

    }

    private ObservableList getRecordData(int fnRow) {
        ObservableList dataDetail = FXCollections.observableArrayList();
        ResultSet loRS = null;

        loRS = getExpiration((String) poTrans.getInv(fnRow, "sStockIDx"));
        double lnQuantity = 0;
        pnlRow = 0;
        pbFound = false;

        try {
            dataDetail.clear();
            loRS.first();
            for (int rowCount = 0; rowCount <= MiscUtil.RecordCount(loRS) - 1; rowCount++) {
                if (FoodInventoryFX.xsRequestFormat(loRS.getDate("dExpiryDt")).equals(FoodInventoryFX.xsRequestFormat((Date) poTrans.getInv(fnRow, "dExpiryDt")))) {
                    if (!pbFound) {
                        pbFound = true;
                    }
                    lnQuantity = Double.valueOf(poTrans.getInv(fnRow, "nQtyUsedx").toString());
                } else {
                    lnQuantity = 0;
                }
                dataDetail.add(new TableModel(String.valueOf(rowCount + 1),
                        String.valueOf(FoodInventoryFX.xsRequestFormat(loRS.getDate("dExpiryDt"))),
                        String.valueOf(loRS.getDouble("nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        String.valueOf((double) loRS.getDouble("nQtyOnHnd") - (double) lnQuantity),
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

    /**
     * jovan since 06-24-2021
     *
     * @return observablelist
     */
    private ObservableList loadEmptyData() {
        ObservableList dataDetail = FXCollections.observableArrayList();
        dataDetail.clear();
        dataDetail.add(new TableModel(String.valueOf(1),
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                "",
                ""
        ));
        return dataDetail;
    }

    /**
     * to handle Inventory Master Expiration
     *
     * @param fsStockIDx
     * @return resultset
     */
    public ResultSet getExpiration(String fsStockIDx) {
        String lsSQL = "SELECT * FROM Inv_Master_Expiration"
                + " WHERE sStockIDx = " + SQLUtil.toSQL(fsStockIDx)
                + " AND sBranchCd = " + SQLUtil.toSQL(poGRider.getBranchCode())
                + " AND nQtyOnHnd > 0"
                + " ORDER BY dExpiryDt";

        ResultSet loRS = poGRider.executeQuery(lsSQL);

        return loRS;
    }
}
