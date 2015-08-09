package com.armor.tap_n_tour;

import android.app.ActionBar;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class TTPreviewSwipe extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_preview_swipe);
		
		
		TabHost tabHost = getTabHost();
        
        // Tab for Photos
        TabSpec photospec = tabHost.newTabSpec("Photos");
        // setting Title and Icon for the Tab
        photospec.setIndicator("Photos", getResources().getDrawable(R.drawable.icon_photos_tab));
        Intent photosIntent = new Intent(this, TTPhotos.class);
        photospec.setContent(photosIntent);
         
        // Tab for Songs
        TabSpec songspec = tabHost.newTabSpec("Audios");        
        songspec.setIndicator("Audios", getResources().getDrawable(R.drawable.icon_audios_tab));
        Intent songsIntent = new Intent(this, TTAudios.class);
        songspec.setContent(songsIntent);
         
        // Tab for Videos
        TabSpec videospec = tabHost.newTabSpec("Videos");
        videospec.setIndicator("Videos", getResources().getDrawable(R.drawable.icon_videos_tab));
        Intent videosIntent = new Intent(this, TTVideos.class);
        videospec.setContent(videosIntent);
         
        // Adding all TabSpec to TabHost
        tabHost.addTab(photospec); // Adding photos tab
        tabHost.addTab(songspec); // Adding songs tab
        tabHost.addTab(videospec); // Adding videos tab
		
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
				
				Toast.makeText(getApplicationContext(), "Tasks", Toast.LENGTH_LONG).show();
				return false;
			}
		});
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.

		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_action:
			showPopup();
			break;
			
		case R.id.menu_previous:
			finish();
			break;		
			
		case R.id.menu_uploads:
			Toast.makeText(getApplicationContext(), "Uploads", Toast.LENGTH_LONG).show();
			break;
				
		case R.id.menu_record:
			Toast.makeText(getApplicationContext(), "Incident", Toast.LENGTH_LONG).show();
			break;
		
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void showPopup() {

		View menuItemView = findViewById(R.id.menu_action);
		PopupMenu popup = new PopupMenu(TTPreviewSwipe.this, menuItemView);
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				
				switch (item.getItemId()) {
				
				case R.id.menu_aboutus:
					Toast.makeText(getApplicationContext(), "About Us", Toast.LENGTH_LONG).show();
					break;
					
				case R.id.menu_settings:
					Toast.makeText(getApplicationContext(), "Settings", Toast.LENGTH_LONG).show();
					break;
					
				case R.id.menu_upload:
					Toast.makeText(getApplicationContext(), "Uploads", Toast.LENGTH_LONG).show();
					break;
					
				case R.id.menu_help:
					Toast.makeText(getApplicationContext(), "Help", Toast.LENGTH_LONG).show();
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
