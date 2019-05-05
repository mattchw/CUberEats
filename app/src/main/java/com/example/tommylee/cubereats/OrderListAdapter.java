package com.example.tommylee.cubereats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    private ArrayList<Order> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView customerID;
        public TextView driverID;
        public MyViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OrderListAdapter(ArrayList<Order> myDataset, Context mContext) {
        mDataset = myDataset;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OrderListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        // create a new view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.order_cell, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.customerID = (TextView) view.findViewById(R.id.customerID);
        holder.driverID = (TextView) view.findViewById(R.id.driverID);

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.customerID.setText(mDataset.get(position).getCustomerID());
        holder.driverID.setText(mDataset.get(position).getDriverID());

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
