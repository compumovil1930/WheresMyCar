package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratatouille.models.Cliente;

public class ChefCercanosActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_cercanos);
        mAuth = FirebaseAuth.getInstance();

        /*
        FirebaseDatabase.getInstance().getReference("chefs/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Cliente.class) != null) {
                    Cliente clAux=dataSnapshot.getValue(Cliente.class);
                    TextView direction=new TextView(mapaDireccion.this);
                    direction.setText(clAux.getDireccion().getDireccion());
                    myDirecitionsLay.addView(direction);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
    }



}
