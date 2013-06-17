package com.example.videocaptureandtransfer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;


import android.os.Environment;
import android.util.Log;

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
	    try 
	    {      
	        client = new Socket( "82.236.30.125",40000 ); 
	    }
	    catch (IOException e) 
	    {
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

	        byte[] mybytearray = new byte[(int) file.length()]; 
	        fileInputStream = new FileInputStream(file);
	        bufferedInputStream = new BufferedInputStream(fileInputStream);

	        /**reads the file */
	        bufferedInputStream.read(mybytearray, 0, mybytearray.length); 
	        outputStream = client.getOutputStream();

	         /** writes file to the output stream byte by byte */
	        outputStream.write(mybytearray, 0, mybytearray.length); 
	        outputStream.flush();
	        bufferedInputStream.close();
	        outputStream.close();

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
