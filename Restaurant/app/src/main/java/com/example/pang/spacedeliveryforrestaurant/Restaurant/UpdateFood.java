package com.example.pang.spacedeliveryforrestaurant.Restaurant;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.food;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.luseen.simplepermission.permissions.PermissionActivity;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import java.io.File;

import static android.content.ContentValues.TAG;

public class UpdateFood extends PermissionActivity {

    private long receivedFoodId;
    private String receivedName,receivedDetail,receivedImg;
    private double receivedPrice,receivedStamp;
    private static final int PICK_IMAGE = 100;
    EditText edtName,edtDetail,edtPrice,edtStamp;
    ImageView imageView ;
    Button button;
    FloatingActionButton actionButton;
    Uri imageUri;
    public food UpdateFood;
    public DbBitmapUtility bitmapUtility;
    byte[] imageByte ;
    public String RealPath = "null";
    private boolean mIsUploading = false;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;


    public void openGallery(){
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery,PICK_IMAGE);

    }

    private String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = { MediaStore.Images.Media.DATA };
            cursor = context.getContentResolver().query(contentUri,  proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            Log.e(TAG, "getRealPathFromURI Exception : " + e.toString());
            return "";
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            imageUri = data.getData();

            RealPath = getRealPathFromURI(getApplicationContext(),imageUri);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), imageUri);
                imageByte = bitmapUtility.getBytes(bitmap);
                System.out.println("Update 1 imageByte"+imageByte);
                Bitmap image = DbBitmapUtility.getImage(imageByte);
                imageView.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        if (id == android.R.id.home){
            this.finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_food);

        edtName =(EditText) findViewById(R.id.edtName);
        edtDetail =(EditText) findViewById(R.id.edtDetail);
        edtPrice = (EditText) findViewById(R.id.edtPrice);
        edtStamp = (EditText) findViewById(R.id.edtStamp);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        button = (Button) findViewById(R.id.button);
        actionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);


        //back button
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        try {
            //get intent to get person id
            receivedFoodId = getIntent().getLongExtra("ID", 1);
            receivedName = getIntent().getStringExtra("name");
            receivedDetail = getIntent().getStringExtra("detail");
            receivedImg = getIntent().getStringExtra("img");
            receivedPrice = getIntent().getDoubleExtra("price", 1);
            receivedStamp = getIntent().getDoubleExtra("stamp", 1);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //set field to this user data
        String price = Double.toString(receivedPrice);
        String stamp = Double.toString(receivedStamp);
        edtName.setText(receivedName);
        edtDetail.setText(receivedDetail);
        edtPrice.setText(price);
        edtStamp.setText(stamp);

        ipConfig ip = new ipConfig();
        final String baseUrl = ip.getBaseUrlFood() ;
        Ion.with(this)
                .load(baseUrl+"upload-img/"+receivedImg)
                .intoImageView(imageView);

        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission(com.luseen.simplepermission.permissions.Permission.READ_EXTERNAL_STORAGE, new SinglePermissionCallback() {
                    @Override
                    public void onPermissionResult(boolean granted, boolean isDeniedForever) {
                        if(granted) {

                            openGallery();

                        } else {
                            Toast.makeText(getBaseContext(), "ถ้าไม่อนุญาต จะไม่สามารถเข้าถึงไฟล์ได้", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        //listen to add button click to update
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateFood();
            }
        });

    }

    private void updateFood(){
        String name = edtName.getText().toString();
        String detail = edtDetail.getText().toString();
        String price = edtPrice.getText().toString();
        String stamp = edtStamp.getText().toString();
        String food_id = Long.toString(receivedFoodId);
        String Img = receivedImg;
        byte [] image = imageByte;

        sharedpreferences = this.getApplication().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String user_id = sharedpreferences.getString(User_id, "");


        if(name.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter a name", Toast.LENGTH_SHORT).show();
        }else if(detail.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter detail", Toast.LENGTH_SHORT).show();
        }else if(price.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an price", Toast.LENGTH_SHORT).show();
        } else if(stamp.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an stamp", Toast.LENGTH_SHORT).show();
        }else if(Img.isEmpty()){
            //error name is empty
            Toast.makeText(this, "You must enter an image link", Toast.LENGTH_SHORT).show();
        }else {

            String path = RealPath;
            System.out.println("Path:"+path);

            final Notification.Builder notifBuilder = new Notification.Builder(getApplicationContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("กำลังอัปโหลด");

            final int id = 1133;
            final NotificationManager notifMan = (NotificationManager) getApplication().getSystemService(NOTIFICATION_SERVICE);
            mIsUploading = true;

            ipConfig ip = new ipConfig();
            final String baseUrl = ip.getBaseUrlFood() ;

            System.out.println("URL:"+baseUrl);
            System.out.println("Food_id:"+food_id);
            System.out.println("User_id:"+user_id);



            if(path.equals("null")) {

                Ion.with(getApplicationContext())
                        .load(baseUrl + "UpdateFood.php")
                        .setMultipartParameter("food_id", food_id)
                        .setMultipartParameter("name", name)
                        .setMultipartParameter("detail", detail)
                        .setMultipartParameter("img", Img)
                        .setMultipartParameter("price", price)
                        .setMultipartParameter("stamp", stamp)
                        .setMultipartParameter("Res_id", user_id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                String res = result.get("status").getAsString();
                                if (res.equals("update success")) {
                                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    edtName.setText("");
                                    edtDetail.setText("");
                                    edtPrice.setText("");
                                    edtStamp.setText("");
                                    imageView.setImageResource(0);

                                    goBackHome();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Can't saved", Toast.LENGTH_SHORT).show();
                                }

                            }
                        });
            }else {

                Ion.with(getApplicationContext())
                        .load(baseUrl + "UpdateFood.php")
                        .uploadProgress(new ProgressCallback() {
                            @Override
                            public void onProgress(long loaded, long total) {
                                notifBuilder.setProgress((int) total, (int) loaded, false);
                                Notification notif = notifBuilder.build();
                                notifMan.notify(id, notif);
                            }
                        })
                        .setMultipartFile("upload_file", new File(path))
                        .setMultipartParameter("food_id", food_id)
                        .setMultipartParameter("name", name)
                        .setMultipartParameter("detail", detail)
                        .setMultipartParameter("img", Img)
                        .setMultipartParameter("price", price)
                        .setMultipartParameter("stamp", stamp)
                        .setMultipartParameter("Res_id", user_id)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                String res = result.get("status").getAsString();
                                notifBuilder.setProgress(100, 100, false);
                                notifBuilder.setContentText(res);
                                notifBuilder.setContentTitle("Food Park Delivery");
                                Notification notif = notifBuilder.build();
                                notifMan.notify(id, notif);


                                if (res.equals("update success")) {
                                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                                    edtName.setText("");
                                    edtDetail.setText("");
                                    edtPrice.setText("");
                                    edtStamp.setText("");
                                    imageView.setImageResource(0);

                                    goBackHome();

                                } else {
                                    Toast.makeText(getApplicationContext(), "Can't saved", Toast.LENGTH_SHORT).show();
                                }

                                mIsUploading = false;
                            }
                        });

            }

        }


    }

    private void goBackHome(){
       Intent intent = new Intent(UpdateFood.this , ResMenuActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//Bundle is optional
        //Bundle bundle = new Bundle();
        //bundle.putString("MyValue1", val1);
       // intent.putExtras(bundle);
//end Bundle
        startActivity(intent);
    }


}
