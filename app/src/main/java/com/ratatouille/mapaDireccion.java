package com.ratatouille;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class mapaDireccion extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button sel_dir;
    Button btn_menu;
    EditText edTxtDir;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_direccion);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        sel_dir = findViewById(R.id.btnDir);
        btn_menu = findViewById(R.id.btn_menu_dir);
        edTxtDir = findViewById(R.id.edTxtDir);
        sel_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), mapaServicio.class);
                intent.putExtra("direccion", edTxtDir.getText().toString());
                startActivity(intent);
            }
        });
        btn_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MenuActivity.class);
                startActivity(intent);
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
