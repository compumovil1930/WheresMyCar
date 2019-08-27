package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;


public class PayMethods extends AppCompatActivity {


    ListView listItem;
    String[] metodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_de_pago);
        metodos = getResources().getStringArray(R.array.mediosPago);
        ArrayList<String> list = new ArrayList<String>();
        Collections.addAll(list, getResources().getStringArray(R.array.mediosPago));


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
