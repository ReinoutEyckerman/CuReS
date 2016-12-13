package com.company;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ProgressBar;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.SystemUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class Processor {
    private final File cuePath;
    private final ProgressBar progressBar;
    private File outpath=null;

    public Processor(File cuePath, ProgressBar progressBar) {
        this.cuePath = cuePath;
        this.progressBar = progressBar;
    }
    public void SetOutpath(File outpath){
        this.outpath=outpath;
    }

    public int run(){
        if(outpath==null) {
            Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert=new Alert(Alert.AlertType.WARNING);
                            alert.setContentText("Please select an output file.");
                            alert.showAndWait();
                        }
                    });
            return 1;
        }
        CueSplitter cueSplitter=new CueSplitter(cuePath);
        FlacCodec codec = new FlacCodec();
        WavSplitter wavSplitter=new WavSplitter();
        AlbumTags metadata;
        try {
            metadata=cueSplitter.parseFile();
            codec.decode(metadata.FileLocation);
            progressBar.setProgress(0.1);
            wavSplitter.Split(metadata, progressBar);
            progressBar.setProgress(0.6);
            Vorbis tagger=new Vorbis();
            File albumOutpath=new File(outpath.getAbsolutePath()+"/"+metadata.Title+"/");
            albumOutpath.mkdirs();
            for(int i=0; i<metadata.tracks.size(); i++){
                String tmppath=System.getProperty("user.dir")+"/tmp/"+metadata.tracks.get(i).Title;
                String path=albumOutpath+"/"+metadata.tracks.get(i).Title;
                codec.encode(tmppath+".wav", path+".flac");
                tagger.WriteTagToFile(path+".flac",metadata,i);
                progressBar.setProgress(0.6+((float)i/(float)metadata.tracks.size())*0.4);            
            }
            progressBar.setProgress(1);
            FileUtils.deleteDirectory(new File(System.getProperty("user.dir")+"/tmp/"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }
}
