package com.ronan.andshmup;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class LeaderboardDB
{
	public static final String KEY_ID = "_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_SCORE = "score";
	
	private static final String DATABASE_NAME = "andshmup.db";
	private static final String DATABASE_TABLE = "leaderboard";
	private static final int DATABASE_VERSION = 2;
	
	private static final String DATABASE_CREATE = 
			"CREATE TABLE "
			+ DATABASE_TABLE
			+ " (_id INTEGER PRIMARY KEY autoincrement, "
			+ "name TEXT NOT NULL, score INTEGER NOT NULL)";
	
	private final Context context;
	
	private LeaderboardDBHelper LBHelper;
	private SQLiteDatabase db;
	
	public LeaderboardDB(Context ctx)
	{
		this.context = ctx;
		LBHelper = new LeaderboardDBHelper(context);
	}
	
	private static class LeaderboardDBHelper extends SQLiteOpenHelper
	{

		public LeaderboardDBHelper(Context context) 
		{
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			db.execSQL(DATABASE_CREATE);
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
	            db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
	            onCreate(db);
		}
	}// end LaderboardDBHelper
	
	public LeaderboardDB open() throws SQLException
	{
		db = LBHelper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		if (LBHelper != null)
			LBHelper.close();
	}
	
	public void insertEnty(String pName, int pScore)
	{
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NAME, pName);
		initialValues.put(KEY_SCORE, pScore);
		db.insertOrThrow(DATABASE_TABLE, null, initialValues);
	}
	
	public ArrayList<String> getAllEntries()
	{
		ArrayList<String> returnVal = new ArrayList<String>();
		Cursor c = db.query(DATABASE_TABLE, new String[] {"_id", "name", "score"}, null, null, null, null, "score DESC", "10");
		c.moveToFirst();
		do
		{
			returnVal.add(c.getString(1) + "~" + c.getString(2));
		}while (c.moveToNext());
		
		
		return returnVal;
	}
}