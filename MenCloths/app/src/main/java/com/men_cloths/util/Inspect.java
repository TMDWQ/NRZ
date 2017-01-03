package com.men_cloths.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/1/2.
 */

public class Inspect{

    public static boolean isHaveEmptyForList(List list){
        for(Object i:list){
            if(i==null){
                return true;
            }
        }
        return false;
    }
    public static boolean isALLEmptyForList(List list){
        for(Object i:list){
            if(i!=null){
                return false;
            }
        }
        return true;
    }
    public static List<Integer>  emptyList(List list){
        List<Integer> integers=new ArrayList<>();
        int n=0;
        for(Object i:list){
            if(i==null){
                integers.add(n);
            }
            n++;
        }
        return integers;
    }

    public static List<Integer>  notEmptyList(List list){
        List<Integer> integers=new ArrayList<>();
        int n=0;
        for(Object i:list){
            if(i!=null){
                integers.add(n);
            }
            n++;
        }
        return integers;
    }


    public static List<Object>  notEmptyObjects(List list){
        List<Object> integers=new ArrayList<>();
        for(Object i:list){
            if(i!=null){
                integers.add(i);
            }
        }
        return integers;
    }


}
