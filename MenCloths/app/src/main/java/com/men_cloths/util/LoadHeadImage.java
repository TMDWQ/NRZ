package com.men_cloths.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Administrator on 2016/12/29.
 */

public class LoadHeadImage extends AsyncTask<String,Void,Bitmap> {


    private HeadImage image;
    private LoadHeadImage(HeadImage image){
        this.image=image;
    }

    public static void load(HeadImage image,String url){
        if(url==null || url.length()==0){
            return;
        }
        LoadHeadImage loadHeadImage=new LoadHeadImage(image);
        loadHeadImage.execute(url);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!=null){
            image.setBitmap(bitmap);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        Bitmap bitmap=null;
        try {
            URLConnection connection=new URL(params[0]).openConnection();
            connection.setConnectTimeout(3000);
            InputStream is=connection.getInputStream();
            bitmap= BitmapFactory.decodeStream(is);
            if(is!=null){
                is.close();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }
}
