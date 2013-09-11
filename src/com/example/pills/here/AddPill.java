package com.example.pills.here;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.utdproject.pills.here.R;


public class AddPill extends Activity implements DialogListener{
	Bitmap photo = null;
	final int PILL_PICTURE_REQUEST = 777;
	String CurPillName;
	String CurPillLocation;
	String CurPillInstructions;
	ImageView imageView;
	String CurPillColor;
	String CurPillDosage;
	ArrayList<Integer> CurPillDays;
	ArrayList<Integer> CurPillTime;
	ArrayList<Long> CurPillRowIds;
	int WorkingCurPillDays;
	int WorkingCurPillTime;
	long WorkingCurPillRowId;
	int itemCount;
	long CurPillRowID;
	class AddPillOnItemClickListener implements OnItemClickListener
	{
		public Context passedContext;
		public Activity passedActivity;
		DialogListener dialogListen;
		
		public void onItemClick(AdapterView<?> arg0, View view, final int position,
				long id) {
			AlertDialog.Builder builder=new AlertDialog.Builder(passedContext);
			
			LayoutInflater inflate=passedActivity.getLayoutInflater();
			builder.setPositiveButton(R.string.dialog_set, new DialogInterface.OnClickListener()
				{
					public void onClick(DialogInterface dialog, int which) {
						dialogListen.onDialogPositiveClick(dialog,position);
					}
				}
			);
			
			builder.setNegativeButton(R.string.dialog_revert, new DialogInterface.OnClickListener()
				{

					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				
				}
			);
			if(position==0)
			{
				builder.setTitle(R.string.dialog_pilledit_pillname);
				View viewDia=inflate.inflate(R.layout.dialog_pilledit_pillname,null);
					((EditText)viewDia.findViewById(R.id.dialog_pilledit_pillname)).setText(CurPillName);
				builder.setView(viewDia);
				Dialog d=builder.create();
				d.show();
			}else
			if(position==1)
			{
				builder.setTitle(R.string.dialog_pilledit_pilllocation);
				View viewDia=inflate.inflate(R.layout.dialog_pilledit_pilllocation,null);
					((EditText)viewDia.findViewById(R.id.dialog_pilledit_pilllocation)).setText(CurPillLocation);
				builder.setView(viewDia);
				Dialog d=builder.create();
				d.show();
			}else
			if(position==2)
			{
				builder.setTitle(R.string.dialog_pilledit_pillinstructions);
				View viewDia=inflate.inflate(R.layout.dialog_pilledit_pillinstructions,null);
					((EditText)viewDia.findViewById(R.id.dialog_pilledit_pillinstructions)).setText(CurPillInstructions);
				builder.setView(viewDia);
				Dialog d=builder.create();
				d.show();
			}else
			if(position==3)
			{
				builder.setTitle(R.string.dialog_pilledit_pillcolor);
				View viewDia=inflate.inflate(R.layout.dialog_pilledit_pillcolor,null);
					((EditText)viewDia.findViewById(R.id.dialog_pilledit_pillcolor)).setText(CurPillColor);
				builder.setView(viewDia);
				Dialog d=builder.create();
				d.show();
			}else
			if(position==4)
			{
				builder.setTitle(R.string.dialog_pilledit_pilldosage);
				View viewDia=inflate.inflate(R.layout.dialog_pilledit_pilldosage,null);
						((EditText)viewDia.findViewById(R.id.dialog_pilledit_pilldosage)).setText(CurPillDosage);
				builder.setView(viewDia);
				Dialog d=builder.create();
				d.show();
			}else
			if(position==5)
			{
				Intent in = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		        startActivityForResult(in, PILL_PICTURE_REQUEST);
		        return;
			}else
			if(position>=6)
			{
				if((position)==itemCount)//create new
				{
					WorkingCurPillDays=0;
					WorkingCurPillTime=12*60*60;//default to 12 noon
					WorkingCurPillRowId=-1;
					displayDaysDialog(position);
				}else//modify existing
				{
					WorkingCurPillDays=CurPillDays.get(position-6);
					WorkingCurPillTime=CurPillTime.get(position-6);
					WorkingCurPillRowId=CurPillRowIds.get(position-6);
					displayDaysDialog(position);
				}
			}
			
			
		}
		//end onItemClick for AddPillOnItemClickListener

