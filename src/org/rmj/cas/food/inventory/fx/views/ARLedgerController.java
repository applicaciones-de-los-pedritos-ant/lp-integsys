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

public class ARLedgerController implements Initializable {

    @FXML
    private AnchorPane MainAnchorPane;
    @FXML
    private Button cmdClose, cmdOkay;
    @FXML
    private FontAwesomeIconView glyphExit;
    @FXML
    private TextField txtField01, txtField02;
    @FXML
    private TableView tblLedger;
    @FXML
    private TableColumn index01, index02, index03, index04, index05, index06;

    @FXML
    void closeForm(ActionEvent event) {
        CommonUtils.closeStage(cmdClose);

    }

    @FXML
    void entryOK(ActionEvent event) {
        CommonUtils.closeStage(cmdOkay);
    }

    private GRider oApp;
    private String lsClientID;
    private String lsBranchCd;
    private boolean pbLoaded = false;
    private int pnRow = -1;

    private final ObservableList<TableModel> ledger_data = FXCollections.observableArrayList();

    public void setGRider(GRider foValue) {
        oApp = foValue;
    }

    public void setsClientID(String foValue) {
        lsClientID = foValue;
    }

    public void setsBranchCd(String foValue) {
        lsBranchCd = foValue;
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
        if (!loadLedgerData()) {
            ShowMessageFX.Warning(null, "Ledger Form", "No Record Found");
            pbLoaded = false;
        }
    }

    private void initGrid() {
        index01.setStyle("-fx-alignment: CENTER;");
        index02.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index03.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index04.setStyle("-fx-alignment: CENTER-LEFT;-fx-padding: 0 0 0 5;");
        index05.setStyle("-fx-alignment: CENTER;");
        index06.setStyle("-fx-alignment: CENTER-RIGHT;-fx-padding: 0 5 0 0;");

        index01.setCellValueFactory(new PropertyValueFactory<>("index01"));
        index02.setCellValueFactory(new PropertyValueFactory<>("index02"));
        index03.setCellValueFactory(new PropertyValueFactory<>("index03"));
        index04.setCellValueFactory(new PropertyValueFactory<>("index04"));
        index05.setCellValueFactory(new PropertyValueFactory<>("index05"));
        index06.setCellValueFactory(new PropertyValueFactory<>("index06"));

        tblLedger.widthProperty().addListener((ObservableValue<? extends Number> source, Number oldWidth, Number newWidth) -> {
            TableHeaderRow header = (TableHeaderRow) tblLedger.lookup("TableHeaderRow");
            header.reorderingProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
                header.setReordering(false);
            });
        });

        tblLedger.setItems(ledger_data);

    }

    private boolean loadLedgerData() {
        ResultSet loRS = null;
        try {
            String lsSQL = "";

            lsSQL = lsSQL = "SELECT"
                    + "  a.sClientID sClientID "
                    + ", b.sCompnyNm sCompnyNm "
                    + ", b.sClientNm sClientNm "
                    + ", a.sBranchCd sBranchCd "
                    + ", c.sBranchNm sBranchNm "
                    + ", a.nEntryNox nEntryNox "
                    + ", a.dTransact dTransact "
                    + ", a.sSourceCd sSourceCd "
                    + ", a.sSourceNo sSourceNo "
                    + ", a.cReversex cReversex "
                    + ", CASE "
                    + " WHEN a.nCreditxx > 0 THEN nCreditxx "
                    + " WHEN a.nDebitxxx > 0 THEN nDebitxxx "
                    + " END AS xAmount "
                    + " FROM AR_Ledger a"
                    + " LEFT JOIN Client_Master b "
                    + " ON a.sClientID = b.sClientID "
                    + " LEFT JOIN Branch c "
                    + " ON a.sBranchCd = c.sBranchCd "
                    + " WHERE a.sClientID = " + SQLUtil.toSQL(lsClientID)
                    + " AND a.sBranchCd = " + SQLUtil.toSQL(lsBranchCd)
                    + " ORDER BY a.nEntryNox , a.dPostedxx ";

            loRS = oApp.executeQuery(lsSQL);
            ledger_data.clear();
            if (!(MiscUtil.RecordCount(loRS) <= 0)) {

                /*ADD THE DETAIL*/
                loRS.first();
                for (int lnCtr = 1; lnCtr <= MiscUtil.RecordCount(loRS); lnCtr++) {
                    if (lnCtr == 1) {
                        if (!(loRS.getObject("sCompnyNm").equals("") ) ){
                        txtField01.setText(loRS.getString("sCompnyNm"));
                        }else{ 
                            txtField01.setText(loRS.getString("sClientNm"));
                        }
                        txtField02.setText(loRS.getString("sBranchNm"));
                    }
                    loRS.absolute(lnCtr);
                    ledger_data.add(new TableModel(String.valueOf(lnCtr),
                            FoodInventoryFX.xsRequestFormat((Date) loRS.getObject("dTransact")),
                            ((String) loRS.getObject("sSourceCd")),
                            ((String) loRS.getObject("sSourceNo")),
                            ((String) loRS.getObject("cReversex")),
                            CommonUtils.NumberFormat((Number) loRS.getObject("xAmount"), "#,##0.00"),
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
            Logger.getLogger(ARLedgerController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return true;
    }

}
