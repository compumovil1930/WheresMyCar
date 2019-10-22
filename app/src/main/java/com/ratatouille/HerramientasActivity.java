package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HerramientasActivity extends AppCompatActivity {

    Button next;
    LinearLayout layUtensilios;
    LinearLayout layElectromesticos;
    TextView txt;
    String tipo;
    List<CheckBox> listaUtensilios=new ArrayList<CheckBox>();
    List<CheckBox> listaElectrodomesticos=new ArrayList<CheckBox>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_herramientas);
        layUtensilios=findViewById(R.id.linearUtensilios);
        layElectromesticos=findViewById(R.id.linearElectro);
        next=findViewById(R.id.buttonSiguienteImagen);
        txt=findViewById(R.id.textViewTexto);

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

        /*CheckBox check=new CheckBox(HerramientasActivity.this);
                check.setText("prueba");
                layUtensilios.addView(check);*/

        /*

        tipo=getIntent().getStringExtra("tipo");
        if(!tipo.equals("chef")){
            txt.setText("De las siguientes listas de utensilios y electrodomesticos selecciona los cuales posees");
        }else{
            txt.setText("De las siguientes listas de utensilios y electrodomesticos selecciona los cuales puedes llevar en cualquier momento a donde un cliente");
        }*/

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tipo.equals("chef")){
                    Intent intent=new Intent(v.getContext(),mapaDireccion.class);
                    startActivity(intent);
                }else{
                    Intent intent=new Intent(v.getContext(),RecetasChefActivity.class);
                    startActivity(intent);
                }

            }
        });
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
