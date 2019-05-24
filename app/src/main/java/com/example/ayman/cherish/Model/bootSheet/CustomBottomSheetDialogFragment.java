package com.example.ayman.cherish.Model.bootSheet;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.ayman.cherish.R;

import java.util.ArrayList;

@SuppressLint("ValidFragment")
public class CustomBottomSheetDialogFragment extends BottomSheetDialogFragment {
    
    ArrayList<String> basicInfo=new ArrayList<>();
    Context con;
    
    @SuppressLint("ValidFragment")
    public CustomBottomSheetDialogFragment(Context con) {
        //get from sharedP..
        //TODO:PRIVATE MODE 0
        this.con=con;
        SharedPreferences pref=con.getSharedPreferences("basicInfo",0);
        basicInfo.add(pref.getString("fname",""));
        basicInfo.add(pref.getString("lname",""));
        basicInfo.add(pref.getString("image",""));
    }
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.dialog_modal, null);
        dialog.setContentView(contentView);
        
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        
        TextView username=contentView.findViewById(R.id.usernameSHB);
        username.setText(basicInfo.get(0)+" "+basicInfo.get(1));
    
        ImageView avatar=contentView.findViewById(R.id.avatarSHB);
    
        //to put into imageView until download image url
        RequestOptions placeholderOption=new RequestOptions();
        placeholderOption.placeholder(R.drawable.def_avatar);
    
        Glide.with(con)
                .applyDefaultRequestOptions(placeholderOption)
                .load(basicInfo.get(2))
                .into(avatar);
        
    }
}
