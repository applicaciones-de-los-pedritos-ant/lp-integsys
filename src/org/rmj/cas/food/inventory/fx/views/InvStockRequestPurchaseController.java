/**
 * Maynard Valencia
 *
 * @since 2024-08-22
 */

package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.cas.food.inventory.fx.views.child.InvStockRequestPOController;
import org.rmj.cas.inventory.base.InvMaster;
import org.rmj.cas.inventory.base.InvRequest;
import org.rmj.cas.inventory.base.InvRequestManager;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMCategory;
import org.rmj.purchasing.agent.PurchaseOrders;

public class InvStockRequestPurchaseController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPane, anchorField, apLoadFilter, apLoadList, apDetail, apDetailField, apDetailTable;
    @FXML
    private Button btnBrowse, btnSave, btnCancel, btnUpdate,
            btnClose, btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private TextField txtField50, txtField51;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private TableView tblRequestList;
    @FXML
    private TableColumn index01, index02, index03;
    @FXML
    private Label lblHeader;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    @FXML
    private TextField txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05,
            txtDetail06, txtDetail07, txtDetail08, txtDetail09, txtDetail10;
    @FXML
    private TableView tblDetail;
    @FXML
    private TableColumn detailindex01, detailindex02, detailindex03, detailindex04, detailindex05,
            detailindex06, detailindex07, detailindex08, detailindex09, detailindex10, detailindex11;

    private final String pxeModuleName = "InvProdRequestPurhcaseController";

    private static GRider poGRider;
    private InvRequestManager poTrans;

    protected Date pdExpiryDt = null;

    protected Boolean pbEdited = false;
    private boolean pbLoaded = false;

    private int pnEditMode = -1;
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnRowDetail = -1;

    private double xOffset = 0;
    private double yOffset = 0;

    private final String pxeDateFormat = "MM/dd/yyyy";
    private final String pxeDateFormatMsg = "Date format must be MM/dd/yyyy (e.g. 12/25/1945)";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();

    private ObservableList<TableModel> ListData = FXCollections.observableArrayList();
    private ObservableList<TableModel> DetailData = FXCollections.observableArrayList();

    @FXML
    void tblDetail_Clicked(MouseEvent event) {
        pnRowDetail = tblDetail.getSelectionModel().getSelectedIndex();

        if (pnRowDetail < 0) {
            return;
        }
        setDetailInfo();
        tblDetail.setOnKeyReleased((KeyEvent t) -> {
            KeyCode key = t.getCode();
            switch (key) {
                case DOWN:
                    pnRow = tblDetail.getSelectionModel().getSelectedIndex();

                    break;
                case UP:
                    pnRow = tblDetail.getSelectionModel().getSelectedIndex();
                    break;
                case ENTER:
                case SPACE:
//                    isBillClicked(pnRow - 1);
                    break;
                default:
                    return;
            }
        });
    }

    @FXML
    void tblRequestList_Clicked(MouseEvent event) {
        pnRow = tblRequestList.getSelectionModel().getSelectedIndex();

        if (pnRow < 0) {
            return;
        }
        loadRecord();
        tblRequestList.setOnKeyReleased((KeyEvent t) -> {
            KeyCode key = t.getCode();
            switch (key) {
                case DOWN:
                    pnRow = tblRequestList.getSelectionModel().getSelectedIndex();

                    break;
                case UP:
                    pnRow = tblRequestList.getSelectionModel().getSelectedIndex();
                    break;
                case ENTER:
                case SPACE:
//                    isBillClicked(pnRow - 1);
                    break;
                default:
                    return;
            }
        });
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvRequestManager(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(10);
        poTrans.setFormType(2);//0 Approval Form//1 Issuance Form//2 Purchase From
        initMasterGrid();
        initDetailGrid();
        initActionButton();
        initFieldListener();
        initFieldKeypress();
        clearFields();
        initButton(pnEditMode);
        setTranStat(String.valueOf(pnEditMode));

        pbLoaded = true;
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

    public void setGRider(GRider foGRider) {
        this.poGRider = foGRider;
    }

    private void unloadForm() {
        MainAnchorPane.getChildren().clear();
        MainAnchorPane.setStyle("-fx-border-color: transparent");
    }

    private void initActionButton() {

        /*Add mouse click event listener*/
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
    }

    private void initFieldListener() {

        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);
        txtDetail08.focusedProperty().addListener(txtDetail_Focus);
        txtDetail09.focusedProperty().addListener(txtDetail_Focus);
        txtDetail10.focusedProperty().addListener(txtDetail_Focus);
    }

    private void initFieldKeypress() {
        /*Add keypress event for field with search*/
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail06.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail07.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail08.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail09.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail10.setOnKeyPressed(this::txtDetail_KeyPressed);
    }

    private void initMasterGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));

        /*making column's position uninterchangebale*/
        tblRequestList.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) tblRequestList.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        /*Set data source to table*/
        tblRequestList.setItems(ListData);
    }

    private void initDetailGrid() {
        detailindex01.setStyle("-fx-alignment: CENTER;");
        detailindex02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        detailindex03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        detailindex04.setStyle("-fx-alignment: CENTER;");
        detailindex05.setStyle("-fx-alignment: CENTER;");
        detailindex06.setStyle("-fx-alignment: CENTER;");
        detailindex07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        detailindex08.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        detailindex09.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        detailindex09.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        detailindex10.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        detailindex11.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");

        detailindex01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        detailindex02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        detailindex03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        detailindex04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));
        detailindex05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index05"));
        detailindex06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index06"));
        detailindex07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index07"));
        detailindex08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index08"));
        detailindex09.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index09"));
        detailindex10.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index10"));
        detailindex11.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index11"));

        /*making column's position uninterchangebale*/
        tblDetail.widthProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) {
                TableHeaderRow header = (TableHeaderRow) tblRequestList.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                    }
                });
            }
        });

        /*Set data source to table*/
        tblDetail.setItems(DetailData);
    }

    private void clearFields() {

        DetailData.clear();
        ListData.clear();

        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtDetail01.setText("");
        txtDetail02.setText("");
        txtDetail03.setText("");
        txtDetail04.setText("0.0");
        txtDetail05.setText("0.0");
        txtDetail06.setText("0.0");
        txtDetail07.setText("0.0");
        txtDetail08.setText("");
        txtDetail09.setText("0.0");
        txtDetail10.setText("0.0");

        pnRow = -1;
        pnIndex = 50;

        pnEditMode = EditMode.UNKNOWN;
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

    private void initButton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);

        //initial 
        btnBrowse.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        apLoadList.setDisable(lbShow);
        apLoadFilter.setDisable(lbShow);

        //updatemode
        btnSave.setVisible(lbShow);
        btnCancel.setVisible(lbShow);
        apDetailField.setDisable(!lbShow);

    }

    private void txtDetail_KeyPressed(KeyEvent event) {
        TextField txtDetail = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();
//        if (event.getCode() == F3) {
//            switch (lnIndex) {
//                //incase my searching
//                }
//        }

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtDetail);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtDetail);
        }
    }

    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();

        if (event.getCode() == F3) {

            clearFields();
            switch (lnIndex) {

                case 50:

                    XMBranch loBranch = poTrans.GetBranch(lsValue, false);

                    if (loBranch != null) {
                        loadInvRequestList((String) loBranch.getMaster("sBranchCd"));
                        txtField03.setText((String) loBranch.getMaster("sBranchNm"));

                    }
                    break;

                case 51:
                    InvRequest loInvRequest = poTrans.GetInvRequest(lsValue, true);

                    if (loInvRequest != null) {
                        loadInvRequestList((String) loInvRequest.getMaster("sBranchCd"));

                        String transno = (String) loInvRequest.getMaster("sTransNox");
                        txtField.setText(transno);
                        boolean lbfound = false;
                        if (ListData.size() > 0) {
                            for (int lnCtr = 0; lnCtr < ListData.size() - 1; lnCtr++) {
                                String listTransNo = ListData.get(lnCtr).getIndex02();

                                if (listTransNo.equals(transno)) {
                                    // Transaction found, select the row
                                    tblRequestList.getSelectionModel().select(lnCtr);
                                    tblDetail.getSelectionModel().select(0);
                                    pnRow = tblRequestList.getSelectionModel().getSelectedIndex();
                                    pnRowDetail = tblDetail.getSelectionModel().getSelectedIndex();
                                    tblRequestList.scrollTo(lnCtr);

                                    setDetailInfo();

                                    lbfound = true;
                                    break;
                                }
                            }

                            if (!lbfound) {
                                ShowMessageFX.Error("The transaction might have already exceeded the approved quantity or has no approved quantity.", pxeModuleName, "Please verify the Transaction");
                            }
                        }
                    } else {
                        txtField.setText("");
                    }
                    break;
            }
            pnEditMode = poTrans.getEditMode();

            initButton(pnEditMode);
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

        TextField txtDetail = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(8, 10));
        pnIndex = lnIndex;

    };
    final ChangeListener<? super Boolean> txtDetail_Focus = (o, ov, nv) -> {
        if (!pbLoaded) {
            return;
        }

        TextField txtDetail = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();
        if (lsValue == null) {
            return;
        }
//        try {
        int lnRow = poTrans.ItemCount(pnRow);
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 10:/*nOrderQty*/
                    double x = 0;
                    try {
                        /*this must be numeric*/
                        x = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        x = 0;
                        txtDetail.setText("0.0");
                    }

                    double orderitem = (Double) poTrans.getDetail(pnRow, pnRowDetail, "nOrderQty");
                    poTrans.setDetail(pnRow, pnRowDetail, "nOrderQty", x + orderitem);

                    poTrans.setDetailOther(pnRow, pnRowDetail, "nOrderQty", x);

                    if (x > 0.00 & !txtDetail10.getText().isEmpty()) {

                    }
                    loadDetail();

                    txtDetail01.requestFocus();
                    txtDetail01.selectAll();
                    break;
            }
            pnIndex = lnIndex;
        } else {
            switch (lnIndex) {
             case 10:/*nOrderQty*/
                    double x = 0;
                    try {
                        /*this must be numeric*/
                        x = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        x = 0;
                        txtDetail.setText("0.0");
                    }

                    double orderitem = (Double) poTrans.getDetail(pnRow, pnRowDetail, "nOrderQty");
                    poTrans.setDetail(pnRow, pnRowDetail, "nOrderQty", x - orderitem);

                    poTrans.setDetailOther(pnRow, pnRowDetail, "nOrderQty", x);
            }
            pnIndex = lnIndex;
            txtDetail.selectAll();
        }
