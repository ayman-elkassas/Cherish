package com.example.ayman.cherish.Model.models;

import android.net.Uri;

public class SetupDataAccount {
	
	private String fname,lname,phone;
	private Uri image_url;
	private String imageFriend;
	private String user_id;
	private String bio;
	
	public SetupDataAccount() {
	}
	
	public SetupDataAccount(String fname, String lname, String phone, Uri image_url) {
		this.fname = fname;
		this.lname = lname;
		this.phone = phone;
		this.image_url = image_url;
	}
	
	//friendlayout model overload const
	public SetupDataAccount(String user_id,String fname, String lname, String imageFriend) {
		this.fname = fname;
		this.lname = lname;
		this.imageFriend = imageFriend;
		this.user_id=user_id;
	}
	
	public String getBio() {
		return bio;
	}
	
	public void setBio(String bio) {
		this.bio = bio;
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
	
	public Uri getImage_url() {
		return image_url;
	}
	
	public void setImage_url(Uri image_url) {
		this.image_url = image_url;
	}
	
	public String getImageFriend() {
		return imageFriend;
	}
	
	public void setImageFriend(String imageFriend) {
		this.imageFriend = imageFriend;
	}
	
	public String getUser_id() {
		return user_id;
	}
	
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
}
