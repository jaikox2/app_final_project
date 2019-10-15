package com.example.pang.spacedelivery.Customer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.pang.spacedelivery.R;
import com.example.pang.spacedelivery.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.MODE_PRIVATE;

public class Login extends Fragment {

    EditText EdUser, passED;
    Button login;
    String user, pass;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;



    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_login, null);


        EdUser = (EditText) view.findViewById(R.id.edtUser);
        passED = (EditText) view.findViewById(R.id.edtPasswd);  //declare Edit text and button
        login = (Button) view.findViewById(R.id.btnLogin);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateUser();
            }
        });

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);  // ดึง share preference ชื่อ MyPrefs เก็บไว้ในตัวแปร sharedpreferences
        if (sharedpreferences.contains(User_id))   // ตรวจสอบ name ใน preference
        {
                startActivity(new Intent(getContext(), MenuActivity.class));
        }

        return view;
    }

    public void ValidateUser() {

        final SharedPreferences.Editor editor = sharedpreferences.edit();
        user = EdUser.getText().toString();
        pass = passED.getText().toString();

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlCustomer() ;

        if(user.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกอีเมล์", Toast.LENGTH_LONG).show();
        }else if(!Patterns.EMAIL_ADDRESS.matcher(user).matches()) {
            Toast.makeText(getContext(), "อีเมลล์ไม่ถูกต้อง", Toast.LENGTH_LONG).show();
        }else if(pass.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_LONG).show();
        }
        else
        {
            Ion.with(getContext())
                    .load(baseUrl+"CusLogin.php")
                    .setBodyParameter("login", EdUser.getText().toString())
                    .setBodyParameter("password", passED.getText().toString())
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String user_id = result.get("user_id").getAsString();
                            String res = result.get("status").getAsString();
                            if (res.endsWith("#1")) {
                                startActivity(new Intent(getContext(), MenuActivity.class));
                                editor.putString(User_id, user_id);  // preferance เก็บค่า user_id จาก edittext
                                editor.commit();  // ยืนยันการแก้ไข preferance
                            } else {
                                Toast.makeText(getContext(), res, Toast.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }
}
