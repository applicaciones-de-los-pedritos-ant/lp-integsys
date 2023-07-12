
package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import org.rmj.appdriver.constants.UserRight;
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
import javafx.scene.layout.VBox;
import org.json.simple.JSONObject;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.cas.inventory.base.InvTransfer;
import org.rmj.lp.parameter.agent.XMBranch;

public class InvTransPostingController implements Initializable {

    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01;
    @FXML private TextField txtField03;
    @FXML private TextField txtField04;
    @FXML private TextField txtField18;
    @FXML private TextField txtField06;
    @FXML private TextField txtField07;
    @FXML private TextField txtField13;
    @FXML private TextArea txtField05;
    @FXML private Label Label12;
    @FXML private TextField txtDetail05;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail80;
    @FXML private TextArea txtDetail10;
    @FXML private TextField txtDetail04;
    @FXML private TextField txtDetail07;
    @FXML private TextField txtDetail06;
    @FXML private TableView table;
    @FXML private ImageView imgTranStat;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private Button btnPost;
    @FXML private Button btnClose;
    @FXML private Button btnBrowse;
    @FXML private TextField txtOther02;
    @FXML private TextField txtField19;
    @FXML private TextField txtDetail09;
    @FXML private TableView tableData;
    @FXML private AnchorPane dataPane;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvTransfer(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(10);
        
        btnPost.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtField19.focusedProperty().addListener(txtField_Focus);
        
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        txtField19.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtDetail09.focusedProperty().addListener(txtDetail_Focus);
        txtDetail09.setOnKeyPressed(this::txtDetail_KeyPressed);
        
        pnEditMode = EditMode.UNKNOWN;
        clearFields();
        initGrid();
        initGridView();
        
        pbLoaded = true;
    }

    private void clearFields(){
        txtField01.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("0.00");
        txtField13.setText("0.00");
        txtField50.setText("");
        txtField51.setText("");
        txtOther02.setText("0");
        
        txtDetail03.setText("");
        txtDetail04.setText("");
        txtDetail05.setText("");
        txtDetail07.setText("0.00");
        txtDetail06.setText("0");
        txtDetail09.setText("0");
        txtDetail80.setText("");
        Label12.setText("0.00");
        
        pnRow = -1;
        pnExp = -1;
        pnOldRow = -1;
        pnIndex = 51;
        pnExpTotl=0;
        setTranStat("-1");
        
        psOldRec = "";
        psDestina = "";
        psTrukNme = "";
        psOrderNm = "";
        psOrderNox = "";
        psTransNox = "";
        txtField19.setEditable(false);
        txtDetail09.setEditable(false);
        data.clear();
        dataDetail.clear();
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Brand");
        TableColumn index05 = new TableColumn("Measure");
        TableColumn index06 = new TableColumn("Total Qty");

        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90); index02.setStyle("-fx-alignment: CENTER;");
        index03.setPrefWidth(120); index03.setStyle("-fx-alignment: CENTER;");
        index04.setPrefWidth(150); index04.setStyle("-fx-alignment: CENTER;");
        index05.setPrefWidth(65); index05.setStyle("-fx-alignment: CENTER;");
        index06.setPrefWidth(80); index06.setStyle("-fx-alignment: CENTER;");
        
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
    
    private void initGridView(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Description");
        TableColumn index03 = new TableColumn("Expiration");
        TableColumn index04 = new TableColumn("Qty");
        TableColumn index05 = new TableColumn("Qty Rcvd");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(150); index02.setStyle("-fx-alignment: CENTER;");
        index03.setPrefWidth(80); index03.setStyle("-fx-alignment: CENTER;");
        index04.setPrefWidth(75); index04.setStyle("-fx-alignment: CENTER;");
        index05.setPrefWidth(75); index05.setStyle("-fx-alignment: CENTER;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);

        tableData.getColumns().clear();        
        tableData.getColumns().add(index01);
        tableData.getColumns().add(index02);
        tableData.getColumns().add(index03);
        tableData.getColumns().add(index04);
        tableData.getColumns().add(index05);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));
        
