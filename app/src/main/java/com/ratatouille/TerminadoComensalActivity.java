package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

public class TerminadoComensalActivity extends AppCompatActivity {

    Button calificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminado_comensal);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        calificar=findViewById(R.id.buttonEnviarCalififacion);
        calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),mapaDireccion.class);
                Toast.makeText(v.getContext(),"Gracias por tu feedback!",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });
    }
}
