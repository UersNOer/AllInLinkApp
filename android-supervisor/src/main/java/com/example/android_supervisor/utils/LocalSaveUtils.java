package com.example.android_supervisor.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;

/**
 * Created by yj on 2019/8/31 0010.
 */

public class LocalSaveUtils {
    private Context context;
    public static final String ConfigName="config.txt";
    private static LocalSaveUtils instance;

//    private LocalSaveUtils(Context context){
//        this.context = context;
//    }

    public static void saveString(Context context,String key,String value){
        SharedPreferences.Editor editor=context.getSharedPreferences(ConfigName, Context.MODE_PRIVATE)
                .edit();
        editor.putString(key,value);
        editor.commit();
    }

    public static String getString(Context context,String key,String defValue){
        SharedPreferences sharedPreferences =context.getSharedPreferences(ConfigName, Context.MODE_PRIVATE);

        String value=sharedPreferences.getString(key,defValue);
        return value;
    }


    public static void saveBoolean(Context context,String key,boolean value){
        SharedPreferences.Editor editor=context.getSharedPreferences(ConfigName, Context.MODE_PRIVATE)
                .edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getBoolean(Context context,String key,boolean defValue){
        SharedPreferences sharedPreferences =context.getSharedPreferences(ConfigName, Context.MODE_PRIVATE);

        boolean value=sharedPreferences.getBoolean(key,defValue);
        return value;
    }

    public static void clean(Context context){
        SharedPreferences.Editor editor=context.getSharedPreferences(ConfigName, Context.MODE_PRIVATE)
                .edit();
        editor.clear();
        editor.apply();
    }




    public static Object readObject(String file) throws StreamCorruptedException, FileNotFoundException, IOException, ClassNotFoundException
    {
        Object obj = null;
        ObjectInputStream objIn = new ObjectInputStream(new FileInputStream(file));
        obj = objIn.readObject();
        objIn.close();
        return obj;
    }

    public static void writeObject(Object obj,String file) throws FileNotFoundException, IOException
    {
        ObjectOutputStream objOut = new ObjectOutputStream(new FileOutputStream(file));
        objOut.writeObject(obj);
        objOut.flush();
        objOut.close();
    }






}
