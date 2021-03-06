package com.armor.tap_n_tour;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class TTAudios extends SwipeListViewActivity {
	ListView lv;
	MediaPlayer mPlayer=null;
	ArrayAdapter<String> aa;
	String[] arr ;
	
	ArrayList<String> filename_list;
	SQLiteDatabase db;
	Cursor cur;
	
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tt_audios);
        db = openOrCreateDatabase("tapntour.db", Context.MODE_PRIVATE, null);
		db.setVersion(1);
		db.setLocale(Locale.getDefault());
		db.setLockingEnabled(true);
		
		filename_list = new ArrayList<String>();

		String str1 = "Create table if not exists audios(filename varchar(200),time integer)";
		db.execSQL(str1);
		int count=0;
		String str2 = "Select * from audios";
		cur = db.rawQuery(str2, null);
		cur.moveToFirst();
		/*while (!cur.isAfterLast()) {
			count++;
			cur.moveToNext();
		}

		cur.moveToFirst();
		arr = new String[count];
		int i = 0;*/

		while (!cur.isAfterLast()) {
			String filename = cur.getString(0).toString();
			
			filename_list.add(filename);
			
			/*arr[i] = filename;
			i++;
			*/
			cur.moveToNext();

		}
		
		
        lv = (ListView) findViewById(R.id.audiolist);
        aa = new ArrayAdapter<String>(this,
		android.R.layout.simple_list_item_1, filename_list);
    	aa.notifyDataSetChanged();
    	lv.setAdapter(aa);
		
    	/*lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {

				int position=arg2;
				 String filename;
				 filename="/sdcard/"+filename_list.get(position);
				          mPlayer = new MediaPlayer();
				          try {
				              mPlayer.setDataSource(filename);
				              mPlayer.prepare();
				              mPlayer.start();
				          } catch (IOException e) {
				          }

				
    }

		
});/**/
    } 

	@Override
	public ListView getListView() {
		
		return this.lv;
	}

	@Override
	public void getSwipeItem(boolean isRight, int position) {
		// TODO Auto-generated method stub
		
		String removed_item = filename_list.get(position);				
		
		filename_list.remove(position);
		aa = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, filename_list);
		lv.setAdapter(aa);
		
		
		if(!removed_item.equals(""))
		{
			db.delete("audios", "filename=?", new String[]{removed_item});
			File file=new File("/sdcard/"+removed_item);
			file.delete();
		
			Toast.makeText(getApplication(), "Deleted" , Toast.LENGTH_LONG).show();
			
		}
	
		
	}

	@Override
	public void onItemClickListener(ListAdapter adapter, int position) {
		// TODO Auto-generated method stub
		
		 String filename;
		 filename="/sdcard/"+filename_list.get(position);
		 Toast.makeText(getApplication(), filename , Toast.LENGTH_LONG).show();
		 
		          mPlayer = new MediaPlayer();
		          try {
		              mPlayer.setDataSource(filename);
		              mPlayer.prepare();
		              mPlayer.start();
		          } catch (IOException e) {
		          }

	}
}