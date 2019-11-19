package com.ratatouille;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;


public class PayMethods extends AppCompatActivity {

    FirebaseAuth fireAuth = FirebaseAuth.getInstance();
    FirebaseUser firebaseUser = fireAuth.getCurrentUser();
    DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chefs").child(firebaseUser.getUid()).child("Metodos de paggo");
    ListView listItem;
    //String[] metodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_de_pago);

       // metodos = getResources().getStringArray(R.array.mediosPago);
        ArrayList<String> list = new ArrayList<String>();
        //Collections.addAll(list, getResources().getStringArray(R.array.mediosPago));


        //instantiate custom adapter
        MyCustomAdapter adapter = new MyCustomAdapter(list, this);

        //handle listview and assign adapter
        listItem = (ListView)findViewById(R.id.listView);
        listItem.setAdapter(adapter);


    }
    public void addPayMethod(View v){
        Intent intent = new Intent(v.getContext(), AddPayMethod.class);
        startActivity(intent);
    }
}
