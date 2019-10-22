package com.ratatouille;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    Button ingresar;
    Button nuevaCuenta;
    EditText edMail;
    EditText edPass;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        edMail=findViewById(R.id.txtEmail);
        edPass=findViewById(R.id.txtPassword);
        nuevaCuenta = findViewById(R.id.buttonCrearCuenta);
        nuevaCuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        ingresar = findViewById(R.id.buttonDataUpdate);
        ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInUser(edMail.getText().toString(),edPass.getText().toString());
            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();
        FirebaseUser currentUser=mAuth.getCurrentUser();
        updateUI(currentUser);
    }



    private void updateUI(FirebaseUser currentUser){
        if(currentUser!=null){
            Intent intent=new Intent(getBaseContext(),EscogerTipoActivity.class);
            intent.putExtra("user",currentUser.getEmail());
            startActivity(intent);
        }
        else{
            edMail.setText("");
            edPass.setText("");
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
        return valid;
    }


    private void signInUser(String email,String password){
        if(validateForm()){
            mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        Log.d("INICIOSESION","signInWithEmail:success");
                        FirebaseUser user=mAuth.getCurrentUser();
                        updateUI(user);
                    }else{
                        Log.w("FALLOSESION","signInWithEmail:failure",task.getException());
                        Toast.makeText(MainActivity.this,"Authenticacion failed",Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }
                }
            });
        }
    }
    private void requestPermission(Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, idCode);
        }
    }
}
