package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
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
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.json.simple.JSONObject;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.cas.inventory.base.Inventory;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMTerm;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.appdriver.constants.UserRight;
import org.rmj.purchasing.agent.PurchaseOrders;


public class PurchaseOrderController implements Initializable {

    @FXML private Button btnExit;
    @FXML private AnchorPane anchorField;
    @FXML private TextField txtField01;
    @FXML private TextField txtField03;
    @FXML private TextField txtField02;
    @FXML private TextField txtField05;
    @FXML private TextField txtField06;
    @FXML private TextField txtField07;
    @FXML private TextField txtField08;
    @FXML private TextArea txtField10;
    @FXML private Label Label09;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail80;
    @FXML private TextField txtDetail04;
    @FXML private TextField txtDetail05,txtDetail06;
    @FXML private TableView table;
    @FXML private Button btnNew;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnConfirm;
    @FXML private Button btnDel;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private Button btnBrowse;
    @FXML private Button btnPrint;
    @FXML private ImageView imgTranStat;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private AnchorPane dataPane;
    @FXML private Button btnUpdate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //set the main anchor pane fit the size of its parent anchor pane
        dataPane.setTopAnchor(dataPane, 0.0);
        dataPane.setBottomAnchor(dataPane, 0.0);
        dataPane.setLeftAnchor(dataPane, 0.0);
        dataPane.setRightAnchor(dataPane, 0.0);   
        
        /*Initialize class*/
        poTrans = new PurchaseOrders(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(0);
        poTrans.setCallBack(poCallBack);
        poTrans.setClientNm(System.getProperty("user.name"));
                
        /*Set action event handler for the buttons*/
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnDel.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnConfirm.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        
        /*Add listener to text fields*/
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtArea_Focus);
        
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail80.focusedProperty().addListener(txtDetail_Focus);
                
