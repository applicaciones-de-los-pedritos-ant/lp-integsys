
package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
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
import javafx.scene.input.KeyCode;
import static javafx.scene.input.KeyCode.DOWN;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import static javafx.scene.input.KeyCode.UP;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JsonDataSource;
import net.sf.jasperreports.view.JasperViewer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.appdriver.constants.UserRight;
import org.rmj.cas.inventory.production.base.ProductionRequest;

public class InvProdRequestRegController implements Initializable {
       @FXML
    private AnchorPane dataPane,anchorField;

    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private Label lblHeader;
    @FXML
    private TextArea txtField07;
    @FXML
    private TextField txtField01, txtField02, txtDetail01, txtDetail02, txtDetail05, txtDetail07, txtField50;
    @FXML
    private TableView table;
    @FXML
    private TableColumn index01,index02,index03,index04,index05;
    @FXML
    private ImageView imgTranStat;
    @FXML
    private Button btnClose,btnPrint,btnBrowse;

    
    private final String pxeModuleName = "InvProdRequestController";
    protected Date pdExpiryDt = null;
    protected Boolean pbEdited = false;
    private static GRider poGRider;
    private ProductionRequest poTrans;
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();
    
    private TableModel model;
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;
    
    private String psTransNox = "";
    private String psOldRec = "";
    private boolean pbFound;
    private int pnlRow=0;
    @FXML
    private TextField txtField11;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new ProductionRequest(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(1234);
        
        
        btnPrint.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
            
                        
        /*Add keypress event for field with search*/
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtFieldArea_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail07.setOnKeyPressed(this::txtDetail_KeyPressed);

        
        txtDetail05.setDisable(true);
        
        pnEditMode = EditMode.UNKNOWN;
        clearFields();
        initGrid();
        txtField50.requestFocus();
        
        pbLoaded = true;
    }
    
    public void setGRider(GRider foGRider){
        this.poGRider = foGRider;
    }
    
    
    private void clearFields(){
        txtField01.setText("");
        txtField02.setText("");
        txtField07.setText("");
        txtField11.setText("");
        txtField50.setText("");
        
        pbFound = false;
        txtDetail01.setText("");
        txtDetail02.setText("");
        txtDetail05.setText("0");
        txtDetail07.setText("0");
        
        pnlRow = 0;
        pnRow = -1;
        pnOldRow = -1;
        pnIndex = 51;
        setTranStat("-1");
        
        psOldRec = "";
        psTransNox = "";
        pbEdited = false;
        
        data.clear();
    }
    
