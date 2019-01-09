package com.example.ayman.cherish.Model.models;

import android.support.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

//this class will extend from basic model class
public class CherishNoteId {
	
	//this is a way to infection any model using firebase @exclude feature
	@Exclude
	public String cherishNoteId;
	
	public <T extends CherishNoteId> T withId(@NonNull final String id) {
		this.cherishNoteId = id;
		//then return generic object this object will include all fields
		//in model and infection fields
		return (T) this;
	}
	
}
