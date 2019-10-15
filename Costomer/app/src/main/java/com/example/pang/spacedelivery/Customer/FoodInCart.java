package com.example.pang.spacedelivery.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class FoodInCart extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FoodInCartAdapter adapter;
    private Button allPrice;

    SharedPreferences sharedpreferences;
    static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";

    Double totalPrice = 0.0 , Price = 0.0;

    private int resID,CartAmount =0;

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_in_cart);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);

        refreshLayout = findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        allPrice = (Button)findViewById(R.id.totalPrice);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("ตะกร้าเลือกเมนู");

        allPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CartAmount == 0){
                    Toast.makeText(getApplication(), "คุณยังไม่ได้เลือกรายการอาหาร", Toast.LENGTH_SHORT).show();
                }else {
                    Intent goToUpdate = new Intent(FoodInCart.this, deliveryInFo.class);
                    goToUpdate.putExtra("resID", resID);
                    goToUpdate.putExtra("cartAmont", CartAmount);
                    goToUpdate.putExtra("totalPrice", totalPrice);
                    startActivity(goToUpdate);
                }
            }
        });


        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                poultererView();
            }
        });
        poultererView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    public void poultererView(){
        sharedpreferences = this.getApplication().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");

        final ArrayList<food> itemArray = new ArrayList<>();
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCart() ;
        CartAmount = 0;
        totalPrice = 0.0;
        Ion.with(getApplication())
                .load(baseUrl+"FoodInCart.php")
                .setMultipartParameter("user_id",user_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            food item = new food();
                            jsonObject = (JsonObject)result.get(i);
                            item.setFoodId(jsonObject.get("foodid").getAsInt());
                            item.setFoodName(jsonObject.get("foodName").getAsString());
                            CartAmount = CartAmount + jsonObject.get("cart_amount").getAsInt();
                            Price = jsonObject.get("cart_amount").getAsInt() * jsonObject.get("foodPrice").getAsDouble();
                            totalPrice = totalPrice + Price;
                            item.setFoodPrice(jsonObject.get("foodPrice").getAsDouble());
                            item.setFoodImage(jsonObject.get("foodImg").getAsString());
                            item.setResName(jsonObject.get("ResName").getAsString());
                             resID = jsonObject.get("Resid").getAsInt();
                            item.setCartAmount(jsonObject.get("cart_amount").getAsInt());
                            item.setCart_id(jsonObject.get("c_id").getAsInt());
                            item.setBaseUrl(baseUrl);
                            itemArray.add(item);
                        }
                        allPrice.setText("จำนวน "+CartAmount+" สรุปรายการ "+totalPrice+" บาท");
                        adapter = new FoodInCartAdapter(itemArray,FoodInCart.this, mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
        refreshLayout.setRefreshing(false);
    }
}
