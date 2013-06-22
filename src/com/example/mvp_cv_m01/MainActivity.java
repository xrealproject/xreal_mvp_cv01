package com.example.mvp_cv_m01;

import java.io.IOException;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.os.Environment;

import android.content.pm.ActivityInfo;

import android.view.Menu;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.example.mvp_cv_m01.FileTransfer;


public class MainActivity extends Activity implements
OnClickListener, SurfaceHolder.Callback
{

	MediaRecorder recorder;
	SurfaceHolder holder;
	boolean recording = false;
	String filePath  = Environment.getExternalStorageDirectory().getPath() + "/xreal.mp4";
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		recorder = new MediaRecorder();
		initRecorder();
		setContentView(R.layout.activity_main);
		
		SurfaceView cameraView = (SurfaceView) findViewById(R.id.videoSurface);
		holder=cameraView.getHolder();
		holder.addCallback(this);
		holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		
		cameraView.setClickable(true);
		cameraView.setOnClickListener(this);
		
	}

	private void initRecorder(){
		recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
		recorder.setVideoSource(MediaRecorder.VideoSource.DEFAULT);
		CamcorderProfile cpHigh = CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH);
		recorder.setProfile(cpHigh);
		recorder.setOutputFile(filePath);
		recorder.setMaxDuration(5000); // 50 seconds
		recorder.setMaxFileSize(5000000); // Approximately 5MB
		
	}
	
	private void prepareRecorder(){
		recorder.setPreviewDisplay(holder.getSurface());
		
		try{
			recorder.prepare();
		}
		catch(IllegalStateException e){
			e.printStackTrace();
			finish();
		}
		catch(IOException e){
			e.printStackTrace();
			finish();
		}
	}
	
	
	public void surfaceCreated(SurfaceHolder holder)
	{
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){
		
	}
	
	public void surfaceDestroyed(SurfaceHolder holder){
		if(recording){
			recorder.stop();
			recording = false;
		}
		recorder.release();
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.main, menu);
		//return true;
		menu.add(0,0,0,"Settings");
		menu.add(0, 1, 0, "Send Video");
		return super.onCreateOptionsMenu(menu);
	}

	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(recording){
			recorder.stop();
			recording = false;
			(new Thread(new FileTransfer())).start();
			//Let's intiRecorder so we can record again
			initRecorder();
		}
		else{
			prepareRecorder();
			recording = true;
			recorder.start();
		}
	}

}
