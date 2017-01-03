package com.men_cloths.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2016/12/30.
 */

public class ShopInfomation{
    public static Shop shop;

    public static void save(String name, String color, String size, String price, String info, Context context){
        SharedPreferences preferences=context.getSharedPreferences("shop_info_",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=preferences.edit();
        editor.putString("name",name);
        editor.putString("color",color);
        editor.putString("price",price);
        editor.putString("info",info);
        editor.putString("size",size);
        editor.commit();
    }
    public static Shop get(Context context){
        SharedPreferences preferences=context.getSharedPreferences("shop_info_",Context.MODE_PRIVATE);
        //String name, String color, String price, String size, String info
        String name=preferences.getString("name","");
        String color=preferences.getString("color","");
        String price=preferences.getString("price","");
        String size=preferences.getString("size","");
        String info=preferences.getString("info","");
        Shop shop=new Shop(name,color,price,size,info);
        return shop;
    }


    public static class Shop{
        private String name;
        private String color;
        private String price;
        private String size;
        private String info;

        public Shop(String name, String color, String price, String size, String info) {
            this.name = name;
            this.color = color;
            this.price = price;
            this.size = size;
            this.info = info;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getColor() {
            return color;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }
    }

}
