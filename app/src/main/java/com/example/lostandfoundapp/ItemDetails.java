package com.example.lostandfoundapp;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

public class ItemDetails extends AppCompatActivity {
    // Class-level Variables
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_item_details);

        int idValue = getIntent().getIntExtra("ITEM_ID", -1);

        dbHelper = new DatabaseHelper(this);

        Cursor cursor = dbHelper.getItemById(idValue);

        if (cursor.moveToFirst()) {
            // Grabbing the values
            String postType = cursor.getString(cursor.getColumnIndexOrThrow
                    (DatabaseHelper.POST_TYPE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PHONE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow
                    (DatabaseHelper.DESCRIPTION));
            String location = cursor.getString(cursor.getColumnIndexOrThrow
                    (DatabaseHelper.LOCATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CATEGORY));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.IMAGE));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(
                    DatabaseHelper.TIMESTAMP));

            // Inserting the values
            TextView tvNameResult = findViewById(R.id.tvNameResult);
            tvNameResult.setText(name);
            TextView tvPostTypeResult = findViewById(R.id.tvPostTypeResult);
            tvPostTypeResult.setText(postType);
            TextView tvPhoneResult = findViewById(R.id.tvPhoneResult);
            tvPhoneResult.setText(phone);
            TextView tvDescriptionResult = findViewById(R.id.tvDescriptionResult);
            tvDescriptionResult.setText(description);
            TextView tvLocationResult = findViewById(R.id.tvLocationResult);
            tvLocationResult.setText(location);
            TextView tvDateResult = findViewById(R.id.tvDateResult);
            tvDateResult.setText(date);
            TextView tvCategoryResult = findViewById(R.id.tvCategoryResult);
            tvCategoryResult.setText(category);
            TextView tvTimestampResult = findViewById(R.id.tvTimestampResult);
            tvTimestampResult.setText(timestamp);
            ImageView ivPreview = findViewById(R.id.ivPreview);
            ivPreview.setImageURI(Uri.parse(image));
        }

        // Remove button initialisation
        Button btnRemove = findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(v -> {
            dbHelper.deleteItem(idValue);
            Toast.makeText(this, "Advert removed!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}