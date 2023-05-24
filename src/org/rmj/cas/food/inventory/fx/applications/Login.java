
package org.rmj.cas.food.inventory.fx.applications;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import org.rmj.cas.food.inventory.fx.views.FoodInventoryFX;
import javafx.application.Application;
import org.rmj.appdriver.GRider;

public class Login {
    
   private static GRider poGRider = null;
    
    public void setGRider(GRider foGRider){poGRider = foGRider;}
    
    public static void main(String [] args){       
        String path;
        if(System.getProperty("os.name").toLowerCase().contains("win")){
            path = "D:/GGC_Java_Systems";
        }
        else{
            path = "/srv/GGC_Java_Systems";
        }
        System.setProperty("sys.default.path.config", path);
        
        if (!loadProperties()){
            System.err.println("Unable to load config.");
            System.exit(1);
        } else System.out.println("Config file loaded successfully.");
        
        String lsProdctID = args[0];
        String lsUserIDxx = args[1];
        
        GRider poGRider = new GRider(lsProdctID);

        if (!poGRider.loadEnv(lsProdctID)) {
            System.err.println(poGRider.getErrMsg());
            System.exit(1);
        }
        if (!poGRider.logUser(lsProdctID, lsUserIDxx)) {
            System.err.println(poGRider.getErrMsg());
            System.exit(1);
        } 
        
        if (!loadProperties()){
            System.err.println("Unable to load config.");
            System.exit(1);
        } else System.out.println("Config file loaded successfully.");       
        
        FoodInventoryFX Login = new FoodInventoryFX();
        Login.setGRider(poGRider);
        
        Application.launch(Login.getClass());
    } 
    
    private static boolean loadProperties(){
        try {
            Properties po_props = new Properties();
            po_props.load(new FileInputStream("D:\\GGC_Java_Systems\\config\\rmj.properties"));
            
            System.setProperty("app.debug.mode", po_props.getProperty("app.debug.mode"));
            System.setProperty("user.id", po_props.getProperty("user.id"));
            System.setProperty("app.product.id", po_props.getProperty("app.product.id"));
            
            if (System.getProperty("app.product.id").equalsIgnoreCase("integsys")){
                System.setProperty("pos.clt.nm", po_props.getProperty("pos.clt.nm.integsys"));              
            } else{
                System.setProperty("pos.clt.nm", po_props.getProperty("pos.clt.nm.telecom"));         
            }
            
            System.setProperty("pos.clt.tin", po_props.getProperty("pos.clt.tin"));        
            System.setProperty("pos.clt.crm.no", po_props.getProperty("pos.clt.crm.no"));        
            System.setProperty("pos.clt.dir.ejournal", po_props.getProperty("pos.clt.dir.ejournal"));    
            
            //store info
            System.setProperty("store.commissary", po_props.getProperty("store.commissary"));
            System.setProperty("store.inventory.type", po_props.getProperty("store.inventory.type"));
            System.setProperty("store.inventory.strict.type", po_props.getProperty("store.inventory.strict.type"));
            
            //UI
            System.setProperty("app.product.id.grider", po_props.getProperty("app.product.id.grider"));
            System.setProperty("app.product.id.general", po_props.getProperty("app.product.id.general"));
            System.setProperty("app.product.id.integsys", po_props.getProperty("app.product.id.integsys"));
            System.setProperty("app.product.id.telecom", po_props.getProperty("app.product.id.telecom"));
            
            return true;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
    }   
}
