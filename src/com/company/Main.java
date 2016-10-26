package com.company;

import java.io.File;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
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
