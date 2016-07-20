package com.example.td190.tesagarson;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class RestaurantMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_menu);

        Intent intent = getIntent();
        String table = intent.getStringExtra("key");
        Toast.makeText(getApplicationContext(), "Table: " + table, Toast.LENGTH_SHORT).show();
    }
}
