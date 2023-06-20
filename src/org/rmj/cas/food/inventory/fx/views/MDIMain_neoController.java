/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;

/**
 * FXML Controller class
 *
 * @author user
 */
public class MDIMain_neoController implements Initializable {

    @FXML
    private AnchorPane mnuMain;
    @FXML
    private MenuBar mnuBar;
    @FXML
    private Menu mnuFiles;
    @FXML
    private MenuItem mnuInventory;
    @FXML
    private MenuItem mnuSerialUpload;
    @FXML
    private MenuItem mnuStocks;
    @FXML
    private MenuItem mnuBrand;
    @FXML
    private MenuItem mnuCategory;
    @FXML
    private MenuItem mnuCategory2;
    @FXML
    private MenuItem mnuCategory3;
    @FXML
    private MenuItem mnuCategory4;
    @FXML
    private MenuItem mnuColor;
    @FXML
    private MenuItem mnuCompany;
    @FXML
    private MenuItem mnuDiscounts;
    @FXML
    private MenuItem mnuInvLocation;
    @FXML
    private MenuItem mnuInvType;
    @FXML
    private MenuItem mnuMeasure;
    @FXML
    private MenuItem mnuModel;
    @FXML
    private MenuItem mnuSupplier;
    @FXML
    private MenuItem mnuTerm;
    @FXML
    private MenuItem mnuClose;
    @FXML
    private FontAwesomeIconView file;
    @FXML
    private Menu mnuTransactions;
    @FXML
    private MenuItem mnuPurchaseOrder;
    @FXML
    private MenuItem mnuPOReceiving;
    @FXML
    private MenuItem mnuPOReturn;
    @FXML
    private MenuItem mnuDailyProductionFG;
    @FXML
    private MenuItem mnuDailyProductionRM;
    @FXML
    private MenuItem mnu_StockRequest;
    @FXML
    private MenuItem mnu_ProductionRequest;
    @FXML
    private MenuItem mnu_InventoryTransfer;
    @FXML
    private MenuItem mnu_inventoryCount;
    @FXML
    private MenuItem menu_TransferPosting;
    @FXML
    private MenuItem menu_InvAdjustment;
    @FXML
    private MenuItem mnuWasteInventory;
    @FXML
    private FontAwesomeIconView transaction;
    @FXML
    private Menu mnuUtilities;
    @FXML
    private FontAwesomeIconView utilities;
    @FXML
    private MenuItem menuNotif;
    @FXML
    private Menu mnuReports;
    @FXML
    private FontAwesomeIconView reports;
    @FXML
    private MenuItem mnuStandard;
    @FXML
    private MenuItem mnuBIRrep;
    @FXML
    private Menu mnuHistory;
    @FXML
    private MenuItem mnu_PurchaseOrderReg;
    @FXML
    private MenuItem mnu_POReceivingReg;
    @FXML
    private MenuItem mnu_POReturnReg;
    @FXML
    private MenuItem mnu_InvDailyProdReg;
    @FXML
    private MenuItem mnu_InvStockReqReg;
    @FXML
    private MenuItem mnu_InvProdReqReg;
    @FXML
    private MenuItem mnu_InvTransReg;
    @FXML
    private MenuItem mnu_InvWasteReg;
    @FXML
    private MenuItem mnu_InvAdjustmentReg;
    @FXML
    private MenuItem mnu_InvCountReg;
    @FXML
    private FontAwesomeIconView history;
    @FXML
    private Menu mnuSettings;
    @FXML
    private CheckMenuItem chkLight;
    @FXML
    private MenuItem mnuResetPOS;
    @FXML
    private FontAwesomeIconView settings;
    @FXML
    private Label lblDate;
    @FXML
    private Label lblCompany;
    @FXML
    private Label lblUser;
    @FXML
    private Label lblFormTitle;
    @FXML
    private Button btnExit;
    @FXML
    private Button btnMinimize;
    @FXML
    private ToggleButton btnRestoreDown;
    @FXML
    private FontAwesomeIconView cmdRestore;
    @FXML
    private AnchorPane rootPane;
    @FXML
    private StackPane spLeft;
    @FXML
    private AnchorPane spRight;
    @FXML
    private TableView<?> table;
    @FXML
    private TableColumn<?, ?> index01;
    @FXML
    private TableColumn<?, ?> index02;
    @FXML
    private TableColumn<?, ?> index03;
    @FXML
    private TableColumn<?, ?> index04;
    @FXML
    private TableColumn<?, ?> index05;
    @FXML
    private TableColumn<?, ?> index06;
    @FXML
    private TableView<?> table01;
    @FXML
    private TableColumn<?, ?> index07;
    @FXML
    private TableColumn<?, ?> index08;
    @FXML
    private TableColumn<?, ?> index09;
    @FXML
    private TableColumn<?, ?> index10;
    @FXML
    private TableColumn<?, ?> index11;
    @FXML
    private TableColumn<?, ?> index12;
    @FXML
    private TreeTableView<?> ProductTable;
    @FXML
    private TreeTableColumn<?, ?> indexmaster01;
    @FXML
    private TreeTableColumn<?, ?> indexmaster02;
    @FXML
    private TreeTableColumn<?, ?> indexmaster03;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void mnuInventory_Click(ActionEvent event) {
    }

