package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class EscogerTipoActivity extends AppCompatActivity {

    Button botonChefCasa;
    Button botonResevaRest;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_tipo);
        mAuth=FirebaseAuth.getInstance();
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int itemClicked=item.getItemId();
        if(itemClicked==R.id.itemSignOut){
            mAuth.signOut();
            Intent intent=new Intent(EscogerTipoActivity.this,MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
