package com.company;

import javafx.scene.control.ProgressBar;

import java.io.File;
import java.io.FileNotFoundException;

public class Processor implements Runnable{
    private final File cuePath;
    private final ProgressBar progressBar;
    private File outpath=null;
    public ErrorCode errorCode=ErrorCode.Success;

    public Processor(File cuePath, ProgressBar progressBar) {
        this.cuePath = cuePath;
        this.progressBar = progressBar;
    }
    public void SetOutpath(File outpath){
        this.outpath=outpath;
    }

    @Override
    public void run(){
        if(outpath==null) {
            errorCode=ErrorCode.NoOutpath;
            return;
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
            System.out.println(albumOutpath.getAbsolutePath());
            albumOutpath.mkdirs();
            for(int i=0; i<metadata.tracks.size(); i++){
                String tmppath=System.getProperty("user.dir")+"/tmp/"+metadata.tracks.get(i).Title;
                String path=outpath+"/"+metadata.tracks.get(i).Title;
                codec.encode(tmppath+".wav", path+".flac");
                tagger.WriteTagToFile(path+".flac",metadata,i);
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
