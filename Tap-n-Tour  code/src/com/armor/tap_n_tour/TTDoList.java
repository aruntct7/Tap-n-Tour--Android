package com.armor.tap_n_tour;

import java.util.ArrayList;
import java.util.Locale;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class TTDoList extends SwipeListViewActivity {

	private SQLiteDatabase db;
	private Cursor cur_task;

	private ListView lv_location;
	private Spinner sp_location;
	private TextView text_task;
	private Button btn_logoff;

	private String location;
	private String tasks;
	private String selected_location;

	private int i = 0;
	private int no_of_tasks;

	private ArrayAdapter<String> mAdapter;
	private ArrayList<String> task_list;

	private ArrayAdapter<String> location_adapter;
	private ArrayAdapter<String> task_adapter;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_do_list);
		
		task_list = new ArrayList<String>();

		lv_location = (ListView) findViewById(R.id.lv_location);
		sp_location = (Spinner) findViewById(R.id.sp_location);

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

		// Select Place from Spinner
		String sel_location = "Select * from Location";
		Cursor cur_location = db.rawQuery(sel_location, null);
		String[] place = new String[cur_location.getCount()];
		cur_location.moveToFirst();

		while (!cur_location.isAfterLast()) {
			String s_place = cur_location.getString(1).toString();
			place[i] = s_place;

			i++;
			cur_location.moveToNext();

		}

		location_adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, place);
		location_adapter
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sp_location.setAdapter(location_adapter);
		
		
		
		
		sp_location.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub

				selected_location = sp_location.getItemAtPosition(arg2)
						.toString();
				
				task_list.clear();						

				String str = "Select Tasks from Tasks where LocationName = '"
						+ selected_location + "' AND Status = '"+ "Open" +"' ";
				cur_task = db.rawQuery(str, null);
				cur_task.moveToFirst();

				while (!cur_task.isAfterLast()) {
					String s_task = cur_task.getString(0).toString();					

					task_list.add(s_task);
					
					cur_task.moveToNext();

				}

				no_of_tasks = task_list.size();
				text_task.setText(String.valueOf(no_of_tasks));
				
				
			task_adapter = new ArrayAdapter<String>(TTDoList.this,
					android.R.layout.simple_list_item_1, task_list);
			lv_location.setAdapter(task_adapter);
						
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub

			}

		});		
		

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

				return false;
			}
		});

		
	}

	@Override
	public ListView getListView() {
		return this.lv_location;
	}

	@Override
	public void getSwipeItem(boolean isRight, int position) {
		
		String removed_item = this.task_list.get(position);				
		
		this.task_list.remove(position);
		mAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, task_list);
		lv_location.setAdapter(mAdapter);			
		
		String sel1 = "select * from Tasks where LocationName ='"+selected_location+"' AND Tasks = '"+removed_item+"' AND Status = '"+"Open"+"' ";
		Cursor cur_remove=db.rawQuery(sel1, null);
	
		if(cur_remove.moveToFirst())
		{
			ContentValues next = new ContentValues();
			next.put("Status", "Closed");
			next.put("AutoClosed", "No");
		
			db.update("Tasks", next, "Tasks=?", new String[] {removed_item});
			Toast.makeText(getApplication(), "Task Closed" , Toast.LENGTH_LONG).show();
			
		}
		
		no_of_tasks = no_of_tasks - 1;	
		text_task.setText(String.valueOf(no_of_tasks));
				
	}

	@Override
	public void onItemClickListener(ListAdapter adapter, int position) {
		Toast.makeText(this, "Give Status" + position,
				Toast.LENGTH_SHORT).show();
		
		giveStatus();
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
			startActivity(new Intent(TTDoList.this, TTViewSwipe.class));
			break;

		case R.id.menu_record:
			startActivity(new Intent(TTDoList.this, TTIncident.class));
			break;

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void showPopup() {

		View menuItemView = findViewById(R.id.menu_action);
		PopupMenu popup = new PopupMenu(TTDoList.this, menuItemView);
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				switch (item.getItemId()) {

				case R.id.menu_aboutus:
					startActivity(new Intent(TTDoList.this, TTAbout.class));
					break;

				case R.id.menu_upload:
					startActivity(new Intent(TTDoList.this, TTViewSwipe.class));
					break;					

				case R.id.menu_settings:
					startActivity(new Intent(TTDoList.this, TTSettings.class));
					break;

				case R.id.menu_help:
					startActivity(new Intent(TTDoList.this, TTHelp.class));
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
	
	private void giveStatus()
	{
		AlertDialog.Builder ad = new AlertDialog.Builder(this);
		ad.setTitle("Status");
		
		final LinearLayout li = new LinearLayout(this);
		li.setOrientation(1);
		
		final Spinner sp1 = new Spinner(this);
	    @SuppressWarnings("rawtypes")
		ArrayAdapter spinnerArrayAdapter = new ArrayAdapter(this,
	        android.R.layout.simple_spinner_dropdown_item,
	            new String[] { "Cannot Do", "Workers Needed", "Equipment Reqd" });
	    sp1.setAdapter(spinnerArrayAdapter);

		
		final EditText et2 = new EditText(this);
		et2.setHint("Give Reason");
		et2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
		et2.setWidth(75);
		
		
		li.addView(sp1);
		li.addView(et2);
		
		ad.setView(li);
		
		ad.setPositiveButton("Done", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
				Toast.makeText(getApplicationContext(), "Status Given", Toast.LENGTH_LONG).show();
				
			}
		});
		
		ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {


				dialog.cancel();
				
			}
		});
		ad.show();
	}
	
}