//        } catch (SQLException ex) {
//           ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Please inform MIS Department.");
//        }
    };

    private void loadInvRequestList(String fsBranchCd) {
        int lnCtr;
        ListData.clear();

        /*LOAD THE RECORD*/
        if (poTrans.loadInvRequestListByBranch(fsBranchCd)) {
            /*ADD THE LIST INTO TABLE*/
            for (lnCtr = 0; lnCtr <= poTrans.getInvRequestCount() - 1; lnCtr++) {
                ListData.add(new TableModel(String.valueOf(lnCtr + 1),
                        (String) poTrans.getMaster(lnCtr, "sTransNox"),
                        FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster(lnCtr, "dTransact")),
                        (String) poTrans.getMaster(lnCtr, "sBranchNm")
                ));
            }
        } else {
            ShowMessageFX.Error(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department.");
        }
        pnRow = -1;

    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
//        try {
        switch (lsButton) {
            case "btnBrowse":
                if (pnIndex < 50) {
                    pnIndex = 50;
                }
                clearFields();
                switch (pnIndex) {
                    case 50:
                        XMBranch loBranch = poTrans.GetBranch(txtField50.getText(), false);

                        if (loBranch != null) {
                            loadInvRequestList((String) loBranch.getMaster("sBranchCd"));
                            txtField03.setText((String) loBranch.getMaster("sBranchNm"));
                        }
                        break;

                    case 51:
                        InvRequest loInvRequest = poTrans.GetInvRequest(txtField51.getText(), true);

                        if (loInvRequest != null) {
                            loadInvRequestList((String) loInvRequest.getMaster("sBranchCd"));

                            String transno = (String) loInvRequest.getMaster("sTransNox");
                            txtField51.setText(transno);
                            boolean lbfound = false;
                            if (ListData.size() > 0) {
                                for (int lnCtr = 0; lnCtr < ListData.size() - 1; lnCtr++) {
                                    String listTransNo = ListData.get(lnCtr).getIndex02();

                                    if (listTransNo.equals(transno)) {
                                        // Transaction found, select the row
                                        tblRequestList.getSelectionModel().select(lnCtr);
                                        tblDetail.getSelectionModel().select(0);
                                        pnRow = tblRequestList.getSelectionModel().getSelectedIndex();
                                        pnRowDetail = tblDetail.getSelectionModel().getSelectedIndex();
                                        tblRequestList.scrollTo(lnCtr);

                                        setDetailInfo();

                                        lbfound = true;
                                        break;
                                    }
                                }

                                if (!lbfound) {
                                    ShowMessageFX.Error("The transaction might have already "
                                            + "exceeded the approved quantity or has no approved quantity.", pxeModuleName, "Please verify the Transaction");
                                }
                            }
                        } else {
                            txtField51.setText("");
                        }
                        break;
                }
                pnEditMode = poTrans.getEditMode();
                initButton(pnEditMode);
                break;

            case "btnUpdate":
                if (!txtField01.getText().trim().equalsIgnoreCase("")) {
                    if (poTrans.updateRecord(pnRow)) {
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Unable to update transaction.");
                    }
                }
                break;

            case "btnSave":
                if (poTrans.isEntryOkay(pnRow)) {
                    if (showInvTransfer()) {
                        if (poTrans.saveTransaction(pnRow)) {
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction saved successfuly.");
                            clearFields();
                            initMasterGrid();
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
                            break;
                        } else {
                            ShowMessageFX.Error(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department.");
                            return;
                        }
                    }
                } else {
                    ShowMessageFX.Error(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department.");
                    return;
                }
                break;

            case "btnCancel":
                if (ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true) {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                    break;
                } else {
                    return;
                }
            case "btnExit":
            case "btnClose":
                unloadForm();
                return;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }

        initButton(pnEditMode);
//        } catch (SQLException ex) {
//            ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Please inform MIS Department.");
//        }

    }

    private void loadRecord() {
        txtField01.setText((String) poTrans.getMaster(pnRow, "sTransNox"));
        txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster(pnRow, "dTransact")));
        XMBranch loBranch = poTrans.GetBranch((String) poTrans.getMaster(pnRow, "sBranchCd"), true);
        if (loBranch != null) {
            txtField03.setText((String) loBranch.getMaster("sBranchNm"));
        }

        pnRowDetail = -1;
        loadDetail();
        setTranStat((String) poTrans.getMaster(pnRow, "cTranStat"));

    }

    private void loadDetail() {
        int lnCtr;

        int lnRow = poTrans.ItemCount(pnRow);
        DetailData.clear();

        if (lnRow <= 0) {
            ShowMessageFX.Error("Selected Transaction has No Detail's", pxeModuleName, "Please inform MIS Department.");
            return;
        }

        for (lnCtr = 0; lnCtr <= lnRow - 1; lnCtr++) {
            /*ADD THE DETAIL TO TABLE*/

            DetailData.add(new TableModel(String.valueOf(lnCtr + 1),
                    (String) poTrans.getDetailOthers(pnRow, lnCtr, "sBarCodex"),
                    (String) poTrans.getDetailOthers(pnRow, lnCtr, "sDescript"),
                    (String) poTrans.getDetailOthers(pnRow, lnCtr, "sBrandNme"),
                    (String) poTrans.getDetailOthers(pnRow, lnCtr, "sMeasurNm"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, lnCtr, "nRecOrder").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, lnCtr, "nApproved").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, lnCtr, "nCancelld").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, lnCtr, "nIssueQty").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetailOthers(pnRow, lnCtr, "nOrderQty").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, lnCtr, "nOnTranst").toString()), "0.00"),
                    ""));
        }

        if (pnRowDetail < 0 || pnRowDetail >= DetailData.size()) {
            if (!DetailData.isEmpty()) {
                /* FOCUS ON FIRST ROW */
                tblDetail.getSelectionModel().select(0);
                tblDetail.getFocusModel().focus(0);
                pnRowDetail = tblDetail.getSelectionModel().getSelectedIndex();
            }
            setDetailInfo();
        } else {
            /* FOCUS ON THE ROW THAT pnRowDetail POINTS TO */
            tblDetail.getSelectionModel().select(pnRowDetail);
            tblDetail.getFocusModel().focus(pnRowDetail);
            setDetailInfo();
        }

    }

    private void setDetailInfo() {
        if (pnRowDetail >= 0) {

            txtDetail01.setText((String) poTrans.getDetailOthers(pnRow, pnRowDetail, "sBarCodex"));
            txtDetail02.setText((String) poTrans.getDetailOthers(pnRow, pnRowDetail, "sDescript"));

            Object categoryCodeObj = poTrans.getDetailOthers(pnRow, pnRowDetail, "sCategCd1");

            if (categoryCodeObj != null && !categoryCodeObj.toString().trim().isEmpty()) {
                XMCategory loCategory = poTrans.GetCategory((String) categoryCodeObj, true);

                if (loCategory != null) {
                    txtDetail03.setText((String) loCategory.getMaster("sDescript"));
                }
            } else {
                txtDetail03.setText("");
            }
            
            txtDetail04.setText(poTrans.getDetail(pnRow, pnRowDetail, "nQtyOnHnd").toString());
            txtDetail05.setText(poTrans.getDetail(pnRow, pnRowDetail, "nRecOrder").toString());
            InvMaster loInventory = poTrans.GetIssueInventory((String) poTrans.getDetailOthers(pnRow, pnRowDetail, "sStockIDx"), true);

            if (loInventory != null) {
                txtDetail06.setText(String.valueOf(loInventory.getMaster("nQtyOnHnd")));
                txtDetail07.setText(String.valueOf(loInventory.getMaster("nResvOrdr")));
            }
            txtDetail08.setText((String) poTrans.getDetail(pnRow, pnRowDetail, "sNotesxxx"));
            txtDetail09.setText(String.valueOf(poTrans.getDetail(pnRow, pnRowDetail, "nApproved")));
            txtDetail10.setText(String.valueOf(poTrans.getDetailOthers(pnRow, pnRowDetail, "nOrderQty")));

        } else {

            txtDetail01.setText("");
            txtDetail02.setText("");
            txtDetail03.setText("");
            txtDetail04.setText("0.00");
            txtDetail05.setText("0.00");
            txtDetail06.setText("0.00");
            txtDetail07.setText("0.00");
            txtDetail08.setText("");
            txtDetail09.setText("0.00");
            txtDetail10.setText("0.00");

        }
    }

    private boolean showInvTransfer() {
        boolean lbHasTransfer = false;
        InvStockRequestPOController loInvStockRequestPOController = new InvStockRequestPOController();
        PurchaseOrders loPurchaseOrders = new PurchaseOrders(poGRider, poGRider.getBranchCode(), false);

        loPurchaseOrders.newTransaction();
        String orderNo = (String) poTrans.getMaster(pnRow, "sTransNox");

        int lnItem = poTrans.ItemCount(pnRow);

        //validate if has item to save and add to detail
        for (int lnCtr = 0; lnCtr <= lnItem - 1; lnCtr++) {
            if ((Double) poTrans.getDetailOthers(pnRow, lnCtr, "nOrderQty") > 0) {
                //to addnew row to end if not last
                loPurchaseOrders.addDetail();
                int lnRow = loPurchaseOrders.ItemCount() - 1;

                Double nOrderQty = (Double) poTrans.getDetailOthers(pnRow, lnCtr, "nOrderQty");

                loPurchaseOrders.SearchDetail(lnRow, 3, ((String) poTrans.getDetailOthers(pnRow, lnCtr, "sBarCodex")), false, true);
                loPurchaseOrders.setDetail(lnRow, "nQuantity", nOrderQty);
                loPurchaseOrders.setDetail(lnRow, "sOrderNox", orderNo);
                lbHasTransfer = true;
//
//                if (lnCtr != paDetail.size() - 1) {
//                }
            }
        }

        if (lbHasTransfer) {
            //set the required master data
            loPurchaseOrders.SearchMaster(2, (String) poGRider.getBranchCode(), true);
            loPurchaseOrders.SearchMaster(5, (String) poTrans.getMaster(pnRow, "sBranchCd"), true);
            loPurchaseOrders.setMaster("sReferNox", orderNo.substring(4));

            loInvStockRequestPOController.setGRider(poGRider);
            loInvStockRequestPOController.setPurchaseOrders(loPurchaseOrders);
//            load modal
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("../views/child/InvStockRequestPO.fxml"));
            fxmlLoader.setController(loInvStockRequestPOController);
            try {
                Parent parent = fxmlLoader.load();

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
                stage.setScene(scene);
                stage.showAndWait();
//
                if (!loInvStockRequestPOController.isCancelled()) {

                    double issueitem = (Double) poTrans.getDetail(pnRow, pnRowDetail, "nIssueQty");
                    poTrans.setDetail(pnRow, pnRowDetail, "nOnTranst", issueitem);
                    return true;
                } else {
                    return false;
                }

            } catch (IOException ex) {
                ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Please inform MIS department.");
                return false;
            }
        }
        return false;
    }

}
