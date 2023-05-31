package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
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
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.appdriver.constants.UserRight;
import org.rmj.cas.inventory.base.InvRequest;
import org.rmj.cas.parameter.agent.XMBranch;
import org.rmj.cas.parameter.agent.XMInventoryType;


/**
 * FXML Controller class
 *
 * @author jovanalic
 * since 07-03-21
 */
public class InvStockRequestRegController implements Initializable {
   @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01,txtField02,txtField03,txtField04,txtField05,txtField50,
                                txtField51,txtDetail01,txtDetail02,txtDetail05,txtDetail07;
    @FXML private TextArea txtField06,txtField07,txtDetail03;
    @FXML private Label Label12;
    @FXML private TableView table,tableDetail; 
    @FXML private Button btnClose,btnPrint,btnBrowse,btnExit;
    @FXML private ImageView imgTranStat;
    @FXML private AnchorPane dataPane;
    
    TableColumn index01 = new TableColumn("No.");
    TableColumn index02 = new TableColumn("Expiration");
    TableColumn index03 = new TableColumn("OnHnd");
    TableColumn index04 = new TableColumn("Out");
    TableColumn index05 = new TableColumn("Rem");
    
    private final String pxeModuleName = "InvStockRequestRegController";
    protected Date pdExpiryDt = null;
    protected Boolean pbEdited = false;
    private static GRider poGRider;
    private InvRequest poTrans;
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
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvRequest(poGRider, poGRider.getBranchCode(), false);
        poTrans.setCallBack(poCallBack);
        poTrans.setTranStat(1234);
        
        btnPrint.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        
                        
