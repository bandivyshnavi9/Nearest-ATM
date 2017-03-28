package com.money.atm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import com.google.android.maps.GeoPoint;
import com.money.data.DataHelper;
import com.money.model.ATMLocation;

public class ATM extends Activity implements LocationListener {
	/** Called when the activity is first created. */
	ListView li;
	DataHelper dh;
	private TextView lblResult;
	public static Location location;
	private static final String SOAP_ACTION = "http://tempuri.org/getATMCard_Hyd";

	private static final String METHOD_NAME = "getATMCard_Hyd";

	private static final String NAMESPACE = "http://tempuri.org/";
	private static final String URL = "http://gcontact.iridiuminteractive.in/webservice1.asmx";
	private String bestProvider = LocationManager.GPS_PROVIDER;
	private ProgressDialog dialog;

	// String challanid = null;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.list);
		dialog = new ProgressDialog(ATM.this);
		lblResult = (TextView) findViewById(R.id.lblresult);
		TextView txt = (TextView) findViewById(R.id.lblselect);
		// startActivity(new Intent(this,ShowMap.class));

		try {

			LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
			LocationListener ll = new ATM();
			Criteria criteria = new Criteria();
			bestProvider = lm.getBestProvider(criteria, false);
			lm.requestLocationUpdates(bestProvider, 0, 0, ll);

			location = lm.getLastKnownLocation(bestProvider);

			/*
			 * LocationManager lm = (LocationManager)
			 * getSystemService(Context.LOCATION_SERVICE); LocationListener ll =
			 * new mylocationlistener();
			 * lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,
			 * ll);
			 */

			double src_lat = location.getLatitude(); // 17.428424033333332;//the
														// testing source
			double src_long = location.getLongitude();// 78.43953266666666;//
			GeoPoint geoPoint = new GeoPoint((int) (src_lat * 1000000),
					(int) (src_long * 1000000));
			Geocoder geoCoder = new Geocoder(getBaseContext(),
					Locale.getDefault());
			List<Address> addresses = geoCoder.getFromLocation(src_lat,
					src_long, 1);
			if (addresses.size() > 0) {
				StringBuilder result = new StringBuilder();
				for (int i = 0; i < addresses.size(); i++) {
					Address address = addresses.get(i);
					int maxIndex = address.getMaxAddressLineIndex();
					for (int x = 0; x <= maxIndex; x++) {
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

			// Toast.makeText(getBaseContext(), add, Toast.LENGTH_LONG).show();
			// txt.setText(add);
		} catch (Exception e) {
			AlertDialog alertDialog = new AlertDialog.Builder(ATM.this)
					.create();
			alertDialog.setTitle("Exit ATM Locator");
			alertDialog.setMessage("GPS is not available?");
			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();
					return;
				}
			});
			alertDialog.show();
		}

