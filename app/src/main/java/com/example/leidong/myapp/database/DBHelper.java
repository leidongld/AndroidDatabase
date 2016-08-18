package com.example.leidong.myapp.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lei Dong
 */
public class DBHelper extends SQLiteOpenHelper
{
	//数据库的名称
	public static final String DATABASE_NAME = "items.db";
	//数据库的版本
	public static final int DATABASE_VERSION = 2;	
	//表名称
	public static final String ITEMS_TABLE = "items";
	//创建表
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
	//创建数据库
	public void onCreate(SQLiteDatabase db)
	{
		db.execSQL(DATABASE_CREATE);
	}
	//更新数据库
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{
		db.execSQL("DROP TABLE IF EXISTS " + ITEMS_TABLE);
		onCreate(db);
	}
}