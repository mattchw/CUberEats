package com.example.tommylee.cubereats;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RestaurantListAdapter extends RecyclerView.Adapter<RestaurantListAdapter.MyViewHolder> {
    private ArrayList<Restaurant> mDataset;
    private Context mContext;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView name;
        public ImageView iv;
        public TextView time;
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
    public RestaurantListAdapter(ArrayList<Restaurant> myDataset, Context mContext) {
        mDataset = myDataset;
        this.mContext = mContext;

    }

    // Create new views (invoked by the layout manager)
    @Override
    public RestaurantListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.res_cell, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.iv = (ImageView) view.findViewById(R.id.respic);
        holder.name = (TextView) view.findViewById(R.id.name);
        holder.time = (TextView) view.findViewById(R.id.time);

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.name.setText(mDataset.get(position).getName());
        holder.time.setText(mDataset.get(position).getTime());

        Glide
                .with(mContext)
                .load(mDataset.get(position).getImgurl())
                .centerCrop()
                .placeholder(R.drawable.loading_spinner)
                .into(holder.iv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(mContext,RestaurantDetailActivity.class);
                intent.putExtra("resid",mDataset.get(holder.getAdapterPosition()).getId());
                mContext.startActivity(intent);
            }
        });


    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
