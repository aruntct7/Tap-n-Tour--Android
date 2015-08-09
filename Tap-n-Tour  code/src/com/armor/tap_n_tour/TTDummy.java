package com.armor.tap_n_tour;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class TTDummy extends Activity{
	
	private SQLiteDatabase db;
	private Cursor cur_task;
	
	private EditText edit_location;
	private Button btn_add_location;
	
	private EditText edit_lid;
	
	private EditText edit_loc;
	private EditText edit_task;
	private Button btn_add_task;
	
	private TextView text_task;
	private Button btn_logoff;

	
	private String location;
	private String tasks;
	
	private int no_of_tasks;
	
	private ArrayList<String> task_list;
	private ArrayList<String> task_list_onRestart;
	
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_dummy);
		
		task_list = new ArrayList<String>();
		task_list_onRestart = new ArrayList<String>();
		
		edit_location = (EditText) findViewById(R.id.edit_location);
		btn_add_location = (Button) findViewById(R.id.btn_add_location);
		
		edit_lid = (EditText) findViewById(R.id.edit_lid);
		edit_loc = (EditText) findViewById(R.id.edit_loc);
		edit_task = (EditText) findViewById(R.id.edit_tasks);
		btn_add_task = (Button) findViewById(R.id.btn_add_task);
		
		ActionBar actionBar = getActionBar();
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);
		View cView = getLayoutInflater().inflate(R.layout.tt_action_bar, null);
		actionBar.setCustomView(cView);
		
		btn_logoff = (Button) findViewById(R.id.btn_logoff);
		text_task = (TextView) findViewById(R.id.text_task);
		
		
		db = openOrCreateDatabase("Tap-n-Tour.db",
				SQLiteDatabase.CREATE_IF_NECESSARY, null);
		db.setLocale(Locale.getDefault());
		db.setVersion(1);
		db.setLockingEnabled(true);
		
		location = "Create table if not exists Location(LID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, LocationName text)";
		db.execSQL(location);


		tasks = "Create table if not exists Tasks(SID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, LID int, LocationName text, Tasks text, Status text, AutoClosed text)";
		db.execSQL(tasks);
		
		btn_add_location.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String location = edit_location.getText().toString();
				
				ContentValues cv = new ContentValues();
				cv.put("LocationName", location);
				
				db.insert("Location", null, cv);
				
				edit_location.setText("");
			}
		});
		
		
		btn_add_task.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String lid = edit_lid.getText().toString();
				String location = edit_loc.getText().toString();
				String task = edit_task.getText().toString();
				
				ContentValues cv = new ContentValues();
				cv.put("LID", lid);
				cv.put("LocationName", location);
				cv.put("Tasks", task);
				cv.put("Status", "Open");
				cv.put("AutoClosed", "None");
				
				db.insert("Tasks", null, cv);
				
				edit_lid.setText("");
				edit_loc.setText("");
				edit_task.setText("");
			}
		});


		String str = "Select Tasks from Tasks where Status = '"+"Open"+"' ";
				
		cur_task = db.rawQuery(str, null);
		cur_task.moveToFirst();

		while (!cur_task.isAfterLast()) {
			String s_task = cur_task.getString(0).toString();			

			task_list.add(s_task);
			
			cur_task.moveToNext();

		}

		no_of_tasks = task_list.size();
		text_task.setText(String.valueOf(no_of_tasks));

		
		btn_logoff.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(getApplicationContext(), "Logoff", Toast.LENGTH_LONG).show();
			}
		});
		
		text_task.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(TTDummy.this,TTDoList.class));
				return false;
			}
		});
	}
	
		
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		String str = "Select Tasks from Tasks where Status = '"+"Open"+"' ";
		
		cur_task = db.rawQuery(str, null);
		cur_task.moveToFirst();

		while (!cur_task.isAfterLast()) {
			String s_task = cur_task.getString(0).toString();			

			task_list_onRestart.add(s_task);
			
			cur_task.moveToNext();

		}

		no_of_tasks = task_list_onRestart.size();
		text_task.setText(String.valueOf(no_of_tasks));
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
			startActivity(new Intent(TTDummy.this,TTViewSwipe.class));
			break;
				
		case R.id.menu_record:
			startActivity(new Intent(TTDummy.this,TTIncident.class));
			break;
		
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void showPopup() {

		View menuItemView = findViewById(R.id.menu_action);
		PopupMenu popup = new PopupMenu(TTDummy.this, menuItemView);
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				switch (item.getItemId()) {
				
				case R.id.menu_aboutus:
					startActivity(new Intent(TTDummy.this,TTAbout.class));
					break;
					
				case R.id.menu_settings:
					startActivity(new Intent(TTDummy.this,TTSettings.class));
					break;
					
				case R.id.menu_upload:
					startActivity(new Intent(TTDummy.this,TTViewSwipe.class));
					break;
					
				case R.id.menu_help:
					startActivity(new Intent(TTDummy.this,TTHelp.class));
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

