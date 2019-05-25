package com.example.ayman.cherish.View.customViews;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.example.ayman.cherish.Model.adapters.LikersAdapter;
import com.example.ayman.cherish.Model.models.Likers;
import com.example.ayman.cherish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class CustomLikeCherishUsers extends DialogFragment {
	
	//vars
	public static RecyclerView liker_recy;
	static LikersAdapter likersAdapter;
	
	//Models
	//list of object model
	public static ArrayList<Likers> likersObjects;
	
	public CustomLikeCherishUsers() {
		// Required empty public constructor
	}
	
	@NonNull
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		
		LayoutInflater inflater = getActivity().getLayoutInflater();
		
		View view=inflater.inflate(R.layout.likers_dialogue, null);
		
		builder.setView(view);
		
		liker_recy = view.findViewById(R.id.liker_recy);
		liker_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
		liker_recy.setHasFixedSize(true);
		liker_recy.setItemAnimator(new DefaultItemAnimator());
		liker_recy.getRecycledViewPool().setMaxRecycledViews(0, 0);
		
		liker_recy.smoothScrollBy(1, 0);
		
		likersAdapter=new LikersAdapter(likersObjects,getContext());
		liker_recy.setAdapter(likersAdapter);
		
		return builder.create();
	}
}
