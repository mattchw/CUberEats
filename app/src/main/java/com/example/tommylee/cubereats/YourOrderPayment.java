package com.example.tommylee.cubereats;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firestore.v1.StructuredQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class YourOrderPayment extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderColRef = db.collection("order");
    CollectionReference mealColRef = db.collection("meal");

    private String tempMealIDs;
    private String[] mealIDs;
    private TextView location;
    private RecyclerView.LayoutManager layoutManager;
    OrderAdapter orderAdapter;
    RecyclerView recyclerView;
    private TextView paymentPrice;
    ArrayList<Meal> meals=new ArrayList<>();
    public double totalPrice = 0.0;
    int finalI=0;
    private void initAdapter(){
        recyclerView = (RecyclerView) findViewById(R.id.list_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        orderAdapter = new OrderAdapter(meals,getApplicationContext(),this);
        recyclerView.setAdapter(orderAdapter);
    }
    private void getGeo(GeoPoint geoPoint){
        Geocoder geoCoder = new Geocoder(getApplicationContext());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            location.setText(bestMatch.getAddressLine(0));
        } catch (IOException e) {

        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_payment);
       // paymentMeals = (TextView) findViewById(R.id.payment_meals);
        paymentPrice = (TextView) findViewById(R.id.payment_price);
        location = (TextView) findViewById(R.id.location);
        Intent intent = getIntent();
        String orderID = intent.getStringExtra("orderID");

        orderColRef.document(orderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tempMealIDs = document.getData().get("mealID").toString();
                        getGeo((GeoPoint) document.getData().get("customerCoordinate"));
                        tempMealIDs = tempMealIDs.replaceAll("[\\[\\]\\(\\)]", "");
                        mealIDs = tempMealIDs.split("\\s*,\\s*");

                        for (int i = 0; i < mealIDs.length; i++) {




                            mealColRef.document(mealIDs[i]).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            finalI++;
                                            DocumentSnapshot document = task.getResult();
                                            Meal meal=new Meal();
                                            meal.setName(document.getData().get("name").toString());
                                            meal.setPrice(Double.parseDouble(document.getData().get("price").toString()));
                                            //adapter?
                                            meals.add(meal);
                                            totalPrice += Double.parseDouble(document.getData().get("price").toString());
                                            Log.e("price", ""+totalPrice);
                                            if (finalI == mealIDs.length) {
                                                initAdapter();
                                                paymentPrice.append(""+totalPrice);
                                            }

                                        }
                                    });
                        }

                    } else {
                        Log.e("error", "No such document");
                    }
                } else {
                    Log.e("fail", "get failed with ", task.getException());
                }
            }
        });

    }
}
