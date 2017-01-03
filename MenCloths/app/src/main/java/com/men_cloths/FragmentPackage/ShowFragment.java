package com.men_cloths.FragmentPackage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.men_cloths.R;
import com.men_cloths.adapter.ShowAdapter;
import com.men_cloths.mainContent.HomeActivity;
import com.men_cloths.mainContent.ShopInfo;
import com.men_cloths.model.Show;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/1.
 */
public class ShowFragment extends Fragment{
    private ListView listView;
    private HomeActivity activity;
    String url="http://139.199.196.199/index.php/home/index/show";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.listview_have_divider,null);
        listView= (ListView) view.findViewById(R.id.listview_have_divider);
        new MyAsyncTask().execute(url);

        activity= (HomeActivity) getActivity();

       if(activity.loaded.getAlerDialog()==null){
           activity.loaded.createAlert();
       }else {
           if(!activity.loaded.isShow()){
               activity.loaded.show();
           }
       }

        return view;
    }
   private class MyAsyncTask extends AsyncTask<String,Integer,String>{
       @Override
       protected void onPostExecute(String s) {
           if(s!=null){
               try {
                   List<Show> showList=new ArrayList<>();
                   JSONObject jsonObject=new JSONObject(s);
                   JSONArray jsonArray=jsonObject.getJSONArray("data");
                   for(int i=0;i<jsonArray.length();i++){
                       JSONObject object=jsonArray.getJSONObject(i);
                       Show show=new Show();
                       show.setTitleImg(object.getString("wholeimg"));
                       show.setPartImg1(object.getString("partimg1"));
                       show.setPartImg2(object.getString("partimg2"));
                       show.setNickName(object.getString("nickname"));
                       show.setMood(object.getString("mood"));
                       showList.add(show);
                   }
                   ShowAdapter showAdapter= new ShowAdapter(getActivity(), showList) {
                       @Override
                       public void click1(String url) {
                           Intent intent=new Intent(getActivity(), ShopInfo.class);
                           intent.putExtra("pic",url);
                           getActivity().startActivity(intent);
                       }

                       @Override
                       public void click2(String url) {
                           Intent intent=new Intent(getActivity(), ShopInfo.class);
                           intent.putExtra("pic",url);
                           getActivity().startActivity(intent);
                       }

                       @Override
                       public void click3(String url) {
                           Intent intent=new Intent(getActivity(), ShopInfo.class);
                           intent.putExtra("pic",url);
                           getActivity().startActivity(intent);
                       }
                   };
                   listView.setAdapter(showAdapter);
                   activity.loaded.dismiss();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }


           super.onPostExecute(s);
       }

       @Override
       protected String doInBackground(String... params) {
           StringBuilder stringBuilder=new StringBuilder();//可序列
           InputStream is=null;
           try {
               URL url=new URL(params[0]);
               HttpURLConnection httpURLConnection= (HttpURLConnection) url.openConnection();
               httpURLConnection.setRequestMethod("GET");
               httpURLConnection.setConnectTimeout(5000);
               httpURLConnection.connect();//建立本次网络请求的连接
               if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_OK){
                   is=httpURLConnection.getInputStream();
                   BufferedReader bufferedReader=new BufferedReader
                           (new InputStreamReader(is,"utf-8"));
                   String s;
                   while ((s=bufferedReader.readLine())!=null){
                       stringBuilder.append(s);
                   }
                   return stringBuilder.toString();
               }
           } catch (MalformedURLException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }
           finally {
               try {
                   if(is!=null)
                       is.close();
               } catch (IOException e) {
                   e.printStackTrace();
               }
           }
           return null;
       }
   }

}
