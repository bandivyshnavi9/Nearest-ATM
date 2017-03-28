package com.money.data;

import java.util.ArrayList;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.location.Location;

import com.money.atm.ATM;
import com.money.atm.ATMList;
import com.money.atm.FindDistance;
import com.money.model.ATMLocation;

import android.util.Log;

public class DataHelper  {
	private static final String DATABASE_NAME = "atm.atmdb";
	private static final int DATABASE_VERSION = 4;
	private SQLiteStatement atmInsertStetement;
	int f;
	private Context context;
	private SQLiteDatabase atmdb;
	private static final String INSERT_ATMLIST = "insert into tblatm(ATM_Name,latitude,longitude,locality,datecreated) values (?,?,?,?,?)";
	ATMLocation loc;
	
		public DataHelper(Context context) {
		this.context = context;
		OpenHelper openHelper = new OpenHelper(this.context);
		this.atmdb = openHelper.getWritableDatabase();
		openHelper.onCreate(atmdb);

		try
		{
			
		this.atmInsertStetement = this.atmdb.compileStatement(INSERT_ATMLIST);
		}catch(SQLiteException e){e.getMessage();}

		
	}
	
	private static class OpenHelper extends SQLiteOpenHelper{

		OpenHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			}

			@Override
			public void onCreate(SQLiteDatabase atmdb) {
				atmdb
			.execSQL("CREATE TABLE IF NOT EXISTS tblatm (_id INTEGER PRIMARY KEY AUTOINCREMENT, ATM_Name TEXT, latitude TEXT, longitude TEXT,locality TEXT,datecreated TEXT) ");
				
			}

			@Override
			public void onUpgrade(SQLiteDatabase atmdb, int oldVersion, int newVersion) {
				Log.w("Example",
						"Upgrading database, this will drop tables and recreate.");
				atmdb.execSQL("DROP TABLE IF EXISTS tblatm");
				onCreate(atmdb);
			}
		}
	public SQLiteDatabase getWritableDatabase() {
		OpenHelper openHelper = new OpenHelper(this.context);
		this.atmdb = openHelper.getWritableDatabase();
		return atmdb;
	}
	
	//@SuppressWarnings("unchecked")
	public ArrayList<ATMLocation> selectAllAtmsData() {
		ArrayList<ATMLocation> list = new ArrayList<ATMLocation>();
		Cursor cursor = this.atmdb.query("tblatm", new String[] {
				"_id", "ATM_Name", 
				 "latitude", "longitude","locality","datecreated"}, null, null, null, null,
				"ATM_Name desc");
	cursor.moveToFirst();
	FindDistance d =new FindDistance();
        while (cursor.isAfterLast() == false) {
                
        	Integer dist = d.Distance(ATM.location,Double.valueOf(cursor.getString(2)),Double.valueOf(cursor.getString(3)));
        	 loc = new ATMLocation(cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(0),dist,cursor.getString(4),cursor.getString(5));
        	list.add(loc);
        	
        	cursor.moveToNext();
        }
        cursor.close();
        
		return list;
	}


	public void closeDb() {
		atmdb.close();
	}
	public void deleteAllAtmData() {
		this.atmdb.delete("tblatm", null, null);
	}
	
	public void loadAtms() {
		
		Cursor cursor = this.atmdb.query("tblatm", new String[] {"ATM_Name","latitude", 
				"longitude","locality","datecreated" }, null, null, null, null, null);
		
		if (cursor.moveToFirst()) {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		} else {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
			insertAtms();
		}
	}
	private void insertAtms() throws SQLiteException{
		// Load 1st User
		this.atmInsertStetement.clearBindings();
		this.atmInsertStetement.bindString(1, "SBI");
		this.atmInsertStetement.bindString(2, "17.428");
		this.atmInsertStetement.bindString(3, "78.547");
		this.atmInsertStetement.bindString(4, "Industrial Finance Branch, Raj Bhavan Road, Somajiguda");
		this.atmInsertStetement.bindString(5, "28Apr2011");
		this.atmInsertStetement.executeInsert();

		// Load 2nd User
		this.atmInsertStetement.clearBindings();
		this.atmInsertStetement.bindString(1, "SBI");
		this.atmInsertStetement.bindString(2, "17.434");
		this.atmInsertStetement.bindString(3, "78.456");
		this.atmInsertStetement.bindString(4, "Greenlands, Begumpet, Hyderabad");
		this.atmInsertStetement.bindString(5, "28Apr2011");
		this.atmInsertStetement.executeInsert();
		// Load 3rd User
		this.atmInsertStetement.clearBindings();
		this.atmInsertStetement.bindString(1, "SBI");
		this.atmInsertStetement.bindString(2, "17.405");
		this.atmInsertStetement.bindString(3, "78.516");
		this.atmInsertStetement.bindString(4, " O.U., Durgabai Deshmukh Colony, Vidya Nagar");
		this.atmInsertStetement.bindString(5, "28Apr2011");
		
		this.atmInsertStetement.executeInsert();
		this.atmInsertStetement.clearBindings();
		this.atmInsertStetement.bindString(1, "SBI");
		this.atmInsertStetement.bindString(2, "17.442");
		this.atmInsertStetement.bindString(3, "78.443");
		this.atmInsertStetement.bindString(4, "Sanjeeva Reddy Nagar, S R Nagar");
		this.atmInsertStetement.bindString(5, "28Apr2011");
		this.atmInsertStetement.executeInsert();
		// Load 4th User
		this.atmInsertStetement.clearBindings();
		this.atmInsertStetement.bindString(1, "SBI");
		this.atmInsertStetement.bindString(2, "17.412");
		this.atmInsertStetement.bindString(3, "78.498");
		this.atmInsertStetement.bindString(4, "Hyderabad, Musheerabad");
		this.atmInsertStetement.bindString(5, "28Apr2011");
		this.atmInsertStetement.executeInsert();
		
		
	}
	public long insertATM(ATMList bm) {
		this.atmInsertStetement.clearBindings();
		this.atmInsertStetement.bindString(1, bm.ATM_Name);
		this.atmInsertStetement.bindString(2, bm.latitude);
		this.atmInsertStetement.bindString(3, bm.longitude);
		this.atmInsertStetement.bindString(4, bm.locality);
		this.atmInsertStetement.bindString(5, bm.datecreated);
		
		return this.atmInsertStetement.executeInsert();
	}
	Location dstlocation=new Location("dst");
	Location srclocation=new Location("jk");
	Location location1 = new Location("hh");
	public  int Distance(Location location,double dstlatitude,double dstlongitude)
	{
		float distance;
		
		
	        dstlocation.setLatitude(dstlatitude);
	        dstlocation.setLongitude(dstlongitude);
	        location1.setLatitude(location.getLatitude());
	        location1.setLongitude(location.getLongitude());
	        distance = location1.distanceTo(dstlocation);  
	        return f=(int) distance;
	}
	
	public DataHelper()
	{
		
	}
}

