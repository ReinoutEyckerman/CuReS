package com.company;

import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by reinout on 10/25/16.
 */
public interface AudioMetadata{
    Map<String,String> getMetadata();
    boolean setFile(File audioFile);
    void setMetadata(FieldKey key, String value);
}
