package com.men_cloths.FragmentPackage;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.men_cloths.R;
import com.men_cloths.adapter.WaitpayAdapter;
import com.men_cloths.mainContent.DingDanActivity;
import com.men_cloths.model.Waitpay;
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
public class WaitPayFragment extends Fragment {

    ListView listView;
    List<Waitpay> lists = new ArrayList<>();
    WaitpayAdapter waitpayAdapter;

    private final String URL_WAIT_PAY="http://139.199.196.199/index.php/home/index/getord?type=0&tel=";
    private Context context;
    public static final int SUCCESS=123;
    public static final int ERROR=-123;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dingdan_daifukuan,null);
        listView = (ListView) view.findViewById(R.id.wait_all_listview);
        context=getContext();
        DingDanActivity.handler3=handler;
        getDate();
        return view;
    }






    public void getDate(){
        Log.i("hhh","调用1");
        HttpTools tools=new HttpTools(URL_WAIT_PAY+ UserInfo.getTel(context)) {
            @Override
            public void requestSuccess(String line) {
                try {
                    JSONObject object=new JSONObject(line);
                    if(object.getString("code").equals("1")){
                        lists.clear();
                        JSONArray array=object.getJSONArray("ord");
                        for(int i=0;i<array.length();i++){
                            object=array.getJSONObject(i);
                            Waitpay waitpay=new Waitpay();
                            waitpay.setPicture(object.getString("url"));
                            waitpay.setName(object.getString("name"));
                            waitpay.setColor(object.optString("color","黑色"));
                            waitpay.setSize(object.optString("dem","XLL"));
                            waitpay.setPrice(object.optString("price","999"));
                            waitpay.setId(object.getInt("id"));
                            lists.add(waitpay);
                        }
                        DingDanActivity.handler3.sendEmptyMessage(SUCCESS);
                    }else {
                        DingDanActivity.handler3.sendEmptyMessage(ERROR);
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

    public Handler handler=new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case SUCCESS:
                    if(listView.getAdapter()==null){
                        waitpayAdapter=new WaitpayAdapter(context,lists) {
                            @Override
                            public void pay(String name, String price, int id, int position) {
                                OrderTool tool=OrderTool.getTool(context, DingDanActivity.handler3);
                                tool.createPayAlert(name,price,id);
                            }
                        };
                        listView.setAdapter(waitpayAdapter);
                    }else {
                        waitpayAdapter.notifyDataSetChanged();
                    }
                    break;
                case ERROR:
                    Toast.makeText(context,"获取已付款订单失败",Toast.LENGTH_SHORT).show();
                    break;
                case OrderTool.PAY_SUCESS:
                    Toast.makeText(context,"付款成功",Toast.LENGTH_SHORT).show();
                    DingDanActivity.ischangePay=true;
                    OrderTool.destory();
                    getDate();
                    break;
                case OrderTool.PAY_ERROR:
                    Toast.makeText(context,"付款失败",Toast.LENGTH_SHORT).show();
                    OrderTool.destory();
                    break;
            }
        }
    };
}
