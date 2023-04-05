package org.rmj.cas.food.inventory.fx.views;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.cas.inventory.base.SerialUpload;
import java.io.File;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class SerialUploadController implements Initializable {

    @FXML private Button btnOkay;
    @FXML private Button btnCancel;
    @FXML private TextField txtField01;
    @FXML private AnchorPane dataPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        
        txtField01.requestFocus();
    }    

    @FXML
    private void btnOkay_Click(ActionEvent event) {
        if (txtField01.getText().isEmpty()) {
            ShowMessageFX.Warning(null, "Notice", "Invalid file location.");
            return;
        }
        
        
        SerialUpload instance = new SerialUpload(poGRider);
        instance.setLocation(txtField01.getText());
        
        if (!instance.Process()){
            ShowMessageFX.Warning(null, "Notice", instance.getMessage());
            return;
        }
        
        ShowMessageFX.Information(null, "Success", "File data uploaded.");
        
        pbCancelled = false;
        unloadForm();
    }

    @FXML
    private void btnCancel_Click(ActionEvent event) {
        pbCancelled = true;
        unloadForm();
    }
    
    private void unloadForm(){
//        VBox myBox = (VBox) VBoxForm.getParent();
//        myBox.getChildren().clear();
           dataPane.getChildren().clear();
           dataPane.setStyle("-fx-border-color: transparent");
    }
    
    public void setGRider(GRider foApp){
        poGRider = foApp;
    }
    
    public void txtField_KeyPressed(KeyEvent event) {        
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        JSONObject loJSON;
             
        if (event.getCode() == F3){                
            switch (lnIndex){                
                case 1: 
                    FileChooser fileChooser = new FileChooser();
                    fileChooser.setTitle("Open Resource File");
                    fileChooser.getExtensionFilters().addAll(
                            new FileChooser.ExtensionFilter("Excel File", "*.xlsx"));
                    
                    File selectedFile = fileChooser.showOpenDialog((Stage) txtField01.getScene().getWindow());
 
                    if (selectedFile != null) {
                        txtField01.setText(selectedFile.getPath());
                     }
                    break;
            }
        }
        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
                break;
            default:
                break;
        }
    }
    
    private boolean pbCancelled = true;
    private GRider poGRider;
    
    private final String pxeModuleName = this.getClass().getSimpleName();
}
