package com.example.pang.spacedelivery.Customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ResInfo;
import com.example.pang.spacedelivery.ipConfig;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class AllRestaurantAdapter extends RecyclerView.Adapter<AllRestaurantAdapter.ViewHolder> {

    private List<ResInfo> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView ResNameTxtV;
        public TextView ResAddressTxtV;
        public TextView ResPhoneTxtV;
        public ImageView ResImageImgV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            ResNameTxtV = (TextView) v.findViewById(R.id.textViewResName);
            ResAddressTxtV = (TextView) v.findViewById(R.id.textViewAddress);
            ResPhoneTxtV = (TextView) v.findViewById(R.id.textViewPhone);
            ResImageImgV = (ImageView) v.findViewById(R.id.imageViewRes);
            ResImageImgV.setScaleType(ImageView.ScaleType.FIT_XY);

        }
    }

    public void add(int position, ResInfo resInfo) {
        mFoodList.add(position, resInfo);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mFoodList.remove(position);
        notifyItemRemoved(position);
    }


    public AllRestaurantAdapter(List<ResInfo> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public AllRestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.activity_all_restaurantr_adapter, parent, false);

        AllRestaurantAdapter.ViewHolder vh = new AllRestaurantAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(AllRestaurantAdapter.ViewHolder holder, final int position) {


        final ResInfo resInfo = mFoodList.get(position);
        holder.ResNameTxtV.setText("ร้าน: " + resInfo.getResName());
        holder.ResAddressTxtV.setText("ที่อยู่: " + resInfo.getResaddress());
        holder.ResPhoneTxtV.setText("เบอร์: " + resInfo.getResphone());
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlRes();
        Ion.with(mContext)
                .load(baseUrl+"upload-proimg/"+resInfo.getImage())
                .intoImageView(holder.ResImageImgV);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                Intent goToUpdate = new Intent(mContext, FoodInRestaurant.class);
                goToUpdate.putExtra("res_id", resInfo.getId());
                goToUpdate.putExtra("resName", resInfo.getResName());
                mContext.startActivity(goToUpdate);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}