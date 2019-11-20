package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
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

import java.text.ParseException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

public class Perfil extends AppCompatActivity {
    ImageView profile_img;
    TextView name, mail, edad, phone, direccion;
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;
    StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);
        mAuth = FirebaseAuth.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReferenceFromUrl("gs://ratatouille-d6acf.appspot.com/" + mAuth.getUid() + "/userProfile");
        profile_img = findViewById(R.id.img_update);
        name = findViewById(R.id.editTextName);
        mail = findViewById(R.id.editTextEmail);
        edad = findViewById(R.id.editTextEdad);
        phone = findViewById(R.id.editTextPhone);
        direccion = findViewById(R.id.editTextDireccion);
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
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Cliente.class) != null) {
                    Cliente clAux=dataSnapshot.getValue(Cliente.class);
                    name.setText(clAux.getNombre());
                    mail.setText(clAux.getCorreo());
                    //TODO: Show password or change password?
                    phone.setText(Integer.toString(clAux.getTelefono()));
                    try {
                        edad.setText(calcularEdad(clAux.getFechaNacimiento()));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    direccion.setText(clAux.getDireccion().getDireccion());
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


    @RequiresApi(api = Build.VERSION_CODES.O)
    private String calcularEdad(String fechaNacimiento) throws ParseException {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fechaNac = LocalDate.parse(fechaNacimiento, fmt);
        LocalDate ahora = LocalDate.now();

        Period periodo = Period.between(fechaNac, ahora);
        return String.valueOf(periodo.getYears());
    }
}
