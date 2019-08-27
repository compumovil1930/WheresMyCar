package com.ratatouille;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class PayMethods extends AppCompatActivity {

    ListView listView;
    TextView textView;
    String[] listItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_metodos_de_pago);

        listView =  findViewById(R.id.listView);
       // textView =  findViewById(R.id.textViewList);
        listItem = getResources().getStringArray(R.array.mediosPago);
        final ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, listItem);
        listView.setAdapter(adapter);


    }
    public void addPayMethod(View v){
        Intent intent = new Intent(v.getContext(), AddPayMethod.class);
        startActivity(intent);
    }
}
