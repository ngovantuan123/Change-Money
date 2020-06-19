package com.tuangh.change_money;

import java.util.ArrayList;

public class SingleTon {
    private static SingleTon instance=null;

    public static ArrayList<Currency> currencies=new ArrayList<>();

    private SingleTon() {
        currencies = new ArrayList<>();
    }
    public static SingleTon getInstance(){
        if(instance==null){
            instance = new SingleTon();
        }
        return instance;
    }
}
