package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class TerminadoChefActivity extends AppCompatActivity {

    Button terminado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terminado_chef);
        terminado=findViewById(R.id.buttonTerminadoChef);
        terminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),MapServiceAvailableActivity.class);
                startActivity(intent);
            }
        });
    }
}
