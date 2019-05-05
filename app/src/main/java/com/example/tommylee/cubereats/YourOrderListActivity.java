package com.example.tommylee.cubereats;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;

public class YourOrderListActivity extends BaseActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    // private String[] myDataset = new String[]{"fkweo"};
    private String userID = null;

    FirebaseAuth mAuth;
    private ArrayList<Order> result_set= new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_order_list);
        mAuth = FirebaseAuth.getInstance();

        getFromFireBase();
    }

    private void getFromFireBase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseUser currentFireBaseUser = FirebaseAuth.getInstance().getCurrentUser();
        userID = currentFireBaseUser.getUid();
        Source source = Source.CACHE;

        CollectionReference colRef = db.collection("order");
        colRef.whereEqualTo("customerID", userID).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.e("dockx", document.getId() + " => " + document.getData());
                        Order notifPojo = document.toObject(Order.class);
                        notifPojo.setDocumentID(document.getId());
                        result_set.add(notifPojo);
                    }
                    initRecyclerView();
                } else {
                    Log.e("dkcx", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    private void initRecyclerView(){
        recyclerView = (RecyclerView) findViewById(R.id.your_order_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        Log.e("data set",result_set.size()+"");
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new YourOrderListAdapter(result_set, getApplicationContext());
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.restaurant_menu, menu);
        return true;
    }
}
