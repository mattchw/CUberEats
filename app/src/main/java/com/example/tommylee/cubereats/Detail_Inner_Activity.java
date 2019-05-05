package com.example.tommylee.cubereats;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.model.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Detail_Inner_Activity extends AppCompatActivity {
    ImageView imageView;
    String id;
    Button addCart;
    Button removeCart;
    Toolbar toolbar;
    TextView description;
    TextView stuff;
    TextView price;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail__inner_);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        addCart = findViewById(R.id.button2);
        removeCart = findViewById(R.id.button3);
        Bundle extras = getIntent().getExtras();

        isAddedToCartAlready();

        //tv = findViewById(R.id.quantity);

        if (extras != null) {
            id=extras.getString("foodid");

            description = findViewById(R.id.info_text);
            stuff = findViewById(R.id.stuff);
            price = findViewById(R.id.price);
            imageView = findViewById(R.id.imageView);
            getDetail();
            // and get whatever type user account id is
        }


    }
    private void initDesp(String name_,Double price_,String description_){
        description.setText(description_);
        price.setText(String.valueOf(price_));
        stuff.setText("1x "+name_);
    }
    public void RemoveFromCart(View v){
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Source source = Source.CACHE;

        final Query colRef = db.collection("order").whereEqualTo("customerID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                Log.d("skdfo",task.getResult().getDocuments().get(0).getId());
                                removeExist(db,task.getResult().getDocuments().get(0).getId());
                                setResult(1);
                                finish();
                            }
                        }});
                } else {
                    Log.d("dkcx", "Error getting documents: ", task.getException());
                }
            }
        });
        setResult(1);
        finish();
    }
    private void isAddedToCartAlready()
    {
        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        final Query colRef = db.collection("order").whereEqualTo("customerID",FirebaseAuth.getInstance().getCurrentUser().getUid());
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    colRef.whereArrayContains("mealID",id).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (!task.getResult().isEmpty()) {
                                //add cart
                                addCart.setVisibility(View.GONE);
                                removeCart.setVisibility(View.VISIBLE);
                            } else {
                                addCart.setVisibility(View.VISIBLE);
                                removeCart.setVisibility(View.GONE);
                               //remove cart
                            }

                        }
                    });

                }
            }
        });
    }
    private void addExist(FirebaseFirestore db,String path){
        final Map<String, Object> order = new HashMap<>();
        ArrayList mealId = new ArrayList<String>();
        mealId.add(id);
        order.put("mealID",mealId);

        final DocumentReference ref=db.collection("order").document(path);

                ref.update("mealID",FieldValue.arrayUnion(id));




    }
    private void removeExist(FirebaseFirestore db,String path){
        final DocumentReference ref=db.collection("order").document(path);
        ref.update("mealID",FieldValue.arrayRemove(id));
    }
    private void initOrder(FirebaseFirestore db){
        Map<String, Object> order = new HashMap<>();
        ArrayList mealId = new ArrayList<String>();
        mealId.add(id);
        order.put("customerCoordinate",new GeoPoint(0,0));
        order.put("customerID",FirebaseAuth.getInstance().getCurrentUser().getUid());
        order.put("driverCoordinate",new GeoPoint(0,0));
        order.put("driverID","");
        order.put("mealID",mealId);
        order.put("isPaid",false);
        db.collection("order").add(order)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("written", "DocumentSnapshot written with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("fail", "Error adding document", e);
                    }
                });



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

        final FirebaseFirestore db = FirebaseFirestore.getInstance();
        Source source = Source.CACHE;

        final Query colRef = db.collection("order").whereEqualTo("customerID", FirebaseAuth.getInstance().getCurrentUser().getUid());
        colRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if(task.getResult().isEmpty())
                    {
                        //create new order;
                        initOrder(db);
                    }
                    else
                    {
                        for(DocumentSnapshot qds:task.getResult())
                        {

                            addExist(db,qds.getId());
                        }

                        //add to exist order
                    }
                    setResult(2);
                    finish();
                    //initAdapter();
                } else {
                    Log.d("dkcx", "Error getting documents: ", task.getException());
                }
            }
        });

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
                    notifPojo.setDescription(document.get("description").toString());
                    Glide
                            .with(getApplicationContext())
                            .load(notifPojo.getImgurl())
                            .centerCrop()
                            .into(imageView);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle(notifPojo.getName());
                    initDesp(notifPojo.getName(),notifPojo.getPrice(),notifPojo.getDescription());
                } else {
                    Log.d("dkcx", "Error getting documents: ", task.getException());
                }
            }
        });

    }
}
