package com.ratatouille;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratatouille.models.Reserva;

import java.util.List;

public class ReservasAdapter extends BaseAdapter {

    FirebaseDatabase database;
    DatabaseReference mDatabaseReservas;
    private Context mContext;
    private List<Reserva> mReservas;
    private String mUser;

    public ReservasAdapter(Context mContext, List<Reserva> mReservas, String user) {
        this.mContext = mContext;
        this.mReservas = mReservas;
        this.mUser = user;
    }

    @Override
    public int getCount() {
        return mReservas.size();
    }

    @Override
    public Object getItem(int position) {
        return mReservas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        database = FirebaseDatabase.getInstance();
        mDatabaseReservas = database.getReference("reservas");
        View v = View.inflate(mContext, R.layout.adapter_reservas, null);
        TextView tvName = v.findViewById(R.id.textViewNombreMisReservas);
        TextView tvHora = v.findViewById(R.id.textViewHoraMisReserva);
        TextView tvDia = v.findViewById(R.id.textViewDiaMisReservas);
        TextView tvParticipantes = v.findViewById(R.id.textViewParticipantesMisReservas);
        Button btnCancelar = v.findViewById(R.id.btnCanReservas);
        tvName.setText(mReservas.get(position).getNombre());
        tvHora.setText(mReservas.get(position).getHora());
        tvDia.setText(mReservas.get(position).getFechaReserva());
        String particiantes = mReservas.get(position).getCantidadInvitados() + " puestos reservados";
        tvParticipantes.setText(particiantes);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReservas.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                                Reserva resAux = singleSnap.getValue(Reserva.class);
                                if (resAux.getId() == mReservas.get(position).getId()) {
                                    singleSnap.getRef().removeValue();
                                    Intent intent = new Intent(mContext, MisReservasActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mContext.startActivity(intent);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
        });
        return v;
    }
}
