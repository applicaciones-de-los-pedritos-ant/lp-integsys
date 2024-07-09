/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import javafx.beans.property.ReadOnlyBooleanPropertyBase;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.hpsf.Decimal;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.callback.IFXML;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.cas.inventory.base.InvMaster;

/**
 * FXML Controller class
 *
 * @author Arsiela
 * Date Created: 07-04-2023
 */
public class SPRecalculateUtilityController implements Initializable, IFXML {
    
    private GRider oApp;
    private InvMaster oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "SP Recalculate"; //Utility
    
    @FXML private VBox VBoxForm;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private TextField txtField01;
    @FXML private TextField txtField02;
    @FXML private TextField txtField03;
    @FXML private TextField txtField04;
    @FXML private TextField txtField06;
    @FXML private DatePicker txtField05;
    @FXML private Button btnRecal;
    @FXML private Button btnClose;
    
    private Stage getStage(){
         return (Stage) txtField02.getScene().getWindow();
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize Class
        oTrans = new InvMaster(oApp, oApp.getBranchCode(), true); 
        
        txtField06.focusedProperty().addListener(txtField_Focus);  
        txtField05.setOnAction(this::getDate); 
        txtField01.setOnKeyPressed(this::txtField_KeyPressed);
        txtField02.setOnKeyPressed(this::txtField_KeyPressed);
        
        //Button SetOnAction using cmdButton_Click() method
        btnExit.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnRecal.setOnAction(this::cmdButton_Click);

        pnEditMode = EditMode.UNKNOWN;
        initbutton(pnEditMode);
    }   
    
    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    
    private void cmdButton_Click(ActionEvent event) {
        try {
            String lsButton = ((Button) event.getSource()).getId();
            switch (lsButton) {
                case "btnRecal": 
                    if ((Date) oTrans.getMaster("dBegInvxx") == null){
                        ShowMessageFX.Warning(null, pxeModuleName, "Beginning Inventory Date cannot be empty.");
                        txtField05.requestFocus();
                        return;
                    }
                    if (oTrans.recalculate((String) oTrans.getMaster("sStockIDx"))) {
                        ShowMessageFX.Information(oTrans.getMessage(), pxeModuleName, "Recalculate successful!");
                        oTrans.SearchStock((String) oTrans.getMaster("sStockIDx"),"",false,true);
                        loadDetails();
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
            initbutton(pnEditMode);
        } catch (SQLException ex) {
            Logger.getLogger(SPRecalculateUtilityController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void txtField_KeyPressed(KeyEvent event) {
        TextField txtField = (TextField) event.getSource();
        int lnIndex = Integer.parseInt(((TextField) event.getSource()).getId().substring(8, 10));
        switch (event.getCode()) {
            case F3:
            case TAB:
            case ENTER:
                switch (lnIndex) {
                    case 1: //BARCODE
                        if (oTrans.SearchStock("",txtField01.getText(),true,false)) {
                            loadDetails();
                            txtField04.setText("1"); //Record
                            pnEditMode = EditMode.UPDATE;
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            clearfields();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                        break;
                    case 2: //DESCRIPTION
                        if (oTrans.SearchStock(txtField02.getText(),"",true,false)) {
                            loadDetails();
                            txtField04.setText("1"); //Record
                            pnEditMode = EditMode.UPDATE;
                        } else {
                            ShowMessageFX.Warning(getStage(), oTrans.getMessage(), "Warning", null);
                            clearfields();
                            pnEditMode = EditMode.UNKNOWN;
                        }
                        break;
                }
                initbutton(pnEditMode);
        }

        switch (event.getCode()) {
            case ENTER:
            case DOWN:
                CommonUtils.SetNextFocus(txtField);
                break;
            case UP:
                CommonUtils.SetPreviousFocus(txtField);
        }
    }
    
    private void loadDetails(){
        txtField01.setText((String) oTrans.getInventory("sBarCodex")); //Barcode
        txtField02.setText((String) oTrans.getInventory("sDescript")); //Description
        txtField03.setText(String.valueOf((BigDecimal) oTrans.getMaster("nQtyOnHnd"))); //Ending Inv.
        //txtField04.setText(""); //Record
        if ((Date) oTrans.getMaster("dBegInvxx") != null){
            txtField05.setValue(strToDate(FoodInventoryFX.xsRequestFormat((Date) oTrans.getMaster("dBegInvxx")))); //Beginning Inv. Date
        } else {
            txtField05.setValue(null);
        }
        String lsbegQty = "0.00";
        if (!String.valueOf((BigDecimal) oTrans.getMaster("nBegQtyxx")).equals("null")){
            lsbegQty = String.valueOf((BigDecimal) oTrans.getMaster("nBegQtyxx"));
        }
        txtField06.setText(lsbegQty); //Beginning Inv. Qty
    }
    
    /*Set TextField Value to Master Class*/
    final ChangeListener<? super Boolean> txtField_Focus = (o, ov, nv) -> {
        TextField txtField = (TextField) ((ReadOnlyBooleanPropertyBase) o).getBean();
        int lnIndex = Integer.parseInt(txtField.getId().substring(8, 10));
        String lsValue = txtField.getText();
        if (lsValue == null) {
            return;
        }
        if (!nv) {
            /*Lost Focus*/
            switch (lnIndex) {
                case 6:
                    double lnValue = 0;
                    try {
                        /*this must be numeric*/
                        lnValue = Double.valueOf(lsValue);
                    } catch (Exception e) {
                        ShowMessageFX.Warning("Please input numbers only.", pxeModuleName, e.getMessage());
                        txtField.requestFocus();   
                    }
                    oTrans.setMaster("nBegQtyxx", lsValue); //Handle Encoded Value
                    break;
            }
            
        } else {
            txtField.selectAll();
        }
    };
    
    /*Set Date Value to Master Class*/
    public void getDate(ActionEvent event) {
        if (txtField05.getValue() != null){
            oTrans.setMaster("dBegInvxx", SQLUtil.toDate(txtField05.getValue().toString(), SQLUtil.FORMAT_SHORT_DATE));
        } else {
            oTrans.setMaster("dBegInvxx", null);
        }
    }
    
    /*Convert Date to String*/
    private LocalDate strToDate(String val) {
        DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate localDate = LocalDate.parse(val, date_formatter);
        return localDate;
    }
    
    private void clearfields() {
        txtField01.clear(); //Barcode
        txtField02.clear(); //Description
        txtField03.clear(); //Ending Inv.
        txtField04.clear(); //Record
        txtField05.setValue(null); //Beginning Inv. Date
        txtField06.clear(); //Beginning Inv. Qty
    }
    
    private void initbutton(int fnValue) {
        boolean lbShow = (fnValue == EditMode.ADDNEW || fnValue == EditMode.UPDATE);
        txtField03.setDisable(true);
        txtField04.setDisable(true);
        txtField05.setDisable(!lbShow);
        txtField06.setDisable(!lbShow);
        btnRecal.setDisable(!lbShow);
    }
    
}
