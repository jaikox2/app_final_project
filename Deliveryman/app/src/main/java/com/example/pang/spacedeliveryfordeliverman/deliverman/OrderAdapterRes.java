package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.deliveryList;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class OrderAdapterRes extends RecyclerView.Adapter<OrderAdapterRes.ViewHolder> {
    private List<deliveryList> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView order;
        public TextView res_name;
        public TextView DateTime;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            order = (TextView) v.findViewById(R.id.order_id);
            res_name = (TextView) v.findViewById(R.id.res_name);
            DateTime = (TextView) v.findViewById(R.id.dateTime);
        }
    }

    public void add(int position, deliveryList food) {
        mFoodList.add(position, food);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mFoodList.remove(position);
        notifyItemRemoved(position);
    }


    public OrderAdapterRes(List<deliveryList> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public OrderAdapterRes.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.single_row_res, parent, false);

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        final String MyPREFERENCES = "MyPrefs" ;
        final String User_id = "userKey";            //save session
        SharedPreferences sharedpreferences;
        sharedpreferences = this.mContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");


        final deliveryList deli = mFoodList.get(position);
        holder.order.setText(""+deli.getOder_id());
        holder.res_name.setText(deli.getRes_name());
        holder.DateTime.setText(deli.getTime()+" "+deli.getDate());

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                if (deli.getDeliman_id() == Integer.valueOf(user_id)){
                    goToDeliMapMydeli(deli.getDeli_id(),deli.getOder_id(),deli.getRes_name(),deli.getRes_location(),deli.getDelivery_location());
                }else {
                    goToDeliMap(deli.getDeli_id(),deli.getOder_id(),deli.getRes_name(),deli.getRes_location(),deli.getDelivery_location());
                }
            }
        });


    }

    private void goToDeliMap(int deli_id,int Order_id,String res_name,String res_location,String deli_location){
        Intent goToUpdate = new Intent(mContext, getResLocationsMapsActivity.class);
        goToUpdate.putExtra("deli_id", deli_id);
        goToUpdate.putExtra("order_id", Order_id);
        goToUpdate.putExtra("res_name", res_name);
        goToUpdate.putExtra("res_location", res_location);
        goToUpdate.putExtra("deli_location", deli_location);
        mContext.startActivity(goToUpdate);
    }

    private void goToDeliMapMydeli(int deli_id,int Order_id,String res_name,String res_location,String deli_location){
        Intent goToUpdate = new Intent(mContext, getLocationsMapsMydeli.class);
        goToUpdate.putExtra("deli_id", deli_id);
        goToUpdate.putExtra("order_id", Order_id);
        goToUpdate.putExtra("res_name", res_name);
        goToUpdate.putExtra("res_location", res_location);
        goToUpdate.putExtra("deli_location", deli_location);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}

