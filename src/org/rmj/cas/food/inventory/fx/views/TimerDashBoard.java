package org.rmj.cas.food.inventory.fx.views;

import java.io.IOException;
import java.util.TimerTask;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.cas.food.inventory.fx.views.child.DashboardController;

/**
 *
 * @author user
 */
public class TimerDashBoard extends TimerTask{
    Notifications notifBuilder = null;
    
    public TimerDashBoard(){
        Image img = new Image("org/rmj/cas/food/inventory/fx/images/check.png");
           notifBuilder = Notifications.create()
            .title("Inventory Expiration Notice")
            .text("Kindly check inventory about to expire!")
            .graphic(new ImageView(img))
            .hideAfter(Duration.seconds(3))
            .position(Pos.BOTTOM_RIGHT)
            .onAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                    DashboardController dashboard = new DashboardController();
                    dashboard.setGRider(poGRider);

                    FXMLLoader fxmlLoader = new FXMLLoader();
                    fxmlLoader.setLocation(getClass().getResource("child/Dashboard.fxml"));
                    fxmlLoader.setController(dashboard);

                    Parent parent;
                    try {
                        parent = fxmlLoader.load();
                        Stage stage = new Stage();
                        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                xOffset = event.getSceneX();
                                yOffset = event.getSceneY();
                            }
                        });

                        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent event) {
                                stage.setX(event.getScreenX() - xOffset);
                                stage.setY(event.getScreenY() - yOffset); 

                            }
                        });

                        Scene scene = new Scene(parent);
                        stage.initModality(Modality.APPLICATION_MODAL);
                        stage.initStyle(StageStyle.UNDECORATED);
                        stage.setAlwaysOnTop(true);
                        stage.setScene(scene);
                        stage.showAndWait();

                    } catch (IOException ex) {
                        ShowMessageFX.Error(ex.getMessage(), pxeModuleName, "Please inform MIS department.");
                        ex.printStackTrace();
                        System.exit(1);
                    }
            }
        });
    }
    

    @Override
    public void run() {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notifBuilder.darkStyle();
                notifBuilder.showConfirm();
            }
        });
       
        
    }
    
    public void setGRider(GRider fsGRider){
        this.poGRider = fsGRider;
    };
    private static GRider poGRider;
    private final String pxeModuleName = "TimerDashboad";
    private double xOffset = 0; 
    private double yOffset = 0;
    
}
