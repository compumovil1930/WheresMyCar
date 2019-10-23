package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ratatouille.models.Receta;

import java.util.ArrayList;
import java.util.List;

public class DetallesChefActivity extends AppCompatActivity {

    Button bTerminar;
    EditText detalles;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_chef);
        bundle=getIntent().getBundleExtra("bundle");
        detalles=findViewById(R.id.editTextDetallesChef);
        bTerminar=findViewById(R.id.buttonTerminarRegistro);
        bTerminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detalles.getText().toString()!=""){
                    Intent intent=new Intent(v.getContext(),RecetasChefActivity.class);
                    intent.putExtra("bundle",bundle);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(DetallesChefActivity.this,"Debe agregar su descripición para continuar",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
