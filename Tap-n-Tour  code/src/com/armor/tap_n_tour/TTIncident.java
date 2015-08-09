package com.armor.tap_n_tour;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Locale;
import com.armor.tap_n_tour.Base6;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Toast;

public class TTIncident extends Activity implements OnClickListener {

	ImageButton mic, cam, vid;
	Button report, preview;
	SQLiteDatabase db;
	Cursor cur_v, cur_p, cur_a;
	String s1, s2, text, eventtype;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_incident);

		db = openOrCreateDatabase("tapntour.db", Context.MODE_PRIVATE, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);

		String c1 = "Create table if not exists videos(filename varchar(200),time integer)";
		db.execSQL(c1);
		String c2 = "Create table if not exists audios(filename varchar(200),time integer)";
		db.execSQL(c2);
		String c3 = "Create table if not exists photos(filename varchar(200),time integer)";
		db.execSQL(c3);
		// String str2 =
		// "Create table if not exists toUploadAudio(audio varcahr(200))";
		mic = (ImageButton) findViewById(R.id.mic);
		cam = (ImageButton) findViewById(R.id.camera);
		vid = (ImageButton) findViewById(R.id.video);
		report = (Button) findViewById(R.id.uploads);
		preview = (Button) findViewById(R.id.preview);

		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		View cView = getLayoutInflater().inflate(R.layout.tt_action_bar, null);
		actionBar.setCustomView(cView);


		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();

		StrictMode.setThreadPolicy(policy);


		Button btn_logoff = (Button) findViewById(R.id.btn_logoff);
		TextView text_task = (TextView) findViewById(R.id.text_task);

		mic.setOnClickListener(this);
		cam.setOnClickListener(this);
		vid.setOnClickListener(this);
		preview.setOnClickListener(this);
		report.setOnClickListener(this);
		btn_logoff.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				signOut();
			}
		});

		text_task.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub

				startActivity(new Intent(TTIncident.this, TTDoList.class));
				return false;
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.popup, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_action:
			showPopup();
			break;

		case R.id.menu_uploads:
			startActivity(new Intent(TTIncident.this, TTViewSwipe.class));
			break;

		case R.id.menu_record:
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void showPopup() {

		View menuItemView = findViewById(R.id.menu_action);
		PopupMenu popup = new PopupMenu(TTIncident.this, menuItemView);
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.menu_upload:
					startActivity(new Intent(TTIncident.this, TTViewSwipe.class));
					break;

				case R.id.menu_aboutus:
					startActivity(new Intent(TTIncident.this, TTAbout.class));
					break;

				case R.id.menu_settings:
					startActivity(new Intent(TTIncident.this, TTSettings.class));
					break;

				case R.id.menu_help:
					startActivity(new Intent(TTIncident.this, TTHelp.class));
					break;
				}

				return false;
			}
		});

		MenuInflater inflate = popup.getMenuInflater();
		inflate.inflate(R.menu.main, popup.getMenu());

		popup.show();

	}

	public void signOut() {

		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Signout");
		ad.setMessage("Some tasks are pending. Do You Want to clear it?");

		ad.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				startActivity(new Intent(TTIncident.this, TTDoList.class));
				finish();

			}
		});

		ad.setNegativeButton("No", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				dialog.cancel();

			}
		});
		ad.show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.mic:
			startActivity(new Intent(TTIncident.this, TTRecordAudio.class));
			break;
		case R.id.camera:
			CaptureImage();
			break;
		case R.id.video:
			startActivity(new Intent(TTIncident.this, TTVideoCapture.class));
			break;
		case R.id.preview:
			startActivity(new Intent(TTIncident.this, TTPreviewSwipe.class));
			break;
		case R.id.uploads:
			// Toast.makeText(getApplicationContext(), "Videos",
			// Toast.LENGTH_LONG).show();
			// System.out.print("videos inserted into db..");
			uploadMedia();
			break;
		}

	}

	/**/

	private void CaptureImage() {

		Date date = new Date();
		SimpleDateFormat am = new SimpleDateFormat("yyyyMMddhhmmss");
		String names = am.format(date);
		Intent cameraIntent = new Intent(
				android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

		String photo_path = Environment.getExternalStorageDirectory().getName()
				+ File.separatorChar + "Picture_" + names + ".jpg";
		s1 = photo_path;
		s2 = names;

		File _photoFile = new File(photo_path);
		try {
			if (_photoFile.exists() == false) {
				_photoFile.getParentFile().mkdirs();
				_photoFile.createNewFile();
			}
		} catch (IOException e) {

		}
		Uri _fileUri = Uri.fromFile(_photoFile);
		cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT,
				_fileUri);
		startActivity(cameraIntent);

		insertintodb();

	}

	private void uploadMedia() {
		System.out.print("entered upload");
		// db = openOrCreateDatabase("tapntour.db",
		// SQLiteDatabase.CREATE_IF_NECESSARY, null);
		// db.setVersion(1);
		// db.setLocale(Locale.getDefault());

		String str = "Select * from videos";
		cur_v = db.rawQuery(str, null);
		cur_v.moveToFirst();
		int countvideo = 0;
		while (!cur_v.isAfterLast()) {
			countvideo++;
			cur_v.moveToNext();
		}
		cur_v.moveToFirst();

		String str1 = "Select * from photos";
		cur_p = db.rawQuery(str1, null);
		cur_p.moveToFirst();
		int countphoto = 0;
		while (!cur_p.isAfterLast()) {
			countphoto++;
			cur_p.moveToNext();
		}
		cur_p.moveToFirst();

		String str2 = "Select * from audios";
		cur_a = db.rawQuery(str2, null);
		cur_a.moveToFirst();
		int countaudio = 0;
		while (!cur_a.isAfterLast()) {
			countaudio++;
			cur_a.moveToNext();
		}
		cur_a.moveToFirst();

		String CVideo = "" + countvideo;
		String CAudio = "" + countaudio;
		String CPhoto = "" + countphoto;

		Toast.makeText(getApplicationContext(),
				CAudio + ":" + CPhoto + ":" + CVideo, Toast.LENGTH_LONG).show();

		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(
				5);
		nameValuePairs.add(new BasicNameValuePair("eventtype", eventtype));
		nameValuePairs.add(new BasicNameValuePair("description", text));
		nameValuePairs.add(new BasicNameValuePair("videos", CVideo));
		nameValuePairs.add(new BasicNameValuePair("audios", CAudio));
		nameValuePairs.add(new BasicNameValuePair("photos", CPhoto));
		Toast.makeText(getApplicationContext(), "enters uploader",
				Toast.LENGTH_LONG).show();
		try {
			HttpClient httpclient = new DefaultHttpClient();
			HttpPost httppost = new HttpPost(
					"http://192.168.1.109/tap-n-tour/details.php");
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			final String response = httpclient.execute(httppost,
					responseHandler);
			System.out.print(response);

			Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG)
					.show();
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "catch", Toast.LENGTH_LONG)
					.show();
		}
