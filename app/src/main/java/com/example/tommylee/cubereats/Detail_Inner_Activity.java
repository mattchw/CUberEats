package com.example.tommylee.cubereats;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.model.Document;

public class Detail_Inner_Activity extends AppCompatActivity {
    ImageView imageView;
    String id;
    Toolbar toolbar;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__inner_);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        Bundle extras = getIntent().getExtras();

        tv = findViewById(R.id.quantity);

        if (extras != null) {
            id=extras.getString("foodid");

            imageView = findViewById(R.id.imageView);
            getDetail();
            // and get whatever type user account id is
        }


    }
    public void AddToCart(View v)
    {
        /*
        firebase connection to collection user cart
        document for user each restaurant order (for 1?)
        push
        spinner?
        finishforactivityresult
         */
        finish();
    }
    public void minusSelection(View v)
    {
        int quantity=Integer.valueOf(tv.getText().toString());
        if(quantity>0)
        quantity--;
        tv.setText(String.valueOf(quantity));
    }
    public void plusSelection(View v)
    {
        int quantity=Integer.valueOf(tv.getText().toString());
        if(quantity<5)
        quantity++;
        tv.setText(String.valueOf(quantity));
    }
    private void getDetail(){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Source source = Source.CACHE;

        DocumentReference colRef = db.collection("meal").document(id);
        colRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    Meal notifPojo = new Meal();
                    DocumentSnapshot document=task.getResult();


                    notifPojo.setImgurl(document.get("imgurl").toString());
                    notifPojo.setName(document.get("name").toString());
                    notifPojo.setPrice(Double.parseDouble(document.get("price").toString()));
                    notifPojo.setDescription("HKD "+document.get("description").toString());
                    Glide
                            .with(getApplicationContext())
                            .load(notifPojo.getImgurl())
                            .centerCrop()
                            .into(imageView);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(notifPojo.getName());
                } else {
                    Log.d("dkcx", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