		protected void displayDaysDialog(final int i) {
			AlertDialog.Builder build=new AlertDialog.Builder(passedContext);
			build.setTitle(R.string.dialog_pilledit_editdays);
			LayoutInflater inflate=passedActivity.getLayoutInflater();
			View v=inflate.inflate(R.layout.dialog_pilledit_scheddays, null);
			((CheckBox)v.findViewById(R.id.pillsched_daysunday)).setChecked((WorkingCurPillDays&1)==1);
			((CheckBox)v.findViewById(R.id.pillsched_daymonday)).setChecked((WorkingCurPillDays&2)==2);
			((CheckBox)v.findViewById(R.id.pillsched_daytuesday)).setChecked((WorkingCurPillDays&4)==4);
			((CheckBox)v.findViewById(R.id.pillsched_daywednesday)).setChecked((WorkingCurPillDays&8)==8);
			((CheckBox)v.findViewById(R.id.pillsched_daythursday)).setChecked((WorkingCurPillDays&16)==16);
			((CheckBox)v.findViewById(R.id.pillsched_dayfriday)).setChecked((WorkingCurPillDays&32)==32);
			((CheckBox)v.findViewById(R.id.pillsched_daysaturday)).setChecked((WorkingCurPillDays&64)==64);
			build.setPositiveButton(R.string.dialog_set,new DialogInterface.OnClickListener(){

				public void onClick(DialogInterface dialog, int which) {
					AlertDialog v=(AlertDialog)dialog;
					int mask=((CheckBox)v.findViewById(R.id.pillsched_daysunday)).isChecked()?1:0;
					mask=mask|(((CheckBox)v.findViewById(R.id.pillsched_daymonday)).isChecked()?2:0);
					mask=mask|(((CheckBox)v.findViewById(R.id.pillsched_daytuesday)).isChecked()?4:0);
					mask=mask|(((CheckBox)v.findViewById(R.id.pillsched_daywednesday)).isChecked()?8:0);
					mask=mask|(((CheckBox)v.findViewById(R.id.pillsched_daythursday)).isChecked()?16:0);
					mask=mask|(((CheckBox)v.findViewById(R.id.pillsched_dayfriday)).isChecked()?32:0);
					mask=mask|(((CheckBox)v.findViewById(R.id.pillsched_daysaturday)).isChecked()?64:0);
					setDays(mask);
					displayTimeDialog(i);
				}});
			build.setInverseBackgroundForced(true);
			build.setView(v);
			build.show();
			
		}

