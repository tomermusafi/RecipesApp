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
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;

import java.io.IOException;

public class FragmentRecipeImage extends Fragment {

    private CallBack_Activity callBack_activity;

    private View view;
    private Context context;

    private TextView recipe_image_TXV_title;
    private ImageView recipe_image_IMG_photo, recipe_image_IMG_recipe_photo;

    private Recipe recipe;

    public FragmentRecipeImage(){

    }
    public FragmentRecipeImage(Context context) {
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
            view = inflater.inflate(R.layout.fragment_recipe_image, container, false);
        }
        findViews(view);

        Bitmap bitmap = null;
        Bitmap bitmap2 = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(recipe.getDish_url()));
            bitmap2 = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(recipe.getRecipe_url()));
            recipe_image_IMG_photo.setImageBitmap(bitmap);
            recipe_image_IMG_recipe_photo.setImageBitmap(bitmap2);
        } catch (IOException e) {
            e.printStackTrace();
        }
        recipe_image_TXV_title.setText(recipe.getName());

        return view;
    }



    private void findViews(View view) {
        recipe_image_TXV_title = view.findViewById(R.id.recipe_image_TXV_title);
        recipe_image_IMG_photo = view.findViewById(R.id.recipe_image_IMG_photo);
        recipe_image_IMG_recipe_photo = view.findViewById(R.id.recipe_image_IMG_recipe_photo);

    }
}
