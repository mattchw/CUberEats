package com.example.tommylee.cubereats;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class RestaurantDetailActivity extends AppCompatActivity {
    String id;
    String name;
    RecyclerView recyclerView;

    ArrayList<Meal> dataset=new ArrayList<>();
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id=extras.getString("resid");
            name=extras.getString("name");

            getSupportActionBar().setTitle(name);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getMeals();
            // and get whatever type user account id is
        }

    }
    private void initAdapter(){
        recyclerView = (RecyclerView) findViewById(R.id.detail_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        Log.d("dataser",dataset.size()+"");
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MealAdapter(dataset,getApplicationContext(),this);
        recyclerView.setAdapter(mAdapter);
    }

    private void getMeals()
    {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Source source = Source.CACHE;

        Query colRef = db.collection("meal").whereEqualTo("restaurantID",id);
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {

                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d("detailx", document.getId() + " => " + document.getData());

                        Meal notifPojo = new Meal();
                        notifPojo.setImgurl(document.get("imgurl").toString());
                        notifPojo.setName(document.get("name").toString());
                        notifPojo.setPrice(Double.parseDouble(document.get("price").toString()));
                        notifPojo.setDescription("HKD "+document.get("description").toString());
                        notifPojo.setId(document.getId());
                        dataset.add(notifPojo);

                    }

                    initAdapter();
                } else {
                    Log.d("dkcx", "Error getting documents: ", task.getException());
                }
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==1)
        {
            View parentLayout = findViewById(android.R.id.content);

            Snackbar sb= Snackbar.make(parentLayout, "Deleted from cart successfully!", Snackbar.LENGTH_LONG);
            View sbView = sb.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
            sb.show();
        }
        else if (resultCode==2)
        {
            View parentLayout = findViewById(android.R.id.content);
            Snackbar sb= Snackbar.make(parentLayout, "Added to cart successfully!", Snackbar.LENGTH_LONG);
            View sbView = sb.getView();
            sbView.setBackgroundColor(ContextCompat.getColor(this, R.color.green));
            sb.show();
        }
    }
}
