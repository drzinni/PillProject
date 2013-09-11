package com.example.pills.here;

import java.util.ArrayList;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.utdproject.pills.here.R;

public class ListPills extends Activity {

	class pillOnClickListener implements OnItemClickListener{
		public Context passedContext;
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long id) {
			PALdb paldb=new PALdb(passedContext);
	        SQLiteDatabase db = paldb.getReadableDatabase();
	        TextView tex=(TextView)arg0.getChildAt(position);
	        String pillName=(String) tex.getText();
	        Cursor cursor=db.query(PALdb.DATABASE_PILL_TABLE, new String[]{"rowID","pillName"}, "pillName=?", new String[]{pillName}, null, null, null);
	        if(cursor.moveToFirst())
	        {
	        	Intent intent=new Intent(passedContext,AddPill.class);
	        	intent.putExtra("rowID", cursor.getLong(cursor.getColumnIndex("rowID")));
	        	intent.putExtra("edit", true);
	        	
	        	startActivity(intent);
	        }else{
	        	//error
	        }
	        cursor.close();
        	db.close();
		}
	
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_list_pills);
		ListView v=(ListView)findViewById(R.id.ListPillsListView);
        PALdb paldb=new PALdb((Context)this);
        SQLiteDatabase db = paldb.getReadableDatabase();
        Cursor cur=db.query(PALdb.DATABASE_PILL_TABLE, new String[]{"pillName"}, null, null, null, null, "rowID",null);
        ArrayList<String> items=new ArrayList<String>();
        
        while(cur.moveToNext())
        {
        	items.add(cur.getString(cur.getColumnIndex("pillName")));
        }
        cur.close();
        db.close();
        if(items.size()==0){
        	Toast.makeText(this, "No Pills Scheduled", 2).show();
        	Intent fail=new Intent(this,GeneralSettings.class);
        	fail.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(fail);
        	return;
        }
        ArrayAdapter<String> listItems=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,items.toArray(new String[items.size()]));
        v.setAdapter(listItems);
        pillOnClickListener listen=new pillOnClickListener();
        listen.passedContext=this;
        v.setOnItemClickListener(listen);
        
	}
	public void goHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
