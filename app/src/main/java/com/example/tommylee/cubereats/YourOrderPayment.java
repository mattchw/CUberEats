package com.example.tommylee.cubereats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class YourOrderPayment extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderColRef = db.collection("order");
    CollectionReference mealColRef = db.collection("meal");

    private String tempMealIDs;
    private String[] mealIDs;
    private TextView paymentMeals;
    private TextView paymentPrice;
    private Button paymentButton;
    public double totalPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_payment);
        paymentMeals = (TextView) findViewById(R.id.payment_meals);
        paymentPrice = (TextView) findViewById(R.id.payment_price);
        paymentButton = (Button) findViewById(R.id.payment_button);

        Intent intent = getIntent();
        String orderID = intent.getStringExtra("orderID");

        orderColRef.document(orderID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        tempMealIDs = document.getData().get("mealID").toString();
                        tempMealIDs = tempMealIDs.replaceAll("[\\[\\]\\(\\)]", "");
                        mealIDs = tempMealIDs.split("\\s*,\\s*");

                        for (int i = 0; i < mealIDs.length; i++) {
                            final int finalI = i;
                            mealColRef.document(mealIDs[i]).get()
                                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                            DocumentSnapshot document = task.getResult();
                                            paymentMeals.append(document.getData().get("name").toString());
                                            paymentMeals.append("\n");
                                            totalPrice += Double.parseDouble(document.getData().get("price").toString());
                                            Log.e("price", "" + totalPrice);
                                            if (finalI == mealIDs.length - 1) {
                                                 paymentPrice.append("" + totalPrice);
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

        paymentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
