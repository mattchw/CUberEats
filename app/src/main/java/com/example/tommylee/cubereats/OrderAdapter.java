package com.example.tommylee.cubereats;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    private ArrayList<Meal> mDataset;
    private Context mContext;
    private Activity activity;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView iv;
        public TextView description;
        public TextView price;
        View itemView;
        public MyViewHolder(View v) {
            super(v);
            this.itemView = v;

        }

    }
    private OnItemClickListener mOnItemClickListener;
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }


    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderAdapter(ArrayList<Meal> myDataset, Context mContext,Activity activity) {
        mDataset = myDataset;
        this.mContext = mContext;
        this.activity = activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                       int viewType) {
        // create a new view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.order_pay_cell, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.name = (TextView) view.findViewById(R.id.info_text);
        holder.price = (TextView) view.findViewById(R.id.price);
        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset.get(position).getName());
        holder.price.append(String.valueOf(mDataset.get(position).getPrice()));



    }


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
