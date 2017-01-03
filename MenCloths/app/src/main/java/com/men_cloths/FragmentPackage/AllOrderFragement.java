package com.men_cloths.FragmentPackage;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.men_cloths.R;
import com.men_cloths.mainContent.DingDanActivity;
import com.men_cloths.mainContent.HomeActivity;
import com.men_cloths.model.ActivityManager;
import com.men_cloths.model.LoadImage;
import com.men_cloths.model.OrderAlll;
import com.men_cloths.util.HttpTools;
import com.men_cloths.util.OrderTool;
import com.men_cloths.util.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public class AllOrderFragement extends Fragment{
    private  TextView goShopping;
    private ListView listView;
    private List<OrderAlll> list=new ArrayList<>();
    private final String URL_GET_ALL_ORDER="http://139.199.196.199/index.php/home/index/getord?tel=";
    private static Context context;
    public static final int SUCESS=789;
    public static final int ERROR=-789;
    public static final int NETWORK_EORROR=-2;
    private LayoutInflater inflater;
    private LinearLayout linearLayout;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dingdan_empty,null);
        goShopping = (TextView) view.findViewById(R.id.go_shop);
        goShopping.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);//添加下划线
        goShopping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0;i<HomeActivity.myButtonClick.length;i++){
                    if(i==1){
                        HomeActivity.myButtonClick[i]=true;
                    }else {
                        HomeActivity.myButtonClick[i]=false;
                    }
                }
                getActivity().finish();
            }
        });
        listView= (ListView) view.findViewById(R.id.all_order_zw);
        context=getContext();
        this.inflater=LayoutInflater.from(context);
        linearLayout= (LinearLayout) view.findViewById(R.id.is_null);
        DingDanActivity.handler2=handler;
        getDate();
        return view;
    }

    public void getDate(){
        Log.i("hhh","调用3");
        HttpTools tools=new HttpTools(URL_GET_ALL_ORDER+ UserInfo.getTel(context)) {
            @Override
            public void requestSuccess(String line) {
                try {
                    JSONObject object=new JSONObject(line);
                    if(object.getString("code").equals("1")){
                         list.clear();
                         JSONArray array=object.getJSONArray("ord");
                         for(int i=0;i<array.length();i++){
                            OrderAlll alll=new OrderAlll();
                            object=array.getJSONObject(i);
                            alll.setColor(object.optString("color","黑色"));
                            alll.setId(object.getInt("id"));
                            alll.setName(object.getString("name"));
                           if( object.optInt("state")==1){
                               alll.setPayed(true);
                           }else {
                               alll.setPayed(false);
                           }
                            alll.setSize(object.optString("dem","XLL"));
                            alll.setUrl(object.getString("url"));
                            alll.setPrice(object.getString("price"));
                            list.add(alll);

                        }
                        DingDanActivity.handler2.sendEmptyMessage(SUCESS);


                    }else {
                        DingDanActivity.handler2.sendEmptyMessage(ERROR);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void requestError() {
                DingDanActivity.handler2.sendEmptyMessage(NETWORK_EORROR);
            }
        };
        tools.runForGet();
    }


    private BaseAdapter baseAdapter;
    public void createAdapter(){
       baseAdapter=new BaseAdapter() {
           @Override
           public int getCount() {
               return list.size();
           }

           @Override
           public Object getItem(int position) {
               return list.get(position);
           }

           @Override
           public long getItemId(int position) {
               return position;
           }

           @Override
           public View getView(final int position, View convertView, ViewGroup parent) {
               if(list.get(position).isPayed()){
                   convertView=inflater.inflate(R.layout.order_waitget_item,null);
                   ImageView goodsPicture;
                   TextView goodsName,goodsColor,goodsSize,goodsPrice,deleteOrder;
                   goodsPicture= (ImageView) convertView.findViewById(R.id.goods_picture);
                   goodsName= (TextView) convertView.findViewById(R.id.goods_name);
                   goodsColor= (TextView) convertView.findViewById(R.id.goods_color);
                   goodsSize= (TextView) convertView.findViewById(R.id.goods_size);
                   goodsPrice= (TextView) convertView.findViewById(R.id.goods_price);
                  // LoadImage.load(goodsPicture,list.get(position).getUrl(),context);
                   deleteOrder= (TextView) convertView.findViewById(R.id.delete_order);
                   if(ActivityManager.memoryCache.get(list.get(position).getUrl())==null) {
                       LoadImage.load(goodsPicture, list.get(position).getUrl(), context);
                   }else {
                       goodsPicture.setImageBitmap(ActivityManager.memoryCache.get(list.get(position).getUrl()));
                   }
                   goodsName.setText(list.get(position).getName());
                   goodsColor.setText(list.get(position).getColor());
                   goodsSize.setText(list.get(position).getSize());
                   goodsPrice.setText("￥"+list.get(position).getPrice());
                   deleteOrder.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                          delete(list.get(position).getId());

                       }
                   });
               }else {
                   convertView=inflater.inflate(R.layout.order_waitpay_item,null);
                   ImageView goodsPicture;
                   TextView goodsName,goodsColor,goodsSize,goodsPrice,goPay;
                   goodsPicture= (ImageView) convertView.findViewById(R.id.goods_picture);
                   goodsName= (TextView) convertView.findViewById(R.id.goods_name);
                   goodsColor= (TextView) convertView.findViewById(R.id.goods_color);
                   goodsSize= (TextView) convertView.findViewById(R.id.goods_size);
                   goodsPrice= (TextView) convertView.findViewById(R.id.goods_price);
                   goPay= (TextView) convertView.findViewById(R.id.go_pay);
                   if(ActivityManager.memoryCache.get(list.get(position).getUrl())==null) {
                       LoadImage.load(goodsPicture, list.get(position).getUrl(), context);
                   }else {
                       goodsPicture.setImageBitmap(ActivityManager.memoryCache.get(list.get(position).getUrl()));
                   }
                   goodsName.setText(list.get(position).getName());
                   goodsColor.setText(list.get(position).getColor());
                   goodsSize.setText(list.get(position).getSize());
                   goodsPrice.setText("￥"+list.get(position).getPrice());
                   goPay.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           fuKuan(list.get(position).getName(),list.get(position).getPrice(),list.get(position).getId());
                       }
                   });

               }
               return convertView;
           }
       };
   }
    public  void  fuKuan(String name,String price,int id){
        OrderTool tool=OrderTool.getTool(context,DingDanActivity.handler2);
        tool.createPayAlert(name,price,id);
    }

    public AlertDialog delete(int id){
        OrderTool tool=OrderTool.getTool(context,DingDanActivity.handler2);
        tool.createDeleteAlert(id);
        return tool.getDelete();
    }

    public  Handler handler=new Handler(){
      public void handleMessage(Message msg){
          switch (msg.what){
              case SUCESS:
                  if(list.size()==0){
                      listView.setVisibility(View.GONE);
                      linearLayout.setVisibility(View.VISIBLE);
                  }else {
                      listView.setVisibility(View.VISIBLE);
                      linearLayout.setVisibility(View.GONE);
                  }
                  if(listView.getAdapter()==null){
                      createAdapter();
                      listView.setAdapter(baseAdapter);
                  }else {
                      baseAdapter.notifyDataSetChanged();
                  }
                  break;
              case ERROR:
                  break;
              case OrderTool.DELETE_ERROR:
                  Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                  break;
              case OrderTool.DELETE_SUCESS:
                  getDate();
                  Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                  DingDanActivity.ischagedAll=true;
                  break;
              case OrderTool.PAY_ERROR:
                  Toast.makeText(context,"付款失败",Toast.LENGTH_SHORT).show();
                  break;
              case OrderTool.PAY_SUCESS:
                  Toast.makeText(context,"付款成功",Toast.LENGTH_SHORT).show();
                  getDate();
                  DingDanActivity.ischagedAll=true;
                  break;
          }
      }
    };

}
