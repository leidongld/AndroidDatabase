package com.example.leidong.myapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper
{
	//
	public static final String DATABASE_NAME = "items.db";
	//
	public static final int DATABASE_VERSION = 2;	
	//
	public static final String ITEMS_TABLE = "items";
	//
	private static final String DATABASE_CREATE = 
		"CREATE TABLE " + ITEMS_TABLE +" ("
		+ ItemColumn._ID+" integer primary key autoincrement,"
		+ ItemColumn.NAME+" text,"
		+ ItemColumn.USERNAME +" text,"
		+ ItemColumn.PASSWORD +" text,"
		+ ItemColumn.URL +" text,"
		+ ItemColumn.INFO +" text )";
	public DBHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	//
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}
	//
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
		onCreate(db);
	}
}