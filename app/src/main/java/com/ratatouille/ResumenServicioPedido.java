package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ResumenServicioPedido extends AppCompatActivity {

    TextView textViewDia ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen_servicio_pedido);

        textViewDia = findViewById(R.id.diaTextView);
        textViewDia.setText(new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));


    }
}