		protected void displayTimeDialog(final int i) {
			AlertDialog.Builder build=new AlertDialog.Builder(passedContext);
			build.setTitle(R.string.dialog_pilledit_edittime);
			LayoutInflater inflate=passedActivity.getLayoutInflater();
			View v=inflate.inflate(R.layout.dialog_pilledit_schedtime,null);
			TimePicker time=(TimePicker)v.findViewById(R.id.pillsched_timepicker);
			time.setCurrentHour(WorkingCurPillTime/60/60);
			time.setCurrentMinute((WorkingCurPillTime/60)%60);
			build.setView(v);
			build.setPositiveButton(R.string.dialog_set, new DialogInterface.OnClickListener() {
				
				public void onClick(DialogInterface dialog, int which) {
					TimePicker v=(TimePicker)((AlertDialog)dialog).findViewById(R.id.pillsched_timepicker);
					WorkingCurPillTime=(((v.getCurrentHour()*60)+v.getCurrentMinute())*60);
					if(itemCount>i)
					{
						CurPillTime.set(i-6, WorkingCurPillTime);
						CurPillDays.set(i-6, WorkingCurPillDays);
						CurPillRowIds.set(i-6, WorkingCurPillRowId);
					}else
					if(itemCount==i)
					{
						CurPillTime.add(i-6, WorkingCurPillTime);
						CurPillDays.add(i-6, WorkingCurPillDays);
						CurPillRowIds.add(i-6, WorkingCurPillRowId);
						itemCount++;
						updateList();
					}
				}
			});
			build.setNegativeButton(R.string.dialog_revert, null);
			build.show();
		}
		private void setDays(int days)
		{
			WorkingCurPillDays=days;
		}
		private void setTime(int sec)
		{
			WorkingCurPillTime=sec;
		}
	}
	private void updateList()
	{
		ArrayList<String> listItems=new ArrayList<String>();
		listItems.add("Pill Name");
		listItems.add("Pill Location");
		listItems.add("Pill Instructions");
		listItems.add("Pill Color");
		listItems.add("Pill Dosage");
		listItems.add("Take Picture");
		for(int i=6;i<itemCount;i++)
		{
			listItems.add("Modify Scheduled Time");
		}
		listItems.add("Add Scheduled Time");
		ArrayAdapter<String> listAdapterItems=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,listItems);
		ListView v=(ListView)findViewById(R.id.listViewAddPill);
        v.setAdapter(listAdapterItems);
	}
	AddPillOnItemClickListener oICL;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pill);
        itemCount=6;
        updateList();
        oICL=new AddPillOnItemClickListener();
        oICL.passedContext=this;
        oICL.passedActivity=this;
        oICL.dialogListen=this;
        ListView v=(ListView)findViewById(R.id.listViewAddPill);
        v.setOnItemClickListener(oICL);
        imageView = (ImageView) findViewById(R.id.add_pill_imageview);
        CurPillName="";
        CurPillLocation="";
        CurPillInstructions="";
        CurPillColor="";
        CurPillDosage="";
        CurPillDays=new ArrayList<Integer>();
        CurPillTime=new ArrayList<Integer>();
        CurPillRowIds=new ArrayList<Long>();
        CurPillRowID=-1;
        if(getIntent().getBooleanExtra("edit", false))
        {
        	PALdb pal=new PALdb(this);
        	SQLiteDatabase db = pal.getReadableDatabase();
        	Cursor cursor=db.query(PALdb.DATABASE_PILL_TABLE, null, "rowID=?", new String[]{Long.toString(getIntent().getLongExtra("rowID",-1))}, null, null, null);
        	if(cursor.moveToFirst())
        	{
        		CurPillRowID=cursor.getLong(cursor.getColumnIndex("rowID"));
        		CurPillName=cursor.getString(cursor.getColumnIndex("pillName"));
        		CurPillLocation=cursor.getString(cursor.getColumnIndex("pillLocation"));
        		CurPillInstructions=cursor.getString(cursor.getColumnIndex("pillInstructions"));
        		CurPillColor=cursor.getString(cursor.getColumnIndex("pillColor"));
        		CurPillDosage=cursor.getString(cursor.getColumnIndex("pillDosageInfo"));
        		byte[] imageBlob = cursor.getBlob(cursor.getColumnIndex("pillPicture"));
        		imageView.setImageBitmap(BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length));
        		photo=BitmapFactory.decodeByteArray(imageBlob, 0, imageBlob.length);
        		Cursor schedCursor=db.query(PALdb.DATABASE_SCHEDULE_TABLE, null, "pillRowID=?", new String[]{Long.toString(getIntent().getLongExtra("rowID", -1))}, null, null, "rowID ASC");
        		
        		while(schedCursor.moveToNext())
        		{
        			CurPillDays.add(schedCursor.getInt(schedCursor.getColumnIndex("weekDaysMask")));
        			CurPillTime.add(schedCursor.getInt(schedCursor.getColumnIndex("secAfterMidnight")));
        			CurPillRowIds.add(schedCursor.getLong(schedCursor.getColumnIndex("rowID")));
        			itemCount++;
        		}
        		schedCursor.close();
        		cursor.close();
        		db.close();
        		updateList();
        	}else{
        		cursor.close();
        		db.close();
        		return;
        	}
        }
        
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    
    	if (requestCode == PILL_PICTURE_REQUEST && resultCode == RESULT_OK) {  
            photo = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(photo);
        }
    
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    private byte[] bitmapToByteArray(Bitmap bmp)
    {
    	ByteArrayOutputStream blob = new ByteArrayOutputStream();
    	bmp.compress(CompressFormat.PNG, 0 /*ignored for PNG*/, blob);
    	byte[] bitmapdata = blob.toByteArray();
    	return bitmapdata;
    }
    
    public void save(View view) throws Exception{
		byte[] pillImageBytes;
		if (photo == null){
			InputStream is = this.getResources().openRawResource(R.drawable.pill);
			photo = BitmapFactory.decodeStream(is);
		}
		pillImageBytes = bitmapToByteArray(photo); 
		
		PALdb pal=new PALdb(this);
		SQLiteDatabase db = pal.getWritableDatabase();
		ContentValues valuess=new ContentValues();
		if(CurPillRowID!=-1)
		{
			valuess.put("rowID", CurPillRowID);
		}
		valuess.put("pillName", CurPillName);
		valuess.put("pillLocation", CurPillLocation);
		valuess.put("pillInstructions", CurPillInstructions);
		valuess.put("pillPicture", pillImageBytes);
		valuess.put("pillColor", CurPillColor);
		valuess.put("pillDosageInfo", CurPillDosage);
		long rowid=db.insertWithOnConflict(PALdb.DATABASE_PILL_TABLE, null, valuess,SQLiteDatabase.CONFLICT_REPLACE);
		
		for(int i=0;i<CurPillDays.size();i++)
		{
			ContentValues schedValues=new ContentValues();
			if(CurPillRowIds.get(i)!=-1)
			{
				schedValues.put("rowID", CurPillRowIds.get(i));
			}
			schedValues.put("pillRowID", rowid);
			schedValues.put("weekDaysMask",CurPillDays.get(i));
			schedValues.put("secAfterMidnight",CurPillTime.get(i));
			db.insertWithOnConflict(PALdb.DATABASE_SCHEDULE_TABLE, null, schedValues,SQLiteDatabase.CONFLICT_REPLACE);
		}
		db.close();
		Intent intent=new Intent(this,AlarmService.class);
		startService(intent);//update the time the alarm will go off
		finish();
		/*
		 * For storing and retrieving from DB, check this: http://lunarmonk.wordpress.com/2011/06/13/android-sqlite-story-of-a-blob/
		 *
		 * */
	}
	
	public void onDialogPositiveClick(DialogInterface dialog,int position) {
		if(position==0)
		{
			EditText text=(EditText) (((Dialog)dialog).findViewById(R.id.dialog_pilledit_pillname));
			String value=text.getText().toString();
			CurPillName=value;
		}else
		if(position==1)
		{
			EditText text=(EditText) (((Dialog)dialog).findViewById(R.id.dialog_pilledit_pilllocation));
			String value=text.getText().toString();
			CurPillLocation=value;
		}else
		if(position==2)
		{
			EditText text=(EditText) (((Dialog)dialog).findViewById(R.id.dialog_pilledit_pillinstructions));
			String value=text.getText().toString();
			CurPillInstructions=value;
		}else
		if(position==3)
		{
			EditText text=(EditText) (((Dialog)dialog).findViewById(R.id.dialog_pilledit_pillcolor));
			String value=text.getText().toString();
			CurPillColor=value;
		}else
		if(position==4)
		{
			EditText text=(EditText) (((Dialog)dialog).findViewById(R.id.dialog_pilledit_pilldosage));
			String value=text.getText().toString();
			CurPillDosage=value;
		}else
		if(position==5)
		{
			//nothing, handled elsewhere
		}else
		if(position>=6)
		{
			CurPillDays.set(position-6, WorkingCurPillDays);
			CurPillTime.set(position-6, WorkingCurPillTime);
		}
	}

	public void onDialogNegativeClick(DialogInterface dialog,int position) {
		dialog.cancel();
	}
	public void goHome(View view){
    	Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
