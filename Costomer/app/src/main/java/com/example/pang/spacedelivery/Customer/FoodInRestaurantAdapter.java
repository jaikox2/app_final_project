package com.example.pang.spacedelivery.Customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pang.spacedelivery.MainActivity;
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.interfaces.AddorRemoveCallbacks;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FoodInRestaurantAdapter extends RecyclerView.Adapter<FoodInRestaurantAdapter.ViewHolder> {

    private List<food> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTxtV;
        public TextView foodPriceTxtV;
        public ImageView foodImageImgV;
        public TextView foodDetailTxtV;
        public TextView foodChooseTxtV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            foodNameTxtV = (TextView) v.findViewById(R.id.textViewName);
            foodPriceTxtV = (TextView) v.findViewById(R.id.textViewPrice);
            foodImageImgV = (ImageView) v.findViewById(R.id.imageViewDis);
            foodDetailTxtV = (TextView) v.findViewById(R.id.textViewDetail);
            foodChooseTxtV = (TextView) v.findViewById(R.id.textViewChoose);
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


    public FoodInRestaurantAdapter(List<food> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public FoodInRestaurantAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.activity_food_in_restaurant_adapter, parent, false);

        FoodInRestaurantAdapter.ViewHolder vh = new FoodInRestaurantAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final FoodInRestaurantAdapter.ViewHolder holder, final int position) {

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;

        final food food = mFoodList.get(position);
        holder.foodNameTxtV.setText("เมนู: " + food.getFoodName());
        holder.foodPriceTxtV.setText("ราคา: " + food.getFoodPrice()+" บาท");
        Ion.with(mContext)
                .load(baseUrl+"upload-img/"+food.getFoodImage())
                .intoImageView(holder.foodImageImgV);


        holder.foodDetailTxtV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToUpdateActivity(food.getFoodId(),food.getFoodName(),food.getFoodDetail(),food.getFoodImage(),food.getFoodPrice(),food.getFoodStamp());
            }
        });

        if(food.getCheckChoose().equals("pass"))
        {
            if(mContext instanceof FoodInRestaurant)
            {
                mFoodList.get(position).setAddedTocart(true);
                holder.foodChooseTxtV.setText("ยกเลิกเลือก");
            }

        }else
        {
            mFoodList.get(position).setAddedTocart(false);
            holder.foodChooseTxtV.setText("เลือก");
        }

        holder.foodChooseTxtV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mFoodList.get(position).isAddedTocart())
                {
                    if(mContext instanceof FoodInRestaurant)
                    {
                        ipConfig ip1 = new ipConfig();
                        final String baseUrl1 = ip1.getBaseUrlCart() ;
                        SharedPreferences sharedpreferences;
                        final String MyPREFERENCES = "MyPrefs" ;
                        final String User_id = "userKey";
                        sharedpreferences = mContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
                        final String user_id1 = sharedpreferences.getString(User_id, "");
                        Ion.with(mContext)
                                .load(baseUrl1+"addToCart.php")
                                .setMultipartParameter("user_id",user_id1)
                                .setMultipartParameter("food_id", String.valueOf(food.getFoodId()))
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                        if(result.equals("cannotAdd")){
                                            System.out.println("TEST"+result);
                                            new AlertDialog.Builder(mContext)
                                                    .setTitle("เลือกรายการอาหาร")
                                                    .setMessage("ไม่สามารถเลือกรายการอาหารสั่งหลายร้านได้ เลือกได้เฉพาะร้านเดียวเท่านั้น")
                                                    .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.dismiss();
                                                        }
                                                    })
                                                    .setIcon(android.R.drawable.ic_menu_close_clear_cancel)
                                                    .show();
                                        }else {
                                            mFoodList.get(position).setAddedTocart(true);
                                            holder.foodChooseTxtV.setText("ยกเลิกเลือก");
                                            ((AddorRemoveCallbacks)mContext).onAddProduct();
                                        }
                                    }
                                });
                    }

                }else
                {
                    mFoodList.get(position).setAddedTocart(false);
                    holder.foodChooseTxtV.setText("เลือก");
                    ((AddorRemoveCallbacks)mContext).onRemoveProduct();
                    removeFoodCart(food.getFoodId());
                }
            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void goToUpdateActivity(long foodId,String name,String detail,String img ,double price ,double stamp){
        Intent goToUpdate = new Intent(mContext, FoodDetail.class);
        goToUpdate.putExtra("ID", foodId);
        goToUpdate.putExtra("name", name);
        goToUpdate.putExtra("detail", detail);
        goToUpdate.putExtra("img", img);
        goToUpdate.putExtra("price", price);
        goToUpdate.putExtra("stamp", stamp);
        mContext.startActivity(goToUpdate);
    }

    private void removeFoodCart(long foodId){
        ipConfig ip1 = new ipConfig();
        final String baseUrl1 = ip1.getBaseUrlCart() ;
        SharedPreferences sharedpreferences;
        final String MyPREFERENCES = "MyPrefs" ;
        final String User_id = "userKey";
        sharedpreferences = this.mContext.getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id1 = sharedpreferences.getString(User_id, "");
        Ion.with(mContext)
                .load(baseUrl1+"removeToCart.php")
                .setMultipartParameter("user_id",user_id1)
                .setMultipartParameter("food_id", String.valueOf(foodId))
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}
