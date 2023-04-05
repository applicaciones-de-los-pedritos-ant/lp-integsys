package org.rmj.cas.food.inventory.fx.modules;

import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ui.showFXDialog;

public class qsTransactions {
    private final static String pxeModuleName = "qsTransactions";
    
    public static JSONObject getPurchaseOrder(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Order No»Branch»Date»Total»Inv. Type»Supplier";
        String lsColName = "sTransNox»sBranchNm»dTransact»nTranTotl»sDescript»sClientNm";
        String lsColCrit = "a.sTransNox»b.sBranchNm»a.dTransact»a.nTranTotl»c.sDescript»d.sClientNm";
        String lsSQL = "SELECT " +
                            "  a.sTransNox" +
                            ", a.sBranchCd" + 
                            ", a.dTransact" +
                            ", a.sInvTypCd" +
                            ", a.nTranTotl" + 
                            ", b.sBranchNm" + 
                            ", c.sDescript" + 
                            ", d.sClientNm" + 
                            ", a.cTranStat" + 
                            ", CASE " +
                                " WHEN a.cTranStat = '0' THEN 'OPEN'" +
                                " WHEN a.cTranStat = '1' THEN 'CLOSED'" +
                                " WHEN a.cTranStat = '2' THEN 'POSTED'" +
                                " WHEN a.cTranStat = '3' THEN 'CANCELLED'" +
                                " WHEN a.cTranStat = '4' THEN 'VOID'" +
                                " END AS xTranStat" +
                        " FROM PO_Master a" + 
                                    " LEFT JOIN Branch b" + 
                                        " ON a.sBranchCd = b.sBranchCd" + 
                                    " LEFT JOIN Inv_Type c" + 
                                        " ON a.sInvTypCd = c.sInvTypCd" + 
                                ", Client_Master d" + 
                        " WHERE a.sSupplier = d.sClientID";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getPOReceiving(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Refer Date»Supplier»Refer No.»Trans No.»Trans Date";
        String lsColName = "dRefernce»sClientNm»sReferNox»sTransNox»dTransact";
        String lsColCrit = "a.dRefernce»d.sClientNm»a.sReferNox»a.sTransNox»a.dTransact";
        String lsSQL = "SELECT " +
                            "  a.sTransNox" +
                            ", a.sBranchCd" + 
                            ", a.dTransact" +
                            ", a.sInvTypCd" +
                            ", a.nTranTotl" + 
                            ", b.sBranchNm" + 
                            ", c.sDescript" + 
                            ", d.sClientNm" + 
                            ", a.cTranStat" + 
                            ", a.dRefernce" +
                            ", a.sReferNox" + 
                            ", CASE " +
                                " WHEN a.cTranStat = '0' THEN 'OPEN'" +
                                " WHEN a.cTranStat = '1' THEN 'CLOSED'" +
                                " WHEN a.cTranStat = '2' THEN 'POSTED'" +
                                " WHEN a.cTranStat = '3' THEN 'CANCELLED'" +
                                " WHEN a.cTranStat = '4' THEN 'VOID'" +
                                " END AS xTranStat" +
                        " FROM PO_Receiving_Master a" + 
                                    " LEFT JOIN Branch b" + 
                                        " ON a.sBranchCd = b.sBranchCd" + 
                                    " LEFT JOIN Inv_Type c" + 
                                        " ON a.sInvTypCd = c.sInvTypCd" + 
                                ", Client_Master d" + 
                        " WHERE a.sSupplier = d.sClientID";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
}