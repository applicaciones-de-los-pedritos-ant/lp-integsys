
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
import org.rmj.cas.inventory.base.InvRequest;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMInventoryType;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.appdriver.constants.UserRight;

public class InvStockRequestController implements Initializable {
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    @FXML private TextField txtField04;
    @FXML private TextField txtField05;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private TextArea txtField06;
    @FXML private TextArea txtField07;
    @FXML private Label Label12;
    @FXML private TextField txtDetail01;
    @FXML private TextField txtDetail02;
    @FXML private TextField txtDetail05;
    @FXML private TextField txtDetail07;
    @FXML private TextArea txtDetail03;
    @FXML private TableView table; 
    
    @FXML private Button btnNew;
    @FXML private Button btnSave;
    @FXML private Button btnConfirm;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnDel;
    @FXML private Button btnBrowse;
    @FXML private ImageView imgTranStat;
    @FXML private Button btnPrint;
    @FXML private TableView tableDetail;
    @FXML private AnchorPane dataPane;
    @FXML private Button btnUpdate;
    
    TableColumn index01 = new TableColumn("No.");
    TableColumn index02 = new TableColumn("Expiration");
    TableColumn index03 = new TableColumn("OnHnd");
    TableColumn index04 = new TableColumn("Out");
    TableColumn index05 = new TableColumn("Rem");
    
    private final String pxeModuleName = "InvStockRequestController";
    protected Date pdExpiryDt = null;
    protected Boolean pbEdited = false;
    private static GRider poGRider;
    private InvRequest poTrans;
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    private final String pxeDateFormat = "MM-dd-yyyy";
    private final String pxeDateFormatMsg = "Date format must be MM-dd-yyyy (e.g. 12-25-1945)";
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
        poTrans.setTranStat(0);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnDel.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnConfirm.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtArea_Focus);
        txtField07.focusedProperty().addListener(txtArea_Focus);
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        
        txtDetail01.focusedProperty().addListener(txtDetail_Focus);
        txtDetail02.focusedProperty().addListener(txtDetail_Focus);
