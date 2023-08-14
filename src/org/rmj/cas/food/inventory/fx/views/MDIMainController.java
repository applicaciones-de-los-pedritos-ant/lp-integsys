package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.util.Duration;
import javafx.event.EventHandler;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;
import org.apache.commons.lang3.time.DateUtils;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.ShowMessageFX;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.callback.IFXML;
import org.rmj.appdriver.agentfx.service.ITokenize;
import org.rmj.appdriver.agentfx.service.TokenApprovalFactory;
import org.rmj.appdriver.agentfx.ui.showFXDialog;
import org.rmj.appdriver.constants.UserRight;
import org.rmj.cas.food.reports.classes.FoodReports;
import org.rmj.lp.parameter.fx.ParameterFX;
//import org.rmj.cas.pos.reports.BIRReports;

public class MDIMainController implements Initializable {
    @FXML private MenuItem mnuClose;
    @FXML private MenuItem mnuTerm;
    @FXML private MenuItem mnuInvType;
    @FXML private MenuItem mnuCompany;
    @FXML private MenuItem mnuInventory;
    @FXML private MenuItem mnuBrand;
    @FXML private MenuItem mnuModel;
    @FXML private MenuItem mnuColor;
    @FXML private MenuItem mnuCategory;
    @FXML private MenuItem mnuCategory2;
    @FXML private MenuItem mnuCategory3;
    @FXML private MenuItem mnuCategory4;
    @FXML private MenuItem mnuSupplier;
    @FXML private MenuItem mnuInvLocation;
    @FXML private Label lblUser;
    @FXML private Button btnExit;
    @FXML private Button btnMinimize;
    @FXML private MenuBar mnuBar;
    @FXML private Label lblDate;
    @FXML private Label lblCompany;
    @FXML private MenuItem mnuPOReceiving;
    @FXML private MenuItem mnuDailyProduction;
    @FXML private MenuItem mnuStocks;
    @FXML private CheckMenuItem chkLight;
    @FXML private MenuItem mnu_StockRequest;
    @FXML private MenuItem mnu_InvStockReqReg;
    @FXML private MenuItem mnu_InvProdReqReg;
    @FXML private MenuItem mnu_InventoryTransfer;
    @FXML private MenuItem mnu_inventoryCount;
    @FXML private MenuItem mnu_POReceivingReg;
    @FXML private MenuItem mnu_InvTransReg;
    @FXML private MenuItem mnu_InvCountReg;
    @FXML private MenuItem mnu_InvDailyProdReg;
    @FXML private MenuItem menu_TransferPosting;
    @FXML private ToggleButton btnRestoreDown;
    @FXML private FontAwesomeIconView cmdRestore;
    @FXML private FontAwesomeIconView file;
    @FXML private FontAwesomeIconView transaction;
    @FXML private FontAwesomeIconView utilities;
    @FXML private FontAwesomeIconView reports;
    @FXML private FontAwesomeIconView history;
    @FXML private FontAwesomeIconView settings;
    @FXML private MenuItem mnuStandard;
    @FXML private MenuItem mnuMeasure;
    @FXML private MenuItem mnuWasteInventory;
    @FXML private MenuItem mnuPurchaseOrder;
    @FXML private MenuItem mnu_InvWasteReg;
    @FXML private MenuItem mnuPOReturn;
    @FXML private MenuItem mnu_PurchaseOrderReg;
    @FXML private MenuItem mnu_POReturnReg;
    @FXML private MenuItem mnuBIRrep;
    @FXML private Label lblFormTitle;
    @FXML private MenuItem mnuResetPOS;
    @FXML private MenuItem mnuDiscounts;
    @FXML private MenuItem mnuSerialUpload;
    @FXML private Menu mnuFiles;
    @FXML private Menu mnuTransactions;
    @FXML private Menu mnuUtilities;
    @FXML private Menu mnuReports;
    @FXML private Menu mnuHistory;
    @FXML private Menu mnuSettings;
    @FXML private AnchorPane mnuMain;
    @FXML private MenuItem menu_InvAdjustment;
    @FXML private MenuItem mnu_InvAdjustmentReg;
    @FXML private MenuItem menuNotif;
    @FXML private TableView table;
    @FXML private TableColumn index01;
    @FXML private TableColumn index02;
    @FXML private TableColumn index03;
    @FXML private TableColumn index04;
    @FXML private TableColumn index05;
    @FXML private TableView table01;
    @FXML private TableColumn index06;
    @FXML private TableColumn index07;
    @FXML private TableColumn index08;
    @FXML private TableColumn index09;
    @FXML private TableColumn index10;
    @FXML private TableColumn index11;
    @FXML private TableColumn index12;
    @FXML private StackPane spLeft;
    @FXML private AnchorPane spRight;
    @FXML private TreeTableView ProductTable;
    @FXML private TreeTableColumn indexmaster01,indexmaster02,indexmaster03;
    
    
    Node vertiCalPane;
    /**
     * For handling of Collections coming from data table
     */
    private ObservableList<TableModel> data = FXCollections.observableArrayList();
    private ObservableList<TableModel> data01 = FXCollections.observableArrayList();
    private ObservableList<TableModel> data02 = FXCollections.observableArrayList(); 
    private ObservableList<TableModel> data03 = FXCollections.observableArrayList();     
    @FXML
    private MenuItem mnu_ProductionRequest;
    @FXML
    private MenuItem mnuSPRec;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if ("IntegSys»Telecom".contains(poGRider.getProductID())){
            //for POS Back End use only
            if (!initMachine()){
                System.err.println("UNIDENTIFIED MACHINE DETECTED.");
                System.exit(1);
            }
            
            mnuStocks.setVisible(false);
            mnuCompany.setVisible(false);
            mnuTerm.setVisible(false);
            mnuSupplier.setVisible(false);
            
            mnuTransactions.setVisible(false);
            mnuUtilities.setVisible(false);
            mnuHistory.setVisible(false);
            mnuSettings.setVisible(false);
            mnuStandard.setVisible(false);
            
            mnuBIRrep.setVisible(true);
            mnuResetPOS.setVisible(false);
        } else{ //General
            mnuSerialUpload.setVisible(false);
            mnuBIRrep.setVisible(false); 
            mnuResetPOS.setVisible(false);
            
            mnuDailyProduction.setVisible(poGRider.getBranchCode().contains("P"));
            mnu_inventoryCount.setVisible(poGRider.getBranchCode().contains("P"));
            menu_TransferPosting.setVisible(poGRider.getBranchCode().contains("P"));
            mnuWasteInventory.setVisible(poGRider.getBranchCode().contains("P"));

            mnu_InvDailyProdReg.setVisible(poGRider.getBranchCode().contains("P"));
            mnu_InvTransReg.setVisible(poGRider.getBranchCode().contains("P"));
            mnu_InvWasteReg.setVisible(poGRider.getBranchCode().contains("P"));
            mnu_InvCountReg.setVisible(poGRider.getBranchCode().contains("P"));
        } 

