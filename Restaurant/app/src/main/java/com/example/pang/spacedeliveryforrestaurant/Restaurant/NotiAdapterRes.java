package com.example.pang.spacedeliveryforrestaurant.Restaurant;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.example.pang.spacedeliveryforrestaurant.InfoNotification;
import com.example.pang.spacedeliveryforrestaurant.R;

import java.util.List;

public class NotiAdapterRes extends RecyclerView.Adapter<NotiAdapterRes.ViewHolder>{
    private List<InfoNotification> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView NotiOrderIDTxtV;
        public TextView NotiTimeTxtV;
        public TextView NotiDateTxtV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            NotiOrderIDTxtV = (TextView) v.findViewById(R.id.textViewOrderID);
            NotiTimeTxtV = (TextView) v.findViewById(R.id.textViewTime);
            NotiDateTxtV = (TextView) v.findViewById(R.id.textViewDate);

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


    public NotiAdapterRes(List<InfoNotification> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public NotiAdapterRes.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.single_row_noti_res, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(NotiAdapterRes.ViewHolder holder,final int position) {

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
    }

    private void goToDetailActivity(int chooseId){
        Intent goToUpdate = new Intent(mContext, OrderRes.class);
        goToUpdate.putExtra("cart_id", chooseId);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }
}
