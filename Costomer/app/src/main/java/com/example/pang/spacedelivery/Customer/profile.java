package com.example.pang.spacedelivery.Customer;

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
import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import static android.content.Context.MODE_PRIVATE;

public class profile extends Fragment {

    Button actionButton;
    ImageView btnImg,profileIMG;
    TextView address,name,surname,email,phone,sex,credit,ordering;
    private String address1,name1,surname1,email1,phone1,sex1,credit1,img1;
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

        name = (TextView) view.findViewById(R.id.name);
        surname = (TextView) view.findViewById(R.id.surname);
        sex = (TextView)view.findViewById(R.id.sex);
        address = (TextView)view.findViewById(R.id.address);
        email = (TextView) view.findViewById(R.id.res_email);
        phone = (TextView) view.findViewById(R.id.res_phone);
        credit = (TextView) view.findViewById(R.id.credit);
        btnImg = (ImageView) view.findViewById(R.id.edit_btn);
        profileIMG = (ImageView) view.findViewById(R.id.profile_image);

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        final String user_id = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCustomer() ;

        refreshLayout = view.findViewById(R.id.Swipe);
        refreshLayout.setColorSchemeResources(R.color.colorPrimaryDark);

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener(){
            @Override
            public void onRefresh() {
                Ion.with(getContext())
                        .load(baseUrl + "profileCus.php")
                        .setMultipartParameter("Res_id",user_id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {

                                name1 = result.get("firstname").getAsString();
                                surname1 = result.get("lastname").getAsString();
                                sex1 = result.get("sex").getAsString();
                                address1 = result.get("address").getAsString();
                                email1 = result.get("email").getAsString();
                                phone1 = result.get("phone").getAsString();
                                credit1 = result.get("credit").getAsString();
                                img1 = result.get("img").getAsString();

                                name.setText(name1);
                                surname.setText(surname1);
                                sex.setText(sex1);
                                address.setText(address1);
                                email.setText(email1);
                                phone.setText(phone1);
                                credit.setText(credit1);

                                Glide.with(getContext())
                                        .load(baseUrl+ "upload-proimg/"+img1)
                                        .into(profileIMG);

                                refreshLayout.setRefreshing(false);
                            }
                        });
            }
        });


        Ion.with(getContext())
                .load(baseUrl + "profileCus.php")
                .setMultipartParameter("Res_id",user_id)
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {

                        name1 = result.get("firstname").getAsString();
                        surname1 = result.get("lastname").getAsString();
                        sex1 = result.get("sex").getAsString();
                        address1 = result.get("address").getAsString();
                        email1 = result.get("email").getAsString();
                        phone1 = result.get("phone").getAsString();
                        credit1 = result.get("credit").getAsString();
                        img1 = result.get("img").getAsString();

                        name.setText(name1);
                        surname.setText(surname1);
                        sex.setText(sex1);
                        address.setText(address1);
                        email.setText(email1);
                        phone.setText(phone1);
                        credit.setText(credit1);

                        Glide.with(getContext())
                                .load(baseUrl+ "upload-proimg/"+img1)
                                .into(profileIMG);

                        refreshLayout.setRefreshing(false);
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
                goToUpdate.putExtra("address", address1);
                goToUpdate.putExtra("email", email1);
                goToUpdate.putExtra("phone", phone1);
                goToUpdate.putExtra("credit", credit1);
                goToUpdate.putExtra("img", img1);
                getContext().startActivity(goToUpdate);
            }
        });

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        ordering = (TextView) view.findViewById(R.id.ordering);
        ordering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoOrderingPage();
            }
        });


        return view;
    }

    public void gotoOrderingPage(){
        Intent goToWaitOrdering = new Intent(getContext(), waitOrderingPage.class);
        getContext().startActivity(goToWaitOrdering);
    }

    public void logOut(){

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();  // ทำการลบข้อมูลทั้งหมดจาก preferences
        editor.commit();  // ยืนยันการแก้ไข preferences

        getActivity().finish();
    }
}
