package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.view.menu.MenuBuilder;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.googledirection.DirectionCallback;
import com.akexorcist.googledirection.GoogleDirection;
import com.akexorcist.googledirection.constant.TransportMode;
import com.akexorcist.googledirection.constant.Unit;
import com.akexorcist.googledirection.model.Direction;
import com.akexorcist.googledirection.model.Route;
import com.akexorcist.googledirection.util.DirectionConverter;
import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.ipConfig;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class getLocationsMapsMydeli extends FragmentActivity implements OnMapReadyCallback,DirectionCallback {

    int deli_id,order_id;
    String res_name,res_location,deli_location;
    TextView res_point,send_point;


    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;

    private String serverKey = "AIzaSyAsJUz5DMHGUh08lrSt3n7XgAXc9eeR9yg";
    private LatLng origin ;
    private LatLng destination = null;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_locations_maps_mydeli);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        res_point = (TextView)findViewById(R.id.res_name);
        send_point = (TextView)findViewById(R.id.send_name);


        try {
            //get intent to get person id
            deli_id = getIntent().getIntExtra("deli_id",0);
            order_id = getIntent().getIntExtra("order_id",0);
            res_name = getIntent().getStringExtra("res_name");
            res_location = getIntent().getStringExtra("res_location");
            deli_location = getIntent().getStringExtra("deli_location");
            System.out.println("deli_id "+deli_id+" order_id "+order_id+" res_name "+res_name+" res_location "+res_location+" deli_location "+deli_location);
        } catch (Exception e) {
            e.printStackTrace();
        }

        res_point.setText("จุดรับ "+res_name);

        android.widget.Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        toolbar.inflateMenu(R.menu.visit_menu);

        setTitle("แผนที่");
        //setTitle(Html.fromHtml("<small>เมื่ออาหารถึงเราจะติดต่อกลับไป....</small>"));

        //back button
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        new AlertDialog.Builder(getLocationsMapsMydeli.this)
                .setTitle("รายการส่ง")
                .setMessage("ข้อมูลการส่งของคุณ")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.visit_menu,menu);
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }else if (id == R.id.visible){
            System.out.println("info page");
            deliInfo();
        }
        return super.onOptionsItemSelected(item);
    }

    public void deliInfo(){
        Intent goToUpdate = new Intent(getLocationsMapsMydeli.this, deliInfo.class);
        goToUpdate.putExtra("deli_id", deli_id);
        goToUpdate.putExtra("order_id", order_id);
        startActivity(goToUpdate);

    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(this)
                .setTitle("กลับไปยังหน้าหลัก")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplication(), MenuActivity.class));
                    }
                })
                .setNegativeButton("ไม่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                .show();

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
        receive(res_location,deli_location);
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

    public void receive(String origin1,String destination1){

        System.out.println("origin "+origin1);

        String[] split = origin1.split(",");
        StringBuilder sb = new StringBuilder();
        StringBuilder sd = new StringBuilder();
        sb.append(split[0]);
        sd.append(split[1]);
        String lat = sb.toString();                  //แปลง String to double
        String lng = sd.toString();
        double latitude = Double.parseDouble(lat);
        double longitude = Double.parseDouble(lng);

        origin = new LatLng(latitude, longitude);  //แปลง Double to LatLng

        String[] split1 = destination1.split(",");
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sd1 = new StringBuilder();
        sb1.append(split1[0]);
        sd1.append(split1[1]);
        String lat1 = sb1.toString();                  //แปลง String to double
        String lng1 = sd1.toString();
        double latitude1 = Double.parseDouble(lat1);
        double longitude1 = Double.parseDouble(lng1);
        getAddress(latitude1,longitude1);

        destination = new LatLng(latitude1, longitude1);  //แปลง Double to LatLng

        MarkerOptions mCenterMarker;
        mCenterMarker = new MarkerOptions().position(origin).title("ร้าน"+res_name)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));  //สร้าง marker
        mMap.addMarker(mCenterMarker);

        MarkerOptions mCenterMarker1;
        mCenterMarker1 = new MarkerOptions().position(destination).title("ที่อยู่จะส่งไป")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));  //สร้าง marker
        mMap.addMarker(mCenterMarker1);

        mMap.moveCamera(CameraUpdateFactory.newLatLng(destination)); //ย้าย camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(13));

        requestDirection();

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

    public void getAddress(double lat, double lng) {
        Geocoder geocoder = new Geocoder(getLocationsMapsMydeli.this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            final Address obj = addresses.get(0);
            String   add = obj.getAddressLine(0);
            String  currentAddress = obj.getSubAdminArea() + ","
                    + obj.getAdminArea();
            double   latitude = obj.getLatitude();
            double longitude = obj.getLongitude();
            String currentCity= obj.getSubAdminArea();
            String currentState= obj.getAdminArea();
            add = add + "\n" + obj.getCountryName();
            add = add + "\n" + obj.getCountryCode();
            add = add + "\n" + obj.getAdminArea();
            add = add + "\n" + obj.getPostalCode();
            add = add + "\n" + obj.getSubAdminArea();
            add = add + "\n" + obj.getLocality();
            add = add + "\n" + obj.getSubThoroughfare();


            System.out.println("obj.getCountryName()"+obj.getCountryName());
            System.out.println("obj.getCountryCode()"+obj.getCountryCode());
            System.out.println("obj.getAdminArea()"+obj.getAdminArea());
            System.out.println("obj.getPostalCode()"+obj.getPostalCode());
            System.out.println("obj.getSubAdminArea()"+obj.getSubAdminArea());
            System.out.println("obj.getLocality()"+obj.getLocality());
            System.out.println("obj.getSubThoroughfare()"+obj.getSubThoroughfare());
            System.out.println("obj.getSubLocality()"+obj.getSubLocality());
            System.out.println("obj.getThoroughfare()"+obj.getThoroughfare());

            send_point.setText("จุดส่ง "+obj.getThoroughfare()+","+obj.getAdminArea()+","+obj.getCountryCode());
            send_point.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    new AlertDialog.Builder(getLocationsMapsMydeli.this)
                            .setTitle("จุดส่ง")
                            .setMessage(""+obj.getThoroughfare()+","+obj.getAdminArea()+","+obj.getCountryCode())
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                            .show();
                }
            });


            Log.v("IGA", "Address" + add);
            // Toast.makeText(this, "Address=>" + add,
            // Toast.LENGTH_SHORT).show();

            // TennisAppActivity.showDialog(add);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
