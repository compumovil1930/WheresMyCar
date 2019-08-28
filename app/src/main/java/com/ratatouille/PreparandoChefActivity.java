package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PreparandoChefActivity extends AppCompatActivity {

    Button prep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preparando_chef);
        prep=findViewById(R.id.buttonPreparando);
        prep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(),TerminadoChefActivity.class);
                startActivity(intent);
            }
        });
    }
}
