package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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
    DatabaseReference mDatabaseClientes;
    TextView txvacio;
    LinearLayout layReservas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mis_reservas);
        mAuth=FirebaseAuth.getInstance();
        layReservas=findViewById(R.id.layoutListaReservas);
        txvacio=findViewById(R.id.textViewVacio);
        /*
        List<Reserva> listaReservasMostrar=loadReservations();

        if(listaReservasMostrar==null){
            txvacio.setText("No tienes ninguna reserva");
            txvacio.setVisibility(View.VISIBLE);
        }else{

            for (int i=0;i<listaReservasMostrar.size();i++){
                ReservasAdapter resAux=new ReservasAdapter(MisReservasActivity.this,null,0);
                resAux.setNombre(listaReservasMostrar.get(i).getNombre());
                resAux.setHora(listaReservasMostrar.get(i).getHora());
                resAux.setFecha(listaReservasMostrar.get(i).getFechaReserva());
                resAux.setPuestos(String.valueOf(listaReservasMostrar.get(i).getCantidadInvitados()));
                resAux.setPuestos(String.valueOf(listaReservasMostrar.get(i).getDireccion().getDireccion()));
                layReservas.addView(resAux);
            }
            Toast.makeText(this,"Si recibo las reservas",Toast.LENGTH_LONG).show();
        }*/

    }


    /*
    public List<Reserva> loadReservations() {
        final List<Reserva> listaReservasAux=new ArrayList<Reserva>();
        database = FirebaseDatabase.getInstance();
        mDatabaseClientes = database.getReference("clientes/"+mAuth.getCurrentUser().getUid()).child("listaReservas");
        //mDatabaseClientes = FirebaseDatabase.getInstance().getReference("clientes/" + mAuth.getCurrentUser().getUid()).child("listaReservas");
        mDatabaseClientes.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0)
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        Reserva aux = singleSnap.getValue(Reserva.class);
                        if (aux!=null){
                            listaReservasAux.add(aux);
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    return listaReservasAux;
    }*/
}
