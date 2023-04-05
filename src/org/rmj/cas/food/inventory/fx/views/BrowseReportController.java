
package org.rmj.cas.food.inventory.fx.views;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;

public class BrowseReportController implements Initializable {

    @FXML private AnchorPane dataPane;
    @FXML private Button btnExit;
    @FXML private FontAwesomeIconView glyphExit;
    @FXML private TableView<?> tableResult;
    @FXML private Button btnOk;
    @FXML private Button btnCancel;

 
    @Override
    public void initialize(URL url, ResourceBundle rb) {
 
    }    

    @FXML
    private void tableResult_Click(MouseEvent event) {
    }
    
}
