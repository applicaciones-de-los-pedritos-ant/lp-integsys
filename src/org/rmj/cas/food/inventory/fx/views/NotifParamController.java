package org.rmj.cas.food.inventory.fx.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;

        
public class NotifParamController implements Initializable {
    
    @FXML private TextField txtField02;
    @FXML private VBox vBoxForm;
    @FXML private Button btnOkay;
    @FXML private Button btnCancel;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    txtField02.setOnKeyPressed(this::txtField_KeyPressed);
    txtField02.setText(CommonUtils.xsDateMedium(poGRider.getServerDate()));
    txtField02.requestFocus();
    
    txtField02.setText(String.valueOf(Integer.valueOf(getNotifKey())/pnMultiplier));
    pbLoaded = true;
    }    

    @FXML
    private void btnOkay_Click(ActionEvent event) {
        pbCancelled = !UpdateNotifSettings();
        if (!pbCancelled){
            ShowMessageFX.Information(null, pxeModuleName, "Record successfully saved!");
            unloadScene();
        }
    }
    /**
     * handling closing of this stage
     */
    private void unloadScene(){
        VBox myBox = (VBox) vBoxForm.getParent();
        myBox.getChildren().clear();
    }

    @FXML
    private void btnCancel_Click(ActionEvent event) {
        psMessagex= "Notifcaiton time update cancelled.";
        pbCancelled = true;
        unloadScene();
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        
        switch(event.getCode()){
            case DOWN:
            case ENTER:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
    }
    
    private double xOffset = 0; 
    private double yOffset = 0;
    private final int pnMultiplier = 6000;
    
    private GRider poGRider;
    private boolean pbCancelled = true;
    private String psMessagex = "";
    private Integer pnIndex = -1;
    private boolean pbLoaded = false;
    private String pxeModuleName = "Notification Settings";
    
    public void setGRider(GRider foGRider){poGRider = foGRider;}
    public boolean isCancelled(){return pbCancelled;}
    public String getMessage(){return psMessagex;}
    public String getNotifKey(){return CommonUtils.getConfiguration(poGRider, "AppNotif");}
    
    private boolean UpdateNotifSettings() {
        if (poGRider == null){
            psMessagex = "Application Driver is not set.";
            return false;
        }
        
        int lsEntry;
        int lsValue;
        try{
            lsEntry = Integer.parseInt(txtField02.getText());
        }catch(NumberFormatException e){
            psMessagex = e.toString();
            ShowMessageFX.Error(null, pxeModuleName, psMessagex);
            txtField02.requestFocus();
            return false;
        }
        
        poGRider.beginTrans();
        
        lsValue= lsEntry * 6000;
        String lsSQL = "UPDATE xxxOtherConfig" +
                    " SET sValuexxx = "+ SQLUtil.toSQL(lsValue) +
                    " WHERE sProdctID = "+ SQLUtil.toSQL(poGRider.getProductID()) +
                    " AND sConfigID = 'AppNotif'";
        poGRider.executeQuery(lsSQL, "xxxOtherConfig", poGRider.getBranchCode(), "");
        
        poGRider.commitTrans();
        
        return true;
    }
    
}
