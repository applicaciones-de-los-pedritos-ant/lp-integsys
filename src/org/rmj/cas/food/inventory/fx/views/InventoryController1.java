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
import javafx.scene.control.Label;
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
import org.rmj.cas.inventory.base.Inventory;

public class InventoryController1 implements Initializable {

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
    @FXML private Label lblHeader;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        /*Initialize class*/
        poRecord = new Inventory(poGRider, poGRider.getBranchCode(), false);
                
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
                
        /*Add listener to text fields*/
        txtField02.focusedProperty().addListener(txtField_Focus);
        txtField03.focusedProperty().addListener(txtField_Focus);
        txtField04.focusedProperty().addListener(txtField_Focus);
        txtField05.focusedProperty().addListener(txtField_Focus);
        txtField06.focusedProperty().addListener(txtField_Focus);
        txtField07.focusedProperty().addListener(txtField_Focus);
        txtField08.focusedProperty().addListener(txtField_Focus);
        txtField09.focusedProperty().addListener(txtField_Focus);
        txtField10.focusedProperty().addListener(txtField_Focus);
        txtField11.focusedProperty().addListener(txtField_Focus);
        txtField12.focusedProperty().addListener(txtField_Focus);
        txtField13.focusedProperty().addListener(txtField_Focus);
        txtField14.focusedProperty().addListener(txtField_Focus);
        txtField15.focusedProperty().addListener(txtField_Focus);
        txtField16.focusedProperty().addListener(txtField_Focus);
        txtField17.focusedProperty().addListener(txtField_Focus);
        txtField18.focusedProperty().addListener(txtField_Focus);
        txtField19.focusedProperty().addListener(txtField_Focus);
        txtField25.focusedProperty().addListener(txtField_Focus);
        
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        txtField03.setOnKeyPressed(this::txtField_KeyPressed);
        txtField04.setOnKeyPressed(this::txtField_KeyPressed);
        txtField05.setOnKeyPressed(this::txtField_KeyPressed);
        txtField06.setOnKeyPressed(this::txtField_KeyPressed);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);
        txtField08.setOnKeyPressed(this::txtField_KeyPressed);
        txtField09.setOnKeyPressed(this::txtField_KeyPressed);
        txtField10.setOnKeyPressed(this::txtField_KeyPressed);
        txtField11.setOnKeyPressed(this::txtField_KeyPressed);
        txtField12.setOnKeyPressed(this::txtField_KeyPressed);
        txtField13.setOnKeyPressed(this::txtField_KeyPressed);
        txtField14.setOnKeyPressed(this::txtField_KeyPressed);
        txtField15.setOnKeyPressed(this::txtField_KeyPressed);
        txtField16.setOnKeyPressed(this::txtField_KeyPressed);
        txtField17.setOnKeyPressed(this::txtField_KeyPressed);
        txtField18.setOnKeyPressed(this::txtField_KeyPressed);
        txtField19.setOnKeyPressed(this::txtField_KeyPressed);
        txtField25.setOnKeyPressed(this::txtField_KeyPressed);
        
        Combo23.setOnKeyPressed(this::ComboBox_KeyPressed);
        Combo24.setOnKeyPressed(this::ComboBox_KeyPressed);
        
        Check20.setOnKeyPressed(this::Check_KeyPressed);
        Check21.setOnKeyPressed(this::Check_KeyPressed);
        Check22.setOnKeyPressed(this::Check_KeyPressed);
        Check26.setOnKeyPressed(this::Check_KeyPressed);
        
        Combo23.setItems(cUnitType);
        Combo23.getSelectionModel().select(1);
        
        Combo24.setItems(cInvStatx);
        Combo24.getSelectionModel().select(1);
        
        pnEditMode = EditMode.UNKNOWN;
        
        clearFields();
        initButton(pnEditMode);
        
        pbLoaded = true;
    }
    
    private void ComboBox_KeyPressed(KeyEvent event){
        ComboBox cmbBox = (ComboBox)event.getSource();
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(cmbBox);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(cmbBox);
        }
    }
    
    private void Check_KeyPressed(KeyEvent event){
        CheckBox chkBox = (CheckBox)event.getSource();
        
        switch (event.getCode()){
        case ENTER:
        case DOWN:
            CommonUtils.SetNextFocus(chkBox);
            break;
        case UP:
            CommonUtils.SetPreviousFocus(chkBox);
        }
    }
    
    private void txtField_KeyPressed(KeyEvent event){
        TextField txtField = (TextField)event.getSource();
        int lnIndex = Integer.parseInt(((TextField)event.getSource()).getId().substring(8, 10));
        
        if (event.getCode() == F3 || event.getCode() == ENTER){
            String lsResult = "";
            switch (lnIndex){
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 14:
                case 15:
                case 16:
                case 17:
                case 18:
                case 19:
                case 25:
                    break;
                case 6: 
                case 7: 
                case 8: 
                case 9: 
                case 10: 
                case 11: 
                case 12: 
                case 13: 
                    txtField.setText(poRecord.SearchMaster(lnIndex, lsResult, false)); break;
                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with index " + lnIndex + " not registered for QuickSearch.");
                    return;
            }
            
            switch (event.getCode()){
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
            }
        }
    }   
        
    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button)event.getSource()).getId();
        
        switch (lsButton){
            case "btnBrowse":
                if (poRecord.BrowseRecord("%", false, true)) loadRecord();
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
                if(sendOtherDetail()){
                    if (poRecord.SaveRecord()){
                        if (poRecord.OpenRecord(psOldRec)) loadRecord();
                        ShowMessageFX.Information(null, pxeModuleName, "Record Save Successfully.");
                    }
                }
                break;
            case "btnSearch":
                break;
            case "btnUpdate":
                if (poRecord.getMaster(1) != null && !poRecord.getMaster(1).toString().equals("")){
                    if (poRecord.UpdateRecord()){
                        pnEditMode = poRecord.getEditMode();
                    }
                }
                break;
            case "btnActivate":
                if (poRecord.getMaster(1) != null && !poRecord.getMaster(1).toString().equals("")){
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
                }
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
        lblHeader.setVisible(lbShow);
                
        btnClose.setVisible(!lbShow);
        btnBrowse.setVisible(!lbShow);
        btnActivate.setVisible(!lbShow);
        btnUpdate.setVisible(!lbShow);
        btnNew.setVisible(!lbShow);
        
        txtField01.setDisable(!lbShow);
        txtField02.setDisable(!lbShow);
        txtField03.setDisable(!lbShow);
        txtField04.setDisable(!lbShow);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        txtField07.setDisable(!lbShow);
        txtField08.setDisable(!lbShow);
        txtField09.setDisable(!lbShow);
        txtField10.setDisable(!lbShow);
        txtField11.setDisable(!lbShow);
        txtField12.setDisable(!lbShow);
        txtField13.setDisable(!lbShow);
        txtField14.setDisable(!lbShow);
        txtField15.setDisable(!lbShow);
        txtField16.setDisable(!lbShow);
        txtField17.setDisable(!lbShow);
        txtField18.setDisable(!lbShow);
        txtField19.setDisable(!lbShow);
        txtField25.setDisable(!lbShow);
        
        Combo23.setDisable(!lbShow);
        Combo24.setDisable(!lbShow);
        Check20.setDisable(!lbShow);
        Check21.setDisable(!lbShow);
        Check22.setDisable(!lbShow);
        Check26.setDisable(true);
        
        if (lbShow)
            txtField02.requestFocus();
        else
            btnNew.requestFocus();
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
        
        psOldRec = txtField01.getText();
        pnEditMode = EditMode.READY;
    }
        
    private boolean sendOtherDetail(){
        if (Combo23.getSelectionModel().getSelectedIndex() < 0){
            ShowMessageFX.Warning("No `Unit Type` selected.", pxeModuleName, "Please select `Unit Type` value.");
            Combo23.requestFocus();
            return false;
        }else 
            poRecord.setMaster(23, String.valueOf(Combo23.getSelectionModel().getSelectedIndex()));
        
        if (Combo24.getSelectionModel().getSelectedIndex() < 0){
            ShowMessageFX.Warning("No `Inv. Status` selected.", pxeModuleName, "Please select `Inv. Status` value.");
            Combo24.requestFocus();
            return false;
        }else 
            poRecord.setMaster(24, String.valueOf(Combo24.getSelectionModel().getSelectedIndex()));
        
        poRecord.setMaster(20, Check20.isSelected() == true ? "1" : "0");
        poRecord.setMaster(21, Check21.isSelected() == true ? "1" : "0");
        poRecord.setMaster(22, Check22.isSelected() == true ? "1" : "0");
               
        return true;
    }
    
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    
    private final String pxeModuleName = "InventoryController";
    private static GRider poGRider;
    private Inventory poRecord;
    
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
                case 2: /*sBarCodex*/
                    if (lsValue.length() > 12){
                        ShowMessageFX.Warning(null, pxeModuleName, "Max length for 'Barcode' exceeds the maximum limit.");
                        txtField.requestFocus();
                        return;
                    }
                    break;
                case 3: /*sDescript*/
                    if (lsValue.length() > 64) lsValue = lsValue.substring(0, 64);
                    break;
                case 4: /*sBriefDsc*/
                    if (lsValue.length() > 20){
                        ShowMessageFX.Warning(null, pxeModuleName, "Max length for 'Brief Desc.' exceeds the maximum limit.");
                        txtField.requestFocus();
                        return;
                    }
                    break;
                case 5: /*sAltBarCd*/
                    if (lsValue.length() > 12){
                        ShowMessageFX.Warning(null, pxeModuleName, "Max length for 'Alternate BCode' exceeds the maximum limit.");
                        txtField.requestFocus();
                        return;
                    }
                    break;
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                case 11:
                case 12:
                case 13:                        
                    if (lsValue.equals("")){ 
                        poRecord.setMaster(lnIndex, "");
                    }else
                        txtField.setText(poRecord.SearchMaster(lnIndex, (String) poRecord.getMaster(lnIndex), true));
                    
                    return;
                case 14: /*nUnitPrce*/
                case 15: /*nSelPrice*/
                case 16: /*nDiscLev1*/   
                case 17: /*nDiscLev2*/
                case 18: /*nDiscLev3*/
                case 19: /*nDealrDsc*/
                    double lnValue = 0;
                    try {
                        /*this must be numeric*/
                        lnValue = Double.parseDouble(lsValue);
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();
                    }
                    
                    poRecord.setMaster(lnIndex, lnValue);
                    txtField.setText(String.valueOf(poRecord.getMaster(lnIndex)));
                    return;
                case 25: /**/
                    break;
                default:
                    ShowMessageFX.Warning("Please inform MIS Dept.", pxeModuleName, "Text field with name " + txtField.getId() + " not registered.");
            }
            
            poRecord.setMaster(lnIndex, lsValue);
            txtField.setText((String)poRecord.getMaster(lnIndex));
        } else
            txtField.selectAll();    
    };
}
