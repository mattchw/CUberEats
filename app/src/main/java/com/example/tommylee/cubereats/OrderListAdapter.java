package com.example.tommylee.cubereats;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.widget.RecyclerView;
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
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.MyViewHolder> {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    CollectionReference orderColRef = db.collection("order");
    CollectionReference mealColRef = db.collection("meal");

    private String tempMealIDs;
    private String[] mealIDs;

    private ArrayList<Order> mDataset;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView deliverCustomerGeo;
        public TextView deliverMeals;
        public TextView takenStatus;
        public TextView ownOrder;

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
        holder.deliverCustomerGeo = (TextView) view.findViewById(R.id.deliver_customer_geo);
        holder.deliverMeals = (TextView) view.findViewById(R.id.deliver_meals);
        holder.takenStatus = (TextView) view.findViewById(R.id.takenStatus);
        holder.ownOrder = (TextView) view.findViewById(R.id.ownOrder);

        return holder;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        tempMealIDs = mDataset.get(position).getMealID().toString();
        tempMealIDs = tempMealIDs.replaceAll("[\\[\\]\\(\\)]", "");
        mealIDs = tempMealIDs.split("\\s*,\\s*");

        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.deliverCustomerGeo.append(getGeo(mDataset.get(position).getCustomerCoordinate()));

        holder.deliverMeals.append("\n");
        for (int i = 0; i < mealIDs.length; i++) {
            mealColRef.document(mealIDs[i]).get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            DocumentSnapshot document = task.getResult();
                            holder.deliverMeals.append(document.getData().get("name").toString());
                            holder.deliverMeals.append("\n");
                        }
                    });
        }

        if (mDataset.get(holder.getAdapterPosition()).getCustomerID().equals(currentFirebaseUser.getUid())) {
            holder.ownOrder.setVisibility(View.VISIBLE);
        }

        if (mDataset.get(position).getDriverID().equals(currentFirebaseUser.getUid())) {
            holder.takenStatus.setVisibility(View.VISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("delivery", "onClick");
                if (mDataset.get(holder.getAdapterPosition()).getCustomerID().equals(currentFirebaseUser.getUid())) {
                    Toast.makeText(mContext, "You can't make delivery for your own order!", Toast.LENGTH_SHORT).show();
                } else {
                    if (mDataset.get(holder.getAdapterPosition()).getDriverID().equals(currentFirebaseUser.getUid())) {
                        Intent intent = new Intent(mContext, MapDeliveryActivity.class);
                        intent.putExtra("orderID", mDataset.get(holder.getAdapterPosition()).getDocumentID());
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, OrderDeliveryDetailActivity.class);
                        intent.putExtra("orderID", mDataset.get(holder.getAdapterPosition()).getDocumentID());
                        mContext.startActivity(intent);
                    }
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    private String getGeo(GeoPoint geoPoint) {
        Geocoder geoCoder = new Geocoder(mContext);
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            return bestMatch.getAddressLine(0);
        } catch (IOException e) {

        }
        return null;
    }
}
