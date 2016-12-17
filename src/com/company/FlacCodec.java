package com.company;

import net.sourceforge.javaflacencoder.FLAC_FileEncoder;
import org.apache.commons.io.FilenameUtils;
import org.jflac.FLACDecoder;
import org.jflac.PCMProcessor;
import org.jflac.metadata.StreamInfo;
import org.jflac.util.ByteData;
import org.jflac.util.WavWriter;

import java.io.*;

/**
 * Created by reinout on 11/9/16.
 */

public class FlacCodec implements PCMProcessor {
    private WavWriter wavWriter;

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
            os=new FileOutputStream(tmpFolder+"/"+tmpOut);
            wavWriter = new WavWriter(os);
            FLACDecoder decoder = new FLACDecoder(is);
            decoder.addPCMProcessor(this);
            decoder.decode();
        }catch(Exception e)
        {
            e.printStackTrace();
        } finally {
            if (is != null)
                is.close();
            if (os != null)
                os.close();
        }
    }
    public void encode(String inFileName, String outFileName) throws IOException{
        File input=new File(inFileName);
        File output=new File(outFileName);
        try{
            FLAC_FileEncoder encoder=new FLAC_FileEncoder();
            encoder.encode(input,output);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Process the StreamInfo block.
     * @param info the StreamInfo block
     * @see org.jflac.PCMProcessor#processStreamInfo(org.jflac.metadata.StreamInfo)
     */
    public void processStreamInfo(StreamInfo info) {
        try {
            wavWriter.writeHeader(info);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Process the decoded PCM bytes.
     * @param pcm The decoded PCM data
     */
    public void processPCM(ByteData pcm) {
        try {
            wavWriter.writePCM(pcm);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

