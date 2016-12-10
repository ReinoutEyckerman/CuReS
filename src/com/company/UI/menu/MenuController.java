package com.company.UI.menu;

import java.io.File;
import java.util.List;

import com.company.UI.CueGridController;
import com.company.UI.model.DataModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.stage.FileChooser;

public class MenuController {

    public Button start;
    private DataModel model ;
    public Button openFile;
    public Button removeFile;
    private ListView listview;

    @FXML
    private MenuBar menuBar ;
    private List<CueGridController> controllers;

    public void initModel(DataModel model) {
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model = model ;
    }
    public void addListView(ListView list, List<CueGridController> controllers){
        this.listview=list;
        this.controllers=controllers;
    }
    @FXML
    public void save() {

        // similar to load...

    }

    @FXML
    public void exit() {
        menuBar.getScene().getWindow().hide();
    }

    public void loadcue(ActionEvent actionEvent) {
        FileChooser chooser = new FileChooser();
        File file = chooser.showOpenDialog(menuBar.getScene().getWindow());
        if (file != null) {
            model.addCue(file);
        }
    }

    public void convertcue(ActionEvent actionEvent) {
        model.convertCue(controllers);
    }

    public void removeEntry(ActionEvent actionEvent) {
        int i=listview.getSelectionModel().getSelectedIndex();
        listview.getItems().remove(i);
        model.removeEntry(i);

    }
}
