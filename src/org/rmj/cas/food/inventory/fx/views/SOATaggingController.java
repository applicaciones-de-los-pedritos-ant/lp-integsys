package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableColumnHeader;
import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.lp.parameter.base.LMasDetTrans;
import org.rmj.lp.parameter.base.StatementOfAccount;

public class SOATaggingController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPane, aPaneMaster, aPaneDetail, anchorField;

    @FXML
    private FontAwesomeIconView glyphExit;

    @FXML
    private Button btnBrowse, btnPay, btnUnPay,
            btnClose, btnExit, btnPayAll, btnWaive;
    @FXML
    private ComboBox cmbBox01;
    @FXML
    private ImageView imgTranStat;

    @FXML
    private TextField txtField01, txtField02, txtField03,
            txtField04, txtField05, txtField08, txtField50, txtField51;
    @FXML
    private Label lbStatus, lblTotal, lblService, lblBranch;
    @FXML
    private TextArea txtField06, txtField07;
    @FXML
    private TableView tblBilling;
    @FXML
    private Tooltip tblTooltip;
    @FXML
    private TableColumn index01, index02,
            index03, index04, index05, index06, index07;

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = tblBilling.getSelectionModel().getSelectedIndex() + 1;
        if (event.getClickCount() > 0) {
            if (billing_data.size() >= 1) {

            }
        }
        tblBilling.setOnKeyReleased((KeyEvent t) -> {
            KeyCode key = t.getCode();
            switch (key) {
                case DOWN:
                    pnRow = tblBilling.getSelectionModel().getSelectedIndex() + 1;

                    break;
                case UP:
                    pnRow = tblBilling.getSelectionModel().getSelectedIndex() + 1;
                    break;
                case ENTER:
                case SPACE:
                    isPayClicked(pnRow - 1);
                    break;
                default:
                    return;
            }
        });

    }

    private GRider oApp;
    private StatementOfAccount oTrans;
    private LMasDetTrans oListener;
    private final String pxeModuleName = "StatementOfAccountServiceController";
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
    private String pesoSign = "\u20B1";
    private String psSourceCd = "";

    private final String pxeDateFormat = "MM-dd-yyyy";
    private final String pxeDateFormatMsg = "Date format must be MM-dd-yyyy (e.g. 12-25-1945)";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();
    private FilteredList<TableModel> filteredData;

    private final ObservableList<TableModel> billing_data = FXCollections.observableArrayList();

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
                    case 6:
                        txtField08.setText(foValue.toString());
                        break;
                }
            }

            @Override
            public void DetailRetreive(int fnRow, int fnIndex, Object foValue) {
                switch (fnIndex) {

                }
            }

        };
        cmbBox01.getItems().addAll("Charge Invoice", "Delivery Service");

        oTrans = new StatementOfAccount(oApp, oApp.getBranchCode(), false);
        oTrans.setListener(oListener);
        oTrans.setWithUI(true);
        oTrans.setTranStat(1);
        pbLoaded = true;

        btnPay.setOnAction(this::cmdButton_Click);
        btnPayAll.setOnAction(this::cmdButton_Click);
        btnUnPay.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnWaive.setOnAction(this::cmdButton_Click);

        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtArea_Focus);

        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);

        pnEditMode = EditMode.UNKNOWN;
        initGrid();
        clearFields();

        Timeline timeline = new Timeline();
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300), event -> {
            if (!psOldRec.isEmpty()) {
                tblTooltip.show(tblBilling, xOffset, yOffset);
            }
        }));
        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(300)));

        tblBilling.setOnMouseEntered((MouseEvent event) -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
            timeline.playFromStart();
        });

        tblBilling.setOnMouseExited(event -> tblTooltip.hide());

        cmbBox01.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                try {
                    int indexCMBox = newValue.intValue();
                    // check if addnew so autoselect will not be triggered
                    if (pnEditMode == EditMode.ADDNEW) {
                        switch (indexCMBox) {

                            case 0:
                                if (oTrans.NewTransaction()) {
                                    clearFields();
                                    pnRow = 1;
                                    pnEditMode = oTrans.getEditMode();
                                }
                                oTrans.setMaster("sSourceCd", "CI");
                                oTrans.setSource("CI");
                                loadMaster();
                                System.out.println("Count of Bill:" + oTrans.getBillItemCount());
                                break;

                            case 1:
                                if (oTrans.NewTransaction()) {
                                    clearFields();
                                    pnRow = 1;
                                    pnEditMode = oTrans.getEditMode();
                                }
                                oTrans.setMaster("sSourceCd", "DS");
                                oTrans.setSource("DS");
                                loadMaster();
                                System.out.println("Count of Bill:" + oTrans.getBillItemCount());

                                break;

                        }
                    }

                } catch (SQLException ex) {
                    Logger.getLogger(SOAEntryController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });

    }

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        try {
            switch (lsButton) {

                case "btnClose":
                case "btnExit":
                    unloadForm();
                    return;

                case "btnWaive":
                    ShowMessageFX.Information("This feature is not available. ", pxeModuleName, "");

                    break;

                case "btnPay":
                    if (!psOldRec.equals("")) {
                        if (oTrans.GetUserApproval()) {
                            if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to Tag this Transaction?") == true) {

                                if (oTrans.PrePostTransaction()) {
                                    ShowMessageFX.Information(oTrans.getMessage(), pxeModuleName, "Transaction Tagged successfuly.");

                                    initGrid();

                                    cmbBox01.getSelectionModel().clearSelection();
                                    oTrans = new StatementOfAccount(oApp, oApp.getBranchCode(), false);
                                    oTrans.setListener(oListener);
                                    oTrans.setWithUI(true);
                                    pbLoaded = true;

                                    oTrans.setTranStat(1);
                                    pnEditMode = EditMode.UNKNOWN;
                                    clearFields();

                                    break;
                                } else {
                                    if (!oTrans.getMessage().equals("")) {
                                        ShowMessageFX.Error(null, pxeModuleName, oTrans.getMessage());
                                    } else {
                                        ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Please verify your entry.");
                                    }
                                    return;
                                }
                            }
                        } else {
                            ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, oTrans.getMessage());
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please Load Transaction First!!");
                    }
                    break;

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
                                pnEditMode = oTrans.getEditMode();
                                loadMaster();

                                break;
                            }

                            return;

                        case 51:
                            /*dTransact*/
                            if (oTrans.SearchTransaction(txtField51.getText(), true) == true) {
                                clearFields();
                                pnRow = 1;
                                pnEditMode = oTrans.getEditMode();
                                loadMaster();

                                break;
                            }

                            return;

                        default:
                            ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                            txtField50.requestFocus();
                    }
                    break;

                case "btnPayAll":
                    if (!psOldRec.equals("")) {
                        if (PayTransaction(1)) {
                            loadDetailBill();
                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, "Please Load Transaction First!!");
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    }

                    break;
                case "btnUnPay":
                    if (!psOldRec.equals("")) {
                        if (PayTransaction(0)) {
                            loadDetailBill();

                        } else {
                            ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                        }
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Please Load Transaction First!!");
                    }

                    break;
