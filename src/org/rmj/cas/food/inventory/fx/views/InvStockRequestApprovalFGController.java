/**
 * Maynard Valencia
 *
 * @since 2024-08-20
 */
package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
import static javafx.scene.input.KeyCode.SPACE;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.cas.inventory.base.InvMaster;
import org.rmj.cas.inventory.base.InvRequestManager;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMCategory;

public class InvStockRequestApprovalFGController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPane, anchorField, apLoadFilter, apLoadList, apDetail, apDetailField;
    @FXML
    private Button btnRetrieve, btnSave, btnCancel, btnUpdate, btnPrint, btnClose, btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private TextField txtField50, txtField51;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private TableColumn index01, index02, index03, index04;
    @FXML
    private Label lblHeader;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtDetail01, txtDetail02, txtDetail03, txtDetail04, txtDetail05, txtDetail06;
    @FXML
    private AnchorPane apDetailTable, apDetailOthers;
    @FXML
    private TableView tblRequestList, tblDetail, tblDetailOther;
    @FXML
    private TableColumn detailindex01, detailindex02, detailindex03, detailindex04, detailindex05;
    @FXML
    private TableColumn DetOtherIndex01, DetOtherIndex02, DetOtherIndex03, DetOtherIndex04, DetOtherIndex05, DetOtherIndex06, DetOtherIndex07;

    private final String pxeModuleName = "InvStockRequestApprovalController";

    private static GRider poGRider;
    private InvRequestManager poTrans;

    protected Date pdExpiryDt = null;

    protected Boolean pbEdited = false;
    private boolean pbLoaded = false;

    private int pnEditMode = -1;
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnRowDetail = -1;

    private final String pxeDateFormat = "MM/dd/yyyy";
    private final String pxeDateFormatMsg = "Date format must be MM/dd/yyyy (e.g. 12/25/1945)";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();

    private ObservableList<TableModel> ListData = FXCollections.observableArrayList();
    private ObservableList<TableModel> DetailData = FXCollections.observableArrayList();
    private ObservableList<TableModel> DetailOthers = FXCollections.observableArrayList();
    private FilteredList<TableModel> MasterFilteredData;

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
        int selectedIndex = tblRequestList.getSelectionModel().getSelectedIndex();

        TableModel selectedItem = (TableModel) tblRequestList.getItems().get(selectedIndex);

        pnRow = ListData.indexOf(selectedItem);
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
        poTrans.setFormType(3);//0 Approval Form(Default)//1 Issuance Form//2 Purchase From//3 FG Approval
        initMasterGrid();
        initDetailGrid();
        initActionButton();
        initFieldListener();
        initFieldKeypress();
        clearFields();
        initButton(pnEditMode);
        setTranStat(String.valueOf(pnEditMode));

