package com.example.pang.spacedelivery.Customer;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class waitingDeliMan extends AppCompatActivity {

    int orderID;
    Timer t1 = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_deli_man);

        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //full page not have title bar
        //        WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setTitle("ค้นหาคนส่งอาหาร");

        try {
            //get intent to get person id
            orderID = getIntent().getIntExtra("orderID",0);
            System.out.println(orderID);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //main method
        t1.schedule(new Demo(), 10000,6000);

    }

    class Demo extends TimerTask {

        @Override
        public void run() {

            ipConfig ip = new ipConfig();
            final String baseUrl = ip.getBaseUrlDelivery() ;

            Ion.with(getApplication())
                    .load(baseUrl+"checkDeliMan.php")
                    .setMultipartParameter("order_id", String.valueOf(orderID))
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {

                            int deliman_id = result.get("deliman_id").getAsInt();

                            if(deliman_id == 0){
                                //Toast.makeText(getApplication(), "Checked"+deliman_id, Toast.LENGTH_SHORT).show();
                            }else {
                                Intent goToUpdate = new Intent(waitingDeliMan.this, finishOrder.class);
                                goToUpdate.putExtra("deliman_id", deliman_id);
                                goToUpdate.putExtra("orderID", orderID);
                                startActivity(goToUpdate);
                                t1.cancel();
                            }

                        }
                    });

        }
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        new AlertDialog.Builder(this)
                .setTitle("กลับไปยังหน้าหลัก")
                .setPositiveButton("ใช่", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(getApplication(), MenuActivity.class));
                        t1.cancel();
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
}
