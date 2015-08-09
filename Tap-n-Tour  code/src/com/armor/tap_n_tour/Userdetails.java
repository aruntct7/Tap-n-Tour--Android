package com.armor.tap_n_tour;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;





import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

public class Userdetails extends Activity {
Dbhelper maindb=new Dbhelper(Userdetails.this);
TextView tvUserlist;
Context context;
JSONArray jsonarray;
JSONObject json;
Cursor cursor;
int i=0;
@SuppressLint("ShowToast")
@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.userlist);
		tvUserlist=(TextView)findViewById(R.id.tvlist);
//maindb.open();
sqldb();
	
}
//cursor.close();
 void sqldb() {
	// TODO Auto-generated method stub
	
	SQLiteDatabase db=openOrCreateDatabase(Dbhelper.DATABASE_NAME,SQLiteDatabase.CREATE_IF_NECESSARY , null);
	 cursor=db.rawQuery("SELECT latitude,longitude  FROM location ORDER BY _id", null);
	cursor.moveToFirst();

	while (cursor.isAfterLast() == false) {
	tvUserlist.append("("+i++ +")\t\t"+cursor.getString(0)+"\t"+cursor.getString(1)+"\n");
	  /*try {
		MysqlInsert(cursor.getString(0), cursor.getString(1),"time" );
	} catch (JSONException e) {
		// TODO Auto-generated catch block
	Toast.makeText(getApplicationContext(), "not working", Toast.LENGTH_LONG).show();
	}*/

	    cursor.moveToNext();  
	}  
	
}
  

}
