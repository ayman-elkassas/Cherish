package com.example.ayman.cherish.View.Profilefragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ayman.cherish.Model.adapters.TimelineParentRecyAdapter;
import com.example.ayman.cherish.Model.models.TimelineChildCardData;
import com.example.ayman.cherish.Model.models.TimelineParentCardData;
import com.example.ayman.cherish.Model.sharedClasses.SharedObjects;
import com.example.ayman.cherish.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyChildren extends Fragment {
	
	//vars
	RecyclerView recy_parent;
	TimelineParentRecyAdapter myAdapter;
	
	//Firebase
	FirebaseAuth mAuth;
	FirebaseFirestore firebaseFirestore;
	
	//Models
	//list of object model
	//parent card
	static ArrayList<TimelineParentCardData> timelineParentCardData =new ArrayList<>();
	static ArrayList<TimelineChildCardData> timelineChildCardDataList=new ArrayList<>();
	
	//last document to make load more post
	DocumentSnapshot lastVisibleAsChild,lastVisibleAsCherish;
	
	//solving problem if added post at runtime
	//because if anyone added post at runtime all posts again loaded ,so there are more duplicated posts
	Boolean isFirstPageFirstLoad = true;
	
	String currentUser;
	static int outerCount,innerCount,MaxOuter,maxInner=0;
	
	public MyChildren() {
		// Required empty public constructor
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		View view= inflater.inflate(R.layout.fragment_my_children,
				container, false);
		
		recy_parent=view.findViewById(R.id.recy_parent);
		
		//firebase
		mAuth=FirebaseAuth.getInstance();
		firebaseFirestore=FirebaseFirestore.getInstance();
		
		//get CurrentUser
		currentUser=mAuth.getCurrentUser().getUid();
		
		if (currentUser != null) {
			//listener if you reach in last scroll fire
			recy_parent.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
						
						Toast.makeText(getActivity(), "Load More", Toast.LENGTH_SHORT).show();
						loadMorePosts();
					}
					
				}
			});
		}
		
		//get All timeline for current user
		
		if(timelineParentCardData.isEmpty())
		{
			firebaseFirestore.collection("Users").document(currentUser)
					.collection("child")
					.limit(3)
					.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
						@Override
						public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
							if (!documentSnapshots.isEmpty()) {
								if(!documentSnapshots.getDocumentChanges().isEmpty())
								{
									MaxOuter=documentSnapshots.size();
									for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
										if (doc.getType() == DocumentChange.Type.ADDED) {
											//I want postId include in each object in list adapter
											//but in reality this field not in doc post object in DB
											//so after return post object from db
											//i will infect this object with any field(s) i want
											//with @Exclude feature firebase
											//i will create class with any field i want to infect return object doc model
											//and create method to initialize all infection fields
											//and return generic type contrast of basic class model doc object
											
											outerCount++;
											
											if (isFirstPageFirstLoad) {
												//to get last document get size and minus 1
												//size mean all previous document was show
												//compute last visible document to start with next doc in next cycle
												lastVisibleAsChild = documentSnapshots.getDocuments().get(
														documentSnapshots.getDocuments().size()-1
												);
											}
											
											Map<String,Object> childId  = doc.getDocument().getData();
											Map.Entry<String,Object> entry = childId.entrySet().iterator().next();
//											Toast.makeText(getActivity(), ""+childId, Toast.LENGTH_SHORT).show();
											String id=entry.getValue().toString();
											
											firebaseFirestore.collection("Cherish").document(id)
													.collection("Timeline")
													.orderBy("timestamp", Query.Direction.DESCENDING)
													.limit(3)
													.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
														@Override
														public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
															if (!documentSnapshots.isEmpty()) {
																if (!documentSnapshots.getDocumentChanges().isEmpty()) {
																	maxInner=documentSnapshots.size();
																	for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
																		if (doc.getType() == DocumentChange.Type.ADDED) {
																			
																			if (isFirstPageFirstLoad) {
																				//to get last document get size and minus 1
																				//size mean all previous document was show
																				//compute last visible document to start with next doc in next cycle
																				lastVisibleAsCherish = documentSnapshots.getDocuments().get(
																						documentSnapshots.getDocuments().size()-1
																				);
																			}
																			
																			//I want postId include in each object in list adapter
																			//but in reality this field not in doc post object in DB
																			//so after return post object from db
																			//i will infect this object with any field(s) i want
																			//with @Exclude feature firebase
																			//i will create class with any field i want to infect return object doc model
																			//and create method to initialize all infection fields
																			//and return generic type contrast of basic class model doc object
																			
																			String CherishNoteId = doc.getDocument().getId();
																			TimelineChildCardData childCardNote = doc.getDocument().toObject(TimelineChildCardData.class)
																					.withId(CherishNoteId);
																			
																			//then blogPost object now content ( basic doc post fields + infection fields )
																			
																			//notify change if any new post added to collection
																			//and update ArrayList adapter
																			
																			//if first time open profile
																			if (isFirstPageFirstLoad) {
																				timelineChildCardDataList.add(childCardNote);
																			} else {
																				//if new post add and listener fire
																				//add new post in head of list
																				timelineChildCardDataList.add(0, childCardNote);
																			}
																		}
																		
																		if(outerCount==MaxOuter)
																		{
																			innerCount++;
																		}
																		
																		if(innerCount==maxInner)
																		{
//																			Toast.makeText(getActivity(), ""+SharedObjects.timelineChildCardDataList.size(), Toast.LENGTH_SHORT).show();
																			
																			ArrayList<ArrayList<TimelineChildCardData>> collectionPair = getPairs(timelineChildCardDataList);
																			
																			if (!collectionPair.isEmpty()) {
																				
																				for (ArrayList<TimelineChildCardData> MemoriesTime : collectionPair) {
																					if (MemoriesTime.get(MemoriesTime.size() - 1).getTimestamp() != null) {
																						timelineParentCardData.add(
																								new TimelineParentCardData(
																										MemoriesTime.get(MemoriesTime.size() - 1).getTimestamp().getDate(),
																										String.valueOf(SharedObjects.months.get(MemoriesTime.get(MemoriesTime.size() - 1).getTimestamp().getMonth())),
																										MemoriesTime
																								));
																						
																						myAdapter.notifyDataSetChanged();
																						
																					}
																				}
																			}
																		}
																	}

//
																}
															}
														}
													});
										}
									}
								}
							}
						}
					});
		}
		
		recy_parent.setLayoutManager(new LinearLayoutManager(getActivity()));

		recy_parent.setHasFixedSize(false);

		myAdapter=new TimelineParentRecyAdapter(timelineParentCardData,getActivity(),getFragmentManager());
		recy_parent.setAdapter(myAdapter);
		
		recy_parent.setItemAnimator(new DefaultItemAnimator());
		
		recy_parent.getRecycledViewPool().setMaxRecycledViews(0, 0);
		
		recy_parent.smoothScrollBy(1, 0);
		
		return view;
	}
	
	private void loadMorePosts() {
		if (currentUser != null) {
			timelineChildCardDataList.clear();
			
			firebaseFirestore.collection("Users").document(currentUser)
					.collection("child")
					.startAfter(lastVisibleAsChild)
					.limit(3)
					.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
						@Override
						public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
							if (!documentSnapshots.isEmpty()) {
								if(!documentSnapshots.getDocumentChanges().isEmpty())
								{
									for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
										if (doc.getType() == DocumentChange.Type.ADDED) {
											//to get last document get size and minus 1
											//size mean all previous document was show
											//compute last visible document to start with next doc in next cycle
											lastVisibleAsChild = documentSnapshots.getDocuments().get(
													documentSnapshots.getDocuments().size()-1
											);
											
											Map<String,Object> childId  = doc.getDocument().getData();
											Map.Entry<String,Object> entry = childId.entrySet().iterator().next();
//											Toast.makeText(getActivity(), ""+childId, Toast.LENGTH_SHORT).show();
											String id=entry.getValue().toString();
											
											firebaseFirestore.collection("Cherish").document(id)
													.collection("Timeline")
													.orderBy("timestamp", Query.Direction.DESCENDING)
													.startAfter(lastVisibleAsCherish)
													.limit(3)
													.addSnapshotListener(getActivity(), new EventListener<QuerySnapshot>() {
														@Override
														public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
															if (!documentSnapshots.isEmpty()) {
																if (!documentSnapshots.getDocumentChanges().isEmpty()) {
																	for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
																		if (doc.getType() == DocumentChange.Type.ADDED) {
																			
																			//to get last document get size and minus 1
																			//size mean all previous document was show
																			//compute last visible document to start with next doc in next cycle
																			lastVisibleAsCherish = documentSnapshots.getDocuments().get(
																					documentSnapshots.getDocuments().size()-1
																			);
																			
																			//I want postId include in each object in list adapter
																			//but in reality this field not in doc post object in DB
																			//so after return post object from db
																			//i will infect this object with any field(s) i want
																			//with @Exclude feature firebase
																			//i will create class with any field i want to infect return object doc model
																			//and create method to initialize all infection fields
																			//and return generic type contrast of basic class model doc object
																			
																			
																			
																			String CherishNoteId = doc.getDocument().getId();
																			TimelineChildCardData childCardNote = doc.getDocument().toObject(TimelineChildCardData.class)
																					.withId(CherishNoteId);
																			
																			//then blogPost object now content ( basic doc post fields + infection fields )
																			
																			//notify change if any new post added to collection
																			//and update ArrayList adapter
																			
																			timelineChildCardDataList.add(childCardNote);
																		}
																	}
																	
																	ArrayList<ArrayList<TimelineChildCardData>> collectionPair = getPairs(timelineChildCardDataList);

																	if (!collectionPair.isEmpty()) {

																		for (ArrayList<TimelineChildCardData> MemoriesTime : collectionPair) {
																			if (MemoriesTime.get(MemoriesTime.size() - 1).getTimestamp() != null) {
																				timelineParentCardData.add(
																						new TimelineParentCardData(
																								MemoriesTime.get(MemoriesTime.size() - 1).getTimestamp().getDate(),
																								String.valueOf(SharedObjects.months.get(MemoriesTime.get(MemoriesTime.size() - 1).getTimestamp().getMonth())),
																								MemoriesTime
																						));
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
							}
						}
					});
			
		}
	}
	
	private ArrayList<ArrayList<TimelineChildCardData>> getPairs(ArrayList<TimelineChildCardData> timelineChildCardDataList) {
		
		ArrayList<ArrayList<TimelineChildCardData>> collectionPair = new ArrayList<>();
		
		//initialization
		ArrayList<TimelineChildCardData> initialArraylist = new ArrayList<>();
		initialArraylist.add(timelineChildCardDataList.get(0));
		collectionPair.add(initialArraylist);
		
		int indicator = 0;
		
		for (int i = 0; i < (timelineChildCardDataList.size()) - 1; i++) {
			
			try {
				if (timelineChildCardDataList.get(i).getTimestamp().getYear() ==
						timelineChildCardDataList.get(i + 1).getTimestamp().getYear()
						&&
						timelineChildCardDataList.get(i).getTimestamp().getMonth() ==
								timelineChildCardDataList.get(i + 1).getTimestamp().getMonth()
						&&
						timelineChildCardDataList.get(i).getTimestamp().getDay() ==
								timelineChildCardDataList.get(i + 1).getTimestamp().getDay()) {
					
					collectionPair.get(indicator).add(timelineChildCardDataList.get(i + 1));
					
				} else {
					ArrayList<TimelineChildCardData> newArrayList = new ArrayList<>();
					newArrayList.add(timelineChildCardDataList.get(i + 1));
					indicator++;
					collectionPair.add(indicator, newArrayList);
				}
			} catch (Exception ex) {
//				Toast.makeText(getActivity(), ""+ex.getMessage(), Toast.LENGTH_LONG).show();
			}
			
		}
		
		return collectionPair;
		
	}
	
}
