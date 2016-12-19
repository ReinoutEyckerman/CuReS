package com.company.UI.menu;

import com.company.UI.CueGridController;
import com.company.UI.model.DataModel;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

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
    public void exit() {
        menuBar.getScene().getWindow().hide();
    }

    public void loadcue(ActionEvent actionEvent) {
        cueSheet.clear();
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
        cueSheet.clear();
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
            File file;
            if(model.cueFiles.size()!=0) {
                file=model.cueFiles.get(model.cueFiles.size()-1);
            }
            else {
                FileChooser chooser = new FileChooser();
                file = chooser.showOpenDialog(menuBar.getScene().getWindow());
            }
            if(file!=null) {
                fw = new FileWriter(file);
                PrintWriter out = new PrintWriter(fw);
                out.write(cueSheet.getText());
                out.flush();
                out.close();
                fw.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        listview.getItems().remove(listview.getItems().size()-1);
        cueSheet.clear();
        File f=model.cueFiles.get(model.cueFiles.size()-1);
        model.cueFiles.remove(f);
        model.addCue(f);
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Your cue file has been saved.");
        alert.showAndWait();
    }

    public void showHelp(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("First load in a .cue file. Edit it to your likings, then make sure to save it. Select an output folder, press the Convert button, and after a while it should be finished.");
        alert.showAndWait();
    }

    public void showAbout(ActionEvent actionEvent) {
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText("Software project made by Reinout Eyckerman.");
        alert.showAndWait();
    }
}
