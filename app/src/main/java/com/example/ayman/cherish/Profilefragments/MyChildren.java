package com.example.ayman.cherish.Profilefragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayman.cherish.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChildren extends Fragment {
	
	
	public MyChildren() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_my_children, container, false);
	}
	
}
