package com.example.pills.here;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.utdproject.pills.here.R;

public class AlarmMain extends Activity implements DialogListener {
	static Ringtone r;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_main);
        try{
        	r=RingtoneManager.getRingtone(this,RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
        	r.play();
        }catch(RuntimeException r)
        {
        	
        }
        Intent intentStarted=getIntent();
    	if(intentStarted.getBooleanExtra("testing", false))
    	{
    		return;//this was called as a test
    	}
    	if(!intentStarted.hasExtra("schedRowID"))
    	{
    		return;//no info on the alarm
    	}
        PALdb pal=new PALdb(this);
        SQLiteDatabase db = pal.getReadableDatabase();
        
        int rowOfInterest=intentStarted.getIntExtra("schedRowID", -1);
        if(rowOfInterest==-1)
        {
        	db.close();finish();return;//no info on this scheduled time
        }
        Cursor cursor = db.query(PALdb.DATABASE_SCHEDULE_TABLE,new String[]{"rowID","pillRowID","secAfterMidnight"}, "rowID=?",new String[]{Integer.toString(rowOfInterest)}, null, null, null);
        cursor.moveToFirst();
        int pillrowid=cursor.getInt(cursor.getColumnIndex("pillRowID"));
        Cursor cursor2=db.query(PALdb.DATABASE_PILL_TABLE, null, "rowID=?", new String[]{Integer.toString(pillrowid)}, null, null, null);
        cursor2.moveToFirst();
        ((TextView)findViewById(R.id.alarm_pillname)).setText(cursor2.getString(cursor2.getColumnIndex("pillName")));
        int secAfterMidnight=cursor.getInt(cursor.getColumnIndex("secAfterMidnight"));
        String alarmTime=Integer.toString((secAfterMidnight/60/60)%12);
        alarmTime+=":";
        String temp=Integer.toString((secAfterMidnight/60)%60);
        if(temp.length()==1){temp=" "+temp;}
        alarmTime+=temp;
        alarmTime+=" ";
        if((secAfterMidnight/60/60/12)==0)
        {
        	alarmTime+="AM";
        }else{
        	alarmTime+="PM";
        }
        ((TextView)findViewById(R.id.alarm_time)).setText(alarmTime);
        byte[] picBlob = cursor2.getBlob(cursor2.getColumnIndex("pillPicture"));
        Bitmap bm=BitmapFactory.decodeByteArray(picBlob, 0, picBlob.length);
        if(bm==null){bm=BitmapFactory.decodeResource(getResources(), R.drawable.pill);}
        ((ImageView)findViewById(R.id.alarm_image)).setImageBitmap(bm);
        ((TextView)findViewById(R.id.alarm_dosage)).setText("Dosage: "+cursor2.getString(cursor2.getColumnIndex("pillDosageInfo")));
        ((TextView)findViewById(R.id.alarm_color)).setText("Color: "+cursor2.getString(cursor2.getColumnIndex("pillColor")));
        ((TextView)findViewById(R.id.alarm_location)).setText("Location: "+cursor2.getString(cursor2.getColumnIndex("pillLocation")));
        ((TextView)findViewById(R.id.alarm_instructions)).setText("Instructions: "+cursor2.getString(cursor2.getColumnIndex("pillInstructions")));
        
        
        db.close();
    }
    @Override
    public void onStop()
    {
    	super.onStop();
    	try{
    		r.stop();
    	}catch(RuntimeException r)
    	{
    		
    	}
    }
    public void pillTaken(View v)
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.alarm_confirmtaken);
        final DialogListener dl=this;
        builder.setPositiveButton(R.string.alarm_confirm_yes, new DialogInterface.OnClickListener()
        	{
				public void onClick(DialogInterface dialog, int arg1) {
					dl.onDialogPositiveClick(dialog,0);
				}
        	}
        );
        builder.setNegativeButton(R.string.alarm_confirm_no, new DialogInterface.OnClickListener()
    		{
				public void onClick(DialogInterface dialog, int arg1) {
					dl.onDialogNegativeClick(dialog,0);
					dialog.cancel();
				}
    		}
        );
        builder.show();
    }
    public void pillMissing(View v)
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.alarm_confirmcantfind);
        final DialogListener dl=this;
        builder.setPositiveButton(R.string.alarm_confirm_yes, new DialogInterface.OnClickListener()
        	{
				public void onClick(DialogInterface dialog, int arg1) {
					dl.onDialogPositiveClick(dialog,0);
				}
        	}
        );
        builder.setNegativeButton(R.string.alarm_confirm_no, new DialogInterface.OnClickListener()
    		{
				public void onClick(DialogInterface dialog, int arg1) {
					dl.onDialogNegativeClick(dialog,0);
					dialog.cancel();
				}
    		}
        );
        builder.show();
    }
    public void pillNoSupply(View v)
    {
    	AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle(R.string.alarm_confirmimout);
        final DialogListener dl=this;
        builder.setPositiveButton(R.string.alarm_confirm_yes, new DialogInterface.OnClickListener()
        	{
				public void onClick(DialogInterface dialog, int arg1) {
					dl.onDialogPositiveClick(dialog,0);
				}
        	}
        );
        builder.setNegativeButton(R.string.alarm_confirm_no, new DialogInterface.OnClickListener()
    		{
				public void onClick(DialogInterface dialog, int arg1) {
					dl.onDialogNegativeClick(dialog,0);
					dialog.cancel();
				}
    		}
        );
        builder.show();
    }
	public void onDialogPositiveClick(DialogInterface dialog,int which) {
		this.finish();
	}
	public void onDialogNegativeClick(DialogInterface dialog,int which) {
		// TODO Auto-generated method stub
		
	}
}
