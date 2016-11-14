package com.company;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by reinout on 11/11/16.
 */
public class Processor {
    public void Process(String cuePath){
        CueSplitter cueSplitter=new CueSplitter(cuePath);
        FlacCodec decoder = new FlacCodec();
        WavSplitter wavSplitter=new WavSplitter();
        AlbumTags metadata;
        try {
            metadata=cueSplitter.parseFile();
            decoder.decode(metadata.FileLocation);
            wavSplitter.Split(metadata);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
