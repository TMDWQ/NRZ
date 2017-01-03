package com.men_cloths.util;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.men_cloths.R;

/**
 * Created by Administrator on 2016/12/29.
 */

public class BeingLoaded{
    private ImageView imageView;
    private Context context;
    private LayoutInflater inflater;
    private  AlertDialog alertDialog;

    public BeingLoaded(Context context) {
        this.context = context;
        inflater=LayoutInflater.from(context);
    }

    public void createAlert(){
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
       // builder.setIcon(R.mipmap.other);
        View view=inflater.inflate(R.layout.jiazai,null);
        imageView= (ImageView) view.findViewById(R.id.image);
        builder.setView(view);
//        builder.setPositiveButton("来吧宝贝", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                Toast.makeText(context,"哈哈哈，你这个哈马屁，被骗了吧",Toast.LENGTH_SHORT).show();
//            }
//        });
//        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        });
//        builder.setNeutralButton("退出", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }});

         alertDialog=builder.create();
         alertDialog.show();
        alertDialog.setCancelable(false);
        alertDialog.getWindow().setLayout(250,300);
        imageView.setBackground(context.getResources().getDrawable(R.drawable.upload));
        AnimationDrawable drawable= (AnimationDrawable) imageView.getBackground();

        drawable.start();

    }

    public void dismiss(){
        alertDialog.dismiss();
    }

    public void show(){
        alertDialog.show();
    }

    public boolean isShow(){
        return alertDialog.isShowing();
    }

    public AlertDialog getAlerDialog(){
        return alertDialog;
    }



}
