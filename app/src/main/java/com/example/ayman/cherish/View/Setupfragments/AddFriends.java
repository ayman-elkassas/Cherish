package com.example.ayman.cherish.View.Setupfragments;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.ayman.cherish.Model.adapters.FriendAdapter;
import com.example.ayman.cherish.Model.adapters.TimelineParentRecyAdapter;
import com.example.ayman.cherish.Model.models.SetupDataAccount;
import com.example.ayman.cherish.Model.models.TimelineChildCardData;
import com.example.ayman.cherish.Model.models.TimelineParentCardData;
import com.example.ayman.cherish.R;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class AddFriends extends Fragment {
	
	//vars
	public static RecyclerView friend_recy;
	FriendAdapter friendAdapter;
	
	//Firebase
	FirebaseAuth mAuth;
	FirebaseFirestore firebaseFirestore;
	
	String currentUser;
	
	int itemClicked=0;
	
	//last document to make load more post
	private DocumentSnapshot lastVisible;
	
	//Models
	//list of object model
	ArrayList<SetupDataAccount> friendsObjects = new ArrayList<>();
	
	//solving problem if added post at runtime
	//because if anyone added post at runtime all posts again loaded ,so there are more duplicated posts
	Boolean isFirstPageFirstLoad = true;
	
	public AddFriends() {
		// Required empty public constructor
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view = inflater.inflate(R.layout.fragment_add_friends, container, false);
		
		friend_recy = view.findViewById(R.id.friend_recy);
		
		//firebase
		mAuth = FirebaseAuth.getInstance();
		firebaseFirestore = FirebaseFirestore.getInstance();
		
		//get CurrentUser
		currentUser = mAuth.getCurrentUser().getUid();
		
		if (currentUser != null) {
			//listener if you reach in last scroll fire
			friend_recy.addOnScrollListener(new RecyclerView.OnScrollListener() {
				@Override
				public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
					super.onScrolled(recyclerView, dx, dy);
					
					//boolean to check if now reach in bottom
					Boolean reachedBottom = !recyclerView.canScrollVertically(1);
					if (reachedBottom) {
//						String desc=lastVisible.getString("desc");
//						Toast.makeText(container.getContext(), "Reached : "+desc,
//								Toast.LENGTH_LONG).show();
						
						//fire load more post
						
						loadMorePosts();
					}
					
				}
			});
		}
		
		firebaseFirestore.collection("Users")
				.limit(10)
				.get()
				.addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
			@Override
			public void onComplete(@NonNull Task<QuerySnapshot> task) {
				if (task.isSuccessful()) {
					if (task.getResult().getDocuments() != null) {
						
						if (isFirstPageFirstLoad) {
							//to get last document get size and minus 1
							//size mean all previous document was show
							//compute last visible document to start with next doc in next cycle
							lastVisible = task.getResult().getDocuments()
									.get(task.getResult().getDocuments().size() - 1);
							
							//in first time only open profile
							//remove all content of list before initialize
							friendsObjects.clear();
						}
						
						for (DocumentSnapshot doc : task.getResult().getDocuments()) {
							
							if(doc.getId()!=currentUser)
							{
								SetupDataAccount setupDataAccount = new SetupDataAccount();
								setupDataAccount.setUser_id(doc.getId());
								setupDataAccount.setFname(doc.getString("fname"));
								setupDataAccount.setLname(doc.getString("lname"));
								setupDataAccount.setImageFriend(doc.getString("image"));
								
								//then blogPost object now content ( basic doc post fields + infection fields )
								
								//if first time open profile
								if (isFirstPageFirstLoad) {
									friendsObjects.add(setupDataAccount);
								} else {
									//if new post add and listener fire
									//add new post in head of list
									friendsObjects.add(0, setupDataAccount);
								}
								
								//notify change if any new post added to collection
								//and update ArrayList adapter
//							    friendAdapter.notifyDataSetChanged();
							}
						}
						
					}
					
				}
			}
		});
		
		friend_recy.setLayoutManager(new LinearLayoutManager(getActivity()));
		
		friend_recy.setHasFixedSize(true);
		
		friendAdapter = new FriendAdapter(friendsObjects, getActivity());
		friend_recy.setAdapter(friendAdapter);
		
		friend_recy.setItemAnimator(new DefaultItemAnimator());
		friend_recy.getRecycledViewPool().setMaxRecycledViews(0, 0);
		
		friend_recy.smoothScrollBy(1, 0);
		
		return view;
	}
	
	private void loadMorePosts() {
		if (currentUser != null) {

			firebaseFirestore.collection("Users")
					.startAfter(lastVisible)
					.limit(10)
					.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
						@Override
						public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
							if (!documentSnapshots.isEmpty()) {
								if (documentSnapshots != null) {
									//if documentsnapshots is empty stop
									if (!documentSnapshots.isEmpty()) {
										//compute new last document to start next cycle
										lastVisible = documentSnapshots.getDocuments()
												.get(documentSnapshots.size() - 1);

										for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
											if (doc.getType() == DocumentChange.Type.ADDED) {
												SetupDataAccount setupDataAccount = new SetupDataAccount();
												setupDataAccount.setUser_id(doc.getDocument().getId());
												setupDataAccount.setFname(doc.getDocument().getString("fname"));
												setupDataAccount.setLname(doc.getDocument().getString("lname"));
												setupDataAccount.setImageFriend(doc.getDocument().getString("image"));

												friendsObjects.add(setupDataAccount);

//												friendAdapter.notifyDataSetChanged();
											}
										}
									}
								}
							}
						}
					});
		}
	}
}
