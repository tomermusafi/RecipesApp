package com.musafi.recipesapp.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.musafi.recipesapp.CallBack_Activity;
import com.musafi.recipesapp.DB.modole.Ingredient;
import com.musafi.recipesapp.DB.MyData;
import com.musafi.recipesapp.DB.MySharedPreferences;
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class FragmentAddManual extends Fragment {
    static CallBack_Activity callBack_activity;
    private View view;
    private Context context;
    private LinearLayout manual_SCV_ingredients;
    private Button manual_BTN_add, manual_BTN_add_categories, manual_BTN_save_recipe, manual_BTN_go_to_picture;
    private AutoCompleteTextView manual_ACTV_ingredient, manual_ACTV_units;
    private EditText manual_ET_amount, manual_EDT_recipe_instruction, manual_EDT_recipe_name;
    private TextView manual_TXV_recipe_categories;
    private ImageView manual_IMG_food_photo;
    private HashMap<String, Ingredient> ingredientHashMap;
    private int keyCounter;
    private static final int PICK_IMAGE = 1;
    private Uri imageUri;
    String foodUrl = "";
    private boolean [] selectCategory;
    private ArrayList<Integer> list = new ArrayList<>();
    private static final String[] categories = MyData.categories;
    private static final String[] units = MyData.units;
    ArrayAdapter<String> adapter;
    ArrayAdapter<String> adapter_ingredient;

    public FragmentAddManual(){

    }
    public FragmentAddManual(Context context) {
        this.context = context;
        this.ingredientHashMap = new HashMap<>();
        keyCounter = 0;
        adapter = new ArrayAdapter(context, android.R.layout.simple_list_item_1, units);
        adapter_ingredient = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, MyData.ingredients);
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
            view = inflater.inflate(R.layout.fragment_add_manual, container, false);
        }
        findViews(view);
        list = new ArrayList<>();
        selectCategory = new boolean[categories.length];

        manual_TXV_recipe_categories.setText("");
        manual_ACTV_units.setText("");
        manual_SCV_ingredients.removeAllViews();
        manual_ACTV_ingredient.setText("");
        manual_EDT_recipe_instruction.setText("");
        manual_EDT_recipe_name.setText("");
        manual_ET_amount.setText("");
        manual_IMG_food_photo.setImageResource(R.drawable.img_gallery);


        manual_ACTV_ingredient.setAdapter(adapter_ingredient);

        manual_BTN_go_to_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callBack_activity.openAddPicturePage();
            }
        });
        manual_ACTV_units.setAdapter(adapter);
        manual_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manual_ACTV_ingredient.getText().toString().isEmpty() &&
                        !manual_ET_amount.getText().toString().isEmpty() &&
                        !manual_ACTV_units.getText().toString().isEmpty()){
                    Ingredient ingredient = new Ingredient(manual_ACTV_ingredient.getText().toString(),
                            Integer.parseInt(manual_ET_amount.getText().toString()),
                            manual_ACTV_units.getText().toString(),"");
                    ingredientHashMap.put(""+keyCounter, ingredient);
                    addIngredient(keyCounter, ingredient);
                    keyCounter ++;
                    manual_ACTV_ingredient.setText("");
                    manual_ET_amount.setText("");
                    manual_ACTV_units.setText("");
                }

            }
        });

        manual_BTN_add_categories.setOnClickListener(new View.OnClickListener() {
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
                        manual_TXV_recipe_categories.setText(stringBuilder);
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
                        manual_TXV_recipe_categories.setText("");
                    }
                });
                builder.show();
            }
        });

        manual_IMG_food_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });
        manual_BTN_save_recipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!manual_EDT_recipe_name.getText().toString().isEmpty()) {
                    saveRecipe();
                    Toast.makeText(context,"Saved",Toast.LENGTH_LONG).show();
                    callBack_activity.openHomePage();
                }
            }
        });

        return view;
    }

    private void saveRecipe(){
        Recipe recipe = new Recipe(manual_EDT_recipe_name.getText().toString(),new ArrayList(ingredientHashMap.values()),manual_EDT_recipe_instruction.getText().toString(),
                new ArrayList<String>(Arrays.asList(manual_TXV_recipe_categories.getText().toString().split(", "))), foodUrl);
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

                manual_IMG_food_photo.setImageBitmap(bitmap);
                foodUrl = imageUri.toString();


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void addIngredient(int key, Ingredient my_ingredient){
        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        param.setMargins(5,5,5,5);
        TextView ingredient = new TextView(context);
        TextView amount = new TextView(context);
        TextView units = new TextView(context);
        Button remove = new Button(context);
        ingredient.setLayoutParams(param);
        amount.setLayoutParams(param);
        units.setLayoutParams(param);
        remove.setLayoutParams(param);
        ingredient.setGravity(Gravity.CENTER);
        amount.setGravity(Gravity.CENTER);
        units.setGravity(Gravity.CENTER);
        remove.setText("-");
        ingredient.setText(my_ingredient.getName());
        amount.setText(""+my_ingredient.getAmount());
        units.setText(my_ingredient.getUnits());
        remove.setId(key);
        remove.setBackgroundColor(Color.WHITE);
        remove.setTextColor(Color.RED);
        linear.addView(ingredient);
        linear.addView(amount);
        linear.addView(units);
        linear.addView(remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = (LinearLayout) view.getParent();
                int id = view.getId();
                //Log.d("pttt","kkk "+((TextView)((ViewGroup) linearLayout).getChildAt(0)).getText());
                linearLayout.setVisibility(View.GONE);
                ingredientHashMap.remove(""+id);

            }
        });
        manual_SCV_ingredients.addView(linear);
    }


    private void findViews(View view) {
        manual_SCV_ingredients = view.findViewById(R.id.manual_SCV_ingredients);
        manual_BTN_add = view.findViewById(R.id.manual_BTN_add);
        manual_ACTV_ingredient = view.findViewById(R.id.manual_ACTV_ingredient);
        manual_ET_amount = view.findViewById(R.id.manual_ET_amount);
        manual_ACTV_units = view.findViewById(R.id.manual_ACTV_units);
        manual_EDT_recipe_instruction = view.findViewById(R.id.manual_EDT_recipe_instruction);
        manual_BTN_add_categories = view.findViewById(R.id.manual_BTN_add_categories);
        manual_TXV_recipe_categories = view.findViewById(R.id.manual_TXV_recipe_categories);
        manual_IMG_food_photo = view.findViewById(R.id.manual_IMG_food_photo);
        manual_EDT_recipe_name = view.findViewById(R.id.manual_EDT_recipe_name);
        manual_BTN_save_recipe = view.findViewById(R.id.manual_BTN_save_recipe);
        manual_BTN_go_to_picture = view.findViewById(R.id.manual_BTN_go_to_picture);
    }
}
