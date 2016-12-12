package com.company;

import javafx.scene.control.ProgressBar;
import org.apache.commons.io.FilenameUtils;
import javax.sound.sampled.*;
import java.io.*;

/**
 * Created by reinout on 11/9/16.
 */
public class WavSplitter {
    public void Split(AlbumTags metadata, ProgressBar progressBar){
        try {
            File in=new File(System.getProperty("user.dir")+"/tmp/"+FilenameUtils.removeExtension(FilenameUtils.getName(metadata.FileLocation))+".wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(in);
            AudioFileFormat format= AudioSystem.getAudioFileFormat(in);
            float samplerate=format.getFormat().getSampleRate();
            float framerate=format.getFormat().getFrameRate();
            int samplesize=format.getFormat().getSampleSizeInBits()/8;
            System.out.println("Samplerate: " +samplerate+", Samplesize: "+samplesize);
            System.out.println(samplesize*samplerate*30);
            AudioInputStream trackstream=null;
            long prevTrackLength=0;
            for(int i=0; i<metadata.tracks.size(); i++) {
                TrackTags currentTrack=metadata.tracks.get(i);
                File out = new File(System.getProperty("user.dir")+"/tmp/"+currentTrack.Title+".wav");

                float duration=0;
                if(i==metadata.tracks.size()-1)
                    duration=stream.available();
                else duration=metadata.tracks.get(i+1).CutPoint-metadata.tracks.get(i).CutPoint;

                long bytesToRead=(long)(framerate * duration);

                stream.skip(prevTrackLength);
                prevTrackLength+=duration;
                trackstream=new AudioInputStream(stream, format.getFormat(),bytesToRead);
                AudioSystem.write(trackstream, AudioFileFormat.Type.WAVE,out);
                progressBar.setProgress(0.1+((float)i/(float)metadata.tracks.size())*0.5);
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
