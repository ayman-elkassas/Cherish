package com.example.ayman.cherish.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayman.cherish.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInformation extends Fragment {
	
	public MoreInformation() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_more_information, container, false);
		
		return view;
	}
	
}
