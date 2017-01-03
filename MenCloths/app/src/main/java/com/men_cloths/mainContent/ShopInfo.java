package com.men_cloths.mainContent;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.men_cloths.R;
import com.men_cloths.model.ActivityManager;
import com.men_cloths.model.HasLogin;
import com.men_cloths.util.HttpTools;
import com.men_cloths.util.ShopInfomation;
import com.men_cloths.util.UserInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2016/11/9.
 */

public class ShopInfo extends Activity {
    public static float width;
    public static  float WIDTH;
    private ViewFillperForNormal normal;
    private View v1,v2,v3;
    private ImageView back;
    private TextView name,price;
    private TextView addInShop;
    private final String URL_ADD_ORDER="http://139.199.196.199/index.php/home/index/setord?";
    private String order_name,order_price,order_url;
    public static final int SUCCESS=1;//修改成功
    public static final int ERROR=-1;//修改失败
    public static final int REQUEST_ERROR=-2;//网络请求失败
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        width=getWindowManager().getDefaultDisplay().getWidth();
        WIDTH=width;
        setContentView(R.layout.single_product_info);
        init();
        back.setOnClickListener(listener);
        Intent intent=getIntent();
        String url=intent.getStringExtra("pic");
        Bitmap bitmap= ActivityManager.bitmaps.get(url);
        if(bitmap!=null)
        normal.setBitmap(bitmap,bitmap,bitmap,this);
        ShopInfomation.Shop shop= ShopInfomation.get(this);
        name.setText(shop.getName());
        price.setText(shop.getPrice()+"0");
        order_name=shop.getName();
        order_price=shop.getPrice();
        order_url=url;
        order_price=infleter(order_price);
        addShopOrd();




    }
    public void addShopOrd(){
        addInShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if(!HasLogin.hasLogin(ShopInfo.this)){
                  Toast.makeText(ShopInfo.this,"请先登录用户",Toast.LENGTH_SHORT).show();
                  return;
              }
                request();

            }
        });
    }

    public void request(){
        try {
            String value="name=" +URLEncoder.encode(order_name,"utf-8")+ "&color="+ URLEncoder.encode("黑色","UTF-8")+"&dem=XLL&price=" +order_price+
                    "&tel="+ UserInfo.getTel(this)+"&url="+order_url;
            Log.i("hhh",URL_ADD_ORDER+value);
            HttpTools tools=new HttpTools(URL_ADD_ORDER+value) {
                @Override
                public void requestSuccess(String line) {
                    try {
                        Log.i("hhh",line);
                        JSONObject object=new JSONObject(line);
                        if(object.getString("code").equals("1")){
                            handler.sendEmptyMessage(SUCCESS);
                        }else {
                            handler.sendEmptyMessage(ERROR);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void requestError() {
                    handler.sendEmptyMessage(REQUEST_ERROR);
                }
            };
            tools.runForGet();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public void init(){
        normal= (ViewFillperForNormal) findViewById(R.id.normal);
        v1=findViewById(R.id.view1);
        v2=findViewById(R.id.view2);
        v3=findViewById(R.id.view3);
        name= (TextView) findViewById(R.id.name);
        price= (TextView) findViewById(R.id.price);
        addInShop= (TextView) findViewById(R.id.add_in_go_shop);
        normal.setChangedListener(new ViewFillperForNormal.CursorChanged(){
            @Override
            public void changedleft(int c) {
                switch (c){
                    case 1:
                        v1.setVisibility(View.VISIBLE);
                        v2.setVisibility(View.INVISIBLE);
                        v3.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        v2.setVisibility(View.VISIBLE);
                        v1.setVisibility(View.INVISIBLE);
                        v3.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        v3.setVisibility(View.VISIBLE);
                        v2.setVisibility(View.INVISIBLE);
                        v1.setVisibility(View.INVISIBLE);
                        break;
                }
            }

            @Override
            public void changedright(int c) {
                switch (c){
                    case 1:
                        v1.setVisibility(View.VISIBLE);
                        v2.setVisibility(View.INVISIBLE);
                        v3.setVisibility(View.INVISIBLE);
                        break;
                    case 2:
                        v2.setVisibility(View.VISIBLE);
                        v1.setVisibility(View.INVISIBLE);
                        v3.setVisibility(View.INVISIBLE);
                        break;
                    case 3:
                        v3.setVisibility(View.VISIBLE);
                        v2.setVisibility(View.INVISIBLE);
                        v1.setVisibility(View.INVISIBLE);
                        break;
                }

            }
        });

        back= (ImageView) findViewById(R.id.back);
    }

    View.OnClickListener listener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.back:
                    finish();
                    break;
            }
        }
    };

    Handler handler=new Handler(){

        public void handleMessage(Message message){
            switch (message.what){
                case SUCCESS:
                   // Toast.makeText(ShopInfo.this,"添加订单成功",Toast.LENGTH_SHORT).show();
                    if(dialog==null){
                        create();
                    }else {
                        if(!dialog.isShowing()){
                            dialog.show();
                        }
                    }
                    break;
                case ERROR:
                    Toast.makeText(ShopInfo.this,"添加订单失败，请重新添加",Toast.LENGTH_SHORT).show();
                    break;
                case REQUEST_ERROR:
                    Toast.makeText(ShopInfo.this,"网络异常，请检查网络是否连接",Toast.LENGTH_SHORT).show();
                    break;
            }
        }

    };

    public void create(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        LayoutInflater inflater=LayoutInflater.from(this);
        View view=inflater.inflate(R.layout.option,null);
        builder.setView(view);
        dialog=builder.create();
        dialog.show();
        LinearLayout linearLayout= (LinearLayout) view.findViewById(R.id.back_home);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                dialog.dismiss();
            }
        });
        linearLayout= (LinearLayout) view.findViewById(R.id.back);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        linearLayout= (LinearLayout) view.findViewById(R.id.scan_order);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ShopInfo.this,DingDanActivity.class);
                startActivity(intent);
                finish();
                dialog.dismiss();
            }
        });
        dialog.setCancelable(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    public String infleter(String str){
        if(str==null){
            return str;
        }
        String s="";
        for(int i=0;i<str.length();i++){
            if(str.charAt(i)!='￥')
            s+=str.charAt(i);
        }
        return s;
    }
}
