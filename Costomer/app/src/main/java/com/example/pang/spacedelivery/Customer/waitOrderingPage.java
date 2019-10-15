package com.example.pang.spacedelivery.Customer;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.pang.spacedelivery.InfoNotification;
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class waitOrderingPage extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private waitOrderingPageAdapter adapter;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_ordering_page);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);

        poultererView();

        setTitle("รายการอาหารที่กำลังสั่ง");
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
        final String baseUrl = ip.getBaseUrlOrder() ;

        System.out.println(baseUrl+"CusOrderingRecycleView.php");

        Ion.with(getApplication())
                .load(baseUrl+"CusOrderingRecycleView.php")
                .setMultipartParameter("user_id",user_id)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            InfoNotification item = new InfoNotification();
                            jsonObject = (JsonObject)result.get(i);
                            item.setOrderId(jsonObject.get("order_id").getAsInt());
                            item.setChooseId(jsonObject.get("cart_id").getAsInt());
                            item.setTime(jsonObject.get("order_time").getAsString());
                            item.setDate(jsonObject.get("order_date").getAsString());
                            itemArray.add(item);
                        }
                        adapter = new waitOrderingPageAdapter(itemArray,getApplication(), mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
    }
}