//
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                    return;
            }
        } catch (SQLException ex) {
            Logger.getLogger(SOATaggingController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public boolean PayTransaction(int fiValue) {

        try {
            int lnCtr;
            int lnRow = oTrans.getBillItemCount();

            if (!(oTrans.getBillItemCount() <= 0)) {
                for (lnCtr = 1; lnCtr <= lnRow; lnCtr++) {
                    int lnCollected = Integer.parseInt(oTrans.getBillDetail(lnCtr, "cCollectd").toString());
                    if (lnCollected <= 0) {
                        int lnBilled = Integer.parseInt(oTrans.getBillDetail(lnCtr, "cPaidxxxx").toString());
                        if (lnBilled != fiValue) {
                            oTrans.setBillDetail(lnCtr, "cPaidxxxx", fiValue);
                        }
                    }
                }
            } else {
                return false;
            }

        } catch (SQLException ex) {
            Logger.getLogger(SOATaggingController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
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
        txtField07.setText("");
        txtField08.setText("");
        txtField50.setText("");
        txtField51.setText("");

        lbStatus.setText("UNKNOWN");
        lblTotal.setText("");
//
        pnRow = 1;
        pnIndex = 50;
        billing_data.clear();
        psOldRec = "";
    }

    private void loadMaster() {

        try {
            String TranNo = (String) oTrans.getMaster("sTransNox");
            txtField01.setText(TranNo);
            txtField02.setText(CommonUtils.xsDateLong((Date) oTrans.getMaster("dTransact")));
            txtField04.setText((String) oTrans.getMaster("sCompnyNm"));
            txtField05.setText((String) oTrans.getMaster("sClientNm"));
            txtField06.setText((String) oTrans.getMaster("xAddressx"));
            txtField07.setText((String) oTrans.getMaster("sRemarksx"));
            txtField08.setText(oTrans.getMaster("nAmtPaidx").toString());

            lbStatus.setText(oTrans.BillngStatus());

            psSourceCd = (String) oTrans.getMaster("sSourceCd");
            loadDetailBill();
            if (!psSourceCd.isEmpty()) {
                switch (psSourceCd) {
                    case "CI":
                        oTrans.setSource("CI");
                        cmbBox01.getSelectionModel().select(0);

                        break;

                    case "DS":
                        oTrans.setSource("DS");
                        cmbBox01.getSelectionModel().select(1);

                        break;
                }

            }
//            loadDetailBill();

        } catch (SQLException ex) {
            Logger.getLogger(SOAEntryController.class.getName()).log(Level.SEVERE, null, ex);
        }

        psOldRec = txtField01.getText();
    }

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

        pnIndex = lnIndex;
        txtField.selectAll();

    };

    private void initGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER;");
        index06.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        index07.setStyle("-fx-alignment: CENTER;");
        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<>("index07"));

        tblBilling.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblBilling.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblBilling.setItems(billing_data);

        index07.setCellFactory(column -> {
            return new TableCell<TableModel, String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);

                    }
                }

                {
                    setOnMouseClicked(event -> {
                        if (!isEmpty()) {
                            isPayClicked(getIndex());
                        }
                    });
                }
            };
        });

    }

    private void isPayClicked(int RowSelect) {
        try {
            if (psOldRec.isEmpty()) {
                return;
            }

            pnRow = RowSelect + 1;
            System.out.println("isPayClicked Rowselect = " + RowSelect);
            if (oTrans.getBillDetail(pnRow, "cPaidxxxx").equals(1)) {
                oTrans.setBillDetail(pnRow, "cPaidxxxx", 0);
                loadDetailBill();

            } else {
                oTrans.setBillDetail(pnRow, "cPaidxxxx", 1);
                loadDetailBill();
            }

        } catch (SQLException ex) {
            Logger.getLogger(SOATaggingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadDetailBill() {
        int lnCtr;
        String isPaid = "";
        BigDecimal lnTotal;
        try {
            int lnRow = oTrans.getBillItemCount();

            billing_data.clear();
            lnTotal = BigDecimal.ZERO;
            /*ADD THE DETAIL*/
            if (!(oTrans.getBillItemCount() <= 0)) {
                for (lnCtr = 1; lnCtr <= lnRow; lnCtr++) {
                    String cPaidxxxx = String.valueOf(oTrans.getBillDetail(lnCtr, "cPaidxxxx"));
                    if (cPaidxxxx.equals("1")) {
                        isPaid = "✔";
                        BigDecimal amount = (BigDecimal) oTrans.getBillDetail(lnCtr, "nAmountxx");
                        lnTotal = lnTotal.add(amount);
                    } else {
                        isPaid = "✘";
                    }
                    billing_data.add(new TableModel(String.valueOf(lnCtr),
                            (String) oTrans.getBillDetail(lnCtr, 2),
                            (String) oTrans.getBillDetail(lnCtr, "sBranchNm"),
                            (String) oTrans.getBillDetail(lnCtr, "sSourceNo"),
                            CommonUtils.xsDateLong((Date) oTrans.getBillDetail(lnCtr, "dTransact")),
                            oTrans.getBillDetail(lnCtr, "nAmountxx").toString(),
                            isPaid,
                            "",
                            "",
                            "",
                            ""
                    ));
                }
                lblTotal.setText(pesoSign + " " + CommonUtils.NumberFormat(lnTotal, "#,##0.00"));
                /*FOCUS ON last selected ROW*/
                if (!billing_data.isEmpty()) {
                    if (lnRow == 1) {
                        pnRow = 1;
                    }

                    txtField03.setText(billing_data.get(0).getIndex03());
                    tblBilling.getSelectionModel().select(pnRow - 1);
                    tblBilling.getFocusModel().focus(pnRow - 1);

                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(SOATaggingController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        try {
            if (!nv) {
                /*Lost Focus*/
                switch (lnIndex) {

                    case 8:/*sAmtPaidx*/
                        double x = 0.00;
                        try {
                            /*this must be numeric*/
                            x = Double.parseDouble(lsValue);
                            if (x > 9999999999.99) {
                                x = 0.00;
                                ShowMessageFX.Warning("Please input not greater than 9999999999.99", pxeModuleName, "");
                                txtField.requestFocus();
                            }
                        } catch (Exception e) {
                            ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                            txtField.requestFocus();
                        }
                        oTrans.setMaster("nAmtPaidx", x);
                        break;

                    default:
                }
                pnIndex = lnIndex;
                txtField.selectAll();
            }
        } catch (SQLException ex) {
            Logger.getLogger(StatementOfAccount.class.getName()).log(Level.SEVERE, null, ex);
        }
    };

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
                    case 51:
                        /*sTransNox*/
                        if (oTrans.SearchTransaction(lsValue, true) == true) {
                            clearFields();
                            pnRow = 1;
                            loadMaster();
                            pnEditMode = oTrans.getEditMode();
                            break;
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
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliveryServiceController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
