package com.musafi.recipesapp.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musafi.recipesapp.CallBack_Activity;
import com.musafi.recipesapp.DB.MyData;
import com.musafi.recipesapp.DB.MySharedPreferences;
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;
import com.musafi.recipesapp.adapter.RecipeAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.stream.Collectors;

public class HomeFragment extends Fragment {
    private CallBack_Activity callBack_activity;
    private View view;
    private Context context;
    SearchView home_SV_search;
    RecyclerView home_RCV_recipes;
    ImageView home_IMG_filter;

    RecipeAdapter recipeAdapter;
    ArrayList<Recipe> recipes;

    String filter = "";
    private boolean [] selectCategory;
    private ArrayList<Integer> list = new ArrayList<>();
    private static final String[] categories = MyData.categories;

    public HomeFragment(){

    }
    public HomeFragment(Context context) {
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
            view = inflater.inflate(R.layout.fragment_home, container, false);
        }
        findViews(view);
        list = new ArrayList<>();
        selectCategory = new boolean[categories.length];

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recipes = (ArrayList<Recipe>) MySharedPreferences.getInstance().getRecipes("MyRecipe", null).stream().filter(a->a.getType() == 0 || a.getType() == 1).collect(Collectors.toList());
        }
        ;
        recipeAdapter = new RecipeAdapter(context, recipes,0);
        initList(context, home_RCV_recipes, recipeAdapter);
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

        home_IMG_filter.setOnClickListener(new View.OnClickListener() {
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
                        filter = stringBuilder.toString();
                        filterList();
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
                        filter = "";
                        filterList();
                    }

                });
                builder.show();
            }
        });

        SearchManager searchManager = (SearchManager) context.getSystemService(Context.SEARCH_SERVICE);
        home_SV_search.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        home_SV_search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                recipeAdapter.getFilter().filter(s);
                return false;
            }
        });

        return view;
    }

    private void filterList(){
        ArrayList<Recipe> tempList = new ArrayList<>(this.recipes);
        String filterParts[] =filter.split(",");

        ArrayList<String> filterList = new ArrayList<>(Arrays.asList(filterParts));


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && !filterList.get(0).isEmpty()) {
            filterList = (ArrayList<String>) filterList.stream().map(String::trim).filter(a->!a.isEmpty()).collect(Collectors.toList());
            ArrayList<String> finalFilterList = filterList;
            tempList = (ArrayList<Recipe>) tempList.stream().filter(recipe -> {
                for(String filter: finalFilterList){
                    if(recipe.getCategories().stream().map(String::trim).collect(Collectors.toList()).contains(filter.trim()))
                        return true;

                }
                return false;
            }).collect(Collectors.toList());
        }

        recipeAdapter.updateList(tempList);

    }

    private   void initList(Context context, RecyclerView RCV, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        RCV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        RCV.setLayoutManager(layoutManager);
        RCV.setAdapter(adapter);
    }



    private void findViews(View view) {
        home_SV_search = view.findViewById(R.id.home_SV_search);
        home_RCV_recipes = view.findViewById(R.id.home_RCV_recipes);
        home_IMG_filter = view.findViewById(R.id.home_IMG_filter);
    }

}
