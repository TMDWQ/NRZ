package com.men_cloths.util;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.men_cloths.R;
import com.men_cloths.mainContent.ThreadInfo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Created by Administrator on 2017/1/1.
 */

public class OrderTool{

    public static OrderTool orderTool;
    private static AlertDialog delete;
    private static AlertDialog pay;
    private Context context;
    public static final int DELETE_SUCESS=111;
    public static final int DELETE_ERROR=-111;
    public static final int PAY_SUCESS=222;
    public static final int PAY_ERROR=-222;
    public static final int PAY_SUCESS_=333;
    private Handler handler;
    private int position;
    boolean bb=false;
    private String name;
    private OrderTool(){}
    public static OrderTool getTool(Context context,Handler handler){
        if(orderTool==null){
            orderTool=new OrderTool();
            orderTool.context=context;
            orderTool.handler=handler;
            return orderTool;
        }
        return orderTool;
    }

    public void createDeleteAlert(final int ordId){
        LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.delete_order,null);
        Button cancle,ok;
        cancle= (Button) view.findViewById(R.id.cancel);
        ok= (Button) view.findViewById(R.id.ok);
        AlertDialog.Builder builder=new AlertDialog.Builder(context);
        builder.setView(view);
        delete=builder.create();
        delete.show();
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete.dismiss();
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(ordId);
                delete.dismiss();
            }
        });

    }

    public AlertDialog getDelete() {
        return delete;
    }

    public void delete(int id){
        HttpTools tools=new HttpTools("http://139.199.196.199/index.php/home/index/deleteord?id="+id) {
            @Override
            public void requestSuccess(String line) {

                try {
                    JSONObject object = new JSONObject(line);
                    if(object.getString("code").equals("1")){
                        handler.sendEmptyMessage(DELETE_SUCESS);
                    }else {
                        handler.sendEmptyMessage(DELETE_ERROR);
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
    public void createPayAlert(String names, String prices, final int id){
        name=names;
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.zi_fu_bao,null);
        builder.setView(view);
        pay=builder.create();
        pay.show();
        TextView name,price,zhangHao;
        name= (TextView) view.findViewById(R.id.type);
        price= (TextView) view.findViewById(R.id.price);
        zhangHao= (TextView) view.findViewById(R.id.zhang_hao);
        ImageView x= (ImageView) view.findViewById(R.id.destory);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay.dismiss();
            }
        });
        name.setText(names);
        price.setText(prices+"元");
        zhangHao.setText(hideTel(UserInfo.getTel(context)));
        Button button= (Button) view.findViewById(R.id.pay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=0;
                pay.dismiss();
                v=inflater.inflate(R.layout.zi_fu,null);
                builder.setView(v);
                pay=builder.create();
                ImageView back= (ImageView) v.findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay.dismiss();
                    }
                });
                pay.show();
                final TextView []ts=new TextView[6];
                Button [] bs=new Button[11];
                int [] tsId={R.id.txt1,R.id.txt2,R.id.txt3,R.id.txt4,R.id.txt5,R.id.txt6};
                int [] bsId={R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,
                        R.id.button7,R.id.button8,R.id.button9,R.id.button0,R.id.button_x
                };
                for(int i=0;i<ts.length;i++){
                    ts[i]= (TextView) v.findViewById(tsId[i]);
                }
                for(int i=0;i<bs.length-1;i++){
                    bs[i]= (Button) v.findViewById(bsId[i]);
                    bs[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b= (Button) v;
                            ts[position++].setText(b.getText());
                            if(position==6){
                                //支付
                                payKuan(id);
                                pay.dismiss();
                            }
                        }
                    });
                }
                bs[10]= (Button) v.findViewById(bsId[10]);
                bs[10].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position>0) {
                            ts[--position].setText("");
                        }
                    }
                });

            }
        });


    }




    public void createPayAlert(final String names, final String prices, final String url){

        name=names;
        final AlertDialog.Builder builder=new AlertDialog.Builder(context);
        final LayoutInflater inflater=LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.zi_fu_bao,null);
        builder.setView(view);
        pay=builder.create();
        pay.show();
        final TextView name,price,zhangHao;
        name= (TextView) view.findViewById(R.id.type);
        price= (TextView) view.findViewById(R.id.price);
        zhangHao= (TextView) view.findViewById(R.id.zhang_hao);
        ImageView x= (ImageView) view.findViewById(R.id.destory);
        x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay.dismiss();
            }
        });
        name.setText(names);
        price.setText(prices+"元");
        zhangHao.setText(hideTel(UserInfo.getTel(context)));
        Button button= (Button) view.findViewById(R.id.pay);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                position=0;
                pay.dismiss();
                v=inflater.inflate(R.layout.zi_fu,null);
                builder.setView(v);
                pay=builder.create();
                ImageView back= (ImageView) v.findViewById(R.id.back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pay.dismiss();
                    }
                });
                pay.show();
                final TextView []ts=new TextView[6];
                Button [] bs=new Button[11];
                int [] tsId={R.id.txt1,R.id.txt2,R.id.txt3,R.id.txt4,R.id.txt5,R.id.txt6};
                int [] bsId={R.id.button1,R.id.button2,R.id.button3,R.id.button4,R.id.button5,R.id.button6,
                        R.id.button7,R.id.button8,R.id.button9,R.id.button0,R.id.button_x
                };
                for(int i=0;i<ts.length;i++){
                    ts[i]= (TextView) v.findViewById(tsId[i]);
                }
                for(int i=0;i<bs.length-1;i++){
                    bs[i]= (Button) v.findViewById(bsId[i]);
                    bs[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Button b= (Button) v;
                            ts[position++].setText(b.getText());
                            if(position==6){
                                Message message=new Message();
                                message.what=PAY_SUCESS_;
                                ThreadInfo.MyInfo info=new ThreadInfo.MyInfo(url,names,prices);
;                               message.obj=info;
                                handler.sendMessage(message);
                                pay.dismiss();
                            }
                        }
                    });
                }
                bs[10]= (Button) v.findViewById(bsId[10]);
                bs[10].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(position>0) {
                            ts[--position].setText("");
                        }
                    }
                });

            }
        });



    }





    public void payKuan(int id){
        String name=null;
        String color= null;
        try {
            name=URLEncoder.encode(this.name,"utf-8");
            color = URLEncoder.encode("黑色","utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String size="XLL";
        HttpTools tools=new HttpTools("http://139.199.196.199/index.php/home/index/changeord?state=1&id="+id+"&name="+name+"&color="+color+"&size="+size) {
            @Override
            public void requestSuccess(String line) {
                try {
                    JSONObject object=new JSONObject(line);
                    if(object.getString("code").equals("1")){
                        handler.sendEmptyMessage(PAY_SUCESS);
                    }else {
                        handler.sendEmptyMessage(PAY_ERROR);
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

    public String hideTel(String tel){
        String t="";
        for(int i=0;i<tel.length();i++){
            if(i>=3&&i<=7){
                t+="*";
            }else {
                t+=tel.charAt(i);
            }
        }
        return t;
    }

    public static void destory(){
        if(delete!=null) {
            if(delete.isShowing())
            delete.dismiss();
        }
        if(pay!=null){
            if (pay.isShowing())
            pay.dismiss();
        }
    }




}
