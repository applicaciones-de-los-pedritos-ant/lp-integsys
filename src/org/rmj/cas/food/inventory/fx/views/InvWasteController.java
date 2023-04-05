package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.cas.inventory.base.InvWaste;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;

public class InvWasteController implements Initializable {

    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01;
    @FXML private TextField txtField02;
    @FXML private TextArea txtField03;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail80;
    @FXML private TextField txtDetail05;
    @FXML private TextField txtDetail04;
    @FXML private TableView table;
    @FXML private ImageView imgTranStat;
    @FXML private Button btnNew;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnConfirm;
    @FXML private Button btnDel;
    @FXML private Button btnBrowse;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private TextField txtDetail82;
    @FXML private TextField txtDetail06;
    @FXML private TableView tableData;
    @FXML private AnchorPane dataPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvWaste(poGRider, poGRider.getBranchCode(), false);
        poTrans.setCallBack(poCallBack);
        
        btnCancel.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnDel.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnConfirm.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        
        txtField01.focusedProperty().addListener(txtField_Focus);
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField50.focusedProperty().addListener(txtField_Focus);
        txtField51.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtArea_Focus);
        
        txtDetail03.focusedProperty().addListener(txtDetail_Focus);
        txtDetail04.focusedProperty().addListener(txtDetail_Focus);
        txtDetail05.focusedProperty().addListener(txtDetail_Focus);
        txtDetail06.focusedProperty().addListener(txtDetail_Focus);
        txtDetail80.focusedProperty().addListener(txtDetail_Focus);
        
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        txtField51.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtFieldArea_KeyPressed);
        
        txtDetail03.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail04.setOnKeyPressed(this::txtDetail_KeyPressed);
        txtDetail05.setOnKeyPressed(this::txtDetail_KeyPressed); 
        txtDetail06.setOnKeyPressed(this::txtDetail_KeyPressed); 
        txtDetail80.setOnKeyPressed(this::txtDetail_KeyPressed);    
        
        pnEditMode = EditMode.UNKNOWN;    
        clearFields();
        
        initGrid();
        initButton(pnEditMode);
        initLisView();
        
        pbLoaded = true;
    }
    
    private void initLisView(){
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90); index02.setStyle("-fx-alignment: CENTER;");
        index03.setPrefWidth(65); index03.setStyle("-fx-alignment: CENTER;");
        index04.setPrefWidth(65); index04.setStyle("-fx-alignment: CENTER;");
        index05.setPrefWidth(65); index05.setStyle("-fx-alignment: CENTER;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(true); index02.setResizable(false);
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
        
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode.");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Cost");
        TableColumn index05 = new TableColumn("Quantity");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(140);
        index03.setPrefWidth(177); 
        index04.setPrefWidth(80); index04.setStyle("-fx-alignment: CENTER;");
        index05.setPrefWidth(70); index05.setStyle("-fx-alignment: CENTER;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);
        
        table.getColumns().clear();        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        table.getColumns().add(index05);
        
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

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
        tableData.setItems(getRecordData(pnRow));
        if(!pbFound){
            addDetailData(pnlRow);
        }

        setDetailInfo(pnRow);
        txtDetail03.requestFocus();
        txtDetail03.selectAll();
    }
    
    private void addDetailData(int fnRow){
        if (poTrans.getDetail(pnRow, "sStockIDx").equals("")) return;
        
        TableModel newData = new TableModel();
        newData.setIndex01(String.valueOf(fnRow + 1));
        newData.setIndex02(CommonUtils.xsDateMedium((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
        newData.setIndex03("0");
        newData.setIndex04(String.valueOf(poTrans.getDetail(pnRow, "nQuantity")));
        newData.setIndex05("");
        newData.setIndex06("");
        newData.setIndex07("");
        newData.setIndex08("");
        newData.setIndex09("");
        newData.setIndex10("");
        tableData.getItems().add(newData);
        
        index02.setSortType(TableColumn.SortType.ASCENDING);
        tableData.getSortOrder().add(index02);
        tableData.sort();
        
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    private final String pxeModuleName = "InvWasteController";
    private static GRider poGRider;
    private InvWaste poTrans;
    
    TableColumn index01 = new TableColumn("No.");
    TableColumn index02 = new TableColumn("Expiration");
    TableColumn index03 = new TableColumn("OnHnd");
    TableColumn index04 = new TableColumn("Out");
    TableColumn index05 = new TableColumn("Rem");
    
    private boolean pbFound;
    private int pnlRow=0;
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();
    
    private TableModel model;
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;
    
    private String psOldRec = "";
    private String psTransNox = "";
    private String psdTransact = "";
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 2: /*dTransact*/
                     if (CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
                        poTrans.setMaster("dTransact", CommonUtils.toDate(txtField.getText()));
                    } else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 07-07-1991)");
                        poTrans.setMaster(lnIndex, CommonUtils.toDate(pxeDateDefault));
                    }
                    return;
                case 1: /*sTransNox*/
                        break; 
                case 50:
                    if(lsValue.equals("") || lsValue.equals("%"))
                       txtField.setText("");
                    break;   
                case 51:
                    if(CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
                         txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(txtField.getText())));
                    }else{
                        txtField.setText(CommonUtils.xsDateLong(CommonUtils.toDate(pxeDateDefault)));
                    }
                   
                   break;
                    
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
                    return;
            }
            pnIndex = lnIndex;
        } else{
            switch (lnIndex){
                case 2: /*dTransact*/
                    try{
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtField.selectAll();
                    break;
                case 51:
                    /*try{
                        if(!CommonUtils.isDate(txtField.getText(), pxeDateFormat))
                            txtField.setText(CommonUtils.xsDateShort(lsValue));
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }*/
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
               case 3: /*Barcode Search*/
                    break;
                case 80:/*sDescript Search*/
                    break;
                case 4:/*nQuantity*/
                    /*This must be numeric*/
                    double y =0;
                    try{
                        y =Double.valueOf(lsValue);
                    }catch (NumberFormatException e){
                        y = 0;
                    }
                    poTrans.setDetail(pnRow, "nQuantity", y);
                    break;
                case 6:
                    if (CommonUtils.isDate(txtDetail.getText(), pxeDateFormat)){
                        poTrans.setDetail(pnRow, "dExpiryDt", CommonUtils.toDate(txtDetail.getText()));
                    }else{
                        ShowMessageFX.Warning("Invalid date entry.", pxeModuleName, "Date format must be yyyy-MM-dd (e.g. 07-07-1991)");
                        poTrans.setDetail(pnRow, "dExpiryDt",CommonUtils.toDate(pxeDateDefault));
                    }
                    txtDetail.setText(CommonUtils.xsDateMedium((Date)poTrans.getDetail(pnRow, "dExpiryDt")));
                    break;
                        
            }
            pnOldRow = table.getSelectionModel().getSelectedIndex();
            pnIndex = lnIndex;
        } else{
            switch (lnIndex){
                case 6: /*dExpiryDt*/
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
    
    final ChangeListener<? super Boolean> txtArea_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextArea txtField = (TextArea)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
        
        if(!nv){ /*Lost Focus*/            
            switch (lnIndex){
                case 3: /*sRemarksx*/
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
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            case "btnNew":
                if (poTrans.newTransaction()){
                    clearFields();
                    loadRecord();
                    txtField50.setText("");
                    txtField51.setText("");
                    pnEditMode = EditMode.ADDNEW;
                }  
                break;
                
            case "btnConfirm":
               if (!psOldRec.equals("")){
                    if(!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)){
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }
                    if( ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to confirm this transasction?")== true){
                        if (poTrans.postTransaction(psOldRec)){
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction CONFIRMED successfully.");
                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                            initButton(pnEditMode);
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
                
            case "btnSearch":
                switch (pnIndex){
                 case 3:
                    if (poTrans.SearchDetail(pnRow, 3, "%", false, false)){
                        txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                    } else {
                        txtDetail03.setText("");
                        txtDetail80.setText("");
                    }
                    break;
                case 80:
                    if (poTrans.SearchDetail(pnRow, 3, "%", true, false)){
                        txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                    } else {
                        txtDetail03.setText("");
                        txtDetail80.setText("");
                    }
                    break;
                }
                return;
                
            case "btnSave": 
                if (poTrans.saveTransaction()){
                    ShowMessageFX.Information(null, pxeModuleName, "Transaction saved successfuly.");
                    clearFields();
                    initGrid();
                    pnEditMode = EditMode.UNKNOWN;
                    initButton(pnEditMode);
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
                        if(poTrans.BrowseRecord(txtField50.getText(), true) == true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }else
                            if(!txtField50.getText().equals(psTransNox)){
                                clearFields();
                                pnEditMode = EditMode.UNKNOWN;
                                break;
                            }else txtField50.setText(psTransNox);
                    
                        return;
                    case 51: /*dTransact*/
                        if(poTrans.BrowseRecord(txtField51.getText() + "%", false) == true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }
                        
                        if(!txtField51.getText().equals(psdTransact)){
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                            break;
                        }else txtField51.setText(psdTransact);
                        
                        return;
                    default:
                        ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
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
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        initButton(pnEditMode);
    }
    
    private void clearFields(){
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField50.setText("");
        //txtField51.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
        
        pbFound = false;
        pnlRow = 0;
        txtDetail03.setText("");
        txtDetail80.setText("");
        txtDetail82.setText("0");
        txtDetail04.setText("0");
        txtDetail05.setText("0.00");
        
        pnRow = -1;
        pnOldRow = -1;
        pnIndex = 51;
        setTranStat("-1");
        psOldRec = "";
        psTransNox = "";
        psdTransact = "";
        tableData.setItems(loadEmptyData());
        data.clear();
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
        
        txtField01.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        
        txtDetail03.setDisable(!lbShow);
        txtDetail04.setDisable(!lbShow);
        txtDetail05.setDisable(!lbShow);
        txtDetail80.setDisable(!lbShow);
        
        if (lbShow)
            txtField02.requestFocus();
        else
            txtField51.requestFocus();
    }
     
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (event.getCode() == ENTER || event.getCode() == F3){
            switch (lnIndex){
                case 50: /*sTransNox*/
                    if(event.getCode() == F3) lsValue = txtField.getText() + "%";
                    if(poTrans.BrowseRecord(lsValue, true)==true){
                            loadRecord();
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }else
                            if(!txtField50.getText().equals(psTransNox)){
                            clearFields();
                            pnEditMode = EditMode.UNKNOWN;
                            break;
                            }else{
                                txtField50.setText(psTransNox);
                                     }
                            return;
                     
                case 51: /*dTransact*/
                    if(poTrans.BrowseRecord(lsValue, false)== true){
                        loadRecord();
                        pnEditMode = poTrans.getEditMode();
                        break;
                    }
                    if(!txtField51.getText().equals(psdTransact)){
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    } else{
                        txtField51.setText(psdTransact);
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
     
    private void txtFieldArea_KeyPressed(KeyEvent event){
        if (event.getCode() == ENTER || event.getCode() == DOWN){
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
        
        if (event.getCode() == F3){
            switch (lnIndex){
                case 3:
                    if (poTrans.SearchDetail(pnRow, 3, lsValue, true, true)){
                        txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                        txtDetail82.setText(poTrans.getDetailOthers(pnRow, "xQtyOnHnd").toString());
                        txtDetail05.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, "nInvCostx").toString()), "0.00"));
                        txtDetail06.setText(CommonUtils.xsDateMedium((Date) poTrans.getDetail(pnRow, "dExpiryDt")));                        
                    } else {
                        txtDetail03.setText("");
                        txtDetail80.setText("");
                        txtDetail82.setText("0");
                        txtDetail05.setText("0.00");
                        txtDetail06.setText("");
                    }
                    break;

                case 80:
                    if (event.getCode() == F3) lsValue = lsValue + "%";
                    
                    if (poTrans.SearchDetail(pnRow, 3, lsValue, false, false)){
                        txtDetail03.setText(poTrans.getDetailOthers(pnRow, "sBarCodex").toString());
                        txtDetail80.setText(poTrans.getDetailOthers(pnRow, "sDescript").toString());
                        txtDetail82.setText(poTrans.getDetailOthers(pnRow, "xQtyOnHnd").toString());
                        txtDetail05.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, "nInvCostx").toString()), "0.00"));
                    } else {
                        txtDetail03.setText("");
                        txtDetail80.setText("");
                        txtDetail82.setText("0");
                        txtDetail05.setText("0.00");
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
    
    private void unloadForm(){
//        VBox myBox = (VBox) VBoxForm.getParent();
//        myBox.getChildren().clear();
        dataPane.getChildren().clear();
        dataPane.setStyle("-fx-border-color: transparent");
    }
    
    private void loadRecord(){
        txtField01.setText((String) poTrans.getMaster("sTransNox"));
        txtField50.setText((String) poTrans.getMaster("sTransNox"));
        psTransNox = txtField50.getText();
        txtField02.setText(CommonUtils.xsDateMedium((Date) poTrans.getMaster("dTransact")));
        
        try{
          txtField51.setText(CommonUtils.xsDateShort(CommonUtils.toDate(poTrans.getMaster("dTransact").toString())));
          psdTransact = CommonUtils.xsDateShort(CommonUtils.toDate(poTrans.getMaster("dTransact").toString()));
        }catch(NullPointerException e){
            System.out.println(e);
        }
        
        txtField03.setText((String) poTrans.getMaster("sRemarksx"));
        setTranStat((String) poTrans.getMaster("cTranStat"));
        
        pnRow = 0;
        pnOldRow = 0;
        loadDetail();
        tableData.setItems(loadEmptyData());
        
        psOldRec = txtField01.getText();
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
                                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nInvCostx").toString()), "0.00"),
                                    String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                    "",
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
        pnRow = fnRow;
        if (pnRow >= 0){
            txtDetail03.setText((String) poTrans.getDetailOthers(pnRow, "sBarCodex"));
            txtDetail80.setText((String) poTrans.getDetailOthers(pnRow, "sDescript"));
            txtDetail82.setText(String.valueOf(poTrans.getDetailOthers(pnRow, "xQtyOnHnd")));
            txtDetail05.setText(CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(pnRow, "nInvCostx").toString()), "0.00"));
            txtDetail04.setText(String.valueOf(poTrans.getDetail(pnRow, "nQuantity")));
            txtDetail06.setText(CommonUtils.xsDateMedium((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
        } else{
            txtDetail03.setText("");
            txtDetail04.setText("0");
            txtDetail05.setText("0.00");
            txtDetail80.setText("");
            txtDetail82.setText("0");
            txtDetail06.setText("");
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
    
    IMasterDetail poCallBack = new IMasterDetail() {
        @Override
        public void MasterRetreive(int fnIndex) {
            getMaster(fnIndex);
        }

        @Override
        public void DetailRetreive(int fnIndex) {
            switch(fnIndex){
                case 4:
                    txtDetail05.setText(String.valueOf(poTrans.getDetail(pnRow,"nQuantity")));
                    if (!poTrans.getDetail(poTrans.ItemCount()- 1, "sStockIDx").toString().isEmpty() && 
                            Double.valueOf(poTrans.getDetail(poTrans.ItemCount()- 1, fnIndex).toString()) > 0){
                        poTrans.addDetail();
                        pnRow = poTrans.ItemCount()-1;

                        //set the previous order numeber to the new ones.
                        //poTrans.setDetail(pnRow, "sOrderNox", psOrderNox);
                    } 
                    
                    loadDetail();
                    
                     if (!txtDetail03.getText().isEmpty()){
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
                /*get the value from the class*/
                txtField02.setText(CommonUtils.xsDateLong((Date)poTrans.getMaster("dTransact")));
                break;
                
                
        }
    }
    
    private ObservableList getRecordData(int fnRow){
        ObservableList dataDetail = FXCollections.observableArrayList();
        ResultSet loRS = null;
        loRS = poTrans.getExpiration((String)poTrans.getDetail(fnRow, "sStockIDx"));
        double lnQuantity = 0;
        pnlRow = 0;
        pbFound = false;
        
        try {
                dataDetail.clear();
                loRS.first();
                    for( int rowCount = 0; rowCount <= MiscUtil.RecordCount(loRS) -1; rowCount++){
                        if (CommonUtils.xsDateShort(loRS.getDate("dExpiryDt")).equals(CommonUtils.xsDateShort((Date) poTrans.getDetail(fnRow, "dExpiryDt")))){
                            if(!pbFound) pbFound = true;
                            lnQuantity = (double)poTrans.getDetail(fnRow, "nQuantity");
                        }else{
                            lnQuantity = 0;
                        }
                    dataDetail.add(new TableModel(String.valueOf(rowCount +1),
                        String.valueOf(CommonUtils.xsDateMedium(loRS.getDate("dExpiryDt"))),
                        String.valueOf(loRS.getDouble("nQtyOnHnd")),
                        String.valueOf(lnQuantity),
                        String.valueOf((double)loRS.getDouble("nQtyOnHnd") - (double) lnQuantity),
                        "",
                        "",
                        "",
                        "",
                        ""     
                    ));
                    pnlRow++;
                    loRS.next();
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return dataDetail;
    }
    
    private ObservableList loadEmptyData(){
        ObservableList dataDetail = FXCollections.observableArrayList();
            dataDetail.clear();
            dataDetail.add(new TableModel(String.valueOf(1),
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            "",
            ""     
            ));
        return dataDetail;
    }
}
