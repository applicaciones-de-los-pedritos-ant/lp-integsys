package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.cas.food.inventory.fx.views.child.FoodLedgerController;
import org.rmj.cas.inventory.base.InvMaster;

public class InvMasterController implements Initializable {

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
    private TextField txtField03;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField06;
    @FXML
    private TextField txtField07;
    @FXML
    private TextField txtField08;
    @FXML
    private TextField txtField09;
    @FXML
    private TextField txtField13;
    @FXML
    private TextField txtField14;
    @FXML
    private TextField txtField16;
    @FXML
    private TextField txtField17;
    @FXML
    private TextField txtField18;
    @FXML
    private TextField txtField19;
    @FXML
    private ComboBox Combo23;
    @FXML
    private ComboBox Combo24;
    @FXML
    private CheckBox Check22;
    @FXML
    private CheckBox Check26;
    @FXML
    private CheckBox Check27;
    @FXML
    private Button btnUpdate;
    @FXML
    private Button btnCancel;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnSearch;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnSave;
    @FXML
    private TextField txtOther03;
    @FXML
    private TextField txtOther04;
    @FXML
    private TextField txtOther05;
    @FXML
    private TextField txtOther07;
    @FXML
    private TextField txtOther06;
    @FXML
    private TextField txtOther08;
    @FXML
    private TextField txtOther09;
    @FXML
    private TextField txtOther11;
    @FXML
    private TextField txtOther10;
    @FXML
    private TextField txtOther12;
    @FXML
    private TextField txtOther13;
    @FXML
    private TextField txtOther15;
    @FXML
    private TextField txtOther14;
    @FXML
    private TextField txtOther16;
    @FXML
    private TextField txtField50;
    @FXML
    private TextField txtField51;
    @FXML
    private Button btnList;
    @FXML
    private Label lblHeader;
    @FXML
    private TextField txtField29;
    @FXML
    private TableView table;
    @FXML
    private TextField txtField10;
    @FXML
    private AnchorPane dataPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        poRecord = new InvMaster(poGRider, poGRider.getBranchCode(), false);

        /*Set action event handler for the buttons*/
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnList.setOnAction(this::cmdButton_Click);

        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtOther03.focusedProperty().addListener(txtOther_Focus);
        txtOther04.focusedProperty().addListener(txtOther_Focus);
        txtOther05.focusedProperty().addListener(txtOther_Focus);
        txtOther06.focusedProperty().addListener(txtOther_Focus);
        txtOther07.focusedProperty().addListener(txtOther_Focus);

        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        txtOther03.setOnKeyPressed(this::txtOther_KeyPressed);
        txtOther04.setOnKeyPressed(this::txtOther_KeyPressed);
        txtOther05.setOnKeyPressed(this::txtOther_KeyPressed);
        txtOther06.setOnKeyPressed(this::txtOther_KeyPressed);
        txtOther07.setOnKeyPressed(this::txtOther_KeyPressed);

        Combo23.setOnKeyPressed(this::ComboBox_KeyPressed);
        Combo24.setOnKeyPressed(this::ComboBox_KeyPressed);

        Check22.setOnKeyPressed(this::Check_KeyPressed);
        Check26.setOnKeyPressed(this::Check_KeyPressed);
        Check27.setOnKeyPressed(this::Check_KeyPressed);

        Combo23.setItems(cUnitType);
        Combo23.getSelectionModel().select(1);

        Combo24.setItems(cInvStatx);
        Combo24.getSelectionModel().select(1);

        pnEditMode = EditMode.READY;
        txtOther07.setDisable(true);

        clearFields();
        initGrid();
        initButton(pnEditMode);

