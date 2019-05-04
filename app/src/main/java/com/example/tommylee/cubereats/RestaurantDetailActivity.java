package com.example.tommylee.cubereats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

public class RestaurantDetailActivity extends AppCompatActivity {
    String id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            id=extras.getString("resid");

            getMeals();
            // and get whatever type user account id is
        }
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


                    }

                } else {
                    Log.d("dkcx", "Error getting documents: ", task.getException());
                }
            }
        });


    }
}
