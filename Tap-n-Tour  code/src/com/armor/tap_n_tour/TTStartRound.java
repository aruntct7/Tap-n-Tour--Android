package com.armor.tap_n_tour;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupMenu.OnMenuItemClickListener;

public class TTStartRound extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_start_round);
		
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
				signOut();
			}
		});
		text_task.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				
				startActivity(new Intent(TTStartRound.this,TTDoList.class));
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
			startActivity(new Intent(TTStartRound.this,TTViewSwipe.class));
			break;
				
		case R.id.menu_record:
			startActivity(new Intent(TTStartRound.this,TTIncident.class));
			break;
		
			
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void showPopup() {

		View menuItemView = findViewById(R.id.menu_action);
		PopupMenu popup = new PopupMenu(TTStartRound.this, menuItemView);
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {

				switch (item.getItemId()) {
				
				case R.id.menu_upload:
					startActivity(new Intent(TTStartRound.this,TTViewSwipe.class));
					break;
				case R.id.menu_aboutus:
					startActivity(new Intent(TTStartRound.this,TTAbout.class));
					break;
					
				case R.id.menu_settings:
					startActivity(new Intent(TTStartRound.this,TTSettings.class));
					break;
					
				case R.id.menu_help:
					startActivity(new Intent(TTStartRound.this,TTHelp.class));
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

		ad.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						startActivity(new Intent(TTStartRound.this,
								TTDoList.class));

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


}
