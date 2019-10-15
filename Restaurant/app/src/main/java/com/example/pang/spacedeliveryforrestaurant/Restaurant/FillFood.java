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
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.pang.spacedeliveryforrestaurant.R;
import com.example.pang.spacedeliveryforrestaurant.ipConfig;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.koushikdutta.ion.ProgressCallback;
import com.luseen.simplepermission.permissions.Permission;
import com.luseen.simplepermission.permissions.PermissionFragment;
import com.luseen.simplepermission.permissions.SinglePermissionCallback;

import java.io.File;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;
import static android.content.Context.NOTIFICATION_SERVICE;

public class FillFood extends PermissionFragment {

    EditText edtName,edtDetail,edtPrice,edtStamp;
    ImageView  imageView ;
    Button     button;
    FloatingActionButton actionButton;

    private static final int PICK_IMAGE = 100;
    Uri imageUri;
    public String RealPath;
    public DbBitmapUtility bitmapUtility;
    byte[] imageByte;

    private boolean mIsUploading = false;

    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String User_id = "userKey";            //save session
    SharedPreferences sharedpreferences;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_res_fill_food, null);


        edtName =(EditText) view.findViewById(R.id.edtName);
        edtDetail =(EditText) view.findViewById(R.id.edtDetail);
        edtPrice = (EditText) view.findViewById(R.id.edtPrice);
        edtStamp = (EditText) view.findViewById(R.id.edtStamp);
        imageView = (ImageView) view.findViewById(R.id.imageView2);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        button = (Button) view.findViewById(R.id.button);
        actionButton = (FloatingActionButton) view.findViewById(R.id.floatingActionButton);



        actionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                requestPermission(Permission.READ_EXTERNAL_STORAGE, new SinglePermissionCallback() {
                    @Override
                    public void onPermissionResult(boolean granted, boolean isDeniedForever) {
                        if(granted) {

                            openGallery();

                        } else {
                            Toast.makeText(getActivity(), "ถ้าไม่อนุญาต จะไม่สามารถเข้าถึงไฟล์ได้", Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveFood();
            }
        });

        return view;
    }

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

            RealPath = getRealPathFromURI(getActivity(),imageUri);

            System.out.println("Real Path : "+RealPath);

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
                imageByte = bitmapUtility.getBytes(bitmap);
                System.out.println("Add imageByte :"+imageByte);
                Bitmap image = DbBitmapUtility.getImage(imageByte);
                imageView.setImageBitmap(image);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }



    private void saveFood(){
        String name = edtName.getText().toString();
        String detail = edtDetail.getText().toString();
        String price = edtPrice.getText().toString();
        String stamp = edtStamp.getText().toString();
        byte [] image = imageByte;

        sharedpreferences = this.getActivity().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String user_id = sharedpreferences.getString(User_id, "");


        if(name.isEmpty()){
            //error name is empty
            Toast.makeText(getActivity(), "You must enter a name", Toast.LENGTH_SHORT).show();
        }else if(detail.isEmpty()){
            //error name is empty
            Toast.makeText(getActivity(), "You must enter detail", Toast.LENGTH_SHORT).show();
        }else if(price.isEmpty()){
            //error name is empty
            Toast.makeText(getActivity(), "You must enter an price", Toast.LENGTH_SHORT).show();
        } else if(stamp.isEmpty()){
            //error name is empty
            Toast.makeText(getActivity(), "You must enter an stamp", Toast.LENGTH_SHORT).show();
        }else if(image == null){
            //error name is empty
            Toast.makeText(getActivity(), "You must enter an image link", Toast.LENGTH_SHORT).show();
        }else {

            String path = RealPath;

            final Notification.Builder notifBuilder = new Notification.Builder(getContext())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentText("กำลังอัปโหลด");

            final int id = 1122;
            final NotificationManager notifMan = (NotificationManager) getActivity().getSystemService(NOTIFICATION_SERVICE);
            mIsUploading = true;

            ipConfig ip = new ipConfig();
            final String baseUrl = ip.getBaseUrlFood() ;

            Ion.with(this)
                    .load(baseUrl+"FoodDB.php")
                    .uploadProgress(new ProgressCallback() {
                        @Override
                        public void onProgress(long loaded, long total) {
                            notifBuilder.setProgress((int) total, (int) loaded, false);
                            Notification notif = notifBuilder.build();
                            notifMan.notify(id, notif);
                        }
                    })
                    .setMultipartFile("upload_file", new File(path))
                    .setMultipartParameter("name",name)
                    .setMultipartParameter("detail",detail)
                    .setMultipartParameter("price",price)
                    .setMultipartParameter("stamp",stamp)
                    .setMultipartParameter("Res_id",user_id)
                    .asJsonObject()
                    .setCallback(new FutureCallback<JsonObject>() {
                        @Override
                        public void onCompleted(Exception e, JsonObject result) {
                            String res = result.get("status").getAsString();
                            notifBuilder.setProgress(100, 100, false);
                            notifBuilder.setContentTitle("Food Park Delivery");
                            notifBuilder.setContentText(res);
                            Notification notif = notifBuilder.build();
                            notifMan.notify(id, notif);

                            String anser ="uploaded";

                            if (res.equals("uploaded")) {
                                Toast.makeText(getActivity(), "Saved", Toast.LENGTH_SHORT).show();
                                edtName.setText("");
                                edtDetail.setText("");
                                edtPrice.setText("");
                                edtStamp.setText("");
                                imageView.setImageResource(0);

                            } else {
                                Toast.makeText(getActivity(), "Can't saved", Toast.LENGTH_SHORT).show();
                            }

                            mIsUploading = false;
                        }
                    });
        }
    }
}