         /*making column's position uninterchangebale*/
        tableData.widthProperty().addListener(new ChangeListener<Number>() {  
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
        tableData.setItems(dataDetail);
    }
    
    private void unloadForm(){
//        VBox myBox = (VBox) VBoxForm.getParent();
//        myBox.getChildren().clear();
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }
    
    private void cmdButton_Click(ActionEvent event) {
         String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
                
            case "btnClose":
            case "btnExit": 
                unloadForm();
                return;
                
            case "btnBrowse":
                switch(pnIndex){
                   case 50: /*sTransNox*/
                    if(poTrans.BrowseAcceptance(txtField50.getText(), true)==true){
                        txtField19.setText(CommonUtils.xsDateMedium(poGRider.getServerDate()));
                        loadRecord(); 
                        pnEditMode = poTrans.getEditMode();
                        break;
                    }else
                        if(!txtField50.getText().equals(psTransNox)){
                            clearFields();
                            break;
                        }else{
                            txtField50.setText(psTransNox);
                        }
                    return;
                     
                case 51: /*sDestination*/
                    if(poTrans.BrowseAcceptance(txtField51.getText() + "%", false)== true){
                        txtField19.setText(CommonUtils.xsDateMedium(poGRider.getServerDate()));
                        loadRecord(); 
                        pnEditMode = poTrans.getEditMode();
                        break;
                    }if(!txtField51.getText().equals(psDestina)){
                        clearFields();
                        break;
                        }else{
                            txtField51.setText(psDestina);
                                 }
                    return;
                    
                    default:
                          ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                          txtField51.requestFocus();
                    }
                return;
                
            case "btnPost":
                setDetail();
               if (psOldRec.equals("")){
                   ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to post!");
                   return;
               }
                if(!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN) && !poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_CLOSED)){
                    ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                    return;
               }          
                   if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to post this transaction?")==true){
                    if(pnDetTotl!= pnExpTotl){
                        if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to post this transaction?")==true){
                            if (poGRider.getUserLevel() <= UserRight.ENCODER){
                                JSONObject loJSON = showFXDialog.getApproval(poGRider);
                                if (loJSON == null){
                                    ShowMessageFX.Warning("Approval failed.", pxeModuleName, "Unable to post transaction");
                                    return;
                                }
                                if ((int) loJSON.get("nUserLevl") <= UserRight.ENCODER){
                                    ShowMessageFX.Warning("User account has no right to approve.", pxeModuleName, "Unable to post transaction");
                                    return;
                                }
                            }
                        }else return;
                    }
                   try {
                       if (poTrans.postTransaction(psOldRec,toDate(CommonUtils.xsDateShort(txtField19.getText())))){
                           ShowMessageFX.Information(null, pxeModuleName, "Transaction POSTED successfully.");
                           clearFields();
                           initGrid();
                           initGridView();
                           pnEditMode = EditMode.UNKNOWN;
                       }
                   } catch (ParseException ex) {
                       Logger.getLogger(InvTransPostingController.class.getName()).log(Level.SEVERE, null, ex);
                   }
               }
                break;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }
    
    private void setDetail(){
        int lnCtr = 0;
        int lnCtr1 = 0;
        double lnReceived =0;
        
        for(lnCtr = 0; lnCtr <= poTrans.ItemCount() -1; lnCtr++){
            lnReceived = 0;
             for(lnCtr1 = 0; lnCtr1 <= poTrans.ItemCountExp() -1;lnCtr1++){
                 if(lnCtr + 1 == (int) poTrans.getDetailExp(lnCtr1, "nEntryNox")){
                     lnReceived = lnReceived + (double) poTrans.getDetailExp(lnCtr1, "nReceived");
                 }
             }
//           poTrans.setDetail(lnCtr, "nReceived", lnReceived);
        }
               
        
//        for (int a= 0; a<=poTrans.ItemCount() -1; a++){
//            System.err.println(poTrans.getDetail(a, "nReceived"));
//        }
    }
    
    public Date toDate(String fsDate){
        Date ldDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
         ldDate = formatter.parse(fsDate);
         return ldDate;
        }catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    @FXML
    private void table_Clicked(MouseEvent event) {
        setDetailInfo();
    }
    
    private void loadRecord(){
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField50.setText((String) poTrans.getMaster("sTransNox"));
        psTransNox = txtField50.getText();
        
        XMBranch loBranch = poTrans.GetBranch((String)poTrans.getMaster(4), true);
        if (loBranch != null) {
            txtField04.setText((String) loBranch.getMaster("sBranchNm"));
            txtField51.setText((String) loBranch.getMaster("sBranchNm"));
        }
        
        //TODO:
        // Order No. and Truck
        txtField18.setText("");
        txtField06.setText("");
        
        txtField03.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
        psDestina = txtField51.getText();
        txtField05.setText((String) poTrans.getMaster("sRemarksx"));
       
        txtField07.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nFreightx").toString()), "0.00"));
        txtField13.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nDiscount").toString()), "0.00"));
        
        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nTranTotl").toString()), "#,##0.00"));
        
        pnRow = 0;
        pnExp = 0;
        pnOldRow = 0;
        loadDetail();
        loadExpiration();
        
        if (poTrans.getMaster("cTranStat").equals("1")){
            txtField19.setEditable(true);
            txtDetail09.setEditable(true);
            txtField19.requestFocus();
        }
        
        setTranStat((String) poTrans.getMaster("cTranStat"));
        psOldRec = txtField01.getText();
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
        pnDetTotl = 0;
        data.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            data.add(new TableModel(String.valueOf(lnCtr + 1), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sDescript"), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sBrandNme"), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"), 
                                    String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),    
                                    "",
                                    "",
                                    "",
                                    ""));
        pnDetTotl = pnDetTotl + (double) poTrans.getDetail(lnCtr, "nQuantity");
        }
    
        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()){
            table.getSelectionModel().select(lnRow -1);
            table.getFocusModel().focus(lnRow -1);
            
            pnRow = table.getSelectionModel().getSelectedIndex();           
            
            setDetailInfo();
        }
        
        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nTranTotl").toString()), "#,##0.00"));
    }
    
    private void loadExpiration(){
        int lnCtr;
        int lnRow = poTrans.ItemCountExp();
        pnExpTotl = 0;
        
        dataDetail.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            poTrans.setDetailExp(lnCtr, "nReceived", poTrans.getDetailExp(lnCtr, "nQuantity"));
            dataDetail.add(new TableModel(String.valueOf(poTrans.getDetailExp(lnCtr, "nEntryNox")), 
                                    (String) poTrans.getDetailExp(lnCtr, "sDescript"), 
                                    String.valueOf(CommonUtils.xsDateShort((Date)poTrans.getDetailExp(lnCtr, "dExpiryDt"))),
                                    String.valueOf(poTrans.getDetailExp(lnCtr, "nQuantity")),    
                                    String.valueOf(poTrans.getDetailExp(lnCtr, "nReceived")),
                                    "",
                                    "",
                                    "",
                                    "",
                                    ""));
            pnExpTotl = pnExpTotl + (double) poTrans.getDetailExp(lnCtr, "nQuantity");
        }
        
        /*FOCUS ON FIRST ROW*/
        if (!dataDetail.isEmpty()){
            tableData.getSelectionModel().select(lnRow -1);
            tableData.getFocusModel().focus(lnRow -1);
            
            pnExp = tableData.getSelectionModel().getSelectedIndex();           
            
//            setExpiryInfo(pnExp);
        }        
        