    private void initGrid(){
       index01.setStyle("-fx-alignment: CENTER;");
            index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
            index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
            index04.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");
            index05.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");


        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));

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
    
    private void txtFieldArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER || event.getCode() == DOWN){ 
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if (event.getCode() ==KeyCode.UP){
        event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
        }
    }
    
     private void txtDetailArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER || event.getCode() == KeyCode.DOWN){
            event.consume();
            CommonUtils.SetNextFocus((TextArea)event.getSource());
        }else if (event.getCode() ==KeyCode.UP){
        event.consume();
            CommonUtils.SetPreviousFocus((TextArea)event.getSource());
        }
    }
    
     private void txtDetail_KeyPressed(KeyEvent event){
        TextField txtDetail = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();
        try {
        if (event.getCode() == F3){
            switch (lnIndex){
                case 1: /*Barcode Search*/       
                    if (poTrans.SearchDetail(pnRow ,lnIndex,  "%" + lsValue, false)){                      
                        txtDetail01.setText(poTrans.getDetail(pnRow, 5).toString());
                        txtDetail02.setText(poTrans.getDetail(pnRow, 7).toString());
                        txtDetail05.setText(poTrans.getDetail(pnRow, 6).toString());
                        txtDetail07.setText("0");
                    } else {
                        txtDetail01.setText(""); 
                        txtDetail02.setText("");
                        txtDetail05.setText("0");
                        txtDetail07.setText("0");
                    }
                    
                    if (txtDetail01.getText().isEmpty()){
                        txtDetail02.requestFocus();
                        txtDetail02.selectAll();
                    }
                    break;
                case 2: /*Description Search*/                   
                    if (poTrans.SearchDetail(pnRow,lnIndex, lsValue, false)){
                        txtDetail01.setText(poTrans.getDetail(pnRow, 5).toString());
                        txtDetail02.setText(poTrans.getDetail(pnRow, 7).toString());
                        txtDetail05.setText(poTrans.getDetail(pnRow, 6).toString());
                        txtDetail07.setText("0");
                    } else {
                        txtDetail01.setText(""); 
                        txtDetail02.setText("");
                        txtDetail05.setText("0");
                        txtDetail07.setText("0");
                    }
                    
                    if (!txtDetail01.getText().isEmpty()){
                        txtDetail07.requestFocus();
                        txtDetail07.selectAll();
                    } else{
                        txtDetail02.requestFocus();
                        txtDetail02.selectAll();
                    }
                    
                    break;
              
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
    }   catch (SQLException ex) {
            Logger.getLogger(InvProdRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
     }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        try {
            if (event.getCode() == ENTER || event.getCode() == F3){
                switch (lnIndex){
                    case 50: /*sTransNox*/
                        if(poTrans.SearchRecord(lsValue, true)==true){
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                        } else {
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
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
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        try {
        switch (lsButton){
           
            case "btnPrint": 
                if (!psOldRec.equals("")){
                    if(poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_CANCELLED)){
                        ShowMessageFX.Warning("Trasaction may be CANCELLED.", pxeModuleName, "Can't print transactions!!!");
                        return;
                    }
                    
                    if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transasction?")== true){
                        if (!printTransfer()) return;
                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                            txtField50.requestFocus();
                    }
                } else ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
                
                break;
            case "btnClose":
                unloadForm();
                return;
            
            case "btnExit":
                unloadForm();
                return;
                
            
            case "btnBrowse":
                if(poTrans.SearchRecord("%" + txtField50.getText(), true)==true){
                    loadRecord(); 
                    pnEditMode = poTrans.getEditMode();
                    break;
                } else {
                    clearFields();
                    pnEditMode = EditMode.UNKNOWN;
                }
                return;                       
            
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        
        txtField50.requestFocus();
    }catch (SQLException ex) {
            Logger.getLogger(InvProdRequestController.class.getName()).log(Level.SEVERE, null, ex);
                }
    
        }
        
    private void loadRecord(){
        try{
    
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField50.setText((String) poTrans.getMaster("sTransNox"));
        psTransNox = txtField50.getText();
        txtField02.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
        
        txtField07.setText((String) poTrans.getMaster("sRemarksx"));
        txtField11.setText((String) poTrans.getMaster("sBranchNm"));
        
        pnRow = 0;
        pnOldRow = 0;
        loadDetail();
        setTranStat((String) poTrans.getMaster("cTranStat"));
        psOldRec = txtField01.getText();
    } catch (SQLException ex) {
            Logger.getLogger(InvProdRequestController.class.getName()).log(Level.SEVERE, null, ex);
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
        try {
            int lnCtr;

            int lnRow = poTrans.getItemCount();
            data.clear();
            /*ADD THE DETAIL*/

            for(lnCtr = 1; lnCtr <= poTrans.getItemCount(); lnCtr++){
                data.add(new TableModel(String.valueOf(lnCtr), 
                                        (String) poTrans.getDetailI(lnCtr, "sBarCodex"),
                                        (String) poTrans.getDetail(lnCtr, 7), 
                                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, 6).toString()), "0.00"),
                                        CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, 3).toString()), "0.00"),
                                        "",
                                        "",
                                        "",
                                        "",
                                        ""));
            }
             initGrid();
            /*FOCUS ON FIRST ROW*/
            if (!data.isEmpty()){
                table.getSelectionModel().select(lnRow -1);
                table.getFocusModel().focus(lnRow -1);
                pnRow = table.getSelectionModel().getSelectedIndex() +1;           

                setDetailInfo(pnRow);
            }
        }   catch (SQLException ex) {
            Logger.getLogger(InvProdRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void setDetailInfo(int fnRow){
        try {
        if (fnRow >= 0){
            txtDetail01.setText((String) poTrans.getDetail(fnRow, 5));
            txtDetail02.setText((String) poTrans.getDetail(fnRow, 7));
            txtDetail05.setText(String.valueOf(poTrans.getDetail(fnRow, 6)));
            txtDetail07.setText(String.valueOf(poTrans.getDetail(fnRow, 3)));
        } else{
            txtDetail01.setText("");
            txtDetail02.setText("");
            txtDetail05.setText("0.00");
            txtDetail07.setText("0.00");
        }
    }   catch (SQLException ex) {
            Logger.getLogger(InvProdRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private boolean printTransfer(){   
        String sourceFileName =  "D://GGC_Java_Systems/reports/InvProdRequest.jasper";
        
        JSONArray json_arr = new JSONArray();
        json_arr.clear();
        try {
        for(int lnCtr = 1; lnCtr <= poTrans.getItemCount(); lnCtr ++){
            JSONObject json_obj = new JSONObject();
            json_obj.put("sField01", (String) poTrans.getDetail(lnCtr, 5));
            json_obj.put("sField02", (String) poTrans.getDetail(lnCtr, 7));
            json_obj.put("sField05", (String) poTrans.getDetail(lnCtr, 10));
            json_obj.put("nField01", (BigDecimal) poTrans.getDetail(lnCtr, 6));
            json_arr.add(json_obj);
        }
        
        String lsSQL = "SELECT sBranchNm FROM Branch WHERE sBranchCD = " + SQLUtil.toSQL((String) poTrans.getMaster("sBranchCd"));
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        
            if (loRS.next())
                lsSQL = loRS.getString("sBranchNm");
            else
                lsSQL = (String) poTrans.getMaster("sBranchCd");
        
        
        //Create the parameter
       
        Map<String, Object> params = new HashMap<>();
        params.put("sCompnyNm", "Los Pedritos");  
        params.put("sBranchNm", poGRider.getBranchName());
//        params.put("sDestinat", lsSQL);
        
        params.put("sTransNox", poTrans.getMaster("sTransNox").toString().substring(1));
        params.put("sReportDt", CommonUtils.xsDateMedium((Date)poTrans.getMaster("dTransact")));
        params.put("sPrintdBy", System.getProperty("user.name"));
                
        
            InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
            JsonDataSource jrjson = new JsonDataSource(stream); 
            
            JasperPrint _jrprint = JasperFillManager.fillReport(sourceFileName, params, jrjson);
            JasperViewer jv = new JasperViewer(_jrprint, false);     
            jv.setVisible(true);
            jv.setAlwaysOnTop(true);
        } catch (JRException | UnsupportedEncodingException  ex) {
            Logger.getLogger(InvTransferController.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SQLException ex) {
            Logger.getLogger(InvProdRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
     @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex() + 1;
        
        if (pnRow < 0) return;
        
        setDetailInfo(pnRow);
    
        txtDetail01.requestFocus();
        txtDetail01.selectAll();
    }
    IMasterDetail poCallBack = new IMasterDetail() {
        @Override
        public void MasterRetreive(int fnIndex) {
            getMaster(fnIndex);
        }

        @Override
        public void DetailRetreive(int fnIndex) {
            switch(fnIndex){
                case 7:                         
                    loadDetail();
                    break;
                        
            }
        }
    };
    
    private void getMaster(int fnIndex){
        try {
        switch(fnIndex){
            case 2:
                txtField02.setText(CommonUtils.xsDateLong((Date)poTrans.getMaster("dTransact")));
                break;

        }
    }   catch (SQLException ex) {
            Logger.getLogger(InvProdRequestController.class.getName()).log(Level.SEVERE, null, ex);
        }
}
}
