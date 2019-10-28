package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratatouille.models.Cliente;
import com.ratatouille.models.Direccion;
import com.ratatouille.models.Receta;
import com.ratatouille.models.Reserva;
import com.ratatouille.models.TimePickerFragment;

import java.util.Calendar;

public class DetallesReservaActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseClientes;
    Button reserva;
    TextView nRes;
    TextView txTime;
    TextView tvFechaReserva;
    EditText etAsistentes;
    DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_reserva);
        mAuth = FirebaseAuth.getInstance();
        final String nomRes=getIntent().getStringExtra("nombreRes");
        tvFechaReserva =findViewById(R.id.editTextFechaReserva);
        reserva=findViewById(R.id.buttonTerminarReserva);
        txTime=findViewById(R.id.textViewHoraReserva);
        etAsistentes=findViewById(R.id.editTextAsistentes);
        nRes=findViewById(R.id.textViewNombreRest);
        nRes.setText("Estás realizando una reserva en el restaurante "+nomRes);
        txTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker=new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(),"time picker");
            }
        });
        reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                upcrb.setPhotoUri(Uri.parse("path/to/pic"));
                user.updateProfile(upcrb.build());
                String nAux=getIntent().getStringExtra("direccion");
                double lauAux=getIntent().getDoubleExtra("latitud",0);
                double lonAux=getIntent().getDoubleExtra("longitud",0);
                Direccion dirAux=new Direccion(1,"",nAux,lauAux,lonAux);
                Reserva reserva=new Reserva(nomRes,dirAux,tvFechaReserva.getText().toString(),txTime.getText().toString(),Integer.parseInt(etAsistentes.getText().toString()));
                mDatabaseClientes = FirebaseDatabase.getInstance().getReference("clientes/" + mAuth.getCurrentUser().getUid()).child("listaReservas");
                mDatabaseClientes.setValue(reserva);
                Toast.makeText(v.getContext(),"Reserva realizada",Toast.LENGTH_LONG).show();
            }
        });
        tvFechaReserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog = new DatePickerDialog(
                        DetallesReservaActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;
                Log.d("DATEPICKER", "onDateSet: " + dayOfMonth + " " + month + " " + year);
                String date = dayOfMonth + "/" + month + "/" + year;
                tvFechaReserva.setText(date);
            }
        };
    }


    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        txTime.setText(hourOfDay+":"+minute);
    }


    /*
    public void showChefs() {
        database = FirebaseDatabase.getInstance();
        mDatabaseChefs = database.getReference("chefs");
        mDatabaseChefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0)
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        Chef aux = singleSnap.getValue(Chef.class);
                        if (aux.getEstado())
                            if (distance(aux.getDireccion().getLatitud(), aux.getDireccion().getLongitud(), latitude, longitude) <= 5.0) {
                                Toast.makeText(mapaDireccion.this, "nuevo chef", Toast.LENGTH_SHORT).show();
                                mMap.addMarker(new MarkerOptions().position(new LatLng(aux.getDireccion().getLatitud(), aux.getDireccion().getLongitud())).icon(BitmapDescriptorFactory.fromResource(R.drawable.remy)));
                            }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/
}
