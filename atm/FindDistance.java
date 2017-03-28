package com.money.atm;

import android.app.Activity;
import android.location.Location;

public class FindDistance extends Activity{
	
	int f;
	Location dstlocation=new Location("dst");
	Location srclocation=new Location("jk");
	Location location1 = new Location("hh");
	public  int Distance(Location location,double dstlatitude,double dstlongitude)
	{
		float distance;
		
		
	        dstlocation.setLatitude(dstlatitude);
	        dstlocation.setLongitude(dstlongitude);
	        location1.setLatitude(location.getLatitude());// location1.setLatitude(17.428424033333332);//
	        location1.setLongitude(location.getLongitude());//location1.setLongitude(78.43953266666666);//
	        distance = location1.distanceTo(dstlocation);  
	        return f=(int) distance;
	}

}