audio();
video();
photo();
String c1 = "drop table audios";
db.execSQL(c1);
String c2 = "drop table videos";
db.execSQL(c2);
String c3 = "drop table photos";
db.execSQL(c3);
	}
	
	public void audio()
	{
		String encoded = "";
		String str2 = "Select * from audios";
		cur_a = db.rawQuery(str2, null);
		cur_a.moveToFirst();
		while (!cur_a.isAfterLast()) {
			String filename = cur_a.getString(0).toString();
			try {
				encoded = encodedFile("/sdcard/"+filename);
				Toast.makeText(getApplicationContext(),encoded, Toast.LENGTH_LONG)
				.show();
				System.out.print(encoded);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ArrayList<NameValuePair> audiovalues = new ArrayList<NameValuePair>(
					2);

			//audiovalues.add(new BasicNameValuePair("eventno","1"));
			audiovalues.add(new BasicNameValuePair("name","yyy"));
			audiovalues.add(new BasicNameValuePair("Song",encoded));
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost_audio = new HttpPost(
						"http://192.168.1.109/tap-n-tour/audio.php");
				httppost_audio.setEntity(new UrlEncodedFormEntity(audiovalues));
				ResponseHandler<String> audioresponse = new BasicResponseHandler();
				// HttpResponse esponse = httpclient.execute(httppost);
				final String audio_response = httpclient.execute(
						httppost_audio, audioresponse);
				Toast.makeText(getApplicationContext(),"dddd  :  " + audio_response, Toast.LENGTH_LONG)
				.show();
				System.out.print(audio_response);
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(),"audio catch", Toast.LENGTH_LONG)
				.show();
			}
              
			cur_a.moveToNext();
		}
		cur_a.moveToFirst();
	}

	private String encodedFile(String fileName) throws IOException {

		File file = new File(fileName);
		byte[] bytes = loadFile(file);
		byte[] encoded = Base64.encodeBase64(bytes);
		String encodedString = new String(encoded);
		return encodedString;

	}

	private byte[] loadFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);

		long length = file.length();
		if (length > Integer.MAX_VALUE) {

		}
		byte[] bytes = new byte[(int) length];

		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		is.close();
		return bytes;
	}


	public void video()
	{
		String encoded = "";
		String str2 = "Select * from videos";
		cur_a = db.rawQuery(str2, null);
		cur_a.moveToFirst();
		while (!cur_a.isAfterLast()) {
			String filename = cur_a.getString(0).toString();
			try {
				encoded = encodedFile("/sdcard/"+filename);
				Toast.makeText(getApplicationContext(),encoded, Toast.LENGTH_LONG)
				.show();
				System.out.print(encoded);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ArrayList<NameValuePair> audiovalues = new ArrayList<NameValuePair>(
					2);

			//audiovalues.add(new BasicNameValuePair("eventno","1"));
			audiovalues.add(new BasicNameValuePair("name","yyy"));
			audiovalues.add(new BasicNameValuePair("video",encoded));
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost_audio = new HttpPost(
						"http://192.168.1.109/tap-n-tour/video.php");
				httppost_audio.setEntity(new UrlEncodedFormEntity(audiovalues));
				ResponseHandler<String> audioresponse = new BasicResponseHandler();
				// HttpResponse esponse = httpclient.execute(httppost);
				final String audio_response = httpclient.execute(
						httppost_audio, audioresponse);
				Toast.makeText(getApplicationContext()," :  " + audio_response, Toast.LENGTH_LONG)
				.show();
				System.out.print(audio_response);
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(),"video catch", Toast.LENGTH_LONG)
				.show();
			}
              
			cur_a.moveToNext();
		}
		cur_a.moveToFirst();

		
		
	}
	public void photo()
	{
		String encoded = "";
		String str2 = "Select * from photos";
		cur_a = db.rawQuery(str2, null);
		cur_a.moveToFirst();
		while (!cur_a.isAfterLast()) {
			String filename = cur_a.getString(0).toString();
			try {
				encoded = encodedFile(filename);
				Toast.makeText(getApplicationContext(),encoded, Toast.LENGTH_LONG)
				.show();
				System.out.print(encoded);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			ArrayList<NameValuePair> audiovalues = new ArrayList<NameValuePair>(
					2);

			//audiovalues.add(new BasicNameValuePair("eventno","1"));
			audiovalues.add(new BasicNameValuePair("name","yyy"));
			audiovalues.add(new BasicNameValuePair("photo",encoded));
			try {

				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost_photo = new HttpPost(
						"http://192.168.1.109/tap-n-tour/photo.php");
				httppost_photo.setEntity(new UrlEncodedFormEntity(audiovalues));
				ResponseHandler<String> photoresponse = new BasicResponseHandler();
				// HttpResponse esponse = httpclient.execute(httppost);
				final String photo_response = httpclient.execute(
						httppost_photo, photoresponse);
				Toast.makeText(getApplicationContext()," :  " + photo_response, Toast.LENGTH_LONG)
				.show();
				System.out.print(photo_response);
				
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(getApplicationContext(),"photo catch", Toast.LENGTH_LONG)
				.show();
			}
              
			cur_a.moveToNext();
		}
		cur_a.moveToFirst();

	}

	private void insertintodb() {
		if (!s1.equals("") && !s2.equals("")) {
			ContentValues cv1 = new ContentValues();
			cv1.put("filename", s1);
			cv1.put("time", s2);

			db.insert("photos", null, cv1);

		}

	}

}
