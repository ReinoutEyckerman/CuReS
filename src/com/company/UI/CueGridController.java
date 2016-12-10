package com.company.UI;


import com.company.UI.model.DataModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.text.Text;
import org.apache.commons.io.FileUtils;

import javax.print.DocFlavor;
import java.io.File;
import java.util.ResourceBundle;


/**
 * Created by reinout on 11/19/16.
 */
public class CueGridController {

    @FXML
    public Text artist;
    public Text album;
    public ListView trackList;
    public DataModel model;
    public ProgressBar progress;

    public void insertModel(DataModel model){
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model=model;
    }
}
