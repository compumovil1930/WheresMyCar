package com.ratatouille;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddPayMethod extends AppCompatActivity {

    EditText mmaa, numberCard, CCV;
    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser fireUser = mAuth.getCurrentUser();
    String idUser = fireUser.getUid();
    long numtarjetas;
    DatabaseReference userReference;
    DatabaseReference payMethodsReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_metodo_de_pago);
        mmaa = findViewById(R.id.MMAA);
        numberCard = findViewById(R.id.Number);
        CCV = findViewById(R.id.CCV);
        userReference = FirebaseDatabase.getInstance().getReference();
        userReference.child("MetodosPago").child(idUser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    numtarjetas = dataSnapshot.getChildrenCount();
                    Log.i("Numero de Tarjetas", String.valueOf(numtarjetas));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mmaa.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String working = s.toString();
                boolean isValid = true;
                mmaa.setError(null);
                if (working.length() == 2 && before == 0) {
                    if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                        isValid = false;
                    } else {
                        working += "/";
                        mmaa.setText(working);
                        mmaa.setSelection(working.length());
                    }
                } else if (working.length() == 5 && before == 0) {
                    String enteredYear = working.substring(3);
                    DateFormat df = new SimpleDateFormat("yy");
                    String formattedDate = df.format(Calendar.getInstance().getTime());
                    int currentYear = Integer.parseInt(formattedDate);
                    if (Integer.parseInt(enteredYear) < currentYear) {
                        isValid = false;
                    }
                } else if (working.length() != 5) {
                    isValid = false;
                } else
                    isValid = true;

                if (!isValid) {
                    mmaa.setError("Enter a valid date: MM/YY");
                } else {
                    mmaa.setError(null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void AgregarMetododePago(View v) {
        if(mmaa.getText()!=null && numberCard.getText()!=null && CCV.getText()!=null)
        if(luhnTest(numberCard.getText().toString())==true){
            String primerosnumeros = numberCard.getText().toString().substring(0,4);
            int tarjeta = Integer.parseInt(primerosnumeros);
            String tipoTarjeta="";
            System.out.println(tarjeta);
            if (tarjeta >= 5100 && tarjeta <= 5599) {
                System.out.println("la tarjeta es Mastercard");
                tipoTarjeta = "MasterCard";
            } else if (tarjeta >= 4000 && tarjeta <= 4999) {
                System.out.println("La tarjeta es visa");
                tipoTarjeta = "Visa";
            } else if (tarjeta >= 3400 && tarjeta <= 3799) {
                System.out.println("La tarjeta es american express");
                tipoTarjeta = "American Express";
            } else if (tarjeta >= 3000 && tarjeta <= 3059) {
                System.out.println("La tarjeta es diners club");
                tipoTarjeta = "Diners Club";
            } else if (tarjeta == 6011) {
                System.out.println("La tarjeta es discover");
                tipoTarjeta = "Discover";
            } else if (tarjeta == 6520) {
                System.out.println("La tarjeta es palacio del hierro");
                tipoTarjeta = "Palacio del Hierro";
            }
            payMethodsReference = userReference.child("MetodosPago").child(idUser).child(String.valueOf(numtarjetas));
            payMethodsReference.child("Proveedor").setValue(tipoTarjeta);
            payMethodsReference.child("Numero").setValue(numberCard.getText().toString());
            finish();
        }
        else
            numberCard.setError("Ingrese un numero de tarjeta valido");
    }

    private static boolean luhnTest(String numero) {

        int s1 = 0, s2 = 0;
        String reversa = new StringBuffer(numero).reverse().toString();
        for (int i = 0; i < reversa.length(); i++) {
            int digito = Character.digit(reversa.charAt(i), 10);
            if (i % 2 == 0) {//this is for odd digits, they are 1-indexed in the algorithm
                s1 += digito;
            } else {//add 2 * digit for 0-4, add 2 * digit - 9 for 5-9
                s2 += 2 * digito;
                if (digito >= 5) {
                    s2 -= 9;
                }
            }
        }

        return (s1 + s2) % 10 == 0;

    }
}
