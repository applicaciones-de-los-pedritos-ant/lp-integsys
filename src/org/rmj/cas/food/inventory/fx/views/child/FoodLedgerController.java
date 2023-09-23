
package org.rmj.cas.food.inventory.fx.views.child;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;

public class FoodLedgerController implements Initializable {

    @FXML private Button btnExit;
    @FXML private TextField txtField03;
    @FXML private TextField txtField80;
    @FXML private TextField txtField82;
    @FXML private TextField txtField81;
    @FXML private Button btnOk;
    @FXML private TableView table;
    @FXML private VBox VBox;
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private TableColumn index03;
    @FXML private TableColumn index04;
    @FXML private TableColumn index05;
    @FXML private TableColumn index06;
    @FXML private TableColumn index07;
    @FXML private TableColumn index08;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        btnExit.setOnAction(this::cmdButton_Click);
        btnOk.setOnAction(this::cmdButton_Click);
        
        txtField03.setText(sBarCodex);
        txtField80.setText(sDescript);
        txtField81.setText(sBriefDes);
        txtField82.setText(sBrandNme);
        initGridLedger();
        loadDetail2Grid();
    } 
    
    public void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
               
        switch (lsButton){
           case "btnExit":  //Close
                CommonUtils.closeStage(btnExit);
            break;
            
           case "btnOk": //OK
                CommonUtils.closeStage(btnOk);
            break;
            
               default: 
                   ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;        
        }     
    }
    
     private void initGridLedger(){
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;");
        index05.setStyle("-fx-alignment: CENTER;");
        index06.setStyle("-fx-alignment: CENTER;");
        index07.setStyle("-fx-alignment: CENTER;");
        index08.setStyle("-fx-alignment: CENTER-LEFT;");
     
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index06"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index07"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index08"));
        
        table.setItems(data);
    }
    
    public void loadDetail2Grid() {               
        data.clear();
        
        if (poRS == null) return;
        
        try {
            poRS.first();
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRS); lnCtr++){
               
                    poRS.absolute(lnCtr);
                    data.add(new TableModel(String.valueOf(lnCtr), 
                                            poRS.getString("dTransact"),
                                            poRS.getString("sDescript"),
                                            poRS.getString("sSourceNo"),
                                            poRS.getString("nQtyInxxx"),
                                            poRS.getString("nQtyOutxx"),
                                            poRS.getString("nQtyOnHnd"),
                                            poRS.getString("sBranchNm"),
                                            "",
                                            ""));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FoodLedgerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }
    
private ObservableList<TableModel> data = FXCollections.observableArrayList();
    public void setBarCodex(String fsBarCodex){sBarCodex = fsBarCodex;}
    public void setDescript(String fsDescript){sDescript = fsDescript;}
    public void setBriefDes(String fsBriefDes){sBriefDes = fsBriefDes;}
    public void setBrandNme(String fsBrandNme){sBrandNme = fsBrandNme;}
    
    public String getBarCodex(){return sBarCodex;}
    public String getDescript(){return sDescript;}
    public String getBriefDes(){return sBriefDes;}
    public String getBrandNme(){return sBrandNme;}
    
    public void setHistory(ResultSet foRS){this.poRS = foRS;}
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
   
    private static GRider poGRider;
    private final String pxeModuleName = "FoodLedgerController";
    private static String sBriefDes = "";
    private static String sBarCodex = "";
    private static String sDescript = "";
    private static String sBrandNme = "";
    private ResultSet poRS = null;
}
