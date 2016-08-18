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
/**
 * Created by Lei Dong
 */
public class MyItems extends ListActivity
{
	private static final int AddItem_ID = Menu.FIRST;
	private static final int DeleItem_ID = Menu.FIRST+2;
	private static final int ExitItem_ID = Menu.FIRST+3;
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setDefaultKeyMode(DEFAULT_KEYS_SHORTCUT);
		//为Intent绑定数据
        Intent intent = getIntent();
        if (intent.getData() == null) {
            intent.setData(ItemsProvider.CONTENT_URI);
        }
        //设置某菜单长按监听器
        getListView().setOnCreateContextMenuListener(this);

        //查询，获得所有联系人的数据
        Cursor cursor = managedQuery(getIntent().getData(), ItemColumn.PROJECTION, null, null,null);

        //注册每个列表表示形式：条目名
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this,
			android.R.layout.simple_list_item_2,
			cursor,
			new String[] {ItemColumn.NAME},
			new int[] { android.R.id.text1});

        setListAdapter(adapter);
	}
	//添加菜单选项
    public boolean onCreateOptionsMenu(Menu menu) 
    {
        super.onCreateOptionsMenu(menu);
        //添加条目
        menu.add(0, AddItem_ID, 0, R.string.add_item)
        	.setShortcut('3', 'a')
        	.setIcon(R.drawable.add);

        //推出应用
        menu.add(0, ExitItem_ID, 0, R.string.exit)
    		.setShortcut('4', 'd')
    		.setIcon(R.drawable.exit);
        return true;
        
    }
    
    //处理菜单操作
    public boolean onOptionsItemSelected(MenuItem item) 
    {
        switch (item.getItemId()) 
        {
        case AddItem_ID:
            //添加条目
            startActivity(new Intent(Intent.ACTION_INSERT, getIntent().getData()));
            return true;
        case ExitItem_ID:
        	//退出应用
        	this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //动态菜单处理
    //单机的默认操作也可以在这里处理
    protected void onListItemClick(ListView l, View v, int position, long id)   
    {   
        Uri uri = ContentUris.withAppendedId(getIntent().getData(), id);       
    	//查看条目
    	startActivity(new Intent(Intent.ACTION_VIEW, uri));       
    }   
    
    //长按出发的菜单
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
		//得到长按的数据项
		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		if (cursor == null)
		{
			return;
		}

		menu.setHeaderTitle(cursor.getString(1));
		//添加删除菜单
		menu.add(0, DeleItem_ID, 0, R.string.delete_item);
	}
    //长按列表触发的函数
    @Override
    public boolean onContextItemSelected(MenuItem item)
	{
		AdapterView.AdapterContextMenuInfo info;
		try
		{
			//获得选中项的信息
			info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		}
		catch (ClassCastException e)
		{
			return false;
		}

		switch (item.getItemId())
		{
			//删除操作
			case DeleItem_ID:
			{
				//删除一条记录
				Uri noteUri = ContentUris.withAppendedId(getIntent().getData(), info.id);
				getContentResolver().delete(noteUri, null, null);
				return true;
			}
		}
		return false;
	}
}