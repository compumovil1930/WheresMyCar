package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Direccion;
import com.ratatouille.models.Reserva;
import com.ratatouille.models.Servicio;

import net.steamcrafted.loadtoast.LoadToast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

public class DescripcionChefsCercanos extends AppCompatActivity {

    Bundle bundle;
    TextView editTextChefName;
    ImageView imageViewChef;
    RatingBar ratingBarChef;
    Button buttonSolicitarServicio;

    LinearLayout linearLayoutRecetas;
    LinearLayout linearLayoutHerramientas;

    FirebaseDatabase database;
    StorageReference storage;
    DatabaseReference myRef;
    DatabaseReference mDatabaseReservations;
    DatabaseReference mDatabaseServices;
    private FirebaseAuth mAuth;

    String keyClient;
    String keyChef;
    String keyServicio;

    public static final String PATH_CHEFS = "chefs/";
    public LoadToast lt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_chefs_cercanos);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        mDatabaseReservations = FirebaseDatabase.getInstance().getReference("Servicio/");

        bundle = getIntent().getBundleExtra("bundle");
        Chef chefSeleccionado = (Chef) bundle.getSerializable("ChefSeleccionado");

        editTextChefName = (TextView) findViewById(R.id.textViewName);
        editTextChefName.setText(chefSeleccionado.getNombre());

        ratingBarChef = (RatingBar) findViewById(R.id.ratingBarChef);
        ratingBarChef.setRating((int) chefSeleccionado.getCalificacion());

        addRecetas(chefSeleccionado);
        addHerramientas(chefSeleccionado);
        loadUsers(chefSeleccionado);

        buttonSolicitarServicio = findViewById(R.id.buttonSolicitarServicio);
        buttonSolicitarServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createService(v.getContext());
            }
        });
    }


    private void addRecetas(Chef chef) {

        linearLayoutRecetas = (LinearLayout) findViewById(R.id.LinearLayoutRecetas);

        for (int i = 0; i < chef.getListaRecetas().size(); i++) {

            TextView textViewReceta = new TextView(getApplicationContext());

            textViewReceta.setText(chef.getListaRecetas().get(i).getNombre());
            textViewReceta.append("\n" + chef.getListaRecetas().get(i).getTiempo() + " minutos");
            textViewReceta.setPadding(15, 10, 15, 10);

            linearLayoutRecetas.addView(textViewReceta);
        }
    }

    private void addHerramientas(Chef chef) {

        linearLayoutRecetas = (LinearLayout) findViewById(R.id.LinearLayoutHerramientas);

        for (int i = 0; i < chef.getListaRecetas().size(); i++) {

            TextView textViewHerramienta = new TextView(getApplicationContext());

            textViewHerramienta.setText(chef.getListaHerramientas().get(i).getNombre());
            textViewHerramienta.setPadding(15, 10, 15, 10);

            linearLayoutRecetas.addView(textViewHerramienta);
        }
    }

    public void loadUsers(final Chef wantedChef) {
        myRef = database.getReference(PATH_CHEFS);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot singleSnapshot : dataSnapshot.getChildren()) {

                    Chef chef = singleSnapshot.getValue(Chef.class);
                    if (wantedChef.getCorreo().equals(chef.getCorreo())) {
                        Log.i("ID wanted chef", singleSnapshot.getKey());
                        keyChef = singleSnapshot.getKey();
                        findImageChefInStorage(singleSnapshot.getKey());
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("Firebase chef not found", "error en la consulta", databaseError.toException());
            }
        });
    }

    private void findImageChefInStorage(String key) {
        storage = FirebaseStorage.getInstance().getReferenceFromUrl("gs://ratatouille-d6acf.appspot.com/" + key + "/userProfile");
        imageViewChef = findViewById(R.id.imageViewUser);

        storage.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageViewChef.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void createService(Context v) {

        keyClient = mAuth.getUid();
        Date initialDate = new Date();

        FirebaseUser user = mAuth.getCurrentUser();
        UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
        upcrb.setPhotoUri(Uri.parse("path/to/pic"));
        user.updateProfile(upcrb.build());
        Servicio servicioSolicitado = new Servicio(keyClient, keyChef, initialDate, 0);
        mDatabaseReservations = FirebaseDatabase.getInstance().getReference("Servicio/" + mDatabaseReservations.push().getKey());
        servicioSolicitado.setId(mDatabaseReservations.getKey());
        keyServicio = mDatabaseReservations.getKey();
        mDatabaseReservations.setValue(servicioSolicitado);
        Toast.makeText(v, "Servicio creado", Toast.LENGTH_LONG).show();

        servicioAceptado();
    }

    private void servicioAceptado() {

        lt = new LoadToast(DescripcionChefsCercanos.this);
        lt.setText("Esperando al Chef... ");
        lt.setTranslationY(90);
        lt.show();

        Log.i("esperando", "al chefff");
        myRef = FirebaseDatabase.getInstance().getReference("Servicio/");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.i("DataSnap", dataSnapshot.getKey());
                if (dataSnapshot.getChildrenCount() != 0)
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        if (singleSnap != null) {
                            Log.i("ACEPTAR", singleSnap.toString());
                            Servicio service = singleSnap.getValue(Servicio.class);
                            if (service.getId().equals(keyServicio)) {
                                Log.i("StatusBien", service.getStatus());
                                if (service.getStatus().equalsIgnoreCase("Aceptado")) {
                                    lt.success();
                                } else if (service.getStatus().equalsIgnoreCase("Cancelado")) {
                                    lt.error();
                                }

                            }
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


}
