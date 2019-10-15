package com.example.pang.spacedeliveryforrestaurant.Restaurant;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import static android.content.Context.MODE_PRIVATE;

public class profile extends Fragment {

    Button actionButton;
    ImageView btnImg,profileIMG;
    TextView res_name,res_address,name,surname,email,phone,resMapAdd;
    private String resname,resAddress,name1,surname1,email1,phone1,resAddMap,img;
    SharedPreferences sharedpreferences;
    static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";

    Activity mActivity;
    private Context mContext;

    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_res_profile, null);

        actionButton = (Button) view.findViewById(R.id.floatingActionButton2);

        btnImg = (ImageView) view.findViewById(R.id.edit_btn);
        res_name = (TextView) view.findViewById(R.id.res_name);
        res_address = (TextView) view.findViewById(R.id.res_address);
        name = (TextView) view.findViewById(R.id.name);
        surname = (TextView) view.findViewById(R.id.surname);
        email = (TextView) view.findViewById(R.id.res_email);
        phone = (TextView) view.findViewById(R.id.res_phone);
        resMapAdd = (TextView) view.findViewById(R.id.res_MapAdd);
        profileIMG = (ImageView) view.findViewById(R.id.profile_image);

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlRes() ;

        refreshLayout = view.findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Ion.with(getContext())
                        .load(baseUrl + "profileRes.php")
                        .setMultipartParameter("Res_id",user_id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                resname = result.get("resname").getAsString();
                                resAddress = result.get("address").getAsString();
                                name1 = result.get("firstname").getAsString();
                                surname1 = result.get("lastname").getAsString();
                                email1 = result.get("email").getAsString();
                                phone1 = result.get("phone").getAsString();
                                resAddMap = result.get("addmap").getAsString();
                                img = result.get("img").getAsString();

                                res_name.setText(resname);
                                res_address.setText(resAddress);
                                name.setText(name1);
                                surname.setText(surname1);
                                email.setText(email1);
                                phone.setText(phone1);
                                resMapAdd.setText(resAddMap);
                                Glide.with(getContext())
                                        .load(baseUrl+ "upload-proimg/"+img)
                                        .into(profileIMG);

                                refreshLayout.setRefreshing(false);
                            }
                        });
            }
        });


        Ion.with(getContext())
                .load(baseUrl + "profileRes.php")
                .setMultipartParameter("Res_id",user_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                         resname = result.get("resname").getAsString();
                         resAddress = result.get("address").getAsString();
                         name1 = result.get("firstname").getAsString();
                         surname1 = result.get("lastname").getAsString();
                         email1 = result.get("email").getAsString();
                         phone1 = result.get("phone").getAsString();
                         resAddMap = result.get("addmap").getAsString();
                         img = result.get("img").getAsString();

                        res_name.setText(resname);
                        res_address.setText(resAddress);
                        name.setText(name1);
                        surname.setText(surname1);
                        email.setText(email1);
                        phone.setText(phone1);
                        resMapAdd.setText(resAddMap);
                        Glide.with(getContext())
                                .load(baseUrl+ "upload-proimg/"+img)
                                .into(profileIMG);
                    }
                });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Edit Info", Toast.LENGTH_SHORT).show();

                Intent goToUpdate = new Intent(getContext(), UpdateProfile.class);
                goToUpdate.putExtra("resName", resname);
                goToUpdate.putExtra("resAdd", resAddress);
                goToUpdate.putExtra("name", name1);
                goToUpdate.putExtra("surname", surname1);
                goToUpdate.putExtra("email", email1);
                goToUpdate.putExtra("phone", phone1);
                goToUpdate.putExtra("resAddMap", resAddMap);
                goToUpdate.putExtra("img", img);
                getContext().startActivity(goToUpdate);
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });



        return view;
    }

    public void logOut(){

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
        editor.commit();  // ยืนยันการแก้ไข preferences

        getActivity().finish();
    }
}
