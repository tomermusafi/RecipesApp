package com.musafi.recipesapp.fragment;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musafi.recipesapp.CallBack_Activity;
import com.musafi.recipesapp.DB.modole.Ingredient;
import com.musafi.recipesapp.DB.MyData;
import com.musafi.recipesapp.DB.MySharedPreferences;
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;
import com.musafi.recipesapp.adapter.RecipeAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class FragmentSearch extends Fragment {
    private CallBack_Activity callBack_activity;
    private View view;
    private Context context;

    private LinearLayout search_SCV_ingredients;
    private AutoCompleteTextView search_EDT_ingredient;
    private Button search_BTN_add, search_BTN_search, search_BTN_clear;
    private RecyclerView search_RCV_recipes;

    private HashMap<String, Ingredient> ingredientHashMap;
    private int keyCounter;

    private ArrayAdapter<String> adapter;
    private List<Recipe> recipes;
    private RecipeAdapter recipeAdapter;
    public FragmentSearch(){

    }
    public FragmentSearch(Context context) {
        this.context = context;
        this.ingredientHashMap = new HashMap<>();
        keyCounter = 0;
        adapter = new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, MyData.ingredients);

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
            view = inflater.inflate(R.layout.fragment_search, container, false);
        }
        findViews(view);

        search_EDT_ingredient.setAdapter(adapter);
        search_EDT_ingredient.setText("");
        search_SCV_ingredients.removeAllViews();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recipes = MySharedPreferences.getInstance().getRecipes("MyRecipe",null).stream().filter(a->a.getType() == 0 || a.getType() == 1).collect(Collectors.toList());
        }
        recipeAdapter = new RecipeAdapter(context, new ArrayList<>(),0);
        initList(context, search_RCV_recipes, recipeAdapter);
        recipeAdapter.SetOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Recipe recipe) {
                if(recipe.getType() == 0){
                    callBack_activity.openRecipeManualPage(recipe);
                }else if(recipe.getType() == 1){
                    callBack_activity.openRecipePicturePage(recipe);
                }
            }
        });
        search_BTN_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!search_EDT_ingredient.getText().toString().isEmpty()){
                    Ingredient ingredient = new Ingredient(search_EDT_ingredient.getText().toString());
                    ingredientHashMap.put(""+keyCounter, ingredient);
                    addIngredient(keyCounter, ingredient);
                    keyCounter ++;
                    search_EDT_ingredient.setText("");

                }
            }
        });
        search_BTN_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ingredientHashMap.clear();
                search_SCV_ingredients.removeAllViews();
            }
        });

        search_BTN_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findRecipes();
            }
        });

        return view;
    }

    private void findRecipes() {
        ArrayList<Ingredient> ingredients = new ArrayList<>(ingredientHashMap.values());
        Log.d("pttt","jjj: " + ingredients.size());
        ArrayList<Recipe> tempRecipes = new ArrayList<>();
        for (Recipe recipe : recipes) {
            if (recipe.getIngredients() != null){
                ArrayList<Ingredient> myIngredients = new ArrayList<>(recipe.getIngredients());
            for (Ingredient ingredient : myIngredients) {
                for (Ingredient ingredient1 : ingredients) {
                    if (ingredient.getName().equals(ingredient1.getName())) {
                        tempRecipes.add(recipe);
                        break;
                    }
                }
            }
        }
        }
        recipeAdapter.updateList(tempRecipes);
    }

    private void addIngredient(int key, Ingredient my_ingredient){
        LinearLayout linear = new LinearLayout(context);
        linear.setOrientation(LinearLayout.HORIZONTAL);
        linear.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f));
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.0f);
        LinearLayout.LayoutParams param2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 4.0f);
        param.setMargins(5,5,5,5);
        param2.setMargins(5,5,5,5);
        TextView ingredient = new TextView(context);
        Button remove = new Button(context);
        ingredient.setLayoutParams(param);
        remove.setLayoutParams(param2);
        ingredient.setGravity(Gravity.CENTER);
        remove.setText("-");
        ingredient.setText(my_ingredient.getName());
        remove.setId(key);
        remove.setBackgroundColor(Color.WHITE);
        remove.setTextColor(Color.RED);
        linear.addView(ingredient);
        linear.addView(remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout linearLayout = (LinearLayout) view.getParent();
                int id = view.getId();
                Log.d("pttt","kkk "+((TextView)((ViewGroup) linearLayout).getChildAt(0)).getText());
                linearLayout.setVisibility(View.GONE);
                ingredientHashMap.remove(""+id);

            }
        });
        search_SCV_ingredients.addView(linear);
    }

    private void findViews(View view) {
        search_SCV_ingredients = view.findViewById(R.id.search_SCV_ingredients);
        search_EDT_ingredient = view.findViewById(R.id.search_EDT_ingredient);
        search_BTN_add = view.findViewById(R.id.search_BTN_add);
        search_BTN_search = view.findViewById(R.id.search_BTN_search);
        search_RCV_recipes = view.findViewById(R.id.search_RCV_recipes);
        search_BTN_clear = view.findViewById(R.id.search_BTN_clear);

    }
    private   void initList(Context context, RecyclerView RCV, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        RCV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        RCV.setLayoutManager(layoutManager);
        RCV.setAdapter(adapter);
    }
}
