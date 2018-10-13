package com.example.ayman.cherish.activities.accountSetup;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.fragments.ConfirmMobile;
import com.example.ayman.cherish.fragments.MoreInformation;

public class PageFragment extends Fragment {

    private TextView lblPage;

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
        else {
            final PageFragment fragment = new PageFragment();
            fragment.setArguments(args);
            return fragment;
        }
        
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_page, container, false);
        lblPage = (TextView) view.findViewById(R.id.lbl_page);
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