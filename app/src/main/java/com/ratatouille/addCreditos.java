package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Cliente;

public class addCreditos extends AppCompatActivity {
    private EditText txtCreditos;
    private TextView txtPesos;
    private FirebaseAuth mAuth =  FirebaseAuth.getInstance();
    private FirebaseUser currentUser = mAuth.getCurrentUser();
    String idU = currentUser.getUid();
    private DatabaseReference creditosReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_creditos);
        txtCreditos = findViewById(R.id.txtCreditos);
        txtPesos = findViewById(R.id.txtPesos);

        txtCreditos.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().isEmpty()==false)
                txtPesos.setText(String.valueOf(Integer.valueOf(charSequence.toString())*1000));

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }


    public void cargarCreditos(View v){
        if (currentUser != null) {
            FirebaseDatabase.getInstance().getReference("chefs/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue(Chef.class) != null) {

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
                        String creditos = dataSnapshot.child("creditos").getValue().toString();
                        creditosReference.child("clientes").child(idU).child("creditos").setValue(Integer.valueOf(creditos)+Integer.valueOf(txtCreditos.getText().toString()));
                        //Log.i("Creditos", String.valueOf(Integer.valueOf(creditos)+Integer.valueOf(txtCreditos.getText().toString())));
                        Intent intent = new Intent(getApplicationContext(), Perfil.class);
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

}
