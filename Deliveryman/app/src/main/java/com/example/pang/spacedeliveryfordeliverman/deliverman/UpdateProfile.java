package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.OpenableColumns;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.pang.spacedeliveryfordeliverman.R;
import com.example.pang.spacedeliveryfordeliverman.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.lang.reflect.Method;

import permission.auron.com.marshmallowpermissionhelper.ActivityManagePermission;
import permission.auron.com.marshmallowpermissionhelper.PermissionResult;
import permission.auron.com.marshmallowpermissionhelper.PermissionUtils;

public class UpdateProfile extends ActivityManagePermission {

    Button actionButton;
    EditText address,name,surname,sex,phone;
    TextView email,licensePlate,vahicle;
    private String name1,surname1,sex1,address1,email1,phone1,licensePlate1,vahicle1,img;
    public String RealPath = "null";

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;

    Activity mActivity;

    //https://jitpack.io/p/jesseruder/Android-Image-Cropper
    private static final int REQUEST_CAMERA_IMAGE = 1888;
    private static final int SELECT_PICTURE = 1;
    private ImageView cropImageView = null;
    File file = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);

        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        sex = (EditText)findViewById(R.id.sex);
        address = (EditText)findViewById(R.id.address);
        email = (TextView) findViewById(R.id.email);
        phone = (EditText) findViewById(R.id.res_phone);
        licensePlate = (TextView) findViewById(R.id.licensePlate);
        vahicle = (TextView)findViewById(R.id.vahicle);

        cropImageView = (ImageView) findViewById(R.id.profile_image);

        actionButton = (Button) findViewById(R.id.floatingActionButton2);

        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            //get intent to get person id
            name1 = getIntent().getStringExtra("name");
            surname1 = getIntent().getStringExtra("surname");
            sex1 = getIntent().getStringExtra("sex");
            address1 = getIntent().getStringExtra("address");
            email1 = getIntent().getStringExtra("email");
            phone1 = getIntent().getStringExtra("phone");
            licensePlate1 = getIntent().getStringExtra("licensePlate");
            vahicle1 = getIntent().getStringExtra("vahicle");
            img = getIntent().getStringExtra("img");
        } catch (Exception e) {
            e.printStackTrace();
        }

        name.setText(name1);
        surname.setText(surname1);
        sex.setText(sex1);
        address.setText(address1);
        email.setText(email1);
        phone.setText(phone1);
        licensePlate.setText(licensePlate1);
        vahicle.setText(vahicle1);

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDeliman() ;
        Glide.with(this)
                .load(baseUrl+ "upload-img/"+img)
                .into(cropImageView);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

        askForPermission();

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        cropImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });

    }

    private void askForPermission() {
        askCompactPermissions(new String[]{PermissionUtils.Manifest_CAMERA, PermissionUtils.Manifest_WRITE_EXTERNAL_STORAGE, PermissionUtils.Manifest_READ_EXTERNAL_STORAGE}, new PermissionResult() {
                    @Override
                    public void permissionGranted() {
                        Toast.makeText(UpdateProfile.this, "permissionGranted", Toast.LENGTH_LONG).show();

                    }

                    @Override
                    public void permissionDenied() {
                    }

                    @Override
                    public void permissionForeverDenied() {
                    }
                }

        );
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateProfile(){
        String Name = name.getText().toString();
        String Surname = surname.getText().toString();
        String Sex = sex.getText().toString();
        String Address = address.getText().toString();
        String Phone = phone.getText().toString();



        sharedpreferences = this.getApplication().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String user_id = sharedpreferences.getString(User_id, "");

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlDeliman() ;

        if(RealPath.equals("null")) {

            Ion.with(this)
                    .load(baseUrl + "UpdateProfile.php")
                    .setMultipartParameter("firstname", Name)
                    .setMultipartParameter("lastname", Surname)
                    .setMultipartParameter("sex",Sex)
                    .setMultipartParameter("address",Address)
                    .setMultipartParameter("phone", Phone)
                    .setMultipartParameter("img", img)
                    .setMultipartParameter("user_id", user_id)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String res = result.get("status").getAsString();
                            if (res.equals("update success")) {
                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                finish();
                                //startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(), "Can't saved", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }else {
            Ion.with(this)
                    .load(baseUrl + "UpdateProfile.php")
                    .setMultipartFile("upload_file", new File(RealPath))
                    .setMultipartParameter("firstname", Name)
                    .setMultipartParameter("lastname", Surname)
                    .setMultipartParameter("sex",Sex)
                    .setMultipartParameter("address",Address)
                    .setMultipartParameter("phone", Phone)
                    .setMultipartParameter("img", img)
                    .setMultipartParameter("user_id", user_id)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String res = result.get("status").getAsString();
                            if (res.equals("update success")) {
                                Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                // mActivity.finish();
                                finish();
                                //startActivity(new Intent(getApplicationContext(), MenuActivity.class));

                            } else {
                                Toast.makeText(getApplicationContext(), "Can't saved", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                //Get ImageURi and load with help of picasso
               // selectedImageURI = data.getData();

                CropImage.activity(data.getData())
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .setFixAspectRatio(true)
                        //.setOutputUri(dsad)
                        .setAspectRatio(2, 2)
                        .start(this);
               /* Picasso.with(MainActivity.this).load(data.getData()).noPlaceholder().centerCrop().fit()
                        .into((Target) findViewById(R.id.cropImageView));*/
            }

        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
               // cropImageView.setImageBitmap(bitmap);
                String realName = getFileName(resultUri);
                File imagePath = new File(getApplicationContext().getCacheDir(), "");
                file = new File(imagePath, realName);
                RealPath = file.toString();
                cropImageView.setImageURI(resultUri);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }
}
