package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Cliente;
import com.ratatouille.models.Herramienta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HerramientasActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    Button next;
    LinearLayout layUtensilios;
    LinearLayout layElectromesticos;
    List<CheckBox> listaUtensilios=new ArrayList<CheckBox>();
    List<CheckBox> listaElectrodomesticos=new ArrayList<CheckBox>();
    TextView txt;
    String tipo;
    DatabaseReference mDatabaseClientes;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herramientas);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseClientes = FirebaseDatabase.getInstance().getReference("clientes");
        bundle=getIntent().getBundleExtra("bundle");
        layUtensilios=findViewById(R.id.linearUtensilios);
        layElectromesticos=findViewById(R.id.linearElectro);
        next=findViewById(R.id.buttonSiguienteImagen);
        txt=findViewById(R.id.textViewTexto);
        //Extracion de tipo, puede ser chef o cliente
        tipo=bundle.getString("tipo");
        if(!tipo.equals("chef")){
            txt.setText("De las siguientes listas de utensilios y electrodomesticos selecciona los cuales posees");
        }else{
            txt.setText("De las siguientes listas de utensilios y electrodomesticos selecciona los cuales puedes llevar en cualquier momento a donde un cliente");
        }

        //Utensilios a la lista de checkbox
        try{
            JSONObject jsonUtensilios = new JSONObject(loadJSONFromAsset("utensilios.json"));
            JSONArray utensiliosJsonArray=jsonUtensilios.getJSONArray("utensilios");
            for(int i=0;i<utensiliosJsonArray.length();i++){
                JSONObject jsonObject=utensiliosJsonArray.getJSONObject(i);
                String nombre=jsonObject.getString("nombre");
                CheckBox chAux=new CheckBox(HerramientasActivity.this);
                chAux.setText(nombre);
                listaUtensilios.add(chAux);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        //Electrodomesticos a la lista de checkbox
        try{
            JSONObject jsonElectro=new JSONObject(loadJSONFromAsset("electrodomesticos.json"));
            JSONArray electrodomesticosJsonArray=jsonElectro.getJSONArray("electrodomesticos");
            for(int i=0;i<electrodomesticosJsonArray.length();i++){
                JSONObject jsonObject=electrodomesticosJsonArray.getJSONObject(i);
                String nombre=jsonObject.getString("nombre");
                CheckBox chAux=new CheckBox(HerramientasActivity.this);
                chAux.setText(nombre);
                listaElectrodomesticos.add(chAux);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }


        //Adicion de checkbox a la lista utensilios
        for(int i=0;i<listaUtensilios.size();i++){
            layUtensilios.addView(listaUtensilios.get(i));
        }
        //Adicion de checkbox a la lista electrodomesticos
        for(int i=0;i<listaElectrodomesticos.size();i++){
            layElectromesticos.addView(listaElectrodomesticos.get(i));
        }

        next.setOnClickListener(new View.OnClickListener() {
            Boolean unUtensilio=false;
            Boolean unElectrodomestico=false;
            @Override
            public void onClick(View v) {
                List<Herramienta> auxHerramientas=new ArrayList<Herramienta>();
                for(CheckBox check : listaUtensilios){
                    if(check.isChecked()){
                        unUtensilio=true;
                        auxHerramientas.add(new Herramienta(check.getText().toString(),1));
                    }
                }
                for(CheckBox check : listaElectrodomesticos){
                    if(check.isChecked()){
                        unElectrodomestico=true;
                        auxHerramientas.add(new Herramienta(check.getText().toString(),2));
                    }
                }
                if(!unUtensilio || !unElectrodomestico){
                    Toast.makeText(v.getContext(),"Debe escoger almenos un utensilio y un electrodomestico",Toast.LENGTH_LONG).show();
                }else{
                    if(tipo.equals("chef")){
                        Chef chAux=(Chef) bundle.getSerializable("datos");
                        chAux.toString();
                        chAux.setListaHerramientas(auxHerramientas);
                        bundle.putSerializable("datos",(Serializable)chAux);
                        Intent intent=new Intent(v.getContext(), DetallesChefActivity.class);
                        intent.putExtra("bundle",bundle);
                        startActivity(intent);

                    }
                    else{
                        Cliente clAux=(Cliente) bundle.getSerializable("datos");
                        clAux.toString();
                        clAux.setListaHerramientas(auxHerramientas);
                        String id = mDatabaseClientes.push().getKey();
                        registerUser(bundle.getString("email"),bundle.getString("password"));
                        mDatabaseClientes.child(id).setValue(clAux);
                    }
                }
            }
        });
    }



    private void registerUser(String email, String password) {
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        Log.d("NEW USER", "createUserWithEmail:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                        if (user != null) {
                            UserProfileChangeRequest.Builder upcrb = new UserProfileChangeRequest.Builder();
                            upcrb.setPhotoUri(Uri.parse("path/to/pic"));
                            user.updateProfile(upcrb.build());
                            updateUI(user);
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w("NEW USER", "createUserWithEmail:failure", task.getException());
                        Toast.makeText(HerramientasActivity.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                        updateUI(null);
                    }

                    // ...
                }
            });
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(getBaseContext(), EscogerTipoActivity.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        }
    }


    public String loadJSONFromAsset(String filename){
        String json;
        try{
            InputStream is=this.getAssets().open(filename);
            int size=is.available();
            byte[] buffer=new byte[size];
            is.read(buffer);
            is.close();
            json=new String(buffer,"UTF-8");
        }catch (IOException ex){
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
