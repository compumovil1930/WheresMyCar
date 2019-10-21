package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    Button buttonSiguiente;
    FirebaseAuth mAuth;
    EditText edMail;
    EditText edPass;
    EditText edPassAgain;
    EditText edPhoneNumber;
    TextView tvNacimiento;
    TextView tvIdNumber;
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        buttonSiguiente =findViewById(R.id.buttonSiguiente);
        edMail=findViewById(R.id.editTextEmail);
        edPass=findViewById(R.id.editTextContraseña);
        edPassAgain=findViewById(R.id.editTextContraseña4);
        edPhoneNumber=findViewById(R.id.editTextPhone);
        tvNacimiento=findViewById(R.id.editTextNacimiento);
        tvIdNumber=findViewById(R.id.editTextCC);
        tvNacimiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal=Calendar.getInstance();
                int year=cal.get(Calendar.YEAR);
                int month=cal.get(Calendar.MONTH);
                int day=cal.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog dialog=new DatePickerDialog(
                        RegisterActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mDateSetListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                Log.d("DATEPICKER","onDateSet: "+dayOfMonth+" "+month+" "+year);
                String date=dayOfMonth+"/"+month+"/"+year;
                tvNacimiento.setText(date);
            }
        };
        buttonSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(v.getContext(), ImagenActivity.class);
                startActivity(intent);
            }
        });
    }

/*
    private void registerUser(String email,String password){
        if(validateForm()){
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("NEW USER", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        if(user!=null){
                            UserProfileChangeRequest.Builder upcrb=new UserProfileChangeRequest.Builder();
                            upcrb.setDisplayName(etNombre.getText().toString()+" "+etApellido.getText().toString());
                            upcrb.setPhotoUri(Uri.parse("path/to/pic"));
                            user.updateProfile(upcrb.build());
                            updateUI(user);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("NEW USER", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // ...
                }
            });
        }
    }
*/


    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent=new Intent(getBaseContext(),EscogerTipoActivity.class);
            intent.putExtra("user",currentUser.getEmail());
            startActivity(intent);
        }
    }


    private boolean validateForm(){
        boolean valid=true;
        String email=edMail.getText().toString();
        if(TextUtils.isEmpty(email)){
            edMail.setError("Requerido");
            valid=false;
        }
        else{
            edMail.setError(null);
        }
        String password=edPass.getText().toString();
        if(TextUtils.isEmpty(password)){
            edPass.setError("Requerido");
            valid=false;
        }
        else{
            edPass.setError(null);
        }
        if(!edPass.equals(edPassAgain)){
            Toast.makeText(RegisterActivity.this,"La contraseñas ingresadas no coinciden",Toast.LENGTH_LONG).show();
            valid=false;
            edPass.setText("");
            edPassAgain.setText("");
        }
        return valid;
    }
}
