package com.morandror.scclient.utils;

import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.DateFormat;

public class JsonHandler {
    private Gson gson;
    public static JsonHandler instance;
    private final static Object key = new Object();

    private JsonHandler() {
        this.gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss.SSS").create();
    }

    public static JsonHandler getInstance() {
        if (instance == null){
            synchronized (key){
                if (instance == null){
                    instance = new JsonHandler();
                }
            }
        }

        return instance;
    }

    public Object fromString(String str, Class cls){
        return gson.fromJson(str, cls);
    }

    public String toString(Object obj){
        return gson.toJson(obj);
    }
}