//        Label12.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getMaster("nTranTotl").toString()), "#,##0.00"));
    }
    
    private void setDetailInfo(){
        int lnRow = table.getSelectionModel().getSelectedIndex();
        pnRow = lnRow;
        if (pnRow >= 0){
            txtDetail05.setText(String.valueOf(poTrans.getDetail(pnRow, "sOrderNox")));
            txtDetail03.setText((String) poTrans.getDetailOthers(pnRow, "sBarCodex"));
            txtDetail80.setText((String) poTrans.getDetailOthers(pnRow, "sDescript"));
            txtDetail04.setText((String) poTrans.getDetailOthers(pnRow, "sOrigCode"));
            txtDetail07.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, "nInvCostx").toString()), "0.00"));
            txtDetail06.setText(String.valueOf(poTrans.getDetail(pnRow, "nQuantity")));
            txtDetail09.setText(String.valueOf(poTrans.getDetail(pnRow, "nReceived")));
            txtDetail10.setText(String.valueOf(poTrans.getDetail(pnRow, "sNotesxxx")));
            txtOther02.setText(String.valueOf(poTrans.getDetailOthers(pnRow, "nQtyOnHnd")));
        } else{
            txtDetail03.setText("");
            txtDetail04.setText("");
            txtDetail05.setText("");
            txtDetail06.setText("0");
            txtDetail07.setText("0.00");
            txtDetail09.setText("0");
            txtDetail10.setText("");
            txtDetail80.setText("");
            txtOther02.setText("0");
        }
    }
    
