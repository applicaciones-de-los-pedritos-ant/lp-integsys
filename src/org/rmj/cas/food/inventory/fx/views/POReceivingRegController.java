package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import org.json.simple.JSONObject;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.constants.UserRight;
import org.rmj.cas.inventory.base.Inventory;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMDepartment;
import org.rmj.lp.parameter.agent.XMInventoryType;
import org.rmj.lp.parameter.agent.XMTerm;
import org.rmj.purchasing.agent.POReceiving;

public class POReceivingRegController implements Initializable {

    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private AnchorPane anchorField;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField17;
    @FXML
    private TextField txtField20;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField08;
    @FXML
    private TextArea txtField16;
    @FXML
    private Label Label09;
    @FXML
    private TextField txtDetail03;
    @FXML
    private TextField txtDetail04;
    @FXML
    private TextField txtDetail80;
    @FXML
    private TextField txtDetail08;
    @FXML
    private TextField txtDetail09;
    @FXML
    private TextField txtDetail07;

    @FXML
    private TextField txtDetail11;
    @FXML
    private TableView table;
    @FXML
    private TextField txtField10;
    @FXML
    private TextField txtField11;
    @FXML
    private TextField txtField15;
    @FXML
    private TextField txtField12;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private TextField txtField01;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnVoid;
    @FXML
    private Button btnPrint;
    @FXML
    private TextField txtField50;
    @FXML
    private TextField txtField51;
    @FXML
    private TextField txtDetail10;
    @FXML
    private AnchorPane dataPane;

    @FXML
    private Label lblHeader;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Initialize class*/
        poTrans = new POReceiving(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(1230);
        poTrans.setClientNm(System.getProperty("user.name"));

        if (poGRider.getUserLevel() < UserRight.SUPERVISOR) {
            pbisEncoder = true;
        }

        /*Set action event handler for the buttons*/
        btnVoid.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);

        /*Add keypress event for field with search*/
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        txtField14.setOnKeyPressed(this::txtField_KeyPressed);
        txtField15.setOnKeyPressed(this::txtField_KeyPressed);
        txtField17.setOnKeyPressed(this::txtField_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        txtField16.setOnKeyPressed(this::txtFieldArea_KeyPressed);

        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail07.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail08.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail09.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail10.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail80.setOnKeyPressed(this::txtDetail_KeyPressed);

        pnEditMode = EditMode.UNKNOWN;

        clearFields();
        initGrid();

        pbLoaded = true;
    }

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();