		DataHelper dh = new DataHelper(ATM.this);
		try {

			// ArrayList<Location> records=dh.selectAllAtmsData();

			li = (ListView) findViewById(R.id.listView1);
			// Get a handle to the list view

			// Bind the data with the list

			ArrayList<ATMLocation> lv_arr = dh.selectAllAtmsData();
			final ArrayList<ATMLocation> lv_arr2 = new ArrayList<ATMLocation>();
			Comparator<ATMLocation> comperator = new Comparator<ATMLocation>() {

				@Override
				public int compare(ATMLocation object1, ATMLocation object2) {
					return object1.distance.compareTo(object2.distance);
				}
			};
			Collections.sort(lv_arr, comperator);
			for (int i = 0; i < 3; i++) {
				lv_arr2.add(lv_arr.get(i));
			}
			li.setAdapter(new MyCustomBaseAdapter(this, lv_arr2));

			li.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> a, View v, int position,
						long id) {

					dialog = ProgressDialog.show(ATM.this, "",
							"Map routing  is in progress...", true, true);
					dialog.show();

					Object o = li.getItemAtPosition(position);
					ATMLocation fullObject = (ATMLocation) o;
					// Object o = lv1.getItemAtPosition(position);
					// SearchResults fullObject = (SearchResults)o;
					Bundle bundle = new Bundle();

					bundle.putString("bank", fullObject.getBank());
					bundle.putString("latitude", fullObject.getLat());
					bundle.putString("longitude", fullObject.getLng());

					Intent i = new Intent(ATM.this, Directions.class);
					i.putExtras(bundle);
					startActivity(i);

					// Toast.makeText(ListViewBlogPost.this, "You have chosen: "
					// + " " + fullObject.getName(), Toast.LENGTH_LONG).show();
					handler.sendEmptyMessage(0);
				}
			});

		} catch (Exception e) {

			AlertDialog alertDialog = new AlertDialog.Builder(ATM.this)
					.create();
			alertDialog.setTitle("Reset...");
			alertDialog.setMessage("Select 'OK' to Import Data !");

			alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {

					importData();
				}
			});
			alertDialog.setIcon(R.drawable.updatemasters);
			alertDialog.show();
			dh.closeDb();
		}

		dh.closeDb();
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			ATM.this.finish();
			// startActivityForResult(new Intent(Intent.CATEGORY_HOME), 0);

			return true;
		}

		return false;
	}

	public boolean isOnline() {
		// ConnectivityManager cm = (ConnectivityManager)
		// getSystemService(Context.CONNECTIVITY_SERVICE);
		// NetworkInfo netInfo = cm.getActiveNetworkInfo();
		// if (netInfo != null && netInfo.isConnectedOrConnecting()) {
		return true;
		// }
		// return false;
	}

	public void importData() {
		dialog = ProgressDialog.show(ATM.this, "",
				"Data Import is in progress...", true, true);
		dialog.show();
		DataHelper dh = new DataHelper(ATM.this);
		int successCount = 0;
		int failureCount = 0;

		if (isOnline()) {

			// ProgressDialog dialog = ProgressDialog.show(ATM.this,
			// "","Data Import is in progress...", true);
			// DataHelper dh=new DataHelper(ATM.this);

			try {

				SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME);

				// request.addProperty("prop1", "myprop");

				SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
						SoapEnvelope.VER11);
				envelope.dotNet = true;
				// request.addProperty( "ATM List", 1 );
				// request.addProperty( "PatID", patientId );
				envelope.setOutputSoapObject(request);

				HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
				envelope.addMapping(NAMESPACE, "ATMList",
						new ATMList().getClass());
				androidHttpTransport.call(SOAP_ACTION, envelope);
				SoapObject wsresult = (SoapObject) envelope.getResponse();
				// to get the data

				// String resultData=wsresult.getProperty(0).toString();
				// 0 is the first object of data
				// Toast.makeText(Login.this,resultData, 30000).show();

				ATMList[] masters = new ATMList[wsresult.getPropertyCount()];
				for (int i = 0; i < masters.length; i++) {
					SoapObject pii = (SoapObject) wsresult.getProperty(i);
					ATMList master = new ATMList();
					// master.city =
					// Integer.parseInt(pii.getProperty(0).toString());
					master.ATM_Name = pii.getProperty(0).toString();
					master.latitude = pii.getProperty(1).toString();
					master.longitude = pii.getProperty(2).toString();
					master.locality = pii.getProperty(3).toString();
					master.datecreated = pii.getProperty(4).toString();

					masters[i] = master;
					dh.insertATM(master);
					successCount = i;
				}

				if (successCount > 0 && failureCount == 0) {
					Toast.makeText(
							this,
							"Data Import completed successfully.  Records Imported: "
									+ successCount + ". ", Toast.LENGTH_LONG)
							.show();
					handler.sendEmptyMessage(0);
					startActivity(new Intent(ATM.this, Splash.class));
				} else if (failureCount > 0) {
					Toast.makeText(
							this,
							"Data Import completed with errors.  Records Imported: "
									+ successCount
									+ " and Records failed to Import: "
									+ failureCount, Toast.LENGTH_LONG).show();
					handler.sendEmptyMessage(0);

				} else if (failureCount == 0 && successCount == 0) {
					Toast.makeText(this,
							"No records found. 0 records Imported.",
							Toast.LENGTH_LONG).show();
					handler.sendEmptyMessage(0);
				} else {
					Toast.makeText(this, "Import failed.  No records Imported",
							Toast.LENGTH_LONG).show();
					handler.sendEmptyMessage(0);
				}
				// lblResult.setText("Record Count: " +
				// wsresult.getPropertyCount() + "     " + resultData);
			} catch (Exception e) {
				Toast.makeText(ATM.this, e.getMessage(), Toast.LENGTH_LONG)
						.show();
				handler.sendEmptyMessage(0);
				dh.closeDb();
			}

			// No Records to update

			dh.closeDb();

		} else {
			lblResult.setText("Data Import failed. check connection.");
			dh.closeDb();
		}
	}

	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			dialog.dismiss();

		}
	};

	@Override
	public void onLocationChanged(Location arg0) {
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

}
