package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Cliente;

public class UpdateUserProfileActivity extends AppCompatActivity {

    ImageView profile_img;
    EditText name;
    EditText mail;
    EditText password;
    EditText phone;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_profile);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://ratatouille-d6acf.appspot.com/" + mAuth.getUid() + "/userProfile");
        profile_img = findViewById(R.id.img_update);
        name = findViewById(R.id.editTextName);
        mail = findViewById(R.id.editTextEmail);
        password = findViewById(R.id.editTextPassword);
        phone = findViewById(R.id.editTextPhone);
        FirebaseDatabase.getInstance().getReference("chefs/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Chef.class) != null) {
                    Chef chAux=dataSnapshot.getValue(Chef.class);
                    name.setText(chAux.getNombre());
                    mail.setText(chAux.getCorreo());
                    phone.setText(Integer.toString(chAux.getTelefono()));
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
                    name.setText(clAux.getNombre());
                    mail.setText(clAux.getCorreo());
                    //TODO: Show password or change password?
                    phone.setText(Integer.toString(clAux.getTelefono()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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
    }
}
