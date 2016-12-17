package com.company;

/**
 * Created by reinout on 10/25/16.
 */
public interface AudioMetadata{
    void writeTagToFile(String fileLocation, AlbumTags metadata, int tracknr);
}
