package com.men_cloths.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.men_cloths.R;
import com.men_cloths.model.ActivityManager;
import com.men_cloths.model.LoadImage;
import com.men_cloths.model.Waitget;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/11/28.
 */
public abstract class WaitgetAdapter extends BaseAdapter{
    List<Waitget> waitgetList = new ArrayList<>();
    Context context;
    LayoutInflater inflater;
    public WaitgetAdapter(Context context, List<Waitget> waitgetList){
        this.context = context;
        this.waitgetList = waitgetList;
        inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return waitgetList.size();
    }

    @Override
    public Object getItem(int position) {
        return waitgetList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    TextView deleteOrder,checkWuliu;
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView==null){
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.order_waitget_item,null);
            viewHolder.goodsPicture = (ImageView) convertView.findViewById(R.id.goods_picture);
            viewHolder.goodsName = (TextView) convertView.findViewById(R.id.goods_name);
            viewHolder.goodsColor = (TextView) convertView.findViewById(R.id.goods_color);
            viewHolder.goodsSize = (TextView) convertView.findViewById(R.id.goods_size);
            viewHolder.goodsPrice = (TextView) convertView.findViewById(R.id.goods_price);
            convertView.setTag(viewHolder);
            deleteOrder = (TextView) convertView.findViewById(R.id.delete_order);
            checkWuliu = (TextView) convertView.findViewById(R.id.check_wuliu);
            ViewHolder viewHolder1 = viewHolder;
            deleteOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 deleteOrder(waitgetList.get(position).getId(),position);
                }
            });
            checkWuliu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    scanOrder(waitgetList.get(position).getId(),position);
                }
            });
        }
        viewHolder = (ViewHolder) convertView.getTag();
        Waitget waitget = waitgetList.get(position);
        //LoadImage.load(viewHolder.goodsPicture,waitgetList.get(position).getPicture(),context);
        if(ActivityManager.memoryCache.get(waitgetList.get(position).getPicture())==null) {
            LoadImage.load(viewHolder.goodsPicture, waitgetList.get(position).getPicture(), context);
        }else {
            viewHolder.goodsPicture.setImageBitmap(ActivityManager.memoryCache.get(waitgetList.get(position).getPicture()));
        }
        viewHolder.goodsName.setText(waitget.getName());
        viewHolder.goodsColor.setText(waitget.getColor());
        viewHolder.goodsSize.setText(waitget.getSize());
        viewHolder.goodsPrice.setText(waitget.getPrice());

        return convertView;
    }
    public class ViewHolder{
        ImageView goodsPicture;
        TextView goodsName,goodsColor,goodsSize,goodsPrice;
    }

    public abstract void deleteOrder(int id,int position);
    public abstract void scanOrder(int id,int position);
}
