package com.example.ayman.cherish.View.Profilefragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.Model.adapters.NotifyAdapter;
import com.example.ayman.cherish.Model.models.NotificationModel;
import com.example.ayman.cherish.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Notifications extends Fragment {
	
	//vars
	public static RecyclerView notification_recy;
	public static NotifyAdapter notifyAdapter=new NotifyAdapter();
	
	public static TextView alertext;
	public static ImageView noalertimage;
	
	//Models
	//list of object model
	public static ArrayList<NotificationModel> notificationModelArrayList
			= new ArrayList();
	
	public Notifications() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view= inflater.inflate(R.layout.fragment_notifications, container, false);
		
		notification_recy = view.findViewById(R.id.notification_recy);
		alertext = view.findViewById(R.id.alerttext);
		noalertimage = view.findViewById(R.id.noalertimage);
		
//		Toast.makeText(getActivity(), "Aloo"+notificationModelArrayList.get(0)
//				.getUser_id(), Toast.LENGTH_SHORT).show();
		notifyAdapter.setContext(getActivity());
		notifyAdapter.setNotifysArrayList(notificationModelArrayList);
		notifyAdapter.setFragmentManager(getActivity().getSupportFragmentManager());
		
		notification_recy.setAdapter(notifyAdapter);
		
		notification_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
		notification_recy.setHasFixedSize(true);
		notification_recy.setItemAnimator(new DefaultItemAnimator());
		notification_recy.getRecycledViewPool().setMaxRecycledViews(0, 0);
		notification_recy.smoothScrollBy(1, 0);
		
		return view;
	}
	
}
