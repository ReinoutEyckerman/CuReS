package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        /*try {
            FlacCodec decoder = new FlacCodec();
            decoder.decode("/home/reinout/repos/CuReS/beware.flac", "/home/reinout/repos/CuReS/beware.wav");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        CueSplitter splitter=new CueSplitter();
        try {
            splitter.parseFile();
        }catch(Exception e){
            System.err.println("Could not parse file. "+ e);
            e.printStackTrace();
        }
        // write your code here
        File file=new File("/home/reinout/repos/CuReS/sooth.mp3");
        AudioMetadata meta=new IDv3();
        meta.setFile(file);
        Map<String,String> map=meta.getMetadata();
        for (Map.Entry<String,String> entry : map.entrySet()) {
            System.out.println(entry.getKey()+", " +entry.getValue());

        }
    }
}
