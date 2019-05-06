package com.example.tommylee.cubereats;

import android.content.Context;
import android.content.Intent;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class YourOrderListAdapter extends RecyclerView.Adapter<YourOrderListAdapter.MyViewHolder> {
    private ArrayList<Order> mDataset;
    private Context mContext;

    private double total = 0.0;
    int counter = 0;

    FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderColRef = db.collection("order");
    CollectionReference mealColRef = db.collection("meal");
    CollectionReference userColRef = db.collection("user");

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mealName, driverName, paidStatus, totalPrice;

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
        holder.paidStatus = (TextView) view.findViewById(R.id.payStatus);
        holder.totalPrice = (TextView) view.findViewById(R.id.totalPrice);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Log.e("order getid", "onBindViewHolder: " + mDataset.get(position).getDocumentID());
        Log.e("order getpaid", "onBindViewHolder: " + mDataset.get(position).getIsPaid());

        if (mDataset.get(position).getDriverID() == "") {
            holder.driverName.append("Not yet taken");
        } else {
            userColRef.document(mDataset.get(position).getDriverID()).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    Log.d("drivername", "DocumentSnapshot data: " + document.getData());
                                    holder.driverName.append(document.getData().get("name").toString());
                                } else {
                                    Log.d("error", "No such document");
                                }
                            } else {
                                Log.d("error", "get failed with ", task.getException());
                            }
                        }
                    });
        }
        if (mDataset.get(position).getIsPaid()) {
            holder.paidStatus.setText(R.string.paid);
        } else {
            holder.paidStatus.setText(R.string.unpaid);
        }

        for (int i = 0; i < mDataset.get(position).getMealID().size(); i++) {
            mealColRef.document(mDataset.get(position).getMealID().get(i))
                    .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Log.e("doc", "DocumentSnapshot data: " + document.getData());
                             Log.e("doc", document.getData().get("name").toString());
                             counter++;
                            holder.mealName.append(" - "+document.getData().get("name").toString());
                            holder.mealName.append("\n");
                            total += Double.parseDouble(document.getData().get("price").toString());
                            if (counter == mDataset.get(position).getMealID().size()) {
                                holder.totalPrice.append(""+total);
                            }
                        } else {
                            Log.d("error", "No such document");
                        }
                    } else {
                        Log.d("fail", "get failed with ", task.getException());
                    }
                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("order getpaid", "onBindViewHolder: " + mDataset.get(holder.getAdapterPosition()).getIsPaid());
                Log.e("order docid", mDataset.get(holder.getAdapterPosition()).getDocumentID());
                if (mDataset.get(holder.getAdapterPosition()).getIsPaid()) {
                    Log.e("PAID", "onClick");
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    intent.putExtra("mapMode", "customer");
                    intent.putExtra("orderID", mDataset.get(holder.getAdapterPosition()).getDocumentID());

                    mContext.startActivity(intent);
                } else {
                    Log.e("unpaid", "onClick");
                    Intent intent = new Intent(mContext, YourOrderPayment.class);
                    intent.putExtra("orderID", mDataset.get(holder.getAdapterPosition()).getDocumentID());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    // Return the size of your data set (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
