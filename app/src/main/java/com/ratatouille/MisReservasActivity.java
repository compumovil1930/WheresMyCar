package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.ratatouille.models.Reserva;

import java.util.ArrayList;
import java.util.List;

public class MisReservasActivity extends AppCompatActivity {

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReservas;
    TextView txvacio;
    ListView listViewReservas;
    ReservasAdapter adapter;
    List<Reserva> listaReservasAux;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);
        mAuth = FirebaseAuth.getInstance();
        txvacio = findViewById(R.id.textViewVacio);
        listViewReservas = findViewById(R.id.listViewReservas);
        listaReservasAux = new ArrayList<>();
        loadReservations();
    }

    public void loadReservations() {
        database = FirebaseDatabase.getInstance();
        mDatabaseReservas = database.getReference("reservas");
        mDatabaseReservas.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0) {
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        Reserva resAux = singleSnap.getValue(Reserva.class);
                        if (resAux.getIdUsuario().equals(mAuth.getCurrentUser().getUid())) {
                            listaReservasAux.add(resAux);
                        }
                    }
                }
                adapter = new ReservasAdapter(getApplicationContext(), listaReservasAux);
                listViewReservas.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
