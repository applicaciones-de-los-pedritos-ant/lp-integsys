

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.rmj.appdriver.GProperty;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.cas.inventory.base.InvMaster;
import org.rmj.lp.parameter.agent.XMBrand;
import org.rmj.lp.parameter.agent.XMModel;

public class DataCapture {
    public static void main(String [] args){
        String lsProdctID = "FoodInv";
        String lsUserIDxx = "M001111122";

        GRider poGRider = new GRider(lsProdctID);
        GProperty loProp = new GProperty("GhostRiderXP");

        if (!poGRider.loadEnv(lsProdctID)) System.exit(0);
        if (!poGRider.logUser(lsProdctID, lsUserIDxx)) System.exit(0);
        
        insertInventory(poGRider);
    }
    
    private static boolean insertBrand(GRider foGRider){
        XMBrand instance = new XMBrand(foGRider, foGRider.getBranchCode(), true);
        
        String [] lasBrand = {"Henry & Sons", "Da Vinci", "Conaprole", "Colavita", "Absolute", "Bing-su", "Del Monte", "Bingsu", "Puratos", "Osterberg", "Nestea", "Twinings", "Jasmine", "Mosa"};
        
        String lsSQL;
        ResultSet loRS;
        
        for (int lnCtr = 0; lnCtr < lasBrand.length; lnCtr++){
            lsSQL = "SELECT * FROM Brand WHERE sDescript = " + SQLUtil.toSQL(lasBrand[lnCtr]);
            
            loRS = foGRider.executeQuery(lsSQL);
            
            try {
                if (!loRS.next()){
                    instance.newRecord();
                    instance.setMaster("sInvTypCd", "RwMt");
                    instance.setMaster("sDescript", lasBrand[lnCtr]);
                    System.out.println(instance.saveRecord());
                }
            } catch (SQLException ex) {
                Logger.getLogger(DataCapture.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return true;
    }
    
    private static boolean insertModel(GRider foGRider){
        XMModel instance = new XMModel(foGRider, foGRider.getBranchCode(), true);
        
        String [] lasModel = {"Bakers Best", "Carte Dor", "Compound", "Dairy Cream", "Filled", "Filling", "Flavocol", "Gold", "Inasal", "Knorr", "Knorr Soup", "Ladys Choice", "Mango", "Mozarella", "Original", "Original Blend", "Original Style", "Quickmelt", "Sarsa", "Singles", "Sweet Style", "Tuna", "Ube", "Unsalted"};
        
        for (int lnCtr = 0; lnCtr < lasModel.length; lnCtr++){
            instance.newRecord();
            instance.setMaster("sInvTypCd", "RwMt");
            instance.setMaster("sDescript", lasModel[lnCtr]);
            System.out.println(instance.saveRecord());
        }
        
        return true;
    }
    
    private static boolean insertInventory(GRider foGRider){
        ResultSet loRS = foGRider.executeQuery("SELECT sStockIDx FROM Inventory");
        
        InvMaster loInvMaster = new InvMaster(foGRider, foGRider.getBranchCode(), true);
        
        try {
            foGRider.beginTrans();
            
            while (loRS.next()){
                loInvMaster.NewRecord();
                if (loInvMaster.SearchInventory(loRS.getString("sStockIDx"), true, true)){
                    loInvMaster.setMaster("sBranchCd", foGRider.getBranchCode());
                    loInvMaster.setMaster("dAcquired", foGRider.getServerDate());
                    loInvMaster.setMaster("dBegInvxx", foGRider.getServerDate());
                    
                    if (!loInvMaster.SaveRecord()){
                        System.err.println(loInvMaster.getErrMsg());
                        foGRider.rollbackTrans();
                        System.exit(1);
                    }
                }
            }
            
            foGRider.commitTrans();
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
            System.exit(1);
        }

        loInvMaster = null;
        loRS = null;
        return true;
    }
}
