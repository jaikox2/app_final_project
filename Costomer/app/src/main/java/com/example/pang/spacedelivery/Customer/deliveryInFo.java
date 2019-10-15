package com.example.pang.spacedelivery.Customer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.ipConfig;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class deliveryInFo extends AppCompatActivity {

    EditText LocateDetail,nameReceve,numReceve;
    TextView Restaurant,mapLocate,vahicle;
    Button btnDeliInfo,btnCost;

    private int resID = 0,CartAmount;
    private Double totalPrice;
    private String Resname,ResAddMap;
    private float Distance = 0;
    private String DesMapAdd;

    private String nameOrder,detailPlaceOrder = "ไม่มี",numberOrder,mapOder,vahicleOder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_in_fo);

        mapLocate = (TextView) findViewById(R.id.mapLocate);
        LocateDetail = (EditText) findViewById(R.id.deltailPlace);
        nameReceve = (EditText) findViewById(R.id.recepName);
        numReceve = (EditText) findViewById(R.id.recepNumber);
        vahicle = (TextView) findViewById(R.id.vahicle);

        Restaurant = (TextView) findViewById(R.id.textViewRestaurant);

        btnDeliInfo = (Button)findViewById(R.id.buttonDeliInfo);
        btnCost = (Button)findViewById(R.id.buttonCostInfo);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("สถานที่จัดส่ง");

        try {
            //get intent to get person id
            resID = getIntent().getIntExtra("resID",0);
            CartAmount = getIntent().getIntExtra("cartAmont",0);
            totalPrice = getIntent().getDoubleExtra("totalPrice",0);
            System.out.println("totalPrice"+totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlRes() ;

        Ion.with(getApplication())
                .load(baseUrl+"profileRes.php")
                .setMultipartParameter("Res_id", String.valueOf(resID))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Resname = result.get("resname").getAsString();
                        ResAddMap = result.get("addmap").getAsString();
                        Restaurant.setText(Resname);
                    }
                });


        mapLocate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(ResAddMap);
                Intent goToUpdate = new Intent(deliveryInFo.this, getResLocationsMapsActivity.class);
                goToUpdate.putExtra("resID",resID);
                goToUpdate.putExtra("resMapAdd", ResAddMap);
                goToUpdate.putExtra("resName", Resname);
                goToUpdate.putExtra("cartAmont", CartAmount);
                goToUpdate.putExtra("totalPrice", totalPrice);
                startActivity(goToUpdate);
            }
        });

        try {
            //get intent to get person id
            DesMapAdd = getIntent().getStringExtra("DesMapAdd");
            Distance = getIntent().getFloatExtra("Distance",0);
            mapLocate.setText(DesMapAdd);
        } catch (Exception e) {
            e.printStackTrace();
        }
        btnCost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //System.out.println("นโยบายค่าบริการ");
                Intent go = new Intent(deliveryInFo.this, ServicePolicy.class);
                startActivity(go);
            }
        });

        btnDeliInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                senddata();
            }
        });

        if(CartAmount >= 20){
            vahicle.setText("ตุกตุ๊ก");
        }else {
            vahicle.setText("มอไซต์");
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

    public void senddata(){
        nameOrder = nameReceve.getText().toString();
        detailPlaceOrder = LocateDetail.getText().toString();
        numberOrder = numReceve.getText().toString();
        mapOder = mapLocate.getText().toString();
        vahicleOder = vahicle.getText().toString();

        if(nameOrder.isEmpty()){
            Toast.makeText(getApplication(), "กรุณากรอกชื่อผู้รับ", Toast.LENGTH_LONG).show();
        }else if(numberOrder.isEmpty()){
            Toast.makeText(getApplication(), "กรุณากรอกเบอร์ผู้รับ", Toast.LENGTH_LONG).show();
        }else if(mapOder.isEmpty()){
            Toast.makeText(getApplication(), "กรุณาเลือกที่อยู่ผู้รับ", Toast.LENGTH_LONG).show();
        }else {
            Intent goTo = new Intent(deliveryInFo.this, ordering.class);
            goTo.putExtra("resID", resID);
            goTo.putExtra("nameOrder", nameOrder);
            goTo.putExtra("detailPlaceOrder", detailPlaceOrder);
            goTo.putExtra("numberOrder", numberOrder);
            goTo.putExtra("mapOrder", mapOder);
            goTo.putExtra("Distance", Distance);
            goTo.putExtra("vahicleOrder", vahicleOder);
            goTo.putExtra("totalPrice", totalPrice);
            startActivity(goTo);
        }
    }
}
