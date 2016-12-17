package com.company;

//import org.apache.commons.io.FilenameUtils;

import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by reinout on 11/9/16.
 */
public class CueSplitter {
    AlbumTags metadata=new AlbumTags();
    private File cueFile=null;
    public CueSplitter(File cueFile){
        this.cueFile=cueFile;
    }
    public AlbumTags parseFile(){
        try {
            List<String> records = new ArrayList<String>();
            String line=null;
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(new FileReader(cueFile));
            while ((line = bufferedReader.readLine()) != null)
            {
                records.add(line);
            }
            ProcessCue(records);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return metadata;
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
                    Pattern p= Pattern.compile("\"(.+)\"");
                    Matcher matcher=p.matcher(s);
                    if(!matcher.find())
                        throw new IllegalArgumentException("No filename");
                    String location=matcher.group(1);
                    metadata.FileLocation=FilenameUtils.getFullPath(cueFile.getAbsolutePath())+location;
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
        int count = 1;
        boolean newFile=false;
        String z=s.split(" ")[0].toUpperCase();
        if(!z.equals("TRACK"))
            throw new IllegalArgumentException("Error in the cue file.");

        while(!newFile&&count + state < records.size()){
            s=records.get(state+count).trim();
            switch (s.split(" ")[0].toUpperCase()) {
                case "INDEX":
                    if(s.split(" ")[1].equals("01"))
                        tags.SetCutPoint(s.split(" ")[2]);
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
                    break;
                default:
                    System.err.println("Unknown metadata " + s.split(" ")[1]);
                    break;
            }
            count++;
        }
        return count;
    }
}
