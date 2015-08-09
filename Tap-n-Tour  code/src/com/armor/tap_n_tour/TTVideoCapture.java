package com.armor.tap_n_tour;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class TTVideoCapture extends Activity implements SurfaceHolder.Callback {

	public String s;
	private SurfaceHolder surfaceHolder;
	private SurfaceView surfaceView;
	public MediaRecorder mrec = new MediaRecorder();
	private static String fileName = null;
	ToggleButton tButton;
	TextView StateofToggleButton;
	// private Button stopRecording = null;
	File video;
	SQLiteDatabase db;
	Cursor cur;
	public android.hardware.Camera mCamera;
	String s1,s2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_video_capture);
		// Log.i(null , "Video starting");

		tButton = (ToggleButton) findViewById(R.id.record);
		StateofToggleButton = (TextView) findViewById(R.id.state);
		db = openOrCreateDatabase("tapntour.db", Context.MODE_MULTI_PROCESS, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);

		String str1 = "Create table if not exists videos(filename varchar(200),time integer)";
		db.execSQL(str1);
		StateofToggleButton.setText("Start Recording");

		// releaseCamera();
		mCamera = android.hardware.Camera.open();
		surfaceView = (SurfaceView) findViewById(R.id.surface_camera);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					StateofToggleButton.setText("Stop Recording");
					try {
						startRecording();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					StateofToggleButton.setText("Start Recording");
					stopRecording();
				}
			}
		});

	}

	@TargetApi(Build.VERSION_CODES.FROYO)
	@SuppressLint("InlinedApi")
	protected void startRecording() throws IOException {
		Date date = new Date();
		SimpleDateFormat am = new SimpleDateFormat("yyyyMMddhhmm");
		String names = am.format(date);
		System.out.print("enters in");
		//fileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		fileName = "/sdcard/video_" + names + ".mp4";
		s1 = "video_" + names + ".mp4";
		s2 = names;
		mrec = new MediaRecorder(); // Works well
		mCamera.unlock();

		mrec.setCamera(mCamera);
		mrec.setOnInfoListener(new OnInfoListener() {
			
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
			if(what==MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
				stopRecording();
			}
				
			}
		});

		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setVideoSource(MediaRecorder.VideoSource.CAMERA);
		mrec.setAudioSource(MediaRecorder.AudioSource.MIC);
		mrec.setOrientationHint(90);
		mrec.setMaxDuration(30000);
		mrec.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
		mrec.setPreviewDisplay(surfaceHolder.getSurface());
		mrec.setOutputFile(fileName);
		System.out.println(fileName);
		System.out.println("ends strat rec");
try{
		mrec.prepare();
}
catch(IOException e)
{
	mrec.reset();
	mrec.release();
	mrec=null;
return ;
}

		mrec.start();
	}

	protected void stopRecording() {
		mrec.stop();
		mrec.release();
		insertintodb();
	}

	@SuppressWarnings("unused")
	private void releaseMediaRecorder() {
		if (mrec != null) {
			mrec.reset(); // clear recorder configuration
			mrec.release(); // release the recorder object
			mrec = null;
			mCamera.lock(); // lock camera for later use
		}
	}

	@SuppressWarnings("unused")
	private void releaseCamera() {
		if (mCamera != null) {
			mCamera.release(); // release the camera for other applications
			mCamera = null;
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera != null) {
			Parameters params = mCamera.getParameters();
			mCamera.setParameters(params);
		} else {
			Toast.makeText(getApplicationContext(), "Camera not available!",
					Toast.LENGTH_LONG).show();
			finish();
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		mCamera.stopPreview();
		mCamera.release();

	}

	private void insertintodb() {
		if (!s1.equals("") && !s2.equals("")) {
			ContentValues cv1 = new ContentValues();
			cv1.put("filename", s1);
			cv1.put("time", s2);

			db.insert("videos", null, cv1);
		}
	}

}
