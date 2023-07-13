package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javafx.scene.layout.VBox;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IMasterDetail;
import org.rmj.appdriver.constants.TransactionStatus;
import org.rmj.cas.inventory.base.InvWaste;

public class InvWasteRegController implements Initializable {

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
    @FXML private TextField txtDetail06;
    @FXML private TableView tableData;
    @FXML private AnchorPane dataPane;
    @FXML private Label lblHeader;
    @FXML private TextField txtDetail82;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvWaste(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(1230);
        
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnPrint.setOnAction(this::cmdButton_Click);
        btnVoid.setOnAction(this::cmdButton_Click);
                
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
        TableColumn index02 = new TableColumn("Barcode");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Brand");
        TableColumn index05 = new TableColumn("Cost");
        TableColumn index06 = new TableColumn("Quantity");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(90);
        index03.setPrefWidth(150); 
        index04.setPrefWidth(150); 
        index05.setPrefWidth(80); index05.setStyle("-fx-alignment: CENTER-RIGHT;");
        index06.setPrefWidth(80); index06.setStyle("-fx-alignment: CENTER-RIGHT;");
        
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
    private final String pxeModuleName = "InvWasteRegController";
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

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            
            case "btnClose":
            case "btnExit":
                unloadForm();
                return;
                
            case "btnPrint": 
                if(!psOldRec.equals("")){
                    ShowMessageFX.Information(null, pxeModuleName, "This feature is coming soon!.");
//                    if(ShowMessageFX.YesNo(null, pxeModuleName, "Do you want to print this transaction?")==true){
////                        if (poTrans.printTransaction(psOldRec))
//                        ShowMessageFX.Information(null, pxeModuleName, "Transaction printed successfully.");
//                        clearFields();
//                        initGrid();
//                        pnEditMode = EditMode.UNKNOWN;
//                        break;
//                    }else
//                        return;
                }else 
                    ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to print!");
                    break;
                
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
                        } else ShowMessageFX.Warning(null, pxeModuleName, poTrans.getMessage());
                    }else return;
                }else 
                   ShowMessageFX.Warning(null, pxeModuleName, "Please select a record to cancel!");
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
//        int lnRow = poTrans.ItemCount();
        pnlRow = poTrans.ItemCount();
        
        data.clear();
        /*ADD THE DETAIL*/
        for(lnCtr = 0; lnCtr <= pnlRow -1; lnCtr++){
            data.add(new TableModel(String.valueOf(lnCtr + 1), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sBarCodex"), 
                                    (String) poTrans.getDetailOthers(lnCtr, "sDescript"),
                                    (String) poTrans.getDetail(lnCtr, "sBrandNme"),
                                    CommonUtils.NumberFormat(Double.valueOf(poTrans.getDetail(lnCtr, "nInvCostx").toString()), "0.00"),
                                    String.valueOf(poTrans.getDetail(lnCtr, "nQuantity")),
                                    "",
                                    "",
                                    "",
                                    ""));
        }
    
        /*FOCUS ON FIRST ROW*/
        if (!data.isEmpty()){
            table.getSelectionModel().select(pnlRow -1);
            table.getFocusModel().focus(pnlRow -1);
            
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
