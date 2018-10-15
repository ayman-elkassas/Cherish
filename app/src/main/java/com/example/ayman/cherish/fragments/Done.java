package com.example.ayman.cherish.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.button.MaterialButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayman.cherish.R;

public class Done extends Fragment {

    private TextView lblPage;
    private MaterialButton goHome;

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
        
        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            
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
}