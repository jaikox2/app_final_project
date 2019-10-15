package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

public class deliInfo extends AppCompatActivity {

    private SwipeRefreshLayout refreshLayout;

    ImageView profile_res,profile_dei;
    TextView name,address,phone;
    private String name1,addreess1,phone1;
    TextView name_deli,address_deli,phone_deli;
    private String name1_deli,surname1_deli,addreess1_deli = "ไม่มี",phone1_deli;
    private String res_img,deli_img;

    private int deli_id,order_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deli_info);

        profile_res = (ImageView)findViewById(R.id.profile_res);
        name = (TextView)findViewById(R.id.res_name);
        address = (TextView)findViewById(R.id.res_address);
        phone = (TextView)findViewById(R.id.res_phone);

        profile_dei = (ImageView)findViewById(R.id.profile_deli);
        name_deli = (TextView)findViewById(R.id.deli_name);
        address_deli = (TextView)findViewById(R.id.deli_address);
        phone_deli = (TextView)findViewById(R.id.deli_phone);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("ข้อมูลการส่งอาหาร");

        try {
            //get intent to get person id
            deli_id = getIntent().getIntExtra("deli_id",0);
            order_id = getIntent().getIntExtra("order_id",0);
            System.out.println("deli_id "+deli_id+" order_id "+order_id);
        } catch (Exception e) {
            e.printStackTrace();
        }

        refreshLayout = findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDelivery() ;

        ipConfig ip1 = new ipConfig();
        final String baseUrl1 = ip1.getBaseUrlRes() ;

        ipConfig ip2 = new ipConfig();
        final String baseUrl2 = ip2.getBaseUrlCustomer() ;

        Ion.with(this)
                .load(baseUrl + "deliInfo.php")
                .setMultipartParameter("deli_id", String.valueOf(deli_id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        name1_deli = result.get("deli_name").getAsString();
                        addreess1_deli = result.get("deli_address").getAsString();
                        phone1_deli = result.get("deli_phone").getAsString();
                        deli_img = result.get("deli_img").getAsString();

                        name1 = result.get("res_name").getAsString();
                        addreess1 = result.get("res_address").getAsString();
                        phone1 = result.get("res_phone").getAsString();
                        res_img = result.get("res_img").getAsString();

                        name.setText("ชื่อ "+name1);
                        address.setText("ที่อยู่ "+addreess1);
                        phone.setText("เบอร์ "+phone1);

                        name_deli.setText("ชื่อ "+name1_deli);
                        address_deli.setText("รายละเอียดที่อยู่ "+addreess1_deli);
                        phone_deli.setText("เบอร์ "+phone1_deli);

                        Glide.with(getApplication())
                                .load(baseUrl2+ "upload-proimg/"+deli_img)
                                .into(profile_dei);

                        Glide.with(getApplication())
                                .load(baseUrl1+ "upload-proimg/"+res_img)
                                .into(profile_res);
                    }
                });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
