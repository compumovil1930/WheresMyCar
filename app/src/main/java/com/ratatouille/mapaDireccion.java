package com.ratatouille;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.ratatouille.models.Chef;
import com.ratatouille.models.Cliente;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class mapaDireccion extends FragmentActivity implements OnMapReadyCallback {

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;
    static final double RADIUS_OF_EARTH_KM = 6371.01;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    Marker customer;
    List<Marker> chefs;
    double latitude;
    double longitude;
    Button sel_dir;
    Button btn_menu;
    EditText edTxtDir;
    LinearLayout myDirecitionsLay;
    FirebaseDatabase database;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseChefs;
    DatabaseReference mDatabaseClientes;

    public static final double lowerLeftLatitude = 4.515144;
    public static final double lowerLeftLongitude = -74.226309;
    public static final double upperRightLatitude = 4.758701;
    public static final double upperRigthLongitude = -74.023228;

    Geocoder mGeocoder;
    double mainLatitude;
    double mainLongitude;
    boolean found;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapa_direccion);
        mAuth = FirebaseAuth.getInstance();
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        myDirecitionsLay = findViewById(R.id.linearMyDirections);
        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mapFragment.getMapAsync(this);

        edTxtDir = findViewById(R.id.edTxtDir);
        found = false;

        /*
        mDatabaseClientes = database.getReference("clientes/"+mAuth.getCurrentUser().getUid());
        mDatabaseClientes.child("direccion").child("direccion");*/

        FirebaseDatabase.getInstance().getReference("clientes/" + mAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue(Cliente.class) != null) {
                    Cliente clAux = dataSnapshot.getValue(Cliente.class);
                    TextView direction = new TextView(mapaDireccion.this);
                    direction.setText(clAux.getDireccion().getDireccion());
                    myDirecitionsLay.addView(direction);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        edTxtDir.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (!edTxtDir.getText().toString().trim().equalsIgnoreCase("")) {
                    if (actionId == EditorInfo.IME_ACTION_DONE) {
                        String addressString = edTxtDir.getText().toString();
                        findAddress(addressString, mGeocoder);
                    }
                }
                return false;
            }
        });

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location locationN) {

                        mGeocoder = new Geocoder(getBaseContext());
                        if (locationN != null) {
                            Log.i("FLOCATION", "First Location update in the callback: " + locationN);
                            customer.setPosition(new LatLng(locationN.getLatitude(), locationN.getLongitude()));
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(customer.getPosition()));
                        }
                    }
                }
        );
        mLocationRequest = createLocationRequest();
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                Location location = locationResult.getLastLocation();
                Log.i("LOCATION", "Location update in the callback: " + location);
                if (location != null) {
                    if (edTxtDir.getText().toString().trim().equalsIgnoreCase("")) {
                        if (location.getLatitude() != latitude || location.getLongitude() != longitude) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            customer.setPosition(new LatLng(latitude, longitude));
                            showChefs();
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(customer.getPosition()));
                            moveCamera(latitude, longitude, 15);
                            //Toast.makeText(getApplicationContext(),"Estoy vacio", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (found) {
                            //Toast.makeText(getApplicationContext(), "NO estoy vacio", Toast.LENGTH_LONG).show();
                            customer.setPosition(new LatLng(mainLatitude, mainLongitude));
                            showChefs();
                            //mMap.moveCamera(CameraUpdateFactory.newLatLng(customer.getPosition()));
                            moveCamera(mainLatitude, mainLongitude, 15);
                        }
                    }
                }
            }
        };


        //-----------------------------------------------------------------------------------------------------------------------------
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
        SettingsClient client = LocationServices.getSettingsClient(mapaDireccion.this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnFailureListener(this, new OnFailureListener() {

            @Override
            public void onFailure(@NonNull Exception e) {

                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case CommonStatusCodes.RESOLUTION_REQUIRED:
                        try {

                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(mapaDireccion.this, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sendEx) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        break;
                }
            }
        });
        //-----------------------------------------------------------------------------------------------------------------------------


        sel_dir = findViewById(R.id.btnDir);
        btn_menu = findViewById(R.id.btn_menu_dir);

        sel_dir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("clientes/"+mAuth.getUid()+"/direccion");
                Intent intent = new Intent(view.getContext(), ChefCercanosActivity.class);
                intent.putExtra("direccion", edTxtDir.getText().toString());
                ref.child("latitud").setValue(latitude);
                ref.child("longitud").setValue(longitude);
                if (edTxtDir.getText().toString().trim().equalsIgnoreCase("")) {
                    ref.child("latitud").setValue(latitude);
                    ref.child("longitud").setValue(longitude);
                    intent.putExtra("latitud", latitude);
                    intent.putExtra("longitud", longitude);
                    startActivity(intent);
                } else {
                    if (found) {
                        ref.child("latitud").setValue(latitude);
                        ref.child("longitud").setValue(longitude);
                        intent.putExtra("latitud", mainLatitude);
                        intent.putExtra("longitud", mainLatitude);
                        startActivity(intent);
                    }
                }

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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        customer = mMap.addMarker(new MarkerOptions().position(new LatLng(4, -72)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

    }

    private void requestPermission(Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, idCode);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS: {
                if (resultCode == RESULT_OK) {
                    startLocationUpdates();
                } else {
                    Toast.makeText(this, "sin acceso a localizacion, hardware deshabilitado!", Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(30000);
        mLocationRequest.setFastestInterval(25000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }


    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
                    SettingsClient client = LocationServices.getSettingsClient(this);
                    Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());
                    task.addOnSuccessListener(this, new OnSuccessListener<LocationSettingsResponse>() {
                        @Override
                        public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                            startLocationUpdates();
                        }
                    });
                    task.addOnFailureListener(this, new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            int statusCode = ((ApiException) e).getStatusCode();
                            switch (statusCode) {
                                case CommonStatusCodes.RESOLUTION_REQUIRED:
                                    try {
                                        ResolvableApiException resolvable = (ResolvableApiException) e;
                                        resolvable.startResolutionForResult(mapaDireccion.this, REQUEST_CHECK_SETTINGS);
                                    } catch (IntentSender.SendIntentException sendEx) {

                                    }
                                    break;
                                case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                                    break;
                            }
                        }
                    });
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            Log.i("LOCATION", "onSuccess location");
                            if (location != null) {
                                Log.i("LOCATION", "Latitud: " + location.getLatitude());
                                Log.i("LOCATION", "Longitud: " + location.getLongitude());
                                Log.i("LOCATION", "Elevacion: " + location.getAltitude());
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Funcionalidad limitada", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void showChefs() {
        database = FirebaseDatabase.getInstance();
        mDatabaseChefs = database.getReference("chefs");
        mDatabaseChefs.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.getChildrenCount() != 0)
                    for (DataSnapshot singleSnap : dataSnapshot.getChildren()) {
                        if (singleSnap != null) {
                            Chef aux = singleSnap.getValue(Chef.class);
                            if (aux.getEstado()) {
                                if (distance(aux.getDireccion().getLatitud(), aux.getDireccion().getLongitud(), latitude, longitude) <= 5.0) {
                                    //Toast.makeText(mapaDireccion.this, "nuevo chef", Toast.LENGTH_SHORT).show();
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(aux.getDireccion().getLatitud(), aux.getDireccion().getLongitud())).icon(BitmapDescriptorFactory.fromResource(R.drawable.remy)));
                                }
                            }
                        }
                    }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result * 100.0) / 100.0;
    }

    public void findAddress(String addressString, Geocoder mGeocoder) {

        if (!addressString.isEmpty()) {
            try {
                List<Address> addresses = mGeocoder.getFromLocationName(addressString, 2,
                        lowerLeftLatitude,
                        lowerLeftLongitude,
                        upperRightLatitude,
                        upperRigthLongitude
                );

                if (addresses != null && !addresses.isEmpty()) {
                    Address addressResult = addresses.get(0);
                    double latitudeDes = addressResult.getLatitude();
                    double longitudeDes = addressResult.getLongitude();

                    mainLatitude = addressResult.getLatitude();
                    mainLongitude = addressResult.getLongitude();
                    found = true;

                    LatLng position = new LatLng(latitudeDes, longitudeDes);

                    if (mMap != null) {
                        MarkerOptions myMarkerOptions = new MarkerOptions();
                        myMarkerOptions.position(position);
                        edTxtDir.getText().toString();
                        myMarkerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
                        mMap.addMarker(myMarkerOptions);

                        moveCamera(latitudeDes, longitudeDes, 15);

                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Toast.makeText(getApplicationContext(), "La dirección esta vacía", Toast.LENGTH_SHORT).show();
        }

    }

    private void moveCamera(double latitude, double longitude, int zoom) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(zoom)
                .build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    @Override
    protected void onResume() {
        super.onResume();
        startLocationUpdates();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }
}
