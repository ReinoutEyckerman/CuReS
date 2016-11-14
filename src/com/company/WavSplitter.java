package com.company;

import org.apache.commons.io.FilenameUtils;
import sun.misc.IOUtils;
import sun.nio.ch.IOUtil;

import javax.sound.sampled.*;
import java.io.*;

/**
 * Created by reinout on 11/9/16.
 */
public class WavSplitter {
    public void Split(AlbumTags metadata){
        try {
            File in=new File(System.getProperty("user.dir")+"/tmp/"+FilenameUtils.removeExtension(FilenameUtils.getName(metadata.FileLocation))+".wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(in);
            AudioFileFormat format= AudioSystem.getAudioFileFormat(in);
            float samplerate=format.getFormat().getSampleRate();
            int samplesize=format.getFormat().getSampleSizeInBits()/8;
            int channels=format.getFormat().getChannels();
            System.out.println("Samplerate: " +samplerate+", Samplesize: "+samplesize);
            System.out.println(samplesize*samplerate*30);

            for(int i=0; i<metadata.tracks.size(); i++) {
                TrackTags currentTrack=metadata.tracks.get(i);
                File out = new File(FilenameUtils.getFullPath(metadata.FileLocation)+currentTrack.Title+".wav");

                int duration=0;
                if(i==metadata.tracks.size()-1)
                    duration=stream.available();
                else duration=metadata.tracks.get(i+1).CutPoint-metadata.tracks.get(i).CutPoint;

                int bytesToRead=(int)(samplesize * samplerate * duration * channels);
                int maxBufferSize=8*1024;
                if(bytesToRead>maxBufferSize)
                {
                    int numReads=bytesToRead/maxBufferSize;
                    int numRemainingRead=bytesToRead%maxBufferSize;
                    for(int j=0; j<numReads; j++){
                        writeOut(maxBufferSize, stream, out);
                    }
                    if(numRemainingRead>0){
                        writeOut(numRemainingRead,stream,out);
                    }

                }else{
                    writeOut(bytesToRead, stream,out);
                }
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }
    private void writeOut(int maxBufferSize, InputStream inStream,File out){
        AudioInputStream ioStream=null;
        try {
            byte[] buffer = new byte[maxBufferSize];
            int value = inStream.read(buffer);
            if (value != -1) {
                ioStream = AudioSystem.getAudioInputStream(new ByteArrayInputStream(buffer));
                AudioSystem.write(ioStream, AudioFileFormat.Type.WAVE, out);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