//        loadInvRequestList();
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
        btnRetrieve.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
    }

    private void initFieldListener() {
        /*Add listener event for field*/
//        txtField01.focusedProperty().addListener(txtField_Focus);
//        txtField50.focusedProperty().addListener(txtField_Focus);
//        txtField51.focusedProperty().addListener(txtField_Focus);

        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
    }

    private void initFieldKeypress() {
        /*Add keypress event for field with search*/
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);

        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail06.setOnKeyPressed(this::txtDetail_KeyPressed);
    }

    private void initMasterGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER;");

        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));

        MasterFilteredData = new FilteredList<>(ListData, b -> true);
        autoSearch(txtField50);
        autoSearch(txtField51);
        // 3. Wrap the FilteredList in a SortedList. 
        SortedList<TableModel> sortedLoadList = new SortedList<>(MasterFilteredData);

        // 4. Bind the SortedList comparator to the TableView comparator.
        // 	  Otherwise, sorting the TableView would have no effect.
        sortedLoadList.comparatorProperty().bind(tblRequestList.comparatorProperty());

        // 5. Add sorted (and filtered) data to the table.
        tblRequestList.setItems(sortedLoadList);
        tblRequestList.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblRequestList.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
            header.setDisable(true);
        });

    }

    private void autoSearch(TextField txtField) {
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        boolean fsCode = true;
        txtField.textProperty().addListener((observable, oldValue, newValue) -> {
            MasterFilteredData.setPredicate(orders -> {
                // If filter text is empty,
                if (newValue == null || newValue.isEmpty()) {
                    return true;
                }
                // Compare order no. and last name of every person with filter text.
                String lowerCaseFilter = newValue.toLowerCase();
                if (lnIndex == 50) {
                    return (orders.getIndex02().toLowerCase().contains(lowerCaseFilter)); // Does not match.   
                } else {
                    return (orders.getIndex04().toLowerCase().contains(lowerCaseFilter)); // Does not match.
                }
            });
        });
    }

    private void initDetailGrid() {
        detailindex01.setStyle("-fx-alignment: CENTER;");
        detailindex02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        detailindex03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        detailindex04.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        detailindex05.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");

        detailindex01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        detailindex02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        detailindex03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        detailindex04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));
        detailindex05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index05"));

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

    private void initDetailGridOthers() {
        DetOtherIndex01.setStyle("-fx-alignment: CENTER;");
        DetOtherIndex02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        DetOtherIndex03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        DetOtherIndex04.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        DetOtherIndex05.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        DetOtherIndex06.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
        DetOtherIndex07.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");

        DetOtherIndex01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index01"));
        DetOtherIndex02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index02"));
        DetOtherIndex03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index03"));
        DetOtherIndex04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index04"));
        DetOtherIndex05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index05"));
        DetOtherIndex06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index06"));
        DetOtherIndex07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel, String>("index07"));

        /*making column's position uninterchangebale*/
        tblDetailOther.widthProperty().addListener(new ChangeListener<Number>() {
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
        tblDetailOther.setItems(DetailOthers);
    }

    private void clearFields() {

        DetailData.clear();
        ListData.clear();
        DetailOthers.clear();

        txtField01.setText("");
        txtDetail01.setText("");
        txtDetail02.setText("");
        txtDetail03.setText("");
        txtDetail04.setText("");
        txtDetail05.setText("0.0");
        txtDetail06.setText("0.0");

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

        //initial view
        btnRetrieve.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnPrint.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);

        //updatemode
        btnSave.setVisible(lbShow);
        btnCancel.setVisible(lbShow);
        apDetailField.setDisable(!lbShow);
        apLoadList.setDisable(false);
        apLoadFilter.setDisable(!lbShow);

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

//        if (event.getCode() == F3) {
//            switch (lnIndex) {
//                //incase my searching
//                }
//        }
        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
    }

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
//        try {
        int lnRow = poTrans.ItemCount(pnRow);
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 06:/*nApproved*/
                    int x = 0;
                    try {
                        /*this must be numeric*/
                        x = Integer.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        x = 0;
                        txtDetail.setText("0");
                    }
                    double z = (Double) poTrans.getDetail(pnRow, pnRowDetail, "nQuantity");
                    if (z > x) {
                        if (x > 0) {
                            double y = 0;

                            y = z - x;
                            poTrans.setDetail(pnRow, pnRowDetail, "nCancelld", y);
                        }
                    }
                    poTrans.setDetail(pnRow, pnRowDetail, "nApproved", x);

                    if (x > 0 & !txtDetail06.getText().isEmpty()) {

                    }
                    loadDetail();
                    loadDetailOthers();

                    txtDetail01.requestFocus();
                    txtDetail01.selectAll();
                    break;
            }
            pnIndex = lnIndex;
        } else {

            pnIndex = lnIndex;
            txtDetail.selectAll();
        }
