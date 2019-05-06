package com.example.tommylee.cubereats;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDeliveryDetailActivity extends AppCompatActivity {
    //    private String orderID;
    private String tempMealIDs;
    private String[] mealIDs;

    private TextView location;
    private TextView deliverMeals;
    private Button acceptButton;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseUser currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    CollectionReference orderColRef = db.collection("order");
    CollectionReference mealColRef = db.collection("meal");

    private void getGeo(GeoPoint geoPoint) {
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
        setContentView(R.layout.activity_order_delivery);

        location = (TextView) findViewById(R.id.location);
        deliverMeals = (TextView) findViewById(R.id.deliver_meals);
        acceptButton = (Button) findViewById(R.id.accept_button);

        Intent intent = getIntent();
        final String orderID = intent.getStringExtra("orderID");
        Log.e("orderID", orderID);

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
                                            DocumentSnapshot document = task.getResult();
                                            deliverMeals.append(document.getData().get("name").toString());
                                            deliverMeals.append("\n");
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

        acceptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MapDeliveryActivity.class);
                intent.putExtra("mapMode", "driver");
                intent.putExtra("orderID", orderID);
                Log.e("orderID", orderID);

                orderColRef.document(orderID)
                        .update("driverID", currentFirebaseUser.getUid())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d("FB write", "DocumentSnapshot successfully written!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.w("FB write", "Error writing document", e);
                            }
                        });

                 getApplication().startActivity(intent);
            }
        });
    }
}
