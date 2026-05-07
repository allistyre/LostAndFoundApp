package com.example.lostandfoundapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CreateAdvert extends AppCompatActivity {
    // Class-level Variables
    private String selectedImagePath = null;
    private ActivityResultLauncher<Intent> imagePickerLauncher;
    private ImageView ivPreview;

    private EditText etName;
    private EditText etPhone;
    private EditText etDescription;
    private EditText etDate;
    private EditText etLocation;
    private RadioGroup rgPostType;
    private Spinner spCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_create_advert);

        // Declaration Variables
        rgPostType = findViewById(R.id.rgPostType);

        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etDescription = findViewById(R.id.etDescription);
        etDate = findViewById(R.id.etDate);
        etLocation = findViewById(R.id.etLocation);

        spCategory = findViewById(R.id.spCategory);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Button btnSave = findViewById(R.id.btnSave);
        ivPreview = findViewById(R.id.ivPreview);

        // Array Adapter Initialisation
        String[] categories =  {"All", "Electronics", "Pets", "Wallets"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        // Upload Image Initialisation
        imagePickerLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri imageUri = result.getData().getData();
                        getContentResolver().takePersistableUriPermission(
                                imageUri,
                                Intent.FLAG_GRANT_READ_URI_PERMISSION
                        );
                        ivPreview.setImageURI(imageUri);
                        selectedImagePath = imageUri.toString();
                    }
                }
        );

        btnUploadImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);
            galleryIntent.setType("image/*");
            galleryIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            galleryIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            imagePickerLauncher.launch(galleryIntent);
        });

        // Button save Initialisation
        btnSave.setOnClickListener(v -> {
            // Get selected post type from the RadioGroup
            int selectedId = rgPostType.getCheckedRadioButtonId();
            RadioButton selectedRadioButton = findViewById(selectedId);
            String postType = selectedRadioButton.getText().toString();

            // Read all EditText values
            String name = etName.getText().toString();
            String phone = etPhone.getText().toString();
            String description = etDescription.getText().toString();
            String date = etDate.getText().toString();
            String location = etLocation.getText().toString();

            // Get selected category from Spinner
            String category = spCategory.getSelectedItem().toString();

            // Validation (check that nothing is empty)
            if (TextUtils.isEmpty(name)) {
                etName.setError("This field cannot be empty");
                return;
            }
            else if (TextUtils.isEmpty(phone)) {
                etPhone.setError("This field cannot be empty");
                return;
            }
            else if (TextUtils.isEmpty(description)) {
                etDescription.setError("This field cannot be empty");
                return;
            }
            else if (TextUtils.isEmpty(date)) {
                etDate.setError("This field cannot be empty");
                return;
            }
            else if (TextUtils.isEmpty(location)) {
                etLocation.setError("This field cannot be empty");
                return;
            }

            if (selectedId == -1) {
                Toast.makeText(this, "Please select Lost or Found",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            // insertItem on DatabaseHelper
            DatabaseHelper dbHelper = new DatabaseHelper(this);
            dbHelper.insertItem(postType, name, phone, description, location, date, category,
                    selectedImagePath);

            Toast.makeText(this, "Advert saved successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });

    }

}