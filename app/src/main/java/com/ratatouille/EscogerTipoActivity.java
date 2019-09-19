package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class EscogerTipoActivity extends AppCompatActivity {

    Button botonChefCasa;
    Button botonResevaRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_tipo);
        botonChefCasa=findViewById(R.id.buttonChefEnCasa);
        botonResevaRest=findViewById(R.id.buttonReservarRestaurante);
        botonChefCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),EscogerRecetaActivity.class);
                startActivity(intent);
            }
        });
    }
}
