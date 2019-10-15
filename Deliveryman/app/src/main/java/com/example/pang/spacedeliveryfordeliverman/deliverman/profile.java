package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
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
import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import static android.content.Context.MODE_PRIVATE;

public class profile extends Fragment {

    Button actionButton;
    ImageView btnImg,profileIMG;
    TextView address,name,surname,email,phone,sex,licensePlate,vahicle;
    private String Address,name1,surname1,email1,phone1,sex1,licensePlate1,vahicle1,img;

    SharedPreferences sharedpreferences;
    static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";

    Activity mActivity;
    private Context mContext;

    private SwipeRefreshLayout refreshLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_profile, null);

        actionButton = (Button) view.findViewById(R.id.floatingActionButton2);

        btnImg = (ImageView) view.findViewById(R.id.edit_btn);
        name = (TextView) view.findViewById(R.id.name);
        surname = (TextView) view.findViewById(R.id.surname);
        sex = (TextView) view.findViewById(R.id.sex);
        address = (TextView) view.findViewById(R.id.Address);
        email = (TextView) view.findViewById(R.id.email);
        phone = (TextView) view.findViewById(R.id.phone);
        licensePlate = (TextView) view.findViewById(R.id.licensePlate);
        vahicle = (TextView) view.findViewById(R.id.vahicle);
        profileIMG = (ImageView) view.findViewById(R.id.profile_image);

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDeliman() ;

        refreshLayout = view.findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Ion.with(getContext())
                        .load(baseUrl + "profileDeli.php")
                        .setMultipartParameter("user_id",user_id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                name1 = result.get("firstname").getAsString();
                                surname1 = result.get("lastname").getAsString();
                                sex1 = result.get("sex").getAsString();
                                Address = result.get("address").getAsString();
                                email1 = result.get("email").getAsString();
                                phone1 = result.get("phone").getAsString();
                                licensePlate1 = result.get("licensePlate").getAsString();
                                vahicle1 = result.get("vahicle").getAsString();
                                img = result.get("img").getAsString();

                                name.setText(name1);
                                surname.setText(surname1);
                                sex.setText(sex1);
                                address.setText(Address);
                                email.setText(email1);
                                phone.setText(phone1);
                                licensePlate.setText(licensePlate1);
                                vahicle.setText(vahicle1);
                                Glide.with(getContext())
                                        .load(baseUrl+ "upload-img/"+img)
                                        .into(profileIMG);

                                refreshLayout.setRefreshing(false);
                            }
                        });
            }
        });


        Ion.with(getContext())
                .load(baseUrl + "profileDeli.php")
                .setMultipartParameter("user_id",user_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        name1 = result.get("firstname").getAsString();
                        surname1 = result.get("lastname").getAsString();
                        sex1 = result.get("sex").getAsString();
                        Address = result.get("address").getAsString();
                        email1 = result.get("email").getAsString();
                        phone1 = result.get("phone").getAsString();
                        licensePlate1 = result.get("licensePlate").getAsString();
                        vahicle1 = result.get("vahicle").getAsString();
                        img = result.get("img").getAsString();

                        name.setText(name1);
                        surname.setText(surname1);
                        sex.setText(sex1);
                        address.setText(Address);
                        email.setText(email1);
                        phone.setText(phone1);
                        licensePlate.setText(licensePlate1);
                        vahicle.setText(vahicle1);
                        Glide.with(getContext())
                                .load(baseUrl+ "upload-img/"+img)
                                .into(profileIMG);


                    }
                });

        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), "Edit Info", Toast.LENGTH_SHORT).show();

                Intent goToUpdate = new Intent(getContext(), UpdateProfile.class);
                goToUpdate.putExtra("name", name1);
                goToUpdate.putExtra("surname", surname1);
                goToUpdate.putExtra("sex", sex1);
                goToUpdate.putExtra("address", Address);
                goToUpdate.putExtra("email", email1);
                goToUpdate.putExtra("phone", phone1);
                goToUpdate.putExtra("licensePlate", licensePlate1);
                goToUpdate.putExtra("vahicle", vahicle1);
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
