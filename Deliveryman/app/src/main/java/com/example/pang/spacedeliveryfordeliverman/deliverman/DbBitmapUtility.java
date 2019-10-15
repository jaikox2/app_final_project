package com.example.pang.spacedeliveryfordeliverman.deliverman;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class DbBitmapUtility {

    // convert from bitmap to byte array
    public static byte[] getBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from byte array to bitmap
    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    public static void getImageShow(ImageView view, byte[] image) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
        view.setImageBitmap(bitmap);
    }

}