        if (!poGRider.getProductID().equalsIgnoreCase("general")){
            String lsTranMode = "";
            if (!System.getProperty("pos.clt.tran.mode").equalsIgnoreCase("a")) lsTranMode = " (Training Mode)";
            switch (poGRider.getClientID().substring(0, 3)){
                case "GTC":
                    lblFormTitle.setText(System.getProperty("app.product.id.telecom") + lsTranMode);
                    mnuSerialUpload.setVisible(true);
                    break;
                case "GGC":
                    lblFormTitle.setText(System.getProperty("app.product.id.integsys") + lsTranMode);
                    mnuSerialUpload.setVisible(false);
                    break;
            }
        } else lblFormTitle.setText(System.getProperty("app.product.id.general"));
        
        btnExit.setTooltip(new Tooltip("Exit"));
        btnMinimize.setTooltip(new Tooltip("Minimize"));
        btnRestoreDown.setTooltip(new Tooltip("Restore down"));
        Tooltip.install(btnExit, new Tooltip("Exit"));
        Tooltip.install(btnMinimize, new Tooltip("Minimize"));
        Tooltip.install(btnRestoreDown, new Tooltip("Restore down"));
        getTime();
        initGridLedger();
        loadRecord();
        loadGrid(); 
        
        /**
         * jovan 
         * since 06-24-2021
         * implement timer to reload sysmonitor ever 30 seconds that user is idling
         */
        p_nLocation = MouseInfo.getPointerInfo().getLocation();
        
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
          @Override
          public void run() {
              Platform.runLater(() -> {
            Point lpLocation =  MouseInfo.getPointerInfo().getLocation();
            if(!p_nLocation.equals(lpLocation)){
                if(p_bRunning) p_bRunning = false;
                p_nLocation = MouseInfo.getPointerInfo().getLocation();
                p_dIdleTIme = java.util.Calendar.getInstance().getTime();
            }else {
                if(!p_bRunning && (((java.util.Calendar.getInstance().getTime().getTime() - p_dIdleTIme.getTime()) / DateUtils.MILLIS_PER_SECOND) > 30)){
                    ProductTable.getSelectionModel().clearSelection();
                    loadGrid(); 
                    p_bRunning = true;
                }
            }
          });
                      }
        }, 0, 1000);
        /**
         * @author jovan
         * @since 2021-06-09
         * disabled this notification sample to produce new system monitor for java
        Timer timer = new Timer();
        timer.schedule(poTrans, 5000, Long.parseLong(getNotifKey()));
        */
    }
    
    /**
     * function to call when loading grid information
     */
    private void loadGrid(){

        data.clear();
        data01.clear();
        data02.clear();
        data03.clear();

        loadDetail2Grid();
        loadExpiredInv();
        loadProduct2Grid();
    }
    
    public void loadRecord(){
        lblCompany.setText(poGRider.getClientName());
        
        ResultSet name;
        String lsQuery = "SELECT a.sClientNm "+
                            " FROM Client_Master a" +
                            " LEFT JOIN xxxSysUser b" + 
                                " ON a.sClientID = b.sEmployNo" + 
                            " WHERE sUserIDxx = " + SQLUtil.toSQL(poGRider.getUserID());
        name = poGRider.executeQuery(lsQuery);
        try {
            if(name.next()){
                lblUser.setText(name.getString("sClientNm"));
                System.setProperty("user.name", name.getString("sClientNm"));
            }
                
        } catch (SQLException ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void mnuClose_Click(ActionEvent event) throws IOException {
        if (ShowMessageFX.YesNo(null, pxeModuleName, "Are you sure you want to logout?")) System.exit(0);
    }
    
    @FXML
    private void mnuTerm_Click(ActionEvent event) throws IOException {
        showParameter("Term");
    }
    
    @FXML
    private void mnuInvType_Click(ActionEvent event) throws IOException {
        if (poGRider.getUserLevel() != UserRight.ENGINEER){
            ShowMessageFX.Information("Only MIS Department can add/modify INVENTORY TYPES.", "Notice", "Please inform MIS Department.");
            return;
        }
        showParameter("InventoryType");
    }
    
    @FXML
    private void mnuCompany_Click(ActionEvent event) throws IOException {
        showParameter("Company");
    }
    
    @FXML
    private void mnuInventory_Click(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeInventory));
        loadScene(FoodInventoryFX.pxeInventory);
    }
    
    @FXML
    private void mnuInvLocation_Click(ActionEvent event) throws IOException {
        showParameter("InventoryLocation");
    }
    
    @FXML
    private void mnuBrand_Click(ActionEvent event) throws IOException {
        showParameter("Brand");
    }

    @FXML
    private void mnuModel_Click(ActionEvent event) throws IOException {
        showParameter("Model");
    }

    @FXML
    private void mnuColor_Click(ActionEvent event) throws IOException {
        showParameter("Color");
    }
    
    @FXML
    private void mnuCategory_Click(ActionEvent event) throws IOException {
        showParameter("Category");
    }
    
    @FXML
    private void mnuCategory2_Click(ActionEvent event) throws IOException {
        showParameter("Category2");
    }

    @FXML
    private void mnuCategory3_Click(ActionEvent event) throws IOException {
        showParameter("Category3");
    }

    @FXML
    private void mnuCategory4_Click(ActionEvent event) throws IOException {
        showParameter("Category4");
    }
    
    @FXML
    private void mnuSupplier_Click(ActionEvent event) throws IOException {
        showParameter("Supplier");
    }
    
    public Parent loadScene(String foURL)throws IOException{
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(foURL));

            Object fxObj = getContoller(foURL);
            fxmlLoader.setController(fxObj);
            
            Parent root = fxmlLoader.load();
            spLeft.getChildren().clear();
            spLeft.getChildren().add(root);
            
            FadeTransition ft = new FadeTransition(Duration.millis(1500));
            ft.setNode(root);
            ft.setFromValue(1);
            ft.setToValue(1);
            ft.setCycleCount(1);
            ft.setAutoReverse(false);
            ft.play();

            return root;
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return null;
    }

    @FXML
    private void mnuSPRec_Click(ActionEvent event) {
        try {
            loadSPRecalculateWindow();
        } catch (SQLException ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private double xOffset = 0;
    private double yOffset = 0;
    private void loadSPRecalculateWindow() throws SQLException{
        try {
            Stage stage = new Stage();

            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("SPRecalculateUtility.fxml"));

            SPRecalculateUtilityController loControl = new SPRecalculateUtilityController();
            loControl.setGRider(poGRider);
            fxmlLoader.setController(loControl);

            //load the main interface
            Parent parent = fxmlLoader.load();

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

            //set the main interface as the scene
            Scene scene = new Scene(parent);
            stage.setScene(scene);
            stage.initStyle(StageStyle.TRANSPARENT);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("");
            stage.showAndWait();
            
        } catch (IOException e) {
            e.printStackTrace();
            ShowMessageFX.Warning(null,e.getMessage(), "Warning", null);
            System.exit(1);
        }
    }
    
    class Delta { double x, y; }
        
    public void dragNode(Node node) {
        // Custom object to hold x and y positions
        final Delta dragDelta = new Delta();

        node.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                dragDelta.x = node.getLayoutX() - mouseEvent.getSceneX();
                dragDelta.y = node.getLayoutY() - mouseEvent.getSceneY();
            }
        });
        
        node.setOnMouseReleased(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setCursor(Cursor.DEFAULT);
            }
        });

        node.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                node.setLayoutX(mouseEvent.getSceneX() + dragDelta.x);
                node.setLayoutY(mouseEvent.getSceneY() + dragDelta.y);
            }
        });

        node.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                node.setCursor(Cursor.HAND);
            }
        });
    }
    
    public Object getContoller(String fsValue){
        switch (fsValue){
            case FoodInventoryFX.pxePOReceiving:
                POReceivingController loPOReceivingObj = new POReceivingController();
                loPOReceivingObj.setGRider(poGRider);
                
                return loPOReceivingObj; 
                
            case FoodInventoryFX.pxeStocks:
                InvMasterController loStocksObj = new InvMasterController();
                loStocksObj.setGRider(poGRider);
                
                return loStocksObj;
                
            case FoodInventoryFX.pxeInvStockRequest:
                InvStockRequestController InvStockRequestObj = new InvStockRequestController();
                InvStockRequestObj.setGRider(poGRider);
                
                return InvStockRequestObj;
                
            case FoodInventoryFX.pxeInvTransfer:
                InvTransferController loInvTransferObj = new InvTransferController();
                loInvTransferObj.setGRider(poGRider);
                
                return loInvTransferObj;
                
            case FoodInventoryFX.pxeInvCount:
                InvCountController loInvCountObj = new InvCountController();
                loInvCountObj.setGRider(poGRider);
                
                return loInvCountObj;
           
            case FoodInventoryFX.pxePurchaseOrder:
                PurchaseOrderController loPOObj = new PurchaseOrderController();
                loPOObj.setGRider(poGRider);
                
                return loPOObj;   
                
            case FoodInventoryFX.pxePOReturn:
                POReturnController loPORet = new POReturnController();
                loPORet.setGRider(poGRider);
                
                return loPORet;
            case FoodInventoryFX.pxePOReceivingReg:
                POReceivingRegController loPOReceivingRegObj = new POReceivingRegController();
                loPOReceivingRegObj.setGRider(poGRider);
                
                return loPOReceivingRegObj;
            case FoodInventoryFX.pxeDailyProd:
                DailyProductionController loDailyProductionObj = new DailyProductionController();
                loDailyProductionObj.setGRider(poGRider);
                
                return loDailyProductionObj;
            case FoodInventoryFX.pxeInvTransferReg:
                InvTransferRegController loInvTransferRegObj = new InvTransferRegController();
                loInvTransferRegObj.setGRider(poGRider);
                
                return loInvTransferRegObj;
                
            case FoodInventoryFX.pxeInvCountReg:
                InvCountRegController loInvCountRegObj = new InvCountRegController();
                loInvCountRegObj.setGRider(poGRider);
                
                return loInvCountRegObj;
                
            case FoodInventoryFX.pxeDailyProdReg:
                DailyProductionRegController loDailyProdRegObj = new DailyProductionRegController();
                loDailyProdRegObj.setGRider(poGRider);
                
                return loDailyProdRegObj;
                
            case FoodInventoryFX.pxeInvTransPosting:
                InvTransPostingController loInvTransPostingObj = new InvTransPostingController();
                loInvTransPostingObj.setGRider(poGRider);
                
                return loInvTransPostingObj;
            case FoodInventoryFX.pxeInvWaste:
                InvWasteController loInvWaste = new InvWasteController();
                loInvWaste.setGRider(poGRider);
                
                return loInvWaste;
            case FoodInventoryFX.pxeInvWasteReg:
                InvWasteRegController loInvWasteReg = new InvWasteRegController();
                loInvWasteReg.setGRider(poGRider);
                
                return loInvWasteReg;
            case FoodInventoryFX.pxePurchaseOrderReg:
                PurchaseOrderRegController loPOReg = new PurchaseOrderRegController();
                loPOReg.setGRider(poGRider);
                
                return loPOReg;
            case FoodInventoryFX.pxePOReturnReg:
                POReturnRegController loPORetReg = new POReturnRegController();
                
                loPORetReg.setGRider(poGRider);
                
                return loPORetReg;
                
            case FoodInventoryFX.pxeInventory:
                InventoryController loInv = new InventoryController();
                loInv.setGRider(poGRider);
                return loInv;
                
            case FoodInventoryFX.pxeSerialUpload:
                SerialUploadController loUpload = new SerialUploadController();
                loUpload.setGRider(poGRider);
                return loUpload;
            
            case FoodInventoryFX.pxeInvAdjustment:
                InvAdjustmentController loInvAdjustment = new InvAdjustmentController();
                loInvAdjustment.setGRider(poGRider);
                return loInvAdjustment;
            
            case FoodInventoryFX.pxeInvAdjustmentReg:
                InvAdjustmentRegController loInvAdjustmentReg = new InvAdjustmentRegController();
                loInvAdjustmentReg.setGRider(poGRider);
                return loInvAdjustmentReg;
                
            case FoodInventoryFX.pxeNotif:
                NotifParamController loNotif = new NotifParamController();
                loNotif.setGRider(poGRider);
                
                return loNotif;
                
            case FoodInventoryFX.pxeInvProdRequest:
                InvProdRequestController loProdRequestObj = new InvProdRequestController();
                loProdRequestObj.setGRider(poGRider);
                
                return loProdRequestObj;
                
            case FoodInventoryFX.pxeStockRequestReg:
                InvStockRequestRegController loStockRequestRegObj = new InvStockRequestRegController();
                loStockRequestRegObj.setGRider(poGRider);
                
                return loStockRequestRegObj;
                
            case FoodInventoryFX.pxeProdRequestReg:
                InvProdRequestRegController loProdRequestRegObj = new InvProdRequestRegController();
                loProdRequestRegObj.setGRider(poGRider);
                
                return loProdRequestRegObj;
                
            default:
                return null;
        }
    }


    @FXML
    private void btnExit_Clicke(ActionEvent event) {
       if (ShowMessageFX.OkayCancel(null, "Confirm", "Do you want to exit?") == true)
                CommonUtils.closeStage(btnExit);
    }

    @FXML
    private void btnMinimize_Click(ActionEvent event) {
        CommonUtils.minimizeStage(btnMinimize);
    }

    @FXML
    private void mnuPOReceiving(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxePOReceiving));
        loadScene(FoodInventoryFX.pxePOReceiving);
    }
    
    @FXML
    private void mnuDailyProduction(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxePOReceiving));
        loadScene(FoodInventoryFX.pxeDailyProd);
    }
    
    @FXML
    private void mnuStocks(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeStocks));
        loadScene(FoodInventoryFX.pxeStocks);
    }


    @FXML
    private void chkLight_Click(ActionEvent event) {
    }

    @FXML
    private void mnu_StockRequestClick(ActionEvent event) throws IOException{
//         setDataPane(fadeAnimate(FoodInventoryFX.pxeInvTransfer));
         loadScene(FoodInventoryFX.pxeInvStockRequest);

    }
    @FXML
    private void mnu_ProductionRequestClick(ActionEvent event) throws IOException{
//         setDataPane(fadeAnimate(FoodInventoryFX.pxeInvTransfer));
         loadScene(FoodInventoryFX.pxeInvProdRequest);

    }
    
    @FXML
    private void mnu_InventoryTransferClick(ActionEvent event) throws IOException{
//         setDataPane(fadeAnimate(FoodInventoryFX.pxeInvTransfer));
         loadScene(FoodInventoryFX.pxeInvTransfer);

    }

    @FXML
    private void mnu_inventoryCountClick(ActionEvent event) throws IOException {
//         setDataPane(fadeAnimate(FoodInventoryFX.pxeInvCount));
         loadScene(FoodInventoryFX.pxeInvCount);
    }

    private void mnu_DailyProductionClick(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeDailyProd));
            loadScene(FoodInventoryFX.pxeDailyProd);
    }

    @FXML
    private void mnu_POReceivingRegClick(ActionEvent event)throws IOException {
//         setDataPane(fadeAnimate(FoodInventoryFX.pxePOReceivingReg));
         loadScene(FoodInventoryFX.pxePOReceivingReg);
    }

    @FXML
    private void mnu_InvTransRegClick(ActionEvent event)throws IOException {
//         setDataPane(fadeAnimate(FoodInventoryFX.pxeInvTransferReg));
         loadScene(FoodInventoryFX.pxeInvTransferReg);
    }

    @FXML
    private void mnu_InvCountRegClick(ActionEvent event)throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeInvCountReg));
        loadScene(FoodInventoryFX.pxeInvCountReg);
    }

    @FXML
    private void mnu_InvDailyProdRegClick(ActionEvent event)throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeDailyProdReg));
        loadScene(FoodInventoryFX.pxeDailyProdReg);
    }

    @FXML
    private void mnu_InvStockRequestRegClick(ActionEvent event)throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeDailyProdReg));
        loadScene(FoodInventoryFX.pxeStockRequestReg);
    }
    @FXML
    private void mnu_InvProdReqRegClick(ActionEvent event)throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeDailyProdReg));
        loadScene(FoodInventoryFX.pxeProdRequestReg);
    }
    @FXML
    private void menu_TransferPostingClick(ActionEvent event)throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeInvTransPosting));
        loadScene(FoodInventoryFX.pxeInvTransPosting);
    }

    @FXML
    private void btnRestoreDown_Clicke(ActionEvent event) {
        Stage stage=(Stage) spLeft.getScene().getWindow();
        if (btnRestoreDown.isSelected()) {
            cmdRestore.setGlyphName("EXPAND");
            btnRestoreDown.setTooltip(new Tooltip("Maximize"));
            Tooltip.install(btnRestoreDown, new Tooltip("Maximize"));
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setY(bounds.getMinY());
            stage.setWidth(1100);
            stage.setHeight(bounds.getHeight());
            stage.centerOnScreen();
        } else {
            cmdRestore.setGlyphName("COMPRESS");
            btnRestoreDown.setTooltip(new Tooltip("RestoreDown"));
            Tooltip.install(btnRestoreDown, new Tooltip("RestoreDown"));
            Screen screen = Screen.getPrimary();
            Rectangle2D bounds = screen.getVisualBounds();
            stage.setY(bounds.getMinY());
            stage.setX(bounds.getMinX());
            stage.setWidth(bounds.getWidth());
            stage.setHeight(bounds.getHeight());
        }
    }

    @FXML
    private void mnuStandard_Click(ActionEvent event) {
        FoodReports loReports = new FoodReports();
        loReports.setGRider(poGRider);
        
        try {
            CommonUtils.showModal(loReports);
        } catch (Exception ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        loReports = null;
    }

    @FXML
    private void mnuMeasure_Click(ActionEvent event) throws IOException {
        showParameter("Measure");
    }

    @FXML
    private void mnuWasteInventory(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeInvWaste));
        loadScene(FoodInventoryFX.pxeInvWaste);
    }

    @FXML
    private void mnuPurchaseOrder(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxePurchaseOrder));
        loadScene(FoodInventoryFX.pxePurchaseOrder);
    }

    @FXML
    private void mnu_InvWasteReg(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeInvWasteReg));
        loadScene(FoodInventoryFX.pxeInvWasteReg);
    }

    @FXML
    private void mnuPOReturn(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxePOReturn));
        loadScene(FoodInventoryFX.pxePOReturn);
    }

    @FXML
    private void mnu_PurchaseOrderReg(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxePurchaseOrderReg));
        loadScene(FoodInventoryFX.pxePurchaseOrderReg);
    }
    

    @FXML
    private void mnu_POReturnReg(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxePOReturnReg));
        loadScene(FoodInventoryFX.pxePOReturnReg);
    }

    @FXML
    private void mnuBIRrep_Click(ActionEvent event) {
//        BIRReports instance = new BIRReports();
//        instance.setGRider(poGRider);
//        
//        if (instance.getParam()){
//            if (!instance.processReport()){
//                ShowMessageFX.Warning(instance.getMessage(), "Warning", "Unable to generate report.");
//            }
//        } else 
//            ShowMessageFX.Information(instance.getMessage(), "Notice", "Report generation cancelled.");
    }

    @FXML
    private void mnuResetPOS_Click(ActionEvent event) {
        if (showFXDialog.resetPOS(poGRider)){
            ShowMessageFX.Information(null, "Success", "POS was successfully reset.");
        }
    }

    @FXML
    private void mnuDiscounts_Click(ActionEvent event) {
        showParameter("PromoDiscount");
    }

    @FXML
    private void mnuSerialUpload_Click(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeSerialUpload));
        loadScene(FoodInventoryFX.pxeSerialUpload);
    }

    @FXML
    private void mnuMain_KeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.F12){
            if (showFXDialog.resetPOS(poGRider)){
                ShowMessageFX.Information(null, "Success", "POS was successfully reset.");
            }
        } else if (event.getCode() == KeyCode.F10){     
            ITokenize instance = TokenApprovalFactory.make("CASys_DBF.PO_Master");
            instance.setGRider(poGRider);
            instance.setTransNmbr("M00120000001");
            if (instance.createCodeRequest()){
                System.out.println(instance.getMessage());
            } else {
                System.err.println(instance.getMessage());
            }
        } else if (event.getCode() == KeyCode.F11){
            if (showFXDialog.getTokenApproval(poGRider, "CASys_DBF.PO_Master", "M00120000001")){
                //TODO:
                //  execute approving of transaction here
                ShowMessageFX.Information(null, "Success", "Transaction was approved successfully..");
            }
        }
        
    }

    @FXML
    private void menu_InvAdjustment_Click(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeInvAdjustment));
        loadScene(FoodInventoryFX.pxeInvAdjustment);
    }

    @FXML
    private void mnu_InvAdjustmentReg_Click(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeInvAdjustmentReg));
        loadScene(FoodInventoryFX.pxeInvAdjustmentReg);
    }

    @FXML
    private void menuNotif_Click(ActionEvent event) throws IOException {
//        setDataPane(fadeAnimate(FoodInventoryFX.pxeNotif));
        loadScene(FoodInventoryFX.pxeNotif);
    }

       
