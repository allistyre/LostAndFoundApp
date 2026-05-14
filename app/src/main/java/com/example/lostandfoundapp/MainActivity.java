package com.example.lostandfoundapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Button Declarations
        Button createAdvert = findViewById(R.id.btnCreateAdvert);
        Button showItems = findViewById(R.id.btnShowItems);
        Button showMap = findViewById(R.id.btnShowMap);

        // Setting onClick listeners
        createAdvert.setOnClickListener(v -> {
            Intent intent = new Intent(this, CreateAdvert.class);
            startActivity(intent);
        });

        showItems.setOnClickListener(v -> {
            Intent intent = new Intent(this, ShowItems.class);
            startActivity(intent);
        });

        showMap.setOnClickListener(v -> {
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);
        });
    }
}