        setDetailInfo();
        txtDetail03.requestFocus();
        txtDetail03.selectAll();
    }

    private void setDetailInfo() {
        if (pnRow >= 0) {
            String lsStockIDx = (String) poTrans.getDetail(pnRow, "sStockIDx");
            txtDetail03.setText((String) poTrans.getDetail(pnRow, 3));

            Inventory loInventory;

            if (!lsStockIDx.equals("")) {
                loInventory = poTrans.GetInventory(lsStockIDx, true, false);
                psBarCodex = (String) loInventory.getMaster("sBarCodex");
                psDescript = (String) loInventory.getMaster("sDescript");
            } else {
                psBarCodex = (String) poTrans.getDetail(pnRow, 100);
                psDescript = (String) poTrans.getDetail(pnRow, 101);
            }

            txtDetail04.setText(psBarCodex);
            txtDetail80.setText(psDescript);
            txtDetail07.setText(String.valueOf(poTrans.getDetail(pnRow, "nQuantity")));

            txtDetail11.setText(String.valueOf(poTrans.getDetail(pnRow, "nOrdrQtyx")));
            if (!pbisEncoder) {
                txtDetail08.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, 8).toString()), "0.00"));
            } else {
                txtDetail08.setText("0.00");
            }

            txtDetail09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, 9).toString()), "0.00"));
            txtDetail10.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
        } else {
            txtDetail03.setText("");
            txtDetail04.setText("");
            txtDetail07.setText("0");
            txtDetail08.setText("0.00");
            txtDetail09.setText("0.00");
            txtDetail80.setText("");
            txtDetail10.setText("");
        }
    }

    private void initGrid() {
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Order No.");
        TableColumn index03 = new TableColumn("Bar Code");
        TableColumn index04 = new TableColumn("Description");
        TableColumn index05 = new TableColumn("Brand");
        TableColumn index06 = new TableColumn("Unit");
        TableColumn index07 = new TableColumn("Superseded");
        TableColumn index08 = new TableColumn("Unit Type");
        TableColumn index09 = new TableColumn("Unit Price");
        TableColumn index10 = new TableColumn("Order Qty");
        TableColumn index11 = new TableColumn("Receive Qty");
//        TableColumn index11 = new TableColumn("Freight");
        TableColumn index12 = new TableColumn("Total");

        index01.setPrefWidth(28);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(90);
        index04.setPrefWidth(115);
        index05.setPrefWidth(123);
        index06.setPrefWidth(65);
        index06.setStyle("-fx-alignment: CENTER;");
//        index07.setPrefWidth(75); index07.setStyle("-fx-alignment: CENTER-RIGHT;");
        index08.setPrefWidth(68);
        index08.setStyle("-fx-alignment: CENTER;");
        index09.setPrefWidth(60);
        index09.setStyle("-fx-alignment: CENTER-RIGHT;");
        index10.setPrefWidth(85);
        index10.setStyle("-fx-alignment: CENTER-RIGHT;");
        index11.setPrefWidth(85);
        index11.setStyle("-fx-alignment: CENTER-RIGHT;");
        index12.setPrefWidth(85);
        index12.setStyle("-fx-alignment: CENTER-RIGHT;");

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
//        index07.setSortable(false); index07.setResizable(false);
        index08.setSortable(false);
        index08.setResizable(false);
        index09.setSortable(false);
        index09.setResizable(false);
        index10.setSortable(false);
        index10.setResizable(false);
        index11.setSortable(false);
        index11.setResizable(false);
        index12.setSortable(false);
        index12.setResizable(false);

        table.getColumns().clear();
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
//        table.getColumns().add(index07);
        table.getColumns().add(index08);
        table.getColumns().add(index09);
        table.getColumns().add(index10);
        table.getColumns().add(index11);
        table.getColumns().add(index12);

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index06"));
//        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index08"));
        index09.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index09"));

        if (!pbisEncoder) {
            index10.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index10"));
            index12.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index12"));
        } else {
            index10.setCellValueFactory(cellData -> new SimpleStringProperty("0.00"));
            index12.setCellValueFactory(cellData -> new SimpleStringProperty("0.00"));
        }

        index11.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index11"));


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

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {

            case "btnClose":
            case "btnExit":
                unloadForm();
                return;
            case "btnPrint":
                ShowMessageFX.Information(null, pxeModuleName, "This feature is unavailable");
//                if(!psOldRec.equals("")){
//                    if (poTrans.printRecord()){
//                        clearFields();
//                        initGrid();
//                        pnEditMode = EditMode.UNKNOWN;
//                    }
//                }else 
//                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");

                break;
            case "btnBrowse":
                if (poTrans.BrowseRecord(txtField50.getText(), true) == true) {
                    loadRecord();
                    pnEditMode = poTrans.getEditMode();
                    break;
                } else {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }

                return;
            case "btnVoid":
                if (!psOldRec.equals("")) {
                    if (!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)) {
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to cancel this transaction?") == true) {
                        if (poTrans.cancelTransaction(psOldRec)) {
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction CANCELLED successfully.");
                        }
                        clearFields();
                        initGrid();
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    } else {
                        return;
                    }

                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to cancel!");
                }
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }

    private void loadRecord() {
        txtField01.setText((String) poTrans.getMaster(1));

        txtField03.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster("dTransact")));
        txtField06.setText((String) poTrans.getMaster(6));
        txtField50.setText((String) poTrans.getMaster(6));
        psReferNox = txtField50.getText();
        txtField07.setText(FoodInventoryFX.xsRequestFormat((Date) (poTrans.getMaster("dRefernce") != null ? poTrans.getMaster("dRefernce") : CommonUtils.toDate(pxeDateDefault))));
        txtField10.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(10).toString()), "0.00"));
        txtField11.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(11).toString()), "#,##0.00"));
        txtField12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(12).toString()), "0.00"));
        txtField13.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(13).toString()), "#,##0.00"));
        txtField14.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(14).toString()), "#,##0.00"));
        txtField15.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(15).toString()), "#,##0.00"));
        txtField16.setText((String) poTrans.getMaster(16));

        JSONObject loSupplier = poTrans.GetSupplier((String) poTrans.getMaster(5), true);
        if (loSupplier != null) {
            txtField05.setText((String) loSupplier.get("sClientNm"));
            txtField51.setText((String) loSupplier.get("sClientNm"));
        }

        XMTerm loTerm = poTrans.GetTerm((String) poTrans.getMaster(8), true);
        if (loTerm != null) {
            txtField08.setText((String) loTerm.getMaster("sDescript"));
        }

        setTranStat((String) poTrans.getMaster("cTranStat"));

        pnRow = 0;
        pnOldRow = 0;
        loadDetail();

        psOldRec = txtField01.getText();
    }

    private void clearFields() {
        pbisEncoder = false;
        txtField01.setText("");
        txtField03.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField10.setText("0.00");
        txtField11.setText("0.00");
        txtField12.setText("0.00");
        txtField13.setText("0.00");
        txtField14.setText("0.00");
        txtField15.setText("0.00");
        txtField16.setText("");
        txtField17.setText("");
        txtField50.setText("");
        txtField51.setText("");

        txtDetail03.setText("");
        txtDetail04.setText("");
        txtDetail07.setText("0");
        txtDetail08.setText("0.00");
        txtDetail09.setText("0.00");
        txtDetail80.setText("");

        Label09.setText("0.00");

        pnRow = 51;
        pnOldRow = -1;
        pnIndex = -1;
        setTranStat("-1");

        psOldRec = "";
        psReferNox = "";

        psOrderNox = "";
        psMeasurNm = "";

        data.clear();
        if (poGRider.getUserLevel() < UserRight.SUPERVISOR) {
            pbisEncoder = true;
        }
    }

    private void unloadForm() {
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (event.getCode() == F3) {
            switch (lnIndex) {

                case 50:
                    /*ReferNox*/
                    if (poTrans.BrowseRecord(lsValue, true) == true) {
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    }

                    return;
                case 51:
                    /*Supplier*/
                    if (poTrans.BrowseRecord(lsValue, false) == true) {
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    }
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

    private void ComboBox_KeyPressed(KeyEvent event) {
        ComboBox cmbBox = (ComboBox) event.getSource();

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(cmbBox);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(cmbBox);
        }
    }

    private void txtFieldArea_KeyPressed(KeyEvent event) {
        if (event.getCode() == DOWN) {
            event.consume();
            CommonUtils.SetNextFocus((TextArea) event.getSource());
        } else if (event.getCode() == UP) {
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea) event.getSource());
        }
    }

    private void txtDetail_KeyPressed(KeyEvent event) {
        TextField txtDetail = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();

        if (lsValue == null) {
            lsValue = "";
        }

        JSONObject loJSON;

        if (event.getCode() == F3) {
            switch (lnIndex) {
                case 3:
                    if (event.getCode() == F3) {
                        if (poTrans.SearchDetail(pnRow, 3, lsValue, false, true));

                    }
                    break;
                case 4:
                    if (poTrans.SearchDetail(pnRow, 4, lsValue, false, false));
                    loadDetail();

                    break;
                case 80:
                    if (poTrans.SearchDetail(pnRow, 4, lsValue, true, false));
                    loadDetail();

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

    private void loadDetail() {
        int lnCtr;
        int lnRow = poTrans.ItemCount();

        data.clear();
        /*ADD THE DETAIL*/

        Inventory loInventory;
        String lsOldCode = "";
        for (lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++) {

            if (!"".equals((String) poTrans.getDetail(lnCtr, "sStockIDx"))) {
                loInventory = poTrans.GetInventory((String) poTrans.getDetail(lnCtr, "sStockIDx"), true, false);
                psBarCodex = (String) loInventory.getMaster("sBarCodex");
                psDescript = (String) loInventory.getMaster("sDescript");
                psMeasurNm = loInventory.getMeasureMent((String) loInventory.getMaster("sMeasurID"));

                if (!"".equals((String) poTrans.getDetail(lnCtr, "sReplacID"))) {
                    loInventory = poTrans.GetInventory((String) poTrans.getDetail(lnCtr, "sReplacID"), true, false);
                    lsOldCode = (String) loInventory.getMaster("sBarCodex");
                }

                data.add(new TableModel(String.valueOf(lnCtr + 1),
                        (String) poTrans.getDetail(lnCtr, "sOrderNox"),
                        psBarCodex,
                        psDescript,
                        (String) poTrans.getDetail(lnCtr, "sBrandNme"),
                        psMeasurNm,
                        lsOldCode,
                        cUnitType.get(Integer.parseInt((String) poTrans.getDetail(lnCtr, "cUnitType"))),
                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString()), "#,##0.00"),
                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nOrdrQtyx").toString()), "#,##0.00"),
                        String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                        //                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nFreightx").toString()), "#,##0.00"),
                        CommonUtils.NumberFormat(((Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()))
                                * Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString()))
                                + Double.valueOf(poTrans.getDetail(lnCtr, "nFreightx").toString()), "#,##0.00")));
            } else {
                data.add(new TableModel(String.valueOf(lnCtr + 1),
                        "",
                        (String) poTrans.getDetail(lnCtr, 100),
                        (String) poTrans.getDetail(lnCtr, 101),
                        (String) poTrans.getDetail(lnCtr, "sBrandNme"),
                        (String) poTrans.getDetail(lnCtr, 102),
                        "",
                        cUnitType.get(Integer.parseInt((String) poTrans.getDetail(lnCtr, "cUnitType"))),
                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString()), "#,##0.00"),
                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nOrdrQtyx").toString()), "#,##0.00"),
                        String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                        //                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nFreightx").toString()), "#,##0.00"),
                        CommonUtils.NumberFormat(((Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()))
                                * Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString()))
                                + Double.valueOf(poTrans.getDetail(lnCtr, "nFreightx").toString()), "#,##0.00")));
            }
        }

        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()) {
            table.getSelectionModel().select(pnRow);
            table.getFocusModel().focus(pnRow);
            pnRow = table.getSelectionModel().getSelectedIndex();

            setDetailInfo();
        }
        if (!pbisEncoder) {
            Label09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()) + Double.valueOf(poTrans.getMaster(11).toString()), "#,##0.00"));
        } else {
            Label09.setText("0.00");
        }
        txtField11.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(11).toString()), "#,##0.00"));
    }

    public void setGRider(GRider foGRider) {
        this.poGRider = foGRider;
    }

    private final String pxeModuleName = "POReceivingRegController";
    private static GRider poGRider;
    private POReceiving poTrans;

    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    private boolean pbisEncoder = false;

    private final String pxeDateFormat = "MM/dd/yyyy";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();

    private TableModel model;
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    ObservableList<String> cUnitType = FXCollections.observableArrayList("Demo", "Regular", "Repo");
    ObservableList<String> cDivision = FXCollections.observableArrayList("Motorcycle", "Mobile Phone", "Hotel", "General");

    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;

    private String psOldRec = "";
    private String psReferNox = "";

    private String psOrderNox = "";

    private String psBarCodex;
    private String psDescript;
    private String psMeasurNm;

    IMasterDetail poCallBack = new IMasterDetail() {
        @Override
        public void MasterRetreive(int fnIndex) {
            getMaster(fnIndex);
        }

        @Override
        public void DetailRetreive(int fnIndex) {
            switch (fnIndex) {
                case 7:
                    txtDetail07.setText(String.valueOf(poTrans.getDetail(pnRow, 7)));
                    loadDetail();

                    poTrans.addDetail(); //pass psOrderNox here

                    pnRow = poTrans.ItemCount() - 1;

                    loadDetail();
                    if (txtDetail04.getText().isEmpty()) {
                        txtDetail04.requestFocus();
                        txtDetail04.selectAll();
                    } else {
                        txtDetail03.requestFocus();
                        txtDetail03.selectAll();
                    }
                    break;
                case 8:
                    txtDetail08.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, fnIndex).toString()), "0.00"));
                    loadDetail();
                    break;
                case 9:
                    txtDetail09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, fnIndex).toString()), "0.00"));
                    loadDetail();
                    break;
                case 10:
                    txtDetail10.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getDetail(pnRow, fnIndex)));
                    loadDetail();
                    break;
            }
        }
    };

    private void getMaster(int fnIndex) {
        switch (fnIndex) {
            case 5:
                JSONObject loSupplier = poTrans.GetSupplier((String) poTrans.getMaster(fnIndex), true);
                if (loSupplier != null) {
                    txtField05.setText((String) loSupplier.get("sClientNm"));
                }
                break;
            case 6:
                txtField06.setText((String) poTrans.getMaster(fnIndex));
                break;
            case 8:
                XMTerm loTerm = poTrans.GetTerm((String) poTrans.getMaster(fnIndex), true);
                if (loTerm != null) {
                    txtField08.setText((String) loTerm.getMaster("sDescript"));
                }
                break;
            case 3:
                txtField03.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster(fnIndex)));
                break;
            case 7:
                txtField07.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster(fnIndex)));
                break;
            case 12:
                txtField12.setText(CommonUtils.NumberFormat((Double) poTrans.getMaster(fnIndex), "0.00"));
                loadDetail();
                break;
            case 13:
                txtField13.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(fnIndex).toString()), "0.00"));
                loadDetail();
                break;
            case 14:
                txtField14.setText(CommonUtils.NumberFormat((Double) poTrans.getMaster(fnIndex), "0.00"));
                loadDetail();
                break;
            case 15:
                txtField15.setText(CommonUtils.NumberFormat((Double) poTrans.getMaster(fnIndex), "0.00"));
                loadDetail();
                break;
            case 17:
                txtField17.setText((String) poTrans.getMaster(fnIndex));
                break;
            case 9:
                Label09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()), "#,##0.00"));
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
}
