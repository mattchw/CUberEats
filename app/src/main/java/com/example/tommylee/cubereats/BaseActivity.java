package com.example.tommylee.cubereats;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {
    Activity curActivity;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.home:
                curActivity = this;
                if(null != curActivity){
                    if(!(curActivity instanceof RestaurantListActivity)){
                        Intent homeIntent = new Intent(this, RestaurantListActivity.class);
                        finish();
                        startActivity(homeIntent);
                    }
                }
                return true;

            case R.id.delivery:
                curActivity = this;
                if(null != curActivity){
                    if(!(curActivity instanceof OrderDeliveryListActivity)){
                        Intent deliveryIntent = new Intent(this, OrderDeliveryListActivity.class);
                        finish();
                        startActivity(deliveryIntent);
                    }
                }
                return true;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                LoginManager.getInstance().logOut();
                Intent intent = new Intent(this, MainActivity.class);
                finish();
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
