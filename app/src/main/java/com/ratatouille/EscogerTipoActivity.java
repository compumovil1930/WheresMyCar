package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class EscogerTipoActivity extends AppCompatActivity {

    Button botonChefCasa;
    Button botonResevaRest;
    private FirebaseAuth mAuth;
    private final int MY_PERMISSIONS_REQUEST_GPS=3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escoger_tipo);
        mAuth = FirebaseAuth.getInstance();
        botonChefCasa = findViewById(R.id.buttonChefEnCasa);
        botonResevaRest = findViewById(R.id.buttonReservarRestaurante);
        botonChefCasa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), EscogerRecetaActivity.class);
                startActivity(intent);
            }
        });
    }


    public void onClickbotonResevaRest(View v){
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Es necesario Acceder a la ubicacion" +
                "para reservar los restaurantes" ,MY_PERMISSIONS_REQUEST_GPS , mapRestaurantes.class);
    }

    private void requestPermission(Activity context, String permiso, String justificacion, int idCode, Class activity){


        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, permiso)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }

            ActivityCompat.requestPermissions(context, new String [] {permiso}, idCode);
        }
        else
            StartActivity(activity);

    }

    private void StartActivity(Class activity){
        Intent intent = new Intent(this, activity);
        startActivity(intent);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_GPS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "Acceso a Ubicacion!", Toast.LENGTH_LONG).show();
                    StartActivity(mapRestaurantes.class);
                } else {

                    Toast.makeText(this, "Funcionalidad Limitada!", Toast.LENGTH_SHORT).show();
                }
            }
            break;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemClicked = item.getItemId();
        if (itemClicked == R.id.itemSignOut) {
            mAuth.signOut();
            Intent intent = new Intent(EscogerTipoActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}
