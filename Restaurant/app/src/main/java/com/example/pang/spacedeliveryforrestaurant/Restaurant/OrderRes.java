package com.example.pang.spacedeliveryforrestaurant.Restaurant;

import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.pang.spacedeliveryforrestaurant.InfoNotification;
import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class OrderRes extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrderResAdapter adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;

    private int receivedCart_Id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_res);


        try {
            //get intent to get person id
            receivedCart_Id = getIntent().getIntExtra("cart_id", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        setTitle("รายการสั่ง");

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);

        poultererView();

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void poultererView(){

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");
        final ArrayList<InfoNotification> itemArray = new ArrayList<>();
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCart() ;

        System.out.println(baseUrl+"NotiRecycleView.php");

        Ion.with(getApplicationContext())
                .load(baseUrl+"foodOrderRes.php")
                .setMultipartParameter("cart_id", String.valueOf(receivedCart_Id))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            InfoNotification item = new InfoNotification();
                            jsonObject = (JsonObject)result.get(i);
                            item.setFoodName(jsonObject.get("food_name").getAsString());
                            item.setFoodImg(jsonObject.get("food_img").getAsString());
                            item.setQuantity(jsonObject.get("food_quantity").getAsInt());
                            itemArray.add(item);
                        }
                        adapter = new OrderResAdapter(itemArray,getApplication(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });


    }
}
