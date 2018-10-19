package com.example.ayman.cherish.models;

public class TimelineChildCardData {
	
	int iconTypePost;
	int colorIconTypePost;
	String title;
	String typePost;
	String location;
	String content;
	
	public TimelineChildCardData() {
	}
	
	public TimelineChildCardData(int iconTypePost, int colorIconTypePost, String title, String typePost, String location, String content) {
		this.iconTypePost = iconTypePost;
		this.colorIconTypePost = colorIconTypePost;
		this.title = title;
		this.typePost = typePost;
		this.location = location;
		this.content = content;
	}
	
	public int getIconTypePost() {
		return iconTypePost;
	}
	
	public void setIconTypePost(int iconTypePost) {
		this.iconTypePost = iconTypePost;
	}
	
	public int getColorIconTypePost() {
		return colorIconTypePost;
	}
	
	public void setColorIconTypePost(int colorIconTypePost) {
		this.colorIconTypePost = colorIconTypePost;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getTypePost() {
		return typePost;
	}
	
	public void setTypePost(String typePost) {
		this.typePost = typePost;
	}
	
	public String getLocation() {
		return location;
	}
	
	public void setLocation(String location) {
		this.location = location;
	}
	
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
