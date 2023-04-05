package org.rmj.cas.food.inventory.fx.views.child;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;

/**
 * FXML Controller class
 *
 * @author jovan
 */
public class DashboardController implements Initializable {

    @FXML private VBox VBox;
    @FXML private Button btnExit;
    @FXML private TableView table;
    
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private ObservableList<TableModel> data01 = FXCollections.observableArrayList();
    private final String pxeModuleName = "DashboardController";
    private static GRider poGRider;
    
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private TableColumn index03;
    @FXML private TableColumn index04;
    @FXML private TableColumn index05;
    @FXML private TableView table01;
    @FXML private TableColumn index06;
    @FXML private TableColumn index07;
    @FXML private TableColumn index08;
    @FXML private TableColumn index09;
    @FXML private TableColumn index10;

    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initGridLedger();
        loadDetail2Grid();
        loadExpiredInv();
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private void initGridLedger(){
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;");
        index04.setStyle("-fx-alignment: CENTER;");
        index05.setStyle("-fx-alignment: CENTER;");
        
        index06.setStyle("-fx-alignment: CENTER;");
        index07.setStyle("-fx-alignment: CENTER-LEFT;");
        index08.setStyle("-fx-alignment: CENTER-LEFT;");
        index09.setStyle("-fx-alignment: CENTER;");
        index10.setStyle("-fx-alignment: CENTER;");
     
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index05"));
        
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index01"));
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index02"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index03"));
        index09.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index04"));
        index10.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.child.TableModel,String>("index05"));
        
        table.setItems(data);
        table01.setItems(data01);
    }
    
    /**
     * 
     * @return string SQL query for inventory about to expire
     */
    private String getSQLInventory(){
        return "SELECT" +
            " a.sBarCodex" +
            ",a.sDescript" +
            ",c.dExpiryDt" +
            ",c.nQtyOnHnd" +
            " FROM Inventory a" +
            ",Inv_Master b" +
            ",Inv_Master_Expiration c" +
            " WHERE a.sStockIDx = b.sStockIDx" +
            " AND b.sStockIDx = c.sStockIDx" +
            " AND b.sBranchCd = c.sBranchCd" +
            " AND b.sBranchCd  =" + SQLUtil.toSQL(poGRider.getBranchCode())+
            " AND b.nQtyOnHnd > 0" +
            " AND c.nQtyOnHnd > 0" +
            " AND DATEDIFF(c.dExpiryDt, "+ SQLUtil.toSQL(CommonUtils.xsDateShort(poGRider.getServerDate()))+") BETWEEN 0 AND " +getDateLimit()+
            " ORDER BY c.dExpiryDt, a.sBarCodex;";
        
    }
    
    /**
     * 
     * @return SQL query that expired already
     */
    private String getExpiredInventory(){
        return "SELECT" +
            " a.sBarCodex" +
            ",a.sDescript" +
            ",c.dExpiryDt" +
            ",c.nQtyOnHnd" +
            " FROM Inventory a" +
            ",Inv_Master b" +
            ",Inv_Master_Expiration c" +
            " WHERE a.sStockIDx = b.sStockIDx" +
            " AND b.sStockIDx = c.sStockIDx" +
            " AND b.sBranchCd = c.sBranchCd" +
            " AND b.sBranchCd  =" + SQLUtil.toSQL(poGRider.getBranchCode())+
            " AND b.nQtyOnHnd > 0" +
            " AND c.nQtyOnHnd > 0" +
            " AND c.dExpiryDt < " + SQLUtil.toSQL(CommonUtils.xsDateShort(poGRider.getServerDate()))+
            " ORDER BY c.dExpiryDt, a.sBarCodex;";
        
    }
    
    /**
     * 
     * @return value of days converted to string
     */
    private String getDateLimit(){
        String lsDateLimit= "";
        try {
            ResultSet loRS = null;
            String lsSQL = "SELECT" +
                    "  sValuexxx" +
                    " FROM xxxOtherConfig" +
                    " WHERE sConfigID = 'Expiry'" +
                    " AND sProdctID =" + SQLUtil.toSQL(poGRider.getProductID());
            loRS = poGRider.executeQuery(lsSQL);
            
            while(loRS.next()){
                lsDateLimit = loRS.getString("sValuexxx");
            }
        } catch (SQLException ex) {
            Logger.getLogger(DashboardController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lsDateLimit;
    }
    
    /**
     * 
     * For loading of expired inventory
     */
    public void loadExpiredInv(){
        ResultSet poRS = null;
        data01.clear();
        
        poRS = poGRider.executeQuery(getExpiredInventory());
        if (MiscUtil.RecordCount(poRS) <= 0){
            data01.add(new TableModel(String.valueOf(1), 
                                        "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         ""));
             return;
        }
        try {
            poRS.first();
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRS); lnCtr++){
               
                    poRS.absolute(lnCtr);
                    data01.add(new TableModel(String.valueOf(lnCtr), 
                                poRS.getString("sBarCodex"),
                                poRS.getString("sDescript"),
                                poRS.getString("dExpiryDt"),
                                poRS.getString("nQtyOnHnd"),
                                "",
                                "",
                                "",
                                "",
                                ""));
    }
        } catch (SQLException ex) {
            Logger.getLogger(FoodLedgerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }
    /**
     * For loading of inventory about to expired
     */
    public void loadDetail2Grid() {
        ResultSet poRS = null;
        data.clear();
        
        poRS = poGRider.executeQuery(getSQLInventory());
        if (MiscUtil.RecordCount(poRS) <= 0){
            data.add(new TableModel(String.valueOf(1), 
                                    "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     ""));
        
             return;
        }
        try {
            poRS.first();
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRS); lnCtr++){
                    poRS.absolute(lnCtr);
                    data.add(new TableModel(String.valueOf(lnCtr), 
                                            poRS.getString("sBarCodex"),
                                            poRS.getString("sDescript"),
                                            poRS.getString("dExpiryDt"),
                                            poRS.getString("nQtyOnHnd"),
                                            "",
                                            "",
                                            "",
                                            "",
                                            ""));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FoodLedgerController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }

    @FXML
    private void btnExit_Clicked(MouseEvent event) {
        CommonUtils.closeStage(btnExit);
    }
    
}
