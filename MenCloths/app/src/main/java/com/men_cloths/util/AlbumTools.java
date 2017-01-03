package com.men_cloths.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.util.Date;

/**
 * Created by 张文 on 2016/12/24.
 */

public class AlbumTools {

    public static void openAlbum(Activity activity,int requestCode){//打开相册（activity,请求吗）
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        activity.startActivityForResult(albumIntent,requestCode);//进行回调
    }

    public static File createFile(Context context){
        File file;
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(Environment.getExternalStorageDirectory() +
                    File.separator + timeStamp+".jpg");
        }else{
            File cacheDir = context.getCacheDir();
            String timeStamp = String.valueOf(new Date().getTime());
            file = new File(cacheDir, timeStamp+".jpg");
        }
        return file;
    }

    public static String choose(int requestCode, int resultCode,int RESULT,Intent data,Context context){
        /**
         * 验证请求码和返回码
         * */
        if(requestCode == RESULT && resultCode == Activity.RESULT_OK && data != null){

            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            return  picturePath;

        }

        return null;
    }

    public static String choose(Intent data,Context context){//将onAvtivityResult的Inten里面的数据quchulai
        /**
         * 不验证请求码和返回码
         * */
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = context.getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            return  picturePath;
    }



    public static void narrowPhoto(ImageView photo,String picturePath,int w){//图片缩放

        if(picturePath.equals(""))
            return;
        // 缩放图片, width, height 按相同比例缩放图片
        BitmapFactory.Options options = new BitmapFactory.Options();
        // options 设为true时，构造出的bitmap没有图片，只有一些长宽等配置信息，但比较快，设为false时，才有图片
        options.inJustDecodeBounds = true;
        Bitmap bitmap =BitmapFactory.decodeFile(picturePath, options);
        int scale = (int)( options.outWidth / (float)w);
        if(scale <= 0)
            scale = 1;
        options.inSampleSize = scale;
        options.inJustDecodeBounds = false;
        bitmap = BitmapFactory.decodeFile(picturePath, options);

        photo.setImageBitmap(bitmap);
        photo.setMaxHeight(350);
    }









}
