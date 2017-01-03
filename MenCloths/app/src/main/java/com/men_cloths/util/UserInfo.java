package com.men_cloths.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Administrator on 2016/12/29.
 */

public class UserInfo{
    public static void saveToken(Context context,String token){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("token",token);
        editor.commit();
    }
    public static String getToken(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_",context.MODE_PRIVATE);
        return sharedPreferences.getString("token","");
    }

    public static void saveTel(Context context,String tel){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("tel",tel);
        editor.commit();
    }
    public static String getTel(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_",context.MODE_PRIVATE);
        return sharedPreferences.getString("tel","");
    }

    public static void saveHeadImage(Context context,String tel,String url){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_head",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("tel",tel);
        editor.putString("head",url);
        editor.commit();
    }
    public static String getHeadImageURL(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_head",context.MODE_PRIVATE);
        return sharedPreferences.getString("head","");
    }
    public static String getHeadImageTel(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_head",context.MODE_PRIVATE);
        return sharedPreferences.getString("tel","");
    }



    public static void saveName(Context context,String name){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_",context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("name",name);
        editor.commit();
    }
    public static String getName(Context context){
        SharedPreferences sharedPreferences=context.getSharedPreferences("user_info_",context.MODE_PRIVATE);
        return sharedPreferences.getString("name","");
    }

    public static void saveFile(Context context,InputStream is,String name){
        try {
            OutputStream os=context.openFileOutput(name,context.MODE_PRIVATE);
            int c;
            while ((c=is.read())!=-1){
                os.write(c);
            }
            os.flush();
            is.close();
            os.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static InputStream  getName(Context context,String name){
        try {
            return context.openFileInput(name);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }


}
