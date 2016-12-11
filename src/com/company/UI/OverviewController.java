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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

/**
 * Created by reinout on 11/19/16.
 */
public class OverviewController {

    @FXML
    public ListView<Pane> listView;
    public List<CueGridController> controllers=new ArrayList<>();
    private ObservableList<AlbumTags> albumView;
    @FXML
    private VBox AlbumVBox;
    private MainApp mainApp;

    /**
     * The constructor.
     * The constructor is called before the initialize() method.
     */
    public OverviewController() {
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

    }
    private DataModel model;
    public void initModel(DataModel model){
        if (this.model != null) {
            throw new IllegalStateException("Model can only be initialized once");
        }
        this.model=model;
        model.getTrackList().addListener(new ListChangeListener<AlbumTags>() {
            @Override
            public void onChanged(Change<? extends AlbumTags> change) {
                while(change.next())
                    if (change.wasUpdated()) {
                        for (AlbumTags album : change.getList()) {
                            updateAlbum(album);
                        }
                    } else {
                        for (AlbumTags addedAlbum : change.getAddedSubList()) {
                            addAlbum(addedAlbum);
                        }
                    }
            }
        });
    }


    private void updateAlbum(AlbumTags album){
        FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("CueGrid.fxml"));
        try {
            Pane p=paneLoader.load();
            CueGridController controller=paneLoader.getController();
            //controller.artist.setText();
            listView.getItems().add(p);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void addAlbum(AlbumTags album){
        FXMLLoader paneLoader = new FXMLLoader(getClass().getResource("CueGrid.fxml"));
        try {
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
