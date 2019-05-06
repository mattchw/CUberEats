package com.example.tommylee.cubereats;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.model.Card;
import com.stripe.android.view.CardInputWidget;

public class PaymentActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private CardInputWidget mCardInputWidget;
    private Button paymentBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        mCardInputWidget = (CardInputWidget) findViewById(R.id.card_input_widget);
        paymentBtn =  (Button) findViewById(R.id.final_payment_button);

        final Intent intent = getIntent();
        final String orderID = intent.getStringExtra("orderID");

        paymentBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent paymentIntent = new Intent(PaymentActivity.this, YourOrderListActivity.class);

                Card cardToSave = mCardInputWidget.getCard();
                if (cardToSave == null) {
                    Toast.makeText(v.getContext(), "Invalid Card Data", Toast.LENGTH_LONG).show();
                } else {
                    DocumentReference docRef = db.collection("order").document(orderID);
                    docRef.update("isPaid",true);
                    startActivity(paymentIntent);
                }

            }
        });
    }


}
