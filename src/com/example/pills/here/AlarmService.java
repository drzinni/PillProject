package com.example.pills.here;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.widget.Toast;

import com.utdproject.pills.here.R;

public class AlarmService extends Service {

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate()
	{
		
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startid)
	{
		PALdb dbopen=new PALdb((Context)this);
		SQLiteDatabase db=dbopen.getReadableDatabase();
		Cursor schedCursor=db.query(PALdb.DATABASE_SCHEDULE_TABLE, null, null, null, null, null, "rowID ASC");
		//TODO: return array of objects that represent a single-pill schedule
		TreeMap<Integer,Date> timesToSchedule=new TreeMap<Integer,Date>();//map rowID to time
		while(schedCursor.moveToNext())
		{
			int schedRowID=schedCursor.getInt(schedCursor.getColumnIndexOrThrow("rowID"));
			double seconds=schedCursor.getDouble(schedCursor.getColumnIndexOrThrow("secAfterMidnight"));
			int daysOfWeek=schedCursor.getInt(schedCursor.getColumnIndexOrThrow("weekDaysMask"));
			
			Calendar cal=Calendar.getInstance();
			cal.setTime(new Date());
			cal.setTimeZone(TimeZone.getDefault());
			int day=cal.get(Calendar.DAY_OF_WEEK);
			if(isLaterToday(seconds,daysOfWeek,cal))
			{
				timesToSchedule.put(schedRowID, getLaterToday(seconds,cal));
			}else{
				timesToSchedule.put(schedRowID, getNotToday(seconds,daysOfWeek,cal));
			}
		}
		//TODO: determine next occurance of each
		for(TreeMap.Entry<Integer,Date> iter:timesToSchedule.entrySet())
		{
			Intent alarmIntent=new Intent((Context)this,AlarmMain.class);
			alarmIntent=alarmIntent.putExtra("schedRowID",iter.getKey());
			PendingIntent pi=PendingIntent.getActivity((Context)this, 0,alarmIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
			//CauseAlarmAt(iter.getValue(),pi);
			
			
			
			
			
			
			
			
			//TODO UNCOMMENT CauseAlarmAt(iter.getValue(),pi); to enable the alarm to work although it is currently very janky
			
			
			
			
			
			
			
			
			
		}
		//TODO: create pendingIntent for each single-pill schedule for the next occurance
		//TODO: call CauseAlarmAt for each
		//TODO:remove following debug code
		if(intent.getBooleanExtra("testing",false))
		{
			Toast t=Toast.makeText(this,"test",Toast.LENGTH_SHORT);t.show();
			//TODO UNCOMMENT CauseAlarmAt(new Date(),PendingIntent.getActivity((Context)this,0,new Intent((Context)this,AlarmMain.class),Intent.FLAG_ACTIVITY_NEW_TASK)); to enable testing through the service
		}
		schedCursor.close();
		db.close();
		return START_NOT_STICKY;
	}
	

	private Date getNotToday(double seconds, int daysOfWeek, Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		if(daysOfWeek==0){return null;}
		while(((2<<cal.get(Calendar.DAY_OF_WEEK))^daysOfWeek)==0)
		{
			cal.roll(Calendar.DAY_OF_WEEK, 1);
		}
		cal.roll(Calendar.SECOND, (int)Math.floor(seconds));
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		return cal.getTime();
	}

	private Date getLaterToday(double seconds, Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, ((int)seconds)/60/60);
		cal.set(Calendar.MINUTE, (((int)seconds)/60)%60);
		cal.set(Calendar.SECOND, ((int)seconds)%60);
		cal.setTimeZone(TimeZone.getTimeZone("UTC"));
		return cal.getTime();
	}

	private boolean isLaterToday(double seconds, int daysOfWeek, Calendar cal) {
		if(((2<<cal.get(Calendar.DAY_OF_WEEK))^daysOfWeek)==(2<<cal.get(Calendar.DAY_OF_WEEK)))
		{
			if(cal.get(Calendar.HOUR_OF_DAY)>=(((int)seconds)/60/60))
			{
				if(cal.get(Calendar.MINUTE)>=((((int)seconds)/60)%60))
				{
					if(cal.get(Calendar.SECOND)>=(((int)seconds)%60))
					{
						return true;
					}
				}
			}
		}
		return false;
	}

	private void CauseAlarmAt(Date d,PendingIntent pend)//executes pend at time d
	{
		AlarmManager am=(AlarmManager)this.getSystemService(Context.ALARM_SERVICE);
		am.set( AlarmManager.RTC_WAKEUP, d.getTime(), pend);
	}
}
