package com.example.sistemaventasfb;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.firestore.FirebaseFirestore;

public class sales extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String idAutomatic;
    double mTotcomision = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales);
        EditText idsellers = findViewById(R.id.etidsellersale);
        EditText idsale = findViewById(R.id.etidsale);
        EditText datesale = findViewById(R.id.etdatesale);
        EditText salevalue = findViewById(R.id.etsalevalue);
        ImageButton btnsalevalue = findViewById(R.id.btnsavesale);
        ImageButton btnback = findViewById(R.id.btnback);



    }
}

