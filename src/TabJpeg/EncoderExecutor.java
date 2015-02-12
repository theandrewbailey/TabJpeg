/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TabJpeg;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.CharArrayWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author alpha
 */
public class EncoderExecutor {
    private static EncoderExecutor singleton = new EncoderExecutor();
    private static ThreadPoolExecutor threadpool = new ThreadPoolExecutor(3, 4, 100000, TimeUnit.DAYS, new ArrayBlockingQueue<Runnable>(10));
    private volatile Boolean beingused = false;

    static{threadpool.prestartAllCoreThreads();}

    public static EncoderExecutor getEncoderExecutor(){
        return singleton;
    }
    public static void transferStream(InputStream in, OutputStream out) throws IOException{
        byte content[] = new byte[4096];
        int readCount = 0;
        int totalReadcount = 0;
        while(-1 != (readCount = in.read(content))){
            out.write(content,0,readCount);
            totalReadcount += readCount;
        }
        //System.out.println("Total readsize: "+totalReadcount);
    }
    public static byte[] toByteArray(InputStream input){
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream(10*1024*1024);
            transferStream(input, output);
            return output.toByteArray();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return new byte[0];
    }
    private class Streamreader implements Callable<byte[]>{
        InputStream input;
        OutputStream output;
        public Streamreader(InputStream in, OutputStream out){
            input=in;
            output=out;
        }
        public byte[] call() throws Exception {
            transferStream(input, output);
            input.close();
            output.close();
            return new byte[0];
        }
        
    }
    private EncoderExecutor(){}
    private byte[] lastrun;
    public void resetLastrun(){
        lastrun = null;
    }
    public byte[] encode(byte[] f, int sizex, int sizey, int quality, String chroma){
        synchronized (beingused){
            try{
                ByteArrayOutputStream output = new ByteArrayOutputStream(10*1024*1024);
                String command = "cjpeg -quality "+quality;
                command += chroma == "0x0" ? " -grayscale" : " -sample "+chroma;
                //command += " "+f.getAbsolutePath();
                
                Process encoder = Runtime.getRuntime().exec(command);
                encoder.getOutputStream().write(f);
                //transferStream(f, encoder.getOutputStream());
                //threadpool.submit(new Streamreader(f, encoder.getOutputStream()));
                Future<byte[]> fut = threadpool.submit(new Streamreader(encoder.getInputStream(), output));

                //System.out.println("running command: "+command);
                encoder.getOutputStream().close();
                int exitcode = encoder.waitFor();
                fut.get();
                if (exitcode == 0){
                    //System.out.println(exitcode);
                    lastrun=output.toByteArray();
                } else {
                    //System.out.println(exitcode);
                    System.out.println(new String(output.toByteArray()));
                }
                encoder.destroy();
                return getLastrun();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return new byte[0];
        }
    }

    /**
     * @return the lastrun
     */
    public byte[] getLastrun() {
        return lastrun;
    }
}
