package com.ratatouille;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PayMethods extends AppCompatActivity {

    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    String idU = fireAuth.getCurrentUser().getUid();
    ListView listItem;
    ArrayList<String> listaMetodos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_de_pago);

        listItem = findViewById(R.id.listView);
        listaMetodos = new ArrayList<String>();


        cargarMetodosDePago();

        listItem.setClickable(true);



    }


    private void cargarMetodosDePago() {
        mDatabase.child("MetodosPago").child(idU).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (ds.exists()) {
                            String proveedor = ds.child("Proveedor").getValue().toString();
                            String numero = ds.child("Numero").getValue().toString();
                            listaMetodos.add(proveedor + " " + numero.substring(0, 4));
                            Log.i("Provedor y numero :", proveedor + " " + numero);
                        }
                    }
                }
                ListaMetodosPagoAdapter adapter = new ListaMetodosPagoAdapter(listaMetodos, getApplicationContext());

                //handle listview and assign adapter
                listItem = (ListView)findViewById(R.id.listView);
                listItem.setAdapter(adapter);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void addPayMethod(View v){
        Intent intent = new Intent(v.getContext(), AddPayMethod.class);
        startActivity(intent);
    }

    public void onClickEfectivo(View v){
        mDatabase.child("MetodosPago").child(idU).child("Efectivo").setValue("true");

    }
}
