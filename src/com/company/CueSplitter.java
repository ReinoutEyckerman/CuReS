package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by reinout on 11/9/16.
 */
public class CueSplitter {
    AlbumTags metadata=new AlbumTags();
    public void parseFile()throws Exception{
        List<String> records = new ArrayList<String>();
        String line=null;
        BufferedReader bufferedReader = new BufferedReader(new FileReader("/home/reinout/repos/CD1/postal.cue"));
        while ((line = bufferedReader.readLine()) != null)
        {
            records.add(line);
        }
        ProcessCue(records);
    }
    private void ProcessCue(List<String> records){
        for(int i=0; i<records.size(); i++) {
            String s=records.get(i);
            String cmd = s.split(" ")[0];
            if (cmd == null)
            {
                System.err.println("Empty string detected");
                continue;
            }
            switch (cmd.toUpperCase()) {
                case "FILE":
                    String location=s.split(" ",2)[1];
                    location=location.substring(0,location.lastIndexOf(" "));
                    metadata.FileLocation=location;
                    i++;
                    i += fetchTrackMetadata(i, records);
                    break;
                case "REM":
                    switch(s.split(" ", 3)[1].toUpperCase()){
                        case "GENRE":
                            metadata.Genre=s.split(" ",3)[2];
                            break;
                        case "DISCID":
                            metadata.DiscID=s.split(" ",3)[2];
                            break;
                        case "DATE":
                            metadata.Date=s.split(" ",3)[2];
                            break;
                        case "COMMENT":
                            metadata.Comment=s.split(" ",3)[2];
                            break;
                    }
                    break;
                case "CATALOG":
                    metadata.Catalog=s.split(" ",2)[1];
                    break;
                case "PERFORMER":
                    metadata.Performer=s.split(" ",2)[1];
                    break;
                case "TITLE":
                    metadata.Title=s.split(" ",2)[1];
                    break;
                case "SONGWRITER":
                    metadata.Songwriter=s.split(" ", 2)[1];
                    break;
                default:
                    System.err.println("Unknown metadata " + s.split(" ")[1]);
                    break;
            }
        }
        metadata.Clean();
    }
    private int fetchTrackMetadata(int state, List<String> records) {
        String s = records.get(state).trim();
        TrackTags tags=new TrackTags();
        int count = 0;
        boolean newFile=false;
        String z=s.split(" ")[0].toUpperCase();
        if(z=="TRACK")
            throw new IllegalArgumentException(" AYY LMAO WAT THE FOCK YOU SAID ABOUT ME YOU LITTLE SHIT");

        while(!newFile&&count + state < records.size()){
            count++;
            s=records.get(state+count).trim();
            switch (s.split(" ")[0].toUpperCase()) {
                case "INDEX":
                    if(s.split(" ")[1]=="01")
                        tags.CutPoint=s.split(" ")[2];
                    else System.err.println("Error in index at line "+ count+state);
                    break;
                case "TITLE":
                    tags.Title=s.split(" ",2)[1];
                    break;
                case "PERFORMER":
                    tags.Performer=s.split(" ",2)[1];
                    break;
                case "SONGWRITER":
                    tags.Songwriter=s.split(" ", 2)[1];
                    break;
                case "FILE":
                    newFile=true;
                    break;
                case "TRACK":
                    metadata.tracks.add(tags);
                    count+=fetchTrackMetadata(state+count, records);
                    break;
                case "ISRC":
                    tags.ISRC=s.split(" ", 2)[1];
                default:
                    System.err.println("Unknown metadata " + s.split(" ")[1]);
                    break;
            }
        }
        return count;
    }
}
