package com.example.ayman.cherish.View.Profilefragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.Model.adapters.TimelineParentRecyAdapter;
import com.example.ayman.cherish.Model.models.TimelineChildCardData;
import com.example.ayman.cherish.Model.models.TimelineParentCardData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class Timeline extends Fragment {
	
	RecyclerView recy_parent;
	TimelineParentRecyAdapter myAdapter;
	
	//Firebase
	FirebaseAuth mAuth;
	FirebaseFirestore firebaseFirestore;
	
	//Models
	//list of object model
	private ArrayList<TimelineChildCardData> timelineChildCardDataList=new ArrayList<>();
	
	//last document to make load more post
	private DocumentSnapshot lastVisible;
	
	//solving problem if added post at runtime
	//because if anyone added post at runtime all posts again loaded ,so there are more duplicated posts
	Boolean isFirstPageFirstLoad=true;
	
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
		
		//firebase
		mAuth=FirebaseAuth.getInstance();
		firebaseFirestore=FirebaseFirestore.getInstance();
		
		//get CurrentUser
		String currentUser=mAuth.getCurrentUser().getUid();
		
		//parent card
		final ArrayList<TimelineParentCardData> timelineParentCardData =new ArrayList<>();
		
		//sub card
		final ArrayList<TimelineChildCardData> timelineChildCardData =new ArrayList<>();
		
		//get All timeline for current user
		
		firebaseFirestore.collection("Cherish").document(currentUser)
				.collection("Notes")
				.orderBy("timestamp", Query.Direction.DESCENDING)
				.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
					@Override
					public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
						if(!documentSnapshots.isEmpty())
						{
							for(DocumentChange doc:documentSnapshots.getDocumentChanges())
							{
								if(doc.getType()==DocumentChange.Type.ADDED)
								{
									//I want postId include in each object in list adapter
									//but in reality this field not in doc post object in DB
									//so after return post object from db
									//i will infect this object with any field(s) i want
									//with @Exclude feature firebase
									//i will create class with any field i want to infect return object doc model
									//and create method to initialize all infection fields
									//and return generic type contrast of basic class model doc object
									String CherishNoteId=doc.getDocument().getId();
									TimelineChildCardData childCardNote=doc.getDocument().toObject(TimelineChildCardData.class)
											.withId(CherishNoteId);
									
									//then blogPost object now content ( basic doc post fields + infection fields )
									
									//notify change if any new post added to collection
									//and update ArrayList adapter
									
									timelineChildCardDataList.add(childCardNote);
									
//									myAdapter.notifyDataSetChanged();
									
								}
							}
							timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardDataList));
							timelineParentCardData.add(new TimelineParentCardData(16, "MAY",timelineChildCardDataList));
						}
					}
				});
		
		
		recy_parent.setLayoutManager(new LinearLayoutManager(getActivity()));

		recy_parent.setHasFixedSize(false);

		myAdapter=new TimelineParentRecyAdapter(timelineParentCardData,getActivity());
		recy_parent.setAdapter(myAdapter);

		recy_parent.setItemAnimator(new DefaultItemAnimator());
		
		return view;
	}
	
}
