package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;


public class PayMethods extends AppCompatActivity {

    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_de_pago);
        listView = findViewById(R.id.ListView);

    }
    public void agregarMetodoDePago(View v){
        Intent intent = new Intent(v.getContext(), AddPayMethod.class);
        startActivity(intent);
    }
}
