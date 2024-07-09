package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.lp.parameter.base.ARDelivery;
import org.rmj.lp.parameter.base.LMasDetTrans;

public class ARDeliveryServiceController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPane, anchorField, aPaneMaster, aPaneDetail;

    @FXML
    private Button btnExit, btnNew,
            btnSave, btnCancel, btnClose,
            btnSearch, btnActivate, btnBrowse,
            btnUpdate, btnLedger, btnAdd,
            btnDelete, btnHistory;

    @FXML
    private FontAwesomeIconView glyphExit, fxIconActivate, fxIconHold;
    @FXML
    private TextField txtField50;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private TextField txtField01, txtField02, txtField03,
            txtField04, txtField05, txtField06;
    @FXML
    private TableView tblBranch;
    @FXML
    private TextField txtOtherDet01, txtOtherDet02, txtOtherDet03,
            txtOtherDet04;
    @FXML
    private TextArea txtOthers03;
    @FXML
    private TextField txtOthers01, txtOthers02, txtOthers04, txtOthers05,
            txtOthers06, txtOthers07, txtOthers08,
            txtOthers09, txtOthers10, txtOthers11,
            txtOthers12, txtOthers13, txtOthers14,
            txtOthers15, txtOthers16;
    @FXML
    private TableColumn index01, index02, index03;
    @FXML
    private CheckBox chkBox01;
    @FXML
    private Label lbStatus;

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = tblBranch.getSelectionModel().getSelectedIndex() + 1;
        if (event.getClickCount() > 0) {
            if (branch_data.size() >= 1) {
                getSelectedItems();
            }
            tblBranch.setOnKeyReleased((KeyEvent t) -> {
                KeyCode key = t.getCode();
                switch (key) {
                    case DOWN:
                        pnRow = tblBranch.getSelectionModel().getSelectedIndex() + 1;
                        int y = 1;
                        pnRow = pnRow + y;
                        getSelectedItems();

                        break;
                    case UP:
                        pnRow = tblBranch.getSelectionModel().getSelectedIndex() + 1;
                        getSelectedItems();
                        break;
                    default:
                        return;
                }
            });
        }

    }

    private GRider oApp;
    private ARDelivery oTrans;
    private LMasDetTrans oListener;
    private final String pxeModuleName = "ARDeliveryServiceController";
    private boolean pbLoaded = false;
    private int pnRow = -1;
    private int oldPnRow = -1;
    private int pnEditMode;
    private int pagecounter;
    private String psOldRec = "";
    private String TransNo = "";
    private int pnIndex = -1;
    double xOffset = 0;
    double yOffset = 0;

    private final String pxeDateFormat = "MM/dd/yyyy";
    private final String pxeDateFormatMsg = "Date format must be MM/dd/yyyy (e.g. 12/25/1945)";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();
    private FilteredList<TableModel> filteredData;

    private final ObservableList<TableModel> branch_data = FXCollections.observableArrayList();

    private Stage getStage() {
        return (Stage) txtField01.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        oListener = new LMasDetTrans() {
            @Override
            public void MasterRetreive(int fnIndex, Object foValue) {
                switch (fnIndex) {
                    case 2:
                        txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) foValue));
                        break;
                    case 3:
                        txtField03.setText((String) foValue);
                        break;
                    case 4:
                        txtField04.setText((String) foValue);
                        break;
                    case 5:
                        txtField05.setText(FoodInventoryFX.xsRequestFormat((Date) foValue));
                        break;
                    case 6:
                        txtField06.setText(CommonUtils.NumberFormat(((Number) foValue), "#,##0.00"));
                        break;

                }
            }

            @Override
            public void DetailRetreive(int fnRow, int fnIndex, Object foValue) {
                switch (fnIndex) {
                    case 4://sCPerson1
                        txtOthers04.setText((String) foValue);
                        break;
                    case 5://sCPPosit1
                        txtOthers05.setText((String) foValue);
                        break;
                    case 6://sTelNoxxx
                        txtOthers06.setText((String) foValue);
                        break;
                    case 7://sFaxNoxxx
                        txtOthers07.setText((String) foValue);
                        break;
                    case 8://sRemarksx
                        txtOthers08.setText((String) foValue);
                        break;
                    case 27://xTermName
                        txtOthers09.setText((String) foValue);
                        break;
                    case 10://nDisCount
                        txtOthers10.setText(CommonUtils.NumberFormat(((Number) foValue), "#,##0.00"));
                        break;
                    case 11://nCredLimt
                        txtOthers11.setText(CommonUtils.NumberFormat(((Number) foValue), "#,##0.00"));
                        break;
                    case 12://nABalance
                        txtOthers12.setText(CommonUtils.NumberFormat(((Number) foValue), "#,##0.00"));
                        break;
                    case 13://nOBalance
                        txtOthers13.setText(CommonUtils.NumberFormat(((Number) foValue), "#,##0.00"));
                        break;
                    case 14://nBalForwd
                        txtOthers14.setText(CommonUtils.NumberFormat(((Number) foValue), "#,##0.00"));
                        break;
                    case 15://dBalForwd
                        txtOthers15.setText(FoodInventoryFX.xsRequestFormat((Date) foValue));
                        break;
                    case 16://dCltSince
                        txtOthers16.setText(FoodInventoryFX.xsRequestFormat((Date) foValue));
                        break;
                    case 18://cHoldAcct
                        if (("1").equals(foValue.toString())) {

                            chkBox01.selectedProperty().setValue(true);
                            fxIconHold.setGlyphName("STOP");
                        } else {
                            chkBox01.selectedProperty().setValue(false);
                            fxIconHold.setGlyphName("PLAY");
                        }
                        break;

                    //client details
                    case 21://sCompnyNm
                        txtOthers02.setText((String) foValue);
                        break;
                    case 22://sLastName
                        txtOtherDet01.setText((String) foValue);
                        break;
                    case 23://sFrstName
                        txtOtherDet02.setText((String) foValue);
                        break;
                    case 24://sMiddName
                        txtOtherDet03.setText((String) foValue);
                        break;
                    case 25://sSuffixNm
                        txtOtherDet04.setText((String) foValue);
                        break;
                    case 26://xAddressx
                        txtOthers03.setText((String) foValue);
                        break;
                }
            }

        };

        oTrans = new ARDelivery(oApp, oApp.getBranchCode(), false);
        oTrans.setListener(oListener);
        oTrans.setWithUI(true);
        pbLoaded = true;

        btnCancel.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnAdd.setOnAction(this::cmdButton_Click);
        btnDelete.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnActivate.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnLedger.setOnAction(this::cmdButton_Click);
        btnHistory.setOnAction(this::cmdButton_Click);

        //Delivery Service 
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);

        //AR Master contactperson
        txtOthers04.focusedProperty().addListener(txtOthers_Focus);
        txtOthers05.focusedProperty().addListener(txtOthers_Focus);
        txtOthers06.focusedProperty().addListener(txtOthers_Focus);
        txtOthers07.focusedProperty().addListener(txtOthers_Focus);
        txtOthers08.focusedProperty().addListener(txtOthers_Focus);
        //ar term
        txtOthers09.setOnKeyPressed(this::txtOthers_KeyPressed);
        //client
        txtOthers02.setOnKeyPressed(this::txtOthers_KeyPressed);

        txtField50.setOnKeyPressed(this::txtField_KeyPressed);

        //AR Master amount and date
        txtOthers10.focusedProperty().addListener(txtOthers_Focus);
        txtOthers11.focusedProperty().addListener(txtOthers_Focus);
        txtOthers12.focusedProperty().addListener(txtOthers_Focus);
        txtOthers13.focusedProperty().addListener(txtOthers_Focus);
        txtOthers14.focusedProperty().addListener(txtOthers_Focus);
        txtOthers15.focusedProperty().addListener(txtOthers_Focus);
        txtOthers16.focusedProperty().addListener(txtOthers_Focus);

        pnEditMode = EditMode.UNKNOWN;
        initButton(pnEditMode);
        initGrid();

    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnAdd.setVisible(lbShow);
        btnDelete.setVisible(lbShow);
        tblBranch.setEditable(lbShow);
        index02.setEditable(lbShow);
        txtField50.setDisable(lbShow);
        btnLedger.setVisible(!lbShow);
        btnHistory.setVisible(!lbShow);
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnActivate.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);

        aPaneMaster.setDisable(!lbShow);
        aPaneDetail.setDisable(!lbShow);

        boolean lbAddNew = (fnValue == EditMode.ADDNEW);
        txtField02.setDisable(!lbAddNew);
        txtField03.setDisable(!lbAddNew);
        txtField04.setDisable(!lbAddNew);
        txtOthers16.setEditable(!lbAddNew);

        if (lbShow) {
            if (lbAddNew) {
                txtField02.requestFocus();
            } else {
                txtField05.requestFocus();
            }
        } else {
            txtField50.requestFocus();
        }

    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        try {
            switch (lsButton) {
                case "btnNew": {
                    if (oTrans.NewTransaction()) {
                        clearFields();
                        loadMaster();
                        pnRow = 1;
                        pnEditMode = oTrans.getEditMode();
                    }

                }
                break;

                case "btnActivate":
                    if (!psOldRec.equals("")) {
                        if (btnActivate.getText().equals("Activate")) {
                            if (oTrans.GetUserApproval()) {
                                if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Activate this Service?") == true) {
                                    if (oTrans.ActivateRecord()) {
                                        ShowMessageFX.Information(null, pxeModuleName, "Record Activated successfully.");
                                        clearFields();
                                        initGrid();
                                        pnEditMode = EditMode.UNKNOWN;
                                        initButton(pnEditMode);

                                        oTrans = new ARDelivery(oApp, oApp.getBranchCode(), false);
                                        oTrans.setListener(oListener);
                                        oTrans.setWithUI(true);
                                        pbLoaded = true;
                                    } else {
                                        ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Can't update record!!!");
                                    }
                                }
                            } else {
                                ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, oTrans.getMessage());
                            }
                        } else {
                            if (oTrans.GetUserApproval()) {
                                if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Deactivate this Service?") == true) {
                                    if (oTrans.DeactivateRecord()) {
                                        ShowMessageFX.Information(null, pxeModuleName, "Record Deactivated successfully.");
                                        clearFields();
                                        initGrid();
                                        pnEditMode = EditMode.UNKNOWN;
                                        initButton(pnEditMode);
                                        oTrans = new ARDelivery(oApp, oApp.getBranchCode(), false);
                                        oTrans.setListener(oListener);
                                        oTrans.setWithUI(true);
                                        pbLoaded = true;
                                    } else {
                                        ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Can't update record!!!");
                                    }
                                }
                            } else {
                                ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, oTrans.getMessage());
                            }
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to confirm!");
                    }
                    break;

                case "btnClose":
                case "btnExit":
                    unloadForm();
                    return;

                case "btnCancel":
                    if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                        clearFields();
                        oTrans = new ARDelivery(oApp, oApp.getBranchCode(), false);
                        oTrans.setListener(oListener);
                        oTrans.setWithUI(true);
                        pbLoaded = true;
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    } else {
                        return;
                    }

                case "btnSearch":
                    if (pnIndex > 2) {
                        pnIndex = 2;
                        txtOthers02.requestFocus();
                    }
                    switch (pnIndex) {
                        case 2:

                            if (oTrans.searchClient(pnRow, txtOthers02.getText(), false)) {
                            } else {
                                txtOthers02.setText((String) oTrans.getARDetail(pnRow, "sCompnyNm"));
                                txtOtherDet01.setText((String) oTrans.getARDetail(pnRow, "sLastName"));
                                txtOtherDet02.setText((String) oTrans.getARDetail(pnRow, "sFrstName"));
                                txtOtherDet03.setText((String) oTrans.getARDetail(pnRow, "sMiddName"));
                                txtOtherDet04.setText((String) oTrans.getARDetail(pnRow, "sSuffixNm"));
                                txtOthers03.setText((String) oTrans.getARDetail(pnRow, "xAddressx"));
                            }

                            break;
                        case 9:

                            if (oTrans.searchTerm(pnRow, txtOthers09.getText(), false)) {
                            } else {
                                txtOthers09.setText((String) oTrans.getARDetail(pnRow, "xTermName"));
                            }
                            break;

                    }
                    return;

                case "btnSave":
                    if (oTrans.SaveTransaction()) {
                        ShowMessageFX.Information(oTrans.getMessage(), pxeModuleName, "Transaction saved successfuly.");
                        clearFields();
                        initGrid();
                        pnEditMode = oTrans.getEditMode();

                        oTrans = new ARDelivery(oApp, oApp.getBranchCode(), false);
                        oTrans.setListener(oListener);
                        oTrans.setWithUI(true);
                        pbLoaded = true;

                        break;
                    } else {
                        if (!oTrans.getMessage().equals("")) {
                            ShowMessageFX.Error(null, pxeModuleName, oTrans.getMessage());
                        } else {
                            ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Please verify your entry.");
                        }
                        return;
                    }
                case "btnBrowse":
                    if (pnIndex < 50) {
                        pnIndex = 50;
                        txtField50.requestFocus();
                    }
                    switch (pnIndex) {

                        case 50:
                            /*sTransNox*/
                            if (oTrans.SearchTransaction(txtField50.getText(), false) == true) {
                                clearFields();
                                pnRow = 1;
                                loadMaster();
                                pnEditMode = oTrans.getEditMode();
                                break;
                            }

                            return;

                        default:
                            ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                            txtField50.requestFocus();
                    }

                    return;
                case "btnUpdate":
                    if (!psOldRec.equals("")) {
                        if ("1".equals((String) oTrans.getMaster("cRecdStat"))) {
                            if (oTrans.UpdateTransaction()) {
                                loadMaster();
                                pnEditMode = oTrans.getEditMode();
                            } else {
                                ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Unable to update transaction.");
                            }
                        } else {
                            ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Unable to update deactivated record...");
                        }
                    }
                    break;

                case "btnDelete":
                    if (!psOldRec.equals("")) {

                        if (oTrans.removeDetail(pnRow)) {
                            loadBranchData();
                        } else {
                            ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Unable to process steps.");
                        }
                    } else {
                        ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Unable to update deactivated record...");
                    }

                    break;
                case "btnAdd":
                    if (!psOldRec.equals("")) {

                        if (oTrans.addDetail()) {
                            loadBranchData();
                        } else {
                            ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Unable to process steps.");
                        }
                    } else {
                        ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Unable to update deactivated record...");
                    }

                    break;
                case "btnHistory":
                    if (oTrans.GetUserApproval()) {
                        if (!psOldRec.equals("")) {
                            LoadHistory(psOldRec);
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please Load Transaction First!!");
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    }

                    break;
                case "btnLedger":
                    if (oTrans.GetUserApproval()) {
                        if (!psOldRec.equals("")) {
                            String psClient = (String) oTrans.getARDetail(pnRow, "sClientID");
                            String psBranchCd = (String) oTrans.getARDetail(pnRow, "sBranchCd");
                            LoadLedger(psClient, psBranchCd);
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please Load Transaction First!!");
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    }

                    break;

                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    return;
            }
            initButton(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void unloadForm() {
//        VBox myBox = (VBox) VBoxForm.getParent();
//        myBox.getChildren().clear();
        MainAnchorPane.getChildren().clear();
        MainAnchorPane.setStyle("-fx-border-color: transparent");
    }

    private void clearFields() {
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField50.setText("");

        txtOthers01.setText("");
        txtOthers02.setText("");
        txtOthers03.setText("");
        txtOthers04.setText("");
        txtOthers05.setText("");
        txtOthers06.setText("");
        txtOthers07.setText("");
        txtOthers08.setText("");
        txtOthers09.setText("");
        txtOthers10.setText("");
        txtOthers11.setText("");
        txtOthers12.setText("");
        txtOthers13.setText("");
        txtOthers14.setText("");
        txtOthers15.setText("");
        txtOthers16.setText("");
        txtOtherDet01.setText("");
        txtOtherDet02.setText("");
        txtOtherDet03.setText("");
        txtOtherDet04.setText("");

        btnActivate.setText("Activate");
        lbStatus.setText("ACTIVE");
        fxIconActivate.setGlyphName("CHECK");
        chkBox01.selectedProperty().setValue(false);

        pnRow = 1;
        pnIndex = 50;
        branch_data.clear();
        psOldRec = "";
    }

    private void loadMaster() {

        try {
            txtField01.setText((String) oTrans.getMaster("sRiderIDx"));
            txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) oTrans.getMaster("dPartnerx")));
            txtField03.setText((String) oTrans.getMaster("sBriefDsc"));
            txtField04.setText((String) oTrans.getMaster("sDescript"));
            txtField05.setText(FoodInventoryFX.xsRequestFormat((Date) oTrans.getMaster("dSrvcChrg")));
            txtField06.setText(CommonUtils.NumberFormat((Number) oTrans.getMaster("nSrvcChrg"), "#,##0.00"));

            if (oTrans.getMaster("cRecdStat").toString().equals("1")) {
                btnActivate.setText("Deactivate");
                fxIconActivate.setGlyphName("CLOSE");
                lbStatus.setText("ACTIVE");
            } else {
                btnActivate.setText("Activate");
                fxIconActivate.setGlyphName("CHECK");
                lbStatus.setText("INACTIVE");
            }
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadBranchData();

        psOldRec = txtField01.getText();
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
        try {
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {

                    case 2:
                        /*dPartnerx*/
                        if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)) {
                            oTrans.setMaster("dPartnerx", SQLUtil.toDate(txtField.getText(), pxeDateFormat));
                        } else {
                            ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                            oTrans.setMaster("dPartnerx", CommonUtils.toDate(pxeDateDefault));
                            txtField.requestFocus();
                            return;
                        }
                        break;
                    case 3:/*sBriefDsc*/
                        if (txtField.getText().length() > 10) {
                            ShowMessageFX.Warning("Max characters for `Brief Description` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        oTrans.setMaster("sBriefDsc", lsValue);
                        break;
                    case 4:/*sDescript*/
                        if (txtField.getText().length() > 64) {
                            ShowMessageFX.Warning("Max characters for `Descript` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtField.requestFocus();
                            return;
                        }
                        oTrans.setMaster("sDescript", lsValue);
                        break;
                    case 5:
                        /*dSrvcChrg*/
                        if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)) {
                            oTrans.setMaster("dSrvcChrg", SQLUtil.toDate(txtField.getText(), pxeDateFormat));
                        } else {
                            ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                            oTrans.setMaster("dSrvcChrg", CommonUtils.toDate(pxeDateDefault));
                        }
                        return;
                    case 6:/*nSrvcChrg*/

                        double x = 0.00;
                        try {
                            /*this must be numeric*/
                            x = Double.parseDouble(lsValue);
                            if (x > 9.99) {
                                x = 0.00;

                                ShowMessageFX.Warning("Please input not greater than 9.99%", pxeModuleName, "");
                                txtField.requestFocus();
                            }
                        } catch (Exception e) {
                            ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                            txtField.requestFocus();
                        }
                        oTrans.setMaster("nSrvcChrg", x);
                        break;
                    case 50:
                        if (lsValue.equals("") || lsValue.equals("%")) {
                            txtField.setText("");
                        }
                        break;

                    default:
                        ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                        return;
                }
                pnIndex = lnIndex;
            } else {
                switch (lnIndex) {
                    case 2:/*dPartnerx*/
                        txtField.setText(SQLUtil.dateFormat(oTrans.getMaster("dPartnerx"), pxeDateFormat));
                        txtField.selectAll();
                        break;

                    case 5:
                        /*dSrvcChrg*/
                        txtField.setText(SQLUtil.dateFormat(oTrans.getMaster("dSrvcChrg"), pxeDateFormat));
                        txtField.selectAll();
                        break;

                    default:
                }
                pnIndex = lnIndex;
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ARDelivery.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void initGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));

        tblBranch.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblBranch.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblBranch.setItems(branch_data);

        index02.setCellFactory(TextFieldTableCell.forTableColumn());
        index02.setOnEditCommit(new EventHandler<CellEditEvent<TableModel, String>>() {
            @Override
            public void handle(CellEditEvent<TableModel, String> event) {
                try {
                    TableModel tableModel = event.getRowValue();
                    tableModel.setIndex02(event.getNewValue());
                    if (!aPaneMaster.isDisable()) {
                        if (oTrans.searchBranch(pnRow, tableModel.getIndex02(), true)) {
                            loadBranchData();
                        } else {
                            ShowMessageFX.Information(null, pxeModuleName, oTrans.getMessage());
                            loadBranchData();
                        }
                    } else {
                        loadBranchData();
                    }
                } catch (SQLException ex) {
                    // Handle SQLException, log, or display an error message
                    Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

        index03.setCellFactory(TextFieldTableCell.forTableColumn());
        index03.setOnEditCommit(new EventHandler<CellEditEvent<TableModel, String>>() {
            @Override
            public void handle(CellEditEvent<TableModel, String> event) {
                try {
                    TableModel tableModel = event.getRowValue();
                    tableModel.setIndex03(event.getNewValue());
                    if (!aPaneMaster.isDisable()) {
                        if (oTrans.searchBranch(pnRow, tableModel.getIndex03(), false)) {
                            loadBranchData();
                        } else {
                            ShowMessageFX.Information(null, pxeModuleName, oTrans.getMessage());
                            loadBranchData();
                        }
                    } else {
                        loadBranchData();
                    }
                } catch (SQLException ex) {
                    // Handle SQLException, log, or display an error message
                    Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    private void loadBranchData() {
        int lnCtr;
        try {
            int lnRow = oTrans.getItemCount();

            branch_data.clear();
            /*ADD THE DETAIL*/
            if (!(oTrans.getItemCount() <= 0)) {
                for (lnCtr = 1; lnCtr <= lnRow; lnCtr++) {
                    branch_data.add(new TableModel(String.valueOf(lnCtr),
                            (String) oTrans.getARDetail(lnCtr, "sBranchCd"),
                            (String) oTrans.getARDetail(lnCtr, "sBranchNm"),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                    ));
                }

                /*FOCUS ON last selected ROW*/
                if (!branch_data.isEmpty()) {
                    if (lnRow == 1) {
                        pnRow = 1;
                    }
                    tblBranch.getSelectionModel().select(pnRow - 1);
                    tblBranch.getFocusModel().focus(pnRow - 1);

                    getSelectedItems();

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void getSelectedItems() {
        try {
            txtOthers01.setText((String) oTrans.getARDetail(pnRow, "sBranchNm"));//bnrach
            //client
            txtOthers02.setText((String) oTrans.getARDetail(pnRow, "sCompnyNm"));//companyname
            txtOtherDet01.setText((String) oTrans.getARDetail(pnRow, "sLastName"));//lastname
            txtOtherDet02.setText((String) oTrans.getARDetail(pnRow, "sFrstName"));//firstname
            txtOtherDet03.setText((String) oTrans.getARDetail(pnRow, "sMiddName"));//middlename
            txtOtherDet04.setText((String) oTrans.getARDetail(pnRow, "sSuffixNm"));//suffix
            txtOthers03.setText((String) oTrans.getARDetail(pnRow, "xAddressx"));//address

            txtOthers04.setText((String) oTrans.getARDetail(pnRow, "sCPerson1"));//contactperson
            txtOthers05.setText((String) oTrans.getARDetail(pnRow, "sCPPosit1"));//positon
            txtOthers06.setText((String) oTrans.getARDetail(pnRow, "sTelNoxxx"));//telno
            txtOthers07.setText((String) oTrans.getARDetail(pnRow, "sFaxNoxxx"));//faxno
            txtOthers08.setText((String) oTrans.getARDetail(pnRow, "sRemarksx"));//remarks

            txtOthers09.setText((String) oTrans.getARDetail(pnRow, "xTermName"));//term
            txtOthers10.setText(CommonUtils.NumberFormat((Number) oTrans.getARDetail(pnRow, "nDisCount"), "#,##0.00"));//discount
            txtOthers11.setText(CommonUtils.NumberFormat((Number) oTrans.getARDetail(pnRow, "nCredLimt"), "#,##0.00"));//creditlimit
            txtOthers12.setText(CommonUtils.NumberFormat((Number) oTrans.getARDetail(pnRow, "nABalance"), "#,##0.00"));//account bal
            txtOthers13.setText(CommonUtils.NumberFormat((Number) oTrans.getARDetail(pnRow, "nOBalance"), "#,##0.00"));//outstang bal
            txtOthers14.setText(CommonUtils.NumberFormat((Number) oTrans.getARDetail(pnRow, "nBalForwd"), "#,##0.00"));//amount bal. forw
            txtOthers15.setText(FoodInventoryFX.xsRequestFormat((Date) oTrans.getARDetail(pnRow, "dBalForwd")));//date bal.forw
            txtOthers16.setText(FoodInventoryFX.xsRequestFormat((Date) oTrans.getARDetail(pnRow, "dCltSince")));//clientsince

            boolean lbCheck = oTrans.getARDetail(pnRow, "cHoldAcct").toString().equals("1");

            if (lbCheck) {
                chkBox01.selectedProperty().setValue(true);
                fxIconHold.setGlyphName("STOP");
            } else {
                chkBox01.selectedProperty().setValue(false);
                fxIconHold.setGlyphName("PLAY");
            }

        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    final ChangeListener<? super Boolean> txtOthers_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtOthers = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtOthers.getId().substring(9, 11));
        String lsValue = txtOthers.getText();

        if (lsValue == null) {
            return;
        }
        try {
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {

                    case 4:/*sCPerson1*/
                        if (txtOthers.getText().length() > 64) {
                            ShowMessageFX.Warning("Max characters for `Contact Person` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtOthers.requestFocus();
                            return;
                        }
                        oTrans.setARDetail(pnRow, "sCPerson1", lsValue);
                        break;
                    case 5:/*sCPPosit1*/
                        if (txtOthers.getText().length() > 32) {
                            ShowMessageFX.Warning("Max characters for `Contact Person Position` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                            txtOthers.requestFocus();
                            return;
                        }
                        oTrans.setARDetail(pnRow, "sCPPosit1", lsValue);
                        break;
                    case 6:/*sTelNoxxx*/
                        if (txtOthers.getText().length() > 30) {
                            ShowMessageFX.Warning("Max characters for `Telephone No.`", pxeModuleName, "Please verify your entry.");
                            txtOthers.requestFocus();
                            return;
                        }
                        oTrans.setARDetail(pnRow, "sTelNoxxx", lsValue);
                        break;
                    case 7:/*sFaxNoxxx*/
                        if (txtOthers.getText().length() > 30) {
                            ShowMessageFX.Warning("Max characters for `Fax No.`", pxeModuleName, "Please verify your entry.");
                            txtOthers.requestFocus();
                            return;
                        }
                        oTrans.setARDetail(pnRow, "sFaxNoxxx", lsValue);
                        break;
                    case 8:/*sRemarksx*/
                        if (txtOthers.getText().length() > 50) {
                            ShowMessageFX.Warning("Max characters for `Remarks`", pxeModuleName, "Please verify your entry.");
                            txtOthers.requestFocus();
                            return;
                        }
                        oTrans.setARDetail(pnRow, "sRemarksx", lsValue);
                        break;
                    case 10:/*nDisCount*/
                        double x = 0.00;
                        try {
                            /*this must be numeric*/
                            x = Double.parseDouble(lsValue);
                            if (x > 99.99) {
                                x = 0.00;
                                ShowMessageFX.Warning("Please input not greater than 99.99", pxeModuleName, "");
                                txtOthers.requestFocus();
                            }
                        } catch (Exception e) {
                            ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                            txtOthers.requestFocus();
                        }
                        oTrans.setARDetail(pnRow, "nDisCount", x);
                        break;
                    case 11:/*nCredLimt*/
                        double y = 0.00;
                        try {
                            /*this must be numeric*/
                            y = Double.parseDouble(lsValue);
                            if (y > 9999999999.99) {
                                y = 0.00;
                                ShowMessageFX.Warning("Please input not greater than 9,999,999,999.99", pxeModuleName, "");
                                txtOthers.requestFocus();
                            }
                        } catch (Exception e) {
                            ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                            txtOthers.requestFocus();
                        }
                        oTrans.setARDetail(pnRow, "nCredLimt", y);
                        break;
                    case 12:/*nABalance*/
                        double z = 0.00;
                        try {
                            /*this must be numeric*/
                            z = Double.parseDouble(lsValue);
                            if (z > 9999999999.99) {
                                z = 0.00;
                                ShowMessageFX.Warning("Please input not greater than 9,999,999,999.99", pxeModuleName, "");
                                txtOthers.requestFocus();
                            }
                        } catch (Exception e) {
                            ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                            txtOthers.requestFocus();
                        }
                        oTrans.setARDetail(pnRow, "nABalance", z);
                        break;
                    case 13:/*nOBalance*/
                        double a = 0.00;
                        try {
                            /*this must be numeric*/
                            a = Double.parseDouble(lsValue);
                            if (a > 9999999999.99) {
                                a = 0.00;
                                ShowMessageFX.Warning("Please input not greater than 9,999,999,999.99", pxeModuleName, "");
                                txtOthers.requestFocus();
                            }
                        } catch (Exception e) {
                            ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                            txtOthers.requestFocus();
                        }
                        oTrans.setARDetail(pnRow, "nOBalance", a);
                        break;
                    case 14:/*nBalForwd*/
                        double s = 0.00;
                        try {
                            /*this must be numeric*/
                            s = Double.parseDouble(lsValue);
                            if (s > 9999999999.99) {
                                s = 0.00;
                                ShowMessageFX.Warning("Please input not greater than 9,999,999,999.99", pxeModuleName, "");
                                txtOthers.requestFocus();
                            }
                        } catch (Exception e) {
                            ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                            txtOthers.requestFocus();
                        }
                        oTrans.setARDetail(pnRow, "nBalForwd", s);
                        break;
                    case 15:/*dBalForwd*/
                        if (CommonUtils.isDate(txtOthers.getText(), pxeDateFormat)) {
                            oTrans.setARDetail(pnRow, "dBalForwd", SQLUtil.toDate(txtOthers.getText(), pxeDateFormat));
                        } else {
                            ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                            oTrans.setARDetail(pnRow, "dBalForwd", CommonUtils.toDate(pxeDateDefault));
                            txtOthers.requestFocus();
                            return;
                        }
                        break;
                    case 16:/*dCltSince*/
                        if (CommonUtils.isDate(txtOthers.getText(), pxeDateFormat)) {
                            oTrans.setARDetail(pnRow, "dCltSince", SQLUtil.toDate(txtOthers.getText(), pxeDateFormat));
                        } else {
                            ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                            oTrans.setARDetail(pnRow, "dCltSince", CommonUtils.toDate(pxeDateDefault));
                            txtOthers.requestFocus();
                            return;
                        }
                        break;

                    default:
                        ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtOthers.getId() + " not registered.");
                        return;
                }
                pnIndex = lnIndex;
            } else {
                switch (lnIndex) {
                    case 15:/*dBalForwd*/
                        txtOthers.setText(SQLUtil.dateFormat(oTrans.getARDetail(pnRow, "dBalForwd"), pxeDateFormat));
                        txtOthers.selectAll();
                        break;

                    case 16:/*dCltSince*/
                        txtOthers.setText(SQLUtil.dateFormat(oTrans.getARDetail(pnRow, "dCltSince"), pxeDateFormat));
                        txtOthers.selectAll();
                        break;

                    default:
                }
                pnIndex = lnIndex;
                txtOthers.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(ARDelivery.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

    private void txtOthers_KeyPressed(KeyEvent event) {
        TextField txtOthers = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtOthers.getId().substring(9, 11));
        String lsValue = txtOthers.getText();
        try {
            if (event.getCode() == F3) {
                switch (lnIndex) {
                    case 2:

                        if (oTrans.searchClient(pnRow, lsValue, false)) {
                        } else {
                            txtOthers.setText((String) oTrans.getARDetail(pnRow, "sCompnyNm"));
                            txtOtherDet01.setText((String) oTrans.getARDetail(pnRow, "sLastName"));
                            txtOtherDet02.setText((String) oTrans.getARDetail(pnRow, "sFrstName"));
                            txtOtherDet03.setText((String) oTrans.getARDetail(pnRow, "sMiddName"));
                            txtOtherDet04.setText((String) oTrans.getARDetail(pnRow, "sSuffixNm"));
                            txtOthers03.setText((String) oTrans.getARDetail(pnRow, "xAddressx"));

                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        }

                        break;
                    case 9:

                        if (oTrans.searchTerm(pnRow, lsValue, false)) {
                        } else {
                            txtOthers.setText((String) oTrans.getARDetail(pnRow, "xTermName"));
                        }
                        break;
                }
            }

            switch (event.getCode()) {
                case ENTER:
                case DOWN:
                    CommonUtils.SetNextFocus(txtOthers);
                    break;
                case UP:
                    CommonUtils.SetPreviousFocus(txtOthers);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        try {
            if (event.getCode() == F3) {
                switch (lnIndex) {
                    case 50:
                        /*sTransNox*/
                        if (oTrans.SearchTransaction(lsValue, false) == true) {
                            clearFields();
                            pnRow = 1;
                            loadMaster();
                            pnEditMode = oTrans.getEditMode();
                            break;
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
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void chkBox01_Click(ActionEvent event) {
        try {
            if (chkBox01.isSelected()) {
                oTrans.setARDetail(pnRow, "cHoldAcct", 1);
            } else {
                oTrans.setARDetail(pnRow, "cHoldAcct", 0);
            }
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void LoadHistory(String lsRiderid) throws SQLException {
        try {
            boolean lbFormLoaded = true;
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/org/rmj/cas/food/inventory/fx/views/ARDeliverySCHistory.fxml"));
            ARDeliverySCHistoryController loControl = new ARDeliverySCHistoryController();
            loControl.setGRider(oApp);
            loControl.setsRiderIDxx(lsRiderid);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();
            lbFormLoaded = loControl.getpbLoaded();

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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            if (lbFormLoaded) {
                stage.showAndWait();
            } else {
                stage.show();
                stage.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            //    ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }

    private void LoadLedger(String psClient, String psBranchCd) throws SQLException {
        try {
            boolean lbFormLoaded = true;
            Stage stage = new Stage();
            FXMLLoader fxmlLoader = new FXMLLoader();

            fxmlLoader.setLocation(getClass().getResource("/org/rmj/cas/food/inventory/fx/views/ARLedger.fxml"));
            ARLedgerController loControlLedger = new ARLedgerController();
            loControlLedger.setGRider(oApp);
            loControlLedger.setsBranchCd(psBranchCd);
            loControlLedger.setsClientID(psClient);

            fxmlLoader.setController(loControlLedger);

            //load the main interface
            Parent parent = fxmlLoader.load();

            lbFormLoaded = loControlLedger.getpbLoaded();

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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            if (lbFormLoaded) {
                stage.showAndWait();
            } else {
                stage.show();
                stage.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
            //    ShowMessageFX.Warning(getStage(),e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
}
