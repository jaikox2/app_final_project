package com.example.pang.spacedelivery.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.addToCart.Converter;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.interfaces.AddorRemoveCallbacks;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.ArrayList;

public class FoodInRestaurant extends AppCompatActivity implements AddorRemoveCallbacks {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private FoodInRestaurantAdapter adapter;

    private long res_id;
    private String res_name;
    private static int cart_count=0;

    SharedPreferences sharedpreferences;
    static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";

    private SwipeRefreshLayout refreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_in_restaurant);

        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getApplication());
        mRecyclerView.setLayoutManager(mLayoutManager);

        refreshLayout = findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        FloatingActionButton actionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent goToUpdate = new Intent(getBaseContext(), resInfomation.class);
                goToUpdate.putExtra("res_id", res_id);
                getApplication().startActivity(goToUpdate);
               // Toast.makeText(getApplicationContext(), "Restaurant Info", Toast.LENGTH_SHORT).show();
            }
        });

        try {
            //get intent to get res id
            res_id = getIntent().getLongExtra("res_id", 1);
            res_name = getIntent().getStringExtra("resName");
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(res_name);
        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                numCart();
                poultererView();
            }
        });
        numCart();
        poultererView();
    }

    private void poultererView(){

        final ArrayList<food> itemArray = new ArrayList<>();
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;

        String user_id = Long.toString(res_id);

        sharedpreferences = this.getApplication().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id1 = sharedpreferences.getString(User_id, "");

        Ion.with(getApplication())
                .load(baseUrl+"FoodRecycleView.php")
                .setMultipartParameter("Res_id",user_id)
                .setMultipartParameter("user_id",user_id1)
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject;
                        for(int i=0; i < result.size(); i++){
                            food item = new food();
                            jsonObject = (JsonObject)result.get(i);
                            item.setFoodId(jsonObject.get("id").getAsInt());
                            item.setFoodName(jsonObject.get("name").getAsString());
                            item.setFoodDetail(jsonObject.get("detail").getAsString());
                            item.setFoodPrice(jsonObject.get("price").getAsDouble());
                            item.setFoodStamp(jsonObject.get("stamp").getAsDouble());
                            item.setFoodImage(jsonObject.get("img").getAsString());
                            item.setCheckChoose(jsonObject.get("status").getAsString());
                            item.setBaseUrl(baseUrl);
                            itemArray.add(item);
                        }
                        adapter = new FoodInRestaurantAdapter(itemArray,FoodInRestaurant.this, mRecyclerView);
                        mRecyclerView.setAdapter(adapter);
                    }
                });
    }

    private void numCart(){

        ipConfig ip1 = new ipConfig();
        final String baseUrl1 = ip1.getBaseUrlCart() ;

        sharedpreferences = this.getApplication().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id1 = sharedpreferences.getString(User_id, "");

        Ion.with(getApplication())
                .load(baseUrl1+"numCart.php")
                .setMultipartParameter("user_id",user_id1)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        cart_count = result.get("Count").getAsInt();
                        invalidateOptionsMenu();
                    }
                });

        refreshLayout.setRefreshing(false);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        if (id == R.id.cart_action){
            startActivity(new Intent(getApplicationContext(), FoodInCart.class));
            //Toast.makeText(getApplicationContext(), "Cart page", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.cart_action);
        menuItem.setIcon(Converter.convertLayoutToImage(FoodInRestaurant.this,cart_count,R.drawable.ic_shopping_cart_white_24dp));
        return true;
    }

    @Override
    public void onAddProduct() {
        cart_count++;
        invalidateOptionsMenu();
        Snackbar.make((CoordinatorLayout)findViewById(R.id.coordinatorLayout), "Added to cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    @Override
    public void onRemoveProduct() {
        cart_count--;
        invalidateOptionsMenu();
        Snackbar.make((CoordinatorLayout)findViewById(R.id.coordinatorLayout), "Removed from cart !!", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }
}
