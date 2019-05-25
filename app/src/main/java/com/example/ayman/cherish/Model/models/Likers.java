package com.example.ayman.cherish.Model.models;

public class Likers {
	
	private String user_id;
	private String image_url;
	private String bio;
	private String fname,lname;
	
	private Boolean flag;
	
	public Likers() {
	}
	
	public Likers(String user_id, String image_url, String bio, String fname, String lname, Boolean flag) {
		this.user_id = user_id;
		this.image_url = image_url;
		this.bio = bio;
		this.fname = fname;
		this.lname = lname;
		this.flag = flag;
	}
	
	public Boolean getFlag() {
		return flag;
	}
	
	public void setFlag(Boolean flag) {
		this.flag = flag;
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
}
