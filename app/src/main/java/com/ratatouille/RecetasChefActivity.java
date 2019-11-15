package com.ratatouille;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Herramienta;
import com.ratatouille.models.Ingrediente;
import com.ratatouille.models.Receta;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RecetasChefActivity extends AppCompatActivity {

    LinearLayout layUtensilios;
    LinearLayout layElectromesticos;
    LinearLayout layIngredientes;
    List<CheckBox> listaUtensilios = new ArrayList<CheckBox>();
    List<CheckBox> listaElectrodomesticos = new ArrayList<CheckBox>();
    List<CheckBox> listaIngredientes = new ArrayList<CheckBox>();
    Boolean minimaReceta = false;
    List<Receta> listaRecetasActuales = new ArrayList<Receta>();
    Button bAnadirReceta;
    Button bTerminarRegistro;
    EditText etNombreReceta;
    EditText etDescripcionReceta;
    EditText etTiempoReceta;
    String tipo;
    Bundle bundle;

    DatabaseReference mDatabaseChefs;
    FirebaseAuth mAuth;
    StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recetas_chef);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseChefs = FirebaseDatabase.getInstance().getReference("chefs");
        mStorage = FirebaseStorage.getInstance().getReference();

        bundle = getIntent().getBundleExtra("bundle");
        tipo = bundle.getString("tipo");
        layUtensilios = findViewById(R.id.linearUtensilios);
        layElectromesticos = findViewById(R.id.linearElectro);
        layIngredientes = findViewById(R.id.linearIngredientes);
        bAnadirReceta = findViewById(R.id.buttonAñadirReceta);
        bTerminarRegistro = findViewById(R.id.buttonTerminarRegistro);
        etNombreReceta = findViewById(R.id.editTextNombreReceta);
        etDescripcionReceta = findViewById(R.id.editTextDescripcionReceta);
        etTiempoReceta = findViewById(R.id.editTextTiempoReceta);
        bAnadirReceta.setOnClickListener(new View.OnClickListener() {
            Boolean unUtensilio = false;
            Boolean unElectrodomestico = false;
            Boolean unIngrediente = false;

            @Override
            public void onClick(View v) {
                if (validateForm()) {
                    List<Herramienta> auxHerramientas = new ArrayList<Herramienta>();
                    List<Ingrediente> auxIngredientes = new ArrayList<Ingrediente>();
                    for (CheckBox check : listaUtensilios) {
                        if (check.isChecked()) {
                            unUtensilio = true;
                            auxHerramientas.add(new Herramienta(check.getText().toString(), 1));
                        }
                    }
                    for (CheckBox check : listaElectrodomesticos) {
                        if (check.isChecked()) {
                            unElectrodomestico = true;
                            auxHerramientas.add(new Herramienta(check.getText().toString(), 2));
                        }
                    }
                    for (CheckBox check : listaIngredientes) {
                        if (check.isChecked()) {
                            unIngrediente = true;
                            auxIngredientes.add(new Ingrediente(check.getText().toString(), "Dos"));
                        }
                    }
                    if (!unUtensilio || !unElectrodomestico) {
                        Toast.makeText(v.getContext(), "Debe escoger almenos un utensilio, un electrodomestico y un ingrediente", Toast.LENGTH_LONG).show();
                    } else {
                        String nombreAux = etNombreReceta.getText().toString();
                        String descAux = etDescripcionReceta.getText().toString();
                        int tiempoAux = Integer.parseInt(etTiempoReceta.getText().toString());
                        Receta recetaAux = new Receta(nombreAux, descAux, tiempoAux, auxHerramientas, auxIngredientes);
                        minimaReceta = true;
                        listaRecetasActuales.add(recetaAux);
                        Toast.makeText(v.getContext(), "Receta añadida con éxito", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        bTerminarRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (minimaReceta) {
                    registerUser(bundle.getString("email"), bundle.getString("password"));
                } else {
                    Toast.makeText(v.getContext(), "Debe añadir almenos una receta para continuar", Toast.LENGTH_LONG).show();
                }
            }
        });

        //Ingredientes a la lista de checkbox
        try {
            JSONObject jsonIngredientes = new JSONObject(loadJSONFromAsset("ingredientes.json"));
            JSONArray ingredientesJsonArray = jsonIngredientes.getJSONArray("ingredientes");
            for (int i = 0; i < ingredientesJsonArray.length(); i++) {
                JSONObject jsonObject = ingredientesJsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                CheckBox chAux = new CheckBox(RecetasChefActivity.this);
                chAux.setText(nombre);
                listaIngredientes.add(chAux);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Utensilios a la lista de checkbox
        try {
            JSONObject jsonUtensilios = new JSONObject(loadJSONFromAsset("utensilios.json"));
            JSONArray utensiliosJsonArray = jsonUtensilios.getJSONArray("utensilios");
            for (int i = 0; i < utensiliosJsonArray.length(); i++) {
                JSONObject jsonObject = utensiliosJsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                CheckBox chAux = new CheckBox(RecetasChefActivity.this);
                chAux.setText(nombre);
                listaUtensilios.add(chAux);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Electrodomesticos a la lista de checkbox
        try {
            JSONObject jsonElectro = new JSONObject(loadJSONFromAsset("electrodomesticos.json"));
            JSONArray electrodomesticosJsonArray = jsonElectro.getJSONArray("electrodomesticos");
            for (int i = 0; i < electrodomesticosJsonArray.length(); i++) {
                JSONObject jsonObject = electrodomesticosJsonArray.getJSONObject(i);
                String nombre = jsonObject.getString("nombre");
                CheckBox chAux = new CheckBox(RecetasChefActivity.this);
                chAux.setText(nombre);
                listaElectrodomesticos.add(chAux);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Adicion de checkbox a la lista ingredientes
        for (int i = 0; i < listaIngredientes.size(); i++) {
            layIngredientes.addView(listaIngredientes.get(i));
        }
        //Adicion de checkbox a la lista utensilios
        for (int i = 0; i < listaUtensilios.size(); i++) {
            layUtensilios.addView(listaUtensilios.get(i));
        }
        //Adicion de checkbox a la lista electrodomesticos
        for (int i = 0; i < listaElectrodomesticos.size(); i++) {
            layElectromesticos.addView(listaElectrodomesticos.get(i));
        }
    }


    public String loadJSONFromAsset(String filename) {
        String json;
        try {
            InputStream is = this.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    private Boolean validateForm() {
        boolean valid = true;
        String nombre = etNombreReceta.getText().toString();
        if (TextUtils.isEmpty(nombre)) {
            etNombreReceta.setError("Requerido");
            valid = false;
        }
        String descripcion = etDescripcionReceta.getText().toString();
        if (TextUtils.isEmpty(descripcion)) {
            etDescripcionReceta.setError("Requerido");
            valid = false;
        }
        String tiempo = etTiempoReceta.getText().toString();
        if (TextUtils.isEmpty(tiempo)) {
            etTiempoReceta.setError("Requerido");
            valid = false;
        }
        return valid;
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
                        Chef chAux = (Chef) bundle.getSerializable("datos");
                        chAux.setListaRecetas(listaRecetasActuales);
                        mDatabaseChefs = FirebaseDatabase.getInstance().getReference("chefs/" + mAuth.getCurrentUser().getUid());
                        mDatabaseChefs.setValue(chAux);
                        chargeImage();
                        updateUI(user);
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("NEW USER", "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RecetasChefActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    updateUI(null);
                }

                // ...
            }
        });
    }


    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(getBaseContext(), MapServiceAvailableActivity.class);
            intent.putExtra("user", currentUser.getEmail());
            startActivity(intent);
        }
    }

    private void chargeImage (){
        Bitmap image = (Bitmap) bundle.getParcelable("userProfile");

        StorageReference filePath = mStorage.child(mAuth.getCurrentUser().getUid()).child("userProfile");

        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        bmOptions.inSampleSize = 1;
        bmOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
        bmOptions.inJustDecodeBounds = false;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 10, baos);
        final byte[] photo = baos.toByteArray();

        UploadTask uploadTask = filePath.putBytes(photo);

        uploadTask.addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"progress",Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"failure",Toast.LENGTH_SHORT).show();
            }
        });
    }
}

