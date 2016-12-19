package com.company.UI;

import com.company.UI.menu.MenuController;
import com.company.UI.model.DataModel;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Created by reinout on 11/19/16.
 */
public class MainApp extends Application{
    public static void main(String[] args) {
        launch(args);
    }


    private Stage primaryStage;

    /**
     * Constructor
     */
    public MainApp() {
    }

    @Override
    public void start(Stage primaryStage) {
        new File(System.getProperty("user.dir")+"/tmp").mkdirs();
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("CuReS");
        this.primaryStage.setMinHeight(400);
        this.primaryStage.setWidth(600);
        this.primaryStage.setResizable(false);
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            /*
            Thread t=new Thread(() -> {
                MusicBrainzAPI api=new MusicBrainzAPI();
                api.GetMetadata();
            });
            t.start();*/
            // Load root layout from fxml file.
            BorderPane root= new BorderPane();
            FXMLLoader menuloader = new FXMLLoader(getClass().getResource("menu/menu.fxml"));
            root.setTop(menuloader.load());

            MenuController menuController=menuloader.getController();

            Image image=new Image(getClass().getResourceAsStream("menu/icons/file.png"), 25,25, false,false);
            menuController.openFile.setGraphic(new ImageView(image));
            image=new Image(getClass().getResourceAsStream("menu/icons/delete.png"), 25,25, false,false);
            menuController.removeFile.setGraphic(new ImageView(image));
            image=new Image(getClass().getResourceAsStream("menu/icons/start.png"), 25,25, false,false);
            menuController.start.setGraphic(new ImageView(image));
            image=new Image(getClass().getResourceAsStream("menu/icons/folder.png"), 25,25, false,false);
            menuController.outpath.setGraphic(new ImageView(image));
            image=new Image(getClass().getResourceAsStream("menu/icons/save.png"), 25,25, false,false);
            menuController.saveButton.setGraphic(new ImageView(image));

            FXMLLoader rootloader=new FXMLLoader(getClass().getResource("Overview.fxml"));
            root.setCenter(rootloader.load());
            OverviewController overviewController=rootloader.getController();
            menuController.addListView(overviewController.listView, overviewController.controllers, overviewController.cuesheet);
            DataModel model=new DataModel();
            overviewController.initModel(model);
            menuController.initModel(model);

            // Show the scene containing the root layout.
            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
