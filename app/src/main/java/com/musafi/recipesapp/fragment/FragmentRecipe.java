package com.musafi.recipesapp.fragment;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.musafi.recipesapp.CallBack_Activity;
import com.musafi.recipesapp.DB.modole.Ingredient;
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;

import java.io.IOException;
import java.util.ArrayList;

public class FragmentRecipe extends Fragment {

    private CallBack_Activity callBack_activity;

    private View view;
    private Context context;

    private TextView recipe_TXV_title, recipe_TXV_ingredients,recipe_TXV_instruction;
    private ImageView recipe_IMG_photo;

    private Recipe recipe;

    public FragmentRecipe(){

    }
    public FragmentRecipe(Context context) {
        this.context = context;

    }
    public void setCallBack(CallBack_Activity _callBack_activity) {
        this.callBack_activity = _callBack_activity;
    }

    public void setRecipe(Recipe recipe){
        this.recipe = recipe;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        Log.d("pttt", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("pttt", "onCreateView");
        if(view==null){
            view = inflater.inflate(R.layout.fragment_recipe, container, false);
        }
        findViews(view);

        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(recipe.getDish_url()));
            recipe_IMG_photo.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipe_TXV_title.setText(recipe.getName());
        recipe_TXV_instruction.setText(recipe.getInstructions());
        ArrayList<Ingredient> ingredients = new ArrayList<>(recipe.getIngredients());
        String st_ingredient = "";
        for(Ingredient ingredient: ingredients){
            st_ingredient += ingredient.getName() +"     " + ingredient.getAmount()+" "+ingredient.getUnits() +"\n\n";
        }
        recipe_TXV_ingredients.setText(st_ingredient);
        return view;
    }



    private void findViews(View view) {
        recipe_TXV_title = view.findViewById(R.id.recipe_TXV_title);
        recipe_TXV_ingredients = view.findViewById(R.id.recipe_TXV_ingredients);
        recipe_TXV_instruction = view.findViewById(R.id.recipe_TXV_instruction);
        recipe_IMG_photo = view.findViewById(R.id.recipe_IMG_photo);
    }
}
