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
/**
 * Created by Lei Dong
 * 为数据库提供操作类
 *
 */
public class ItemsProvider extends ContentProvider
{
	//标签
	private static final String TAG= "ItemsProvider";
	//数据库帮助类
	private DBHelper dbHelper;
	//数据库
	private SQLiteDatabase itemsDB;
	//数据库操作uri地址
	public static final String AUTHORITY = "com.example.leidong.provider.ItemsProvider";
	public static final String ITEMS_TABLE = "items";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/"+ ITEMS_TABLE);
	
	//自定义的类型
	public static final int ITEMS = 1;
	public static final int ITEM_ID = 2;
	private static final UriMatcher uriMatcher;	
	static
	{
		//没有匹配的信息
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		//全部条目的信息
		uriMatcher.addURI(AUTHORITY,"items", ITEMS);
		//单独一个条目的信息
		uriMatcher.addURI(AUTHORITY,"items/#", ITEM_ID);
	}
	//取得数据库
	@Override
	public boolean onCreate()
	{
		dbHelper = new DBHelper(getContext());
		//执行创建数据库
		itemsDB = dbHelper.getWritableDatabase();
		return (itemsDB == null) ? false : true;
	}
	
	//删除指定的数据列
	@Override
	public int delete(Uri uri, String where, String[] selectionArgs)
	{
		int count;
		switch (uriMatcher.match(uri))
		{
			//删除满足where条件的行
			case ITEMS:
				count = itemsDB.delete(ITEMS_TABLE, where, selectionArgs);
				break;
			case ITEM_ID:
				//取得条目的ID信息
				String itemID = uri.getPathSegments().get(1);
				//删除满足where条件，并且ID值为itemID的记录
				count = itemsDB.delete(ITEMS_TABLE,
										  ItemColumn._ID
										  + "=" + itemID
										  + (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""),
										  selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
	
	//URI类型转换
	public String getType(Uri uri)
	{
		switch (uriMatcher.match(uri))
		{
			//所有条目
			case ITEMS:
				return "vnd.android.cursor.dir/vnd.leidong.android.myitems";
			//指定条目
			case ITEM_ID:
				return "vnd.android.cursor.item/vnd.leidong.android.myitems";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
	
	//插入数据
	public Uri insert(Uri uri, ContentValues initialValues)
	{
		//判断uri地址是否合法
		if (uriMatcher.match(uri) != ITEMS)
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
		//如果对应的名称没有值则是顶默认值为""
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
		//插入数据
		long rowId = itemsDB.insert(ITEMS_TABLE, null, values);
		if (rowId > 0)
		{
			//将id值加入uri地址中
			Uri noteUri = ContentUris.withAppendedId(CONTENT_URI, rowId);
			//通知改变
			getContext().getContentResolver().notifyChange(noteUri, null);
			Log.e(TAG + "insert", noteUri.toString());
			return noteUri;
		}
		throw new SQLException("Failed to insert row into " + uri);
	}
	
	//查询数据
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder)
	{
		Log.e(TAG + ":query", " in Query");
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//设置要查询的数据表
		qb.setTables(ITEMS_TABLE);

		switch (uriMatcher.match(uri))
		{
			//构建where语句，定位到指定id值得列
			case ITEM_ID:
				qb.appendWhere(ItemColumn._ID + "=" + uri.getPathSegments().get(1));
				break;
			default:
				break;
		}
		//查询
		Cursor c = qb.query(itemsDB, projection, selection, selectionArgs, null, null, sortOrder);
		//设置通知改变uri
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}
	
	//更新数据库
	public int update(Uri uri, ContentValues values, String where, String[] selectionArgs)
	{
		int count;
		Log.e(TAG + "update", values.toString());
		Log.e(TAG + "update", uri.toString());
		/*Log.e(TAG + "update :match", "" + uriMatcher.match(uri));*/
		switch (uriMatcher.match(uri))
		{
			//根据where条件批量进行更新
			case ITEMS:
				Log.e(TAG + "update", ITEMS + "");
				count = itemsDB.update(ITEMS_TABLE, values, where, selectionArgs);
				break;
			//更新指定行
			case ITEM_ID:
				String itemID = uri.getPathSegments().get(1);
				Log.e(TAG + "update", itemID + "");
				count = itemsDB.update(ITEMS_TABLE, values, ItemColumn._ID + "=" + itemID
						+ (!TextUtils.isEmpty(where) ? " AND (" + where + ")" : ""), selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
		//通知更改
		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}
}