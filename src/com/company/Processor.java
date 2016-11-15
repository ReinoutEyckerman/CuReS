package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by reinout on 11/11/16.
 */
public class Processor {
    public void Process(String cuePath){
        CueSplitter cueSplitter=new CueSplitter(cuePath);
        FlacCodec codec = new FlacCodec();
        WavSplitter wavSplitter=new WavSplitter();
        AlbumTags metadata;
        try {
            metadata=cueSplitter.parseFile();
            codec.decode(metadata.FileLocation);
            wavSplitter.Split(metadata);
            for(int i=0; i<metadata.tracks.size(); i++){
                codec.encode(System.getProperty("user.dir")+"/tmp/"+metadata.tracks.get(i).Title+".wav");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
