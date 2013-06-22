package com.example.videocaptureandtransfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


import android.os.Environment;
import android.util.Log;

//CVM03_V01 - to transfer the video
public class FileTransfer implements Runnable {
	public Socket client = null;
	public FileInputStream fileInputStream = null;
	public BufferedInputStream bufferedInputStream = null;
	public OutputStream outputStream = null;
	String filePath = Environment.getExternalStorageDirectory().getPath() + "/xreal.mp4" ;
	String TAG = "FILE TRANSFER API";
	public void Connect()
	{   
		Log.i(TAG, "Connecting");
	    try {
			client = new Socket( "82.236.30.125",40000 );
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	    Log.i(TAG, "Connected");
	}
	public void SendFile( String path ) 
	{
		Log.i(TAG, "Sending File");
	    try
	    {
	        File file = new File(path);
	        Log.i(TAG, "1.0");
	        byte[] mybytearray = new byte[(int) file.length()]; 
	        Log.i(TAG, "1.1");
	        fileInputStream = new FileInputStream(file);
	        bufferedInputStream = new BufferedInputStream(fileInputStream);
	        Log.i(TAG, "1.2");
	        /**reads the file */
	        bufferedInputStream.read(mybytearray, 0, mybytearray.length); 
	        outputStream = client.getOutputStream();
	        Log.i(TAG, "2");
	         /** writes file to the output stream byte by byte */
	        outputStream.write(mybytearray, 0, mybytearray.length); 
	        Log.i(TAG, "3");
	        outputStream.flush();
	        bufferedInputStream.close();
	        outputStream.close();
	        Log.i(TAG, "4");

	    }
	    catch(IOException e)
	    {
	        e.printStackTrace();
	    }
	    Log.i(TAG, "File Sent");
	}
	public void Disconnect()
	{
		Log.i(TAG, "Disconnecting");
	    try 
	    {
	        client.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	    Log.i(TAG, "Disconnecting");
	}
	@Override
	public void run( ) {
		// TODO Auto-generated method stub
		Connect();
		SendFile( filePath );
		Disconnect();
	}
}
