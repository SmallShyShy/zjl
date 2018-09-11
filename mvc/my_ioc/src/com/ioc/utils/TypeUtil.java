package com.ioc.utils;

import java.util.Date;

public class TypeUtil {
    public static Object getType(String type){
        switch (type){
            case "String":
                return String.class;

            case "Date":
                return Date.class;
            case "int":
                return int.class;

            case "Integer":
                return Integer.class;

            case "boolean":
                return boolean.class;
            default:
                return null;
        }
    }
}
