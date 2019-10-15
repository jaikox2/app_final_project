package com.example.pang.spacedeliveryforrestaurant.Restaurant;

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

import com.example.pang.spacedeliveryforrestaurant.R;

import java.util.regex.Pattern;

public class ResRegister extends Fragment {
    Button register;
    EditText name,surname,phone,email,pass,Cpass,resName,resAdd;
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

        View view = inflater.inflate(R.layout.activity_res_register, null);

       // this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  //full page not have title bar
       //         WindowManager.LayoutParams.FLAG_FULLSCREEN);

        name = (EditText) view.findViewById(R.id.editName);
        surname = (EditText) view.findViewById(R.id.editSurname);
        phone = (EditText) view.findViewById(R.id.editPhone);
        email = (EditText) view.findViewById(R.id.editEmail);
        pass = (EditText) view.findViewById(R.id.editPass);
        Cpass = (EditText) view.findViewById(R.id.editCpass);
        resName = (EditText) view.findViewById(R.id.editResName);
        resAdd = (EditText) view.findViewById(R.id.editResAdd);
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
        String phone1 = phone.getText().toString();
        String email1 = email.getText().toString();
        String resName1 = resName.getText().toString();
        String resAdd1 = resAdd.getText().toString();


        if(name1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกชื่อ", Toast.LENGTH_LONG).show();
        }else if(surname1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกนามสกุล", Toast.LENGTH_LONG).show();
        }else if (resName1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกชื่อร้านอาหาร", Toast.LENGTH_LONG).show();
        }else if(resAdd1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกที่อยู่ร้านอาหาร", Toast.LENGTH_LONG).show();
        }else if(phone1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกเบอร์โทร", Toast.LENGTH_LONG).show();
        }else if(email1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกอีเมลล์", Toast.LENGTH_LONG).show();
        }else if (!Patterns.EMAIL_ADDRESS.matcher(email1).matches()){
            Toast.makeText(getContext(), "อีเมลล์ไม่ถูกต้อง", Toast.LENGTH_LONG).show();
        }else if (pass1.isEmpty()){
            Toast.makeText(getContext(), "กรุณากรอกรหัสผ่าน", Toast.LENGTH_LONG).show();
        }else if(!PASSWORD_PATTERN.matcher(pass1).matches()){
            Toast.makeText(getContext(), "กรุณากรอกรหัสผ่านอย่างน้อย 4 ตัว", Toast.LENGTH_LONG).show();
        }else if(pass1.equals(pass2)) {

        Intent goToUpdate = new Intent(getContext(), getResLocationsMapsActivity.class);
        goToUpdate.putExtra("name", name1);
        goToUpdate.putExtra("surname", surname1);
        goToUpdate.putExtra("phone",phone1);
        goToUpdate.putExtra("email", email1);
        goToUpdate.putExtra("pass", pass1);
        goToUpdate.putExtra("resName", resName1);
        goToUpdate.putExtra("resAdd", resAdd1);
        this.startActivity(goToUpdate);

        }else {
            Toast.makeText(getContext(), "รหัสผ่านยืนยันไม่ถูกต้อง", Toast.LENGTH_LONG).show();
        }
    }
}
