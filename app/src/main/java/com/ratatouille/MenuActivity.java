package com.ratatouille;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

//import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Cliente;

public class MenuActivity extends AppCompatActivity {

    ImageView profile_img;
    LinearLayout list_menu;
    TextView item_profile;
    TextView item_payments;
    TextView item_services;
    TextView item_reservas;
    TextView user_name;
    TextView user_rate;
    Button bLogOut;
    FirebaseAuth mAuth;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        getSupportActionBar().hide();
        mAuth = FirebaseAuth.getInstance();
        list_menu = findViewById(R.id.list_menu);
        item_profile = findViewById(R.id.item_profile);
        item_reservas = findViewById(R.id.item_reservas);
        item_payments = findViewById(R.id.item_payments);
        item_services = findViewById(R.id.items_services);
        profile_img = findViewById(R.id.user_img);
        user_name = findViewById(R.id.user_name);
        user_rate = findViewById(R.id.user_rating);
        FirebaseDatabase.getInstance().getReference("chefs/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Chef.class) != null) {
                    Chef chAux=dataSnapshot.getValue(Chef.class);
                    user_name.setText(chAux.getNombre());
                    user_rate.setText(Double.toString(chAux.getCalificacion()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        FirebaseDatabase.getInstance().getReference("clientes/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Cliente.class) != null) {
                    Cliente clAux=dataSnapshot.getValue(Cliente.class);
                    user_name.setText(clAux.getNombre());
                    user_rate.setText(Double.toString(clAux.getCalificacion()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://ratatouille-d6acf.appspot.com/" + mAuth.getUid() + "/userProfile");
        mStorageRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                profile_img.setImageBitmap(bmp);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(getApplicationContext(), "No Such file or Path found!!", Toast.LENGTH_LONG).show();
            }
        });
        bLogOut = findViewById(R.id.buttonLogOut);
        bLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(MenuActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        item_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), HistoryServiceActivity.class);
                startActivity(intent);
            }
        });
        item_payments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PayMethods.class);
                startActivity(intent);
            }
        });
        item_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UpdateUserProfileActivity.class);
                startActivity(intent);
            }
        });
        item_reservas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MisReservasActivity.class);
                startActivity(intent);
            }
        });
    }
}
