package com.example.tommylee.cubereats;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.io.IOException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MapDeliveryActivity extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private GoogleMap mMap;

    private Double longitude, latitude;

    private Button routeButton;
    private Button finishButton;
    private TextView mapCustomerAddr;
    private MarkerOptions userPosition, deliveryDest;
    private Polyline mPolyline;

    LocationManager lm;
    Location location;
    LatLng destination, currPosition;
    private String orderID;
    private String mapMode;

    GeoPoint customerCoordinate;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference orderColRef = db.collection("order");
    private Timer mTimer;
    private void UploadCurrentPosition() {
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {

                currPosition = new LatLng(location.getLatitude(), location.getLongitude());
                Log.d("hi",currPosition.latitude+" "+currPosition.longitude);
                orderColRef
                        .document(orderID).update("driverCoordinate",new GeoPoint(currPosition.latitude,currPosition.longitude));

            }
        }, 500, 7000);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mTimer = new Timer();

        Intent intent = getIntent();
        mapMode = getIntent().getStringExtra("mapMode");
        orderID = getIntent().getStringExtra("orderID");

        mapCustomerAddr = findViewById(R.id.mapCustomerAddr);

        orderColRef
                .document(orderID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        customerCoordinate = document.getGeoPoint("customerCoordinate");
                        mapCustomerAddr.setText(getGeo(customerCoordinate));
                    } else {
                        Log.d("emptyDoc", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mSupportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mSupportMapFragment.getMapAsync(this);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 2000, 10, locationListener);


        routeButton = findViewById(R.id.routeButton);

        // destination = new LatLng(22.422546, 114.204388);
        // deliveryDest = new MarkerOptions().position(destination).title("Destination");


        finishButton = findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // Check if location permissions are granted and if so enable the
        // location data layer.
        switch (requestCode) {
            case REQUEST_LOCATION_PERMISSION:
                if (grantResults.length > 0
                        && grantResults[0]
                        == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                    break;
                }
        }
    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        }
    }

    private String getGeo(GeoPoint geoPoint) {
        Geocoder geoCoder = new Geocoder(getApplicationContext());
        List<Address> matches = null;
        try {
            matches = geoCoder.getFromLocation(geoPoint.getLatitude(), geoPoint.getLongitude(), 1);
            Address bestMatch = (matches.isEmpty() ? null : matches.get(0));
            return bestMatch.getAddressLine(0);
        } catch (IOException e) {

        }
        return null;
    }

    private void UploadPosition(Double latitude,Double longitude){
        orderColRef
                .document(orderID).update("driverCoordinate",new GeoPoint(latitude,longitude));
    }
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            Log.e("longitude", "longitude: " + longitude);
            Log.e("latitude", "latitude: " + latitude);
            UploadPosition(latitude,longitude);
        }
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
        @Override
        public void onProviderEnabled(String provider) {

        }
        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        enableMyLocation();

        orderColRef
                .document(orderID)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        customerCoordinate = document.getGeoPoint("customerCoordinate");
                        currPosition = new LatLng(location.getLatitude(), location.getLongitude());
                        userPosition = new MarkerOptions().position(currPosition).title("Your position");
                        destination = new LatLng(customerCoordinate.getLatitude(), customerCoordinate.getLongitude());
                        deliveryDest = new MarkerOptions().position(destination).title("Deliver Man Position");

                        mMap.addMarker(deliveryDest);
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(destination, 15));
                    } else {
                        Log.d("emptyDoc", "No such document");
                    }
                } else {
                    Log.d("error", "get failed with ", task.getException());
                }
            }
        });

        routeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getUrl(userPosition.getPosition(), deliveryDest.getPosition(), "driving");
                new FetchURL(MapDeliveryActivity.this).execute(url, "driving");
            }
        });
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (mPolyline != null)
            mPolyline.remove();
        mPolyline = mMap.addPolyline((PolylineOptions) values[0]);
    }

}
