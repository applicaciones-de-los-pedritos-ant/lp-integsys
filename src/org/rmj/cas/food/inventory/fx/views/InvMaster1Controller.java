package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import static javafx.scene.input.KeyCode.F3;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.cas.inventory.base.InvMaster;

public class InvMaster1Controller implements Initializable {
    
    @FXML private VBox VBoxForm;
    @FXML private Button btnExit;
    @FXML private AnchorPane anchorField;
    @FXML private TextField txtField01;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    @FXML private TextField txtField04;
    @FXML private TextField txtField05;
    @FXML private TextField txtField06;
    @FXML private TextField txtField07;
    @FXML private TextField txtField08;
    @FXML private TextField txtField09;
    @FXML private TextField txtField10;
    @FXML private TextField txtField11;
    @FXML private TextField txtField12;
    @FXML private TextField txtField13;
    @FXML private TextField txtField14;
    @FXML private TextField txtField15;
    @FXML private TextField txtField16;
    @FXML private TextField txtField17;
    @FXML private TextField txtField18;
    @FXML private TextField txtField19;
    @FXML private ComboBox Combo23;
    @FXML private ComboBox Combo24;
    @FXML private CheckBox Check20;
    @FXML private CheckBox Check21;
    @FXML private CheckBox Check22;
    @FXML private CheckBox Check26;
    @FXML private Button btnSave;
    @FXML private Button btnUpdate;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnBrowse;
    @FXML private Button btnNew;
    @FXML private Button btnActivate;
    @FXML private TextField txtField25;
    @FXML private FontAwesomeIconView glyphExit;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Initialize class*/
        poRecord = new InvMaster(poGRider, poGRider.getBranchCode(), false);
                
        /*Set action event handler for the buttons*/
        btnBrowse.setOnAction(this::cmdButton_Click);
        btnCancel.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnExit.setOnAction(this::cmdButton_Click);
        btnNew.setOnAction(this::cmdButton_Click);
        btnSave.setOnAction(this::cmdButton_Click);
        btnSearch.setOnAction(this::cmdButton_Click);
        btnUpdate.setOnAction(this::cmdButton_Click);
        btnActivate.setOnAction(this::cmdButton_Click);               
        
        pnEditMode = EditMode.UNKNOWN;
        
        clearFields();
        initButton(pnEditMode);
        