    @FXML
    private void mnuSerialUpload_Click(ActionEvent event) {
    }

    @FXML
    private void mnuStocks(ActionEvent event) {
    }

    @FXML
    private void mnuBrand_Click(ActionEvent event) {
    }

    @FXML
    private void mnuCategory_Click(ActionEvent event) {
    }

    @FXML
    private void mnuCategory2_Click(ActionEvent event) {
    }

    @FXML
    private void mnuCategory3_Click(ActionEvent event) {
    }

    @FXML
    private void mnuCategory4_Click(ActionEvent event) {
    }

    @FXML
    private void mnuColor_Click(ActionEvent event) {
    }

    @FXML
    private void mnuCompany_Click(ActionEvent event) {
    }

    @FXML
    private void mnuDiscounts_Click(ActionEvent event) {
    }

    @FXML
    private void mnuInvLocation_Click(ActionEvent event) {
    }

    @FXML
    private void mnuInvType_Click(ActionEvent event) {
    }

    @FXML
    private void mnuMeasure_Click(ActionEvent event) {
    }

    @FXML
    private void mnuModel_Click(ActionEvent event) {
    }

    @FXML
    private void mnuSupplier_Click(ActionEvent event) {
    }

    @FXML
    private void mnuTerm_Click(ActionEvent event) {
    }

    @FXML
    private void mnuClose_Click(ActionEvent event) {
    }

    @FXML
    private void mnuPurchaseOrder(ActionEvent event) {
    }

    @FXML
    private void mnuPOReceiving(ActionEvent event) {
    }

    @FXML
    private void mnuPOReturn(ActionEvent event) {
    }

    @FXML
    private void mnuDailyProductionFG(ActionEvent event) {
    }

    @FXML
    private void mnuDailyProductionRM(ActionEvent event) {
    }

    @FXML
    private void mnu_StockRequestClick(ActionEvent event) {
    }

    @FXML
    private void mnu_ProductionRequestClick(ActionEvent event) {
    }

    @FXML
    private void mnu_InventoryTransferClick(ActionEvent event) {
    }

    @FXML
    private void mnu_inventoryCountClick(ActionEvent event) {
    }

    @FXML
    private void menu_TransferPostingClick(ActionEvent event) {
    }

    @FXML
    private void menu_InvAdjustment_Click(ActionEvent event) {
    }

    @FXML
    private void mnuWasteInventory(ActionEvent event) {
    }

    @FXML
    private void menuNotif_Click(ActionEvent event) {
    }

    @FXML
    private void mnuStandard_Click(ActionEvent event) {
    }

    @FXML
    private void mnuBIRrep_Click(ActionEvent event) {
    }

    @FXML
    private void mnu_PurchaseOrderReg(ActionEvent event) {
    }

    @FXML
    private void mnu_POReceivingRegClick(ActionEvent event) {
    }

    @FXML
    private void mnu_POReturnReg(ActionEvent event) {
    }

    @FXML
    private void mnu_InvDailyProdRegClick(ActionEvent event) {
    }

    @FXML
    private void mnu_InvStockRequestRegClick(ActionEvent event) {
    }

    @FXML
    private void mnu_InvProdReqRegClick(ActionEvent event) {
    }

    @FXML
    private void mnu_InvTransRegClick(ActionEvent event) {
    }

    @FXML
    private void mnu_InvWasteReg(ActionEvent event) {
    }

    @FXML
    private void mnu_InvAdjustmentReg_Click(ActionEvent event) {
    }

    @FXML
    private void mnu_InvCountRegClick(ActionEvent event) {
    }

    @FXML
    private void chkLight_Click(ActionEvent event) {
    }

    @FXML
    private void mnuResetPOS_Click(ActionEvent event) {
    }

    @FXML
    private void btnExit_Clicke(ActionEvent event) {
    }

    @FXML
    private void btnMinimize_Click(ActionEvent event) {
    }

    @FXML
    private void btnRestoreDown_Clicke(ActionEvent event) {
    }

    @FXML
    private void mnuMain_KeyPressed(KeyEvent event) {
    }
    
}
