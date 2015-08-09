package com.armor.tap_n_tour;

import android.os.Bundle;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Typeface;
import android.view.Menu;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TTAdmin extends Activity {
	
	private TextView text_ip;
	private EditText edit_ip;
	private Button btn_save;
	private Button btn_cancel;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tt_admin);
		
		text_ip = (TextView) findViewById(R.id.text_ip);
		edit_ip = (EditText) findViewById(R.id.edit_ip);
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_cancel = (Button) findViewById(R.id.btn_cancel);
		
		Typeface tf = Typeface.createFromAsset(getAssets(), "font/Roboto-Black.ttf");
		text_ip.setTypeface(tf);
		
		ActionBar actionBar = getActionBar();
		actionBar.show();
		actionBar.setDisplayHomeAsUpEnabled(true);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.tt_admin, menu);
		return true;
	}

}
