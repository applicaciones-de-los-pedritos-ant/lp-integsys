package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.ResourceBundle;
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
import javafx.scene.layout.VBox;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;

import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.constants.EditMode;

import org.rmj.cas.inventory.base.InvAdjustment;

/**
 * FXML Controller class
 *
 * @author user
 */
public class InvAdjustmentRegController implements Initializable {

    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01;
    @FXML private TextField txtField02;
    @FXML private TextArea txtField04;
    @FXML private TextField txtDetail07;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail80;
    @FXML private TextField txtDetail04;
    @FXML private TextField txtDetail05;
    @FXML private TextField txtDetail06;
    @FXML private TableView table;
    @FXML private ImageView imgTranStat;
    @FXML private TextField txtField50;
    @FXML private TableView tableDetail;
    @FXML private Button btnClose;
    @FXML private Button btnBrowse;
    @FXML private AnchorPane dataPane;
    
    private static GRider poGRider;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        poTrans = new InvAdjustment(poGRider, poGRider.getBranchCode(), false);
        poTrans.setTranStat(1234);
        
        btnClose.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);
        
        txtField50.setOnKeyPressed(this::txtField_KeyPressed);
        
        pnEditMode = EditMode.UNKNOWN;    
        clearFields();
        
        initGrid();
        initLisView();
        
        pbLoaded = true;
        
    }  
    
    private void initGrid(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Barcode.");
        TableColumn index03 = new TableColumn("Description");
        TableColumn index04 = new TableColumn("Expiry Date");
        TableColumn index05 = new TableColumn("Credt Qty");
        TableColumn index06 = new TableColumn("Debt Qty");
        
        
        index01.setPrefWidth(50); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(100);
        index03.setPrefWidth(142); 
        index04.setPrefWidth(100); index04.setStyle("-fx-alignment: CENTER;");
        index05.setPrefWidth(80); index05.setStyle("-fx-alignment: CENTER;");
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

    @FXML
    private void table_Clicked(MouseEvent event) {
        pnRow = table.getSelectionModel().getSelectedIndex();
        getRecordData(pnRow);
        setDetailInfo(pnRow);
        
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    private InvAdjustment poTrans;
    
    private int pnEditMode = -1;
    private boolean pbLoaded = false;
    
    private final String pxeDateFormat = "yyyy-MM-dd";
    private final String pxeDateDefault = java.time.LocalDate.now().toString();
    
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private ObservableList<TableModel> dataDetail =  FXCollections.observableArrayList();
    
    private int pnIndex = -1;
    private int pnRow = -1;
    private int pnOldRow = -1;
    
    private String psOldRec = "";
    private String psTransNox = "";
    private String psdTransact = "";
    private final String pxeModuleName = "Inventory Adjustment Controller";
    
    private void getRecordData(int fnRow){
        ResultSet loRS = null;
        loRS = poTrans.getExpiration((String)poTrans.getDetail(fnRow, "sStockIDx"));
        try {
                dataDetail.clear();
                loRS.first();
                for( int rowCount = 0; rowCount <= MiscUtil.RecordCount(loRS) -1; rowCount++){
                    dataDetail.add(new TableModel(String.valueOf(rowCount +1),
                        String.valueOf(CommonUtils.xsDateMedium(loRS.getDate("dExpiryDt"))),
                        String.valueOf(loRS.getDouble("nQtyOnHnd")),
                        "",
                        "",
                        "",
                        "",
                        "",
                        "",
                        ""
                    ));
                      loRS.next();
                }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
                
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
                    default:
                        ShowMessageFX.Warning("No Entry", pxeModuleName, "Please have at least one keyword to browse!");
                }
                
                return;
                
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
    }
    
    private void clearFields(){
        txtField01.setText("");
        txtField02.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
        txtField04.setText("");
        txtField50.setText("");
        
        txtDetail03.setText("");
        txtDetail04.setText("");
        txtDetail05.setText("");
        txtDetail05.setText("0");
        txtDetail06.setText("0.00");
        txtDetail07.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
        txtDetail80.setText("");
        
        pnRow = -1;
        pnOldRow = -1;
        pnIndex = 51;
        setTranStat("-1");
        psOldRec = "";
        psTransNox = "";
        psdTransact = "";
        data.clear();
        dataDetail.clear();
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
    
    private void initLisView(){
        TableColumn index01 = new TableColumn("No.");
        TableColumn index02 = new TableColumn("Expiration");
        TableColumn index03 = new TableColumn("OnHnd");
        
        index01.setPrefWidth(30); index01.setStyle("-fx-alignment: CENTER;");
        index02.setPrefWidth(120); index02.setStyle("-fx-alignment: CENTER;");
        index03.setPrefWidth(90); index03.setStyle("-fx-alignment: CENTER;");
        
        index01.setSortable(false); index01.setResizable(false);
        index02.setSortable(true); index02.setResizable(false);
        index03.setSortable(false); index03.setResizable(false);

        tableDetail.getColumns().clear();
        tableDetail.getColumns().add(index01);
        tableDetail.getColumns().add(index02);
        tableDetail.getColumns().add(index03);
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        
        tableDetail.setItems(dataDetail);
        
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
          
          psdTransact = CommonUtils.xsDateMedium(CommonUtils.toDate(poTrans.getMaster("dTransact").toString()));
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        
        txtField04.setText((String) poTrans.getMaster("sRemarksx"));
        setTranStat((String) poTrans.getMaster("cTranStat"));
        
        pnRow = 0;
        pnOldRow = 0;
        loadDetail();
        
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
                                    CommonUtils.xsDateMedium((Date) poTrans.getDetail(lnCtr, "dExpiryDt")),
                                    String.valueOf(poTrans.getDetail(lnCtr, "nCredtQty")),
                                    String.valueOf(poTrans.getDetail(lnCtr, "nDebitQty")),
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
            txtDetail03.setText((String) poTrans.getDetailOthers(fnRow, "sBarCodex"));
            txtDetail80.setText((String) poTrans.getDetailOthers(fnRow, "sDescript"));
            txtDetail04.setText(String.valueOf(poTrans.getDetail(fnRow, "nCredtQty")));
            txtDetail05.setText(String.valueOf(poTrans.getDetail(fnRow, "nDebitQty")));
            txtDetail06.setText(String.valueOf(poTrans.getDetail(fnRow, "nInvCostx")));
            txtDetail07.setText(CommonUtils.xsDateMedium((Date) poTrans.getDetail(fnRow, "dExpiryDt")));
            txtDetail03.requestFocus();
        } else{
            txtDetail03.setText("");
            txtDetail04.setText("0");
            txtDetail05.setText("0");
            txtDetail06.setText("0.00");
            txtDetail07.setText(CommonUtils.xsDateLong((Date) java.sql.Date.valueOf(LocalDate.now())));
            txtDetail80.setText("");
            txtDetail03.requestFocus();
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
