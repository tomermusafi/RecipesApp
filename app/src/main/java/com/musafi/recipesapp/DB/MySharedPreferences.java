package com.musafi.recipesapp.DB;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.musafi.recipesapp.DB.modole.Ingredient;
import com.musafi.recipesapp.DB.modole.Recipe;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MySharedPreferences {
    private static MySharedPreferences instance;
    private SharedPreferences prefs;
    private Gson gson;

    private MySharedPreferences(Context context) {
        prefs = context.getSharedPreferences("MyPref", MODE_PRIVATE);
        gson = new Gson();
    }
    public static MySharedPreferences initHelper(Context context){
        if(instance == null){
            instance = new MySharedPreferences(context);
        }
        return instance;
    }
    public static MySharedPreferences getInstance(){
        return instance;
    }

    public int getInt(String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public String getString(String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public boolean getBool(String key, boolean defaultValue){
        return prefs.getBoolean(key, defaultValue);
    }
    public void putBool(String key, Boolean value){
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }


    public ArrayList<Recipe> getRecipes(String key, String defaultValue){
        Type type = new TypeToken<List<Recipe>>(){}.getType();
        String json = prefs.getString(key,defaultValue);
        ArrayList<Recipe> recipes = gson.fromJson(json, type);
        return recipes;
    }
    public void  putRecipes(String key, ArrayList<Recipe> value){
        SharedPreferences.Editor editor = prefs.edit();
        String json = gson.toJson(value);
        editor.putString(key, json);
        editor.apply();
    }

    public void removeKey(String key) {
        prefs.edit().remove(key);
    }
}
