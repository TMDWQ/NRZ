package com.men_cloths.mainContent;


import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.men_cloths.R;
import com.men_cloths.model.ActivityManager;
import com.men_cloths.model.Commodity;
import com.men_cloths.model.LoadImage;
import com.men_cloths.util.HttpTools;
import com.men_cloths.util.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
/**
 * //还需要删除远程数据库的内容
 *
 * */
public class Collection extends Activity{
	
	private ListView listView;
	private List<Commodity> list;
	private ImageView imageView;
	private BaseAdapter baseAdapter;
	private int position;
	private LayoutInflater inflater;
	private static final String URL_COLLECT="http://139.199.196.199/index.php/home/index/getsc?tel=";
	public static  final  int GET_COLLECTION_SUCCESS=1;
	public static  final  int GET_COLLECTION_ERROR=-1;



	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.collect);
		init();
		listView=(ListView) findViewById(R.id.collect_list_view);
		getscList();
	}

	public void init(){
		imageView= (ImageView) findViewById(R.id.back);
		list=new ArrayList<>();
		inflater=LayoutInflater.from(this);
	}



	public void getscList(){
		HttpTools tools=new HttpTools(URL_COLLECT+ UserInfo.getTel(this)) {
			@Override
			public void requestSuccess(String line) {
				try {
					JSONObject object=new JSONObject(line);
					if(object.getString("code").equals(1)){
						list.clear();
						JSONArray array=object.getJSONArray("info");
						for(int i=0;i<array.length();i++){
							Commodity commodity=new Commodity();
							object=array.getJSONObject(i);
							commodity.setName(object.getString("name"));
							commodity.setPrice(object.getString("price"));
							commodity.setIamge(object.getString("url"));
							commodity.setId(object.getString("id"));
							commodity.setRule(object.getString("size"));
							commodity.setColor(object.getString("color"));
							list.add(commodity);

						}

						handler.sendEmptyMessage(GET_COLLECTION_SUCCESS);
					}else {

						handler.sendEmptyMessage(GET_COLLECTION_ERROR);
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

	private Handler handler=new Handler(){

		public void handleMessage(Message message){
			switch (message.what){
				case GET_COLLECTION_SUCCESS:
					createAdapter();
					listView.setAdapter(baseAdapter);
					break;
				case GET_COLLECTION_ERROR:
					Toast.makeText(Collection.this,"你没有收藏任何商品",Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

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
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewHolder viewHolder;
				if(convertView==null){
					convertView=inflater.inflate(R.layout.collect_list_item,null);
					viewHolder=new ViewHolder();
					viewHolder.imageCollect= (ImageView) convertView.findViewById(R.id.image_collect);
					viewHolder.price= (TextView) convertView.findViewById(R.id.price);
					viewHolder.title= (TextView) convertView.findViewById(R.id.title);
					viewHolder.size= (TextView) convertView.findViewById(R.id.size);
					convertView.setTag(viewHolder);
				}else{
					viewHolder= (ViewHolder) convertView.getTag();
				}

				String url=list.get(position).getIamge();
				if(ActivityManager.memoryCache.get(url)!=null){
					viewHolder.imageCollect.setImageBitmap(ActivityManager.memoryCache.get(url));
				}else {
					LoadImage.load(viewHolder.imageCollect,url,Collection.this);
				}
				viewHolder.title.setText(list.get(position).getPrice());
				String size="颜色：" +list.get(position).getColor()+ " 尺码："+list.get(position).getRule();
				viewHolder.size.setText(size);
				viewHolder.price.setText(list.get(position).getPrice());

				return convertView;
			}
		};
	}

	private class ViewHolder{
		ImageView imageCollect;
		TextView title,size,price;
			//颜色：白色 尺码：XL
			//178.00
	}


}
