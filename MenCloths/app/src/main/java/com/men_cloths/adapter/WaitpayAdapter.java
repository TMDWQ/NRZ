package com.men_cloths.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.men_cloths.R;
import com.men_cloths.model.ActivityManager;
import com.men_cloths.model.LoadImage;
import com.men_cloths.model.Waitpay;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public abstract class WaitpayAdapter extends BaseAdapter {

    List<Waitpay> waitpayList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    CheckOrder checkOrder;


    public WaitpayAdapter(Context context,List<Waitpay> waitpayList){
        this.context = context;
        this.waitpayList = waitpayList;
        inflater = LayoutInflater.from(context);
    }
    //自定义接口写item上的点击事件
    public interface CheckOrder{
        public void check(String name);
    }
    public void setCheckOrder(CheckOrder checkOrder){
        this.checkOrder = checkOrder;
    }
    @Override
    public int getCount() {
        return waitpayList.size();
    }

    @Override
    public Object getItem(int position) {
        return waitpayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    TextView check,pay;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            convertView = inflater.inflate(R.layout.order_waitpay_item,null);
            check = (TextView) convertView.findViewById(R.id.check_order);
            pay = (TextView) convertView.findViewById(R.id.go_pay);
            viewHolder = new ViewHolder();
            viewHolder.picture = (ImageView) convertView.findViewById(R.id.goods_picture);
            viewHolder.name = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.color = (TextView) convertView.findViewById(R.id.goods_color);
            viewHolder.size = (TextView) convertView.findViewById(R.id.goods_size);
            viewHolder.price = (TextView) convertView.findViewById(R.id.goods_price);

//            final ViewHolder finalViewHolder = viewHolder;
//            check.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    String str = finalViewHolder.name.getText().toString();
//                    checkOrder.check(str);
//                }
//            });


            convertView.setTag(viewHolder);
        }
        viewHolder = (ViewHolder) convertView.getTag();
        Waitpay waitpay = waitpayList.get(position);
       // LoadImage.load(viewHolder.picture,waitpayList.get(position).getPicture(),context);
        if(ActivityManager.memoryCache.get(waitpayList.get(position).getPicture())==null) {
            LoadImage.load(viewHolder.picture, waitpayList.get(position).getPicture(), context);
        }else {
            viewHolder.picture.setImageBitmap(ActivityManager.memoryCache.get(waitpayList.get(position).getPicture()));
        }

        viewHolder.name.setText(waitpay.getName());
        viewHolder.color.setText(waitpay.getColor());
        viewHolder.size.setText(waitpay.getSize());
        viewHolder.price.setText("￥"+waitpay.getPrice());
        pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(waitpayList.get(position).getName(),waitpayList.get(position).getPrice(),waitpayList.get(position).getId(),position);
            }
        });

        return convertView;
    }
    public class ViewHolder{
        ImageView picture;
        TextView name,color,size,price;
    }

    public abstract void pay(String name,String price,int id,int position);

}
