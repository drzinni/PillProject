package com.example.pills.here;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.utdproject.pills.here.R;

public class PALdb extends SQLiteOpenHelper  {
	private static final String DATABASE_NAME = "paldb";
	public static final String DATABASE_PILL_TABLE = "pills";
	public static final String DATABASE_SCHEDULE_TABLE = "sched";
	private static int DATABASE_VERSION = 2;
	
	public PALdb(Context context)
	{
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase db)
	{
		//TODO: use db.execSQL(); to create any tables etcetera
		db.execSQL("CREATE TABLE "+DATABASE_PILL_TABLE+
				" (rowID INTEGER PRIMARY KEY AUTOINCREMENT,"+
				"pillName TEXT not null UNIQUE,"+
				"pillLocation TEXT not null,"+
				"pillInstructions TEXT,"+
				"pillPicture BLOB,"+
				"pillColor TEXT,"+
				"pillDosageInfo TEXT"+
				");"
				);
		db.execSQL("CREATE TABLE "+DATABASE_SCHEDULE_TABLE+
				" (rowID INTEGER PRIMARY KEY AUTOINCREMENT, "+
				"pillRowID INT, "+
				"weekDaysMask INT, "+
				"secAfterMidnight INT"+
				");"
				);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
	}
}