        /*Add keypress event for field with search*/
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtFieldArea_KeyPressed);
        
        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail80.setOnKeyPressed(this::txtDetail_KeyPressed);
        
        pnEditMode = EditMode.UNKNOWN;
        
        clearFields();
        initGrid();
        initButton(pnEditMode);
        
        pbLoaded = true;
    }    

    @FXML
    private void table_Clicked(MouseEvent event) { 
        pnRow = table.getSelectionModel().getSelectedIndex();
        
        setDetailInfo(); 
        txtDetail03.requestFocus();
        txtDetail03.selectAll();
    }
    
    private void setDetailInfo(){
        String lsStockIDx = (String) poTrans.getDetail(pnRow, "sStockIDx");
        if (pnRow >= 0){                        
            if (!lsStockIDx.equals("")){    
                Inventory loInventory = poTrans.GetInventory(lsStockIDx, true, false);
                psBarCodex = (String) loInventory.getMaster("sBarCodex");
                psDescript = (String) loInventory.getMaster("sDescript");
            } else {
                psBarCodex = (String) poTrans.getDetail(pnRow, 100);
                psDescript = (String) poTrans.getDetail(pnRow, 101);
            }
            
            /*load barcode and description*/
            txtDetail03.setText(psBarCodex);
            txtDetail80.setText(psDescript);
            
            txtDetail04.setText(String.valueOf(poTrans.getDetail(pnRow, 4))); /*Quantity*/
            txtDetail05.setText(String.valueOf(poTrans.getDetail(pnRow, 5))); /*Quantity*/
            txtDetail06.setText(String.valueOf(poTrans.getDetail(pnRow, 9))); /*Quantity*/
        } else{
            txtDetail03.setText("");
            txtDetail80.setText("");
            txtDetail04.setText("0.00");
            txtDetail05.setText("0.00");
            txtDetail06.setText("0.00");
        }
    }
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnDel.setVisible(lbShow);
                
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnConfirm.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnPrint.setVisible(!lbShow);

        txtField01.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        txtField08.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtDetail03.setDisable(!lbShow);
        txtDetail04.setDisable(!lbShow);
        txtDetail05.setDisable(!lbShow);
        txtDetail06.setDisable(!lbShow);
        txtDetail80.setDisable(!lbShow);
        txtField50.setDisable(lbShow);
        
        if (lbShow)
            txtField02.requestFocus();
        else
            txtField50.requestFocus();
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Bar Code");
        TableColumn index03 = new TableColumn("Brand");
        TableColumn index04 = new TableColumn("Description");
        TableColumn index05 = new TableColumn("Measure");
        TableColumn index06 = new TableColumn("Qty");
        TableColumn index07 = new TableColumn("UPrice");
        
        index01.setPrefWidth(30);
        index02.setPrefWidth(90);
        index03.setPrefWidth(150);
        index04.setPrefWidth(120);
        index05.setPrefWidth(75);
        index06.setPrefWidth(45); index06.setStyle("-fx-alignment: CENTER-RIGHT;");
        index07.setPrefWidth(85); index07.setStyle("-fx-alignment: CENTER-RIGHT;");

        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);
        index06.setSortable(false); index06.setResizable(false);
        index07.setSortable(false); index07.setResizable(false);

        table.getColumns().clear();        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index04);
        table.getColumns().add(index03);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        table.getColumns().add(index07);
      
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index07"));
       
        /*Set data source to table*/
        table.setItems(data);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            case "btnNew":
                if (poTrans.newTransaction()){
                    clearFields(); 
                    loadRecord(); 
                    pnEditMode = poTrans.getEditMode();
                    
                    if (poTrans.SearchMaster("sBranchCd", poGRider.getBranchCode(), true))
                        txtField05.requestFocus();
                } 
                break;
            case "btnConfirm":
                if (!psOldRec.equals("")){  
                    if (CommonUtils.getConfiguration(poGRider, "TokenAprvl").equals("1")){
                        if (!"0".equals((String) poTrans.getMaster("cTranStat"))) return;
                        
                        //token type approval
                        if (showFXDialog.getTokenApproval(poGRider, "CASys_DBF.PO_Master", psOldRec)){
                            if (poTrans.closeTransaction(psOldRec, poGRider.getUserID(), "TOKENAPPROVL")){
                                ShowMessageFX.Information("Transaction was approved successfully.", pxeModuleName, "Approval successful!!!");

                                if (poTrans.openTransaction(psOldRec)){       
                                    clearFields();
                                    loadRecord(); 
                                    psOldRec = (String) poTrans.getMaster("sTransNox");

                                    if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transasction?")== true){
                                        poTrans.printRecord();
                                    }
                            
                                    clearFields();
                                    initGrid();
                                    pnEditMode = EditMode.UNKNOWN;
                                }
                            }else poTrans.ShowMessageFX();
                        }else poTrans.ShowMessageFX();
                    } else{
                        //user approval type
                        if (poGRider.getUserLevel() < UserRight.MANAGER){
                            JSONObject loJSON = showFXDialog.getApproval(poGRider);

                            if (loJSON != null){
                                if ((int) loJSON.get("nUserLevl") < UserRight.MANAGER){
                                    ShowMessageFX.Information("Only managerial accounts can approved transactions.", pxeModuleName, "Authentication failed!!!");
                                    return;
                                }

                                if (poTrans.closeTransaction(psOldRec, (String) loJSON.get("sUserIDxx"), "USERAPPROVAL")){
                                    ShowMessageFX.Information("Transaction was approved successfully.", pxeModuleName, "Approval successful!!!");

                                    if (poTrans.openTransaction(psOldRec)){                                
                                        loadRecord(); 
                                        psOldRec = (String) poTrans.getMaster("sTransNox");

                                        if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transasction?")== true){
                                            poTrans.printRecord();
                                        }

                                        pnEditMode = poTrans.getEditMode();
                                    }
                                    
                                    clearFields();
                                    initGrid();
                                    pnEditMode = EditMode.UNKNOWN;
                                }else poTrans.ShowMessageFX();
                            }else poTrans.ShowMessageFX();
                        } else {
                            if (poTrans.closeTransaction(psOldRec, poGRider.getUserID(), "USERAPPROVAL")){
                                ShowMessageFX.Information("Transaction was approved successfully.", pxeModuleName, "Approval successful!!!");

                                if (poTrans.openTransaction(psOldRec)){                                
                                    loadRecord(); 
                                    psOldRec = (String) poTrans.getMaster("sTransNox");

                                    poTrans.printRecord();

                                    pnEditMode = poTrans.getEditMode();
                                } else {
                                    clearFields();
                                    initGrid();
                                    pnEditMode = EditMode.UNKNOWN;
                                }
                            }else poTrans.ShowMessageFX();
                        }
                    }
                } else {
                    ShowMessageFX.Information("Please load transaction to approve.", pxeModuleName, "No transaction loaded.");
                    return;
                }
                break;
            case "btnClose":
            case "btnExit": 
                unloadForm();
                return;
            case "btnCancel": 
                clearFields();
                pnEditMode = EditMode.UNKNOWN;
                break;
            case "btnSearch": return;
            case "btnSave": 
                if (poTrans.saveTransaction()){
                    ShowMessageFX.Information(null, pxeModuleName, "Transaction saved successfuly.");

                    //re open and print the record
                    if (poTrans.openTransaction((String) poTrans.getMaster("sTransNox"))){
                        loadRecord(); 
                        psOldRec = (String) poTrans.getMaster("sTransNox");
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields();
                        initGrid();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                    break;
                } else poTrans.ShowMessageFX(); return;
            case "btnDel":  
                deleteDetail();
                return;
            case "btnBrowse":               
                if (pnIndex == 50){
                    if(poTrans.BrowseRecord(txtField50.getText(), true)){
                        loadRecord();
                    }else poTrans.ShowMessageFX();
                } else {
                    if(poTrans.BrowseRecord(txtField51.getText(), false)){
                        loadRecord();
                    }else poTrans.ShowMessageFX();
                }
                
                pnEditMode = poTrans.getEditMode();   
                break;
//                return;
            case "btnUpdate":
                if (!psOldRec.equals("")){
                    if ("0".equals((String) poTrans.getMaster("cTranStat"))){
                        if (poTrans.updateRecord()){
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                        } else 
                            ShowMessageFX.Warning(null, pxeModuleName, "Unable to update transaction.");
                    } else {
                        ShowMessageFX.Warning(null, pxeModuleName, "Unable to update transaction...");
                    }
                }
                break;
            case "btnPrint":
                if (!psOldRec.equals("")){
                    if ("1".equals(poTrans.getMaster("cTranStat")))
                        poTrans.printRecord();
                    else 
                        ShowMessageFX.Warning(null, pxeModuleName, "Approval is needed to print this transaction.");
                }
                break;
//                return;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        
        initButton(pnEditMode);
    }
    
    
    private void loadRecord(){
        txtField01.setText((String) poTrans.getMaster(1));
        txtField03.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster(3)));
        txtField07.setText((String) poTrans.getMaster(7));
        txtField50.setText((String) poTrans.getMaster(7));
        txtField10.setText((String) poTrans.getMaster(10));

        XMBranch loBranch = poTrans.GetBranch((String)poTrans.getMaster(2), true);
        if (loBranch != null) txtField02.setText((String) loBranch.getMaster("sBranchNm"));

        XMBranch loDestinat = poTrans.GetBranch((String)poTrans.getMaster(5), true);
        if (loDestinat != null) txtField05.setText((String) loDestinat.getMaster("sBranchNm"));

        JSONObject loSupplier = poTrans.GetSupplier((String)poTrans.getMaster(6), true);
        if (loSupplier != null) {
            txtField06.setText((String) loSupplier.get("sClientNm"));
            txtField51.setText((String) loSupplier.get("sClientNm"));
        }

        XMTerm loTerm = poTrans.GetTerm((String)poTrans.getMaster(8), true);
        if (loTerm != null) txtField08.setText((String) loTerm.getMaster("sDescript"));

