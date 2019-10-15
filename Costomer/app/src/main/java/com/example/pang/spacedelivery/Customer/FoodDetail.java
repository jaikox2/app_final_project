package com.example.pang.spacedelivery.Customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.ipConfig;
import com.koushikdutta.ion.Ion;
import com.luseen.simplepermission.permissions.PermissionActivity;

import static android.content.ContentValues.TAG;

public class FoodDetail extends PermissionActivity {

    private long receivedFoodId;
    private String receivedName,receivedDetail,receivedImg;
    private double receivedPrice,receivedStamp;
    TextView edtName,edtDetail,edtPrice,edtStamp;
    ImageView imageView ;

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        edtName =(TextView) findViewById(R.id.edtName);
        edtDetail =(TextView) findViewById(R.id.edtDetail);
        edtPrice = (TextView) findViewById(R.id.edtPrice);
        edtStamp = (TextView) findViewById(R.id.edtStamp);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

        setTitle("ข้อมูลอาหาร");
        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            //get intent to get person id
            receivedFoodId = getIntent().getLongExtra("ID", 1);
            receivedName = getIntent().getStringExtra("name");
            receivedDetail = getIntent().getStringExtra("detail");
            receivedImg = getIntent().getStringExtra("img");
            receivedPrice = getIntent().getDoubleExtra("price", 1);
            receivedStamp = getIntent().getDoubleExtra("stamp", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //set field to this user data
        String price = Double.toString(receivedPrice);
        String stamp = Double.toString(receivedStamp);
        edtName.setText(receivedName);
        edtDetail.setText(receivedDetail);
        edtPrice.setText(price);
        edtStamp.setText(stamp);

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;
        Ion.with(this)
                .load(baseUrl+"upload-img/"+receivedImg)
                .intoImageView(imageView);
    }

    private void goBackHome(){
       Intent intent = new Intent(FoodDetail.this , MenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//Bundle is optional
        //Bundle bundle = new Bundle();
        //bundle.putString("MyValue1", val1);
       // intent.putExtras(bundle);
//end Bundle
        startActivity(intent);
    }


}
