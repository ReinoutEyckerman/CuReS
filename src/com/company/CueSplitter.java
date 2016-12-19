package com.company;


import javafx.application.Platform;
import javafx.scene.control.Alert;
import org.apache.commons.io.FilenameUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by reinout on 11/9/16.
 */
public class CueSplitter {
    private AlbumTags metadata=new AlbumTags();
    private File cueFile=null;
    private List<String> warnings=new ArrayList<>();
    private List<String> errors=new ArrayList<>();
    public CueSplitter(File cueFile){
        this.cueFile=cueFile;
    }
    public AlbumTags parseFile(){
        try {
            List<String> records = new ArrayList<>();
            String line=null;
            BufferedReader bufferedReader = null;
            bufferedReader = new BufferedReader(new FileReader(cueFile));
            while ((line = bufferedReader.readLine()) != null)
            {
                records.add(line);
            }
            ProcessCue(records);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(errors.size()!=0||warnings.size()!=0)
            Platform.runLater(() -> {
                Alert alert=new Alert(Alert.AlertType.WARNING);
                alert.setTitle(warnings.size()+" warnings and "+errors.size()+" errors");
                String out="Warnings: \n";
                for (String s:warnings) {
                    out+=s+"\n";
                }
                out+="Errors: \n";
                for (String s:errors) {
                    out+=s+"\n";
                }
                alert.setContentText(out);
                alert.showAndWait();
            });
        return metadata;
    }
    private void ProcessCue(List<String> records){
        for(int i=0; i<records.size(); i++) {
            String s=records.get(i);
            String cmd = s.split(" ")[0];
            if (cmd == null)
            {
                addWarning(i,"Empty line detected.");
                continue;
            }
            switch (cmd.toUpperCase()) {
                case "FILE":
                    Pattern p= Pattern.compile("\"(.+)\"");
                    Matcher matcher=p.matcher(s);
                    if(!matcher.find()) {
                        addError(i, "No file defined. Program will not run until it is defined.");
                        return;
                    }
                    String location=matcher.group(1);
                    metadata.FileLocation.add(FilenameUtils.getFullPath(cueFile.getAbsolutePath())+location);
                    i++;
                    int x = fetchTrackMetadata(i, records);
                    if(x==-1)
                        return;
                    else i+=x;
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
                         default:
                            addError(i,"Unknown REM tag.");
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
                    addError(i,"Unknown metadata " + s.split(" ")[1]);
                    break;
            }
        }
        metadata.clean();
    }
    private int fetchTrackMetadata(int state, List<String> records) {
        String s = records.get(state).trim();
        TrackTags tags = new TrackTags();
        int count = 1;
        boolean newFile = false;
        String z = s.split(" ")[0].toUpperCase();
        if (!z.equals("TRACK"))
        {
            addError(state, "Unknown tag, should be TRACK. Program will not run until it is repaired.");
            return -1;
        }

        while(!newFile&&count + state < records.size()){
            s=records.get(state+count).trim();
            switch (s.split(" ")[0].toUpperCase()) {
                case "INDEX":
                    if(s.split(" ")[1].equals("01"))
                        tags.setCutPoint(s.split(" ")[2]);
                    else addError(state+count,"Error in index." );
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
                    addError(state+count,"Unknown metadata " + s.split(" ")[1]);
                    break;
            }
            count++;
        }
        return count;
    }
    private void addError(int line, String s){
       errors.add("Line "+line+": "+s);
    }
    private void addWarning(int line, String s){
        warnings.add("Line "+ line+": "+s);
    }
}
