package com.ratatouille;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView nuevaCuenta;
    Button ingresar;
    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        nuevaCuenta = findViewById(R.id.textViewNueva);
        nuevaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        ingresar = findViewById(R.id.buttonDataUpdate);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), mapaDireccion.class);
                startActivity(intent);
            }
        });


        test=findViewById(R.id.buttonTestPerformer);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), TerminadoChefActivity.class);
                startActivity(intent);
            }
        });
    }
}
