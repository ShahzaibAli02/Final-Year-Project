package com.example.digitalshop;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.digitalshop.Model.User;
import com.example.digitalshop.Utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class SharedPref
{

    public  static  void saveUser(Context context, User user)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.digitalshop",Context.MODE_PRIVATE);
        String json=new Gson().toJson(user);
        sharedPreferences.edit().putString(Constants.DB_USERS ,json).apply();
    }



    public  static  void saveFavourite(Context context,String id,boolean isfvrt)
    {
        User user = getUser(context);
        SharedPreferences sharedPreferences=context.getSharedPreferences(user.getEmail(),Context.MODE_PRIVATE);
        sharedPreferences.edit().putBoolean(id ,isfvrt).apply();
    }

    public  static  boolean isFavorite(Context context,String id)
    {
        User user = getUser(context);
        SharedPreferences sharedPreferences=context.getSharedPreferences(user.getEmail(),Context.MODE_PRIVATE);
        return  sharedPreferences.getBoolean(id,false);
    }

    public  static  void clearUser(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.digitalshop",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString(Constants.DB_USERS ,"").apply();
    }

    public  static  User getUser(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.digitalshop",Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(Constants.DB_USERS , "");
        if(json.isEmpty())
            return null;
        else
           return new Gson().fromJson(json,new TypeToken<User>(){}.getType());
    }

}
