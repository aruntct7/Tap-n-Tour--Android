package com.armor.tap_n_tour;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("ShowToast")
public class TTQrScan extends Activity implements android.view.View.OnClickListener {
	

	TextView tvUserlist;
	Context context;
	JSONArray jsonarray;
	JSONObject json;
	Cursor cursor;
	private int i=0;


	public static final int DIALOG_DOWNLOAD_JSON_PROGRESS = 0;
	private static final BroadcastReceiver WifiStateChangedReceiver = null;
	boolean inter;



	ArrayList<HashMap<String, Object>> MyArrList;
	TextView tv;
	Location location;
	Handler handler = new Handler();
	LocationManager lm;
	ToggleButton tgtoggle;
	String sqrloc ;
	String iqrloc;
	ListView lv;
	LocationListener ll;
	String[] s;
	boolean userscroll = true;

	boolean stopgps = true;
	ImageButton scanner2,database;

	String slat;
	String slong;

	@SuppressLint({ "ResourceAsColor", "InlinedApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.tt_qrscan);
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}


		actionbar();

		iqrloc = sqrloc;

		Toast.makeText(TTQrScan.this, sqrloc, Toast.LENGTH_LONG)
		.show();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
				"http://192.168.1.109/tap-n-tour/names.php");

		ResponseHandler<String> responseHandler = new BasicResponseHandler();

		String response = null;
		try {
			response = httpclient.execute(httppost,responseHandler);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	System.out.println("Response : " + response);
	


		

		try {

			scanner2 = (ImageButton) findViewById(R.id.scanner2);
			scanner2.setOnClickListener(this);

		} catch (ActivityNotFoundException anfe) {
			Log.e("onCreate", "Scanner Not Found", anfe);
		}

	}



	@Override
	public void onClick(View v) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");


		switch (v.getId()) {

		case R.id.scanner2:
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);

			break;
		}
	}

	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	
				sqrloc = contents;
				iqrloc=sqrloc;
	

				userscroll = false;
	
				Toast toast = Toast.makeText(this, "Content:" + contents,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			} else if (resultCode == RESULT_CANCELED) {
	
				Toast toast = Toast.makeText(this, "Scan was Cancelled!",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();

			}
		}
	}

	




	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (stopgps == false)
		//	lm.removeUpdates(this);
		unregisterReceiver(WifiStateChangedReceiver);

	}

	@Override
	protected void onPause() {

		super.onPause();

		Toast.makeText(getApplicationContext(), "OnPause", Toast.LENGTH_SHORT)
				.show();
	}

	protected void OnResume() {

		super.onResume();

		Toast.makeText(getApplicationContext(), "OnResume", Toast.LENGTH_SHORT)
				.show();
	}

	@SuppressLint("NewApi")
	void actionbar() {
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		View cView = getLayoutInflater().inflate(R.layout.tt_action_bar, null);
		actionBar.setCustomView(cView);

		Button btn_logoff = (Button) findViewById(R.id.btn_logoff);
		TextView text_task = (TextView) findViewById(R.id.text_task);
	}

}