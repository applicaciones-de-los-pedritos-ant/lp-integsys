package org.rmj.cas.food.inventory.fx.views;

import com.sun.javafx.scene.control.skin.TableHeaderRow;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.MiscUtil;
import org.rmj.appdriver.SQLUtil;
import org.rmj.appdriver.agentfx.CommonUtils;
import org.rmj.appdriver.agentfx.ShowMessageFX;

public class ARDeliverySCHistoryController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private Button cmdClose, cmdOkay;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private TextField txtField01, txtField02, txtField03;
    @FXML
    private TableView tblHistory;
    @FXML
    private TableColumn index01, index02, index03;

    @FXML
    void closeForm(ActionEvent event) {
        CommonUtils.closeStage(cmdClose);

    }

    @FXML
    void entryOK(ActionEvent event) {
        CommonUtils.closeStage(cmdOkay);
    }

    private GRider oApp;
    private String lsRiderID;
    private boolean pbLoaded = false;
    private int pnRow = -1;

    private final ObservableList<TableModel> history_data = FXCollections.observableArrayList();

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }
    public void setsRiderIDxx(String foValue) {
        lsRiderID = foValue;
    }
    
    public boolean getpbLoaded() {
        return pbLoaded;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO

        pbLoaded = true;
        initGrid();
        if (!loadHistoryData()) {
            ShowMessageFX.Warning(null, "History Form", "No Record Found");
           pbLoaded = false;
        }

    }

    private void initGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));

        tblHistory.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblHistory.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblHistory.setItems(history_data);
    }

    private boolean loadHistoryData() {
        ResultSet loRS = null;
        try {
            String lsSQL = "";

            lsSQL = lsSQL = "SELECT"
                    + "  sRiderIDx"
                    + ", dSrvcChrg"
                    + ", nSrvcChrg"
                    + ", dTimeStmp"
                    + " FROM Delivery_Service_Charge_History "
                    + " WHERE sRiderIDx = " + SQLUtil.toSQL(lsRiderID)
                    + " ORDER BY dTimeStmp ";

            loRS = oApp.executeQuery(lsSQL);
            history_data.clear();
            if (!(MiscUtil.RecordCount(loRS) <= 0)) {

                /*ADD THE DETAIL*/
                loRS.first();
                for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(loRS); lnCtr++) {
                    if (lnCtr == 1) {
                        txtField01.setText(loRS.getString("sRiderIDx"));
                        txtField02.setText(FoodInventoryFX.xsRequestFormat((Date) loRS.getObject("dSrvcChrg")));
                        txtField03.setText(CommonUtils.NumberFormat((Number) loRS.getObject("nSrvcChrg"), "#,##0.00"));
                    }
                    loRS.absolute(lnCtr);
                    history_data.add(new TableModel(String.valueOf(lnCtr),
                            FoodInventoryFX.xsRequestFormat((Date) loRS.getObject("dSrvcChrg")),
                            CommonUtils.NumberFormat((Number) loRS.getObject("nSrvcChrg"), "#,##0.00"),
                            "",
                            "",
                            "",
                            "",
                            "",
                            "",
                            ""
                    ));
                }
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(ARDeliverySCHistoryController.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;
    }
}
