/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.callback.IFXML;
import org.rmj.cas.inventory.base.InvMaster;
import org.rmj.purchasing.agent.AutoVoidTrans;
import org.rmj.purchasing.agent.POReceiving;

/**
 * FXML Controller class
 *
 * @author User
 */
public class AutoVoidTransController implements Initializable, IFXML {
    private GRider oApp;
    private AutoVoidTrans oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "SP Recalculate"; //Utility
    
    @FXML private Button btnExit;
    @FXML private Button btnVoid;
    @FXML private Button btnClose;
    
    private Stage getStage(){
         return (Stage) btnVoid.getScene().getWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
        oTrans = new AutoVoidTrans(oApp, oApp.getBranchCode(), false);
        
        btnExit.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnVoid.setOnAction(this::cmdButton_Click);
    }    
    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnVoid": 
                
                if (oTrans.voidTransaction()) {
                    ShowMessageFX.Information(oTrans.getMessage(), pxeModuleName, "Transactions successfully void!");
                    
                    pnEditMode = oTrans.getEditMode();
                } else {
                    ShowMessageFX.Warning(null, pxeModuleName, oTrans.getMessage());
                    return;
                }
                break;
            case "btnClose":
                CommonUtils.closeStage(btnClose);
                break;
            case "btnExit":
                CommonUtils.closeStage(btnExit);
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                break;

        }
    }
    
    
}