//        } catch (SQLException ex) {
//           ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Please inform MIS Department.");
//        }
    };

    private void loadInvRequestList() {
        int lnCtr;
        ListData.clear();

        /*LOAD THE RECORD*/
        if (poTrans.loadInvRequestList()) {
            /*ADD THE LIST INTO TABLE*/
            for (lnCtr = 0; lnCtr <= poTrans.getInvRequestCount() - 1; lnCtr++) {
                String lsBranchNm = "";

                XMBranch loBranch = poTrans.GetBranch((String) poTrans.getMaster(lnCtr, "sBranchCd"), true);
                if (loBranch != null) {
                    lsBranchNm = (String) loBranch.getMaster("sBranchNm");
                }
                ListData.add(new TableModel(String.valueOf(lnCtr + 1),
                        lsBranchNm,
                        (String) poTrans.getMaster(lnCtr, "sTransNox"),
                        FoodInventoryFX.xsRequestFormat((Date) poTrans.getMaster(lnCtr, "dTransact"))
                ));
            }
        } else {
            ShowMessageFX.Error(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department.");
        }
        pnRow = -1;

    }

    private boolean printDetail() {
        String printFileName = null;
        try {
            //Create the parameter
            Map<String, Object> params = new HashMap<>();
            params.put("sCompnyNm", "Los Pedritos");
            params.put("sBranchNm", poGRider.getBranchName());
            params.put("sAddressx", poGRider.getAddress());
            params.put("sReportNm", "Inventory Stock Request - FG");
            params.put("xRemarksx", "");

            params.put("sDestinat", "LP General Warehouse");
            params.put("sReportDt", CommonUtils.xsDateLong(poGRider.getServerDate()));
            params.put("sTransNox", poTrans.getMaster(pnRow, "sTransNox").toString().substring(1));
            params.put("sPrintdBy", System.getProperty("user.name"));

            String sourceFileName = "D://GGC_Java_Systems/reports/InvStockRequestApproval.jasper";
            JRBeanCollectionDataSource beanColDataSource1 = new JRBeanCollectionDataSource(DetailOthers);

            JasperPrint jasperPrint = JasperFillManager.fillReport(
                    sourceFileName, params, beanColDataSource1);

            printFileName = jasperPrint.toString();
            if (printFileName != null) {
                JasperViewer jv = new JasperViewer(jasperPrint, false);
                jv.setVisible(true);
//                jv.setAlwaysOnTop(true);
            }
        } catch (JRException ex) {
            ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Please inform MIS Department.");
        }

        return true;
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
//        try {
        switch (lsButton) {
            case "btnRetrieve":
                clearFields();
                loadInvRequestList();

                pnEditMode = poTrans.getEditMode();
                break;

            case "btnUpdate":
                if (poTrans.getInvRequestCount() > 0) {
                    if (poTrans.updateRecord()) {
                        if (pnRow < 0) {
                            if (!ListData.isEmpty()) {
                                tblRequestList.getSelectionModel().select(0);
                                tblRequestList.getFocusModel().focus(0);
                                pnRow = tblRequestList.getSelectionModel().getSelectedIndex();

                                loadRecord();
                            }
                        }
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Unable to update transaction.");
                    }
                }
                break;

            case "btnSave":
                if (poTrans.isEntryOkay(poTrans.getInvRequestCount() - 1)) {
                    if (poTrans.saveTransactionProduct()) {
                        ShowMessageFX.Information(null, pxeModuleName, "Transaction saved successfuly.");
                        clearFields();
                        initMasterGrid();

                        loadInvRequestList();
                        pnEditMode = EditMode.UNKNOWN;
                        initButton(pnEditMode);
                        break;
                    } else {
                        ShowMessageFX.Error(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department.");
                        return;
                    }
                } else {
                    ShowMessageFX.Error(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department.");
                    return;
                }
            case "btnPrint":
                if (!txtField01.getText().trim().equalsIgnoreCase("")) {
                    if (ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transaction?") == true) {
                        if (!printDetail()) {
                            return;
                        }
                    }
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
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
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, lnCtr, "nQuantity").toString()), "0.00"),
                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, lnCtr, "nApproved").toString()), "0.00"),
                    "", "", "", "", ""));
        }
        initDetailGrid();
        loadDetailOthers();

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

    private void loadDetailOthers() {
        LinkedHashMap<String, TableModel> collationDetail = new LinkedHashMap<>();
        DetailOthers.clear();

        for (int lnRow = 0; lnRow <= poTrans.getInvRequestCount() - 1; lnRow++) {
            int lnItem = poTrans.ItemCount(lnRow);

            if (lnItem <= 0) {
                continue;
            }
            for (int lnCtr = 0; lnCtr <= lnItem - 1; lnCtr++) {
                String lsBarCodex = (String) poTrans.getDetailOthers(lnRow, lnCtr, "sBarCodex");
                String lsDescript = (String) poTrans.getDetailOthers(lnRow, lnCtr, "sDescript");

                double lnQuantity = Double.valueOf(poTrans.getDetail(lnRow, lnCtr, "nQuantity").toString());
                double lnApproved = Double.valueOf(poTrans.getDetail(lnRow, lnCtr, "nApproved").toString());
                double lnCancelled = Double.valueOf(poTrans.getDetail(lnRow, lnCtr, "nCancelld").toString());

                // Only process if nApproved > 0 
                if (lnApproved > 0) {
                    // Check if barcode already exists in the map
                    if (collationDetail.containsKey(lsBarCodex)) {
                        // If exists, sum the quantities
                        TableModel existingModel = collationDetail.get(lsBarCodex);
                        double lnnewQuantity = Double.valueOf(existingModel.getIndex04()) + lnQuantity;
                        double lnnewApproved = Double.valueOf(existingModel.getIndex05()) + lnApproved;
                        double lnnewCancelled = Double.valueOf(existingModel.getIndex06()) + lnCancelled;
                        double lnnewNetQty = Double.valueOf(existingModel.getIndex07()) + lnApproved;

                        // Update the existing entry with new values
                        existingModel.setIndex04(CommonUtils.NumberFormat(lnnewQuantity, "0.00"));
                        existingModel.setIndex05(CommonUtils.NumberFormat(lnnewApproved, "0.00"));
                        existingModel.setIndex06(CommonUtils.NumberFormat(lnnewCancelled, "0.00"));
                        existingModel.setIndex07(CommonUtils.NumberFormat(lnnewNetQty, "0.00"));
                    } else {
                        // If not exists, add a new entry
                        collationDetail.put(lsBarCodex, new TableModel(
                                String.valueOf(collationDetail.size() + 1),
                                lsBarCodex,
                                lsDescript,
                                CommonUtils.NumberFormat(lnQuantity, "0.00"),
                                CommonUtils.NumberFormat(lnApproved, "0.00"),
                                CommonUtils.NumberFormat(lnCancelled, "0.00"),
                                CommonUtils.NumberFormat(lnApproved, "0.00"),
                                "", "", ""
                        ));
                    }
                }
            }
        }

        // Add all collated items to DetailOthers
        DetailOthers.addAll(collationDetail.values());

        initDetailGridOthers();
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

            txtDetail04.setText((String) poTrans.getDetail(pnRow, pnRowDetail, "sNotesxxx"));
            txtDetail05.setText(String.valueOf(poTrans.getDetail(pnRow, pnRowDetail, "nQuantity")));
            txtDetail06.setText(String.valueOf(poTrans.getDetail(pnRow, pnRowDetail, "nApproved")));

        } else {

            txtDetail01.setText("");
            txtDetail02.setText("");
            txtDetail03.setText("");
            txtDetail04.setText("");
            txtDetail05.setText("0");
            txtDetail06.setText("0");

        }
    }

}
