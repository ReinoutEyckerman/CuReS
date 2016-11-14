package com.company;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.commons.io.FilenameUtils;
import org.jflac.PCMProcessor;
import org.jflac.FLACDecoder;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;
import org.jflac.util.WavWriter;

/**
 * Created by reinout on 11/9/16.
 */

public class FlacCodec implements PCMProcessor {
    private WavWriter wav;

    public void encode(){

    }
    /**
     * Decode a FLAC file to a WAV file.
     * @param inFileName    The input FLAC file name
     * @throws IOException  Thrown if error reading or writing files
     */
    public void decode(String inFileName) throws IOException {
        String tmpFolder=System.getProperty("user.dir")+"/tmp";
        String tmpOut=FilenameUtils.removeExtension(FilenameUtils.getName(inFileName))+".wav";
        FileInputStream is = null;
        OutputStream os=null;
        try {
            is = new FileInputStream(inFileName);
           // os = new ByteArrayOutputStream();
            os=new FileOutputStream(tmpFolder+"/"+tmpOut);
            wav = new WavWriter(os);

            WavSplitter splitter = new WavSplitter();
            FLACDecoder decoder = new FLACDecoder(is);
            decoder.addPCMProcessor(this);
            decoder.decode();
          //  splitter.Split(os, outFileName);
        }catch(Exception e)
        {
            e.printStackTrace();
        } finally {
            if (is != null) {
                is.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * Process the StreamInfo block.
     * @param info the StreamInfo block
     * @see org.jflac.PCMProcessor#processStreamInfo(org.jflac.metadata.StreamInfo)
     */
    public void processStreamInfo(StreamInfo info) {
        try {
            wav.writeHeader(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the decoded PCM bytes.
     * @param pcm The decoded PCM data
     * @see org.jflac.PCMProcessor#processPCM(org.jflac.util.ByteSpace)
     */
    public void processPCM(ByteData pcm) {
        try {
            wav.writePCM(pcm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

