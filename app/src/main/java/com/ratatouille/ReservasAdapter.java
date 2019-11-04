package com.ratatouille;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.ratatouille.models.Reserva;

import java.io.Serializable;
import java.util.ArrayList;

public class ReservasAdapter extends ArrayAdapter<Reserva> implements Serializable {

    /*
    private static final int NOMBRE_RESERVA=0;
    private static final int HORA_RESERVA=1;
    private static final int FECHA_RESERVA=2;
    private static final int PARTICIPANTES_RESERVA=3;
    */
    private int resourceLayout;
    private Context mContext;

    public ReservasAdapter(Context context, int resource, ArrayList<Reserva> items){
        super(context,resource,items);
        this.resourceLayout=resource;
        this.mContext=context;
    }


    @Override
    public View getView(int position,View convertView,ViewGroup parent) {
        View mView=convertView;
        if(mView==null){
            LayoutInflater mLayInflater;
            mLayInflater=LayoutInflater.from(mContext);
            mView=mLayInflater.inflate(resourceLayout,null);
        }
        Reserva resAux=getItem(position);
        if(resAux!=null){
            TextView txNombre=(TextView)mView.findViewById(R.id.textViewNombreRest);
            TextView txHora =(TextView)mView.findViewById(R.id.textViewHoraReserva);
            TextView txFecha=(TextView)mView.findViewById(R.id.editTextFechaReserva);
            TextView txPuestos=(TextView)mView.findViewById(R.id.textViewParticipantesReserva);
            TextView txDireccion=(TextView)mView.findViewById(R.id.textViewDireccionReserva);
            //int idnum=cursor.getString(NOMBRE_RESERVA)

            txNombre.setText(resAux.getNombre());
            txHora.setText(resAux.getHora());
            txFecha.setText(resAux.getFechaReserva());
            txPuestos.setText(resAux.getCantidadInvitados());
            txDireccion.setText(String.valueOf(resAux.getDireccion()));
        }
        return mView;
    }

}
