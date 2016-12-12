package com.company;

import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by reinout on 11/11/16.
 */
public class Processor implements Runnable{
    private final File cuePath;
    private final ProgressBar progressBar;

    public Processor(File cuePath, ProgressBar progressBar) {
        this.cuePath = cuePath;
        this.progressBar = progressBar;
    }

    @Override
    public void run(){
        CueSplitter cueSplitter=new CueSplitter(cuePath);
        FlacCodec codec = new FlacCodec();
        WavSplitter wavSplitter=new WavSplitter();
        AlbumTags metadata;
        try {
            metadata=cueSplitter.parseFile();
            codec.decode(metadata.FileLocation);
            wavSplitter.Split(metadata);
            progressBar.setProgress(0.6);
Vorbis tagger=new Vorbis();
            for(int i=0; i<metadata.tracks.size(); i++){
                String path=System.getProperty("user.dir")+"/tmp/"+metadata.tracks.get(i).Title;
                codec.encode(path+".wav");
                tagger.WriteTagToFile(path+".flac",metadata,i);
                progressBar.setProgress(0.6+((float)i/(float)metadata.tracks.size())*0.4);            
}
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
