package com.example.lostandfoundapp;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.lostandfoundapp.databinding.ActivityMapsBinding;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private DatabaseHelper dbHelper;
    private FusedLocationProviderClient fusedLocationClient;

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private Button btnFilter;
    private EditText etRadius;
    private String currentLat = "";
    private String currentLng = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     binding = ActivityMapsBinding.inflate(getLayoutInflater());
     setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnFilter = findViewById(R.id.btnFilter);
        etRadius = findViewById(R.id.etRadius);
        dbHelper = new DatabaseHelper(this);


        // Filter button initialisation
        btnFilter.setOnClickListener(v -> {
            // Check permissions first
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    double radiusVal = Double.parseDouble
                                            (etRadius.getText().toString()) * 1000;
                                    double currentLat = Double.parseDouble
                                            (String.valueOf(location.getLatitude()));
                                    double currentLng = Double.parseDouble
                                            (String.valueOf(location.getLongitude()));
                                    Toast.makeText(MapsActivity.this, "User: " +
                                            currentLat + ", " + currentLng, Toast.LENGTH_LONG).show();
                                    mMap.clear();

                                    Cursor cursor = dbHelper.getAllItems();
                                    while (cursor.moveToNext()) {
                                        String itemLatitude = cursor.getString
                                                (cursor.getColumnIndexOrThrow(DatabaseHelper.LATITUDE));
                                        String itemLongitude = cursor.getString
                                                (cursor.getColumnIndexOrThrow(DatabaseHelper.LONGITUDE));
                                        String itemName = cursor.getString
                                                (cursor.getColumnIndexOrThrow(DatabaseHelper.NAME));

                                        Toast.makeText(MapsActivity.this, "Item: " +
                                                itemLatitude + ", " + itemLongitude, Toast.LENGTH_LONG).show();


                                        if (!itemLatitude.isEmpty() && !itemLongitude.isEmpty()) {
                                            // Parse string as double
                                            double itemLatDouble = Double.parseDouble(itemLatitude);
                                            double itemLngDouble = Double.parseDouble(itemLongitude);
                                            float[] results = new float[1];

                                            Location.distanceBetween(currentLat, currentLng,
                                                    itemLatDouble, itemLngDouble, results);
                                            float distanceInMeters = results[0];

                                            if (distanceInMeters <= radiusVal) {
                                                LatLng position = new LatLng(itemLatDouble,
                                                        itemLngDouble);
                                                mMap.addMarker(new MarkerOptions().
                                                        position(position).title("Lost " + itemName +
                                                        " marker"));
                                            }
                                        }

                                    }

                                } else {
                                    Toast.makeText(MapsActivity.this, "Location Unavailable",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 101);


            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        dbHelper = new DatabaseHelper(this);
        Cursor cursor = dbHelper.getAllItems();

        while (cursor.moveToNext()) {
            String latitude = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LATITUDE));
            String longitude = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LONGITUDE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NAME));

            if (!latitude.isEmpty() && !longitude.isEmpty()) {
                // Parse string as double
                double latDouble = Double.parseDouble(latitude);
                double lngDouble = Double.parseDouble(longitude);

                LatLng position = new LatLng(latDouble, lngDouble);
                mMap.addMarker(new MarkerOptions().position(position).title("Lost " + name +
                        " marker"));
            }

        }

        // Add a marker in Melbourne and move the camera to Melbourne as a default
        LatLng melbourne = new LatLng(-37.8136, 144.9631);
        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(melbourne, 10));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    double radiusVal = Double.parseDouble
                                            (etRadius.getText().toString()) * 1000;
                                    double currentLat = Double.parseDouble
                                            (String.valueOf(location.getLatitude()));
                                    double currentLng = Double.parseDouble
                                            (String.valueOf(location.getLongitude()));
                                    mMap.clear();

                                    Cursor cursor = dbHelper.getAllItems();
                                    while (cursor.moveToNext()) {
                                        String itemLatitude = cursor.getString
                                                (cursor.getColumnIndexOrThrow(DatabaseHelper.LATITUDE));
                                        String itemLongitude = cursor.getString
                                                (cursor.getColumnIndexOrThrow(DatabaseHelper.LONGITUDE));
                                        String itemName = cursor.getString
                                                (cursor.getColumnIndexOrThrow(DatabaseHelper.NAME));


                                        if (!itemLatitude.isEmpty() && !itemLongitude.isEmpty()) {
                                            // Parse string as double
                                            double itemLatDouble = Double.parseDouble(itemLatitude);
                                            double itemLngDouble = Double.parseDouble(itemLongitude);
                                            float[] results = new float[1];

                                            Location.distanceBetween(currentLat, currentLng,
                                                    itemLatDouble, itemLngDouble, results);
                                            float distanceInMeters = results[0];

                                            if (distanceInMeters <= radiusVal) {
                                                LatLng position = new LatLng(itemLatDouble,
                                                        itemLngDouble);
                                                mMap.addMarker(new MarkerOptions().
                                                        position(position).title("Lost " + itemName +
                                                                " marker"));
                                            }
                                        }

                                    }

                                } else {
                                    Toast.makeText(MapsActivity.this, "Location Unavailable",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                }
            }
        }
    }
}