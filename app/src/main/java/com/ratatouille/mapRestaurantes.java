package com.ratatouille;

import androidx.annotation.NonNull;
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
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.List;

public class mapRestaurantes extends FragmentActivity implements OnMapReadyCallback, TaskLoadedCallback {

    static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;
    static final int REQUEST_CHECK_SETTINGS = 200;
    static final double RADIUS_OF_EARTH_KM = 6371.01;
    public static final double lowerLeftLatitude = 4.579131;
    public static final double lowerLeftLongitude = -74.250243;
    public static final double upperRightLatitude = 4.768837;
    public static final double upperRightLongitude = -74.022898;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Polyline currentPolyline;
    Marker customer, restaurant;
    double latitude;
    double longitude;
    double height;
    Button btn_menu;
    Button btnEscogerRestaurante;
    EditText txtPlace;
    Geocoder mGeocoder;
    Boolean choosen=false;
    Address dirRes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_restaurantes);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, "Para ver ubicación", MY_PERMISSIONS_REQUEST_LOCATION);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            txtPlace = findViewById(R.id.txtPlace);
            btn_menu = findViewById(R.id.btn_menu_rest);


            btnEscogerRestaurante = findViewById(R.id.buttonEcogerRestauranteReserva);
            mGeocoder = new Geocoder(mapRestaurantes.this);
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null) {
                                customer.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
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
                        if (location.getLatitude() != latitude || location.getLongitude() != longitude) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            LatLng pos = new LatLng(latitude, longitude);
                            customer.setPosition(pos);
                        }
                    }
                }
            };


            //-----------------------------------------------------------------------------------------------------------------------------
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(mLocationRequest);
            SettingsClient client = LocationServices.getSettingsClient(mapRestaurantes.this);
            Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

            task.addOnFailureListener(this, new OnFailureListener() {


                @Override
                public void onFailure(@NonNull Exception e) {

                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case CommonStatusCodes.RESOLUTION_REQUIRED:
                            try {

                                ResolvableApiException resolvable = (ResolvableApiException) e;
                                resolvable.startResolutionForResult(mapRestaurantes.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sendEx) {

                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
            //-----------------------------------------------------------------------------------------------------------------------------

            btnEscogerRestaurante.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choosen) {
                        Intent intent = new Intent(v.getContext(), DetallesReservaActivity.class);
                        intent.putExtra("direccion", dirRes.getFeatureName().toString());
                        intent.putExtra("latitud", dirRes.getLatitude());
                        intent.putExtra("longitud", dirRes.getLongitude());
                        intent.putExtra("nombreRes", txtPlace.getText().toString());
                        startActivity(intent);
                    }

                }
            });
            txtPlace.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        String dir = txtPlace.getText().toString();
                        if (!dir.isEmpty()) {
                            try {
                                List<Address> dirs = mGeocoder.getFromLocationName(dir, 2, lowerLeftLatitude, lowerLeftLongitude, upperRightLatitude, upperRightLongitude);
                                if (dirs != null && !dirs.isEmpty()) {
                                    dirRes = dirs.get(0);
                                    LatLng pos = new LatLng(dirRes.getLatitude(), dirRes.getLongitude());
                                    if (mMap != null) {
                                        restaurant.setPosition(pos);
                                        restaurant.setTitle(dirRes.getFeatureName());
                                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                        builder.include(customer.getPosition());
                                        builder.include(restaurant.getPosition());
                                        new FetchURL(mapRestaurantes.this).execute(getUrl(customer.getPosition(), restaurant.getPosition(), "driving"), "driving");
                                        LatLngBounds bounds = builder.build();
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 125));
                                        Toast.makeText(mapRestaurantes.this, "Distancia a " + txtPlace.getText().toString() + ": " + distance(customer.getPosition().latitude, customer.getPosition().longitude, restaurant.getPosition().latitude, customer.getPosition().longitude) + "Km.", Toast.LENGTH_LONG).show();
                                        choosen = true;
                                    }
                                } else {
                                    Toast.makeText(mapRestaurantes.this, "Dirección no encontrada", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(mapRestaurantes.this, "La dirección está vacía", Toast.LENGTH_SHORT).show();
                        }
                    }
                    return false;
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
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
        mMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        customer = mMap.addMarker(new MarkerOptions().position(new LatLng(4, -72)).icon(BitmapDescriptorFactory.fromResource(R.drawable.anton)).title("Tu ubicación")); //TODO: add snippet with name
        restaurant = mMap.addMarker(new MarkerOptions().position(new LatLng(4, -72)).icon(BitmapDescriptorFactory.fromResource(R.drawable.restaurant)).title("Restaurante"));
    }

    private void requestPermission(Activity context, String permiso, String justificacion, int idCode) {
        if (ContextCompat.checkSelfPermission(context, permiso) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
                Toast.makeText(context, justificacion, Toast.LENGTH_LONG).show();
            }
            ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, idCode);
        }
    }


    private void startLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }


    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(2000);
        mLocationRequest.setFastestInterval(1800);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
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

    public double distance(double lat1, double long1, double lat2, double long2) {
        double latDistance = Math.toRadians(lat1 - lat2);
        double lngDistance = Math.toRadians(long1 - long2);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double result = RADIUS_OF_EARTH_KM * c;
        return Math.round(result * 100.0) / 100.0;
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
                                        resolvable.startResolutionForResult(mapRestaurantes.this, REQUEST_CHECK_SETTINGS);
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
                                height = location.getAltitude();
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Funcionalidad limitada", Toast.LENGTH_LONG).show();
                }
            }
        }
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

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = mMap.addPolyline((PolylineOptions) values[0]);
        currentPolyline.setColor(0xFFF44336);
    }
}
