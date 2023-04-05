package org.rmj.cas.food.inventory.fx.modules;

import org.json.simple.JSONObject;
import org.rmj.appdriver.GRider;
import org.rmj.appdriver.agentfx.ui.showFXDialog;

public class qsParameter {
    private final static String pxeModuleName = "qsParameter";
    
    public static JSONObject getTerm(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name";
        String lsColName = "sTermCode»sDescript";
        String lsColCrit = "sTermCode»sDescript";
        String lsSQL = "SELECT " +
                            "  sTermCode" +
                            ", sDescript" + 
                            ", cCoverage" + 
                            ", nTermValx" + 
                            ", cRecdStat" + 
                        " FROM Term";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getInvType(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name";
        String lsColName = "sInvTypCd»sDescript";
        String lsColCrit = "sInvTypCd»sDescript";
        String lsSQL = "SELECT " +
                            "  sInvTypCd" +
                            ", sDescript" + 
                            ", cRecdStat" + 
                        " FROM Inv_Type";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getCompany(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "ID»Name»Code";
        String lsColName = "sCompnyID»sCompnyNm»sCompnyCd";
        String lsColCrit = "sCompnyID»sCompnyNm»sCompnyCd";
        String lsSQL = "SELECT " +
                            "  sCompnyID" +
                            ", sCompnyNm" + 
                            ", sCompnyCd" + 
                            ", sEmplyrNo" + 
                            ", cRecdStat" + 
                        " FROM Company";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getBrand(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name»Inv. Type";
        String lsColName = "sBrandCde»sDescript»xInvTypNm";
        String lsColCrit = "a.sBrandCde»a.sDescript»b.sDescript";
        String lsSQL = "SELECT " +
                            "  a.sBrandCde" +
                            ", a.sInvTypCd" + 
                            ", a.sDescript" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xInvTypNm" + 
                        " FROM Brand a" +
                            " LEFT JOIN Inv_Type b" + 
                                " ON a.sInvTypCd = b.sInvTypCd";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getCategory(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name»Inv. Type";
        String lsColName = "sCategrCd»sDescript»xInvTypNm";
        String lsColCrit = "a.sCategrCd»a.sDescript»b.sDescript";
        String lsSQL = "SELECT " +
                            "  a.sCategrCd" +
                            ", a.sDescript" + 
                            ", a.sInvTypCd" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xInvTypNm" + 
                        " FROM Category a" +
                            " LEFT JOIN Inv_Type b" + 
                                " ON a.sInvTypCd = b.sInvTypCd";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getCategory2(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name»Inv. Type»Main Categ.";
        String lsColName = "sCategrCd»sDescript»xInvTypNm»xCategrNm";
        String lsColCrit = "a.sCategrCd»a.sDescript»b.sDescript»c.sDescript";
        String lsSQL = "SELECT " +
                            "  a.sCategrCd" +
                            ", a.sDescript" + 
                            ", a.sInvTypCd" + 
                            ", a.sMainCatx" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xInvTypNm" + 
                            ", c.sDescript xCategrNm" +
                        " FROM Category_Level2 a" +
                            " LEFT JOIN Inv_Type b" + 
                                " ON a.sInvTypCd = b.sInvTypCd" + 
                            " LEFT JOIN Category c" +
                                " ON a.sMainCatx = c.sCategrCd";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getCategory3(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name»Main Categ.";
        String lsColName = "sCategrCd»sDescript»xCategrNm";
        String lsColCrit = "a.sCategrCd»a.sDescript»b.sDescript";
        String lsSQL = "SELECT " +
                            "  a.sCategrCd" +
                            ", a.sDescript" + 
                            ", a.sMainCatx" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xCategrNm" + 
                        " FROM Category_Level3 a" +
                            " LEFT JOIN Category b" + 
                                " ON a.sMainCatx = b.sCategrCd";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getCategory4(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name»Main Categ.";
        String lsColName = "sCategrCd»sDescript»xCategrNm";
        String lsColCrit = "a.sCategrCd»a.sDescript»b.sDescript";
        String lsSQL = "SELECT " +
                            "  a.sCategrCd" +
                            ", a.sDescript" + 
                            ", a.sMainCatx" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xCategrNm" + 
                        " FROM Category_Level4 a" +
                            " LEFT JOIN Category b" + 
                                " ON a.sMainCatx = b.sCategrCd";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getModel(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Description»Category»Brand»Inv. Type";
        String lsColName = "sModelCde»sDescript»xCategrNm»xBrandNme»xInvTypNm";
        String lsColCrit = "a.sModelCde»a.sDescript»d.sDescript»c.sDescript»b.sDescript";
        String lsSQL = "SELECT " +
                            "  a.sModelCde" +
                            ", a.sInvTypCd" + 
                            ", a.sBriefDsc" + 
                            ", a.sModelNme" + 
                            ", a.sDescript" + 
                            ", a.sBrandCde" + 
                            ", a.sCategrCd" + 
                            ", a.cEndOfLfe" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xInvTypNm" +
                            ", c.sDescript xBrandNme" +
                            ", d.sDescript xCategrNm" +
                        " FROM Model a" +
                            " LEFT JOIN Inv_Type b" + 
                                " ON a.sInvTypCd = b.sInvTypCd" + 
                            " LEFT JOIN Brand c" +
                                " ON a.sBrandCde = c.sBrandCde" + 
                            " LEFT JOIN Category_Level2 d" + 
                                " ON a.sCategrCd = d.sCategrCd";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getMeasure(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "ID»Name";
        String lsColName = "sMeasurID»sMeasurNm";
        String lsColCrit = "sMeasurID»sMeasurNm";
        String lsSQL = "SELECT " +
                            "  sMeasurID" +
                            ", sMeasurNm" + 
                            ", cRecdStat" + 
                        " FROM Measure";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getColor(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name";
        String lsColName = "sColorCde»sDescript";
        String lsColCrit = "sColorCde»sDescript";
        String lsSQL = "SELECT " +
                            "  sColorCde" +
                            ", sDescript" + 
                            ", cRecdStat" + 
                        " FROM Color";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getInvLocation(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Description»Brief Desc.";
        String lsColName = "sLocatnCd»sDescript»sBriefDsc";
        String lsColCrit = "sLocatnCd»sDescript»sBriefDsc";
        String lsSQL = "SELECT " +
                            "  sLocatnCd" +
                            ", sBriefDsc" + 
                            ", sDescript" + 
                            ", cRecdStat" + 
                        " FROM Inv_Location";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getInventory(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Barcode»Description»Inv. Type»Brand»Model»Stock ID";
        String lsColName = "sBarCodex»sDescript»xInvTypNm»xBrandNme»xModelNme»sStockIDx";
        String lsColCrit = "a.sBarCodex»a.sDescript»d.sDescript»b.sDescript»c.sDescript»a.sStockIDx";
        String lsSQL = "SELECT " +
                            "  a.sStockIDx" +
                            ", a.sBarCodex" + 
                            ", a.sDescript" + 
                            ", a.sBriefDsc" + 
                            ", a.sAltBarCd" + 
                            ", a.sCategCd1" + 
                            ", a.sCategCd2" + 
                            ", a.sCategCd3" + 
                            ", a.sCategCd4" + 
                            ", a.sBrandCde" + 
                            ", a.sModelCde" + 
                            ", a.sColorCde" + 
                            ", a.sInvTypCd" + 
                            ", a.nUnitPrce" + 
                            ", a.nSelPrice" + 
                            ", a.nDiscLev1" + 
                            ", a.nDiscLev2" + 
                            ", a.nDiscLev3" + 
                            ", a.nDealrDsc" + 
                            ", a.cComboInv" + 
                            ", a.cWthPromo" + 
                            ", a.cSerialze" + 
                            ", a.cUnitType" + 
                            ", a.cInvStatx" + 
                            ", a.sSupersed" + 
                            ", a.cRecdStat" + 
                            ", b.sDescript xBrandNme" + 
                            ", c.sDescript xModelNme" + 
                            ", d.sDescript xInvTypNm" + 
                        " FROM Inventory a" + 
                            " LEFT JOIN Brand b" + 
                                " ON a.sBrandCde = b.sBrandCde" + 
                            " LEFT JOIN Model c" + 
                                " ON a.sModelIDx = c.sModelCde" + 
                            " LEFT JOIN Inv_Type d" + 
                                " ON a.sInvTypCd = d.sInvTypCd";
                
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getBranch(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "Code»Name»Company";
        String lsColName = "sBranchCd»sBranchNm»sCompnyNm";
        String lsColCrit = "a.sBranchCd»a.sBranchNm»b.sCompnyNm";
        String lsSQL = "SELECT " +
                            "  a.sBranchCd" +
                            ", a.sBranchNm" + 
                            ", a.sCompnyID" + 
                            ", a.cRecdStat" + 
                            ", b.sCompnyNm" + 
                        " FROM Branch a" + 
                            " LEFT JOIN Company b" +
                                " ON a.sCompnyID = b.sCompnyID";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getClient(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "ID»Name»Address";
        String lsColName = "sClientID»sClientNm»xAddressx";
        String lsColCrit = "a.sClientID»a.sClientNm»CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName)";
        String lsSQL = "SELECT " +
                            "  a.sClientID" +
                            ", a.sClientNm" +
                            ", CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName) xAddressx" + 
                        " FROM Client_Master a" + 
                            " LEFT JOIN Client_Address b" + 
                                " ON a.sClientID = b.sClientID" + 
                                    " AND b.nPriority = 1" +
                            " LEFT JOIN TownCity c" + 
                                " ON b.sTownIDxx = c.sTownIDxx" + 
                            " LEFT JOIN Province d" +
                                " ON c.sProvIDxx = d.sProvIDxx"; 
                        //" WHERE a.cClientTp = '1'";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject searchClientCompany(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "ID»Name»Address»Last Name»First Name»Midd Name»Suffix";
        String lsColName = "sClientID»sClientNm»xAddressx»sLastName»sFrstName»sMiddName»sSuffixNm";
        String lsColCrit = "a.sClientID»a.sClientNm»CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName)»a.sLastName»a.sFrstName»a.sMiddName»a.sSuffixNm";
        String lsSQL = "SELECT " +
                            "  a.sClientID" +
                            ", a.sClientNm" +
                            ", CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName) xAddressx" +
                            ", a.sLastName" + 
                            ", a.sFrstName" + 
                            ", a.sMiddName" + 
                            ", a.sSuffixNm" + 
                        " FROM Client_Master a" + 
                            " LEFT JOIN Client_Address b" + 
                                " ON a.sClientID = b.sClientID" + 
                                    " AND b.nPriority = 1" +
                            " LEFT JOIN TownCity c" + 
                                " ON b.sTownIDxx = c.sTownIDxx" + 
                            " LEFT JOIN Province d" +
                                " ON c.sProvIDxx = d.sProvIDxx";
                        //" WHERE a.cClientTp = '0'";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getClientCompany(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "ID»Name»Address»Last Name»First Name»Midd Name»Suffix";
        String lsColName = "sClientID»sClientNm»xAddressx»sLastName»sFrstName»sMiddName»sSuffixNm";
        String lsColCrit = "a.sClientID»a.sClientNm»CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName)»a.sLastName»a.sFrstName»a.sMiddName»a.sSuffixNm";
        String lsSQL = "SELECT " +
                            "  a.sClientID" +
                            ", a.sClientNm" +
                            ", CONCAT(b.sHouseNox, ' ', b.sAddressx, ', ', c.sTownName, ' ', d.sProvName) xAddressx" +
                            ", a.sLastName" + 
                            ", a.sFrstName" + 
                            ", a.sMiddName" + 
                            ", a.sSuffixNm" + 
                        " FROM Client_Master a" + 
                            " LEFT JOIN Client_Address b" + 
                                " ON a.sClientID = b.sClientID" + 
                                    " AND b.nPriority = 1" +
                            " LEFT JOIN TownCity c" + 
                                " ON b.sTownIDxx = c.sTownIDxx" + 
                            " LEFT JOIN Province d" +
                                " ON c.sProvIDxx = d.sProvIDxx";  
                        //" WHERE a.cClientTp = '0'";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
        
    public static JSONObject getSupplier(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "ID»Name»Branch";
        String lsColName = "sClientID»sClientNm»sBranchNm";
        String lsColCrit = "a.sClientID»b.sClientNm»c.sBranchNm";
        String lsSQL = "SELECT " +
                            "  a.sClientID" +
                            ", b.sClientNm" + 
                            ", c.sBranchNm" + 
                            ", a.sBranchCd" +
                        " FROM Supplier a" + 
                            ", Client_Master b" + 
                            ", Branch c" + 
                        " WHERE a.sClientID = b.sClientID" +
                            " AND a.sBranchCd = c.sBranchCd";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
    
    public static JSONObject getDepartment(GRider foGRider, String fsValue, int fnSort){
        String lsHeader = "ID»Department»sDeptCode";
        String lsColName = "sDeptIDxx»sDeptName»sDeptCode";
        String lsColCrit = "sDeptIDxx»sDeptName»sDeptCode";
        String lsSQL = "SELECT " +
                            "  sDeptIDxx" +
                            ", sDeptName" + 
                            ", sDeptCode" + 
                        " FROM Department" + 
                        " WHERE cRecdStat = '1'";
        
        return showFXDialog.jsonSearch(foGRider, 
                                        lsSQL, 
                                        fsValue, 
                                        lsHeader, 
                                        lsColName, 
                                        lsColCrit, 
                                        fnSort);
    }
}
