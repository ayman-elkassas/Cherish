package com.example.ayman.cherish.View.Setupfragments;


import android.os.Bundle;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.Model.sharedClasses.SharedObjects;
import com.example.ayman.cherish.Presenter.MainPresenter;
import com.example.ayman.cherish.R;
import com.raycoarana.codeinputview.CodeInputView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConfirmMobile extends Fragment implements View.OnClickListener {
	
	private com.raycoarana.codeinputview.CodeInputView code;
	private android.support.design.button.MaterialButton sendCode;
	
	String phoneNo = "";
	
	//MVP
	private MainPresenter presenter;
	private TextView retrySms;
	private MaterialButton verify;
	
	public ConfirmMobile() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_confirm_mobile, container, false);
		this.verify = (MaterialButton) view.findViewById(R.id.verify);
		this.sendCode = (MaterialButton) view.findViewById(R.id.sendCode);
		this.code = (CodeInputView) view.findViewById(R.id.code);
		this.retrySms = (TextView) view.findViewById(R.id.retrySms);
		
		sendCode.setOnClickListener(this);
		retrySms.setOnClickListener(this);
		verify.setOnClickListener(this);
		
		//TODO:1-CREATE INSTANCE FROM PRESENTER CLASS
		presenter = new MainPresenter(this, getContext());
		
		return view;
	}
	
	@Override
	public void onClick(View v) {
		if (v == sendCode) {
			presenter.verifyMobileNum(MoreInformation.fireMoreInfo(), getActivity());
		} else if (v == retrySms) {
			presenter.resendCode(MoreInformation.fireMoreInfo(), getActivity());
		}
		else if(v==verify)
		{
			if(code.getCode().isEmpty())
			{
				Toast.makeText(getActivity(), "Code empty", Toast.LENGTH_SHORT).show();
			}
			else
			{
				if(code.getCode().equals(SharedObjects.verifyCode))
				{
					SharedObjects.finalVerifyFragment=true;
					Toast.makeText(getActivity(), "Done verify", Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
}
