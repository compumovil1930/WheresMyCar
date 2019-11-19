package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.ratatouille.models.Direccion;
import com.ratatouille.models.Reserva;
import com.ratatouille.models.TimePickerFragment;

import java.io.Serializable;
import java.util.Calendar;

public class DetallesReservaActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, Serializable {

    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReservations;
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
        mDatabaseReservations = FirebaseDatabase.getInstance().getReference("reservas");
        final String nomRes = getIntent().getStringExtra("nombreRes");
        tvFechaReserva = findViewById(R.id.editTextFechaReserva);
        reserva = findViewById(R.id.buttonTerminarReserva);
        txTime = findViewById(R.id.textViewHoraMisReserva);
        etAsistentes = findViewById(R.id.editTextAsistentes);
        nRes = findViewById(R.id.textViewNombreRest);
        nRes.setText("Estás realizando una reserva en el restaurante " + nomRes);
        txTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });
        reserva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();
                UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                upcrb.setPhotoUri(Uri.parse("path/to/pic"));
                user.updateProfile(upcrb.build());
                String dirResAux = getIntent().getStringExtra("direccion");
                Double latAux = getIntent().getDoubleExtra("latitud", 0);
                Double lonAux = getIntent().getDoubleExtra("longitud", 0);
                Direccion dirAux = new Direccion(1, "", dirResAux, latAux, lonAux);
                String fechaAux = tvFechaReserva.getText().toString();
                String timeAux = txTime.getText().toString();
                int asisAux = Integer.parseInt(etAsistentes.getText().toString());
                Reserva reserva = new Reserva(mAuth.getCurrentUser().getUid(), nomRes, dirAux, fechaAux, timeAux, asisAux);
                mDatabaseReservations = FirebaseDatabase.getInstance().getReference("reservas/" + mDatabaseReservations.push().getKey());
                reserva.setId(mDatabaseReservations.push().getKey());
                mDatabaseReservations.setValue(reserva);
                Toast.makeText(v.getContext(), "Reserva realizada", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(v.getContext(), EscogerTipoActivity.class);
                startActivity(intent);
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
        txTime.setText("Hora de la reserva: " + hourOfDay + ":" + minute);
    }
}
