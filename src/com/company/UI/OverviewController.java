package com.company.UI;

import com.company.AlbumTags;
import com.company.TrackTags;
import com.company.UI.model.DataModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * Created by reinout on 11/19/16.
 */
public class OverviewController {

    @FXML
    public ListView<Pane> listView;
    public List<CueGridController> controllers=new ArrayList<>();
    public Tab cueRepairTab;
    public TextArea cuesheet;


    private DataModel model;
    public void initModel(DataModel model){
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model=model;
        model.cueFiles.addListener(new ListChangeListener<File>() {
            @Override
            public void onChanged(Change<? extends File> c) {
                while(c.next()){
                    if(!c.wasUpdated()){
                        for(File f:c.getAddedSubList()){
                            try {
                                Scanner s = new Scanner(f);
                                while(s.hasNext()){
                                    if (s.hasNextInt()) { // check if next token is an int
                                        cuesheet.appendText(s.nextInt() + " "); // display the found integer
                                    } else {
                                        cuesheet.appendText(s.nextLine() + '\n'); // else read the next token
                                    }
                                }
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        });
        model.getTrackList().addListener(new ListChangeListener<AlbumTags>() {
            @Override
            public void onChanged(Change<? extends AlbumTags> change) {
                while(change.next())
                    if (!change.wasUpdated()) {
                        for (AlbumTags addedAlbum : change.getAddedSubList()) {
                            addAlbum(addedAlbum);
                        }
                    }
            }
        });
        cueRepairTab.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if(newValue){
//TODO Vragen hoe te verbinden met menucontroller
                }
            }
        });
    }

    private void addAlbum(AlbumTags album){
        FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("CueGrid.fxml"));
        try {
            //cuesheet.setText();
            Pane p=paneLoader.load();
            CueGridController controller=paneLoader.getController();
            controller.artist.setText(album.Performer);
            controller.album.setText(album.Title);
            for(int i=0; i<album.tracks.size(); i++){
                controller.trackList.getItems().add(album.tracks.get(i).Title);
            }
            listView.getItems().add(p);
            controllers.add(controller);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
