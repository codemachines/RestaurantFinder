package com.example.jaydave_sanchitshah_comp304_;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String provider;
    protected String latitude, longitude;
    FusedLocationProviderClient fusedLocationProviderClient;
    List<Address> addresses;
    //double logg;
    //double lat;

    ToggleButton toggle;
    Button btn_search;
    ImageButton btn_current_loc;
    String addr;
    double res_latitude;
    double res_longitude;
    Address add;


    //LocationTrack locationTrack;

    SharedPreferences sharedpreferences;
    public static final String MyPREFERENCES = "MyPrefs" ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        toggle=findViewById(R.id.toggle);
        addr=getIntent().getStringExtra("add");



        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btn_current_loc=findViewById(R.id.btn_current_loc);
        btn_current_loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLocation();
            }
        });
        btn_search=findViewById(R.id.btn_search);
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MapsActivity.this,MainActivity.class);
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
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        // locationTrack = new LocationTrack(MainActivity.this);
        if (toggle.isChecked()){
            toggle.setText("Map");
            mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        }else {
            toggle.setText("Satellite");
            mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle.isChecked()){
                    toggle.setText("Map");
                    mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                }else {
                    toggle.setText("Satellite");
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (addr!=null){
                getAddress();
            }else {
                getLocation();
            }

        } else {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }


    }

    private void getLocation() {
        // if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);

        }else {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        Geocoder geocoder = new Geocoder(MapsActivity.this, Locale.CANADA);
                        try {
                            if (toggle.isChecked()){
                                toggle.setText("Map");
                                mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                            }else {
                                toggle.setText("Satellite");
                                mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                            }

                            toggle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (toggle.isChecked()){
                                        toggle.setText("Map");
                                        mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                                    }else {
                                        toggle.setText("Satellite");
                                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                                    }
                                }
                            });

                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            Toast.makeText(MapsActivity.this, "" + addresses.get(0).getLatitude()+" "+addresses.get(0).getLongitude(), Toast.LENGTH_SHORT).show();
                            LatLng canada = new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            editor.putString("latitude",""+addresses.get(0).getLatitude());
                            editor.putString("longitude",""+addresses.get(0).getLongitude());
                            editor.commit();
                            mMap.addMarker(new MarkerOptions().position(canada).title("You are Here"));
                            mMap.setTrafficEnabled(true);
                            CameraPosition cameraPosition = new CameraPosition.Builder()
                                    .target(canada)
                                    .zoom(17).build();
                            mMap.moveCamera(CameraUpdateFactory.newLatLng(canada));
                            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                            mMap.setMaxZoomPreference(100);

                            mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                                @Override
                                public void onMapClick(LatLng latLng) {
                                    Toast.makeText(MapsActivity.this, "Map CLicked", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }

            });

            return;
        }

          //  return;
        //}

    }

    private void getAddress()  {

        Geocoder coder = new Geocoder(this);


        if (toggle.getText().equals("Satellite")){
            mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            toggle.setText("Map");
        }else{
            toggle.setText("Satellite");
        }
        toggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toggle.isChecked()){
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                    toggle.setText("Map");
                }else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    toggle.setText("Satellite");
                }
            }
        });
        //List<Address> addressList = coder.getFromLocationName(addr, 5);
        //if (addressList != null && addressList.size() > 0) {
        //double lat = addressList.get(0).getLatitude();
        //double lng = addressList.get(0).getLongitude();
        res_latitude=Double.parseDouble(getIntent().getStringExtra("lat"));
        res_longitude=Double.parseDouble(getIntent().getStringExtra("lng"));
        final String res_vicinity=getIntent().getStringExtra("vicinity");
        LatLng res = new LatLng(res_latitude, res_longitude);
        mMap.addMarker(new MarkerOptions().position(res).title(addr+", \n"+res_vicinity));
        Circle circle = mMap.addCircle(new CircleOptions()
                .center(new LatLng(res_latitude, res_longitude))
                .radius(150)
                .fillColor(0x220000FF)
                .strokeColor(Color.parseColor("#2271cce7"))
                .strokeWidth(5));
        mMap.setTrafficEnabled(true);
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(res)
                .zoom(17).build();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(res));
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        mMap.setMaxZoomPreference(100);
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                Toast.makeText(MapsActivity.this, "Hello your address is: "+res_vicinity+" at: "+res_latitude+" & "+res_longitude, Toast.LENGTH_SHORT).show();

              /*  Intent intent=new Intent(MapsActivity.this,MessageLayout.class);

                startActivity(intent);*/
                AlertDialog.Builder builder=new AlertDialog.Builder(MapsActivity.this);

                builder.setTitle("Do you want to share this restaurants location?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        Toast.makeText(MapsActivity.this, "Hello your address is: "+res_vicinity+" at: "+res_latitude+" & "+res_longitude, Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(MapsActivity.this,MessageLayout.class);

                        startActivity(intent);

                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                //AlertDialog dialog=builder.create();
                //dialog.show();
                builder.create();
               builder.show();
               // Toast.makeText(MapsActivity.this, "Hello your address is: "+res_vicinity+" at: "+res_latitude+" & "+res_longitude, Toast.LENGTH_SHORT).show();
            }
        });
        //mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //}
    }

}