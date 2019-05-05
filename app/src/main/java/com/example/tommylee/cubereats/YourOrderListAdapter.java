package com.example.tommylee.cubereats;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class YourOrderListAdapter extends RecyclerView.Adapter<YourOrderListAdapter.MyViewHolder> {
    private ArrayList<Order> mDataset;
    private Context mContext;

    FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference colRef = db.collection("order");

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mealName, driverName, paidStatus;
        public MyViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public YourOrderListAdapter(ArrayList<Order> myDataset, Context mContext) {
        mDataset = myDataset;
        this.mContext = mContext;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public YourOrderListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.your_order_cell, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        holder.mealName = (TextView) view.findViewById(R.id.mealName);
        holder.driverName = (TextView) view.findViewById(R.id.driverName);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (currentFireBaseUser.getUid() == mDataset.get(position).getCustomerID()) {
            if (mDataset.get(position).getDriverID() != null) {
                holder.mealName.append(mDataset.get(position).getDriverID());
            } else {
                holder.mealName.append("Not yet taken");
            }
            if (mDataset.get(position).getPaid()) {
                holder.paidStatus.setText(R.string.paid);
            } else {
                holder.paidStatus.setText(R.string.unpaid);
            }

            for (int i = 0; i < mDataset.get(position).getMealID().size(); i++) {
                if (i == 0) {
                    holder.mealName.setText(mDataset.get(position).getMealID().get(i));
                    holder.mealName.append("\n");
                } else if (i > 0) {
                    holder.mealName.append(mDataset.get(position).getMealID().get(i));
                    holder.mealName.append("\n");
                }

            }
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        int count = 0;
        for (int i = 0; i < mDataset.size(); i++) {
            if (currentFireBaseUser.getUid() == mDataset.get(i).getCustomerID()) {
                count++;
            }
        }
        return count;
    }
}
