package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ratatouille.models.Chef;

public class DescripcionChefsCercanos extends AppCompatActivity {

    Bundle bundle;
    TextView editTextChefName;
    ImageView imageViewChef;
    RatingBar ratingBarChef;

    LinearLayout linearLayoutRecetas;
    LinearLayout linearLayoutHerramientas;

    FirebaseDatabase database;
    StorageReference storage;
    DatabaseReference myRef;

    public static final String PATH_CHEFS="chefs/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_descripcion_chefs_cercanos);

        database = FirebaseDatabase.getInstance();

        bundle = getIntent().getBundleExtra("bundle");
        Chef chef = (Chef) bundle.getSerializable("ChefSeleccionado");

        editTextChefName = (TextView) findViewById(R.id.textViewName);
        editTextChefName.setText(chef.getNombre());

        ratingBarChef = (RatingBar) findViewById(R.id.ratingBarChef);
        ratingBarChef.setRating((int) chef.getCalificacion());

        addRecetas(chef);
        addHerramientas(chef);
        loadUsers(chef);
    }


    private void addRecetas(Chef chef) {

        linearLayoutRecetas = (LinearLayout) findViewById(R.id.LinearLayoutRecetas);

        for (int i = 0; i < chef.getListaRecetas().size(); i++){

            TextView textViewReceta = new TextView(getApplicationContext());

            textViewReceta.setText(chef.getListaRecetas().get(i).getNombre());
            textViewReceta.append("\n" + chef.getListaRecetas().get(i).getTiempo() + " minutos");
            textViewReceta.setPadding(15,10, 15,10);

            linearLayoutRecetas.addView(textViewReceta);
        }
    }

    private void addHerramientas(Chef chef) {

        linearLayoutRecetas = (LinearLayout) findViewById(R.id.LinearLayoutHerramientas);

        for (int i = 0; i < chef.getListaRecetas().size(); i++){

            TextView textViewHerramienta = new TextView(getApplicationContext());

            textViewHerramienta.setText(chef.getListaHerramientas().get(i).getNombre());
            textViewHerramienta.setPadding(15,10, 15,10);

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
                    if (wantedChef.getCorreo().equals(chef.getCorreo())){
                        Log.i("ID wanted chef", singleSnapshot.getKey());
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
}
