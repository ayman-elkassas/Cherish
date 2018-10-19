package com.example.ayman.cherish.customViews;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;

import com.example.ayman.cherish.R;

public class CustomDialogue extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());


        LayoutInflater inflater=getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.new_note,null));
        
        return builder.create();
    }
}
