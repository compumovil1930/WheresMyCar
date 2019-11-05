package com.ratatouille;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ratatouille.models.Reserva;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ReservasAdapter extends BaseAdapter {


    private Context mContext;
    private List<Reserva> mReservas;

    public ReservasAdapter(Context mContext, List<Reserva> mReservas) {
        this.mContext = mContext;
        this.mReservas = mReservas;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        View v=View.inflate(mContext,R.layout.adapter_reservas,null);
        TextView tvName=(TextView)v.findViewById(R.id.textViewNombreMisReservas);
        //TextView tvDireccion=(TextView)v.findViewById(R.id.textViewDireccionMisResevas);
        TextView tvHora=(TextView)v.findViewById(R.id.textViewHoraMisReserva);
        TextView tvDia=(TextView)v.findViewById(R.id.textViewDiaMisReservas);
        TextView tvParticipantes=(TextView)v.findViewById(R.id.textViewParticipantesMisReservas);

        tvName.setText(mReservas.get(position).getNombre());
        //tvDireccion.setText(mReservas.get(position).getDireccion().getDireccion());
        tvHora.setText(mReservas.get(position).getHora());
        tvDia.setText(mReservas.get(position).getFechaReserva());
        tvParticipantes.setText(String.valueOf(mReservas.get(position).getCantidadInvitados())+" puestos reservados");
        //v.setTag(mReservas.get(position).get);

        return v;
    }
}
