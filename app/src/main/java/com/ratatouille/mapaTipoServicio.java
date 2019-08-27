package com.ratatouille;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class mapaTipoServicio extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    Button pedirEnTipoServicio;
    String[] arreglo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_tipo_servicio);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        initArray();
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,arreglo);
        ListView listView=(ListView)findViewById(R.id.listTipoServicio);
        listView.setAdapter(adapter);

        Spinner spinner=(Spinner)findViewById(R.id.spinnerDirecciones);
        ArrayAdapter adapter2 = ArrayAdapter.createFromResource(this, R.array.direcciones, android.R.layout.simple_list_item_1);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter2);

        Spinner spinner2=(Spinner)findViewById(R.id.spinnerMetodosPago);
        ArrayAdapter adapter3 = ArrayAdapter.createFromResource(this, R.array.mediosPago, android.R.layout.simple_list_item_1);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter3);

        pedirEnTipoServicio= findViewById(R.id.buttonPedirServicio);
        pedirEnTipoServicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), PreparandoComensalActivity.class);
                startActivity(intent);
            }
        });
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */

    private void initArray(){
        arreglo=new String[3];
        arreglo[0]="Chef principiante";
        arreglo[1]="Chef profesional";
        arreglo[2]="Chef de primera";
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng puj = new LatLng(4.626882, -74.064094);
        mMap.addMarker(new MarkerOptions().position(puj).title("Javeriana"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(puj,17));
    }
}
