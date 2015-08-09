package com.armor.tap_n_tour;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaRecorder;
import android.media.MediaRecorder.OnInfoListener;
import android.os.Bundle;
import android.os.Environment;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

public class TTRecordAudio extends Activity {

	ToggleButton tButton;
	public String s1;
	public String s2;
	TextView StateofToggleButton;
	public int count = 0;
	private MediaRecorder mRecorder = null;
	private static String mFileName = null;
	SQLiteDatabase db;
	Cursor cur;
	ListView lv;
	ArrayAdapter<String> aa;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_record_audio);

		tButton = (ToggleButton) findViewById(R.id.record);
		StateofToggleButton = (TextView) findViewById(R.id.state);
		StateofToggleButton.setText("Start Recording");
		db = openOrCreateDatabase("tapntour.db", Context.MODE_PRIVATE, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);

		String str1 = "Create table if not exists audios(filename varchar(200),time integer)";
		db.execSQL(str1);
		tButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				if (isChecked) {
					StateofToggleButton.setText("Stop Recording");
					startRecording();
				} else {
					StateofToggleButton.setText("Start Recording");
					stopRecording();
				}
			}
		});
	}

	private void startRecording() {
		mRecorder = new MediaRecorder();
		Date date = new Date();
		SimpleDateFormat d = new SimpleDateFormat("yyyyMMddhhmmss");
		String names = d.format(date);
		System.out.print(names);
mRecorder.setOnInfoListener(new OnInfoListener() {
			
			@Override
			public void onInfo(MediaRecorder mr, int what, int extra) {
			if(what==MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED){
				stopRecording();
			}
				
			}
		});
		//mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
		mFileName = "/sdcard/aud_" + names + ".mp4";
		mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
		mRecorder.setOutputFile(mFileName);
		mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mRecorder.setMaxDuration(1200000);
		s1 = "aud_" + names + ".mp4";
		s2 = names;
		try {
			mRecorder.prepare();
		} catch (IOException e) {
			
		}

		mRecorder.start();
	}

	private void stopRecording() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
		insertintodb();
	}

	private void insertintodb() {
		if (!s1.equals("") && !s2.equals("")) {
			ContentValues cv1 = new ContentValues();
			cv1.put("filename", s1);
			cv1.put("time", s2);

			db.insert("audios", null, cv1);

		}
		
	}
}