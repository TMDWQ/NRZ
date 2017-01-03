package com.men_cloths.FragmentPackage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;

import com.men_cloths.R;
import com.men_cloths.adapter.NewProductAdapter;
import com.men_cloths.mainContent.HomeActivity;
import com.men_cloths.mainContent.ShopInfo;
import com.men_cloths.model.NewProduct;

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
public class NewProductFragment extends Fragment{
    ListView listView;
    NewProduct newProduct;
    List<NewProduct> newProductList;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.listview_no_divider,null);
        listView= (ListView) view.findViewById(R.id.listview_no_divider);
        startAsyncTask();
        ImageView imageView=new ImageView(getActivity());
        AbsListView.LayoutParams layoutParams=new  AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,  AbsListView.LayoutParams.WRAP_CONTENT
        );
        imageView.setLayoutParams(layoutParams);
        imageView.setImageResource(R.mipmap.new_top);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        listView.addHeaderView(imageView);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                newProduct= (NewProduct) parent.getItemAtPosition(position);
//                ImageView imageView1= (ImageView) view.findViewById(R.id.new_img1);
//                ImageView imageView2= (ImageView) view.findViewById(R.id.new_img2);
//                pos=position+1;
//                if(imageView1!=null)
//                imageView1.setOnClickListener(onClickListener);
//                if(imageView2!=null)
//                imageView2.setOnClickListener(onClickListener);
//            }
//        });
        if(HomeActivity.loaded.getAlerDialog()==null){
            HomeActivity.loaded.createAlert();
        }else {
            if(!HomeActivity.loaded.isShow()){
                HomeActivity.loaded.show();
            }
        }
        return view;
    }

    public class MyAsyncTask extends AsyncTask<String,Integer,String> {

        @Override
        protected void onPreExecute() {
            //在execute(Params... params)被调用后立即执行，一般用来在执行后台任务前对UI做一些标记
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {//执行UI操作
            if(s!=null){
                try {
                    newProductList=new ArrayList<>();
                    JSONObject jsonObject=new JSONObject(s);
                    JSONArray jsonArray=jsonObject.getJSONArray("data");
                    for(int i=0;i<jsonArray.length();i++){
                        if(i%2==0) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            NewProduct newProduct=new NewProduct();
                            newProduct.setContent1(obj.getString("name"));
                            newProduct.setImg1(obj.getString("img"));
                            newProduct.setPrice1(obj.getDouble("price"));
                            JSONObject object = jsonArray.getJSONObject(i + 1);
                            newProduct.setContent2(object.getString("name"));
                            newProduct.setImg2(object.getString("img"));
                            newProduct.setPrice2(object.getDouble("price"));
                            newProductList.add(newProduct);
                        }
                    }
                    NewProductAdapter newProductAdapter= new NewProductAdapter(getActivity(), newProductList) {
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
                    };
                    listView.setAdapter(newProductAdapter);
                    HomeActivity.loaded.dismiss();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            super.onPostExecute(s);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            //更新类似于进度条的控件的进度效果
            super.onProgressUpdate(values);
        }

        @Override//所有的耗时操作都在此时进行，不能进行UI操作
        protected String doInBackground(String... params) {//在后台运行的回调方法
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
    public void startAsyncTask(){
        MyAsyncTask myAsyncTask=new MyAsyncTask();
        String httpUrl="http://139.199.196.199/index.php/home/index/new_product";
        myAsyncTask.execute(httpUrl);
    }
}
