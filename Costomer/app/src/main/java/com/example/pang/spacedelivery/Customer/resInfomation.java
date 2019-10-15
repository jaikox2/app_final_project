package com.example.pang.spacedelivery.Customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class resInfomation extends AppCompatActivity {

    ImageView profileIMG;
    TextView res_name,res_address,phone;
    private String resname,resAddress,phone1,img;
    private long res_id;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_res_infomation);

        res_name = (TextView) findViewById(R.id.res_name);
        res_address = (TextView) findViewById(R.id.res_address);
        phone = (TextView) findViewById(R.id.res_phone);
        profileIMG = (ImageView) findViewById(R.id.profile_image);

        setTitle("ข้อมูลร้านอาหาร");

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            //get intent to get person id
            res_id = getIntent().getLongExtra("res_id", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlRes() ;

        refreshLayout = findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        final String user_id = Long.toString(res_id);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Ion.with(getApplication())
                        .load(baseUrl + "profileRes.php")
                        .setMultipartParameter("Res_id",user_id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                resname = result.get("resname").getAsString();
                                resAddress = result.get("address").getAsString();
                                phone1 = result.get("phone").getAsString();
                                img = result.get("img").getAsString();

                                res_name.setText(resname);
                                res_address.setText(resAddress);
                                phone.setText(phone1);
                                Glide.with(getBaseContext())
                                        .load(baseUrl+ "upload-proimg/"+img)
                                        .into(profileIMG);

                                refreshLayout.setRefreshing(false);
                            }
                        });
            }
        });


        Ion.with(getApplication())
                .load(baseUrl + "profileRes.php")
                .setMultipartParameter("Res_id",user_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        resname = result.get("resname").getAsString();
                        resAddress = result.get("address").getAsString();
                        phone1 = result.get("phone").getAsString();
                        img = result.get("img").getAsString();

                        res_name.setText(resname);
                        res_address.setText(resAddress);
                        phone.setText(phone1);
                        Glide.with(getBaseContext())
                                .load(baseUrl+ "upload-proimg/"+img)
                                .into(profileIMG);
                    }
                });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