//        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail03.focusedProperty().addListener(txtDetailArea_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
//        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail07.focusedProperty().addListener(txtDetail_Focus);
                
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
//        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed);
//        txtDetail06.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail07.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setDisable(true);
        
        pnEditMode = EditMode.UNKNOWN;
        clearFields();
        initGrid();
        initButton(pnEditMode);
        
        pbLoaded = true;
    }
    
    public void setGRider(GRider foGRider){
        this.poGRider = foGRider;
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
        btnPrint.setVisible(!lbShow);
        btnConfirm.setVisible(!lbShow);
        btnClose.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        
        txtField01.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        
        txtDetail01.setDisable(!lbShow);
        txtDetail02.setDisable(!lbShow);
        txtDetail03.setDisable(!lbShow);
//        txtDetail04.setDisable(!lbShow);
//        txtDetail05.setDisable(!lbShow);
//        txtDetail06.setDisable(!lbShow);
        txtDetail07.setDisable(!lbShow);
        
        if (lbShow)
            txtField01.requestFocus();
        else
            txtField51.requestFocus();
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
//        txtDetail04.setText("0");
        txtDetail05.setText("0");
//        txtDetail06.setText("0");
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
        TableColumn index02 = new TableColumn("Bar Code");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Brand");
        TableColumn index05 = new TableColumn("Measure");
        TableColumn index06 = new TableColumn("QOH");
        TableColumn index07 = new TableColumn("Qty");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(260);
        index04.setPrefWidth(240);
        index05.setPrefWidth(75); index05.setStyle("-fx-alignment: CENTER;");
        index06.setPrefWidth(65); index06.setStyle("-fx-alignment: CENTER-RIGHT;");
        index07.setPrefWidth(40); index07.setStyle("-fx-alignment: CENTER-RIGHT;");
        
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
        table.getColumns().add(index03);
        table.getColumns().add(index04);
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
        String lsValue = txtDetail.getText() + "%";
        
        if (event.getCode() == F3){
            switch (lnIndex){
                case 1: /*Barcode Search*/                   
                    if (poTrans.SearchDetail(pnRow, 1, lsValue, false, false)){                      
                        txtDetail01.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail02.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                        txtDetail03.setText("");
                        txtDetail05.setText(poTrans.getDetail(pnRow, "nQtyOnHnd").toString());
                        txtDetail07.setText("0");
                    } else {
                        txtDetail01.setText("");
                        txtDetail02.setText("");
                        txtDetail05.setText("0");
                        txtDetail07.setText("0");
                    }
                    
                    if (!txtDetail01.getText().isEmpty()){
                        txtDetail03.requestFocus();
                        txtDetail03.selectAll();
                    } else{
                        txtDetail02.requestFocus();
                        txtDetail02.selectAll();
                    }           
                    break;
                case 2: /*Description Search*/                   
                    if (poTrans.SearchDetail(pnRow, 2, lsValue, false, false)){
                        txtDetail01.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail02.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                        txtDetail05.setText(poTrans.getDetail(pnRow, "nQtyOnHnd").toString());
                        txtDetail07.setText("0");
                    } else {
                        txtDetail01.setText("");
                        txtDetail05.setText("0");
                        txtDetail07.setText("0");
                    }
                    
                    if (!txtDetail01.getText().isEmpty()){
                        txtDetail03.requestFocus();
                        txtDetail03.selectAll();
                    } else{
                        txtDetail02.requestFocus();
                        txtDetail02.selectAll();
                    }
                    
                    break;
                case 80: /*Description Search*/
                    if (poTrans.SearchDetail(pnRow, 3, lsValue, true, false)){
                        txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail07.setText(poTrans.getDetail(pnRow, "nInvCostx").toString());
                    } else {
                        txtDetail03.setText("");
                        txtDetail07.setText("");
                    }
                    
                    if (!txtDetail03.getText().isEmpty()){
                        txtDetail07.requestFocus();
                        txtDetail07.selectAll();
                    } else{
                        txtDetail05.requestFocus();
                        txtDetail05.selectAll();
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
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText() + "%";
            if (event.getCode() == ENTER || event.getCode() == F3){
                switch (lnIndex){
                    case 3: /*sBranchCd*/
                        if (poTrans.SearchMaster(lnIndex, txtField.getText(), false)){
                            CommonUtils.SetNextFocus(txtField); 
                        }else txtField.setText("");
                        break;
                    case 4: /*sInvTypCd*/
                        if (poTrans.SearchMaster(lnIndex, lsValue, false)){
                            CommonUtils.SetNextFocus(txtField);
                        }else txtField.setText("");
                        return;
                    case 50: /*sTransNox*/
                        if(poTrans.BrowseRecord(lsValue, true)==true){
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                        } else {
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                        return;
                    case 51: /*psDestina*/
                        if(poTrans.BrowseRecord(lsValue, false)== true){
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
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            case "btnNew":
                if (poTrans.newTransaction()){  
                    clearFields();
                    loadRecord();
                    txtField50.setText("");
                    pnEditMode = poTrans.getEditMode();
                    initButton(pnEditMode);
                }  
                break;
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
                            initButton(pnEditMode);
                    }
                    
                } else ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
                break;
            case "btnClose":
                unloadForm();
                return;
            case "btnConfirm":
                if (!psOldRec.equals("")){
                    if(!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)){
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/CLOSED.", pxeModuleName, "Can't update transactions!!!");
                        return;
                    }
                    
                    if (poGRider.getUserLevel() <= UserRight.ENCODER){
                        JSONObject loJSON = showFXDialog.getApproval(poGRider);
            
                        if (loJSON == null){
                        ShowMessageFX.Warning("Approval failed.", pxeModuleName, "Unable to post transaction");
                        }
            
                        if ((int) loJSON.get("nUserLevl") <= UserRight.ENCODER){
                            ShowMessageFX.Warning("User account has no right to approve.", pxeModuleName, "Unable to post transaction");
                            return;
                        }
                    }  
                    
                    if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to confirm this transasction?")== true){
                        if (poTrans.closeTransaction(psOldRec)){
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction CONFIRMED successfully.");
                            
                            if (poTrans.openTransaction(psOldRec)){
                                clearFields();
                                loadRecord(); 
                                
                                psOldRec = (String) poTrans.getMaster("sTransNox");
                                
                                if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transasction?")== true){
                                    if (!printTransfer()) return;
                                }
                            }

                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                    }
                } else ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to confirm!");
                break;
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
                
            case "btnSearch":return;
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
                } else{
                    if (!poTrans.getErrMsg().equals(""))
                        ShowMessageFX.Error(poTrans.getErrMsg(), pxeModuleName, "Please inform MIS Department.");
                    else
                        ShowMessageFX.Warning(poTrans.getMessage(), pxeModuleName, "Please verify your entry.");
                    return;
                } 
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
                                    (String) poTrans.getDetailOthers(lnCtr, "sBrandNme"), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sMeasurNm"),
                                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQtyOnHnd").toString()), "0.00"),
                                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nQuantity").toString()), "0.00"),
                                    "",
                                    "",
                                    ""));
            System.out.println(poTrans.getDetailOthers(lnCtr, "sBrandNme"));
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
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/      
            switch (lnIndex){
                case 1: /*sTransNox*/
                    break;
                case 2: /*dTransact*/
                  if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
                        poTrans.setMaster("dTransact", SQLUtil.toDate(txtField.getText(), pxeDateFormat));
                    } else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, pxeDateFormatMsg);
                        poTrans.setMaster(lnIndex, CommonUtils.toDate(pxeDateDefault));
                    }
                    return;
                case 3:/*sBranchCd*/
                case 4:/*sInvTypex*/
                case 5:/*sReferNox*/
                    poTrans.setMaster("sReferNox",txtField.getText());
                case 50:
                    if(lsValue.equals("") || lsValue.equals("%"))
                        txtField.setText("");
                    break; 
                case 51: 
                    if(lsValue.equals("") || lsValue.equals("%"))
                        txtField.setText("");
                    break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                    return;
            }
            pnIndex = lnIndex;
        } else{
            switch (lnIndex){
                case 2: /*dTransact*/
                    txtField.setText(SQLUtil.dateFormat(poTrans.getMaster("dTransact"), pxeDateFormat));
                    txtField.selectAll();
                    break;
                default:
            }
            pnIndex = lnIndex;
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
                case 1: /*Barcode Search*/
                case 2:/*sDescript Search*/
                case 5:/*Qty onhand*/
                    break;
                
                case 7:/*nQuantity*/
                    double x = 0;
                    try {
                        /*this must be numeric*/
                        x = Double.valueOf(lsValue);
                    } catch (NumberFormatException e) {
                        x = 0;
                        txtDetail.setText("0");
                    }
                 
                    poTrans.setDetail(pnRow, "nQuantity",  x);
                    if (x > 0.00 & !txtDetail01.getText().isEmpty()) {
                        poTrans.addDetail();
                        pnRow = poTrans.ItemCount()-1;
                        
                        loadDetail();
                    }
                    
                    txtDetail01.requestFocus();
                    txtDetail01.selectAll();
                    break;
            }
            pnOldRow = table.getSelectionModel().getSelectedIndex();
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
    
    final ChangeListener<? super Boolean> txtDetailArea_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextArea txtDetail = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtDetail.getId().substring(9, 11));
        String lsValue = txtDetail.getText();
        
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/            
            switch (lnIndex){
                case 3: /*sNotesxxx*/
                    if (lsValue.length() > 256) lsValue = lsValue.substring(0, 256);
                    
                    poTrans.setDetail(pnRow, "sNotesxxx", CommonUtils.TitleCase(lsValue));
            }
        }else{ 
            pnIndex = -1;
            txtDetail.selectAll();
        }
    };
    
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/            
            switch (lnIndex){
                case 6: /*sNotesxxx*/
                    if (lsValue.length() > 256) lsValue = lsValue.substring(0, 256);
                    
                    poTrans.setMaster("sIssNotes", CommonUtils.TitleCase(lsValue));
                    txtField.setText((String)poTrans.getMaster("sIssNotes"));
                    break;
                case 7: /*sRemarksx*/
                    if (lsValue.length() > 256) lsValue = lsValue.substring(0, 256);
                    
                    poTrans.setMaster("sRemarksx", CommonUtils.TitleCase(lsValue));
                    txtField.setText((String)poTrans.getMaster("sRemarksx"));
                    break;
            }
        }else{ 
            pnIndex = -1;
            txtField.selectAll();
        }
    };

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
            json_obj.put("sField06", (String) poTrans.getDetailOthers(lnCtr, "sBrandNme"));
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