//        XMInventoryType loInv = poTrans.GetInventoryType((String)poTrans.getMaster(16), true);
//        if (loInv != null) txtField16.setText((String) loInv.getMaster("sDescript"));
        
        Label09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()), "#,##0.00"));      
        
        loadDetail();
        setTranStat((String) poTrans.getMaster("cTranStat"));
            
        pnRow = 0;
        pnOldRow = 0;
        psOldRec = txtField01.getText();
    }
    
    private void clearFields(){
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField10.setText("");
//        txtField16.setText("");
        txtField50.setText("");
        txtField51.setText("");
        
        txtDetail03.setText("");
        txtDetail04.setText("0.00");
        txtDetail06.setText("0.00");
        txtDetail05.setText("0.00");
        txtDetail80.setText("");
        
        Label09.setText("0.00");
        
        pnRow = -1;
        pnOldRow = -1;
        pnIndex = -1;
        
        psOldRec = "";
        psBranchNm = "";
        psDestinat= "";
        psInvTypNm = "";
        psSupplier = "";
        
        psBarCodex = "";
        psDescript = "";
        
        setTranStat("-1");
        data.clear();
    }
    
    private void unloadForm(){
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();        
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        
        switch (event.getCode()){
            case F3:
                switch (lnIndex){
                case 2: /*sBranchCd*/
                    if (!poTrans.SearchMaster(lnIndex, txtField.getText(), false)==true){
                        txtField.setText("");
                    }
                    break;
                case 5: /*sDestinat*/
                    if (!poTrans.SearchMaster(lnIndex, txtField.getText(), false)==true){
                        txtField.setText("");
                    }
                    break;
                case 6: /*sSupplier*/
                    if (!poTrans.SearchMaster(lnIndex, txtField.getText(), false)==true){
                        txtField.setText("");
                    }
                    break;
                case 8: /*sTermCode*/
                    if (!poTrans.SearchMaster(lnIndex, txtField.getText(), false)==true){
                        txtField.setText("");
                    }
                    break;
                case 50: /*ReferNox*/
                    if(poTrans.BrowseRecord(txtField.getText(), true)==true){
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields(); 
                        pnEditMode = EditMode.UNKNOWN; break;
                    }
                            
                    return;
                case 51: /*sSupplier*/
                    if(poTrans.BrowseRecord(txtField.getText(), false)==true){
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN; break;
                    }
                            
                    return;
                }   
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField); break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
    }
    
    private void txtFieldArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER){ 
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }
    }
    
    private void txtDetail_KeyPressed(KeyEvent event){
        TextField txtDetail = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = "";
        JSONObject loJSON;
        
        switch (event.getCode()){
            case F3:
                if (lnIndex == 3){
                    lsValue = txtDetail.getText();
                    if (lsValue == null || lsValue.isEmpty()) lsValue = "";
                    if(poTrans.SearchDetail(pnRow, 3, "%" + lsValue, false, true));
                 
                } else if (lnIndex == 80){
                    lsValue = txtDetail.getText();
                    if (lsValue == null || lsValue.isEmpty()) lsValue = "";

                    if(poTrans.SearchDetail(pnRow, 3, lsValue  + "%", false, false));
//                    
                }
                    loadDetail();
                }
                
        
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(txtDetail);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtDetail);
        }
    }
    
    private void deleteDetail(){
        if (pnOldRow == -1) return;
        if (poTrans.deleteDetail(pnOldRow)){
            pnRow = poTrans.ItemCount() - 1;
            pnOldRow = pnRow;
            
            loadDetail();
            setDetailInfo();
        }
    }
    
    private void setTranStat(String fsValue){
        switch (fsValue){
            case "0":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/open.png")); break;
            case "1":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/closed.png")); break;
            case "2":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/posted.png")); break;
            case "3":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/cancelled.png")); break;
            case "4":
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/void.png")); break;
            default:
                imgTranStat.setImage(new Image("org/rmj/cas/food/inventory/fx/images/unknown.png"));
        }    
    }
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = poTrans.ItemCount();
        
        data.clear();
        /*ADD THE DETAIL*/
        
        Inventory loInventory;
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            if (!"".equals((String) poTrans.getDetail(lnCtr, "sStockIDx"))) {
                loInventory = poTrans.GetInventory((String) poTrans.getDetail(lnCtr, "sStockIDx"), true, false);
            
                data.add(new TableModel(String.valueOf(lnCtr + 1), 
                                        (String) loInventory.getMaster("sBarCodex"), 
                                        (String) poTrans.getDetail(lnCtr,"sBrandNme"), 
                                        (String) loInventory.getMaster("sDescript"), 
                                        loInventory.getMeasureMent((String) loInventory.getMaster("sMeasurID")),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce")),
                                        CommonUtils.NumberFormat(((Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()))
                                            * Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString())), "#,##0.00"),
                                        "",
                                        ""));
            } else {
                data.add(new TableModel(String.valueOf(lnCtr + 1), 
                                        (String) poTrans.getDetail(lnCtr, 100), 
                                        (String) poTrans.getDetail(lnCtr,"sBranchNme"), 
                                        (String) poTrans.getDetail(lnCtr, 101), 
                                        (String) poTrans.getDetail(lnCtr, 102),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce")),
                                        CommonUtils.NumberFormat(((Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()))
                                            * Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString())), "#,##0.00"),
                                        "",
                                        "",
                                        ""));
            }
        }
                
        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()){
            table.getSelectionModel().select(lnRow -1);
            table.getFocusModel().focus(lnRow - 1);
            
            pnRow = lnRow -1;
            //pnRow = table.getSelectionModel().getSelectedIndex();           
            setDetailInfo();
        }
        
        Label09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()), "#,##0.00"));      
    }

    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private final String pxeModuleName = "PurchaseOrderController";
    private static GRider poGRider;
    private PurchaseOrders poTrans;
    
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    
    private final String pxeDateFormat = "MM-dd-yyyy";
    private final String pxeDateFormatMsg = "Date format must be MM-dd-yyyy (e.g. 12-25-1945)";
    private final String pxeDateDefault = "1900-01-01";
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private TableModel model;
    
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;
    
    private String psOldRec = "";
    private String psBranchNm = "";
    private String psDestinat = "";
    private String psInvTypNm = "";
    private String psTermName = "";
    private String psSupplier = "";
    
    private String psBarCodex = "";
    private String psDescript = "";
    
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/            
            switch (lnIndex){
                case 10: /*sRemarksx*/
                    if (lsValue.length() > 256) lsValue = lsValue.substring(0, 256);
                    
                    poTrans.setMaster(lnIndex, CommonUtils.TitleCase(lsValue));
                    txtField.setText((String)poTrans.getMaster(lnIndex));
            }
        }else{ 
            pnIndex = -1;
            txtField.selectAll();
        }
    };
    
    final ChangeListener<? super Boolean> txtDetail_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(9, 11));
        String lsValue = txtField.getText();
        
        if (pnRow < 0) return;
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/            
            switch (lnIndex){
                case 3: /*Barcode*/
                    if (poTrans.getDetail(pnRow, "sStockIDx").equals("")){
                        poTrans.setDetail(pnRow, 100, txtDetail03.getText());
                    }
                    break;
                case 80: /*Description*/
                    //send the barcode and descript to class if it has no stock id
                    if (poTrans.getDetail(pnRow, "sStockIDx").equals("")){                        
                        if (txtDetail80.getText().equals("")){
                            ShowMessageFX.Warning(null, "Warning", "Description must have a value if stock is not existing.");
                            return;
                        }
                        poTrans.setDetail(pnRow, 101, txtDetail80.getText());
                    }
                    break;
                case 4: /*Quantity*/
                    double lnValue = 0;
                    try {
                        /*this must be numeric*/
                        lnValue = Double.valueOf(lsValue);
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();   
                    }
                    poTrans.setDetail(pnRow, lnIndex, lnValue);
                    break;
                case 5: /*UnitPrice*/
                    lnValue = 0;
                    try {
                        /*this must be numeric*/
                        lnValue = Double.valueOf(lsValue);
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();   
                    }
                    poTrans.setDetail(pnRow, lnIndex, lnValue);
            }
            
            pnOldRow = table.getSelectionModel().getSelectedIndex();
        } else {
            pnIndex = -1;
            txtField.selectAll();
        }
    };
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/           
            switch (lnIndex){
                case 2: /*sBranchCd*/
                case 5: /*sDestinat*/
                case 6: /*sSupplier*/
                case 8: /*sTermCode*/
                    break;
//                case 16: /*sInvTypCd*/
//                    getMaster(lnIndex);
//                    return;
                case 7: /*sReferNox*/
                    if (txtField.getText().length() > 8){
                        ShowMessageFX.Warning("Max characters for `Reference No.` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                        txtField.requestFocus();
                        return;
                    }
                    poTrans.setMaster(lnIndex, txtField.getText());
                    return;
                case 3: /*dTransact*/
                    if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
                        poTrans.setMaster(lnIndex, SQLUtil.toDate(txtField.getText(), pxeDateFormat));
                    } else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                        poTrans.setMaster(lnIndex, CommonUtils.toDate(pxeDateDefault));
                    }
                    return;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
            pnIndex = -1;
        } else{
            switch (lnIndex){
                case 3: /*dTransact*/
                    txtField.setText(SQLUtil.dateFormat(poTrans.getMaster("dTransact"), pxeDateFormat));
                    txtField.selectAll();
                    break;
                default:
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        }
    };   
    
    IMasterDetail poCallBack = new IMasterDetail() {
        @Override
        public void MasterRetreive(int fnIndex) {
            getMaster(fnIndex);
        }

        @Override
        public void DetailRetreive(int fnIndex) {
            switch(fnIndex){
                case 4:
                    txtDetail04.setText(String.valueOf(poTrans.getDetail(pnRow, fnIndex)));
//                    loadDetail();
                    
                    //if (!poTrans.getDetail(poTrans.getDetailCount() - 1, "sStockIDx").toString().isEmpty() &&
                    //        (int) poTrans.getDetail(poTrans.getDetailCount() - 1, fnIndex) > 0){
                    //    poTrans.addDetail();
                    //    pnRow = poTrans.getDetailCount() - 1;
                    //}
                    
                    poTrans.addDetail();
                    pnRow = poTrans.ItemCount() - 1;
                    loadDetail();
                    if (txtDetail04.getText().isEmpty()){
                        txtDetail04.requestFocus();
                        txtDetail04.selectAll();
                    } else{
                        txtDetail03.requestFocus();
                        txtDetail03.selectAll();
                    }
            }
        }
    };
    
    private void getMaster(int fnIndex){
        switch(fnIndex){
        case 2:
            XMBranch loBranch = poTrans.GetBranch((String)poTrans.getMaster(fnIndex), true);
            if (loBranch != null) txtField02.setText((String) loBranch.getMaster("sBranchNm"));
            break;
        case 5:
            XMBranch loDestinat = poTrans.GetBranch((String)poTrans.getMaster(fnIndex), true);
            if (loDestinat != null) txtField05.setText((String) loDestinat.getMaster("sBranchNm"));
            break;
        case 6:
            JSONObject loSupplier = poTrans.GetSupplier((String)poTrans.getMaster(fnIndex), true);
            if (loSupplier != null) txtField06.setText((String) loSupplier.get("sClientNm"));
            break;
        case 8:
            XMTerm loTerm = poTrans.GetTerm((String)poTrans.getMaster(fnIndex), true);
            if (loTerm != null) txtField08.setText((String) loTerm.getMaster("sDescript"));
            break;
//        case 16:
//            XMInventoryType loInv = poTrans.GetInventoryType((String)poTrans.getMaster(fnIndex), true);
//            if (loInv != null) txtField16.setText((String) loInv.getMaster("sDescript"));
//            break;
        case 3:
            txtField03.setText(CommonUtils.xsDateLong((Date)poTrans.getMaster(fnIndex)));
            break;
        case 7:
            txtField07.setText((String)poTrans.getMaster(fnIndex));
            break;
        case 9:
            Label09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()), "#,##0.00"));      
        }
    }
    
    private String getMeasureMent(String fsMeasureID) {
        String lsSQL;
        ResultSet loRS = null;
        
        lsSQL = "SELECT" +
                " a.sMeasurID" +
                ", b.sMeasurNm" +
                " FROM Inventory a" +
                ", Measure b" +
                " WHERE a.sMeasurID = b.sMeasurID" +
                " AND a.sMeasurID =" + SQLUtil.toSQL(fsMeasureID) +
                " LIMIT 1";
        try{
            loRS = poGRider.executeQuery(lsSQL);
            loRS.first();
            
            return loRS.getString("sMeasurNm");
        }catch(SQLException e){
            return "";
        }
    }
}
