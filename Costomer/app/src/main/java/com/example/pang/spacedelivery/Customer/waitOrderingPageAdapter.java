package com.example.pang.spacedelivery.Customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.pang.spacedelivery.InfoNotification;
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class waitOrderingPageAdapter extends RecyclerView.Adapter<waitOrderingPageAdapter.ViewHolder>{
    private List<InfoNotification> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView NotiOrderIDTxtV;
        public TextView NotiTimeTxtV;
        public TextView NotiDateTxtV;
        public Button btnGetFood;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            NotiOrderIDTxtV = (TextView) v.findViewById(R.id.textViewOrderID);
            NotiTimeTxtV = (TextView) v.findViewById(R.id.textViewTime);
            NotiDateTxtV = (TextView) v.findViewById(R.id.textViewDate);
            btnGetFood = (Button) v.findViewById(R.id.btngotfood);


        }
    }

    public void add(int position, InfoNotification infoNotification) {
        mFoodList.add(position, infoNotification);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mFoodList.remove(position);
        notifyItemRemoved(position);
    }


    public waitOrderingPageAdapter(List<InfoNotification> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public waitOrderingPageAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.activity_wait_ordering_page_adapter, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(waitOrderingPageAdapter.ViewHolder holder,final int position) {

        final InfoNotification infoNotification = mFoodList.get(position);
        holder.NotiOrderIDTxtV.setText("หมายเลข : " + infoNotification.getOrderId());
        holder.NotiTimeTxtV.setText( infoNotification.getTime());
        holder.NotiDateTxtV.setText(infoNotification.getDate());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                goToDetailActivity(infoNotification.getChooseId());
            }
        });

        holder.btnGetFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
                goToGetFood(infoNotification.getOrderId());
            }
        });
    }

    private void goToDetailActivity(int chooseId){
        Intent goToUpdate = new Intent(mContext, foodInOrdering.class);
        goToUpdate.putExtra("cart_id", chooseId);
        mContext.startActivity(goToUpdate);
    }

    private void goToGetFood(int order_id){
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlOrder() ;

        System.out.println(baseUrl+"getFood.php");
        Ion.with(mContext)
                .load(baseUrl+"getFood.php")
                .setMultipartParameter("order_id", String.valueOf(order_id))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        Toast.makeText(mContext, "ขอบคุณที่ใช้บริการเรา", Toast.LENGTH_LONG).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}
