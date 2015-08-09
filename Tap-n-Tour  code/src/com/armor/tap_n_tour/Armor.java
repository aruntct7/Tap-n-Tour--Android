package com.armor.tap_n_tour;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Armor {
	// TTGps gps = new TTGps(this);
	// Context context;

	public boolean haveNetworkConnection(Context context) {
		boolean haveConnectedWifi = false;
		boolean haveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo) {
			if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
				if (ni.isConnected()) {
					haveConnectedWifi = true;
					Log.v("WIFI CONNECTION ", "AVAILABLE");
				} else {
					Log.v("WIFI CONNECTION ", "NOT AVAILABLE");
				}
			}
			if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
				if (ni.isConnected()) {
					haveConnectedMobile = true;
					Log.v("MOBILE INTERNET CONNECTION ", "AVAILABLE");
				} else {
					Log.v("MOBILE INTERNET CONNECTION ", "NOT AVAILABLE");
				}
			}
		}
		return haveConnectedWifi || haveConnectedMobile;
	}

	public boolean checkurl() throws IOException {

		try {
			URL url = new URL("http://192.168.1.14/TapnTour/");
			InputStream i = null;

			try {
				i = url.openStream();
			} catch (UnknownHostException ex) {
				// System.out.println("THIS URL IS NOT VALID");
			}

			if (i != null) {
				System.out.println("Its working");
				return true;
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return false;

	}

	/*
	 * void tst(String variable) { Toast.makeText(context, variable,
	 * Toast.LENGTH_LONG).show(); }
	 */

}
