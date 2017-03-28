package com.money.atm;

import java.text.DecimalFormat;
import java.util.ArrayList;

import com.money.atm.R;
import com.money.model.ATMLocation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyCustomBaseAdapter extends BaseAdapter {
	 private static ArrayList<ATMLocation> searchArrayList;
	 
	 private LayoutInflater mInflater;

	 public MyCustomBaseAdapter(Context context, ArrayList<ATMLocation> results) {
	  searchArrayList = results;
	  mInflater = LayoutInflater.from(context);
	 }

	 public int getCount() {
	  return searchArrayList.size();
	 }

	 public Object getItem(int position) {
	  return searchArrayList.get(position);
	 }

	 public long getItemId(int position) {
	  return position;
	 }

	 public View getView(int position, View convertView, ViewGroup parent) {
	  ViewHolder holder;
	  if (convertView == null) {
	   convertView = mInflater.inflate(R.layout.customlist, null);
	   holder = new ViewHolder();
	   holder.txtName = (TextView) convertView.findViewById(R.id.name);
	   holder.txtDistance = (TextView) convertView.findViewById(R.id.txtdist);
	   holder.txtlocal = (TextView) convertView.findViewById(R.id.txtadrs);
	   convertView.setTag(holder);
	  } else {
	   holder = (ViewHolder) convertView.getTag();
	  }
	  
	  holder.txtName.setText(searchArrayList.get(position).getBank());
	  Double dist = (double) (searchArrayList.get(position).getDistance()/1000);
	 DecimalFormat dec = new DecimalFormat("#.##");
	 dec.setMinimumFractionDigits(2);
	String strdist = dec.format(dist);
	  //String strdist = String.valueOf(round(dist,2));
	  
	  //String strdist = Integer.toString(dist);
	  holder.txtDistance.setText(strdist+" km");
	 holder.txtlocal.setText(searchArrayList.get(position).getLocal());
	  return convertView;
	 }

	 public static double round(double valueToRound, int numberOfDecimalPlaces)
	 {
	     double multipicationFactor = Math.pow(10, numberOfDecimalPlaces);
	     double interestedInZeroDPs = valueToRound * multipicationFactor;
	     return Math.round(interestedInZeroDPs) / multipicationFactor;
	 }

	 static class ViewHolder {
	  TextView txtName;
	TextView txtDistance;
	TextView txtlocal;
	  
	  
	 }
	}