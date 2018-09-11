package com.weguard.controller;

import java.lang.reflect.Method;
import java.util.List;

public class ListThreadTest implements Runnable{
    private List<?> list;
    public ListThreadTest(List<?> list){
        this.list=list;
    }
    @Override
    public void run() {
        list.remove(1);
        for(int i=0;i<list.size();i++){
            System.out.println(Thread.currentThread().getName()+":"+list.get(i));
        }

    }
}
