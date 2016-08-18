package com.example.leidong.myapp.database;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.leidong.myapp.R;

public class ItemView extends Activity
{
	//
	private TextView mTextViewName;
	//
	private TextView mTextViewUsername;
	//
	private TextView mTextViewPassword;
	//
	private TextView mTextViewURL;
	//
	private TextView mTextViewInfo;
	
    private Cursor mCursor;
    private Uri mUri;
    //
    private static final int REVERT_ID = Menu.FIRST;
    private static final int DELETE_ID = Menu.FIRST + 1;
    private static final int EDITOR_ID = Menu.FIRST + 2;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		mUri = getIntent().getData();		
		this.setContentView(R.layout.database_item_view);
		//
		mTextViewName = (TextView) findViewById(R.id.TextView_Name);
		mTextViewUsername = (TextView) findViewById(R.id.TextView_Mobile);
		mTextViewPassword = (TextView) findViewById(R.id.TextView_Home);
		mTextViewURL = (TextView) findViewById(R.id.TextView_Address);
		mTextViewInfo = (TextView) findViewById(R.id.TextView_Email);
		
	    //
        mCursor = managedQuery(mUri, ItemColumn.PROJECTION, null, null, null);
        mCursor.moveToFirst();
        if (mCursor != null)
		{
			//
			mCursor.moveToFirst();
			
			mTextViewName.setText(mCursor.getString(ItemColumn.NAME_COLUMN));
			mTextViewUsername.setText(mCursor.getString(ItemColumn.USERNAME_COLUMN));
			mTextViewPassword.setText(mCursor.getString(ItemColumn.PASSWORD_COLUMN));
			mTextViewURL.setText(mCursor.getString(ItemColumn.URL_COLUMN));
			mTextViewInfo.setText(mCursor.getString(ItemColumn.INFO_COLUMN));
		}
		else
		{
			setTitle("错误信息");
		}
	}
	//
    public boolean onCreateOptionsMenu(Menu menu)
	{
		super.onCreateOptionsMenu(menu);
		//
		menu.add(0, REVERT_ID, 0, R.string.revert).setShortcut('0', 'r').setIcon(R.drawable.listuser);
		//
		menu.add(0, DELETE_ID, 0, R.string.delete_item).setShortcut('0', 'd').setIcon(R.drawable.remove);
		//
		menu.add(0, EDITOR_ID, 0, R.string.editor_item).setShortcut('0', 'd').setIcon(R.drawable.edituser);
		return true;
	}
    
    public boolean onOptionsItemSelected(MenuItem item)
	{
		switch (item.getItemId())
		{
			//
			case DELETE_ID:
				deleteContact();
				finish();
				break;
			//
			case REVERT_ID:
				setResult(RESULT_CANCELED);
				finish();
				break;
			case EDITOR_ID:
			//
				startActivity(new Intent(Intent.ACTION_EDIT, mUri)); 
				finish();
				break;
		}
		return super.onOptionsItemSelected(item);
	}

	//
	private void deleteContact()
	{
		if (mCursor != null)
		{
			mCursor.close();
			mCursor = null;
			getContentResolver().delete(mUri, null, null);
			setResult(RESULT_CANCELED);
		}
	}
}