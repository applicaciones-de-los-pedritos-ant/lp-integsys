package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
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
import javafx.scene.layout.VBox;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.cas.inventory.base.InvTransfer;
import org.rmj.cas.parameter.agent.XMBranch;


public class InvTransferRegController implements Initializable {
    
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
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
    @FXML private Button btnVoid;
    @FXML private Button btnClose;
    @FXML private Button btnPrint;
    @FXML private Button btnBrowse;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private TextField txtOther02;
    @FXML private TextField txtDetail08;
    @FXML private AnchorPane dataPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvTransfer(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(1230);
        
        btnPrint.setOnAction(this::cmdButton_Click);
        btnVoid.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        
        pnEditMode = EditMode.UNKNOWN;
        clearFields();
        initGrid();
        
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
        txtDetail80.setText("");
        Label12.setText("0.00");
        
        pnRow = -1;
        pnOldRow = -1;
        pnIndex = 51;
        setTranStat("-1");
        
        psOldRec = "";
        psDestina = "";
        psTrukNme = "";
        psOrderNm = "";
        psOrderNox = "";
        psTransNox = "";
        
        data.clear();
    }
    
    private void initGrid(){
        
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Order No.");
        TableColumn index03 = new TableColumn("Barcode");
        TableColumn index04 = new TableColumn("Description");
        TableColumn index05 = new TableColumn("Old Code");
        TableColumn index06 = new TableColumn("Unit Price");
        TableColumn index07 = new TableColumn("Qty");
        TableColumn index08 = new TableColumn("Notes");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(110);
        index03.setPrefWidth(110);
        index04.setPrefWidth(180);
        index05.setPrefWidth(110);
        index06.setPrefWidth(80);
        index07.setPrefWidth(40); index07.setStyle("-fx-alignment: CENTER;");
        index08.setPrefWidth(140);index08.setStyle("-fx-alignment: CENTER-LEFT;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);
        index06.setSortable(false); index06.setResizable(false);
        index07.setSortable(false); index07.setResizable(false);
        index08.setSortable(false); index08.setResizable(false);

        table.getColumns().clear();        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        table.getColumns().add(index06);
        table.getColumns().add(index07);
        table.getColumns().add(index08);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index08"));
         
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
               
            case "btnPrint": 
               if(!psOldRec.equals("")){
                    if (!printTransfer()) return;

                    clearFields();
                    initGrid();
                    pnEditMode = EditMode.UNKNOWN;
                }else 
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
                    
                break;
            case "btnBrowse":
                switch(pnIndex){
                    case 50: /*sTransNox*/
                        if(poTrans.BrowseRecord("%" +txtField50.getText(), true)==true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }
                        
                        if(!txtField50.getText().equals(psTransNox)){
                            clearFields();
                            break;
                        }else txtField50.setText(psTransNox);
                        
                        return;
                    case 51: /*sDestination*/
                        if(poTrans.BrowseRecord(txtField51.getText() + "%", false)== true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }
                        
                        if(!txtField51.getText().equals(psDestina)){
                            clearFields();
                            break;
                        } else txtField51.setText(psDestina);
                        
                        return;
                    default:
                        ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                            txtField51.requestFocus();
                }
                
                return;
            case "btnVoid":
               if (!psOldRec.equals("")){
                    /*if(!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)){
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }*/
                    
                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to cancel this transaction?")==true){
                        if (poTrans.cancelTransaction(psOldRec)){
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction CANCELLED successfully.");
                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                        } else {
                            ShowMessageFX.Information(poTrans.getMessage(), pxeModuleName, poTrans.getErrMsg());
                        }
                    }
                    return;
                } else 
                   ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to cancel!");
                break;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
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
        pnOldRow = 0;
        loadDetail();
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
        
        data.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            data.add(new TableModel(String.valueOf(lnCtr + 1), 
                                    (String) poTrans.getDetail(lnCtr, "sOrderNox"),
                                    (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sDescript"),
                                    (String) poTrans.getDetailOthers(lnCtr, "sOrigCode"),
                                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nInvCostx").toString()), "0.00"),
                                    String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                    (String) poTrans.getDetail(lnCtr, "sNotesxxx"),
                                    "",
                                    ""));
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
            txtDetail10.setText(String.valueOf(poTrans.getDetail(pnRow, "sNotesxxx")));
            txtOther02.setText(String.valueOf(poTrans.getDetailOthers(pnRow, "nQtyOnHnd")));
            txtDetail08.setText(CommonUtils.xsDateMedium((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
        } else{
            txtDetail03.setText("");
            txtDetail04.setText("");
            txtDetail05.setText("");
            txtDetail06.setText("0");
            txtDetail07.setText("0.00");
            txtDetail10.setText("");
            txtDetail80.setText("");
            txtOther02.setText("0");
            txtDetail08.setText("0");
        }
    }
    
    private boolean printTransfer(){        
        JSONArray json_arr = new JSONArray();
        json_arr.clear();
        
        for(int lnCtr = 0; lnCtr <= poTrans.ItemCount()-1; lnCtr ++){
            JSONObject json_obj = new JSONObject();
            json_obj.put("sField01", (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"));
            json_obj.put("sField02", (String) poTrans.getDetailOthers(lnCtr, "sDescript"));
            json_obj.put("sField05", (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"));
            json_obj.put("nField01", (Double) poTrans.getDetail(lnCtr, "nQuantity"));
            json_obj.put("lField01", Double.valueOf(poTrans.getDetail(lnCtr, "nInvCostx").toString()));
            json_arr.add(json_obj);
        }
        
        String lsSQL = "SELECT sBranchNm FROM Branch WHERE sBranchCD = " + SQLUtil.toSQL((String) poTrans.getMaster("sDestinat"));
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        try {
            if (loRS.next())
                lsSQL = loRS.getString("sBranchNm");
            else
                lsSQL = (String) poTrans.getMaster("sDestinat");
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Unable to print...");
            System.exit(1);
        }
        
        //Create the parameter
        Map<String, Object> params = new HashMap<>();
        params.put("sCompnyNm", "Los Pedritos");  
        params.put("sBranchNm", poGRider.getBranchName());
        params.put("sDestinat", lsSQL);
        
        params.put("sAddressx", poGRider.getAddress());
        params.put("sReportNm", "Inventory Transfer");
        params.put("sTransNox", poTrans.getMaster("sTransNox").toString().substring(1));
        params.put("sReportDt", CommonUtils.xsDateMedium((Date)poTrans.getMaster("dTransact")));
        params.put("sPrintdBy", System.getProperty("user.name"));
                
        try {
            InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
            JsonDataSource jrjson = new JsonDataSource(stream); 
            
            JasperPrint _jrprint = JasperFillManager.fillReport("d:/GGC_Java_Systems/reports/InvTransferPrint.jasper", params, jrjson);
            JasperViewer jv = new JasperViewer(_jrprint, false);     
            jv.setVisible(true);
            jv.setAlwaysOnTop(true);
        } catch (JRException | UnsupportedEncodingException  ex) {
            Logger.getLogger(InvTransferRegController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
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
    
    private int pnIndex = -1;
    private int pnRow = -1;
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
                case 50: /*sTransNox*/
                    if(event.getCode() == F3) lsValue = "%" + txtField.getText();
                        if(poTrans.BrowseRecord(lsValue, true)==true){
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
                        if(poTrans.BrowseRecord(lsValue, false)== true){
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
                     
                case 51: /*sSupplierId*/
                    if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                    break;
                    
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
            pnIndex = lnIndex;
        }
        
    };
    
}
