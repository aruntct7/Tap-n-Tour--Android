package com.armor.tap_n_tour;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Dbhelper extends SQLiteOpenHelper{

 public static String DATABASE_NAME="ttgps";
 public static String Table_Name="location";
 public static String Latitude="latitude";
 public static String Longitude="longitude";
 public static String Gps_Time="time";
 private String TAG;
 
 public Dbhelper(Context context) {
  super(context, DATABASE_NAME, null, 1);
  // TODO Auto-generated constructor stub
  
  
  
 }

 @Override
 public void onCreate(SQLiteDatabase db) {
  // TODO Auto-generated method stub
  
  String query="CREATE TABLE "+Table_Name+" ( _id integer primary key autoincrement, "+Latitude+" text not null, "+Longitude+" text not null, "+Gps_Time+ " text not null )";
  
  db.execSQL(query);
  
  Log.d(TAG, "query executed");
  
 }

 
 public void insertEmpData(ContentValues cv)
 {
  SQLiteDatabase db=getWritableDatabase();
  
  db.insert(Table_Name, null, cv);
 }
 
 public Cursor showData()
 {
  SQLiteDatabase db=getReadableDatabase();
  
  return db.rawQuery("SELECT * FROM "+Table_Name, null);
  
 }
 
 @Override
 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
  // TODO Auto-generated method stub
  
 }
 

}
