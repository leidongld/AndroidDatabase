package com.example.leidong.myapp.database;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.example.leidong.myapp.R;

public class ItemEditor extends Activity
{
	//
	private static final int STATE_EDIT = 0;
    private static final int STATE_INSERT = 1;
    //
    private static final int REVERT_ID = Menu.FIRST;
    private static final int DISCARD_ID = Menu.FIRST + 1;
    private static final int DELETE_ID = Menu.FIRST + 2;
    
    private Cursor mCursor;
    private int mState;		//
    private Uri mUri;
    //
    private EditText nameText;
    private EditText usernameText;
    private EditText passwordText;
    private EditText urlText;
    private EditText infoText;
    //
    private Button okButton;
    private Button cancelButton;
    
	public void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);

		final Intent intent = getIntent();
		final String action = intent.getAction();
		//
		//
		if (Intent.ACTION_EDIT.equals(action))
		{
			mState = STATE_EDIT;
			mUri = intent.getData();
		}
		else if (Intent.ACTION_INSERT.equals(action))
		{
			//
			mState = STATE_INSERT;
			mUri = getContentResolver().insert(intent.getData(), null);
			if (mUri == null)
			{
				finish();
				return;
			}
			setResult(RESULT_OK, (new Intent()).setAction(mUri.toString()));
		}
		//
		else
		{
			finish();
			return;
		}        
        setContentView(R.layout.database_item_add);
        //
        nameText = (EditText) findViewById(R.id.EditText01);
        usernameText = (EditText) findViewById(R.id.EditText02);
        passwordText = (EditText) findViewById(R.id.EditText03);
        urlText = (EditText) findViewById(R.id.EditText04);
        infoText = (EditText) findViewById(R.id.EditText05);
        //
        okButton = (Button)findViewById(R.id.Button01);
        cancelButton = (Button)findViewById(R.id.Button02);
        //
        okButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				String text = nameText.getText().toString();
				if(text.length() == 0)
				{
					//
					setResult(RESULT_CANCELED);
					deleteContact();
					finish();
				}
				else
				{
					//
					updateContact();
				}
			}       	
        });
        //
        cancelButton.setOnClickListener(new OnClickListener()
        {
			public void onClick(View v) 
			{
				//
				setResult(RESULT_CANCELED);
				deleteContact();
				finish();

			}
        });
        //
        mCursor = managedQuery(mUri, ItemColumn.PROJECTION, null, null, null);
        mCursor.moveToFirst();
        if (mCursor != null)
		{
			//
			mCursor.moveToFirst();
			if (mState == STATE_EDIT)
			{
				setTitle(getText(R.string.editor_item));
			}
			else if (mState == STATE_INSERT)
			{
				setTitle(getText(R.string.add_item));
			}
			String name = mCursor.getString(ItemColumn.NAME_COLUMN);
			String moblie = mCursor.getString(ItemColumn.USERNAME_COLUMN);
			String home = mCursor.getString(ItemColumn.PASSWORD_COLUMN);
			String address = mCursor.getString(ItemColumn.URL_COLUMN);
			String email = mCursor.getString(ItemColumn.INFO_COLUMN);
			//��ʾ��Ϣ
			nameText.setText(name);
			usernameText.setText(moblie);
			passwordText.setText(home);
			urlText.setText(address);
			infoText.setText(email);
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
        if (mState == STATE_EDIT) 
        {
        	//
            menu.add(0, REVERT_ID, 0, R.string.revert)
                    .setShortcut('0', 'r')
                    .setIcon(R.drawable.listuser);
            //
            menu.add(0, DELETE_ID, 0, R.string.delete_item)
            .setShortcut('0', 'f')
            .setIcon(R.drawable.remove);
        } 
        else 
        {
        	//���ذ�ť
            menu.add(0, DISCARD_ID, 0, R.string.revert)
                    .setShortcut('0', 'd')
                    .setIcon(R.drawable.listuser);
        }
        return true;
    }
    //
	@Override
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
        case DISCARD_ID:
        	cancelContact();
        	finish();
            break;
        //
        case REVERT_ID:
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
            nameText.setText("");
        }
	}
	//
	private void cancelContact() 
	{
		if (mCursor != null) 
		{
			deleteContact();
        }
        setResult(RESULT_CANCELED);
        finish();
	}
	//
	private void updateContact() 
	{
		if (mCursor != null) 
		{
			mCursor.close();
            mCursor = null;
            ContentValues values = new ContentValues();
			values.put(ItemColumn.NAME, nameText.getText().toString());
			values.put(ItemColumn.USERNAME, usernameText.getText().toString());
			values.put(ItemColumn.PASSWORD, passwordText.getText().toString());
			values.put(ItemColumn.URL, urlText.getText().toString());
			values.put(ItemColumn.INFO, infoText.getText().toString());
			//
            getContentResolver().update(mUri, values, null, null);
        }
        setResult(RESULT_CANCELED);
        finish();
	}
}