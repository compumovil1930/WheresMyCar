package com.ratatouille;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapaServicio extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button confirm;
    Button btn_menu;
    TextView txtDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_servicio);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        txtDir = findViewById(R.id.txtDir);
        txtDir.setText(getIntent().getStringExtra("direccion"));
        btn_menu = findViewById(R.id.btn_menu_ser);
        Spinner spinner=(Spinner)findViewById(R.id.spinnerDirecciones);
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.servicios, android.R.layout.simple_list_item_1);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter3);
        confirm = findViewById(R.id.btnConfirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), mapaTipoServicio.class);
                startActivity(intent);
            }
        });
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: agregar actividad de lista de items de menú
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng puj = new LatLng(4.626882, -74.064094);
        mMap.addMarker(new MarkerOptions().position(puj).title("Javeriana"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puj,17));
    }
}