        /*Add keypress event for field with search*/
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtFieldArea_KeyPressed);
        txtField07.setOnKeyPressed(this::txtFieldArea_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtDetail01.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail02.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail03.setOnKeyPressed(this::txtDetailArea_KeyPressed);
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
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField50.setText("");
        txtField51.setText("");
        
        pbFound = false;
        txtDetail01.setText("");
        txtDetail02.setText("");
        txtDetail03.setText("");
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
        
//        tableDetail.setItems(loadEmptyData());
        data.clear();
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("M.");
        TableColumn index05 = new TableColumn("QOH");
        TableColumn index06 = new TableColumn("Qty");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(220);
        index03.setPrefWidth(260);
        index04.setPrefWidth(160);
        index05.setPrefWidth(65); index05.setStyle("-fx-alignment: CENTER;");
        index06.setPrefWidth(40); index06.setStyle("-fx-alignment: CENTER;");
        
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
      
    private void unloadForm(){
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
        
        if (event.getCode() == F3){
            
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(txtDetail);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(txtDetail);
        }
    }
     }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
            if (event.getCode() == ENTER || event.getCode() == F3){
                switch(lnIndex){
                    case 50: /*sTransNox*/
                        if(poTrans.BrowseRecord(txtField50.getText(), true)==true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        } else {
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                        return;    
                    case 51: /*sDestination*/
                        if(poTrans.BrowseRecord(txtField51.getText() + "%", false)== true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                        }
                    default:
                        txtField51.requestFocus();
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
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
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
//                        }
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
                switch(pnIndex){
                    case 50: /*sTransNox*/
                        if(poTrans.BrowseRecord(txtField50.getText(), true)==true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        } else {
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                        return;    
                    case 51: /*sDestination*/
                        if(poTrans.BrowseRecord(txtField51.getText() + "%", false)== true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                        }
                    default:
                        txtField51.requestFocus();
                }
                return;
           
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        
        txtField50.requestFocus();
    }
    
    private void loadRecord(){
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField50.setText((String) poTrans.getMaster("sTransNox"));
        psTransNox = txtField50.getText();
        
        XMBranch loBranch = poTrans.GetBranch((String)poTrans.getMaster(3), true);
        if (loBranch != null) {
            txtField03.setText((String) loBranch.getMaster("sBranchNm"));
            txtField51.setText((String) loBranch.getMaster("sBranchNm"));
        }
        
        XMInventoryType loInv = poTrans.GetInventoryType((String)poTrans.getMaster(4), true);
        if (loInv != null) txtField04.setText((String) loInv.getMaster("sDescript"));
        
        txtField02.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
        txtField05.setText((String) poTrans.getMaster("sReferNox"));
        txtField06.setText((String) poTrans.getMaster("sIssNotes"));
        txtField07.setText((String) poTrans.getMaster("sRemarksx"));
        
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
                                    (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"),
                                    (String) poTrans.getDetailOthers(lnCtr, "sDescript"), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"),
                                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQtyOnHnd").toString()), "0.00"),
                                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()), "0.00"),
                                    "",
                                    "",
                                    "",
                                    ""));
        }
    
        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()){
            table.getSelectionModel().select(lnRow -1);
            table.getFocusModel().focus(lnRow -1);
            
            pnRow = table.getSelectionModel().getSelectedIndex();           
            
            setDetailInfo(pnRow);
        }
    }
    
    private void setDetailInfo(int fnRow){
        if (fnRow >= 0){
            txtDetail01.setText((String) poTrans.getDetailOthers(fnRow, "sBarCodex"));
            txtDetail02.setText((String) poTrans.getDetailOthers(fnRow, "sDescript"));
            txtDetail03.setText((String) poTrans.getDetail(fnRow, "sNotesxxx"));
            txtDetail05.setText(String.valueOf(poTrans.getDetail(fnRow, "nQtyOnHnd")));
            txtDetail07.setText(String.valueOf(poTrans.getDetail(fnRow, "nQuantity")));
        } else{
            txtDetail01.setText("");
            txtDetail02.setText("");
            txtDetail03.setText("");
            txtDetail05.setText("0.00");
            txtDetail07.setText("0.00");
        }
    }
    
    
    
    

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
        if (pnRow < 0) return;
        
        setDetailInfo(pnRow);
    
        txtDetail01.requestFocus();
        txtDetail01.selectAll();
    }
    
    private boolean printTransfer(){        
        JSONArray json_arr = new JSONArray();
        json_arr.clear();
        
        for(int lnCtr = 0; lnCtr <= poTrans.ItemCount()-1; lnCtr ++){
            JSONObject json_obj = new JSONObject();
            json_obj.put("sField01", (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"));
            json_obj.put("sField02", (String) poTrans.getDetailOthers(lnCtr, "sDescript"));
            json_obj.put("sField05", (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"));
            json_obj.put("nField01", Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()));
            json_arr.add(json_obj);
        }
        
        String lsSQL = "SELECT sBranchNm FROM Branch WHERE sBranchCD = " + SQLUtil.toSQL((String) poTrans.getMaster("sBranchCd"));
        ResultSet loRS = poGRider.executeQuery(lsSQL);
        try {
            if (loRS.next())
                lsSQL = loRS.getString("sBranchNm");
            else
                lsSQL = (String) poTrans.getMaster("sBranchCd");
        } catch (SQLException ex) {
            ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Unable to print...");
            System.exit(1);
        }
        
        //Create the parameter
        Map<String, Object> params = new HashMap<>();
        params.put("sCompnyNm", "Los Pedritos");  
        params.put("sBranchNm", poGRider.getBranchName());
        params.put("sDestinat", lsSQL);
        params.put("sAddressx", poGRider.getAddress());
        params.put("sReportNm", "Inventory Stock Request");
        
        params.put("sTransNox", poTrans.getMaster("sTransNox").toString().substring(1));
        params.put("sReportDt", CommonUtils.xsDateMedium((Date)poTrans.getMaster("dTransact")));
        params.put("sPrintdBy", System.getProperty("user.name"));
                
        try {
            InputStream stream = new ByteArrayInputStream(json_arr.toJSONString().getBytes("UTF-8"));
            JsonDataSource jrjson = new JsonDataSource(stream); 
            
            JasperPrint _jrprint = JasperFillManager.fillReport("d:/GGC_Java_Systems/reports/InvStockRequest.jasper", params, jrjson);
            JasperViewer jv = new JasperViewer(_jrprint, false);     
            jv.setVisible(true);
            jv.setAlwaysOnTop(true);
        } catch (JRException | UnsupportedEncodingException  ex) {
            Logger.getLogger(InvTransferController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return true;
    }
    
    IMasterDetail poCallBack = new IMasterDetail() {
        @Override
        public void MasterRetreive(int fnIndex) {
            getMaster(fnIndex);
        }

        @Override
        public void DetailRetreive(int fnIndex) {
            switch(fnIndex){
                
                case 10:
//                    txtDetail10.setText((String)poTrans.getDetail(pnRow,"sNotesxxx"));
//                    loadDetail();
                    break;
                case 7:
                    txtDetail07.setText(String.valueOf(poTrans.getDetail(pnRow,"nQuantity")));
//                    loadDetail();
                    
                    if (!poTrans.getDetail(poTrans.ItemCount()- 1, "sStockIDx").toString().isEmpty() && 
                            Double.valueOf(poTrans.getDetail(poTrans.ItemCount()- 1, fnIndex).toString()) > 0){
//                        poTrans.addDetail();
//                        pnRow = poTrans.ItemCount()- 1;

                        //set the previous order numeber to the new ones.
//                        poTrans.setDetail(pnRow, "sOrderNox", psOrderNox);
                    }                           
                    loadDetail();
//                    if (!txtDetail03.getText().isEmpty()){
//                        txtDetail08.requestFocus();
//                        txtDetail08.selectAll();
//                    } else{
//                        txtDetail05.requestFocus();
//                        txtDetail05.selectAll();
//                    }
                    break;
//                case 8:
//                    txtDetail08.setText(CommonUtils.xsDateLong((Date)poTrans.getDetail(pnRow,"dExpiryDt")));
//                    break;
                        
            }
        }
    };
    
    private void getMaster(int fnIndex){
        switch(fnIndex){
            case 2:
                txtField02.setText(CommonUtils.xsDateLong((Date)poTrans.getMaster("dTransact")));
                break;
            case 3:
                XMBranch loBranch = poTrans.GetBranch((String)poTrans.getMaster(fnIndex), true);
                if (loBranch != null) txtField03.setText((String) loBranch.getMaster("sBranchNm"));
                break;
            case 4:
                XMInventoryType loInventoryType = poTrans.GetInventoryType((String)poTrans.getMaster(fnIndex), true);
                if (loInventoryType != null) txtField04.setText((String) loInventoryType.getMaster("sDescript"));
                break;   
        }
    }
}
