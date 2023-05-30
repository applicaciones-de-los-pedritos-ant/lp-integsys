package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
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
import org.rmj.cas.parameter.agent.XMBranch;
import org.rmj.cas.parameter.agent.XMInventoryType;
import org.rmj.cas.parameter.agent.XMTerm;
import org.rmj.purchasing.agent.XMPurchaseOrder;


public class PurchaseOrderRegController implements Initializable {
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private TextField txtField03;
    @FXML private TextField txtField02;
    @FXML private TextField txtField05;
    @FXML private TextField txtField06;
    @FXML private TextField txtField07;
    @FXML private TextField txtField08;
//    @FXML private TextField txtField16;
    @FXML private Label Label09;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail04,txtDetail05,txtDetail06;
    @FXML private TextField txtDetail80;
    @FXML private TableView table;
    @FXML private TextArea txtField10;
    @FXML private ImageView imgTranStat;
    @FXML private TextField txtField01;
    @FXML private Button btnClose;
    @FXML private Button btnBrowse;
    @FXML private Button btnVoid;
    @FXML private Button btnPrint;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private AnchorPane dataPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        poTrans = new XMPurchaseOrder(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(123);
        poTrans.setClientNm(System.getProperty("user.name"));
        
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnVoid.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        
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
    
 private void loadDetail(){
        int lnCtr;
        int lnRow = poTrans.getDetailCount();
        
        data.clear();
        /*ADD THE DETAIL*/
        
        Inventory loInventory;
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            if (!"".equals((String) poTrans.getDetail(lnCtr, "sStockIDx"))) {
                loInventory = poTrans.GetInventory((String) poTrans.getDetail(lnCtr, "sStockIDx"), true, false);
            
                data.add(new TableModel(String.valueOf(lnCtr + 1), 
                                        (String) loInventory.getMaster("sBarCodex"), 
                                        (String) loInventory.getMaster("sDescript"), 
                                        loInventory.getMeasureMent((String) loInventory.getMaster("sMeasurID")),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce")),
                                        "",
                                        "",
                                        "",
                                        ""));
            } else {
                data.add(new TableModel(String.valueOf(lnCtr + 1), 
                                        (String) poTrans.getDetail(lnCtr, 100), 
                                        (String) poTrans.getDetail(lnCtr, 101), 
                                        (String) poTrans.getDetail(lnCtr, 102),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                        String.valueOf(poTrans.getDetail(lnCtr, "nUnitPrce")),
                                        "",
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
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Bar Code");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("M.");
        TableColumn index05 = new TableColumn("Qty");
        TableColumn index06 = new TableColumn("UPrice");
        
        index01.setPrefWidth(30);
        index02.setPrefWidth(110);
        index03.setPrefWidth(130);
        index04.setPrefWidth(75);
        index05.setPrefWidth(45); index05.setStyle("-fx-alignment: CENTER-RIGHT;");
        index06.setPrefWidth(85); index06.setStyle("-fx-alignment: CENTER-RIGHT;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);
        index06.setSortable(false); index06.setResizable(false);

        table.getColumns().clear();        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index06"));

        /*Set data source to table*/
        table.setItems(data);
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        switch (lsButton){
            case "btnClose":
            case "btnExit": 
                unloadForm();
                return;
               
            case "btnPrint":
                if(!psOldRec.equals("")){
                    if (poTrans.printRecord()){
                        clearFields();
                        initGrid();
                        pnEditMode = EditMode.UNKNOWN;
                    }
                }else 
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
                
                break;
            case "btnBrowse":              
                if (pnIndex == 50){
                    if(poTrans.BrowseRecord(txtField50.getText(), true)){
                        loadRecord();
                    }
                } else {
                    if(poTrans.BrowseRecord(txtField51.getText(), false)){
                        loadRecord();
                    }
                }
                
                pnEditMode = poTrans.getEditMode();    
                return;
            case "btnVoid":
               if (!psOldRec.equals("")){
                    if(!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)){
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }
                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to cancel this transaction?")==true){
                        if (poTrans.cancelRecord(psOldRec))
                        ShowMessageFX.Information(null, pxeModuleName, "Transaction CANCELLED successfully.");
                        clearFields();
                        initGrid();
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    }else
                        return;
                    
                } else ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to cancel!");
                break;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
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
        txtDetail04.setText("0");
        txtDetail80.setText("");
        
        Label09.setText("0.00");
        
        pnRow = -1;
        pnOldRow = -1;
        pnIndex = -1;
        
        psOldRec = "";
        
        psBarCodex = "";
        psDescript = "";
        
        setTranStat("-1");
        data.clear();
    }
    
    private void unloadForm(){
//        VBox myBox = (VBox) VBoxForm.getParent();
//        myBox.getChildren().clear();
        
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
        
    }
    
    
 public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private final String pxeModuleName = "PurchaseOrderController";
    private static GRider poGRider;
    private XMPurchaseOrder poTrans;
    
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    
    private final String pxeDateFormat = "yyyy-MM-dd";
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
    private String psReferNox = "";
    private String psBarCodex = "";
    private String psDescript = "";
    
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
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();        
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        
        switch (event.getCode()){
            case F3:
                switch (lnIndex){
                
//                case 16: /*sInvTypCd*/
//                    if (!poTrans.SearchMaster(lnIndex, txtField.getText(), false)==true){
//                        txtField.setText("");
//                    }
//                    break;
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
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/           
            switch (lnIndex){
                case 50: /*sReferNox*/
                     if(lsValue.equals("") || lsValue.equals("%")){
                       txtField.setText(psReferNox);
                    }else
                    txtField.setText(""); break;
                case 51: /*sSupplierId*/
                     if(lsValue.equals("") || lsValue.equals("%")){
                       txtField.setText("");
                    }else
                    txtField.setText(psSupplier); break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
            pnIndex = lnIndex;
        }
        
    };
    
}