        pbLoaded = true;
    }
    
    private void txtDetail_KeyPressed(KeyEvent event){
        TextField txtDetail = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8, 10));
        
        if (event.getCode() == F3 || event.getCode() == ENTER){
            String lsResult = "";
            switch (lnIndex){
                case 1:
                    break;
                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with index " + lnIndex + " not registered for QuickSearch.");
                    return;
            }
            
            switch (event.getCode()){
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtDetail);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtDetail);
            }
        }
    }   
        
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            case "btnBrowse":
                if (poRecord.SearchInventory("%", false, true)) loadRecord();
                break;
            case "btnCancel":
                clearFields();
                pnEditMode = EditMode.UNKNOWN;
                break;
            case "btnClose":
            case "btnExit":
                unloadForm();
                return;
            case "btnNew":
                if (poRecord.NewRecord()) loadRecord(); pnEditMode = poRecord.getEditMode();
                break;
            case "btnSave":
                /*if (poRecord.SaveRecord()){
                    if (poRecord.OpenRecord(psOldRec)) loadRecord();
                    ShowMessageFX.Information(null, pxeModuleName, "Record Save Successfully.");
                }*/
                break;
            case "btnSearch":
                break;
            case "btnUpdate":
                /*if (poRecord.getMaster(1) != null && !poRecord.getMaster(1).toString().equals("")){
                    if (poRecord.UpdateRecord()){
                        pnEditMode = poRecord.getEditMode();
                    }
                }*/
                break;
            case "btnActivate":
                /*if (poRecord.getMaster(1) != null && !poRecord.getMaster(1).toString().equals("")){
                    if (btnActivate.getText().equals("Activate")){
                        if (poRecord.ActivateRecord(poRecord.getMaster(1).toString())){
                            if (poRecord.OpenRecord(psOldRec)) loadRecord();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Activated Successfully.");
                        }
                    } else{
                        if (poRecord.DeactivateRecord(poRecord.getMaster(1).toString())){
                            if (poRecord.OpenRecord(psOldRec)) loadRecord();
                            ShowMessageFX.Information(null, pxeModuleName, "Record Deactivated Successfully.");
                        }
                            
                    }
                }*/
                break;
            default:
                ShowMessageFX.Warning(null, pxeModuleName, "Button with name " + lsButton + " not registered.");
                return;
        }
        
        initButton(pnEditMode);
    } 
    
    private void unloadForm(){
        VBox myBox = (VBox) VBoxForm.getParent();
        myBox.getChildren().clear();
    }
    
    private void initButton(int fnValue){
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        
        btnCancel.setVisible(lbShow);
        btnSearch.setVisible(lbShow);
        btnSave.setVisible(lbShow);
                
        btnClose.setVisible(!lbShow);
        btnBrowse.setVisible(!lbShow);
        btnActivate.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
    }
    
    private void clearFields(){
        txtField01.setText("");
        txtField02.setText("");
        txtField03.setText("");
        txtField04.setText("");
        txtField05.setText("");
        txtField06.setText("");
        txtField07.setText("");
        txtField08.setText("");
        txtField09.setText("");
        txtField10.setText("");
        txtField11.setText("");
        txtField12.setText("");
        txtField13.setText("");
        txtField14.setText("0.0");
        txtField15.setText("0.0");
        txtField16.setText("0.0");
        txtField17.setText("0.0");
        txtField18.setText("0.0");
        txtField19.setText("0.0");
        
        Combo23.getSelectionModel().select(0);
        Combo24.getSelectionModel().select(0);
        
        Check20.selectedProperty().setValue(false);
        Check21.selectedProperty().setValue(false);
        Check22.selectedProperty().setValue(false);
        Check26.selectedProperty().setValue(false);
        btnActivate.setText("Activate");
        
        psOldRec = "";
    }
    
    private void loadRecord(){
        /*
        txtField01.setText((String) poRecord.getMaster(1));
        txtField02.setText((String) poRecord.getMaster(2));
        txtField03.setText((String) poRecord.getMaster(3));
        txtField04.setText((String) poRecord.getMaster(4));
        txtField05.setText((String) poRecord.getMaster(5));
        
        txtField06.setText(poRecord.SearchMaster(6, (String) poRecord.getMaster(6), true));
        txtField07.setText(poRecord.SearchMaster(7, (String) poRecord.getMaster(7), true));
        txtField08.setText(poRecord.SearchMaster(8, (String) poRecord.getMaster(8), true));
        txtField09.setText(poRecord.SearchMaster(9, (String) poRecord.getMaster(9), true));
        txtField10.setText(poRecord.SearchMaster(10, (String) poRecord.getMaster(10), true));
        txtField11.setText(poRecord.SearchMaster(11, (String) poRecord.getMaster(11), true));
        txtField12.setText(poRecord.SearchMaster(12, (String) poRecord.getMaster(12), true));
        txtField13.setText(poRecord.SearchMaster(13, (String) poRecord.getMaster(13), true));

        txtField14.setText(String.valueOf(poRecord.getMaster(14)));
        txtField15.setText(String.valueOf(poRecord.getMaster(15)));
        txtField16.setText(String.valueOf(poRecord.getMaster(16)));
        txtField17.setText(String.valueOf(poRecord.getMaster(17)));
        txtField18.setText(String.valueOf(poRecord.getMaster(18)));
        txtField19.setText(String.valueOf(poRecord.getMaster(19)));
        
        Combo23.getSelectionModel().select(Integer.parseInt(poRecord.getMaster("cUnitType").toString()));
        Combo24.getSelectionModel().select(Integer.parseInt(poRecord.getMaster("cInvStatx").toString()));
                
        boolean lbCheck;

        lbCheck = poRecord.getMaster("cComboInv").toString().equals("1");
        Check20.selectedProperty().setValue(lbCheck);

        lbCheck = poRecord.getMaster("cWthPromo").toString().equals("1");
        Check21.selectedProperty().setValue(lbCheck);

        lbCheck = poRecord.getMaster("cSerialze").toString().equals("1");
        Check22.selectedProperty().setValue(lbCheck);

        lbCheck = poRecord.getMaster("cRecdStat").toString().equals("1");
        Check26.selectedProperty().setValue(lbCheck);
        
        if (poRecord.getMaster("cRecdStat").toString().equals("1"))
            btnActivate.setText("Deactivate");
        else
            btnActivate.setText("Activate");
        */
        psOldRec = txtField01.getText();
        pnEditMode = EditMode.READY;
    }    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private final String pxeModuleName = "InvMasterController";
    private static GRider poGRider;
    private InvMaster poRecord;
    
    private int pnEditMode;
    private boolean pbLoaded = false;
    private String psOldRec;
    
    ObservableList<String> cUnitType = FXCollections.observableArrayList("Demo", "Regular", "Repo");
    ObservableList<String> cInvStatx = FXCollections.observableArrayList("Inactive", "Active", "Limited Inv.",
                                                                            "Push Product", "Stop Production");
   
    final ChangeListener<? super Boolean> txtField_Focus = (o,ov,nv)->{
        if (!pbLoaded) return;
        
        TextField txtField = (TextField)((ReadOnlyBooleanPropertyBase)o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        
        if (lsValue == null) return;
            
        if(!nv){ /*Lost Focus*/
            switch (lnIndex){
                case 1:
                    break;
                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
            
            //poRecord.setMaster(lnIndex, lsValue);
            //txtField.setText((String)poRecord.getMaster(lnIndex));
        } else
            txtField.selectAll();    
    };

}
