package com.men_cloths.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.men_cloths.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Administrator on 2016/12/2.
 */
public class LoadImage extends AsyncTask<String,Void,Bitmap>{
    private ImageView imageView;
    private String url;
    private Context context;
    //private   BeingLoaded loaded;
    private AnimationDrawable drawable;

    private LoadImage(){}

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if(bitmap!=null && imageView.getTag().equals(url)){
            imageView.setImageBitmap(bitmap);
            ActivityManager.memoryCache.add(url,bitmap);
        }
//        loaded.dismiss();
        //Log.i("hhh",bitmap+"");
        drawable.stop();
        imageView.setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//       loaded=new BeingLoaded(context);
//        if(loaded.getAlerDialog()==null){
//            loaded.createAlert();
//        }else {
//            if(!loaded.isShow()){
//                loaded.show();
//            }
//        }
        imageView.setImageDrawable(null);
        imageView.setBackground(context.getResources().getDrawable(R.drawable.ld_image));
        drawable= (AnimationDrawable) imageView.getBackground();
        drawable.start();

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap=null;
        try {
            InputStream is=null;

            if(CachetToFile.isloaded(context,url)){
               is=CachetToFile.getImage(url,context);
               bitmap=BitmapFactory.decodeStream(is);
            }else {
                is=new URL(params[0]).openConnection().getInputStream();
                CachetToFile.saveImage(url,context,is);
                bitmap= BitmapFactory.decodeStream(CachetToFile.getImage(url,context));
            }

            if (is!=null){
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return bitmap;
    }

    public static void load(ImageView imageView,String url,Context context){
        LoadImage loadImage=new LoadImage();
        loadImage.imageView=imageView;
        loadImage.url=url;
        loadImage.context=context;
        imageView.setTag(url);
        loadImage.execute(url);
    }
}

