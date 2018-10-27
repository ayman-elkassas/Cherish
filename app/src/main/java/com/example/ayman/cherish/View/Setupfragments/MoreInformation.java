package com.example.ayman.cherish.View.Setupfragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.chip.Chip;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.util.Util;
import com.example.ayman.cherish.MainMVP.MainMVPInterfaceComponent;
import com.example.ayman.cherish.Presenter.MainPresenter;
import com.example.ayman.cherish.R;

import studio.carbonylgroup.textfieldboxes.ExtendedEditText;
import studio.carbonylgroup.textfieldboxes.TextFieldBoxes;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoreInformation extends Fragment implements MainMVPInterfaceComponent.IView {
	
	private studio.carbonylgroup.textfieldboxes.ExtendedEditText firstName;
	private studio.carbonylgroup.textfieldboxes.ExtendedEditText lastName;
	private studio.carbonylgroup.textfieldboxes.ExtendedEditText phone;
	
	Boolean checkEmpty=false;
	
	private MainPresenter presenter;
	
	public MoreInformation() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view=inflater.inflate(R.layout.fragment_more_information, container, false);
		
		this.phone = (ExtendedEditText) view.findViewById(R.id.phone);
		this.lastName = (ExtendedEditText) view.findViewById(R.id.lastName);
		this.firstName = (ExtendedEditText) view.findViewById(R.id.firstName);
		
		//TODO:1-CREATE INSTANCE FROM PRESENTER CLASS
		presenter=new MainPresenter(this);
		
		return view;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		String fname=firstName.getText().toString();
		String lname=lastName.getText().toString();
		String phoneNo=phone.getText().toString();
		
		if(!TextUtils.isEmpty(fname) && !TextUtils.isEmpty(lname)
				&& !TextUtils.isEmpty(phoneNo)) {
			presenter.getMoreInfoData(fname,lname,phoneNo);
		}
		else
		{
			Toast.makeText(getActivity(), "Fields empty", Toast.LENGTH_SHORT)
					.show();
		}
	}
}
