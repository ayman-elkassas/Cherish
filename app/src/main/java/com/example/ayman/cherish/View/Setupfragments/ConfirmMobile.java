package com.example.ayman.cherish.View.Setupfragments;


import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ayman.cherish.R;
import com.raycoarana.codeinputview.CodeInputView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmMobile extends Fragment {
	
	
	private android.widget.TextView textView5;
	private com.raycoarana.codeinputview.CodeInputView code;
	private android.widget.TextView textView6;
	private android.widget.TextView textView7;
	private android.widget.ImageView imageView2;
	private android.support.design.button.MaterialButton verify;
	
	public ConfirmMobile() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_confirm_mobile, container, false);

		this.verify = (MaterialButton) view.findViewById(R.id.verify);
		this.code = (CodeInputView) view.findViewById(R.id.code);
		
		return view;
	}
	
}
