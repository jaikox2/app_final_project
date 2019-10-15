package com.example.pang.spacedelivery.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DecimalFormat;

public class ordering extends AppCompatActivity {

    private int resID;
    private String nameOrder,detailPlaceOrder,numberOrder,vahicleOder,mapOder;
    private float Distance,allDistance;
    private Double totalPrice;
    float creditDB1,sumCost;

    TextView deliCostDistance,allDeliCost,deliCostLow,foodCost,foodList,creditHas,allCost,credit;
    Button payBTN;

    private int CartAmount = 0,num = 0,cart_id ;
    float allDistanceCost = 0;
    float credithas = 0;
    public int orderID;

    SharedPreferences sharedpreferences;
    static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";
    private String user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ordering);

        allCost = (TextView)findViewById(R.id.allCost);  //ค่าทั้งหมด
        credit = (TextView)findViewById(R.id.credit);     //creditที่มี
        creditHas = (TextView)findViewById(R.id.creditHas); //credit คงเหลือ

        foodCost = (TextView)findViewById(R.id.foodCost);  // ค่าอาหารทั้งหมด
        foodList = (TextView)findViewById(R.id.foodList);  //รายการอาหารที่สั้ง

        allDeliCost = (TextView)findViewById(R.id.allDeliCost);    //ค่าส่งทั้งหมด
        deliCostLow = (TextView)findViewById(R.id.deliCostLow);    //ค่าส่งขั้นต่ำ
        deliCostDistance = (TextView)findViewById(R.id.deliCostDistance); //ค่าส่งตามระยะทาง

        payBTN = (Button) findViewById(R.id.orderPay);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("รายการชำระ");

        try {
            //get intent to get person id
            resID = getIntent().getIntExtra("resID",0);
            nameOrder = getIntent().getStringExtra("nameOrder");
            detailPlaceOrder = getIntent().getStringExtra("detailPlaceOrder");
            numberOrder = getIntent().getStringExtra("numberOrder");
            mapOder = getIntent().getStringExtra("mapOrder");
            float Distance1 = getIntent().getFloatExtra("Distance",0);
            DecimalFormat df = new DecimalFormat();
            df.setMaximumFractionDigits(2);
            Distance = Float.parseFloat(df.format(Distance1));
            allDistance = Distance;
            vahicleOder = getIntent().getStringExtra("vahicleOrder");
            totalPrice = getIntent().getDoubleExtra("totalPrice",0);
            System.out.println("name "+nameOrder+" pdetail " +
                    detailPlaceOrder+" numOrder "+numberOrder+" mapOrder "+mapOder+" distance "+Distance+" vahicle "+vahicleOder+" resId "+resID+" totalPrice "+totalPrice);
        } catch (Exception e) {
            e.printStackTrace();
        }
        calculateDistanceCost();
        setAllCost();
        pullCredit();
        pullFoodList();

        payBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(credithas == 0){
                    Toast.makeText(getApplication(), "กรุณาเติมเครดิตก่อน", Toast.LENGTH_SHORT).show();
                }else {
                    tb_cart();
                    tb_order();
                    updateCredit();
                }
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

    private void pullCredit(){

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        user_id = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCustomer() ;

        Ion.with(getApplication())
                .load(baseUrl+"pullCredit.php")
                .setMultipartParameter("user_id", String.valueOf(user_id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        float creditDB = result.get("credit").getAsFloat();
                        credit.setText(creditDB+"");
                        payCalculate(creditDB);
                    }
                });

    }

    public void updateCredit(){
        float creditUdate;
        creditUdate = credithas;
        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String user_id1 = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCustomer() ;

        Ion.with(getApplication())
                .load(baseUrl+"creditUpdate.php")
                .setMultipartParameter("cus_id", String.valueOf(user_id1))
                .setMultipartParameter("credit_update", String.valueOf(creditUdate))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });

    }

    public void pullFoodList(){
        foodCost.setText(totalPrice+"");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCart() ;

        final int[] cart_ID = {0};

        Ion.with(getApplication())
                .load(baseUrl+"pullFoodList.php")
                .setMultipartParameter("user_id", String.valueOf(user_id))
                .asJsonArray()
                .setCallback(new FutureCallback<JsonArray>() {
                    @Override
                    public void onCompleted(Exception e, JsonArray result) {
                        JsonObject jsonObject = null;
                        for(int i=0; i < result.size(); i++){
                            jsonObject = (JsonObject)result.get(i);
                            CartAmount = CartAmount + jsonObject.get("cart_amount").getAsInt();
                            cart_ID[0] = jsonObject.get("cart_id").getAsInt();
                        }
                        num = result.size();
                        foodList.setText(num+" เมนู "+CartAmount+" รายการ");
                        setCart_id(cart_ID[0]);
                    }
                });

    }

    private void calculateDistanceCost(){
        float ditanceCost = 0;
        float distanceCostLow;
        float allDistance;
        allDistance = Distance;

        if(vahicleOder.equals("ตุกตุ๊ก")){
            deliCostLow.setText("60 บาท");
            distanceCostLow = 60;
            if(Distance <= 1){
                deliCostDistance.setText("0 บาท");
            }else {
                Distance = Distance -1;
                ditanceCost = Distance * 15;
                deliCostDistance.setText("("+Distance+"*15)/"+ditanceCost+" บาท");
            }
            allDistanceCost = distanceCostLow + ditanceCost;

        }else if(vahicleOder.equals("มอไซต์")){
            deliCostLow.setText("55 บาท");
            distanceCostLow = 55;
            if(Distance <= 1){
                deliCostDistance.setText("0 บาท");
            }else {
                Distance = Distance -1;
                ditanceCost = Distance * 9;
                deliCostDistance.setText("("+Distance+"*9)/"+ditanceCost+" บาท");
            }
            allDistanceCost = distanceCostLow + ditanceCost;
        }
        
        allDeliCost.setText(allDistance+" กม./"+allDistanceCost+" บาท");

    }

    private void setAllCost(){

         sumCost = totalPrice.floatValue() +allDistanceCost;

         allCost.setText(sumCost+" บาท");
    }

    private void payCalculate(float creditDB){

        if(creditDB <= 0){
            Toast.makeText(getApplication(), "กรุณาเติมเครดิตก่อน", Toast.LENGTH_SHORT).show();
        }else if(creditDB < sumCost) {
            Toast.makeText(getApplication(), "กรุณาเติมเครดิตก่อน", Toast.LENGTH_SHORT).show();
        }else{
                credithas = creditDB - sumCost;
        }

        creditHas.setText(credithas+" บาท ");

    }

    public void setCart_id(int id){
        this.cart_id = id;
        System.out.println("cart_id"+cart_id);
    }

    public void tb_cart(){
       int cart_id1= cart_id;
       String status = "Order";

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String user_id1 = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCart() ;

        Ion.with(getApplication())
                .load(baseUrl+"cartStatusOrder.php")
                .setMultipartParameter("cart_id", String.valueOf(cart_id1))
                .setMultipartParameter("status", String.valueOf(status))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });


    }

    public void tb_order(){
        int cart_id1= cart_id;
        Double cost = totalPrice;

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String user_id1 = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlOrder() ;

        Ion.with(getApplication())
                .load(baseUrl+"ordering.php")
                .setMultipartParameter("cart_id", String.valueOf(cart_id1))
                .setMultipartParameter("cus_id", String.valueOf(user_id1))
                .setMultipartParameter("order_cost", String.valueOf(cost))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        pullOrderId();
                    }
                });


    }

    public void pullOrderId(){
        int cart_id1= cart_id;

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlOrder() ;

        Ion.with(getApplication())
                .load(baseUrl+"pullOrderId.php")
                .setMultipartParameter("cart_id", String.valueOf(cart_id1))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        int order_id = result.get("order_id").getAsInt();
                        tb_payment(order_id);
                    }
                });
    }

    public void tb_delivery(int order_id){
        orderID = order_id;
        String deli_address;
        float deli_distance;
        String deli_phone;
        String deli_addDetail;
        String deli_name,deli_vahicle;
        float deli_distanceCost;

        deli_address = mapOder;
        deli_distance = allDistance;
        deli_phone = numberOrder;
        deli_vahicle = vahicleOder;
        deli_addDetail = detailPlaceOrder;
        deli_name = nameOrder;
        deli_distanceCost = allDistanceCost;

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDelivery() ;

        Ion.with(getApplication())
                .load(baseUrl+"delivery.php")
                .setMultipartParameter("order_id", String.valueOf(order_id))
                .setMultipartParameter("deli_address", String.valueOf(deli_address))
                .setMultipartParameter("deli_distance", String.valueOf(deli_distance))
                .setMultipartParameter("deli_phone", String.valueOf(deli_phone))
                .setMultipartParameter("deli_vahicle", String.valueOf(deli_vahicle))
                .setMultipartParameter("deli_addDetail", String.valueOf(deli_addDetail))
                .setMultipartParameter("deli_name", String.valueOf(deli_name))
                .setMultipartParameter("deli_distanceCost", String.valueOf(deli_distanceCost))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });
        Intent goToUpdate = new Intent(ordering.this, waitingDeliMan.class);
        goToUpdate.putExtra("orderID", orderID);
        startActivity(goToUpdate);
        System.out.println("orderID"+orderID);
    }

    public void tb_payment(int order_id){
        float paymentCots;
        paymentCots = sumCost;

        sharedpreferences = this.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String user_id1 = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlPayment() ;

        Ion.with(getApplication())
                .load(baseUrl+"payment.php")
                .setMultipartParameter("cus_id", String.valueOf(user_id1))
                .setMultipartParameter("order_id", String.valueOf(order_id))
                .setMultipartParameter("paymentCost", String.valueOf(paymentCots))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });

        tb_delivery(order_id);
    }


}
