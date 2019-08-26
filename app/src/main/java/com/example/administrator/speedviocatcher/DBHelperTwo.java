package com.example.administrator.speedviocatcher;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.content.Context.MODE_PRIVATE;


public class DBHelperTwo extends SQLiteOpenHelper {
	/*public static String lisensya2= "";
	Context pref;*/
	public DBHelperTwo(Context c, String dbName, int dbVer)
	{
		super(c, dbName, null, dbVer);
	}

	public void onCreate(SQLiteDatabase db)
	{
		/*SharedPreferences lisensyas = pref.getSharedPreferences("MyApp", MODE_PRIVATE);
		lisensya2=lisensyas.getString("lisensya",  "UNKNOWN");*/
		//String sql= "CREATE TABLE "+lisensya2+"(vioid integer primary key not null, datetime text, license text, speed text, vehicle text)";
		String sql= "CREATE TABLE VioFile(vioid integer primary key not null, datetime text, license text, speed text, vehicle text)";
		db.execSQL(sql);
	}
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
	{
		String sql= "DROP TABLE IF EXISTS VioFile";
		db.execSQL(sql);
		onCreate(db);
	}
}



/*
public class DBHelper extends SQLiteOpenHelper{
	
	public DBHelper(Context c, String dbName, int dbVer)
	{		
		super(c, dbName, null, dbVer);
	}
	
	public void onCreate(SQLiteDatabase db)
	{
		String sql= "CREATE TABLE DriverViolation(viocount integer primary key not null, localdatetime text, locallicense text, localspeed text, localvehicle text)";
		
		db.execSQL(sql);
	}
	public void onUpgrade(SQLiteDatabase db, int oldVer, int newVer)
	{
		String sql= "DROP TABLE IF EXISTS DriverViolation";
		
		db.execSQL(sql);
		
		onCreate(db);
	}
}*/
