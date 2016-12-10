package com.company;

import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by reinout on 11/11/16.
 */
public class Processor implements Runnable{
    private File cueFile;
    private ProgressBar progressBar;
    public Processor(File cueFile, ProgressBar progressBar){
        this.cueFile=cueFile;
        this.progressBar=progressBar;
    }
    public void run(){
        CueSplitter cueSplitter=new CueSplitter(cueFile);
        FlacCodec codec = new FlacCodec();
        WavSplitter wavSplitter=new WavSplitter();
        AlbumTags metadata;
        try {
            metadata=cueSplitter.parseFile();
            progressBar.setProgress(0.1);
            codec.decode(metadata.FileLocation);
            progressBar.setProgress(0.5);
            wavSplitter.Split(metadata);
            progressBar.setProgress(0.6);
            for(int i=0; i<metadata.tracks.size(); i++){
                codec.encode(System.getProperty("user.dir")+"/tmp/"+metadata.tracks.get(i).Title+".wav");
                progressBar.setProgress(0.6+((float)i/(float)metadata.tracks.size())*0.4);
            }
            progressBar.setProgress(1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
