package com.musafi.recipesapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.musafi.recipesapp.CallBack_Activity;
import com.musafi.recipesapp.DB.MyData;
import com.musafi.recipesapp.DB.MySharedPreferences;
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static android.app.Activity.RESULT_OK;

public class FragmentAddImagePage extends Fragment {
    private CallBack_Activity callBack_activity;
    private View view;
    private Context context;
    private EditText image_EDT_recipe_name;
    private Button image_BTN_add_categories, image_BTN_save_recipe, image_BTN_go_to_manual;
    private TextView image_TXV_recipe_categories;
    private ImageView image_IMG_recipe_photo, image_IMG_food_photo;
    private ArrayList<Integer> list = new ArrayList<>();
    private boolean [] selectCategory;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    private String foodUrl = "";
    private String recipeUrl = "";
    private int flag = 0;
    private static final String[] categories = MyData.categories;

    public FragmentAddImagePage(){

    }
    public FragmentAddImagePage(Context context) {
        this.context = context;
        selectCategory = new boolean[categories.length];

    }
    public void setCallBack(CallBack_Activity _callBack_activity) {
        this.callBack_activity = _callBack_activity;
    }



    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState);
        Log.d("pttt", "onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("pttt", "onCreateView");
        if(view==null){
            view = inflater.inflate(R.layout.fragment_add_image, container, false);
        }
        findViews(view);
        list = new ArrayList<>();
        selectCategory = new boolean[categories.length];
        image_IMG_recipe_photo.setImageResource(R.drawable.img_gallery);
        image_IMG_food_photo.setImageResource(R.drawable.img_gallery);
        image_EDT_recipe_name.setText("");
        image_TXV_recipe_categories.setText("");

        image_BTN_save_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!image_EDT_recipe_name.getText().toString().isEmpty()) {
                    saveRecipe();
                    Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
                    callBack_activity.openHomePage();
                }
            }
        });
        image_IMG_food_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 2;
                getImageFromGallery();
            }
        });
        image_IMG_recipe_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 1;
                getImageFromGallery();
            }
        });
        image_BTN_add_categories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Select name");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(categories, selectCategory, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b){
                            list.add(new Integer(i));
                            Collections.sort(list);
                        }else{
                            list.remove(new Integer(i));
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int j = 0; j < list.size(); j ++){
                            stringBuilder.append(categories[list.get(j)]);
                            if(j != list.size()-1){
                                stringBuilder.append(", ");
                            }
                        }
                        image_TXV_recipe_categories.setText(stringBuilder);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        for(int j = 0; j < selectCategory.length; j ++){
                            selectCategory[j] = false;

                            list.clear();

                        }
                        image_TXV_recipe_categories.setText("");
                    }
                });
                builder.show();
            }
        });

        image_BTN_go_to_manual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack_activity.openAddManualPage();
            }
        });
        return view;
    }

    private void saveRecipe(){
        Recipe recipe = new Recipe(image_EDT_recipe_name.getText().toString(),
                new ArrayList<String>(Arrays.asList(image_TXV_recipe_categories.getText().toString().split(", "))), foodUrl,recipeUrl);
        ArrayList<Recipe> recipes = MySharedPreferences.getInstance().getRecipes("MyRecipe",null);
        if(recipes == null){
            ArrayList<Recipe> tempRecipe = new ArrayList<>();
            tempRecipe.add(recipe);
            MySharedPreferences.getInstance().putRecipes("MyRecipe", tempRecipe);
        }else{
            recipes.add(recipe);
            MySharedPreferences.getInstance().putRecipes("MyRecipe",recipes);
        }

    }


    private void findViews(View view) {
        image_EDT_recipe_name = view.findViewById(R.id.image_EDT_recipe_name);
        image_BTN_add_categories = view.findViewById(R.id.image_BTN_add_categories);
        image_BTN_save_recipe = view.findViewById(R.id.image_BTN_save_recipe);
        image_TXV_recipe_categories = view.findViewById(R.id.image_TXV_recipe_categories);
        image_IMG_recipe_photo = view.findViewById(R.id.image_IMG_recipe_photo);
        image_IMG_food_photo = view.findViewById(R.id.image_IMG_food_photo);
        image_BTN_go_to_manual = view.findViewById(R.id.image_BTN_go_to_manual);
    }
    private void getImageFromGallery(){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Picture"), PICK_IMAGE);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK){
            imageUri = data.getData();
            System.out.println("------------------------------: "+imageUri);
            try{
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imageUri);
                if(flag == 1) {
                    image_IMG_recipe_photo.setImageBitmap(bitmap);
                    recipeUrl = imageUri.toString();
                }
                else if( flag == 2){
                    image_IMG_food_photo.setImageBitmap(bitmap);
                    foodUrl = imageUri.toString();
                }
                flag = 0;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
