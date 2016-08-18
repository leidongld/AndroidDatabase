package com.example.leidong.myapp.database;

import android.provider.BaseColumns;

//
public class ItemColumn implements BaseColumns
{
	public ItemColumn()
	{
	}
	//
	public static final String NAME = "name";				//
	public static final String USERNAME = "username";//
	public static final String PASSWORD = "password";	//
	public static final String URL = "url";		//
	public static final String INFO = "info";			//
	//
	public static final int _ID_COLUMN = 0;
	public static final int NAME_COLUMN = 1;
	public static final int USERNAME_COLUMN = 2;
	public static final int PASSWORD_COLUMN = 3;
	public static final int URL_COLUMN = 4;
	public static final int INFO_COLUMN = 5;

	//
	public static final String[] PROJECTION ={
		    _ID,
		    NAME,
			USERNAME,
			PASSWORD,
			URL,
			INFO
	};
}