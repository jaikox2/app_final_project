package com.example.pang.spacedeliveryforrestaurant.Restaurant;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pang.spacedeliveryforrestaurant.InfoNotification;
import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OrderResAdapter extends RecyclerView.Adapter<OrderResAdapter.ViewHolder> {

    private List<InfoNotification> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView FoodNameTxtV;
        public TextView Quantity;
        public ImageView FoodImageImgV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            FoodNameTxtV = (TextView) v.findViewById(R.id.textViewFoodName);
            FoodImageImgV = (ImageView) v.findViewById(R.id.imageViewFood);
            FoodImageImgV.setScaleType(ImageView.ScaleType.FIT_XY);
            Quantity = (TextView) v.findViewById(R.id.textViewQuan);
        }
    }

    public void add(int position, InfoNotification Info) {
        mFoodList.add(position, Info);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mFoodList.remove(position);
        notifyItemRemoved(position);
    }


    public OrderResAdapter(List<InfoNotification> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public OrderResAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.activity_order_res_adapter, parent, false);

        OrderResAdapter.ViewHolder vh = new OrderResAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final OrderResAdapter.ViewHolder holder, final int position) {

        final InfoNotification food = mFoodList.get(position);
        holder.FoodNameTxtV.setText("เมนู:" +food.getFoodName());
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood();
        Ion.with(mContext)
                .load(baseUrl+"upload-img/"+food.getFoodImg())
                .intoImageView(holder.FoodImageImgV);

        holder.Quantity.setText(String.valueOf(food.getQuantity()));

    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}
