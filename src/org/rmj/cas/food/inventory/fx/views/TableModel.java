package org.rmj.cas.food.inventory.fx.views;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TableModel {  
    private StringProperty index01;
    private StringProperty index02;        
    private StringProperty index03;     
    private StringProperty index04;     
    private StringProperty index05;
    private StringProperty index06;
    private StringProperty index07;
    private StringProperty index08;
    private StringProperty index09;
    private StringProperty index10;
    private StringProperty index11;
    private StringProperty index12;

    
    public TableModel(){
        this.index01 = new SimpleStringProperty("");
        this.index02 = new SimpleStringProperty("");
        this.index03 = new SimpleStringProperty("");
        this.index04 = new SimpleStringProperty("");
        this.index05 = new SimpleStringProperty("");
        this.index06 = new SimpleStringProperty("");
        this.index07 = new SimpleStringProperty("");
        this.index08 = new SimpleStringProperty("");
        this.index09 = new SimpleStringProperty("");
        this.index10 = new SimpleStringProperty("");
        this.index11 = new SimpleStringProperty("");
        this.index12 = new SimpleStringProperty("");
    }
    
    TableModel(String index01, String index02, String index03, String index04, String index05,
                String index06, String index07, String index08, String index09, String index10){
        this.index01 = new SimpleStringProperty(index01);
        this.index02 = new SimpleStringProperty(index02);
        this.index03 = new SimpleStringProperty(index03);
        this.index04 = new SimpleStringProperty(index04);
        this.index05 = new SimpleStringProperty(index05);
        this.index06 = new SimpleStringProperty(index06);
        this.index07 = new SimpleStringProperty(index07);
        this.index08 = new SimpleStringProperty(index08);
        this.index09 = new SimpleStringProperty(index09);
        this.index10 = new SimpleStringProperty(index10);
    }
    TableModel(String index01, String index02, String index03, String index04, String index05,
                String index06, String index07, String index08, String index09, String index10, String index11){
        this.index01 = new SimpleStringProperty(index01);
        this.index02 = new SimpleStringProperty(index02);
        this.index03 = new SimpleStringProperty(index03);
        this.index04 = new SimpleStringProperty(index04);
        this.index05 = new SimpleStringProperty(index05);
        this.index06 = new SimpleStringProperty(index06);
        this.index07 = new SimpleStringProperty(index07);
        this.index08 = new SimpleStringProperty(index08);
        this.index09 = new SimpleStringProperty(index09);
        this.index10 = new SimpleStringProperty(index10);
        this.index11 = new SimpleStringProperty(index11);
    }
       
    TableModel(String index01, String index02, String index03, String index04, String index05,
                String index06, String index07, String index08, String index09, String index10, String index11, String index12){
        this.index01 = new SimpleStringProperty(index01);
        this.index02 = new SimpleStringProperty(index02);
        this.index03 = new SimpleStringProperty(index03);
        this.index04 = new SimpleStringProperty(index04);
        this.index05 = new SimpleStringProperty(index05);
        this.index06 = new SimpleStringProperty(index06);
        this.index07 = new SimpleStringProperty(index07);
        this.index08 = new SimpleStringProperty(index08);
        this.index09 = new SimpleStringProperty(index09);
        this.index10 = new SimpleStringProperty(index10);
        this.index11 = new SimpleStringProperty(index11);
        this.index12 = new SimpleStringProperty(index12);
    }
       
    
    public String getIndex01(){return index01.get();}
    public void setIndex01(String index01){this.index01.set(index01);}
    
    public String getIndex02(){return index02.get();}
    public void setIndex02(String index02){this.index02.set(index02);}
    
    public String getIndex03(){return index03.get();}
    public void setIndex03(String index03){this.index03.set(index03);}
    
    public String getIndex04(){return index04.get();}
    public void setIndex04(String index04){this.index04.set(index04);}
    
    public String getIndex05(){return index05.get();}
    public void setIndex05(String index05){this.index05.set(index05);}
    
    public String getIndex06(){return index06.get();}
    public void setIndex06(String index06){this.index06.set(index06);}
    
    public String getIndex07(){return index07.get();}
    public void setIndex07(String index07){this.index07.set(index07);}
    
    public String getIndex08(){return index08.get();}
    public void setIndex08(String index08){this.index08.set(index08);}
    
    public String getIndex09(){return index09.get();}
    public void setIndex09(String index09){this.index09.set(index09);}
    
    public String getIndex10(){return index10.get();}
    public void setIndex10(String index10){this.index10.set(index10);}
    
    
    public String getIndex11() {
        return index11.get();
    }

    public void setIndex11(String index11) {
        this.index11.set(index11); 
    }
    
    public String getIndex12() {
        return index12.get();
    }

    public void setIndex12(String index12) {
        this.index12.set(index12); 
    }
    
    public StringProperty index01Property() {
        return index01;
    }
    
    public StringProperty index02Property() {
        return index02;
    }
    
    public StringProperty index03Property() {
        return index03;
    }
    
    public StringProperty index04Property() {
        return index04;
    }    
    public StringProperty index05Property() {
        return index05;
    }
    
    public StringProperty index06Property() {
        return index06;
    }
    
}