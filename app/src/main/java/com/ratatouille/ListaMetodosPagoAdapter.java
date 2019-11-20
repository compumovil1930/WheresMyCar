package com.ratatouille;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratatouille.models.Reserva;

import java.util.ArrayList;


public class ListaMetodosPagoAdapter extends BaseAdapter implements ListAdapter {
    private ArrayList<String> list = new ArrayList<String>();
    private Context context;
    private DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
    private DatabaseReference mDatabasePago = FirebaseDatabase.getInstance().getReference("MetodosPago");
    private FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    private String idU = fireAuth.getCurrentUser().getUid();


    public ListaMetodosPagoAdapter(ArrayList<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int pos) {
        return list.get(pos);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.paymethodlistiew, null);
        }

        //Handle TextView and display string from your list
        TextView listItemText = (TextView) view.findViewById(R.id.list_item_string);
        listItemText.setText(list.get(position));

        //Handle buttons and add onClickListeners
        Button deleteBtn = (Button) view.findViewById(R.id.delete_btn);
        final Button selectBtn = view.findViewById(R.id.SelectMethod);

        selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //do something
                selectBtn.setEnabled(false);
                Log.i("posicion ", String.valueOf(position));
                mDatabase.child("MetodosPago").child(idU).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot DS : dataSnapshot.getChildren()) {
                                if (DS.child("Seleccionado").getValue()!=null) {
                                    String seleccionado = DS.child("Seleccionado").getValue().toString();
                                    //Log.i("Seleccionado", seleccionado + " " + padre);
                                    if (seleccionado.equalsIgnoreCase("true")) {
                                        mDatabase.child("MetodosPago").child(idU).child(DS.getKey()).child("Seleccionado").setValue("false");
                                        //Log.i("Seleccionado Cambiado a", "false");
                                    }


                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                mDatabase.child("MetodosPago").child(idU).child(String.valueOf(position)).child("Seleccionado").setValue("true");
                Intent i = new Intent(context, addCreditos.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                context.startActivity(i);
                //notifyDataSetChanged();

            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                mDatabase.child("MetodosPago").child(idU).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount() != 0) {
                            for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                                String mpAux = singleSnap.getKey();
                                //Log.i("IDLIST", mpAux + " " + position);
                                if(mpAux.equalsIgnoreCase("Efectivo")==false){
                                    if (Integer.valueOf(mpAux) == position) {
                                        singleSnap.getRef().removeValue();
                                        Intent intent = new Intent(v.getContext(), MenuActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(intent);
                                    }
                                }

                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                //do something
                list.remove(position); //or some other task
                notifyDataSetChanged();
            }
        });
        return view;
    }
}