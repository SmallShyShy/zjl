package com.ioc.utils;

import com.ioc.annotation.RequestParam;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class ParamUtil {
    public static List<String> getParamNameAndTypeByAnnotation(Method method){
        List<String> map=new ArrayList<>();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
      //  Class<?>[] parameterTypes =new Class[parameterAnnotations.length];
        Parameter[] parameters = method.getParameters();
        int z=0;
        for(int j=0;j<parameters.length;j++){
            if(parameters[j].isAnnotationPresent(RequestParam.class)){
                Annotation[] a=parameterAnnotations[z];
                for(Annotation annotation:a) {
                    if (annotation instanceof RequestParam) {
                        RequestParam param = (RequestParam) annotation;
                        //当前参数有注释 则获取参数的返回值作为key
                        //当前参数有注释 则获取注释的value 作为value;
                        String list=parameters[j].getType().getSimpleName()+":"+param.value();
                        map.add(list);
                    }
                }
                //有几个参数  parameterAnnotations就有几个 没有注解的parameterAnnotations[i] 长度为0
                z++;
            }else{
                //有几个参数  parameterAnnotations就有几个 没有注解的parameterAnnotations[i] 长度为0
                z++;
                continue;
            }

        }
        return map;
//        if(parameterAnnotations==null||parameterAnnotations.length==0){
//            return null;
//        }
//        String[] paramNames=new String[parameterAnnotations.length];
//        int i=0;
//        for(Annotation[] annotations:parameterAnnotations){
//            for(Annotation annotation:annotations){
//                if(annotation instanceof RequestParam){
//                    RequestParam param=(RequestParam) annotation;
//                    paramNames[i++]=param.value();
//                }
//            }
//        }
//        return paramNames;
    }
}
