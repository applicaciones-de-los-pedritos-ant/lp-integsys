package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


/**
 * FXML Controller class
 *
 * @author jovanalic
 * since 07-03-21
 */
public class InvStockRequestRegController implements Initializable {

    @FXML private AnchorPane dataPane;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private AnchorPane anchorField;
    @FXML private Label lblHeader;
    @FXML private TextField txtField01;
    @FXML private TextField txtField03;
    @FXML private TextField txtField04;
    @FXML private TextField txtField06;
    @FXML private TextArea txtField05;
    @FXML private TextField txtDetail05;
    @FXML private TextField txtDetail03;
    @FXML private TextField txtDetail80;
    @FXML private TextArea txtDetail10;
    @FXML private TextField txtDetail04;
    @FXML private TextField txtDetail07;
    @FXML private TextField txtDetail06;
    @FXML private TableView table;
    @FXML private ImageView imgTranStat;
    @FXML private TextField txtField50;
    @FXML private TextField txtField51;
    @FXML private TextField txtOther02;
    @FXML private Button btnNew;
    @FXML private Button btnSave;
    @FXML private Button btnCancel;
    @FXML private Button btnClose;
    @FXML private Button btnSearch;
    @FXML private Button btnPrint;
    @FXML private Button btnDel;
    @FXML private Button btnBrowse;
    @FXML private TextArea txtField051;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void table_Clicked(MouseEvent event) {
    }
    
}
