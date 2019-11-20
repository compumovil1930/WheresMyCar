package com.ratatouille;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Servicio;

public class ChefEnCaminoActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final double RADIUS_OF_EARTH_KM = 6371.01;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private Polyline currentPolyline;
    Marker chef, customer;
    Button btnCancelar;
    double latitude;
    double longitude;

    String keyServicio;
    Bundle bundle;

    FirebaseDatabase database;
    DatabaseReference mDatabaseChefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chef_en_camino);
        getSupportActionBar().hide();
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        btnCancelar = findViewById(R.id.buttonCancelar);
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), mapaDireccion.class);
                cambiarEstadoDeReserva();
                startActivity(intent);
            }
        });
        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();

                            customer.setPosition(new LatLng(latitude, longitude));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                            chef.setPosition(new LatLng(location.getLatitude()-0.05, location.getLongitude()-0.05)); // TODO: bring chef address from firebase
                            chef.setTitle("Tu chef"); //TODO: add snippet with name
                            LatLngBounds.Builder builder = new LatLngBounds.Builder();
                            builder.include(customer.getPosition());
                            builder.include(chef.getPosition());
                            new FetchURL(ChefEnCaminoActivity.this).execute(getUrl(customer.getPosition(), chef.getPosition(), "driving"), "driving");
                            LatLngBounds bounds = builder.build();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 125));
                            Toast.makeText(ChefEnCaminoActivity.this, "Tu chef está a " + distance(chef.getPosition().latitude, chef.getPosition().longitude, customer.getPosition().latitude, customer.getPosition().longitude) + "Km.", Toast.LENGTH_LONG).show();
                        }
                    }
                }
        );
        //TODO: move chef marker with onDataChange callback
        //keyServicio = putExtraData("keyService");

        bundle = getIntent().getBundleExtra("bundle");

        mDatabaseChefs = database.getInstance().getReference("chefs");
        mDatabaseChefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0)
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        if (singleSnap != null) {
                            Log.i("KeyChef", bundle.get("keyChef").toString());
                            if (bundle.get("keyChef").toString().equals(singleSnap.getKey())){
                                Chef chefEncontrado = singleSnap.getValue(Chef.class);

                                //chef.remove();
                                latitude = chefEncontrado.getDireccion().getLatitud();
                                longitude = chefEncontrado.getDireccion().getLongitud();
                                chef.setPosition(new LatLng(latitude, longitude));
                                //mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(latitude, longitude)));
                                moveCamera(latitude, longitude, 15);
                            }
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        customer = mMap.addMarker(new MarkerOptions().position(new LatLng(4, -72)).icon(BitmapDescriptorFactory.fromResource(R.drawable.anton)).title("Tu ubicación")); //TODO: add snippet with name
        chef = mMap.addMarker(new MarkerOptions().position(new LatLng(4, -72)).icon(BitmapDescriptorFactory.fromResource(R.drawable.remy)).title("Tu chef"));
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    private void requestPermission(Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, idCode);
        }
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result * 100.0) / 100.0;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setColor(0xFFF44336);
    }

    private void cambiarEstadoDeReserva(){
        //Ingresar a firebase y cambiar el estado de la reserva a cancelado
    }

    private void moveCamera(double latitude, double longitude, int zoom){
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoom)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }
}