        pbLoaded = true;
    }

    public void initGrid() {
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Expiry");
        TableColumn index03 = new TableColumn("QOH");

        index01.setPrefWidth(50);
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(200);
        index02.setStyle("-fx-alignment: CENTER;");
        index03.setPrefWidth(100);
        index03.setStyle("-fx-alignment: CENTER;");

        index01.setSortable(false);
        index01.setResizable(false);
        index02.setSortable(true);
        index02.setResizable(false);
        index03.setSortable(false);
        index03.setResizable(false);

        table.getColumns().clear();
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));

        table.setItems(data);
    }

    private void loadDetailData(String fsStockIDx) {
        ResultSet loRS = null;
        String lsSQL = "SELECT * FROM Inv_Master_Expiration"
                + " WHERE sStockIDx = " + SQLUtil.toSQL(fsStockIDx)
                + " AND sBranchCd = " + SQLUtil.toSQL(poGRider.getBranchCode())
                + " ORDER BY dExpiryDt";

        loRS = poGRider.executeQuery(lsSQL);
        int rowCount = 0;
        if (MiscUtil.RecordCount(loRS) == 0) {
            data.clear();
            initGrid();
            data.add(new TableModel(String.valueOf(rowCount + 1),
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
            return;
        }
        try {
            data.clear();
            while (loRS.next()) {
                data.add(new TableModel(String.valueOf(rowCount + 1),
                        String.valueOf(FoodInventoryFX.xsRequestFormat(loRS.getDate("dExpiryDt"))),
                        String.valueOf(loRS.getDouble("nQtyOnHnd")),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                ));
                rowCount++;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            Logger.getLogger(InvTransferController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (event.getCode() == F3) {
            switch (lnIndex) {
                case 50:
                    if (event.getCode() == F3) {
                        lsValue = txtField.getText() + "%";
                    }
                    if (poRecord.SearchInventory(lsValue, false, false) == true) {
                        loadRecord();
                        initButton(poRecord.getEditMode());

                    } else if (!txtField50.getText().equals(psBarcode)) {
                        clearFields();
                        break;
                    } else {
                        txtField50.setText(psBarcode);
                    }
                    return;

                case 51:
                    if (event.getCode() == F3) {
                        lsValue = txtField.getText() + "%";
                    }
                    if (poRecord.SearchInventory(lsValue, true, false) == true) {
                        loadRecord();
                        initButton(poRecord.getEditMode());

                    }
                    if (!txtField51.getText().equals(psDescript)) {
                        clearFields();
                        break;
                    } else {
                        txtField51.setText(psDescript);
                    }
                    return;

                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with index " + lnIndex + " not registered for QuickSearch.");
                    return;
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
    }

    private void txtOther_KeyPressed(KeyEvent event) {
        TextField txtOther = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        String lsValue = txtOther.getText();
        if (event.getCode() == F3) {
            switch (lnIndex) {
                case 3:
                    if (event.getCode() == F3) {
                        lsValue = txtOther.getText() + "%";
                    }
                    txtOther.setText(poRecord.SearchMaster(lnIndex, lsValue, false));
                    psLocation = txtOther.getText();
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    break;

                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with index " + lnIndex + " not registered for QuickSearch.");
                    return;
            }

            switch (event.getCode()) {
                case ENTER:
                case DOWN:
                    CommonUtils.SetNextFocus(txtOther);
                    break;
                case UP:
                    CommonUtils.SetPreviousFocus(txtOther);
            }
        }
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        String lsValue;

        switch (lsButton) {
            case "btnBrowse":
                switch (pnIndex) {
                    case 50:
                        /*sBarcode*/
                        lsValue = txtField50.getText() + "%";

                        if (poRecord.SearchInventory(lsValue, false, false) == true) {
                            loadRecord();
                            initButton(poRecord.getEditMode());
                        } else if (!txtField50.getText().equals(psBarcode)) {
                            clearFields();
                            break;
                        } else {
                            txtField50.setText(psBarcode);
                        }
                        return;

                    case 51:
                        /*sDescript*/
                        lsValue = txtField51.getText() + "%";

                        if (poRecord.SearchInventory(lsValue, true, false) == true) {
                            loadRecord();
                            initButton(poRecord.getEditMode());
                        }
                        if (!txtField51.getText().equals(psDescript)) {
                            clearFields();
                            break;
                        } else {
                            txtField51.setText(psDescript);
                        }
                        return;

                    default:
                        ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                        txtField51.requestFocus();
                }
                return;

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                    break;
                } else {
                    return;
                }

            case "btnClose":
            case "btnExit":
                unloadForm();
                return;

            case "btnSave":
                if (poRecord.SaveRecord()) {
                    if (poRecord.SearchInventory(psOldRec, true, true)) {
                        loadRecord();
                    }
                    ShowMessageFX.Information(null, pxeModuleName, "Record Save Successfully.");
                    break;
                } else {
                    if (!poRecord.getErrMsg().equals("")) {
                        ShowMessageFX.Error(poRecord.getErrMsg(), pxeModuleName, "Please inform MIS Department.");
                    } else {
                        ShowMessageFX.Warning(poRecord.getMessage(), pxeModuleName, "Please verify your entry.");
                    }
                    return;
                }

            case "btnSearch":
                switch (pnIndex) {
                    case 3:
                        txtOther03.setText(poRecord.SearchMaster(pnIndex, "%", false));
                        psLocation = txtOther03.getText();
                        break;
                }
                return;

            case "btnUpdate":
                if (poRecord.getInventory(1) != null && !txtField01.getText().equals("")) {
                    txtOther05.setEditable(false);
                    txtOther06.setEditable(false);
                    txtField50.setText("");
                    txtField51.setText("");
                    if (poRecord.UpdateRecord()) {
                        pnEditMode = poRecord.getEditMode();
                    }
                } else {
                    ShowMessageFX.Error(null, pxeModuleName, "Please select record to update");
                }
                break;

            case "btnList":
                boolean lbShow = (pnEditMode == EditMode.ADDNEW || pnEditMode == EditMode.UPDATE);
                if (lbShow) {
                    return;
                }
                if (txtField01.getText().equals("")) {
                    ShowMessageFX.Error(null, pxeModuleName, "Please select a record first");
                    break;
                } else {
                    FoodLedgerController foodLedger = new FoodLedgerController();
                    foodLedger.setHistory(poRecord.GetHistory());

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("child/FoodLedger.fxml"));
                    fxmlLoader.setController(foodLedger);

                    Parent parent;
                    try {

                        foodLedger.setBarCodex((String) txtField02.getText());
                        foodLedger.setDescript((String) txtField03.getText());
                        foodLedger.setBriefDes((String) txtField04.getText());
                        foodLedger.setBegInvDate((String) txtOther06.getText());
                        foodLedger.setBrandNme("");

                        parent = fxmlLoader.load();

                        Stage stage = new Stage();

                        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                xOffset = event.getSceneX();
                                yOffset = event.getSceneY();
                            }
                        });
                        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                stage.setX(event.getScreenX() - xOffset);
                                stage.setY(event.getScreenY() - yOffset);

                            }
                        });

                        Scene scene = new Scene(parent);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setAlwaysOnTop(true);
                        stage.setScene(scene);
                        stage.showAndWait();

                    } catch (IOException ex) {
                        ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Please inform MIS department.");
                        System.exit(1);
                    }
                }
                break;

            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        initButton(pnEditMode);
    }

    private void unloadForm() {
//        VBox myBox = (VBox) VBoxForm.getParent();
//        myBox.getChildren().clear();
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        txtField50.setDisable(lbShow);
        txtField51.setDisable(lbShow);

        txtOther03.setDisable(!lbShow);
        txtOther04.setDisable(!lbShow);
        txtOther05.setDisable(!lbShow);
        txtOther06.setDisable(!lbShow);

        btnClose.setVisible(!lbShow);
        btnBrowse.setVisible(!lbShow);
        btnList.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);

        if (lbShow) {
            txtOther03.requestFocus();
        } else {
            txtField51.requestFocus();
        }
        pnEditMode = poRecord.getEditMode();
    }

    private void clearFields() {
        txtField50.setText("");
        txtField51.setText("");
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
//        txtField10.setText("");
//        txtField11.setText("");
//        txtField12.setText("");
        txtField13.setText("");
        txtField14.setText("0.0");
//        txtField15.setText("0.0");
        txtField16.setText("0.0");
        txtField17.setText("0.0");
        txtField18.setText("0.0");
        txtField19.setText("0.0");
//        txtField25.setText("0.0");
        txtField29.setText("");
        txtField50.setText("");
        txtField51.setText("");

        txtOther03.setText("");
        txtOther04.setText("");
        txtOther05.setText("");
        txtOther06.setText("");
        txtOther07.setText("");
        txtOther08.setText("");
        txtOther09.setText("");
        txtOther10.setText("");
        txtOther11.setText("");
        txtOther12.setText("");
        txtOther13.setText("");
        txtOther14.setText("");
        txtOther15.setText("");
        txtOther16.setText("");

        Combo23.getSelectionModel().select(0);
        Combo24.getSelectionModel().select(0);

//        Check20.selectedProperty().setValue(false);
//        Check21.selectedProperty().setValue(false);
        Check22.selectedProperty().setValue(false);
        Check26.selectedProperty().setValue(false);
        Check27.selectedProperty().setValue(false);

        txtOther05.setEditable(true);
        txtOther06.setEditable(true);

        psOldRec = "";
        psBarcode = "";
        psDescript = "";
        psLocation = "";
//        loadDetailData("");
        pnIndex = 51;

        data.clear();
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

    private void Check_KeyPressed(KeyEvent event) {
        CheckBox chkBox = (CheckBox) event.getSource();

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(chkBox);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(chkBox);
        }
    }

    private void loadRecord() {
        Object lsResult;
        txtField01.setText((String) poRecord.getInventory(1));
        txtField02.setText((String) poRecord.getInventory(2));
        txtField50.setText((String) poRecord.getInventory(2));
        psBarcode = txtField50.getText();
        txtField03.setText((String) poRecord.getInventory(3));
        txtField51.setText((String) poRecord.getInventory(3));
        psDescript = txtField51.getText();
        txtField04.setText((String) poRecord.getInventory(4));
        txtField05.setText((String) poRecord.getInventory(5));

        lsResult = poRecord.SearchInventory(6, (String) poRecord.getInventory(6), true);
        txtField06.setText(lsResult != null ? lsResult.toString() : "");

        lsResult = poRecord.SearchInventory(7, (String) poRecord.getInventory(7), true);
        txtField07.setText(lsResult != null ? lsResult.toString() : "");

        lsResult = poRecord.SearchInventory(8, (String) poRecord.getInventory(8), true);
        txtField08.setText(lsResult != null ? lsResult.toString() : "");

        lsResult = poRecord.SearchInventory(9, (String) poRecord.getInventory(9), true);
        txtField09.setText(lsResult != null ? lsResult.toString() : "");

        lsResult = poRecord.SearchInventory(10, (String) poRecord.getInventory(10), true);
        txtField10.setText(lsResult != null ? lsResult.toString() : "");
//      txtField11.setText(poRecord.SearchInventory(11, (String) poRecord.getInventory(11), true));
//      txtField12.setText(poRecord.SearchInventory(12, (String) poRecord.getInventory(12), true));
        txtField13.setText(poRecord.SearchInventory(13, (String) poRecord.getInventory(13), true));
        txtField29.setText(poRecord.SearchInventory(29, (String) poRecord.getInventory(29), true));

        txtField14.setText(String.valueOf(poRecord.getInventory(14)));
//      txtField15.setText(String.valueOf(poRecord.getInventory(15)));
        txtField16.setText(String.valueOf(poRecord.getInventory(16)));
        txtField17.setText(String.valueOf(poRecord.getInventory(17)));
        txtField18.setText(String.valueOf(poRecord.getInventory(18)));
        txtField19.setText(String.valueOf(poRecord.getInventory(19)));

        txtOther03.setText(poRecord.SearchMaster(3, (String) poRecord.getMaster("sLocatnCd"), true));
        psLocation = txtOther03.getText();
        txtOther04.setText(String.valueOf(poRecord.getMaster("nBinNumbr")));
        if (poRecord.getMaster("dAcquired") == null) {
            txtOther05.setText("");
        } else {
            txtOther05.setText(FoodInventoryFX.xsRequestFormat((Date) poRecord.getMaster("dAcquired")));
        }
        if (poRecord.getMaster("dBegInvxx") == null) {
            txtOther06.setText("");
        } else {
            txtOther06.setText(FoodInventoryFX.xsRequestFormat((Date) poRecord.getMaster("dBegInvxx")));
        }

        txtOther07.setText(String.valueOf(poRecord.getMaster("nBegQtyxx")));
        txtOther08.setText(String.valueOf(poRecord.getMaster("nQtyOnHnd")));
        txtOther09.setText(String.valueOf(poRecord.getMaster("nMinLevel")));
        txtOther10.setText(String.valueOf(poRecord.getMaster("nMaxLevel")));
        txtOther11.setText(String.valueOf(poRecord.getMaster("nAvgMonSl")));
        txtOther12.setText(String.valueOf(poRecord.getMaster("nAvgCostx")));
        txtOther13.setText((String) poRecord.getMaster("cClassify"));
        txtOther14.setText(String.valueOf(poRecord.getMaster("nBackOrdr")));
        txtOther15.setText(String.valueOf(poRecord.getMaster("nResvOrdr")));
        txtOther16.setText(String.valueOf(poRecord.getMaster("nFloatQty")));

        Combo23.getSelectionModel().select(Integer.parseInt(poRecord.getInventory("cUnitType").toString()));
        Combo24.getSelectionModel().select(Integer.parseInt(poRecord.getInventory("cInvStatx").toString()));

        boolean lbCheck;

//      lbCheck = poRecord.getInventory("cComboInv").toString().equals("1");
//      Check20.selectedProperty().setValue(lbCheck);
//      lbCheck = poRecord.getInventory("cWthPromo").toString().equals("1");
//      Check21.selectedProperty().setValue(lbCheck);
        lbCheck = poRecord.getInventory("cSerialze").toString().equals("1");
        Check22.selectedProperty().setValue(lbCheck);

        lbCheck = poRecord.getInventory("cRecdStat").toString().equals("1") && 
                poRecord.getMaster("cRecdStat").toString().equals("1");
        Check26.selectedProperty().setValue(lbCheck);
        if (!lbCheck) {
            if (poRecord.getEditMode() == EditMode.READY) {
                if (poRecord.UpdateRecord()) {
                    poRecord.setMaster("cRecdStat", 1);
                    Check26.selectedProperty().setValue(true);
                    ShowMessageFX.Warning("The item was deactivated.", pxeModuleName, "To reactivate Inventory, Save Inventory to activate record. ");
                }
            }
        }

        if (poRecord.getInventory("cNoExpiry") == null) {
            lbCheck = false;
        } else {
            lbCheck = poRecord.getInventory("cNoExpiry").toString().equals("1");
        }
        Check27.selectedProperty().setValue(lbCheck);

        psOldRec = txtField01.getText();
        loadDetailData((String) poRecord.getInventory(1));
        pnEditMode = poRecord.getEditMode();
    }

    public void setGRider(GRider foGRider) {
        this.poGRider = foGRider;
    }

    private final String pxeModuleName = "InvMasterController";
    private final String pxeDefaultDte = "12/25/1945";
    private final String pxeDateFormat = "MM/dd/yyyy";
    private final String pxeCurrentDate = java.time.LocalDate.now().toString();
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private static GRider poGRider;
    private InvMaster poRecord;
    private String psLocation;

    private double xOffset = 0;
    private double yOffset = 0;

    private String psBarcode = "";
    private String psDescript = "";
    private int pnEditMode;
    private int pnIndex = -1;
    private boolean pbLoaded = false;
    private String psOldRec;

    ObservableList<String> cUnitType = FXCollections.observableArrayList("Demo", "Regular", "Repo");
    ObservableList<String> cInvStatx = FXCollections.observableArrayList("Inactive", "Active", "Limited Inv.",
            "Push Product", "Stop Production");

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
                    if (txtField.getText().equals("") || txtField.getText().equals("%")) {
                        txtField.setText("");
                    }
                    break;

                case 51:
                    if (txtField.getText().equals("") || txtField.getText().equals("%")) {
                        txtField.setText("");
                    }
                    break;

                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }

        }
        pnIndex = lnIndex;
        txtField.selectAll();
    };

    final ChangeListener<? super Boolean> txtOther_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtOther = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtOther.getId().substring(8, 10));
        String lsValue = txtOther.getText();

        if (lsValue == null) {
            return;
        }

        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 3:
                    if (!txtOther.getText().equals("") && !txtOther.getText().equals("%")) {
                        txtOther.setText(psLocation);
                        break;
                    } else {
                        txtOther.setText("");
                    }
                    return;

                case 4:
                    int lnBinNumbr;
                    try {
                        lnBinNumbr = Integer.parseInt(lsValue);
                    } catch (NumberFormatException e) {
                        lnBinNumbr = 0;
                        txtOther.setText("0");
                    }

                    if (lnBinNumbr > 65535) {
                        ShowMessageFX.Error(null, pxeModuleName, "Please input number between 0 to 65535");
                        poRecord.setMaster("nBinNumbr", 0);
                        txtOther.setText("0");
                        txtOther.setText(String.valueOf(poRecord.getMaster("nBinNumbr")));
                    } else {
                        poRecord.setMaster("nBinNumbr", lnBinNumbr);
                        txtOther.setText(String.valueOf(poRecord.getMaster("nBinNumbr")));
                    }
                    break;

                case 5:
                    if (CommonUtils.isDate(lsValue, pxeDateFormat)) {
                        poRecord.setMaster("dAcquired", SQLUtil.toDate(lsValue, pxeDateFormat));
                    } else {
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                        poRecord.setMaster("dAcquired", CommonUtils.toDate(pxeCurrentDate));
                    }

                    txtOther.setText(FoodInventoryFX.xsRequestFormat((Date) poRecord.getMaster("dAcquired")));
                    break;
                case 6:
                    if (CommonUtils.isDate(lsValue, pxeDateFormat)) {
                        poRecord.setMaster("dBegInvxx", SQLUtil.toDate(lsValue, pxeDateFormat));
                    } else {
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                        poRecord.setMaster("dBegInvxx", CommonUtils.toDate(pxeCurrentDate));
                    }
                    txtOther.setText(FoodInventoryFX.xsRequestFormat((Date) poRecord.getMaster("dBegInvxx")));
                    break;
                case 7:
                    int lnBegBal;
                    try {
                        lnBegBal = Integer.parseInt(lsValue);
                    } catch (NumberFormatException e) {
                        lnBegBal = 0;
                        txtOther.setText("0");
                    }
                    poRecord.setMaster("nBegQtyxx", lnBegBal);
                    txtOther.setText(String.valueOf(poRecord.getMaster("nBegQtyxx")));
                    break;

                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with name " + txtOther.getId() + " not registered.");
            }
            pnIndex = lnIndex;

        } else {
            switch (lnIndex) {
                case 5:
                /*dTransact*/
                case 6:
                    /*dRefernce*/
                    if (!txtOther.getText().equals(""))
                    try {
                        txtOther.setText(FoodInventoryFX.xsRequestFormat(lsValue));
                    } catch (ParseException e) {
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtOther.selectAll();
                    break;
                default:
            }
            txtOther.selectAll();
        }

    };

    @FXML
    private void table_Clicked(MouseEvent event) {
    }

}
