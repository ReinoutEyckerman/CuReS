package com.company.UI.menu;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.company.UI.CueGridController;
import com.company.UI.model.DataModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import static java.lang.System.out;

public class MenuController {

    public Button start;
    public Button outpath;
    public Button saveButton;
    private DataModel model ;
    public Button openFile;
    public Button removeFile;
    private TextArea cueSheet;
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
    public void addListView(ListView list, List<CueGridController> controllers, TextArea cuesheet){
        this.listview=list;
        this.controllers=controllers;
        this.cueSheet=cuesheet;
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

    public void setOutpath(ActionEvent actionEvent) {
        DirectoryChooser dchooser=new DirectoryChooser();
        File file=dchooser.showDialog(menuBar.getScene().getWindow());
        if(file!=null)
            model.setOutPath(file);
    }

    public void saveCue(ActionEvent actionEvent) {
        FileWriter fw = null;
        try {
            fw = new FileWriter(model.cueFiles.get(model.cueFiles.size()-1));
            PrintWriter out = new PrintWriter(fw);
            out.write(cueSheet.getText());
            //Flush the output to the file
            out.flush();

            //Close the Print Writer
            out.close();

            //Close the File Writer
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Your cue file has been saved.");
        alert.showAndWait();
    }
}
