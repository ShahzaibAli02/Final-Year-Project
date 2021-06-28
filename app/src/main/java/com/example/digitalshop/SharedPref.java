package com.example.digitalshop;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.digitalshop.Model.Order;
import com.example.digitalshop.Model.User;
import com.example.digitalshop.Utils.Constants;
import com.google.firebase.firestore.FieldValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class SharedPref
{

    public  static  void saveUser(Context context, User user)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.digitalshop",Context.MODE_PRIVATE);
        String json=new Gson().toJson(user);
        sharedPreferences.edit().putString(Constants.DB_USERS ,json).apply();
    }


    public static void  saveUserType(Context context,String type)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.digitalshop",Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("Type",type).apply();
    }

    public static   String  getUserType(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences("com.example.digitalshop",Context.MODE_PRIVATE);
        return  sharedPreferences.getString("Type","");
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
        if(user==null)
            return false;
        SharedPreferences sharedPreferences=context.getSharedPreferences(user.getEmail(),Context.MODE_PRIVATE);

        return  sharedPreferences.getBoolean(id,false);
    }


    public  static  void addInCart(Context context, Order order)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(getUser(context).getEmail(),Context.MODE_PRIVATE);
        List<Order> cartItems = getCartItems(context);
        cartItems.add(order);
        sharedPreferences.edit().putString("cart",new Gson().toJson(cartItems)).apply();
    }
    public static void  clearCart(Context context)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(getUser(context).getEmail(),Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("cart","").apply();
    }
    public static void  updateCart(Context context, List<Order> cartItems)
    {
        SharedPreferences sharedPreferences=context.getSharedPreferences(getUser(context).getEmail(),Context.MODE_PRIVATE);
        sharedPreferences.edit().putString("cart",new Gson().toJson(cartItems)).apply();
    }
    public  static List<Order> getCartItems (Context context)
    {
        if(getUser(context)==null)
            return  new ArrayList<>();

        SharedPreferences sharedPreferences=context.getSharedPreferences(getUser(context).getEmail(),Context.MODE_PRIVATE);
        String json = sharedPreferences.getString("cart" , "");
        if(json.isEmpty())
            return new ArrayList<>();
        else
            return new Gson().fromJson(json,new TypeToken<List<Order>>(){}.getType());
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
