package com.example.pang.spacedelivery.Customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.pang.spacedelivery.MainActivity;
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;


public class getResLocationsMapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleMap.OnMapClickListener,DirectionCallback {

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;
    public Button btnmap;
    public double lat=0.0,lng=0.0;

    private String serverKey = "AIzaSyAsJUz5DMHGUh08lrSt3n7XgAXc9eeR9yg";
    private String resMapAdd,resName;
    private int resID,CartAmount;
    private LatLng origin ;
    private LatLng destination = null;
    private float distancePerKm = (float) 0.0;
    private Double totalPrice;


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
                sendBack();
            }
        });

        android.widget.Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //back button
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);
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
        receive();
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
            destination = latLng;
            receive();
            float result[] = new float[10];
            Location.distanceBetween(origin.latitude,origin.longitude,destination.latitude,destination.longitude,result);

            distancePerKm = result[0] / 1000;

            mMap.addMarker(new MarkerOptions()
                    .position(latLng)
                    .title("I'm here")
                    .snippet("ระยะทาง "+distancePerKm+" กม.")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW)));
            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    //Do something after 100ms
                    requestDirection();
                }
            }, 1000);
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

    public void receive(){

        try {
            //get intent to get person id
            resID = getIntent().getIntExtra("resID",0);
            resMapAdd = getIntent().getStringExtra("resMapAdd");
            resName = getIntent().getStringExtra("resName");
            CartAmount = getIntent().getIntExtra("cartAmont",0);
            totalPrice = getIntent().getDoubleExtra("totalPrice",0);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] split = resMapAdd.split(",");
        StringBuilder sb = new StringBuilder();
        StringBuilder sd = new StringBuilder();
            sb.append(split[0]);
            sd.append(split[1]);
        String lat = sb.toString();                  //แปลง String to double
        String lng = sd.toString();
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lng);

        origin = new LatLng(latitude, longitude);  //แปลง Double to LatLng

        MarkerOptions mCenterMarker;
        mCenterMarker = new MarkerOptions().position(origin).title("ร้าน "+resName)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));  //สร้าง marker
        mMap.addMarker(mCenterMarker);

        if(destination == null){
            mMap.moveCamera(CameraUpdateFactory.newLatLng(origin)); //ย้าย camera
            mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }


        //Toast.makeText(getApplication(), "Lat = "+lat+" Lng = "+lng, Toast.LENGTH_SHORT).show();

        //Toast.makeText(getApplication(), "Lat,Lng = "+resMapAdd, Toast.LENGTH_SHORT).show();
    }

    public void requestDirection() {
        //Snackbar.make(btnRequestDirection, "Direction Requesting...", Snackbar.LENGTH_SHORT).show();
        GoogleDirection.withServerKey(serverKey)
                .from(origin)
                .to(destination)
                .unit(Unit.METRIC)
                .transportMode(TransportMode.DRIVING)
                .execute(this);
    }

    @Override
    public void onDirectionSuccess(Direction direction, String rawBody) {
        // Snackbar.make(btnRequestDirection, "Success with status : " + direction.getStatus(), Snackbar.LENGTH_SHORT).show();
       //Toast.makeText(getApplication(), "Success with status : " + direction.getStatus(), Toast.LENGTH_SHORT).show();
        if (direction.isOK()) {
            Route route = direction.getRouteList().get(0);
            ArrayList<LatLng> directionPositionList = route.getLegList().get(0).getDirectionPoint();
            mMap.addPolyline(DirectionConverter.createPolyline(this, directionPositionList, 5, Color.RED));
            setCameraWithCoordinationBounds(route);
        } else {
            // Snackbar.make(btnRequestDirection, direction.getStatus(), Snackbar.LENGTH_SHORT).show();
            Toast.makeText(getApplication(), direction.getStatus(), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDirectionFailure(Throwable t) {
        //Snackbar.make(btnRequestDirection, t.getMessage(), Snackbar.LENGTH_SHORT).show();
        Toast.makeText(getApplication(), t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void setCameraWithCoordinationBounds(Route route) {
        LatLng southwest = route.getBound().getSouthwestCoordination().getCoordination();
        LatLng northeast = route.getBound().getNortheastCoordination().getCoordination();
        LatLngBounds bounds = new LatLngBounds(southwest, northeast);
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
    }

    private void sendBack(){

        if(destination == null){
            Toast.makeText(getApplication(), "กรุณาเลือกตำแหน่งส่งของคุณ", Toast.LENGTH_SHORT).show();
        }else {
            double lat = destination.latitude;
            double lng = destination.longitude;
            String address = lat + "," + lng;

            if (distancePerKm >= 40.0) {
                Toast.makeText(getApplication(), "ระยะทางการส่งไม่เกิน 40 กม.", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(getResLocationsMapsActivity.this, deliveryInFo.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("resID", resID);
                intent.putExtra("DesMapAdd", address);
                intent.putExtra("Distance", distancePerKm);
                intent.putExtra("cartAmont", CartAmount);
                intent.putExtra("totalPrice", totalPrice);
                startActivity(intent);
            }
        }

    }
}
