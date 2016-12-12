package com.company.UI.model;

import com.company.AlbumTags;
import com.company.CueSplitter;
import com.company.Processor;
import com.company.TrackTags;
import com.company.UI.CueGridController;
import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.Pane;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DataModel {

    //private final ObservableList<TrackTags> fileList = FXCollections.observableArrayList(track -> new Observable[] {track.title(), track.performer()});
    public ObservableList<AlbumTags> fileList = FXCollections.observableArrayList();

    public ObservableList<AlbumTags> getTrackList() {return fileList;}

    public ObservableList<File> cueFiles=FXCollections.observableArrayList();
    private File outPath=null;

    public void setOutPath(File outPath){
        this.outPath=outPath;
    }
    public void convertCue(List<CueGridController> controllers){
        for(int i=0; i<cueFiles.size(); i++){
            Processor p= new Processor(cueFiles.get(i), controllers.get(i).progress);
            p.SetOutpath(outPath);
            Thread t=new Thread(p);
            t.start();
        }
        //TODO How to put it after thread with some non blocking join?
        Alert alert=new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Finished");
        alert.setContentText("Your audio files have been split.");
        alert.showAndWait();
    }
    public void addCue(File cueFile){
        cueFiles.add(cueFile);
        CueSplitter cueSplitter=new CueSplitter(cueFile);
        AlbumTags metadata=cueSplitter.parseFile();
        fileList.add(metadata);
    }
    public void removeEntry(int i){
        fileList.remove(i);
        cueFiles.remove(i);
    }
}