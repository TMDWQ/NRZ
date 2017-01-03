package com.men_cloths.mainContent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.men_cloths.R;
import com.men_cloths.adapter.AdapterforAll;
import com.men_cloths.model.ActivityManager;
import com.men_cloths.model.HasLogin;
import com.men_cloths.model.LoadImage;
import com.men_cloths.util.HttpTools;
import com.men_cloths.util.OrderTool;
import com.men_cloths.util.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/2.
 */

public class ThreadInfo extends Activity{

    private ListView listView;
    private List list;
    private LayoutInflater inflater;
    private ImageView back,head;
    private String ImageURL;
    private String name;
    public static final int SUCCESS=1;
    public static final int ERROR=-1;
    public static final int ADD_ORD_SUCESS=2;
    public static final int ADD_ORD_ERROR=-2;
    private final String URL_ADD_ORDER="http://139.199.196.199/index.php/home/index/setord?";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trend_zw);
        initView();
        View view=inflater.inflate(R.layout.trend_write_zw,null);
        head= (ImageView) view.findViewById(R.id.image_trend);
        listView.addHeaderView(view);
        Bitmap bitmap= ActivityManager.memoryCache.get(ImageURL);
        if(bitmap==null) {
            LoadImage.load(head, ImageURL, this);
        }else {
            head.setImageBitmap(bitmap);
        }
        createData();
        //listSetAdapter();


    }

    public void initView(){
        Intent intent=getIntent();
        ImageURL=intent.getStringExtra("url");
        name=intent.getStringExtra("name");
        listView= (ListView) findViewById(R.id.list_view_for_trend);
        list=new ArrayList();
        inflater=LayoutInflater.from(this);

        back= (ImageView) findViewById(R.id.back);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public static class MyInfo{
        String url;
        String name;
        String price;
        public MyInfo(String url, String name, String price){
            this.url=url;
            this.name=name;
            this.price=price;
        }
    }

    public void listSetAdapter(){
        listView.setAdapter(new AdapterforAll(this, list) {
            @Override
            public View setView(int position, View convertView) {
                Holder holder=null;
                final MyInfo info= (MyInfo) list.get(position);
                if(convertView==null){
                    convertView=inflater.inflate(R.layout.trend_write_zhangwen_item,null);
                    holder=new Holder();
                    holder.imageView= (ImageView) convertView.findViewById(R.id.zhangwen_suai);
                    holder.title= (TextView) convertView.findViewById(R.id.title);
                    holder.contents= (TextView) convertView.findViewById(R.id.content);
                    holder.logo= (TextView) convertView.findViewById(R.id.logo);
                    holder.goumai= (TextView) convertView.findViewById(R.id.goumai);
                    convertView.setTag(holder);
                }else {
                    holder= (Holder) convertView.getTag();
                }
                if(ActivityManager.memoryCache.get(info.url)!=null){
                    holder.imageView.setImageBitmap(ActivityManager.memoryCache.get(info.url));
                }else {
                    LoadImage.load(holder.imageView,info.url,ThreadInfo.this);
                }
                holder.title.setText(name);
                holder.logo.setText(info.name);
                holder.goumai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!HasLogin.hasLogin(ThreadInfo.this)){
                            Toast.makeText(ThreadInfo.this,"请先登录",Toast.LENGTH_SHORT).show();
                            return;
                        }
                        OrderTool tool=OrderTool.getTool(ThreadInfo.this,handler);
                        tool.createPayAlert(info.name,info.price,info.url);

                    }
                });
                return convertView;
            }
        });
    }
    /**
     * title        TextView
     content        TextView
     zhangwen_suai ImageView
     logo      TextView
     goumai    TextView
     * goumai
     * */
    private class Holder{
        ImageView imageView;
        TextView title,contents,logo,goumai;
    }


    public void createData(){
        HttpTools tools=new HttpTools("http://139.199.196.199/index.php/home/index/getclxq") {
            @Override
            public void requestSuccess(String line) {
                try {
                    JSONObject object=new JSONObject(line);
                    if(object.getString("code").equals("1")){
                        list.clear();
                        JSONArray array=object.getJSONArray("info");
                        for(int i=0;i<array.length();i++){
                            object=array.getJSONObject(i);
                            MyInfo info=new MyInfo(object.getString("url"),object.getString("name"),object.getString("price"));
                            list.add(info);
                        }
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

            }
        };
        tools.runForGet();
    }

    Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SUCCESS:
                    listSetAdapter();
                    break;
                case ERROR:
                    Toast.makeText(ThreadInfo.this,"付款失败",Toast.LENGTH_SHORT).show();
                    break;
                case ADD_ORD_SUCESS:
                    Toast.makeText(ThreadInfo.this,"付款成功",Toast.LENGTH_SHORT).show();
                    break;
                case OrderTool.PAY_SUCESS_:
                    OrderTool.destory();
                    MyInfo info= (MyInfo) msg.obj;
                    request(info.name,info.price,info.url);
                    break;
            }
        }
    };



    public void request(final String name, final String price, String url){
        try {
            String value="name=" + URLEncoder.encode(name,"utf-8")+ "&color="+ URLEncoder.encode("黑色","UTF-8")+"&dem=XLL&price=" +price+
                    "&tel="+ UserInfo.getTel(this)+"&url="+url+"&state=1";
            Log.i("hhh",URL_ADD_ORDER+value);
            HttpTools tools=new HttpTools(URL_ADD_ORDER+value) {
                @Override
                public void requestSuccess(String line) {
                    try {
                        Log.i("hhh",line);
                        JSONObject object=new JSONObject(line);
                        if(object.getString("code").equals("1")){
                            handler.sendEmptyMessage(ADD_ORD_SUCESS);
                        }else {
                            handler.sendEmptyMessage(ADD_ORD_ERROR);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void requestError() {

                }
            };
            tools.runForGet();


        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

}
