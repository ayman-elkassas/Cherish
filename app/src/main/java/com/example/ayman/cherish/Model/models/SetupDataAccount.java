package com.example.ayman.cherish.Model.models;

public class SetupDataAccount {
	
	private String fname,lname,phone;
	private String image_url;
	
	public SetupDataAccount() {
	}
	
	public SetupDataAccount(String fname, String lname, String phone, String image_url) {
		this.fname = fname;
		this.lname = lname;
		this.phone = phone;
		this.image_url = image_url;
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
	
	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getImage_url() {
		return image_url;
	}
	
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
}
