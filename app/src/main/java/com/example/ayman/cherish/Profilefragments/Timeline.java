package com.example.ayman.cherish.Profilefragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.adapters.TimelineParentRecyAdapter;
import com.example.ayman.cherish.models.TimelineChildCardData;
import com.example.ayman.cherish.models.TimelineParentCardData;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class Timeline extends Fragment {
	
	RecyclerView recy_parent;
	
	public Timeline() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view= inflater.inflate(R.layout.fragment_timeline,
				container, false);
		
		recy_parent=view.findViewById(R.id.recy_parent);
		
		//parent card
		ArrayList<TimelineParentCardData> timelineParentCardData =new ArrayList<>();
		
		//sub card
		ArrayList<TimelineChildCardData> timelineChildCardData =new ArrayList<>();
		timelineChildCardData.add(new TimelineChildCardData(
				R.drawable.timelinesmscard,
				R.color.sms_color,
				"Old Man?!",
				"Note",
				"Amesterdam",
				"I'm creating a chat app and I'm thinking on ways to" +
						" create the actual chat view.I already have the " +
						"layout for the chat window itself but I was thinking"
		));
		
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardData));
		
		recy_parent.setLayoutManager(new LinearLayoutManager(getContext()));
		TimelineParentRecyAdapter myAdapter=new TimelineParentRecyAdapter(timelineParentCardData,getContext());
		
		myAdapter.notifyDataSetChanged();
		
		recy_parent.setAdapter(myAdapter);

		recy_parent.setItemAnimator(new DefaultItemAnimator());
		
		return view;
	}
	
}
