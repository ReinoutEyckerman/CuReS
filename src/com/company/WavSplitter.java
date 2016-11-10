package com.company;

import javax.sound.sampled.*;
import java.io.*;

/**
 * Created by reinout on 11/9/16.
 */
public class WavSplitter {
    public void Split(){
        try {

            File file=new File("/home/reinout/repos/CuReS/beware.wav");
            File out=new File("/home/reinout/repos/CuReS/bewaresplit.wav");
            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFileFormat format= AudioSystem.getAudioFileFormat(file);
            float samplerate=format.getFormat().getSampleRate();
            int samplesize=format.getFormat().getSampleSizeInBits()/8;
            int channels=format.getFormat().getChannels();
            System.out.println("Samplerate: " +samplerate+", Samplesize: "+samplesize);
            System.out.println(samplesize*samplerate*30);
            float seconds=10.5f;
            stream.skip((long)(samplesize*samplerate*seconds*channels));
            AudioSystem.write(stream, AudioFileFormat.Type.WAVE,out);

        }catch(Exception e){

        }
    }
}
