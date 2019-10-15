package com.example.pang.spacedelivery.Customer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.ipConfig;
import com.koushikdutta.ion.Ion;

import java.util.List;

import static java.security.AccessController.getContext;

public class FoodAdapterRecom extends RecyclerView.Adapter<FoodAdapterRecom.ViewHolder> {
    private List<food> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTxtV;
        public TextView foodPriceTxtV;
        public ImageView foodImageImgV;
        public TextView ResNameTxtV;
        public TextView ResAddressTxtV;
        public ImageView ResImageImgV;
        public LinearLayout ResProfile;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            foodNameTxtV = (TextView) v.findViewById(R.id.textViewName);
            foodPriceTxtV = (TextView) v.findViewById(R.id.textViewPrice);
            foodImageImgV = (ImageView) v.findViewById(R.id.imageViewRecom);
            ResNameTxtV = (TextView) v.findViewById(R.id.textViewResName);
            ResAddressTxtV = (TextView) v.findViewById(R.id.textViewResAddress);
            ResImageImgV = (ImageView) v.findViewById(R.id.resimage);
            ResProfile = (LinearLayout) v.findViewById(R.id.linear);

        }
    }

    public void add(int position, food food) {
        mFoodList.add(position, food);
        notifyItemInserted(position);
    }

    public void remove(int position) {
        mFoodList.remove(position);
        notifyItemRemoved(position);
    }


    public FoodAdapterRecom(List<food> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public FoodAdapterRecom.ViewHolder onCreateViewHolder(ViewGroup parent,
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

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;

        final food food = mFoodList.get(position);
        holder.foodNameTxtV.setText("เมนู: " + food.getFoodName());
        holder.foodPriceTxtV.setText("ราคา: " + food.getFoodPrice()+" บาท");
        Ion.with(mContext)
                .load(baseUrl+"upload-img/"+food.getFoodImage())
                .intoImageView(holder.foodImageImgV);

        ipConfig ip1 = new ipConfig();
        final String baseUrl1 = ip1.getBaseUrlRes() ;

        holder.ResNameTxtV.setText( food.getResName());
        holder.ResAddressTxtV.setText( food.getResAddress());
        Glide.with(mContext)
                .load(baseUrl1+ "upload-proimg/"+food.getResImage())
                .into(holder.ResImageImgV);


        holder.ResProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goToUpdate = new Intent(mContext, resInfomation.class);
                goToUpdate.putExtra("res_id", food.getResId());
                mContext.startActivity(goToUpdate);
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

                Intent goToUpdate = new Intent(mContext, FoodInRestaurant.class);
                goToUpdate.putExtra("res_id", food.getResId());
                goToUpdate.putExtra("resName", food.getResName());
                mContext.startActivity(goToUpdate);

            }
        });


    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}

