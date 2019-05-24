package com.example.ayman.cherish.View.Setupfragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ayman.cherish.Model.sharedClasses.SharedObjects;
import com.example.ayman.cherish.Presenter.MainPresenter;
import com.example.ayman.cherish.R;
import com.example.ayman.cherish.View.activities.Profile.MainLancher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class Done extends Fragment {

    private TextView lblPage;
    private MaterialButton goHome;
    
    //firebase objects
    FirebaseAuth auth;
    StorageReference storageReference;
    FirebaseFirestore firebaseFirestore;
    
    private String user_id;
    
//    public Activity activity;
    
    public static Fragment newInstance(int page, boolean isLast) {
        Bundle args = new Bundle();
        args.putInt("page", page);
        if (isLast)
            args.putBoolean("isLast", true);
        if(page==1)
        {
            final MoreInformation fragment = new MoreInformation();
            fragment.setArguments(args);
            return fragment;
        }
        else if(page==2)
        {
            final ConfirmMobile fragment = new ConfirmMobile();
            fragment.setArguments(args);
            return fragment;
        }
        else if(page==3)
        {
            final AddAvatar fragment = new AddAvatar();
            fragment.setArguments(args);
            return fragment;
        }
        else if(page==4)
        {
            final AddFriends fragment = new AddFriends();
            fragment.setArguments(args);
            return fragment;
        }
        else {
            final Done fragment = new Done();
            fragment.setArguments(args);
            return fragment;
        }
        
    }
    
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_done, container, false);
        lblPage = (TextView) view.findViewById(R.id.lbl_page);
        goHome=view.findViewById(R.id.goHome);
    
        //create firebase objects
        auth=FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseFirestore= FirebaseFirestore.getInstance();
        
        user_id=auth.getCurrentUser().getUid();
        
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(SharedObjects.finalVerifyFragment)
                {
    
                    final String user_id = auth.getCurrentUser().getUid();
    
                    //create profile image folder and image name defined with each user id
                    //unique avatar image with userId as name
                    StorageReference image_path = storageReference.child("profile_images")
                            .child(user_id + ".jpg");
    
                    image_path.putFile(MainPresenter.setupDataAccount.getImage_url()).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
            
                            if (task.isSuccessful()) {
                                //firestore document
                                storeFirestore(task);
                            } else {
                                String error = task.getException().getMessage();
                                Toast.makeText(getActivity(),
                                        "Error : " + error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                    
//                    Intent in=new Intent(getActivity(), MainLancher.class);
//                    startActivity(in);
//                    getActivity().finish();
                }
                else 
                {
                    Toast.makeText(getActivity(), "Complete setup information", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
        
        return view;
    }
    
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final int page = getArguments().getInt("page", 0);
        if (getArguments().containsKey("isLast"))
            lblPage.setText("You're done!");
        else
            lblPage.setText(Integer.toString(page));
    }
    
    private void storeFirestore(@NonNull Task<UploadTask.TaskSnapshot> task) {
        
        Uri download_uri;
        if(task!=null)
        {
            //get download url of image uploaded in firebase storage
            download_uri=task.getResult().getDownloadUrl();
    
            //prepare HashMap object
            Map<String,String> userMap=new HashMap<>();
            userMap.put("fname",MainPresenter.setupDataAccount.getFname());
            userMap.put("lname",MainPresenter.setupDataAccount.getLname());
            userMap.put("phone",MainPresenter.setupDataAccount.getPhone());
            userMap.put("image",download_uri.toString());
    
            firebaseFirestore= FirebaseFirestore.getInstance();
            
            firebaseFirestore.collection("Users").document(user_id)
                    .set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
    
//                        Intent mainIntent=new Intent(getContext(), MainLancher.class);
//                        startActivity(mainIntent);
                        firebaseFirestore.collection("Users").document(user_id).get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if(task.getResult().exists())
                                        {
                                            Intent in=new Intent(getActivity(), MainLancher.class);
                                            startActivity(in);
                                            getActivity().finish();
                                        }
                                    }
                                });
                    }
                    else {
                        String error=task.getException().getMessage();
                        Toast.makeText(getActivity(),
                                "FireStore Error : "+error, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
        
        
        
    }
    
}