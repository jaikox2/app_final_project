package com.example.pang.spacedelivery.Customer;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
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
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
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

import java.util.ArrayList;

public class finishOrder extends FragmentActivity implements OnMapReadyCallback,DirectionCallback {

    int deliman_id;
    int orderID;

    TextView delimanName,numberDeliman;
    ImageView imageViewDeliman;

    private GoogleMap mMap;
    private static final int LOCATION_REQUEST = 500;

    private String serverKey = "AIzaSyAsJUz5DMHGUh08lrSt3n7XgAXc9eeR9yg";
    private LatLng origin ;
    private LatLng destination = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_order);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        delimanName = (TextView)findViewById(R.id.delimanName);
        numberDeliman = (TextView)findViewById(R.id.numberDeliman);
        imageViewDeliman = (ImageView)findViewById(R.id.imageViewDeliman);


        try {
            //get intent to get person id
            deliman_id = getIntent().getIntExtra("deliman_id",0);
            System.out.println("deliman_id = "+deliman_id);
            orderID = getIntent().getIntExtra("orderID",0);
            System.out.println(orderID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        android.widget.Toolbar toolbar = (android.widget.Toolbar) findViewById(R.id.my_toolbar);
        setActionBar(toolbar);
        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));

        //setTitle("เมื่ออาหารถึงเราจะติดต่อกลับไป....");
        setTitle(Html.fromHtml("<small>เมื่ออาหารถึงเราจะติดต่อกลับไป....</small>"));

        //back button
        getActionBar().setDisplayShowHomeEnabled(true);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        loadDeliman();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            startActivity(new Intent(getApplication(), MenuActivity.class));
        }
        return super.onOptionsItemSelected(item);
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
        loadMap();
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


    public  void loadDeliman(){
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDelimanUser() ;

        Ion.with(getApplication())
                .load(baseUrl+"getInfoDeliman.php")
                .setMultipartParameter("deliman_id", String.valueOf(deliman_id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        String name = result.get("name").getAsString();
                        String lastname = result.get("lastname").getAsString();
                        String number = result.get("number").getAsString();
                        String image = result.get("img").getAsString();

                        String fullname = name+" "+lastname;
                        delimanName.setText("ชื่อ "+fullname);
                        numberDeliman.setText("เบอร์ "+number);
                        Ion.with(getApplication())
                                .load(baseUrl+"upload-img/"+image)
                                .intoImageView(imageViewDeliman);


                    }
                });
    }

    public  void loadMap(){

        final String[] origin1 = new String[1];
        final String[] destination1 = new String[1];

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDelivery() ;

        Ion.with(getApplication())
                .load(baseUrl+"getLatLngOrder.php")
                .setMultipartParameter("order_id", String.valueOf(orderID))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        origin1[0] = result.get("origin").getAsString();
                        destination1[0] = result.get("destination").getAsString();

                        System.out.println("origin = "+origin1[0]+" destination = "+destination1[0]);
                        receive(origin1[0],destination1[0]);

                    }
                });

    }

    public void receive(String origin1,String destination1){

       /* final String baseUrl1 = ip.getBaseUrlDelivery() ;
        Ion.with(getApplication())
                .load(baseUrl1+"checkDeliMan.php")
                .setMultipartParameter("order_id", String.valueOf(orderID))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        destination1[0] = result.get("deli_add").getAsString();

                    }
                });*/

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

        destination = new LatLng(latitude1, longitude1);  //แปลง Double to LatLng

        MarkerOptions mCenterMarker;
        mCenterMarker = new MarkerOptions().position(origin).title("ร้านอาหาร")
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
}
