package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
        getSupportActionBar().hide();
        list_menu = findViewById(R.id.list_menu);
        item_profile = findViewById(R.id.item_profile);
        item_payments = findViewById(R.id.item_payments);
        item_services = findViewById(R.id.items_services);
        item_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(v.getContext(), HistoryServiceActivity.class);
                startActivity(intent);
            }
        });
        item_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PayMethods.class);
                startActivity(intent);
            }
        });
        item_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateUserProfileActivity.class);
                startActivity(intent);
            }
        });
    }
}
