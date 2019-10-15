package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.app.Activity;
import android.content.Intent;
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

import com.example.pang.spacedeliveryfordeliverman.MainActivity;
import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import java.util.regex.Pattern;

public class DeliRegister extends Fragment {
    Button register;
    EditText name,surname,sex,address,phone,email,pass,Cpass,licensePlate,vahicle;
    Activity mActivity;
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z0-9])" +      //any letter
                    // "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    // "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_register, null);

       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //full page not have title bar
       //         WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = (EditText) view.findViewById(R.id.editName);
        surname = (EditText) view.findViewById(R.id.editSurname);
        sex = (EditText) view.findViewById(R.id.editSex);
        address = (EditText) view.findViewById(R.id.editAdd);
        phone = (EditText) view.findViewById(R.id.editPhone);
        email = (EditText) view.findViewById(R.id.editEmail);
        pass = (EditText) view.findViewById(R.id.editPass);
        Cpass = (EditText) view.findViewById(R.id.editCpass);
        licensePlate = (EditText) view.findViewById(R.id.editLicensePlate);
        vahicle = (EditText) view.findViewById(R.id.editVahicle);
        register = (Button) view.findViewById(R.id.Register);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

        return view;
    }

    public void Register(){

        String pass1 = pass.getText().toString();
        String pass2 = Cpass.getText().toString();

        String name1 = name.getText().toString();
        String surname1 = surname.getText().toString();
        String sex1 = sex.getText().toString();
        String address1 = address.getText().toString();
        String phone1 = phone.getText().toString();
        String email1 = email.getText().toString();
        String licensePalte1 = licensePlate.getText().toString();
        String vahicle1 = vahicle.getText().toString();


        if(name1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกชื่อ", Toast.LENGTH_LONG).show();
        }else if(surname1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกนามสกุล", Toast.LENGTH_LONG).show();
        }else if (sex1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกเพศ", Toast.LENGTH_LONG).show();
        }else if(address1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกที่อยู่", Toast.LENGTH_LONG).show();
        }else if(phone1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกเบอร์โทร", Toast.LENGTH_LONG).show();
        }else if(licensePalte1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกป้ายทะเบียนรถ", Toast.LENGTH_LONG).show();
        }else if(vahicle1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกชนิดของรถคุณ", Toast.LENGTH_LONG).show();
        }else if(email1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกอีเมลล์", Toast.LENGTH_LONG).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            Toast.makeText(getContext(), "อีเมลล์ไม่ถูกต้อง", Toast.LENGTH_LONG).show();
        }else if (pass1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_LONG).show();
        }else if(!PASSWORD_PATTERN.matcher(pass1).matches()){
            Toast.makeText(getContext(), "กรุณากรอกรหัสผ่านอย่างน้อย 4 ตัว", Toast.LENGTH_LONG).show();
        }else if(pass1.equals(pass2)) {

            ipConfig ip = new ipConfig();
            final String baseUrl = ip.getBaseUrlDeliman() ;

            Ion.with(getContext())
                    .load(baseUrl + "DelimanRegister.php")
                    .setMultipartParameter("name", name1)
                    .setMultipartParameter("surname", surname1)
                    .setMultipartParameter("sex", sex1)
                    .setMultipartParameter("address", address1)
                    .setMultipartParameter("phone", phone1)
                    .setMultipartParameter("email", email1)
                    .setMultipartParameter("pass", pass1)
                    .setMultipartParameter("licensePlate", licensePalte1)
                    .setMultipartParameter("vahicle", vahicle1)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String re = result.get("res").getAsString();
                            String res = result.get("status").getAsString();
                            if (res.endsWith("#1")) {
                                startActivity(new Intent(getContext(), MainActivity.class));
                                Toast.makeText(getContext(), re, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getContext(), re, Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else {
            Toast.makeText(getContext(), "รหัสผ่านยืนยันไม่ถูกต้อง", Toast.LENGTH_LONG).show();
        }
    }
}
