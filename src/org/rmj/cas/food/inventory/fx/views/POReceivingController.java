package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.text.ParseException;
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
import org.rmj.cas.inventory.base.Inventory;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMDepartment;
import org.rmj.lp.parameter.agent.XMTerm;
import org.rmj.purchasing.agent.XMPOReceiving;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;

public class POReceivingController implements Initializable {

    @FXML private Button btnExit;
    @FXML private AnchorPane anchorField;
    @FXML private TextField txtField01;
    @FXML private TextField txtField03;
    @FXML private TextField txtField05;
    @FXML private TextField txtField06;
    @FXML private TextField txtField07;
    @FXML private TextField txtField08;
    @FXML private TextArea txtField16;
    @FXML private Label Label09;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail80;
    @FXML private TextField txtDetail04;
    @FXML private TableView table;
    @FXML private TextField txtField10;
    @FXML private TextField txtField11;
    @FXML private TextField txtField15;
    @FXML private TextField txtField12;
    @FXML private TextField txtField13;
    @FXML private TextField txtField14;
    @FXML private Button btnNew;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnConfirm;
    @FXML private Button btnDel;
    @FXML private TextField txtDetail08;
    @FXML private TextField txtDetail09;
    @FXML private TextField txtDetail07;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private Button btnBrowse;
    @FXML private Label lblHeader;
    @FXML private ImageView imgTranStat;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private TextField txtDetail10;
    @FXML private AnchorPane dataPane;
    @FXML private TextField txtField17;
    @FXML private Button btnUpdate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Initialize class*/
        poTrans = new XMPOReceiving(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(0);
        poTrans.setCallBack(poCallBack);
        poTrans.setClientNm(System.getProperty("user.name"));
                
        /*Set action event handler for the buttons*/
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnDel.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnConfirm.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        
        /*Add listener to text fields*/
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField13.focusedProperty().addListener(txtField_Focus);
        txtField14.focusedProperty().addListener(txtField_Focus);
        txtField15.focusedProperty().addListener(txtField_Focus);
        txtField17.focusedProperty().addListener(txtField_Focus);
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtField16.focusedProperty().addListener(txtArea_Focus);
        
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);
        txtDetail08.focusedProperty().addListener(txtDetail_Focus);
        txtDetail09.focusedProperty().addListener(txtDetail_Focus);
        txtDetail10.focusedProperty().addListener(txtDetail_Focus);
        txtDetail80.focusedProperty().addListener(txtDetail_Focus);
                
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
        if (pnRow >= 0){
        String lsStockIDx = (String) poTrans.getDetail(pnRow, "sStockIDx");                   
            txtDetail03.setText((String) poTrans.getDetail(pnRow, 3));
            
            Inventory loInventory; 
            
            if (!lsStockIDx.equals("")){    
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
            txtDetail08.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, 8).toString()), "0.00"));
            txtDetail09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, 9).toString()), "0.00"));
            txtDetail10.setText(CommonUtils.xsDateMedium((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
        } else{
            txtDetail03.setText("");
            txtDetail04.setText("");
            txtDetail07.setText("0");
            txtDetail08.setText("0.00");
            txtDetail09.setText("0.00");
            txtDetail80.setText("");   
            txtDetail10.setText("");
        }
    }
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
        btnDel.setVisible(lbShow);
        lblHeader.setVisible(lbShow);
        
        txtField50.setDisable(lbShow);
        txtField51.setDisable(lbShow);
                
        btnBrowse.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        btnConfirm.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        
        txtField01.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        txtField08.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        txtField14.setDisable(!lbShow);
        txtField15.setDisable(!lbShow);
        txtField16.setDisable(!lbShow);
        txtField17.setDisable(!lbShow);
        txtDetail03.setDisable(!lbShow);
        txtDetail04.setDisable(!lbShow);
        txtDetail07.setDisable(!lbShow);
        txtDetail08.setDisable(!lbShow);
        txtDetail09.setDisable(!lbShow);
        txtDetail80.setDisable(!lbShow);

        if (lbShow)
            txtField17.requestFocus();
        else
            txtField50.requestFocus();
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Order No.");
        TableColumn index03 = new TableColumn("Bar Code");
        TableColumn index04 = new TableColumn("Description");
        TableColumn index05 = new TableColumn("Brand");
        TableColumn index06 = new TableColumn("Unit");
        TableColumn index07 = new TableColumn("Superseded");
        TableColumn index08 = new TableColumn("Unit Type");
        TableColumn index09 = new TableColumn("Qty");
        TableColumn index10 = new TableColumn("Unit Price");
        TableColumn index11 = new TableColumn("Freight");
        TableColumn index12 = new TableColumn("Total");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(90);
        index04.setPrefWidth(250);
        index05.setPrefWidth(120);
        index06.setPrefWidth(80);
        index07.setPrefWidth(80);
        index08.setPrefWidth(80); index08.setStyle("-fx-alignment: CENTER;");
        index09.setPrefWidth(80); index09.setStyle("-fx-alignment: CENTER-RIGHT;");
        index10.setPrefWidth(80); index10.setStyle("-fx-alignment: CENTER-RIGHT;");
        index11.setPrefWidth(80); index11.setStyle("-fx-alignment: CENTER-RIGHT;");
        index12.setPrefWidth(80); index12.setStyle("-fx-alignment: CENTER-RIGHT;");
       
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);
        index06.setSortable(false); index06.setResizable(false);
        index07.setSortable(false); index07.setResizable(false);
        index08.setSortable(false); index08.setResizable(false);
        index09.setSortable(false); index09.setResizable(false);
        index10.setSortable(false); index10.setResizable(false);
        index11.setSortable(false); index11.setResizable(false);
        index12.setSortable(false); index12.setResizable(false);

        table.getColumns().clear();        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        table.getColumns().add(index07);
        table.getColumns().add(index08);
        table.getColumns().add(index09);
        table.getColumns().add(index10);
        table.getColumns().add(index11);
        table.getColumns().add(index12);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index08"));
        index09.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index09"));
        index10.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index10"));
        index11.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index11"));
        index12.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index12"));
        
        /*making column's position uninterchangebale*/
        table.widthProperty().addListener(new ChangeListener<Number>() {  
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
            {
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
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            case "btnNew":
                if (poTrans.newRecord()) {
                    clearFields(); 
                    loadRecord();
                    txtField50.setText("");
                    
                    if (poTrans.SearchMaster("sBranchCd", poGRider.getBranchCode(), true))
                        txtField17.requestFocus();
                        
                    pnEditMode = poTrans.getEditMode();
                }
                break;
            case "btnConfirm": 
                if (!psOldRec.equals("")){
                    if(poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_CANCELLED) ||
                        poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_POSTED) ||
                        poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_VOID)){
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }
                    
                    if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to confirm this transasction?")== true){
                        if (poTrans.closeRecord(psOldRec)){
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction CONFIRMED successfully.");
                            
                            if (poTrans.openRecord(psOldRec)){
                                clearFields();
                                loadRecord(); 
                                
                                psOldRec = (String) poTrans.getMaster("sTransNox");
                                
                                if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transasction?")== true){
                                    poTrans.printRecord();
                                }
                            }

                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                    }
                } else ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to confirm!");
                break;
            case "btnClose":
            case "btnExit": 
                unloadForm();
                return;   
            case "btnCancel": 
                if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to disregard changes?") == true){
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                    break;
                } else
                    return;
                
            case "btnSearch": return;
            case "btnSave": 
                if (poTrans.saveRecord()){
                    ShowMessageFX.Information(null, pxeModuleName, "Transaction saved successfuly.");
                    
                    //re open and print the record
                    if (poTrans.openRecord((String) poTrans.getMaster("sTransNox"))){
                        loadRecord(); 
                        psOldRec = (String) poTrans.getMaster("sTransNox");
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields();
                        initGrid();
                        pnEditMode = EditMode.UNKNOWN;
                        initButton(pnEditMode);
                    }
                    break;
                } else return;
                
            case "btnBrowse":
                if(poTrans.BrowseRecord(txtField50.getText(), true)==true){
                    loadRecord(); 
                    pnEditMode = poTrans.getEditMode();
                    break;
                }else {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                    
                return;    
            case "btnDel":  
               int lnIndex = table.getSelectionModel().getFocusedIndex();    
                if(table.getSelectionModel().getSelectedItem() == null){
                     ShowMessageFX.Warning(null, pxeModuleName, "Please select item to remove!");
                     break;
                }
                if(ShowMessageFX.OkayCancel(null, pxeModuleName, "Do you want to remove this item?") == true){
                    poTrans.deleteDetail(lnIndex);
                    loadDetail();
                }     
                break;
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


            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        
        initButton(pnEditMode);
    }
    
    private void loadRecord(){
        txtField01.setText((String) poTrans.getMaster(1));
        
        txtField03.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
        txtField06.setText((String) poTrans.getMaster(6));
        txtField50.setText((String) poTrans.getMaster(6));
        psReferNox = txtField50.getText();
        txtField07.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dRefernce")));
        txtField10.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(10).toString()), "0.00"));
        txtField11.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(11).toString()), "#,##0.00"));
        txtField12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(12).toString()), "0.00"));
        txtField13.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(13).toString()), "#,##0.00"));
        txtField14.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(14).toString()), "#,##0.00"));
        txtField15.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(15).toString()), "#,##0.00"));
        txtField16.setText((String) poTrans.getMaster(16));
        
        JSONObject loSupplier = poTrans.GetSupplier((String)poTrans.getMaster(5), true);
        if (loSupplier != null) {
            txtField05.setText((String) loSupplier.get("sClientNm"));
            txtField51.setText((String) loSupplier.get("sClientNm"));
        }

        XMTerm loTerm = poTrans.GetTerm((String)poTrans.getMaster(8), true);
        if (loTerm != null) txtField08.setText((String) loTerm.getMaster("sDescript"));

        setTranStat((String) poTrans.getMaster("cTranStat"));

        pnRow = 0;
        pnOldRow = 0;
        loadDetail();
        
        psOldRec = txtField01.getText();
    }
    
    private void clearFields(){
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
    }
    
    private void unloadForm(){
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (event.getCode() == F3){
            switch (lnIndex){
                case 5: /*sSupplier*/
                    if (poTrans.SearchMaster(lnIndex, lsValue, false)){
                        CommonUtils.SetNextFocus(txtField);
                    }else txtField.setText("");
                    return;
                case 8: /*sTermCode*/
                    if (poTrans.SearchMaster(lnIndex, lsValue, false)){
                        CommonUtils.SetNextFocus(txtField);
                    } else txtField.setText("");
                    return;
                case 17: /*sSourceNo*/
                    if (poTrans.SearchMaster(lnIndex, lsValue, false)){
                        CommonUtils.SetNextFocus(txtField);
                        loadDetail();
                    } else txtField.setText("");
                    return;
                case 20: /*sInvTypCd*/
                    if (poTrans.SearchMaster(lnIndex, lsValue, false)){
                        CommonUtils.SetNextFocus(txtField);
                    }else txtField.setText("");
                    return;
                case 50: /*ReferNox*/
                    if(poTrans.BrowseRecord(lsValue, true)==true){
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN; break;
                    }
                            
                    return;
                case 51: /*Supplier*/
                    if(poTrans.BrowseRecord(lsValue, false)== true){
                    loadRecord(); 
                    pnEditMode = poTrans.getEditMode();
                    } else {
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                    }
            }
        } 
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(txtField);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtField);
        }
    }
    
    private void ComboBox_KeyPressed(KeyEvent event){
        ComboBox cmbBox = (ComboBox)event.getSource();
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(cmbBox);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(cmbBox);
        }
    }
    
    private void txtFieldArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER || event.getCode() == DOWN){ 
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if(event.getCode()== UP ){
            event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
        }
    }
    
    private void txtDetail_KeyPressed(KeyEvent event){
        TextField txtDetail = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();
        
        if (lsValue == null) lsValue = "";
        
        JSONObject loJSON;
        
        if (event.getCode() == F3 || event.getCode() == ENTER){                    
            switch (lnIndex){
                case 3: 
                    if (event.getCode() == F3){
                        loJSON = poTrans.SearchDetail(pnRow, 3, lsValue, false, false);  

                        if (loJSON != null){
                            txtDetail03.setText((String) loJSON.get("sTransNox"));
                            CommonUtils.SetNextFocus(txtDetail);
                        }
                    } else {
                        if (!lsValue.isEmpty()){
                            loJSON = poTrans.SearchDetail(pnRow, 3, lsValue, false, false);  

                            if (loJSON != null){
                                txtDetail03.setText((String) loJSON.get("sTransNox"));
                                CommonUtils.SetNextFocus(txtDetail);
                            }
                        } else {
                            poTrans.setDetail(pnRow, 3, "");
                            CommonUtils.SetNextFocus(txtDetail);
                        }
                    }
                    break;
                case 4:
                    loJSON = poTrans.SearchDetail(pnRow, 4, lsValue, false, false);
                    if (loJSON != null){
                        psBarCodex = (String) loJSON.get("sBarCodex");
                        psDescript = (String) loJSON.get("sDescript");
                        txtDetail04.setText(psBarCodex);
                        txtDetail80.setText(psDescript);
                        loadDetail();
                    }
                    break;
                case 80:
                    loJSON = poTrans.SearchDetail(pnRow, 4, lsValue, true, false);
                    if (loJSON != null){
                        psBarCodex = (String) loJSON.get("sBarCodex");
                        psDescript = (String) loJSON.get("sDescript");
                        txtDetail04.setText(psBarCodex);
                        txtDetail80.setText(psDescript);
                        loadDetail();
                    }
                    break;
            }
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
            pnRow = poTrans.getDetailCount() - 1;
            pnOldRow = pnRow;
            
            loadDetail();
            setDetailInfo();
        }
    }  
    
    private void loadDetail(){
        int lnCtr;
        int lnRow = poTrans.getDetailCount();
        
        data.clear();
        /*ADD THE DETAIL*/
        
        Inventory loInventory;
        String lsOldCode = "";
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){           
            if (!"".equals((String) poTrans.getDetail(lnCtr, "sStockIDx"))) {
                loInventory = poTrans.GetInventory((String) poTrans.getDetail(lnCtr, "sStockIDx"), true, false);
                psBarCodex = (String) loInventory.getMaster("sBarCodex");
                psDescript = (String) loInventory.getMaster("sDescript");
                psMeasurNm = loInventory.getMeasureMent((String) loInventory.getMaster("sMeasurID"));
                
                if (!"".equals((String) poTrans.getDetail(lnCtr, "sReplacID"))){
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
                            String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                            CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString()), "#,##0.00"),
                            CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nFreightx").toString()), "#,##0.00"),
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
                            String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                            CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString()), "#,##0.00"),
                            CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nFreightx").toString()), "#,##0.00"),
                            CommonUtils.NumberFormat(((Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()))
                                * Double.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce").toString()))
                                + Double.valueOf(poTrans.getDetail(lnCtr, "nFreightx").toString()), "#,##0.00")));
            }
        }

        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()){
            table.getSelectionModel().select(pnRow);
            table.getFocusModel().focus(pnRow); 
            pnRow = table.getSelectionModel().getSelectedIndex();           
            
            setDetailInfo();
        }
        
        Label09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()) + Double.valueOf(poTrans.getMaster(11).toString()), "#,##0.00"));
        txtField11.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(11).toString()), "#,##0.00"));
    }    
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private final String pxeModuleName = "POReceivingController";
    private static GRider poGRider;
    private XMPOReceiving poTrans;
    
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    
    private final String pxeDateFormat = "yyyy-MM-dd";
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
    
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/            
            switch (lnIndex){
                case 16: /*sRemarksx*/
                    if (lsValue.length() > 256) lsValue = lsValue.substring(0, 256);
                    
                    poTrans.setMaster(lnIndex, CommonUtils.TitleCase(lsValue));
                    txtField.setText((String)poTrans.getMaster(lnIndex));
            }
            pnIndex = lnIndex;
        }else{ 
            pnIndex = -1;
            txtField.selectAll();
        }
    };
    
    final ChangeListener<? super Boolean> txtDetail_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtDetail = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();
        
        if (pnRow < 0) return;
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/     
            switch (lnIndex){
                case 3: /*Order No*/
                case 5: /*Replaced ID*/
                    break;
                case 4: /*Barcode*/
                    if (poTrans.getDetail(pnRow, "sStockIDx").equals("")){
                        poTrans.setDetail(pnRow, 100, txtDetail04.getText());
                    }
                    break;
                case 80: /*Description*/
                    //send the barcode and descript to class if it has no stock id
                    if (poTrans.getDetail(pnRow, "sStockIDx").equals("")){                        
//                        if (txtDetail80.getText().equals("")){
//                            ShowMessageFX.Warning(null, "Warning", "Description must have a value if stock is not existing.");
//                            return;
//                        }
                        poTrans.setDetail(pnRow, 101, txtDetail80.getText());
                    }
                    break;
                case 7: /*Quantity*/
                    Number lnValue = 0;
                    try {
                        /*this must be numeric*/
                        lnValue = Double.valueOf(lsValue);
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtDetail.requestFocus(); 
                    }
                    poTrans.setDetail(pnRow, lnIndex, lnValue);
                    break;
                case 8: /*nUnitPrce*/
                case 9: /*nFreightx*/
                    double x = 0.00;
                    try {
                        /*this must be numeric*/
                        x = Double.parseDouble(lsValue);
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtDetail.requestFocus(); 
                    }
                    poTrans.setDetail(pnRow, lnIndex, x);
                    break;
                 case 10: /*dExpiryDt*/
                    if (CommonUtils.isDate(txtDetail.getText(), pxeDateFormat)){
                        poTrans.setDetail(pnRow, "dExpiryDt", CommonUtils.toDate(txtDetail.getText()));
                    }else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                        poTrans.setDetail(pnRow, "dExpiryDt",CommonUtils.toDate(pxeDateDefault));
                    }
                    return;
            }
            pnOldRow = table.getSelectionModel().getSelectedIndex();
            pnIndex= lnIndex;
        } else{
            switch (lnIndex){
                case 10: /*dExpiryDt*/
                    try{
                        txtDetail.setText(CommonUtils.xsDateShort(lsValue));
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtDetail.selectAll();
                    break;
                default:
            }
            pnIndex = -1;
            txtDetail.selectAll();
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
                case 5: /*sSupplier*/
                case 17: /*sSourceNo*/
                case 29: /*sDeptIDxx*/
                    return;
                case 6: /*sReferNox*/
                    if(txtField.getText().equals("")){
                       txtField.setText("");
                       poTrans.setMaster(lnIndex, "");
                    }
                    
                    if (txtField.getText().length() > 8){
                        ShowMessageFX.Warning("Max characters for `Reference No.` exceeds the limit.", pxeModuleName, "Please verify your entry.");
                        txtField.requestFocus();
                        return;
                    }
                    poTrans.setMaster(lnIndex, txtField.getText());
                    return;
                case 8: /*sTermCode*/
                case 20: /*sInvTypCd*/
                    return;
                case 3: /*dTransact*/
                case 7: /*dRefernce*/
                    if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
                        poTrans.setMaster(lnIndex, CommonUtils.toDate(txtField.getText()));
                    } else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 1991-07-07)");
                        poTrans.setMaster(lnIndex, CommonUtils.toDate(pxeDateDefault));
                    }
                    return;
                case 10: /*nVATRatex*/
                case 11: /*nTWithHld*/
                    break;
                case 12: /*nDiscount*/
                    double x = 0.00;
                    try {
                        /*this must be numeric*/
                        x = Double.parseDouble(lsValue);
                    } catch (Exception e) {
                        x = 0.00;
                        txtField.setText("0.00");
                    }
                    poTrans.setMaster(lnIndex, Double.parseDouble(txtField.getText()));
                    break;
                case 13: /*nAddDiscx*/
                case 14: /*nAmtPaidx*/
                case 15: /*nFreightx*/
                    double y = 0.00;
                    try {
                        /*this must be numeric*/
                        y = Double.parseDouble(lsValue);
                    } catch (Exception e) {
                        y = 0.00;
                        txtField.setText("0.00");
                        
                    }
                    poTrans.setMaster(lnIndex, Double.parseDouble(txtField.getText()));
                    break;
                case 50:
                case 51:
                    return;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
            pnIndex = lnIndex;
        } else{
            switch (lnIndex){
                case 3: /*dTransact*/
                case 7: /*dRefernce*/
                    try{
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtField.selectAll();
                    break;
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
                case 7:
                    txtDetail07.setText(String.valueOf(poTrans.getDetail(pnRow,7)));
                    loadDetail();
                        
                    poTrans.addDetail(); //pass psOrderNox here
                    
                    pnRow = poTrans.getDetailCount() - 1;

                    loadDetail();
                    if (txtDetail04.getText().isEmpty()){
                        txtDetail04.requestFocus();
                        txtDetail04.selectAll();
                    } else{
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
                    txtDetail10.setText(CommonUtils.xsDateMedium((Date) poTrans.getDetail(pnRow, fnIndex)));
                    loadDetail();
                    break;                
            }
        }
    };
    
    private void getMaster(int fnIndex){
        switch(fnIndex){
        case 5:
            JSONObject loSupplier = poTrans.GetSupplier((String)poTrans.getMaster(fnIndex), true);
            if (loSupplier != null) txtField05.setText((String) loSupplier.get("sClientNm"));
            break;
        case 6:
            txtField06.setText((String)poTrans.getMaster(fnIndex));
            break;
        case 8:
            XMTerm loTerm = poTrans.GetTerm((String)poTrans.getMaster(fnIndex), true);
            if (loTerm != null) txtField08.setText((String) loTerm.getMaster("sDescript"));
            break;
        case 3:
            txtField03.setText(CommonUtils.xsDateLong((Date)poTrans.getMaster(fnIndex)));
            break;
        case 7:
            txtField07.setText(CommonUtils.xsDateLong((Date)poTrans.getMaster(fnIndex)));
            break;
        case 12:
            txtField12.setText(CommonUtils.NumberFormat((Double)poTrans.getMaster(fnIndex), "0.00"));
            loadDetail();
            break;
        case 13:
            txtField13.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(fnIndex).toString()), "0.00"));
            loadDetail();
            break;
        case 14:
            txtField14.setText(CommonUtils.NumberFormat((Double)poTrans.getMaster(fnIndex), "0.00"));
            loadDetail();
            break;    
        case 15:
            txtField15.setText(CommonUtils.NumberFormat((Double)poTrans.getMaster(fnIndex), "0.00"));
            loadDetail();
            break;
        case 17:
            txtField17.setText((String) poTrans.getMaster(fnIndex));
            break;
        case 9:
            Label09.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster(9).toString()), "#,##0.00"));      
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
}