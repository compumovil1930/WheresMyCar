package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Cliente;

public class ChefCercanosActivity extends AppCompatActivity {

    static final double RADIUS_OF_EARTH_KM = 6371.01;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseChefs;
    LinearLayout myLay;
    double clientLatitude;
    double clientLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_cercanos);
        mAuth = FirebaseAuth.getInstance();
        myLay=findViewById(R.id.linearChefs);
        clientLatitude=getIntent().getDoubleExtra("latitud",0);
        clientLongitude=getIntent().getDoubleExtra("longitud",0);
        nearbyChefs();

    }



    public void nearbyChefs() {
        database = FirebaseDatabase.getInstance();
        mDatabaseChefs = database.getReference("chefs");
        mDatabaseChefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0)
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        Chef aux = singleSnap.getValue(Chef.class);
                        if (aux.getEstado())
                            if (distance(aux.getDireccion().getLatitud(), aux.getDireccion().getLongitud(), clientLatitude, clientLongitude) <= 5.0) {
                                Toast.makeText(ChefCercanosActivity.this,"Se encontró uno",Toast.LENGTH_LONG).show();
                                TextView tvAux=new TextView(ChefCercanosActivity.this);
                                tvAux.setText(aux.getNombre());
                                myLay.addView(tvAux);
                            }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        Toast.makeText(ChefCercanosActivity.this,"Resultado de distancias "+String.valueOf(result),Toast.LENGTH_LONG).show();
        return Math.round(result * 100.0) / 100.0;
    }



}
