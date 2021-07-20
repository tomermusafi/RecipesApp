package com.musafi.recipesapp.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.musafi.recipesapp.CallBack_Activity;
import com.musafi.recipesapp.DB.MySharedPreferences;
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.R;
import com.musafi.recipesapp.adapter.RecipeAdapter;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class FragmentFavorites extends Fragment {
    private CallBack_Activity callBack_activity;
    private View view;
    private Context context;

    private EditText favorites_EDT_name, favorites_EDT_link;
    private Button favorites_BTN_save;
    private RecyclerView favorites_RCV_recipes;
    private ArrayList<Recipe> recipes;
    private RecipeAdapter recipeAdapter;
    public FragmentFavorites(){

    }
    public FragmentFavorites(Context context) {
        this.context = context;
        this.recipes = new ArrayList<>();

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
            view = inflater.inflate(R.layout.fragment_favorites, container, false);
        }
        findViews(view);
        favorites_EDT_link.setText("");
        favorites_EDT_name.setText("");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            recipes = (ArrayList<Recipe>) MySharedPreferences.getInstance().getRecipes("MyRecipe",null).stream().filter(a->a.getType() == 2).collect(Collectors.toList());
        }
        recipeAdapter = new RecipeAdapter(context, recipes,2);
        initList(context, favorites_RCV_recipes,recipeAdapter);
        recipeAdapter.SetOnItemClickListener(new RecipeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Recipe recipe) {
                    callBack_activity.openWebPage(recipe.getWeb_url());
            }
        });

        favorites_BTN_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = favorites_EDT_name.getText().toString();
                String link = favorites_EDT_link.getText().toString();
                if(!name.isEmpty() && !link.isEmpty()){
                    ArrayList<Recipe> temp = new ArrayList<>(recipes);
                    temp.add(new Recipe(name,link));
                    MySharedPreferences.getInstance().putRecipes("MyRecipe",temp);
                    recipeAdapter.updateList(temp);
                    favorites_EDT_name.setText("");
                    favorites_EDT_link.setText("");
                }
            }
        });
        return view;
    }

    private void findViews(View view) {
        favorites_EDT_name = view.findViewById(R.id.favorites_EDT_name);
        favorites_EDT_link = view.findViewById(R.id.favorites_EDT_link);
        favorites_BTN_save = view.findViewById(R.id.favorites_BTN_save);
        favorites_RCV_recipes = view.findViewById(R.id.favorites_RCV_recipes);
    }
    private   void initList(Context context, RecyclerView RCV, RecyclerView.Adapter<RecyclerView.ViewHolder> adapter) {
        RCV.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
        RCV.setLayoutManager(layoutManager);
        RCV.setAdapter(adapter);
    }
}
