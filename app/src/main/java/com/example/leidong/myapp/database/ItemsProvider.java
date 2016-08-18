package com.example.leidong.myapp.database;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ItemsProvider extends ContentProvider
{
	//
	private static final String TAG= "ItemsProvider";
	//
	private DBHelper dbHelper;
	//
	private SQLiteDatabase itemsDB;
	//
	public static final String AUTHORITY = "com.example.leidong.provider.ItemsProvider";
	public static final String ITEMS_TABLE = "items";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+ ITEMS_TABLE);
	
	//
	public static final int CONTACTS = 1;
	public static final int CONTACT_ID = 2;
	private static final UriMatcher uriMatcher;	
	static
	{
		//
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//
		uriMatcher.addURI(AUTHORITY,"items",CONTACTS);
		//
		uriMatcher.addURI(AUTHORITY,"items/#",CONTACT_ID);
	}
	//
	@Override
	public boolean onCreate()
	{
		dbHelper = new DBHelper(getContext());
		//
		itemsDB = dbHelper.getWritableDatabase();
		return (itemsDB == null) ? false : true;
	}
	
	//
	@Override
	public int delete(Uri uri, String where, String[] selectionArgs)
	{
		int count;
		switch (uriMatcher.match(uri))
		{
			//
			case CONTACTS:
				count = itemsDB.delete(ITEMS_TABLE, where, selectionArgs);
				break;
			case CONTACT_ID:
				//
				String contactID = uri.getPathSegments().get(1);
				//
				count = itemsDB.delete(ITEMS_TABLE,
										  ItemColumn._ID
										  + "=" + contactID 
										  + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""),
										  selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	//
	public String getType(Uri uri)
	{
		switch (uriMatcher.match(uri))
		{
			//
			case CONTACTS:
				return "vnd.android.cursor.dir/vnd.leidong.android.mycontacts";
			//
			case CONTACT_ID:
				return "vnd.android.cursor.item/vnd.leidong.android.mycontacts";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
	
	//
	public Uri insert(Uri uri, ContentValues initialValues)
	{
		//
		if (uriMatcher.match(uri) != CONTACTS)
		{
			throw new IllegalArgumentException("Unknown URI " + uri);
		}
		ContentValues values;
		if (initialValues != null)
		{
			values = new ContentValues(initialValues);
			Log.e(TAG + "insert", "initialValues is not null");
		}
		else
		{
			values = new ContentValues();
		}
		//
		if (values.containsKey(ItemColumn.NAME) == false)
		{
			values.put(ItemColumn.NAME, "");
		}
		if (values.containsKey(ItemColumn.USERNAME) == false)
		{
			values.put(ItemColumn.USERNAME, "");
		}
		if (values.containsKey(ItemColumn.PASSWORD) == false)
		{
			values.put(ItemColumn.PASSWORD, "");
		}
		if (values.containsKey(ItemColumn.URL) == false)
		{
			values.put(ItemColumn.URL, "");
		}
		if (values.containsKey(ItemColumn.INFO) == false)
		{
			values.put(ItemColumn.INFO, "");
		}
		Log.e(TAG + "insert", values.toString());
		//
		long rowId = itemsDB.insert(ITEMS_TABLE, null, values);
		if (rowId > 0)
		{
			//
			Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			//
			getContext().getContentResolver().notifyChange(noteUri, null);
			Log.e(TAG + "insert", noteUri.toString());
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}
	
	//
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		Log.e(TAG + ":query", " in Query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//
		qb.setTables(ITEMS_TABLE);

		switch (uriMatcher.match(uri))
		{
			//
			case CONTACT_ID:
				qb.appendWhere(ItemColumn._ID + "=" + uri.getPathSegments().get(1));
				break;
			default:
				break;
		}
		//
		Cursor c = qb.query(itemsDB, projection, selection, selectionArgs, null, null, sortOrder);
		//
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	//
	public int update(Uri uri, ContentValues values, String where, String[] selectionArgs)
	{
		int count;
		Log.e(TAG + "update", values.toString());
		Log.e(TAG + "update", uri.toString());
		/*Log.e(TAG + "update :match", "" + uriMatcher.match(uri));*/
		switch (uriMatcher.match(uri))
		{
			//
			case CONTACTS:
				Log.e(TAG + "update", CONTACTS + "");
				count = itemsDB.update(ITEMS_TABLE, values, where, selectionArgs);
				break;
			//
			case CONTACT_ID:
				String contactID = uri.getPathSegments().get(1);
				Log.e(TAG + "update", contactID + "");
				count = itemsDB.update(ITEMS_TABLE, values, ItemColumn._ID + "=" + contactID
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		//
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}