package com.money.atm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.widget.Toast;
import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.money.model.MyOverLay;

public class ShowMap extends MapActivity implements LocationListener {
	 double  latitude,longitude;
	MapView map ;
	MapController myMC=null;
	GeoPoint geoPoint=null;
	LocationManager locationManager;
	double Long = 0;
	double Lat =0;
	public  Location location;
	private String bestProvider;
	
	  public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);  // Suppress title bar to give more space
	        setContentView(R.layout.main);
	        
	       
	        Bundle bundle = this.getIntent().getExtras();
			Lat = Double.valueOf(bundle.getString("latitude"));
			Long = Double.valueOf(bundle.getString("longitude"));
	        
	        /*LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	    	lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f,  this);
	    	Location location =lm.getLastKnownLocation(LocationManager.GPS_PROVIDER); */
	    	
			 locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
			 locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000L, 500.0f,  this);
		       
		       Criteria criteria = new Criteria();
				bestProvider =  locationManager.getBestProvider(criteria, false);
				 location = locationManager.getLastKnownLocation(bestProvider);
	    	 map = (MapView) findViewById(R.id.myGMap);
	    	double intLat = location.getLatitude(); // 17.428424033333332;// the testing source
	    	double intLong =location.getLongitude();//78.43953266666666;//
	    	double dest_lat =Lat; // the testing destination
	    	double dest_long = Long;
	    	GeoPoint srcGeoPoint = new GeoPoint((int) (intLat * 1E6),
	    	(int) (intLong * 1E6));
	    	GeoPoint destGeoPoint = new GeoPoint((int) (dest_lat * 1E6),
	    	(int) (dest_long * 1E6));

	    	DrawPath(srcGeoPoint, destGeoPoint, Color.GREEN, map);
	        
	        //map = (MapView) findViewById(R.id.myGMap);
            map.setSatellite(false);	        
	        myMC = map.getController();
	        myMC.setZoom(15);
	        map.setBuiltInZoomControls(true);
	        map.displayZoomControls(true);
	        geoPoint = new GeoPoint((int) (intLat*1000000 ), (int) (intLong*1000000));
		    myMC.animateTo(geoPoint);
		    myMC.setCenter(geoPoint);
		 
	  }
	 	  public  void putLatLong(double lat, double lon){
			latitude = lat;
			longitude = lon;
		}
	 	 private void DrawPath(GeoPoint src,GeoPoint dest, int color, MapView mMapView01)
	 	{
	 	// connect to map web service
	 	StringBuilder urlString = new StringBuilder();
	 	urlString.append("http://maps.google.com/maps?f=d&hl=en");
	 	urlString.append("&saddr=");//from
	 	urlString.append( Double.toString((double)src.getLatitudeE6()/1.0E6 ));
	 	urlString.append(",");
	 	urlString.append( Double.toString((double)src.getLongitudeE6()/1.0E6 ));
	 	urlString.append("&daddr=");//to
	 	urlString.append( Double.toString((double)dest.getLatitudeE6()/1.0E6 ));
	 	urlString.append(",");
	 	urlString.append( Double.toString((double)dest.getLongitudeE6()/1.0E6 ));
	 	urlString.append("&ie=UTF8&0&om=0&output=kml");
	 	//Log.d("xxx","URL="+urlString.toString());
	 	// get the kml (XML) doc. And parse it to get the coordinates(direction route).
	 	Document doc = null;
	 	HttpURLConnection urlConnection= null;
	 	URL url = null;
	 	try
	 	{
	 	url = new URL(urlString.toString());
	 	urlConnection=(HttpURLConnection)url.openConnection();
	 	urlConnection.setRequestMethod("GET");
	 	urlConnection.setDoOutput(true);
	 	urlConnection.setDoInput(true);
	 	urlConnection.connect();

	 	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	 	DocumentBuilder db = dbf.newDocumentBuilder();
	 	doc = db.parse(urlConnection.getInputStream());

	 	if(doc.getElementsByTagName("GeometryCollection").getLength()>0)
	 	{
	 	//String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getNodeName();
	 	String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue() ;
	 	//Log.d("xxx","path="+ path);
	 	String [] pairs = path.split(" ");
	 	String[] lngLat = pairs[0].split(","); // lngLat[0]=longitude lngLat[1]=latitude lngLat[2]=height
	 	// src
	 	GeoPoint startGP = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
	 	mMapView01.getOverlays().add(new MyOverLay(startGP,startGP,1));
	 	GeoPoint gp1;
	 	GeoPoint gp2 = startGP;
	 	for(int i=1;i<pairs.length;i++) // the last one would be crash
	 	{
	 	lngLat = pairs[i].split(",");
	 	gp1 = gp2;
	 	// watch out! For GeoPoint, first:latitude, second:longitude
	 	gp2 = new GeoPoint((int)(Double.parseDouble(lngLat[1])*1E6),(int)(Double.parseDouble(lngLat[0])*1E6));
	 	mMapView01.getOverlays().add(new MyOverLay(gp1,gp2,2,color));
	 	//Log.d("xxx","pair:" + pairs[i]);
	 	}
	 	mMapView01.getOverlays().add(new MyOverLay(dest,dest, 3)); // use the default color
	 	}
	 	}
	 	catch (MalformedURLException e)
	 	{
	 	e.printStackTrace();
	 	}
	 	catch (IOException e)
	 	{
	 	e.printStackTrace();
	 	}
	 	catch (ParserConfigurationException e)
	 	{
	 	e.printStackTrace();
	 	}
	 	catch (SAXException e)
	 	{
	 	e.printStackTrace();
	 	}
	 	}
	  @Override
		protected boolean isRouteDisplayed() {
			// TODO Auto-generated method stub
			return false;
		}
	  public boolean onCreateOptionsMenu(Menu menu) {
			super.onCreateOptionsMenu(menu);
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.mainmenu, menu);
			return true;
		}
		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId()) {
			case R.id.sateliteview:
				ProgressDialog dialog = ProgressDialog.show(this, "",
						"Displaying SateliteView.  Please wait for few seconds...", true);
				
				 map.setSatellite(true);
				
					dialog.dismiss();
				break;
				
			case R.id.Mapview:
				map.setSatellite(false);
				break;

			default:
				return super.onOptionsItemSelected(item);
			}

			return true;
		}
	  public boolean onKeyDown(int keyCode, KeyEvent event) {
	         if (keyCode == KeyEvent.KEYCODE_BACK) {
	        	ProgressDialog dialog = ProgressDialog.show(ShowMap.this, "","ATM's List is in progress...", true);
	             startActivity(new Intent(ShowMap.this,ATM.class));
	             dialog.dismiss();
	             return true;
	        }
	        else if (keyCode == KeyEvent.FLAG_VIRTUAL_HARD_KEY) {
	              Toast.makeText(this,"On VIrtual Hard Key" ,Toast.LENGTH_LONG).show();
	              return true;
	        }
	        return false;
	  }
	 
	
		@Override
		public void onLocationChanged(Location arg0) {
			
		}
		@Override
		public void onProviderDisabled(String provider) {
			
		}
		@Override
		public void onProviderEnabled(String provider) {
			
		}
		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			
		}
	

}
