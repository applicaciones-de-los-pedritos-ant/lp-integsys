
package org.rmj.cas.food.inventory.fx.applications;

import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.rmj.appdriver.GRider;
import org.rmj.cas.food.inventory.fx.views.MDIMainController;


public class FoodInventoryFX extends Application {
    public final static String pxeMainFormTitle = "Food Inventory FX v1.0";
    public final static String pxeMainForm = "../views/MDIMain.fxml";
    public final static String pxeStageIcon = "org/rmj/foodinventoryfx/images/ic_launcher1.png";
    public static GRider poGRider;
    
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource(pxeMainForm));
        
        //get the controller of the main interface
        MDIMainController loControl = new MDIMainController();
        //set the GRider Application Driver to the controller
        loControl.setGRider(poGRider);
        
        //the controller class to the main interface
        fxmlLoader.setController(loControl);
        
        //load the main interface
        Parent parent = fxmlLoader.load();
        //set the main interface as the scene
        Scene scene = new Scene(parent);
        
        //get the screen size
        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();
        
        stage.setScene(scene);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.getIcons().add(new Image(pxeStageIcon));
        
        switch (poGRider.getClientID().substring(0, 3)){
            case "GTC":
                stage.setTitle(System.getProperty("app.product.id.telecom"));
                break;
            case "GGC":
                stage.setTitle(System.getProperty("app.product.id.integsys"));
                break;
            default:
                stage.setTitle(pxeMainFormTitle);
        }
        
        //set stage as maximized but not full screen
        stage.setX(bounds.getMinX());
        stage.setY(bounds.getMinY());
        stage.setWidth(bounds.getWidth());
        stage.setHeight(bounds.getHeight());
        
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public void setGRider(GRider foGRider){
        this.poGRider = foGRider;
    }
    
}
