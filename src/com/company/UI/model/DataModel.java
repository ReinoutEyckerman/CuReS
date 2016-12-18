package com.company.UI.model;

import com.company.AlbumTags;
import com.company.CueSplitter;
import com.company.Processor;
import com.company.UI.CueGridController;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DataModel {

    //private final ObservableList<TrackTags> fileList = FXCollections.observableArrayList(track -> new Observable[] {track.title(), track.performer()});
    private ObservableList<AlbumTags> fileList = FXCollections.observableArrayList();

    public ObservableList<AlbumTags> getTrackList() {return fileList;}

    public ObservableList<File> cueFiles=FXCollections.observableArrayList();
    private File outPath=null;
    private final ExecutorService pool= Executors.newFixedThreadPool(4);


    public void setOutPath(File outPath){
        this.outPath=outPath;
    }
    public void convertCue(List<CueGridController> controllers){
        if(cueFiles.size()==0)
        {
            Platform.runLater(() -> {
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setContentText("Please select an input file.");
                alert.showAndWait();
            });
            return;
        }
        for(int i=0; i<cueFiles.size(); i++){
            Processor p= new Processor(cueFiles.get(i), controllers.get(i).progress);
            p.setOutpath(outPath);
            Thread t=new Thread(() -> {
                int x =p.run();
                if (x !=0)
                    return;
                Platform.runLater(() -> {
                    Alert alert=new Alert(Alert.AlertType.INFORMATION);
                    alert.setContentText("Your audio files have been split.");
                    alert.showAndWait();
                });
            });
            pool.execute(t);
        }
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