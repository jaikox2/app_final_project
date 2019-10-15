package com.example.pang.spacedeliveryforrestaurant.Restaurant;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.food;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.List;

public class FoodAdapterRes extends RecyclerView.Adapter<FoodAdapterRes.ViewHolder> {
    private List<food> mFoodList;
    private Context mContext;
    private RecyclerView mRecyclerV;




    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView foodNameTxtV;
        public TextView foodPriceTxtV;
        public ImageView foodImageImgV;


        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            foodNameTxtV = (TextView) v.findViewById(R.id.textViewName);
            foodPriceTxtV = (TextView) v.findViewById(R.id.textViewPrice);
            foodImageImgV = (ImageView) v.findViewById(R.id.imageViewDis);
            foodImageImgV.setScaleType(ImageView.ScaleType.FIT_XY);

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


    public FoodAdapterRes(List<food> myDataset, Context context, RecyclerView recyclerView) {
        mFoodList = myDataset;
        mContext = context;
        mRecyclerV = recyclerView;
    }

    @Override
    public FoodAdapterRes.ViewHolder onCreateViewHolder(ViewGroup parent,
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


        final food food = mFoodList.get(position);
        holder.foodNameTxtV.setText("เมนู: " + food.getName());
        holder.foodPriceTxtV.setText("ราคา: " + food.getPrice()+" บาท");
       // Bitmap imagebitmap = DbBitmapUtility.getImage(food.getImage());
       // holder.foodImageImgV.setImageBitmap(imagebitmap);
        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;
        Ion.with(mContext)
                .load(baseUrl+"upload-img/"+food.getImage())
                .intoImageView(holder.foodImageImgV);

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Choose option");
                //"Choose option"
                //"Update or delete food menu?"
                builder.setMessage("Update or delete food menu?");
                builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //go to update activity
                        goToUpdateActivity(food.getId(),food.getName(),food.getDetail(),food.getImage(),food.getPrice(),food.getStamp());

                    }
                });
                builder.setNeutralButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long id = food.getId();
                        String food_id = Long.toString(id);
                        Ion.with(mContext)
                                .load(baseUrl+"DeleteFood.php")
                                .setBodyParameter("food_id", food_id)
                                .setBodyParameter("img", food.getImage())
                                .asString()
                                .setCallback(new FutureCallback<String>() {
                                    @Override
                                    public void onCompleted(Exception e, String result) {
                                            Toast.makeText(mContext, result, Toast.LENGTH_LONG).show();
                                    }
                                });

                        mFoodList.remove(position);
                        // mRecyclerV.removeViewAt(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, mFoodList.size());
                        notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                //builder.create().show();
                AlertDialog alert = builder.create();
                alert.show();
                Button nbutton = alert.getButton(DialogInterface.BUTTON_NEUTRAL);
                //Set negative button text color
                nbutton.setTextColor(Color.parseColor("#FB724B"));
                Button pbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                //Set negative button text color
                pbutton.setTextColor(Color.parseColor("#FB724B"));
                Button cbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                //Set negative button text color
                cbutton.setTextColor(Color.parseColor("#FB724B"));
            }
        });


    }

    private void goToUpdateActivity(long foodId,String name,String detail,String img ,double price ,double stamp){
        Intent goToUpdate = new Intent(mContext, UpdateFood.class);
        goToUpdate.putExtra("ID", foodId);
        goToUpdate.putExtra("name", name);
        goToUpdate.putExtra("detail", detail);
        goToUpdate.putExtra("img", img);
        goToUpdate.putExtra("price", price);
        goToUpdate.putExtra("stamp", stamp);
        mContext.startActivity(goToUpdate);
    }

    @Override
    public int getItemCount() {
        return mFoodList.size();
    }

}

