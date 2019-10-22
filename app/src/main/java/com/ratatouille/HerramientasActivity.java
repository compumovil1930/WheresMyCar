package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class HerramientasActivity extends AppCompatActivity {

    Button next;
    LinearLayout lay;
    TextView txt;
    String tipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herramientas);
        lay=findViewById(R.id.linearElectro);
        next=findViewById(R.id.buttonSiguienteImagen);
        txt=findViewById(R.id.textViewTexto);

        tipo=getIntent().getStringExtra("tipo");
        if(!tipo.equals("chef")){
            txt.setText("De las siguientes listas de utensilios y electrodomesticos selecciona los cuales posees");
        }else{
            txt.setText("De las siguientes listas de utensilios y electrodomesticos selecciona los cuales puedes llevar en cualquier momento a donde un cliente");
        }

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tipo.equals("chef")){
                    Intent intent=new Intent(v.getContext(),mapaDireccion.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(v.getContext(),RecetasChefActivity.class);
                    startActivity(intent);
                }
                /*CheckBox check=new CheckBox(HerramientasActivity.this);
                check.setText("prueba");
                lay.addView(check);*/
            }
        });
    }
}
