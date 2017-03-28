package com.money.atm;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.maps.MapActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Directions extends MapActivity implements OnClickListener,LocationListener
{
	private String bestProvider=LocationManager.GPS_PROVIDER;
	ProgressDialog pd;
	LocationManager lm;
	Location location;
	@Override
    public void onCreate(Bundle savedInstanceState) {
		double Long = 0;
		double Lat =0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.direction);	
        pd=new ProgressDialog(Directions.this);
        View map=findViewById(R.id.BackGMap);
        View layout=findViewById(R.id.directionlayout);
       // layout.setBackgroundResource(R.id.BackGMap);
       // Button b=(Button)findViewById(R.id.btnmap);
        TextView txt=(TextView)findViewById(R.id.lbldirection);
        EditText dstName=(EditText)findViewById(R.id.editdestination);
        EditText srcName=(EditText)findViewById(R.id.editmylocation);
        
        

        Bundle bundle = this.getIntent().getExtras();
        dstName.setText(bundle.getString("bank"));
		Lat = Double.valueOf(bundle.getString("latitude"));
		Long = Double.valueOf(bundle.getString("longitude"));
		
		lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
	    LocationListener ll = new Directions();
	    Criteria criteria = new Criteria();
		bestProvider = lm.getBestProvider(criteria, false);
	    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, ll);
	    location = lm.getLastKnownLocation(bestProvider);
	   
	    double src_lat = location.getLatitude(); // 17.428424033333332;//the testing source
    	double src_long =location.getLongitude();//78.43953266666666;//
	    
	    Geocoder geoCoder = new Geocoder(
                getBaseContext(), Locale.getDefault());
	    try {
   		 List<Address> addresses = 
   			 geoCoder.getFromLocation(src_lat, src_long,1);
            if (addresses.size() > 0) {
                StringBuilder result = new StringBuilder();
                for(int i = 0; i < addresses.size(); i++){
                    Address address =  addresses.get(i);
                    int maxIndex = address.getMaxAddressLineIndex();
                    for (int x = 0; x <= maxIndex; x++ ){
                        result.append(address.getAddressLine(x));
                        result.append(",");
                    }               
                    result.append(address.getLocality());
                    result.append(",");
                    result.append(address.getPostalCode());
                    result.append("\n\n");
                }
                txt.setText(result.toString());
            }

            //Toast.makeText(getBaseContext(), add, Toast.LENGTH_LONG).show();
           // txt.setText(add);
        }
        catch (IOException e) {                
            e.printStackTrace();
        }   
	}
	
	public void onButtonClick(View v){
		
			pd = ProgressDialog.show(Directions.this, "", "Displaying ATMs List ,please wait.......", true,true);
			pd.show();
			startActivity(new Intent(Directions.this,ShowMap.class));
			
			handler.sendEmptyMessage(0);
		
		}
	 public boolean onKeyDown(int keyCode, KeyEvent event) {
	        if (keyCode == KeyEvent.KEYCODE_BACK) {
	       	        Directions.this.finish();    
	       //	startActivityForResult(new Intent(Intent.CATEGORY_HOME), 0);
	           
	            return true;
	       }
	       
	       return false;
	    }
	 private Handler handler = new Handler() {

		    @Override
		    public void handleMessage(Message msg) {
		            pd.dismiss();
		            
		    }
		};

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}
	}