//    private void setExpiryInfo(int fnRow){
//        pnExp = fnRow;
//        if (pnExp >= 0){
//            txtDetail08.setText(CommonUtils.xsDateLong((Date)poTrans.getDetailExp(pnExp, "dExpiryDt")));
//            txtDetail09.setText(String.valueOf(poTrans.getDetailExp(pnExp, "nReceived")));
//        }else{
//            txtDetail08.setText("");
//            txtDetail09.setText("0");
//        }
//        
//        if(poTrans.getDetailExp(pnExp, "nReceived").toString().equals("0")){
//            txtDetail09.setText(String.valueOf((double)poTrans.getDetailExp(pnExp, "nQuantity")));
//            txtDetail09.requestFocus();
//        }
//    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    private final String pxeModuleName = "InvTransferController";
    private static GRider poGRider;
    private InvTransfer poTrans;
    
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeDateDefault = "1900-01-01";
    
    private TableModel model;
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private ObservableList<TableModel> dataDetail = FXCollections.observableArrayList();
    
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnExp = -1;
    private double pnExpTotl = 0;
    private double pnDetTotl = 0;
    private int pnOldRow = -1;
    
    private String psDestina = "";
    private String psTransNox = "";
    private String psTrukNme = "";
    private String psOrderNm = "";
    
    private String psOldRec = "";
    private String psOrderNox = "";
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (event.getCode() == ENTER || event.getCode() == F3){
            switch (lnIndex){
                case 19:
                    txtDetail09.requestFocus();
                    break;
                case 50: /*sTransNox*/
                    if(event.getCode() == F3) lsValue =  txtField.getText();
                    if(poTrans.BrowseAcceptance(lsValue, true)==true){
                       loadRecord(); 
                       pnEditMode = poTrans.getEditMode();
                       break;
                    }else
                        if(!txtField50.getText().equals(psTransNox)){
                            clearFields();
                            break;
                            }else{
                                txtField50.setText(psTransNox);
                                     }
                    return;
                     
                case 51: /*psDestina*/
                    if(event.getCode() == F3) lsValue = txtField.getText() + "%";
                    if(poTrans.BrowseAcceptance(lsValue, false)== true){
                        loadRecord(); 
                        pnEditMode = poTrans.getEditMode();
                        break;
                    } else {
                        ShowMessageFX.Warning(poTrans.getMessage(), pxeModuleName, "Please inform MIS Department");
                    }
                    
                    if(!txtField51.getText().equals(psDestina)){
                        clearFields();
                        break;
                    }else{
                        txtField51.setText(psDestina);
                    }
                    
                    return;
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
    
    private void txtDetail_KeyPressed(KeyEvent event){
        TextField txtDetail = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
//        if (event.getCode() == ENTER || event.getCode() == F3){
//            switch (lnIndex){
//                case 9:
//                    loadExpiration();
//                    break;
//            }
//        }
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(txtDetail);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtDetail);
        }
    }
    
    final ChangeListener<? super Boolean> txtDetail_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtDetail = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();
        
        if (pnExp < 0) return;
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/     
            switch (lnIndex){
                case 9:/*nReceived*/
                    double y = 0;
                    try {
                        /*this must be numeric*/
                        y = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        y = 0;
                    }
                    poTrans.setDetailExp(pnExp,"nReceived", y);
                    txtDetail.setText(poTrans.getDetailExp(pnExp, "nReceived").toString());
                    loadExpiration();
                    break;
                case 8:
                    if (CommonUtils.isDate(txtDetail.getText(), pxeDateFormat)){
                        poTrans.setDetailExp(pnExp, "dExpiryDt", CommonUtils.toDate(txtDetail.getText()));
                    }else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 07-07-1991)");
                        poTrans.setDetailExp(pnExp, "dExpiryDt",CommonUtils.toDate(pxeDateDefault));
                    }
                    txtDetail.setText(CommonUtils.xsDateMedium((Date)poTrans.getDetailExp(pnExp, "dExpiryDt")));
                    break;
                        
            }
            pnOldRow = tableData.getSelectionModel().getSelectedIndex();
            pnIndex= lnIndex;
        } else{
            switch (lnIndex){
                case 8: /*dExpiryDt*/
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
                case 50: /*sTransNox*/
                    if(lsValue.equals("") || lsValue.equals("%"))
                        txtField.setText("");
                        break;
                case 51: /*sDestination*/
                   if(lsValue.equals("") || lsValue.equals("%"))
                        txtField.setText("");
                        break;
                case 19: /*dReceived*/
                    if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
                        txtField.setText(CommonUtils.xsDateMedium(CommonUtils.toDate(txtField.getText())));
                    }else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 07-07-1991)");
                        txtField.setText(CommonUtils.xsDateMedium(CommonUtils.toDate(pxeDateDefault)));
                    }
                    return;
            }
            pnOldRow = table.getSelectionModel().getSelectedIndex();
            pnIndex= lnIndex;
        } else{
            switch (lnIndex){
                case 19: /*dReceived*/
                    try{
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtField.selectAll();
                    break;
                default:
            }
            pnIndex = -1;
            txtField.selectAll();
        }
        
    };

    @FXML
    private void tableData_Clicked(MouseEvent event) {
        int lnRow = tableData.getSelectionModel().getSelectedIndex();
//        setExpiryInfo(lnRow);
    }
    
}
