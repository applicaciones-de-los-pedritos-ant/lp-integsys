package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
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
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.cas.inventory.production.base.DailyProduction;

public class DailyProductionRegController implements Initializable {

    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private TextField txtField01;
    @FXML private TextField txtField02;
    @FXML private TextArea txtField03;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail80;
    @FXML private TextField txtDetail05;
    @FXML private TextField txtDetail04;
    @FXML private TableView table;
    @FXML private ImageView imgTranStat;
    @FXML private Button btnVoid;
    @FXML private Button btnClose;
    @FXML private Button btnPrint;
    @FXML private Button btnBrowse;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private AnchorPane dataPane;
    @FXML private TableView table1;
    @FXML private TextField txtDetail06;
    @FXML private TextField txtDetail07;
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new DailyProduction(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(123);
        
        btnVoid.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
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
        initRawData();
        pbLoaded = true;  
    }    

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
        setDetailInfo(pnRow);
    }
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode.");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("QTY");
        
        index01.setPrefWidth(50); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(100);
        index03.setPrefWidth(175); 
        index04.setPrefWidth(85); index04.setStyle("-fx-alignment: CENTER;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        
        table.getColumns().clear();        
        table.getColumns().add(index01);
        table.getColumns().add(index02);
        table.getColumns().add(index03);
        table.getColumns().add(index04);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        
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
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    private final String pxeModuleName = "DailyProductionController";
    private static GRider poGRider;
    private DailyProduction poTrans;
    
    private String psTransNox = "";
    private String psdTransact = "";
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();
    private int pnRawdata = -1;
    
    private TableModel model;
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private ObservableList<RawTable> rawData = FXCollections.observableArrayList();
    
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;
    
    private String psOldRec = "";
    
     private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
                
            case "btnClose":
            case "btnExit": 
                unloadForm();
                return;
               
            case "btnPrint": 
                if(!psOldRec.equals("")){
                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transaction?")==true){
                        //if (poTrans.printTransaction(psOldRec))
                        ShowMessageFX.Information(null, pxeModuleName, "Transaction printed successfully.");
                        clearFields();
                        initGrid();
                        pnEditMode = EditMode.UNKNOWN;
                        break;
                    }else
                        return;
                }else 
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
                break;
                
            case "btnBrowse":
                switch(pnIndex){
                    case 50: /*sTransNox*/
                        if(poTrans.BrowseRecord(txtField50.getText(), false) == true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }else
                            if(!txtField50.getText().equals(psTransNox)){
                                clearFields();
                                break;
                            }else txtField50.setText(psTransNox);
                        
                        return;                     
                    case 51: /*dTransact*/
                        String lsValue = "";
                        if (CommonUtils.isDate(txtField51.getText(), SQLUtil.FORMAT_MEDIUM_DATE)){
                            lsValue = SQLUtil.dateFormat(SQLUtil.toDate(txtField51.getText(), SQLUtil.FORMAT_MEDIUM_DATE), SQLUtil.FORMAT_SHORT_DATE);
                        }

                        if(poTrans.BrowseRecord(lsValue, false)== true){
                            loadRecord(); 
                            pnEditMode = poTrans.getEditMode();
                            break;
                        }

                        if(!txtField51.getText().equals(psdTransact)){
                            clearFields();
                            break;
                        }else txtField51.setText(psdTransact);                            

                        return;
                default:
                    ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                    txtField51.requestFocus();
                }

                return;
            case "btnVoid":
               if (!psOldRec.equals("")){
                    if(!poTrans.getMaster("cTranStat").equals(TransactionStatus.STATE_OPEN)){
                        ShowMessageFX.Warning("Trasaction may be CANCELLED/POSTED.", pxeModuleName, "Can't update processed transactions!!!");
                        return;
                    }
                    
                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to void this transaction?")==true){
                        if (poTrans.voidTransaction(psOldRec)){
                            ShowMessageFX.Information(null, pxeModuleName, "Transaction VOIDED successfully.");
                            clearFields();
                            initGrid();
                            pnEditMode = EditMode.UNKNOWN;
                        } else ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                    }else return;
                }else 
                   ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to void!");
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }
     
    private void clearFields(){
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField50.setText("");
        txtField51.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
        
        txtDetail03.setText("");
        txtDetail04.setText("0");
        txtDetail05.setText("");
        txtDetail80.setText("");
        txtDetail07.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
        
        
        pnRow = -1;
        pnRawdata = -1;
        pnOldRow = -1;
        pnIndex = 51;
        setTranStat("-1");
        psOldRec = "";
        psTransNox = "";
        psdTransact = "";
        data.clear();
        
        pnIndex = 50;
    }
    
    /**
     * for handling of editable table view and passing of parameters to setters.
     */
    private void initRawData(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("RcQty");
        TableColumn index05 = new TableColumn("UsQty");
        
        index01.setPrefWidth(40); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(70);
        index03.setPrefWidth(120); 
        index04.setPrefWidth(60); index04.setStyle("-fx-alignment: CENTER;");
        index05.setPrefWidth(60); index05.setStyle("-fx-alignment: CENTER;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(false); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);
        index04.setSortable(false); index04.setResizable(false);
        index05.setSortable(false); index05.setResizable(false);
        
        table1.getColumns().clear();        
        table1.getColumns().add(index01);
        table1.getColumns().add(index02);
        table1.getColumns().add(index03);
        table1.getColumns().add(index04);
        table1.getColumns().add(index05);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.RawTable,String>("index05"));
        
        /*making column's position uninterchangebale*/
        table1.widthProperty().addListener(new ChangeListener<Number>() {  
            public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
            {
                TableHeaderRow header = (TableHeaderRow) table1.lookup("TableHeaderRow");
                header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
                    @Override
                    public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                        header.setReordering(false);
                            }
                        });
                    }
                });
        
        table1.getColumns().clear();  
        table1.getColumns().add(index01);
        table1.getColumns().add(index02);
        table1.getColumns().add(index03);
        table1.getColumns().add(index04);
        table1.getColumns().add(index05);
        
        /*Set data source to table*/
        table1.setItems(rawData);
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
          txtField51.setText(CommonUtils.xsDateMedium(CommonUtils.toDate(poTrans.getMaster("dTransact").toString())));
          psdTransact = CommonUtils.xsDateMedium(CommonUtils.toDate(poTrans.getMaster("dTransact").toString()));
        }catch(NullPointerException e){
            System.out.println(e);
        }
        txtField03.setText((String) poTrans.getMaster("sRemarksx"));
        setTranStat((String) poTrans.getMaster("cTranStat"));
        
        pnRow = 0;
        pnOldRow = 0;
        loadDetail();
        loadRawDetail();
        
        psOldRec = txtField01.getText();
    }
    
    /**
     * since 06-11-21
     * this method will load raw detail per detail
     */
    private void loadRawDetail(){
        int lnCtr;
        int lnRow = poTrans.InvCount();
        
        rawData.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= lnRow -1; lnCtr++){
            rawData.add(new RawTable(String.valueOf(lnCtr + 1), 
                        (String) poTrans.getInvOthers(lnCtr, "sBarCodex"), 
                        (String) poTrans.getInvOthers(lnCtr, "sDescript"), 
                        (String) poTrans.getInvOthers(lnCtr, "sMeasurNm"), 
                        String.valueOf(poTrans.getInv(lnCtr, "nQtyReqrd")),
                        String.valueOf(poTrans.getInv(lnCtr, "nQtyUsedx"))
                        ));
        }
    
        /*FOCUS ON FIRST ROW*/
        if (!rawData.isEmpty()){
            table1.getSelectionModel().select(lnRow -1);
            table1.getFocusModel().focus(lnRow -1);
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
                                    String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                    "",
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
        if (!poTrans.getDetail(fnRow, "sStockIDx").equals("")){
            txtDetail03.setText((String) poTrans.getDetailOthers(pnRow, "sBarCodex"));
            txtDetail80.setText((String) poTrans.getDetailOthers(pnRow, "sDescript"));
            txtDetail06.setText(CommonUtils.xsDateMedium((Date) poTrans.getDetail(pnRow, "dExpiryDt")));
            txtDetail04.setText(String.valueOf(poTrans.getDetail(pnRow, "nQuantity")));
            txtDetail05.setText(String.valueOf(poTrans.getDetail(pnRow, "nGoalQtyx")));
        } else{
            txtDetail03.setText("");
            txtDetail04.setText("0");
            txtDetail05.setText("");
            txtDetail06.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
            txtDetail80.setText("");
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
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (event.getCode() == ENTER || event.getCode() == F3){
            switch (lnIndex){
                 case 50: /*sTransNox*/
                    if(event.getCode()== F3) lsValue = txtField.getText();
                        if(poTrans.BrowseRecord(lsValue, true)==true){
                        loadRecord(); 
                        pnEditMode = poTrans.getEditMode();   
                    }
                    
                    if(!txtField50.getText().equals(psTransNox)){
                        clearFields();
                        break;
                        }else{
                            txtField50.setText(psTransNox);
                                 }
                    return;
                     
                case 51: /*dTransact*/
                    if(poTrans.BrowseRecord(lsValue, false)== true){
                        loadRecord(); 
                        pnEditMode = poTrans.getEditMode();
                    }
                    if(!txtField51.getText().equals(psdTransact)){
                        clearFields();
                        break;
                        }else{
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
    
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/           
            switch (lnIndex){
                case 50: /*sTransNox*/
                    if(lsValue.equals("") || lsValue.equals("%")){
                       txtField.setText("");
                    }else
                    txtField.setText(psTransNox); break;
                case 51: /*sSupplierId*/
                    if(CommonUtils.isDate(txtField.getText(), pxeDateFormat)){
                         txtField.setText(CommonUtils.xsDateMedium(CommonUtils.toDate(txtField.getText())));
                    }else{
                        txtField.setText(CommonUtils.xsDateMedium(CommonUtils.toDate(pxeDateDefault)));
                    }
                   break;
                default:
                    ShowMessageFX.Warning(null, pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
            pnIndex = lnIndex;   
        }else{
            switch (lnIndex){
                case 51:
                    try{
                        txtField.setText(CommonUtils.xsDateShort(lsValue));
                    }catch(ParseException e){
                        ShowMessageFX.Error(e.getMessage(), pxeModuleName, null);
                    }
                    txtField.selectAll();
                    break;
                default:
            }
            pnIndex = lnIndex;
            txtField.selectAll();
        }
    };

    @FXML
    private void tableRaw_Clicked(MouseEvent event) {
        pnRawdata   = table1.getSelectionModel().getSelectedIndex();
       if(pnRawdata < 0) return;
      
        
        if(!poTrans.getInv(pnRawdata, "sStockIDx").equals("")){
            txtDetail07.setText(CommonUtils.xsDateMedium((Date) poTrans.getInv(pnRawdata, "dExpiryDt")));
        }else{
            txtDetail07.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
        }
    }
    
}
