package com.example.leidong.myapp.database;
import android.app.ListActivity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.example.leidong.myapp.R;

public class MyItems extends ListActivity
{
	private static final int AddItem_ID = Menu.FIRST;
	private static final int DeleItem_ID = Menu.FIRST+2;
	private static final int ExitItem_ID = Menu.FIRST+3;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		//
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(ItemsProvider.CONTENT_URI);
        }
        //
        getListView().setOnCreateContextMenuListener(this);

        //
        Cursor cursor = managedQuery(getIntent().getData(), ItemColumn.PROJECTION, null, null,null);

        //
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
			android.R.layout.simple_list_item_2,
			cursor,
			new String[] {ItemColumn.NAME, ItemColumn.USERNAME},
			new int[] { android.R.id.text1, android.R.id.text2 });

        setListAdapter(adapter);
	}
	//
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        //
        menu.add(0, AddItem_ID, 0, R.string.add_item)
        	.setShortcut('3', 'a')
        	.setIcon(R.drawable.add);

        //�˳�����
        menu.add(0, ExitItem_ID, 0, R.string.exit)
    		.setShortcut('4', 'd')
    		.setIcon(R.drawable.exit);
        return true;
        
    }
    
    //
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
        case AddItem_ID:
            //
            startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
            return true;
        case ExitItem_ID:
        	//
        	this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    //
    //
    protected void onListItemClick(ListView l, View v, int position, long id)   
    {   
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);       
    	//
    	startActivity(new Intent(Intent.ACTION_VIEW, uri));       
    }   
    
    //
	public void onCreateContextMenu(ContextMenu menu, View view, ContextMenuInfo menuInfo)
	{
		AdapterView.AdapterContextMenuInfo info;
		try
		{
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		}
		catch (ClassCastException e)
		{
			return;
		}
		//
		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		if (cursor == null)
		{
			return;
		}

		menu.setHeaderTitle(cursor.getString(1));
		//
		menu.add(0, DeleItem_ID, 0, R.string.delete_item);
	}
    //
    @Override
    public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info;
		try
		{
			//
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		}
		catch (ClassCastException e)
		{
			return false;
		}

		switch (item.getItemId())
		{
			//
			case DeleItem_ID:
			{
				//
				Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
				getContentResolver().delete(noteUri, null, null);
				return true;
			}
		}
		return false;
	}
}