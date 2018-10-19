package com.example.ayman.cherish.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.ayman.cherish.R;
import com.example.ayman.cherish.models.TimelineChildCardData;
import com.example.ayman.cherish.models.TimelineParentCardData;

import java.util.ArrayList;

public class TimelineParentRecyAdapter extends RecyclerView.Adapter<TimelineParentRecyAdapter.ViewHolder>
{
    private ArrayList<TimelineParentCardData> timelineParentCardData;
    Context context;

    public TimelineParentRecyAdapter(ArrayList<TimelineParentCardData> timelineParentCardData, Context con) {
        this.timelineParentCardData = timelineParentCardData;
        this.context=con;
    }
    
    @NonNull
    @Override
    public TimelineParentRecyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    
        View itemLayout= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.parent_recy_timelinefragment,null);
    
        ViewHolder viewHolder=new ViewHolder(itemLayout);
    
        viewHolder.setIsRecyclable(false);

        return viewHolder;
    }
    
    @Override
    public void onBindViewHolder(@NonNull TimelineParentRecyAdapter.ViewHolder holder, int position) {
	
	    holder.setIsRecyclable(false);
	
	    holder.day.setText(String.valueOf(timelineParentCardData.get(position).getDay()));
        holder.month.setText(timelineParentCardData.get(position).getMonth());
        
        holder.childRecy.setLayoutManager(new LinearLayoutManager(context));

        TimelineChildRecyAdapter myAdapter=new TimelineChildRecyAdapter(timelineParentCardData.get(position).getTimelineChildCardData(),context);
        holder.childRecy.setAdapter(myAdapter);

        holder.childRecy.setItemAnimator(new DefaultItemAnimator());
        
    }
    
    @Override
    public int getItemCount() {
        return timelineParentCardData.size();
    }
    
    public  class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView day;
        TextView month;
        
        RecyclerView childRecy;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day=itemView.findViewById(R.id.day);
            month=itemView.findViewById(R.id.month);
    
            childRecy=itemView.findViewById(R.id.childRecy);
        }
    }
}
