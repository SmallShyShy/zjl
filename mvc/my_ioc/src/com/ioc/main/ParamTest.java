package com.ioc.main;

import com.ioc.annotation.RequestMapping;
import com.ioc.utils.ParamUtil;
import com.ioc.annotation.RequestParam;
import com.weguard.controller.RBuildingZoneAppController;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class ParamTest {
    public void test(@RequestParam("x")String str, @RequestParam("y")int x,@RequestParam("z")String s){
        System.out.println(str+","+x);
    }
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
//        Class clazz=ParamTest.class;
//        Method m=clazz.getDeclaredMethod("test", String.class, int.class,String.class);
//        List<String> map=ParamUtil.getParamNameAndTypeByAnnotation(m);
//        System.out.println(map.toString());
//        RBuildingZoneAppController r=new RBuildingZoneAppController();
//        Class clazz=r.getClass();
//        Object[] param=new Object[1];
//        param[0]="hh";
//
//        Method[] m=clazz.getMethods();
//        String s="";
//        for(Method me:m){
//            if(!me.isAnnotationPresent(RequestMapping.class)){
//                continue;
//            }
//            s= (String) me.invoke(clazz.newInstance(),param);
//        }
//
//        System.out.println(s);
        Map<String,Object> map=new HashMap<>();
        map.put("1",new Date());
        map.put("2","haha");
        for(Map.Entry<String,Object> e:map.entrySet()){
            e.getValue();
        }
    }
}

