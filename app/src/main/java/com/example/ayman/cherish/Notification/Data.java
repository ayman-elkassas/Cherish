package com.example.ayman.cherish.Notification;

import com.google.firebase.firestore.FieldValue;

import java.util.Date;

public class Data {
	
	private String user;
	private int icon;
	private String body;
	private String title;
	private String sented;
	
	private String cherishCommentId;
	private String timestampComment;
	
	public Data() {
	}
	
	public Data(String user, int icon, String body, String title, String sented, String cherishCommentId, String timestampComment) {
		this.user = user;
		this.icon = icon;
		this.body = body;
		this.title = title;
		this.sented = sented;
		this.cherishCommentId = cherishCommentId;
		this.timestampComment = timestampComment;
	}
	
	public String getUser() {
		return user;
	}
	
	public void setUser(String user) {
		this.user = user;
	}
	
	public int getIcon() {
		return icon;
	}
	
	public void setIcon(int icon) {
		this.icon = icon;
	}
	
	public String getBody() {
		return body;
	}
	
	public void setBody(String body) {
		this.body = body;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getSented() {
		return sented;
	}
	
	public void setSented(String sented) {
		this.sented = sented;
	}
	
	public String getCherishCommentId() {
		return cherishCommentId;
	}
	
	public void setCherishCommentId(String cherishCommentId) {
		this.cherishCommentId = cherishCommentId;
	}
	
	public String getTimestampComment() {
		return timestampComment;
	}
	
	public void setTimestampComment(String timestampComment) {
		this.timestampComment = timestampComment;
	}
}
