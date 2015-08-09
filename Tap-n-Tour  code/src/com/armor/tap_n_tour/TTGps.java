package com.armor.tap_n_tour;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;







import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

@SuppressLint("ShowToast")
public class TTGps extends Activity implements LocationListener,
		android.view.View.OnClickListener {
	
	Dbhelper maindb=new Dbhelper(TTGps.this);
	TextView tvUserlist;
	Context context;
	JSONArray jsonarray;
	JSONObject json;
	Cursor cursor;
	int i=0;
	Timer t ;
	Userdetails usrd=new Userdetails();

	public static final int DIALOG_DOWNLOAD_JSON_PROGRESS = 0;
	boolean inter=false;
	public static int connected=1;
	private ProgressDialog mProgressDialog;
	ListView lstView1;
	ArrayList<HashMap<String, Object>> MyArrList;
	TextView tv;
	Location location;
	Handler handler = new Handler();
	LocationManager lm;
	ToggleButton tgtoggle;
	String sqrloc = "-1";
	int iqrloc;
	ListView lv;
	LocationListener ll;
	String[] s;
	TimerTask mtTimerTask;
	Timer mtimer;
	boolean userscroll = true;
	/*
	 * private final List<Car> myCars = new ArrayList<Car>();
	 */ListView list;
	boolean stopgps = true;
	Button scanner2,database;
	Armor arm = new Armor();
	String slat;
	String slong;
	String stime;
	@SuppressLint({ "ResourceAsColor", "InlinedApi", "NewApi" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);
//maindb.open();
		// Permission StrictMode
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
		
		actionbar();

	
				iqrloc = Integer.parseInt(sqrloc);
		/*this.registerReceiver(this.WifiStateChangedReceiver, new IntentFilter(
				WifiManager.WIFI_STATE_CHANGED_ACTION));*/

		tgtoggle = (ToggleButton) findViewById(R.id.tgtoggle);
		tgtoggle.setOnClickListener(this);

		try {

			scanner2 = (Button) findViewById(R.id.scanner2);
			scanner2.setOnClickListener(this);
database=(Button)findViewById(R.id.dbresult);
database.setOnClickListener(this);

		} catch (ActivityNotFoundException anfe) {
			Log.e("onCreate", "Scanner Not Found", anfe);
		}
		if(connected==0){
			new DownloadJSONFileAsync().execute();
			}
		//Declare the timer
				 t = new Timer();
				//Set the schedule function and rate
				t.scheduleAtFixedRate(new TimerTask() {
	    	    	int io=0;
                    int syn=0;
                    int trn=0;
				    @Override
				    public void run() {
				    	
		    	    	
				    	runOnUiThread(new Runnable() {

				    	    @Override
				    	    public void run() {
				    	        /*TextView tv = (TextView) findViewById(R.id.timer);
				    	        tv.setText(String.valueOf(time));
				    	        time += 1;*/
				    	    	if(isOnline()){
				    	    		//Toast.makeText(getApplicationContext(),"online",Toast.LENGTH_SHORT).show();
				    	    		
				    	    		
				    	    		io=value(1,io);
				    	    		//trn=trun(1,trn);

				    	    		syn=sqldb(1,syn);
				    				//Toast.makeText(getApplicationContext(),connected+"", Toast.LENGTH_LONG).show();

					    	    	Button scanner2=(Button)findViewById(R.id.scanner2);
					    	    scanner2.setEnabled(true);			
					   
								/*try {
									new DownloadJSONFileAsync().execute();
								} catch (Exception e) {
									// TODO Auto-generated catch block
				    	    		Toast.makeText(getApplicationContext(),"problem with listview",Toast.LENGTH_SHORT).show();

									e.printStackTrace();
								}*/
				    	    	}

				    	    	else{
				    	    		Toast.makeText(getApplicationContext(),"offline",Toast.LENGTH_SHORT).show();
				                             syn=0;
				                             trn=0;
				                             inter=false;
				    	    	Button scanner2=(Button)findViewById(R.id.scanner2);
				    	    scanner2.setEnabled(false);			
				    	    	}
				    	    }
				    	     
				    	});  //Called each time when 1000 milliseconds (1 second) (the period parameter)
				    }
				         
				},
				//Set how long before to start calling the TimerTask (in milliseconds)
				0,
				//Set the amount of time between each execution (in milliseconds)
				5000);
								
	
	
	}
int value(int a,int b){
	if(a==1&&b==0){
		try {
			new DownloadJSONFileAsync().execute();
			if(!scanner2.isEnabled())
				scanner2.setEnabled(true);
			inter=true;
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
Toast.makeText(getApplicationContext(), "OOps server down", Toast.LENGTH_SHORT).show();
}
	b=1;
	}else
		scanner2.setEnabled(false);

return b;
}

	@Override
	public void onLocationChanged(Location location) {
		
		// TODO Auto-generated method stub
		Date today = new Date();
		Timestamp currentTimeStamp = new Timestamp(today.getTime());
		if (location != null) {
			/*Log.d("LOCATION CHANGED", location.getLatitude() + "");
			Log.d("LOCATION CHANGED", location.getLongitude() + "");
			String str = "\n CurrentLocation: " + "\n Latitude: "
					+ location.getLatitude() + "\n Longitude: "
					+ location.getLongitude() + "\n Altitude: "
					+ location.getAltitude() + "\n Accuracy: "
					+ location.getAccuracy() + "\n CurrentTimeStamp "
					+ currentTimeStamp;*/
			//Toast.makeText(TTGps.this, str, Toast.LENGTH_LONG).show();
			slat = location.getLatitude()+"";
			slong = location.getLongitude()+"";
			stime = currentTimeStamp+"";
			if(inter==true){
				try {
					MysqlInsert(location.getLatitude(),
							location.getLongitude(), currentTimeStamp);
					//updateDatabase();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					Toast.makeText(getApplicationContext(), "server error",
							Toast.LENGTH_LONG).show();
					//updateDatabase();

				}

			}else{
				updateDatabase();
			}

		}
	}
	
	private void MysqlInsert(double lat, double lon, Timestamp currentTimeStamp)
			throws JSONException {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(
		/* "http://192.168.1.14/TapnTour/TapnTour_insert.php"); */
		"http://192.168.1.14/TapnTour/TapnTour_insert.php");
		JSONObject json = new JSONObject();

		try {
			// JSON data:
			json.put("EmpId", "619");

			json.put("Latitude", lat);

			json.put("Longitude", lon);
			json.put("LocTime", currentTimeStamp);
			JSONArray postjson = new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost.setHeader("json", json.toString());
			httppost.getParams().setParameter("jsonpost", postjson);

			// Execute HTTP Post Request
			System.out.print(json);
			HttpResponse response = httpclient.execute(httppost);

			// for JSON:
			if (response != null) {
				InputStream is = response.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// text = sb.toString();
				Toast.makeText(TTGps.this, sb.toString(), Toast.LENGTH_LONG)
						.show();
			}

			// tv.setText(text);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}

	// ----mysql insert finished

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	
	@Override
	public void onClick(View v) {
		Intent intent = new Intent("com.google.zxing.client.android.SCAN");

		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.tgtoggle:

			if (tgtoggle.isChecked()) {
				Toast.makeText(getApplicationContext(), "stop",
						Toast.LENGTH_LONG).show();

				// lm = (LocationManager)
				// getSystemService(Context.LOCATION_SERVICE);

				// LocationListener ll = new mylocationlistener();
				if (!lm.equals(null))
					lm.removeUpdates(this);
				stopgps = true;
			}
			/*
			 * Intent intent = new
			 * Intent("android.location.GPS_ENABLED_CHANGE");
			 * intent.putExtra("enabled", false); sendBroadcast(intent);
			 */

			else {
				/*
				 * Toast.makeText(getApplicationContext(),
				 * "start",Toast.LENGTH_LONG ).show(); Intent intent = new
				 * Intent("android.location.GPS_ENABLED_CHANGE");
				 * intent.putExtra("enabled", true); sendBroadcast(intent);
				 */

				// turnGPSOn();
				lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				boolean gpsStatus = lm
						.isProviderEnabled(LocationManager.GPS_PROVIDER);

				if (!gpsStatus) {
					// ---display the "Location services" settings page---
					/*
					 * Intent in = new Intent(
					 * android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS
					 * ); startActivity(in);
					 */

					Toast.makeText(TTGps.this, "GPS is not enabled",
							Toast.LENGTH_LONG).show();
					tgtoggle.setChecked(true);

				}

				else if (gpsStatus) {
					tgtoggle.setChecked(false);

				}
				if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					tgtoggle.setChecked(false);

					Criteria criteria = new Criteria();
					criteria.setAccuracy(Criteria.ACCURACY_FINE);
					criteria.setPowerRequirement(Criteria.NO_REQUIREMENT);
					String bestProvider = lm.getBestProvider(criteria, true);

					// LocationListener ll = new mylocationlistener();
					lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
							10 * 1000L, 0, this);
					lm.getProvider(LocationManager.GPS_PROVIDER)
							.supportsAltitude();
					// handler.postDelayed(runnable, 600000);
					stopgps = false;
				}
			}
			break;
		case R.id.scanner2:
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);

			break;
		case R.id.dbresult:
			/*Cursor c=null;
		    try {
		     AlertDialog.Builder al=new AlertDialog.Builder(TTGps.this);
		     c=maindb.showData();
		     
		     if(c.getCount()>0)
		     {
		      for (c.moveToFirst();!c.isAfterLast(); c.moveToNext())
		      {
		       //c.getInt(0);
		       c.getString(1);
		       c.getString(2);
		       c.getString(3);
		       
		       
		       al.setTitle("gpslocation").setMessage("Latitude :"+c.getString(1)+" \nLongitude : "+c.getString(2)+" \nTime : "+c.getString(3));
		       al.setCancelable(true);
		       
		      }
		      
		      al.show();
		      c.close();
		     }
		     else
		     {
		      Log.d("", "No value to display");
		     }
		     
		     
		    } catch (Exception e) {
		     // TODO: handle exception
		     c.close();
		     e.printStackTrace();
		    }*/
		    Intent i= new Intent(this,Userdetails.class);
		    startActivity(i);
			break;
		}
	}

	// turnGPSOff();
	// handler.postDelayed(runnable, 2000);}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				// Handle successful scan
				sqrloc = contents;
				iqrloc = Integer.parseInt(sqrloc);

				userscroll = false;
				// populateCarList();
				// populateListView();
				// listhighlight();
				new DownloadJSONFileAsync().execute();

				Toast toast = Toast.makeText(this, "Content:" + contents,
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();
			} else if (resultCode == RESULT_CANCELED) {
				// Handle cancel
				Toast toast = Toast.makeText(this, "Scan was Cancelled!",
						Toast.LENGTH_LONG);
				toast.setGravity(Gravity.TOP, 25, 400);
				toast.show();

			}
		}
	}

		void scr() {
		lstView1.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				userscroll = true;

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == 0)
					userscroll = true;

			}
		});
	}

	//

	void scr2() {
		lstView1.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				// userscroll = true;

			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (scrollState == 0)
					userscroll = true;

			}
		});
	}

	

	/*private final BroadcastReceiver WifiStateChangedReceiver = new BroadcastReceiver() {

		@SuppressLint("ShowToast")
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			isNetworkConnected();
			
			 * int extraWifiState = intent.getIntExtra(
			 * WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
			 * 
			 * switch (extraWifiState) { case WifiManager.WIFI_STATE_DISABLED:
			 * Toast.makeText(MainActivity.this, "WIFI STATE DISABLED",
			 * Toast.LENGTH_LONG).show(); System.out.println("disabling");
			 * Toast.makeText(MainActivity.this, "Switching to Sqlite",
			 * Toast.LENGTH_LONG).show(); break; case
			 * WifiManager.WIFI_STATE_DISABLING:
			 * Toast.makeText(getApplicationContext(), "WIFI STATE DISABLING",
			 * Toast.LENGTH_LONG);
			 * 
			 * break; case WifiManager.WIFI_STATE_ENABLED:
			 * Toast.makeText(getApplicationContext(), "WIFI STATE ENABLED",
			 * Toast.LENGTH_LONG).show(); System.out.println("Enabled");
			 * Toast.makeText(MainActivity.this, "Switching to Mysql and Sync",
			 * Toast.LENGTH_LONG) .show();
			 * 
			 * break; case WifiManager.WIFI_STATE_ENABLING:
			 * Toast.makeText(getApplicationContext(), "WIFI STATE ENABLING",
			 * Toast.LENGTH_LONG); break; case WifiManager.WIFI_STATE_UNKNOWN:
			 * Toast.makeText(getApplicationContext(), "WIFI STATE UNKNOWN",
			 * Toast.LENGTH_LONG);
			 * 
			 * break; }
			 
		}
	};*/

	private boolean isNetworkConnected() {
		if (!arm.haveNetworkConnection(getApplicationContext())) {
			inter = false;
			scanner2.setEnabled(false);
			return false;
		} else
			inter = true;
		Toast.makeText(getApplicationContext(), " Internet Connection found",
				Toast.LENGTH_LONG).show();
		Toast.makeText(getApplicationContext(), "Sync to mysql",
				Toast.LENGTH_LONG).show();

		//sqldb();

		try {
			if (arm.checkurl())
				new DownloadJSONFileAsync().execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			// arm.tst("Server Error");
			Toast.makeText(getApplicationContext(), "Server Down",
					Toast.LENGTH_LONG).show();
		}

		if (!scanner2.isEnabled()) {
			scanner2.setEnabled(true);

		}
		/*
		 * if (!tgtoggle.isEnabled()) { tgtoggle.setEnabled(true);
		 * 
		 * }
		 */

		//
		return true;
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_DOWNLOAD_JSON_PROGRESS:
			mProgressDialog = new ProgressDialog(this);
			mProgressDialog.setMessage("Loading Please Wait.....");
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			mProgressDialog.setCancelable(true);
			mProgressDialog.show();
			return mProgressDialog;
		default:
			return null;
		}
	}

	// Show All Content

	public void ShowAllContent() {
		// listView1
		lstView1 = (ListView) findViewById(R.id.listView1);
		lstView1.setAdapter(new ImageAdapter(TTGps.this, MyArrList));
		String count =""+lstView1.getCount();
	//Toast.makeText(getApplicationContext(), count, Toast.LENGTH_LONG).show();
	}

	public class ImageAdapter extends BaseAdapter {
		private final Context context;
		private ArrayList<HashMap<String, Object>> MyArr = new ArrayList<HashMap<String, Object>>();

		public ImageAdapter(Context c,
				ArrayList<HashMap<String, Object>> myArrList) {
			// TODO Auto-generated method stub
			context = c;
			MyArr = myArrList;

			/*
			 * if(MyArr.equals(null)) {
			 * 
			 * }
			 */
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub

			return MyArr.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@SuppressLint("InlinedApi")
		@SuppressWarnings("unused")
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub

			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

			if (convertView == null) {
				convertView = inflater.inflate(R.layout.activity_column, null);
			}

			// ColImage
			ImageView imageView = (ImageView) convertView
					.findViewById(R.id.ColImgPath);
			/*
			 * imageView.getLayoutParams().height = 80;
			 * imageView.getLayoutParams().width = 80;
			 */
			imageView.setPadding(5, 5, 5, 5);
			// imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
			try {
				/*
				 * if (imageView == null) { if (mProgressDialog.isShowing())
				 * mProgressDialog.dismiss(); if (lstView1.isShown())
				 * lstView1.setVisibility(View.GONE); }
				 */
				imageView.setImageResource(R.drawable.link);

				/*
				 * imageView.setImageBitmap((Bitmap) MyArr.get(position).get(
				 * "ImageThumBitmap"));
				 */
			} catch (Exception e) {
				// When Error
				imageView.setImageResource(R.drawable.ic_launcher);
			}

			
			// ColImgName
			TextView txtPicName = (TextView) convertView
					.findViewById(R.id.locationname);
			txtPicName.setPadding(10, 0, 0, 0);
			txtPicName.setText(MyArr.get(position).get("ImageName").toString());
			//
			
			// ColImgID
						TextView txtImgID = (TextView) convertView
								.findViewById(R.id.ColImgID);
						txtImgID.setPadding(10, 0, 0, 0);
						txtImgID.setText("Direction : "+" " +"Move left"
								/*+ MyArr.get(position).get("ImageID").toString()*/);
						txtImgID.setVisibility(View.GONE);
			
			
			// Scroll and highlight
			lstView1.setDivider(null);
			convertView.setBackgroundResource(R.color.needtogo);
			
			if (position<iqrloc - 1){
				convertView.setBackgroundResource(R.color.completed);
			}else if (position==iqrloc - 1){
				txtImgID.setVisibility(View.VISIBLE);

				convertView.setBackgroundResource(R.color.current);
			//	userscroll = true;
				//scr2();
			}
				
		//	lstView1.setSelectionFromTop(iqrloc - 1, iqrloc - 1);
			if (userscroll == false){
				//lstView1.smoothScrollToPosition(iqrloc - 1);

			for(int i=0;i<=iqrloc-1;i++)
			{
				//Toast.makeText(getApplicationContext(), ""+i, Toast.LENGTH_SHORT).show();
				if (userscroll == false)
					lstView1.smoothScrollToPosition(i);
				
				
				if (position<iqrloc - 1){
					convertView.setBackgroundResource(R.color.completed);
				}else if (position==iqrloc - 1){
txtImgID.setVisibility(View.VISIBLE);
convertView.setBackgroundResource(R.color.current);
				//	userscroll = true;
					scr2();
				}
				/*txtImgID.setText("ID : "+""+i+""
						+ MyArr.get(position).get("ImageID").toString());*/
				//convertView.setBackgroundResource(android.R.color.background_light);

			}

			}
				System.out.println(position);

			
			
			/*if (position == iqrloc - 1 ) {
				
				scr2();
									
				// scr();
				// TODO: set the proper selection color here:
				convertView.setBackgroundResource(android.R.color.darker_gray);
			} else if (position < iqrloc - 1 )
				convertView.setBackgroundResource(android.R.color.black);
			*/
			return convertView;

		}

	}

	// Download JSON in Background
	public class DownloadJSONFileAsync extends AsyncTask<String, Void, Void> {

		@SuppressWarnings("deprecation")
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
		}

		@Override
		protected Void doInBackground(String... params) {
			// TODO Auto-generated method stub



			String url = "http://192.168.1.14/TapnTour/TapLocations.php";

			JSONArray data;
			try {
				data = new JSONArray(getJSONUrl(url));

				MyArrList = new ArrayList<HashMap<String, Object>>();
				HashMap<String, Object> map;

				for (int i = 0; i < data.length(); i++) {
					JSONObject c = data.getJSONObject(i);
					map = new HashMap<String, Object>();
					map.put("ImageName", c.getString("Location"));

					map.put("ImageID", c.getString("ID"));


					MyArrList.add(map);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Void unused) {
			ShowAllContent(); // When Finish Show Content
			dismissDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
			removeDialog(DIALOG_DOWNLOAD_JSON_PROGRESS);
		}

	}

	/*** Get JSON Code from URL ***/
	public String getJSONUrl(String url) {
		StringBuilder str = new StringBuilder();
		HttpClient client = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
		try {
			HttpResponse response = client.execute(httpGet);
			StatusLine statusLine = response.getStatusLine();
			int statusCode = statusLine.getStatusCode();
			if (statusCode == 200) { // Download OK
				HttpEntity entity = response.getEntity();
				InputStream content = entity.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(content));
				String line;
				while ((line = reader.readLine()) != null) {
					str.append(line);
				}
			} else {
				Log.e("Log", "Failed to download file..");
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str.toString();
	}

	/***** Get Image Resource from URL (Start) *****/
	private static final String TAG = "Image";
	private static final int IO_BUFFER_SIZE = 4 * 1024;

	public static Bitmap loadBitmap(String url) {
		Bitmap bitmap = null;
		InputStream in = null;
		BufferedOutputStream out = null;

		try {
			in = new BufferedInputStream(new URL(url).openStream(),
					IO_BUFFER_SIZE);

			final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
			out = new BufferedOutputStream(dataStream, IO_BUFFER_SIZE);
			copy(in, out);
			out.flush();

			final byte[] data = dataStream.toByteArray();
			BitmapFactory.Options options = new BitmapFactory.Options();
			// options.inSampleSize = 1;

			bitmap = BitmapFactory.decodeByteArray(data, 0, data.length,
					options);
		} catch (IOException e) {
			Log.e(TAG, "Could not load Bitmap from: " + url);
		} finally {
			closeStream(in);
			closeStream(out);
		}

		return bitmap;
	}

	private static void closeStream(Closeable stream) {
		if (stream != null) {
			try {
				stream.close();
			} catch (IOException e) {
				android.util.Log.e(TAG, "Could not close stream", e);
			}
		}
	}

	private static void copy(InputStream in, OutputStream out)
			throws IOException {
		byte[] b = new byte[IO_BUFFER_SIZE];
		int read;
		while ((read = in.read(b)) != -1) {
			out.write(b, 0, read);
		}
	}

	/***** Get Image Resource from URL (End) *****/

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if (stopgps == false)
			lm.removeUpdates(this);
		//unregisterReceiver(WifiStateChangedReceiver);

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// isNetworkConnected2();
		Toast.makeText(getApplicationContext(), "OnPause", Toast.LENGTH_SHORT)
				.show();
	}

	protected void OnResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// isNetworkConnected2();
		Toast.makeText(getApplicationContext(), "OnResume", Toast.LENGTH_SHORT)
				.show();
	}

	private boolean isNetworkConnected2() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = cm.getActiveNetworkInfo();
		if (ni == null) {
			// There are no active networks.
			Toast.makeText(getApplicationContext(), "No Internet Connection",
					Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Switching to sqlite",
					Toast.LENGTH_LONG).show();

			/*
			 * if (mProgressDialog.isShowing())
			 * 
			 * mProgressDialog.dismiss();
			 */scanner2.setEnabled(false);
			// tgtoggle.setEnabled(false);
			// lstView1.setVisibility(View.GONE);
			return false;
		} else
			/*
			 * Toast.makeText(getApplicationContext(),
			 * " Internet Connection Found", Toast.LENGTH_LONG).show();
			 * Toast.makeText(getApplicationContext(),
			 * "Switching to Mysql and Sync", Toast.LENGTH_LONG).show();
			 */

			// load listview
			// new DownloadJSONFileAsync().execute();

			scanner2.setEnabled(true);

		/*
		 * if (!tgtoggle.isEnabled()) { tgtoggle.setEnabled(true);
		 * 
		 * }
		 */

		//
		return true;
	}
	public boolean isOnline() {
	    ConnectivityManager cm =
	        (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo netInfo = cm.getActiveNetworkInfo();
	    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
	        return true;
	    }
	    return false;
	}
	/*
	 * @Override protected void onResume() { // TODO Auto-generated method stub
	 * super.onResume(); new DownloadJSONFileAsync().execute();
	 * Toast.makeText(getApplicationContext(), "OnResume", Toast.LENGTH_SHORT)
	 * .show(); }
	 */

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

	
	public  void updateDatabase(){
		 maindb=new Dbhelper(TTGps.this);
		    
		    ContentValues cv=new ContentValues();
		    {
		     cv.put(Dbhelper.Latitude, slat.trim());
		     cv.put(Dbhelper.Longitude, slong.trim());
		     cv.put(Dbhelper.Gps_Time, stime.trim()); 
		    
		    
		     try
		     {
		      maindb.insertEmpData(cv); 
		     } catch (SQLiteException e) {
		      // TODO: handle exception
		      e.printStackTrace();
		     }
		     catch (Exception e) {
		      // TODO: handle exception
		      e.printStackTrace();
		     }
		    }
    Toast.makeText(getApplicationContext(), "Registered", Toast.LENGTH_LONG).show();
	}
	///sync
 int sqldb(int a,int b) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = maindb.getReadableDatabase();
		if(a==1&&b==0){
	Cursor cursor1=db.rawQuery("SELECT latitude,longitude ,time FROM location ORDER BY _id", null);
		cursor1.moveToPosition(0);

	while (cursor1.isAfterLast() == false) {
		//tvUserlist.append("("+i++ +")\t\t"+cursor.getString(0)+"\t"+cursor.getString(1)+"\n");
		  try {
			MysqlInsert1(cursor1.getString(0), cursor1.getString(1),cursor1.getString(2) );
		} catch (JSONException e) {
			// TODO Auto-generated catch block
		Toast.makeText(getApplicationContext(), "check php file", Toast.LENGTH_LONG).show();
		}

		    cursor1.moveToNext();  
		}
	cursor1.moveToPosition(0);
cursor1.close();
		}
		b=1;
	return b;
 }
 
 int trun(int a,int b){
	 if(a==1&&b==0){
		 try {
			Mysqldelete();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			Toast.makeText(getApplicationContext(), "Check employee id", Toast.LENGTH_LONG).show();
			e.printStackTrace();
		}
	 /*HttpClient mClient= new DefaultHttpClient();

	 HttpGet get = new HttpGet("http://192.168.1.14/TapnTour/truncate.php");

	 try {
		mClient.execute(get);
	} catch (ClientProtocolException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		Toast.makeText(getApplicationContext(), "not truncated", Toast.LENGTH_SHORT).show();
		e.printStackTrace();
	}*/}
	 b=1;
return b;
 }
 
	public void MysqlInsert1(String lat, String lon, String currentTimeStamp)
			throws JSONException {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
				
			
		HttpPost httppost1 = new HttpPost(
		 "http://192.168.1.14/TapnTour/TapnTour_insert.php"); 
		/*"http://192.168.1.14/TapnTour/truncate.php");*/
		JSONObject json = new JSONObject();

		try {
			// JSON data:
			json.put("EmpId", "619");

			json.put("Latitude", lat);

			json.put("Longitude", lon);
			json.put("LocTime", currentTimeStamp);
			JSONArray postjson = new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost1.setHeader("json", json.toString());
			httppost1.getParams().setParameter("jsonpost", postjson);

			// Execute HTTP Post Request
			System.out.print(json);
			//HttpResponse response = httpclient.execute(httppost);
			HttpResponse response1 = httpclient.execute(httppost1);

			// for JSON:
			if (response1 != null) {
				InputStream is = response1.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// text = sb.toString();
				Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG)
						.show();
			}

			// tv.setText(text);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	
	//mysql delete
	
	public void Mysqldelete()
			throws JSONException {
		// TODO Auto-generated method stub
		HttpClient httpclient = new DefaultHttpClient();
				
			
		HttpPost httppost1 = new HttpPost(
		// "http://192.168.1.14/TapnTour/TapnTour_insert.php"); 
		"http://192.168.1.14/TapnTour/truncate.php");
		JSONObject json = new JSONObject();

		try {
			// JSON data:
			json.put("EmpId", "619");

			
			JSONArray postjson = new JSONArray();
			postjson.put(json);

			// Post the data:
			httppost1.setHeader("json", json.toString());
			httppost1.getParams().setParameter("jsonpost", postjson);

			// Execute HTTP Post Request
			System.out.print(json);
			//HttpResponse response = httpclient.execute(httppost);
			HttpResponse response1 = httpclient.execute(httppost1);

			// for JSON:
			if (response1 != null) {
				InputStream is = response1.getEntity().getContent();

				BufferedReader reader = new BufferedReader(
						new InputStreamReader(is));
				StringBuilder sb = new StringBuilder();

				String line = null;
				try {
					while ((line = reader.readLine()) != null) {
						sb.append(line + "\n");
					}
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
					try {
						is.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				// text = sb.toString();
				Toast.makeText(this, sb.toString(), Toast.LENGTH_LONG)
						.show();
			}

			// tv.setText(text);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.popup, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_action:
			showPopup();
			break;

		case R.id.menu_uploads:
			// startActivity(new Intent(TTDirections.this, TTViewSwipe.class));
			break;

		case R.id.menu_record:
			// startActivity(new Intent(TTDirections.this, TTIncident.class));
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	@SuppressLint("NewApi")
	public void showPopup() {

		View menuItemView = findViewById(R.id.menu_action);
		PopupMenu popup = new PopupMenu(TTGps.this, menuItemView);
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				// TODO Auto-generated method stub
				switch (item.getItemId()) {

				case R.id.menu_aboutus:
					// startActivity(new Intent(TTDirections.this,
					// TTAbout.class));
					break;

				case R.id.menu_upload:
					// startActivity(new
					// Intent(TTDirections.this,TTViewSwipe.class));
					break;

				case R.id.menu_settings:
					// startActivity(new
					// Intent(TTDirections.this,TSettings.class));
					break;

				case R.id.menu_help:
					// startActivity(new Intent(TTDirections.this,
					// TTHelp.class));
					break;

				}
				return false;
			}
		});

		MenuInflater inflate = popup.getMenuInflater();
		inflate.inflate(R.menu.main, popup.getMenu());

		popup.show();

	}
	


}
