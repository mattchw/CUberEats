package com.example.tommylee.cubereats;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;

public abstract class BaseActivity extends AppCompatActivity {
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.delivery:
                Intent deliveryIntent = new Intent(this, OrderDeliveryListActivity.class);
                finish();
                startActivity(deliveryIntent);
                return true;

            case R.id.order:
                Intent orderIntent = new Intent(this, YourOrderListActivity.class);
                finish();
                startActivity(orderIntent);
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
