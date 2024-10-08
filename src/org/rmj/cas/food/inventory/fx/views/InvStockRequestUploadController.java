package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.callback.IFXML;
import org.rmj.appdriver.constants.EditMode;
import org.rmj.cas.inventory.base.InvRequest;
import org.rmj.lp.parameter.agent.XMBranch;
import org.rmj.lp.parameter.agent.XMInventoryType;

/**
 * FXML Controller class
 *
 * @author Maynard Date Created: 07-26-2024
 */
public class InvStockRequestUploadController implements Initializable, IFXML {

    private GRider oApp;
    private InvRequest oTrans;
    private int pnEditMode;
    private final String pxeModuleName = "Inventory Stock Request Upload"; //Utility

    @FXML
    private VBox VBoxForm;
    @FXML
    private Button btnExit;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private AnchorPane anchorField;
    @FXML
    private TextArea txtField07;
    @FXML
    private TextField txtField03;
    @FXML
    private TextField txtField02;
    @FXML
    private TextField txtField01;
    @FXML
    private TextField txtField05;
    @FXML
    private TextField txtField04;
    @FXML
    private TextField txtField06;
    @FXML
    private Button btnBrowse;
    @FXML
    private Button btnClose;
    @FXML
    private Button btnUpload;

    private Stage getStage() {
        return (Stage) anchorField.getScene().getWindow();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //Initialize Class
        oTrans = new InvRequest(oApp, oApp.getBranchCode(), true);
        txtField07.setOnKeyPressed(this::txtField_KeyPressed);

        btnExit.setOnAction(this::cmdButton_Click);
        btnClose.setOnAction(this::cmdButton_Click);
        btnUpload.setOnAction(this::cmdButton_Click);
        btnBrowse.setOnAction(this::cmdButton_Click);

        pnEditMode = EditMode.UNKNOWN;
    }

    @Override
    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    private void cmdButton_Click(ActionEvent event) {
        String lsButton = ((Button) event.getSource()).getId();
        switch (lsButton) {
            case "btnUpload":
                if (!txtField01.getText().trim().isEmpty()) {
                    if (oTrans.saveUtilTransaction()) {
                        ShowMessageFX.Information(null, pxeModuleName, "Transaction uploaded successfuly.");
                        clearFields();
                        pnEditMode = EditMode.UNKNOWN;

                    } else {
                        if (!oTrans.getErrMsg().equals("")) {
                            ShowMessageFX.Error(oTrans.getErrMsg(), pxeModuleName, "Please inform MIS Department.");
                        } else {
                            ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Please verify your entry.");
                        }
                        return;
                    }
                } else {
                    ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "No File Detected!");
                }
                return;
            case "btnBrowse":
                if (oTrans.ImportData((Stage) anchorField.getScene().getWindow(), true)) {
                    clearFields();
                    loadExcelDetail();
                }else{
                
                 ShowMessageFX.Warning(oTrans.getMessage()+ " " + oTrans.getErrMsg(), pxeModuleName, "Please check the file for uploading");
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

    private void txtField_KeyPressed(KeyEvent event) {
        TextArea txtField = (TextArea) event.getSource();
        int lnIndex = Integer.parseInt(((TextArea) event.getSource()).getId().substring(8, 10));
        switch (event.getCode()) {
            case F3:
            case ENTER:
                switch (lnIndex) {
                    case 7: 
                        if (oTrans.ImportData((Stage) anchorField.getScene().getWindow(), true)) {
                            loadExcelDetail();
                        } else {
                        if (!oTrans.getErrMsg().equals("")) {
                            ShowMessageFX.Error(oTrans.getErrMsg(), pxeModuleName, "Please inform MIS Department.");
                        } else {
                            ShowMessageFX.Warning(oTrans.getMessage(), pxeModuleName, "Please verify your entry.");
                        }
                        return;
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
        }
    }

    private void loadExcelDetail() {
        txtField01.setText((String) oTrans.getMaster("sTransNox"));
        txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) oTrans.getMaster("dTransact")));
        XMBranch loBranch = oTrans.GetBranch((String) oTrans.getMaster(3), true);
        if (loBranch != null) {
            txtField03.setText((String) loBranch.getMaster("sBranchNm"));
        }

        XMInventoryType loInv = oTrans.GetInventoryType((String) oTrans.getMaster(4), true);
        if (loInv != null) {
            txtField05.setText((String) loInv.getMaster("sDescript"));
        }

        txtField04.setText((String) oTrans.getMaster("sReferNox"));
        txtField05.setText((String) oTrans.getMaster("sTransNox"));
        txtField06.setText(String.valueOf(oTrans.ItemCount() - 1));

        txtField07.setText(oTrans.getFilePath());

    }

    private void clearFields() {
        txtField01.clear();
        txtField02.clear();
        txtField03.clear();
        txtField04.clear();
        txtField05.clear();
        txtField06.clear();
        txtField07.clear();
    }

}
