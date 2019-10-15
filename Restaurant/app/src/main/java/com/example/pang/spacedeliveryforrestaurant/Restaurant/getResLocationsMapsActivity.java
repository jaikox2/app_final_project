package com.example.pang.spacedeliveryforrestaurant.Restaurant;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pang.spacedeliveryforrestaurant.MainActivity;
import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class getResLocationsMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    ArrayList<LatLng> listPoints;
    public Button btnmap;
    public double lat=0.0,lng=0.0;

    private String name,surname,phone,email,pass,resName,resAdd,resMapAdd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_res_locations_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        btnmap = (Button) findViewById(R.id.btnmap);

        btnmap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        android.widget.Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        setTitle("ตำแหน่งร้านอาหาร");


        //back button
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        listPoints = new ArrayList<>();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapClickListener(this);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case LOCATION_REQUEST:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mMap.setMyLocationEnabled(true);
                }
                break;
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if(mMap != null){
            mMap.clear();
            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("I'm here")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
            lat = latLng.latitude;
            lng = latLng.longitude;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void Register(){

        String latS = Double.toString(lat);
        String lngS = Double.toString(lng);

        resMapAdd = latS+","+lngS;

        try {
            //get intent to get person id
            name = getIntent().getStringExtra("name");
            surname = getIntent().getStringExtra("surname");
            phone = getIntent().getStringExtra("phone");
            email = getIntent().getStringExtra("email");
            pass = getIntent().getStringExtra("pass");
            resName = getIntent().getStringExtra("resName");
            resAdd = getIntent().getStringExtra("resAdd");
        } catch (Exception e) {
            e.printStackTrace();
        }

       // Toast.makeText(getApplication(), "Lat,Lng = "+resMapAdd, Toast.LENGTH_SHORT).show();

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlRes() ;

        if(resMapAdd.equals("0.0,0.0")){
            Toast.makeText(getApplication(), "เลือกตำแหน่งร้านอาหารของคุณ", Toast.LENGTH_SHORT).show();
        }else {
            Ion.with(getApplication())
                    .load(baseUrl + "ResRegister.php")
                    .setMultipartParameter("name", name)
                    .setMultipartParameter("surname", surname)
                    .setMultipartParameter("phone", phone)
                    .setMultipartParameter("email", email)
                    .setMultipartParameter("pass", pass)
                    .setMultipartParameter("resName", resName)
                    .setMultipartParameter("resAdd", resAdd)
                    .setMultipartParameter("resMapAdd", resMapAdd)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String re = result.get("res").getAsString();
                            String res = result.get("status").getAsString();
                            if (res.endsWith("#1")) {
                                startActivity(new Intent(getBaseContext(), MainActivity.class));
                                Toast.makeText(getBaseContext(), re, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getBaseContext(), re, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

}
