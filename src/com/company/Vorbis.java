package com.company;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;


/**
 * Created by reinout on 10/25/16.
 */
public class Vorbis implements AudioMetadata {

    @Override
    public void writeTagToFile(String fileLocation, AlbumTags metadata, int tracknr){
        try {
            TrackTags track=metadata.tracks.get(tracknr);
            AudioFile f = AudioFileIO.read(new File(fileLocation));
            Tag tag = f.getTag();
            tag.setField(FieldKey.ARTIST, track.Performer);
            tag.setField(FieldKey.TITLE, track.Title );
            tag.setField(FieldKey.ALBUM, metadata.Title);
            tag.setField(FieldKey.GENRE, metadata.Genre);
            tag.setField(FieldKey.DISC_NO,metadata.DiscID);
            tag.setField(FieldKey.YEAR,metadata.Date);
            tag.setField(FieldKey.COMMENT,metadata.Comment);
            tag.setField(FieldKey.ISRC,track.ISRC);
            f.commit();
        }catch(Exception e){
        }
    }
}
