package com.example.pang.spacedelivery.Customer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
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

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ResInfo;
import com.example.pang.spacedelivery.food;
import com.example.pang.spacedelivery.interfaces.AddorRemoveCallbacks;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class FoodInCartAdapter extends RecyclerView.Adapter<FoodInCartAdapter.ViewHolder> {

    private List<food> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView FoodNameTxtV;
        public TextView FoodPrice,DeleteTxTV,ResName,Quantity;
        public ImageView FoodImageImgV;
        public FloatingActionButton plus,minus;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            FoodNameTxtV = (TextView) v.findViewById(R.id.textViewFoodName);
            FoodPrice = (TextView) v.findViewById(R.id.textViewFoodPrice);
            ResName = (TextView) v.findViewById(R.id.textViewResName);
            DeleteTxTV = (TextView) v.findViewById(R.id.textViewDelete);
            FoodImageImgV = (ImageView) v.findViewById(R.id.imageViewFood);
            FoodImageImgV.setScaleType(ImageView.ScaleType.FIT_XY);
            plus = (FloatingActionButton) v.findViewById(R.id.floatingActionPlus);
            minus = (FloatingActionButton) v.findViewById(R.id.floatingActionMinus);
            Quantity = (TextView) v.findViewById(R.id.textViewQuan);
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


    public FoodInCartAdapter(List<food> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public FoodInCartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                              int viewType) {

        LayoutInflater inflater = LayoutInflater.from(
                parent.getContext());
        View v =
                inflater.inflate(R.layout.activity_food_in_cart_adapter, parent, false);

        FoodInCartAdapter.ViewHolder vh = new FoodInCartAdapter.ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final FoodInCartAdapter.ViewHolder holder, final int position) {

        final food food = mFoodList.get(position);
        holder.FoodNameTxtV.setText("เมนู:" +food.getFoodName());
        holder.FoodPrice.setText("ราคา: " + food.getFoodPrice());
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood();
        Ion.with(mContext)
                .load(baseUrl+"upload-img/"+food.getFoodImage())
                .intoImageView(holder.FoodImageImgV);

        holder.ResName.setText(""+food.getResName());

        holder.DeleteTxTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeFoodCart(food.getFoodId());

                mFoodList.remove(position);
                // mRecyclerV.removeViewAt(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, mFoodList.size());
                notifyDataSetChanged();
            }
        });

        holder.Quantity.setText(String.valueOf(food.getCartAmount()));

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(food.getCartAmount() == 1){

                }else {
                    food.setCartAmount(food.getCartAmount()-1);
                    holder.Quantity.setText(String.valueOf(food.getCartAmount()));
                    ipConfig ip1 = new ipConfig();
                    final String baseUrl1 = ip1.getBaseUrlCart() ;
                    Ion.with(mContext)
                            .load(baseUrl1+"updateAmount.php")
                            .setMultipartParameter("c_id",String.valueOf(food.getCart_id()))
                            .setMultipartParameter("quan", String.valueOf(food.getCartAmount()))
                            .asString()
                            .setCallback(new FutureCallback<String>() {
                                @Override
                                public void onCompleted(Exception e, String result) {
                                    FoodInCart userhome = (FoodInCart) mContext;
                                    userhome.poultererView();
                                }
                            });
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                food.setCartAmount(food.getCartAmount()+1);
                holder.Quantity.setText(String.valueOf(food.getCartAmount()));
                ipConfig ip1 = new ipConfig();
                final String baseUrl1 = ip1.getBaseUrlCart() ;
                Ion.with(mContext)
                        .load(baseUrl1+"updateAmount.php")
                        .setMultipartParameter("c_id",String.valueOf(food.getCart_id()))
                        .setMultipartParameter("quan", String.valueOf(food.getCartAmount()))
                        .asString()
                        .setCallback(new FutureCallback<String>() {
                            @Override
                            public void onCompleted(Exception e, String result) {
                                FoodInCart userhome = (FoodInCart) mContext;
                                userhome.poultererView();
                            }
                        });
            }
        });


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
                        FoodInCart userhome = (FoodInCart) mContext;
                        userhome.poultererView();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}
