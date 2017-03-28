package com.money.atm;

import java.util.Hashtable;
import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class ATMList implements KvmSerializable {
	
	public String ATM_Name;
	public String latitude;
    public String longitude;
    public String locality;
    public String datecreated;
        
    public ATMList()
    {
    }
    
    public ATMList(String ATM_Name, String latitude, 
    		String longitude, String locality, 
    		String datecreated)
    {
    	this.ATM_Name = ATM_Name;
    	this.latitude = latitude;
    	this.longitude = longitude;
    	this.locality = locality;
    	this.datecreated = datecreated;
    	
    }
    
	
	public Object getProperty(int arg0) {
		// TODO Auto-generated method stub
		
		switch(arg0)
        {
        case 0:
            return ATM_Name;
        case 1:
            return latitude;
        case 2:
            return longitude;
        case 3:
            return locality;
        case 4:
            return datecreated;
       
        }
		return null;
	}

	@Override
	public int getPropertyCount() {
		// TODO Auto-generated method stub
		return 5;
	}

	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		// TODO Auto-generated method stub
		switch(index)
        {
        case 0:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "ATM_Name";
            break;
        case 1:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "latitude";
            break;
        case 2:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "longitude";
            break;
        case 3:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "locality";
            break;
        case 4:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "datecreated";
            break;
        
        default:break;
        }
	}

	@Override
	public void setProperty(int index, Object value) {
		// TODO Auto-generated method stub
		switch(index)
        {
        case 0:
            ATM_Name = value.toString();
            break;
        case 1:
            latitude = value.toString();
            break;
        case 2:
            longitude = value.toString();
            break;
        case 3:
            locality = value.toString();
        case 4:
            datecreated = value.toString();
      
        default:
            break;
        }
	}

}

