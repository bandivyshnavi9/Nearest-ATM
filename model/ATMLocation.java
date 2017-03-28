package com.money.model;

public class ATMLocation {

	private String bank;
 	private String lat;
 	private String lng;
 	private String id;
 	public Integer distance;
 	private String local;
 	private String datecreated;
 	
 	public String getBank() {
 		return bank;
 	}
 	public void setBank(String bank) {
 		this.bank =bank;
 	}
 	public String getLat() {
 		return lat;
 	}
 	public void setLat(String lat) {
 		this.lat = lat;
 	}
 	public String getLng() {
 		return lng;
 	}
 	public void setLng(String lng) {
 		this.lng = lng;
 	}
 	public String getId() {
 		return id;
 	}
 	public void setId(String id) {
 		this.id = id;
 	}
 	public Integer getDistance() {
 		return distance;
 	}
 	public void setDistance(Integer distance) {
 		this.distance =distance;
 	}
 	public String getLocal() {
 		return local;
 	}
 	public void setLocal(String local) {
 		this.local =local;
 	}
 	public String getDateCreated() {
 		return datecreated;
 	}
 	public void setDateCreated(String datecreated) {
 		this.datecreated =datecreated;
 	}
 	
 	public ATMLocation(String bank, String lat, String lng, String id, Integer distance,String local,String datecreated) {
 		this.setBank(bank);
  		this.setLat(lat);
 		this.setLng(lng);
 		this.setId(id);
 		this.setDistance(distance);
 		this.setLocal(local);
 		this.setDateCreated( datecreated);
 	}
 }
