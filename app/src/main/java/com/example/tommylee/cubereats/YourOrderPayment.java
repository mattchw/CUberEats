package com.example.tommylee.cubereats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class YourOrderPayment extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_payment);

        Intent intent = getIntent();
        String cuisine = intent.getStringExtra("cuisineId");

    }
}
