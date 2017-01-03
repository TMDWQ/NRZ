package com.men_cloths.mainContent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.men_cloths.R;
import com.men_cloths.util.AlbumTools;
import com.men_cloths.util.HeadImage;
import com.men_cloths.util.HttpTools;
import com.men_cloths.util.ImageCat;
import com.men_cloths.util.LoadHeadImage;
import com.men_cloths.util.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

/**
 * Created by Administrator on 2016/10/19.
 */

public class PersonInfo extends Activity {
   private ImageView back;
   //private ImageView head;
   private TextView userName;
   private String token,tel;
   private String imageUrl;
   private String name;
   private String str=null;
   private EditText editText;
   private String disanf;
   private HeadImage head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.person_info);
        init();
        if(tel.length()!=11){
            editText.setText(disanf);
            editText.setEnabled(false);
        }else {
            new Thread(){
                public void run(){
                    getInfo();
                }
            }.start();
        }
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(UserInfo.getTel(PersonInfo.this).length()!= 11){
                    Toast.makeText(PersonInfo.this,"暂不支持此功能",Toast.LENGTH_SHORT).show();
                    return;
                }

                AlbumTools.openAlbum(PersonInfo.this,100);
            }
        });



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 100:
                if(data==null){
                    return;
                }
                ImageCat.cat(data.getData(),120,120,this,101);
                break;
            case 101:
                if(data==null){
                    return;
                }
                Bitmap bitmap=ImageCat.getBitmap(data);
                if(bitmap!=null){
                    head.setBitmap(bitmap);
                    try {
                        OutputStream os=openFileOutput("head",MODE_PRIVATE);
                        bitmap.compress(Bitmap.CompressFormat.PNG,90,os);
                        upload(openFileInput("head"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

                break;
        }
    }

    private void init(){
        back= (ImageView) findViewById(R.id.back);
        head= (HeadImage) findViewById(R.id.head);
        userName= (TextView) findViewById(R.id.user_name);
        editText= (EditText) findViewById(R.id.editText2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (str==null || str.equals("") || tel.length()!=11){
                  finish();
              }else {
                  Toast.makeText(PersonInfo.this,"正在保存信息请稍候",Toast.LENGTH_SHORT).show();
                  new Thread(){
                      public void run(){
                          setInfo(str);
                      }
                  }.start();
              }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                  str=s.toString();
            }
        });
        SharedPreferences preferences=getSharedPreferences("login_info",MODE_PRIVATE);
        token=preferences.getString("token","");
        tel=preferences.getString("tel","");
        disanf=preferences.getString("message","");
    }

    private void getInfo(){
        String url="http://139.199.196.199/index.php/home/index/userinfo?token="+token+"&tel="+tel;
        HttpURLConnection connection=null;
        BufferedReader reader=null;

        try {
            connection= (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode()==200){
                String line="";
                reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                line=reader.readLine();
                Log.i("hhh",line);
                JSONObject o1=new JSONObject(line);
                String state=o1.optString("statue","");
                if(state.equals("1")){
                    JSONObject object=o1.getJSONObject("info");
                    name=object.optString("username",null);
                    imageUrl=object.optString("head_portrait",null);
                    Message message=Message.obtain();
                    message.what=1;
                    handler.sendMessage(message);
                    SharedPreferences preferences=getSharedPreferences("login_info",MODE_PRIVATE);
                    SharedPreferences.Editor editor=preferences.edit();
                    editor.putInt("coupon_expired_count",object.optInt("coupon_expired_count",0));
                    editor.putInt("coupon_used_count",object.optInt("coupon_used_count",0));
                    editor.putInt("coupon_notuesd_count",object.optInt("coupon_notuesd_count",0));
                    editor.commit();
                    UserInfo.saveHeadImage(PersonInfo.this,tel,imageUrl);


                }else {
                    Message message=Message.obtain();
                    message.what=-2;
                    handler.sendMessage(message);
                }


            }else {
                Message message=Message.obtain();
                message.what=-1;
                handler.sendMessage(message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }finally {
            if (connection!=null){
                connection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void setInfo(String s){

        String url= null;
        try {
            url = "http://139.199.196.199/index.php/home/index/changeinformation?token="+token+"&tel="+tel+"&username="+ URLEncoder.encode(s,"utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        HttpURLConnection connection=null;
        BufferedReader reader=null;

        try {
            connection= (HttpURLConnection) new URL(url).openConnection();
            connection.setConnectTimeout(5000);
            connection.setRequestMethod("GET");
            connection.connect();

            if(connection.getResponseCode()==200){
                String line="";
                reader=new BufferedReader(new InputStreamReader(connection.getInputStream()));
                line=reader.readLine();
                Log.i("hhh",line);
                Message message=Message.obtain();
                message.what=0;
                handler.sendMessage(message);


            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(connection!=null){
                connection.disconnect();
            }
            if(reader!=null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }

    Handler handler=new Handler(){

        public void handleMessage(Message message){
            switch (message.what){
                case 1:
                    if(name!=null){
                        try {
                            editText.setText(URLDecoder.decode(name,"utf-8"));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }
                    if(imageUrl!=null){
                       // LoadImage.load(head,imageUrl,PersonInfo.this);
                        LoadHeadImage.load(head,imageUrl);
                    }
                    break;
                case -1:
                    Toast.makeText(PersonInfo.this,"发生网络请求的错误",Toast.LENGTH_SHORT).show();
                    break;
                case 0:
                    UserInfo.saveName(PersonInfo.this,str);
                    finish();
                    break;
                case -2:
                    Toast.makeText(PersonInfo.this,"你还没有设置任何信息",Toast.LENGTH_SHORT).show();
                    break;
                case 8:
                    Toast.makeText(PersonInfo.this,"图片上传成功",Toast.LENGTH_SHORT).show();
                    break;
                case -8:
                    Toast.makeText(PersonInfo.this,"图片上传失败",Toast.LENGTH_SHORT).show();
                    break;
                case -88:
                    Toast.makeText(PersonInfo.this,"图片上传网络异常",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void upload(InputStream is){
        Log.i("hhh","ssssssssssssssssssssss");
        String url="http://139.199.196.199/index.php/home/index/uploadheadportrait";
        HttpTools tools= new HttpTools(url) {
            @Override
            public void requestSuccess(String line) {

                try {
                    JSONObject object=new JSONObject(line);

                    if(object.getString("status").equals("1")){
                        handler.sendEmptyMessage(8);
                        Log.i("hhh","aaaaaaaaaaaaaaaaaaaaaaa");
                    }else {
                        Log.i("hhh","bbbbbbbbbbbbbbbbb");
                        handler.sendEmptyMessage(-8);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError() {

                handler.sendEmptyMessage(-88);
            }
        };
        HashMap<String,Object> hashMap=new HashMap<>();
        HashMap<String,InputStream> iss=new HashMap<>();
        iss.put("test",is);
        hashMap.put("tel", UserInfo.getTel(this));
        hashMap.put("token",UserInfo.getToken(this));
        tools.runForInput(hashMap,iss);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (str==null || str.equals("") || tel.length()!=11){
            finish();
        }else {

            Toast.makeText(PersonInfo.this,"正在保存信息请稍候",Toast.LENGTH_SHORT).show();
            new Thread(){
                public void run(){
                    setInfo(str);
                }
            }.start();
        }
    }
}