//    public static class MouseGestures {
//        class DragContext {
//            double x;
//            double y;
//        }
//
//        DragContext dragContext = new DragContext();
//
//        public void makeDraggable(VBox pane, VBox node) {
//            node.setOnMousePressed(onMousePressedEventHandler);
//            node.setOnMouseDragged(onMouseDraggedEventHandler);
//            
//            double centerXPosition = (pane.getWidth() - node.getPrefWidth())/2;
//            double centerYPosition = (pane.getHeight() - node.getPrefHeight())/2;
//
//            node.setTranslateX(centerXPosition);
//            node.setTranslateY(centerYPosition);
//        }
//
//        EventHandler<MouseEvent> onMousePressedEventHandler = event -> {
//            Node node = ((Node) (event.getSource()));
//            
//            dragContext.x = node.getTranslateX() - event.getSceneX();
//            dragContext.y = node.getTranslateY() - event.getSceneY();
//        };
//
//        EventHandler<MouseEvent> onMouseDraggedEventHandler = event -> {
//            Node node = ((Node) (event.getSource()));
//            
//            node.setTranslateX( dragContext.x + event.getSceneX());            
//            if (dragContext.y + event.getSceneY() > 0)
//                node.setTranslateY(dragContext.y + event.getSceneY());
//        };
//    }
    
    /**
     *
     * @param foGRider - passing master class to this controller
     */
    public void setGRider(GRider foGRider){this.poGRider = foGRider;}
    private final String pxeModuleName = "Pedritos Integrated System";
    private static GRider poGRider;
    private Point p_nLocation;
    private Boolean p_bRunning = false;
    private Date p_dIdleTIme = java.util.Calendar.getInstance().getTime();
    /**
     * @author jovan
     * @since 2021-06-09
     * partially remove checking of notification
     * disabled this function to create system monitor
     * public String getNotifKey(){
     *    return CommonUtils.getConfiguration(poGRider, "AppNotif");
        }
    */
    
    private void getTime(){
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
        
        Date date = new Date();
        String strTimeFormat = "hh:mm:";
        String strDateFormat = "MMMM dd, yyyy";
        String secondFormat = "ss";
        
        DateFormat timeFormat = new SimpleDateFormat(strTimeFormat + secondFormat);
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        
        String formattedTime= timeFormat.format(date);
        String formattedDate= dateFormat.format(date);
        
        lblDate.setText(formattedDate+ " || " + formattedTime);
        
        }),
         new KeyFrame(Duration.seconds(1))
        );
        
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    
    private boolean initMachine(){
        if ("telecom»integsys".contains(poGRider.getProductID().toLowerCase())){
            try {
                String lsMIN = System.getProperty("pos.clt.crm.no");
                
                if (lsMIN.isEmpty()){
                    System.err.println("Invalid Machine Identification Info Detected...");
                    return false;
                }

                String lsSQL = "SELECT" +
                                    "  a.sAccredtn" +
                                    ", a.sPermitNo" +
                                    ", a.sSerialNo" +
                                    ", a.nPOSNumbr" +
                                    ", a.nZReadCtr" +
                                    ", IFNULL(b.sWebSrver, '') sWebSrver" +
                                    ", IFNULL(b.sPrinter1, '') sPrinter1" +
                                    ", a.cTranMode" +
                                " FROM Cash_Reg_Machine a" +
                                    " LEFT JOIN Cash_Reg_Machine_Printer b" +
                                        " ON a.sIDNumber = b.sIDNumber" +
                                " WHERE a.sIDNumber = " + SQLUtil.toSQL(lsMIN);

                ResultSet loRS = poGRider.executeQuery(lsSQL);
                long lnRow = MiscUtil.RecordCount(loRS);

                if (lnRow != 1){
                    System.err.println("Invalid Config for MIN Detected..");
                    return false;
                }

                loRS.first();
                System.setProperty("pos.clt.accrd.no", loRS.getString("sAccredtn"));
                System.setProperty("pos.clt.prmit.no", loRS.getString("sPermitNo"));
                System.setProperty("pos.clt.srial.no", loRS.getString("sSerialNo"));
                System.setProperty("pos.clt.trmnl.no", loRS.getString("nPOSNumbr"));
                System.setProperty("pos.clt.zcounter", String.valueOf(loRS.getInt("nZReadCtr")));

                System.setProperty("pos.clt.web.svrx", loRS.getString("sWebSrver"));
                System.setProperty("pos.clt.prntr.01", loRS.getString("sPrinter1"));
                
                System.setProperty("pos.clt.tran.mode", loRS.getString("cTranMode"));
                
                return true;
            } catch (SQLException ex) {
                Logger.getLogger(this.getClass().getName()).log(Level.SEVERE, null, ex);
                System.err.println(ex.getMessage());
            }
            return false;
        } else 
            return true;
    }   
    
    private void showParameter(String fsFormName){
        try {
            IFXML fxObj = getController(fsFormName);
        
            ParameterFX instance = new ParameterFX();
            instance.setFXMLForm(fsFormName + ".fxml");
            instance.setFXController(fxObj);
            
            CommonUtils.showModal(instance);
        } catch (Exception ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private IFXML getController(String fsValue){                
        IFXML instance;
        
        switch (fsValue){
            case "Brand":
                instance = new org.rmj.lp.parameter.fx.BrandController();
                instance.setGRider(poGRider);
                return instance;                
            case "InventoryType":
                instance = new org.rmj.lp.parameter.fx.InventoryTypeController();
                instance.setGRider(poGRider);
                return instance;      
            case "Category":
                instance = new org.rmj.lp.parameter.fx.CategoryController();
                instance.setGRider(poGRider);
                return instance;          
            case "Category2":
                instance = new org.rmj.lp.parameter.fx.Category2Controller();
                instance.setGRider(poGRider);
                return instance;          
            case "Category3":
                instance = new org.rmj.lp.parameter.fx.Category3Controller();
                instance.setGRider(poGRider);
                return instance;    
            case "Category4":
                instance = new org.rmj.lp.parameter.fx.Category4Controller();
                instance.setGRider(poGRider);
                return instance;    
            case "PromoDiscount":
                instance = new org.rmj.lp.parameter.fx.PromoDiscountController();
                instance.setGRider(poGRider);
                return instance;    
            case "Model":
                instance = new org.rmj.lp.parameter.fx.ModelController();
                instance.setGRider(poGRider);
                return instance;   
            case "Color":
                instance = new org.rmj.lp.parameter.fx.ColorController();
                instance.setGRider(poGRider);
                return instance;  
            case "Company":
                instance = new org.rmj.lp.parameter.fx.CompanyController();
                instance.setGRider(poGRider);
                return instance;  
            case "Measure":
                instance = new org.rmj.lp.parameter.fx.MeasureController();
                instance.setGRider(poGRider);
                return instance;      
            case "Supplier":
                instance = new org.rmj.lp.parameter.fx.SupplierController();
                instance.setGRider(poGRider);
                return instance;      
            case "Term":
                instance = new org.rmj.lp.parameter.fx.TermController();
                instance.setGRider(poGRider);
                return instance;      
            case "InventoryLocation":
                instance = new org.rmj.lp.parameter.fx.InventoryLocationController();
                instance.setGRider(poGRider);
                return instance; 
                
            default: 
                return null;
        }
    }
    
    /**
     * 
     * disabled this instance; already disabled notification
     * poTrans.setGRider(poGRider);
     * TimerDashBoard poTrans = new TimerDashBoard();
     * created initGridLedger to initiate table at menu
     */
    private void initGridLedger(){
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;");
        index04.setStyle("-fx-alignment: CENTER;");
        index05.setStyle("-fx-alignment: CENTER;");
        index06.setStyle("-fx-alignment: CENTER;");
        
        index07.setStyle("-fx-alignment: CENTER;");
        index08.setStyle("-fx-alignment: CENTER-LEFT;");
        index09.setStyle("-fx-alignment: CENTER-LEFT;");
        index10.setStyle("-fx-alignment: CENTER;");
        index11.setStyle("-fx-alignment: CENTER;");
        index12.setStyle("-fx-alignment: CENTER;");
        
        index01.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        
        index07.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index01"));
        index08.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index02"));
        index09.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index03"));
        index10.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index04"));
        index11.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index05"));
        index12.setCellValueFactory(new PropertyValueFactory<org.rmj.cas.food.inventory.fx.views.TableModel,String>("index06"));
        
        table.setItems(data);
        table01.setItems(data01); 
        
        //set data product request list ito
        indexmaster01.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableModel, String>, ObservableValue<String>>() {
     @Override
     public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableModel, String> p) {
         return p.getValue().getValue().index02Property();
     }
  });
        indexmaster02.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableModel, String>, ObservableValue<String>>() {
     @Override
     public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableModel, String> p) {
         return p.getValue().getValue().index03Property();
     }
  });
        indexmaster03.setCellValueFactory(new Callback<TreeTableColumn.CellDataFeatures<TableModel, String>, ObservableValue<String>>() {
     @Override
     public ObservableValue<String> call(TreeTableColumn.CellDataFeatures<TableModel, String> p) {
         return p.getValue().getValue().index04Property();
     }
  });
    }
    
    /**
     * 
     * @return string SQL query for inventory about to expire
     */
    private String getSQLInventory(){
        return "SELECT" +
                    " a.sBarCodex" +
                    ",a.sDescript" +
                    ",c.dExpiryDt" +
                    ",c.nQtyOnHnd" +
                    ",d.sMeasurNm" +
                    " FROM Inventory a" +
                    ",Inv_Master b" +
                    ",Inv_Master_Expiration c" +
                    ",Measure d" +
                    " WHERE a.sStockIDx = b.sStockIDx" +
                    " AND b.sStockIDx = c.sStockIDx" +
                    " AND b.sBranchCd = c.sBranchCd" +
                    " AND a.sMeasurID = d.sMeasurID" +
                    " AND a.cWthExprt =" + SQLUtil.toSQL("1")+
                    " AND b.sBranchCd  =" + SQLUtil.toSQL(poGRider.getBranchCode())+
                    " AND b.nQtyOnHnd > 0" +
                    " AND c.nQtyOnHnd > 0" +
                    " AND DATEDIFF(c.dExpiryDt, "+ SQLUtil.toSQL(CommonUtils.xsDateShort(poGRider.getServerDate()))+") BETWEEN 0 AND " +getDateLimit()+
                    " ORDER BY c.dExpiryDt, a.sBarCodex;";
    }
    
    /**
     * 
     * @return SQL query that expired already
     */
    private String getExpiredInventory(){
        return "SELECT" +
                    " a.sBarCodex" +
                    ",a.sDescript" +
                    ",c.dExpiryDt" +
                    ",c.nQtyOnHnd" +
                    ",d.sMeasurNm" +
                    " FROM Inventory a" +
                    ",Inv_Master b" +
                    ",Inv_Master_Expiration c" +
                    ",Measure d" +
                    " WHERE a.sStockIDx = b.sStockIDx" +
                    " AND b.sStockIDx = c.sStockIDx" +
                    " AND b.sBranchCd = c.sBranchCd" +
                    " AND a.sMeasurID = d.sMeasurID" +
                    " AND a.cWthExprt =" + SQLUtil.toSQL("1")+
                    " AND b.sBranchCd  =" + SQLUtil.toSQL(poGRider.getBranchCode())+
                    " AND b.nQtyOnHnd > 0" +
                    " AND c.nQtyOnHnd > 0" +
                    " AND c.dExpiryDt < " + SQLUtil.toSQL(CommonUtils.xsDateShort(poGRider.getServerDate()))+
                    " ORDER BY c.dExpiryDt, a.sBarCodex;";
    }
    
    /**
     * 
     * @return value of days converted to string
     */
    private String getDateLimit(){
        String lsDateLimit= "";
        try {
            ResultSet loRS = null;
            String lsSQL = "SELECT" +
                    "  sValuexxx" +
                    " FROM xxxOtherConfig" +
                    " WHERE sConfigID = 'Expiry'" +
                    " AND sProdctID =" + SQLUtil.toSQL(poGRider.getProductID());
            loRS = poGRider.executeQuery(lsSQL);
            
            while(loRS.next()){
                lsDateLimit = loRS.getString("sValuexxx");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return lsDateLimit;
    }
    
    /**
     * 
     * For loading of expired inventory
     */
    public void loadExpiredInv(){
        ResultSet poRS = null;
        data01.clear();
        
        poRS = poGRider.executeQuery(getExpiredInventory());
        if (MiscUtil.RecordCount(poRS) <= 0){
            data01.add(new TableModel(String.valueOf(1), 
                                        "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         "",
                                         ""));
             return;
        }
        try {
            poRS.first();
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRS); lnCtr++){
               
                    poRS.absolute(lnCtr);
                    data01.add(new TableModel(String.valueOf(lnCtr), 
                                poRS.getString("sBarCodex"),
                                poRS.getString("sDescript"),
                                poRS.getString("dExpiryDt"),
                                poRS.getString("sMeasurNm"),
                                poRS.getString("nQtyOnHnd"),
                                "",
                                "",
                                "",
                                ""));
    }
        } catch (SQLException ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }
    /**
     * For loading of inventory about to expired
     */
    public void loadDetail2Grid() {
        ResultSet poRS = null;
        data.clear();
        
        poRS = poGRider.executeQuery(getSQLInventory());
        
        if (MiscUtil.RecordCount(poRS) <= 0){
            data.add(new TableModel(String.valueOf(1), 
                                    "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     ""));
        
             return;
        }
        try {
            poRS.first();
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRS); lnCtr++){
                    poRS.absolute(lnCtr);
                    data.add(new TableModel(String.valueOf(lnCtr), 
                                            poRS.getString("sBarCodex"),
                                            poRS.getString("sDescript"),
                                            poRS.getString("dExpiryDt"),
                                            poRS.getString("sMeasurNm"),
                                            poRS.getString("nQtyOnHnd"),
                                            "",
                                            "",
                                            "",
                                            ""));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }
    
    public void loadProduct2Grid() {
        ResultSet poRS = null;
        ResultSet poRSDetail = null;
        data02.clear();  
             TreeItem<TableModel>  ColumnDetail = new TreeItem<>(new TableModel("","BARCODE","DESCRIPT","QTY",
             "","","","","",""));
        
        poRS = poGRider.executeQuery(getSQLProductMaster());
        poRSDetail = poGRider.executeQuery(getSQLProductMaster());
        if (MiscUtil.RecordCount(poRS) <= 0){
            data02.add(new TableModel(String.valueOf(1), 
                                    "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     "",
                                     ""));
        
             return;
        }
        try {
            poRS.first();
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRS); lnCtr++){
                    poRS.absolute(lnCtr);
//                    TableModel rowData = new TableModel();
                    data02.add(new TableModel(String.valueOf(lnCtr), 
                                            poRS.getString("sTransNox"),
                                            poRS.getString("dTransact"),
                                            poRS.getString("sRemarksx"),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            ""));
            }
            
            
            
            

//            list master's
            //dummy
            TreeItem<TableModel> rootMaster = new TreeItem<>();
            
            //list master
//            TreeItem<TableModel> childItem1 = new TreeItem<>();
            
            for (TableModel rowDataMaster : data02) {
            TreeItem<TableModel>  item = new TreeItem<>(rowDataMaster);
            rootMaster.getChildren().add(item);
            
            //listing detail
            //clear to refresh data03 each loop
            data03.clear();
                    
            poRSDetail = poGRider.executeQuery(getSQLProductDetail(rowDataMaster.getIndex02().toString()));
            poRSDetail.first();
            for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(poRSDetail); lnCtr++){
                    poRSDetail.absolute(lnCtr);
//                    TableModel rowData = new TableModel();
                    data03.add(new TableModel(String.valueOf(lnCtr), 
                                            poRSDetail.getString("sBarCodex"),
                                            poRSDetail.getString("sDescript"),
                                            poRSDetail.getString("nQuantity"),
                                            "",
                                            "",
                                            "",
                                            "",
                                            "",
                                            ""));
            }
            if (!data03.isEmpty()){
            item.getChildren().add(ColumnDetail);
            }
            for (TableModel rowDataDetail : data03) {
            TreeItem<TableModel>  item1= new TreeItem<>(rowDataDetail);
             
            item.getChildren().add(item1);
            
            }
            if(item.isExpanded());{
                item.setExpanded(false);
            }
            }
        
  
            
        //display
        ProductTable.setRoot(rootMaster);
        ProductTable.setShowRoot(false);
//            }
        } catch (SQLException ex) {
            Logger.getLogger(MDIMainController.class.getName()).log(Level.SEVERE, null, ex);
            return;
        }
    }
    private String getSQLProductMaster(){
        return "SELECT " + 
                " a.sTransNox" +
                ", a.dTransact" +
                ", a.nEntryNox" + 
                ", a.sRemarksx" +
                ", a.cTranStat" +
                " FROM Product_Request_Master a" +
                " WHERE a.cTranStat IN ('0', '1') " ;
                
    }
    private String getSQLProductDetail(String sTransnox){
        
        return 
                "SELECT " + 
                " a.sTransNox" +
                ", a.sStockIDx" +
                ", a.nQuantity" +
                ", a.nEntryNox" +
                ", b.sBarCodex" +
                ", b.sDescript" + 
                ", IFNULL(c.sDescript,'') xBrandNme" + 
                ", IFNULL(d.sDescript,'') xModelNme" + 
                ", IFNULL(e.sDescript,'') xInvTypNm" + 
                ", f.sMeasurNm " +
                " FROM Product_Request_Detail a " +
                " LEFT JOIN Inventory b " + 
                "   ON a.sStockIDx = b.sStockIDx " +
                " LEFT JOIN Brand c" + 
                    " ON b.sBrandCde = c.sBrandCde" + 
                " LEFT JOIN Model d" + 
                    " ON b.sModelCde = d.sModelCde" + 
                " LEFT JOIN Inv_Type e" + 
                    " ON b.sInvTypCd = e.sInvTypCd" +
                " LEFT JOIN Measure f" + 
                    " ON b.sMeasurID = f.sMeasurID " +
                " WHERE a.sTransNox = " + SQLUtil.toSQL(sTransnox);
        
        
      
    }
    
}
