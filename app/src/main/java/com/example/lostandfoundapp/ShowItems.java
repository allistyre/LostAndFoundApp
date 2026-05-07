package com.example.lostandfoundapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ShowItems extends AppCompatActivity {
    // Class-level variables
    private List<LostFoundItem> itemList;
    private ItemAdapter adapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_items);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Spinner spCategoryFilter = findViewById(R.id.spCategoryFilter);

        dbHelper = new DatabaseHelper(this);

        itemList = new ArrayList<>();


        adapter = new ItemAdapter(this, itemList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Array Adapter Initialisation
        String[] categories =  {"All", "Electronics", "Pets", "Wallets"};

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoryFilter.setAdapter(categoryAdapter);

        // Spinner listener Initialisation
        spCategoryFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = parent.getItemAtPosition(position).toString();
                Cursor newCursor;

                itemList.clear();

                if (selectedItem.equals("All")) {
                    newCursor = dbHelper.getAllItems();
                } else {
                    newCursor = dbHelper.getItemsByCategory(selectedItem);
                }

                while (newCursor.moveToNext()) {
                    int cursorId = newCursor.getInt(newCursor.getColumnIndexOrThrow(DatabaseHelper.ID));
                    String postType = newCursor.getString(newCursor.getColumnIndexOrThrow
                            (DatabaseHelper.POST_TYPE));
                    String name = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.NAME));
                    String phone = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.PHONE));
                    String description = newCursor.getString(newCursor.getColumnIndexOrThrow
                            (DatabaseHelper.DESCRIPTION));
                    String location = newCursor.getString(newCursor.getColumnIndexOrThrow
                            (DatabaseHelper.LOCATION));
                    String date = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.DATE));
                    String category = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.CATEGORY));
                    String image = newCursor.getString(newCursor.getColumnIndexOrThrow(DatabaseHelper.IMAGE));
                    String timestamp = newCursor.getString(newCursor.getColumnIndexOrThrow(
                            DatabaseHelper.TIMESTAMP));

                    LostFoundItem item = new LostFoundItem(location, cursorId, postType, name, phone, description,
                            date, category, image, timestamp);

                    itemList.add(item);
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        itemList.clear();
        Cursor cursor = dbHelper.getAllItems();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseHelper.ID));
            String postType = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.POST_TYPE));
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.NAME));
            String phone = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.PHONE));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DESCRIPTION));
            String location = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.LOCATION));
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.DATE));
            String category = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.CATEGORY));
            String image = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.IMAGE));
            String timestamp = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.TIMESTAMP));
            itemList.add(new LostFoundItem(location, id, postType, name, phone, description, date, category, image, timestamp));
        }
        adapter.notifyDataSetChanged();
    }
}

