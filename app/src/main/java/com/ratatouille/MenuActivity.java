package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    LinearLayout list_menu;
    TextView item_profile;
    TextView item_payments;
    TextView item_services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        list_menu = findViewById(R.id.list_menu);
        item_profile = findViewById(R.id.item_profile);
        item_payments = findViewById(R.id.item_payments);
        item_services = findViewById(R.id.items_services);
    }
}
