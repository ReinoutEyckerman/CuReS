package com.company;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by reinout on 10/25/16.
 */
public class IDv3 implements AudioMetadata{

    Tag tag=null;
    @Override
    public Map<String, String> getMetadata() {
       if(tag!=null)
       {
           Map<String,String> metadata= new HashMap<>();
           metadata.put("Album", tag.getFirst(FieldKey.ALBUM));
           metadata.put("Artist", tag.getFirst(FieldKey.ARTIST));
           metadata.put("BPM", tag.getFirst(FieldKey.BPM));
           metadata.put("Comment", tag.getFirst(FieldKey.COMMENT));
           metadata.put("Composer", tag.getFirst(FieldKey.COMPOSER));
           metadata.put("Genre", tag.getFirst(FieldKey.GENRE));
           metadata.put("Title", tag.getFirst(FieldKey.TITLE));
           metadata.put("Year", tag.getFirst(FieldKey.ORIGINAL_YEAR));
           return metadata;
       }
        return null;
    }

    @Override
    public boolean setFile(File audioFile) {
        try{
            AudioFile f = AudioFileIO.read(audioFile);
            tag = f.getTag();
            AudioHeader header = f.getAudioHeader();
        }catch(Exception e){
           System.out.println("Exception " + e + "found at audio file loading");
            return false;
        }
        return true;
    }

    @Override
    public void setMetadata(FieldKey key, String value) {
        try {
            tag.setField(key, value);
        }catch(Exception e)
        {
            System.out.println("This is your local code telling you were too lazy to fix something so you just put it in a try-catch. FIX THIS SHIT YOU WORTHLESS PIECE OF CRAP." );
            System.out.println("Exiting...");
            System.exit(1);
        }
    }
}
