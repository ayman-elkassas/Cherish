package com.example.ayman.cherish.Model.models;

public class NotificationModel {
	
	private String user_id;
	private String image_url;
	private String typeNotify;
	private String fname,lname;
	private String bodyNotify;
	
	public NotificationModel() {
	}
	
	public NotificationModel(String user_id, String image_url, String typeNotify, String fname, String lname, String bodyNotify) {
		this.user_id = user_id;
		this.image_url = image_url;
		this.typeNotify = typeNotify;
		this.fname = fname;
		this.lname = lname;
		this.bodyNotify = bodyNotify;
	}
	
	public String getUser_id() {
		return user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	
	public String getImage_url() {
		return image_url;
	}
	
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
	public String getTypeNotify() {
		return typeNotify;
	}
	
	public void setTypeNotify(String typeNotify) {
		this.typeNotify = typeNotify;
	}
	
	public String getFname() {
		return fname;
	}
	
	public void setFname(String fname) {
		this.fname = fname;
	}
	
	public String getLname() {
		return lname;
	}
	
	public void setLname(String lname) {
		this.lname = lname;
	}
	
	public String getBodyNotify() {
		return bodyNotify;
	}
	
	public void setBodyNotify(String bodyNotify) {
		this.bodyNotify = bodyNotify;
	}
}
