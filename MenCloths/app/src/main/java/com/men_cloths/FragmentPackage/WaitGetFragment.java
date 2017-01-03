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
import com.men_cloths.adapter.WaitgetAdapter;
import com.men_cloths.mainContent.DingDanActivity;
import com.men_cloths.model.Waitget;
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
public class WaitGetFragment extends Fragment{
    ListView listView;
    List<Waitget> lists = new ArrayList<>();
    private WaitgetAdapter waitgetAdapter;

    private final String URL_GET="http://139.199.196.199/index.php/home/index/getord?type=1&tel=";
    public static final int SUCCESS=456;
    public static final int ERROR=-456;
    private Context context;
    private   OrderTool tool;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dingdan_daifukuan,null);
        listView = (ListView) view.findViewById(R.id.wait_all_listview);
        context=getContext();
        DingDanActivity.handle1=handler;
        getDate();
        return view;
    }

   public  void getDate(){
       Log.i("hhh",URL_GET+ UserInfo.getTel(context));
        HttpTools tools=new HttpTools(URL_GET+ UserInfo.getTel(context)) {
            @Override
            public void requestSuccess(String line) {
                try {
                    JSONObject object=new JSONObject(line);

                    if(object.getString("code").equals("1")){
                        lists.clear();
                        JSONArray array=object.getJSONArray("ord");
                        for(int i=0;i<array.length();i++){
                            Waitget waitget=new Waitget();
                            object=array.getJSONObject(i);
                            waitget.setId(object.getInt("id"));
                            waitget.setPicture(object.getString("url"));
                            waitget.setSize(object.getString("dem"));
                            waitget.setPrice(object.getString("price"));
                            waitget.setColor(object.getString("color"));
                            lists.add(waitget);
                            Log.i("hhh",object.optString("dem","XLL")+object.optString("color","黑色")+"sss");
                        }
                        DingDanActivity.handle1.sendEmptyMessage(SUCCESS);
                    }else {
                        DingDanActivity.handle1.sendEmptyMessage(ERROR);
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
                    if(listView.getAdapter()==null){
                        waitgetAdapter=new WaitgetAdapter(context,lists) {
                            @Override
                            public void deleteOrder(int id, int position) {
                                OrderTool tool=OrderTool.getTool(context,DingDanActivity.handle1);
                                tool.createDeleteAlert(id);
                            }

                            @Override
                            public void scanOrder(int id, int position) {

                            }
                        };
                        listView.setAdapter(waitgetAdapter);

                    }else {
                        waitgetAdapter.notifyDataSetChanged();
                    }

                    break;
                case ERROR:
                    break;

                case OrderTool.DELETE_ERROR:
                    Toast.makeText(context,"删除失败",Toast.LENGTH_SHORT).show();
                    break;
                case OrderTool.DELETE_SUCESS:
                    getDate();
                    DingDanActivity.ischangedGet=true;
                    Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();
                    OrderTool.destory();
                    break;

            }
        }
   };

}
