package com.musafi.recipesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Toast;

import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.musafi.recipesapp.DB.MySharedPreferences;
import com.musafi.recipesapp.DB.modole.Recipe;
import com.musafi.recipesapp.fragment.FragmentAddImagePage;
import com.musafi.recipesapp.fragment.FragmentAddManual;
import com.musafi.recipesapp.fragment.FragmentFavorites;
import com.musafi.recipesapp.fragment.FragmentRecipe;
import com.musafi.recipesapp.fragment.FragmentRecipeImage;
import com.musafi.recipesapp.fragment.FragmentSearch;
import com.musafi.recipesapp.fragment.HomeFragment;
import com.musafi.recipesapp.fragment.WebPageFragment;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public class MainActivity extends AppCompatActivity {

    private WebPageFragment webPageFragment;
    private FragmentAddImagePage fragmentAddImagePage;
    private FragmentAddManual fragmentAddManual;
    private HomeFragment homeFragment;
    private FragmentSearch fragmentSearch;
    private FragmentFavorites fragmentFavorites;
    private FragmentRecipe fragmentRecipe;
    private FragmentRecipeImage fragmentRecipeImage;

    private int Storage_permission = 1;

    MeowBottomNavigation bottomNavigation;
    private final int HOME = 1;
    private final int ADD = 2;
    private final int FAVORITES = 3;
    private final int SEARCH = 4;

    private int page;
    private int web_page;
    private int currentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MySharedPreferences.initHelper(this);

        page = 0;
        web_page = 0;
        currentPage = HOME;
        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.add(new MeowBottomNavigation.Model(HOME, R.drawable.img_home));
        bottomNavigation.add(new MeowBottomNavigation.Model(ADD, R.drawable.img_add));
        bottomNavigation.add(new MeowBottomNavigation.Model(FAVORITES, R.drawable.img_star));
        bottomNavigation.add(new MeowBottomNavigation.Model(SEARCH, R.drawable.img_search));
        bottomNavigation.show(HOME,true);


        bottomNavigation.setOnClickMenuListener(new Function1<MeowBottomNavigation.Model, Unit>() {
            @Override
            public Unit invoke(MeowBottomNavigation.Model model) {
                switch (model.getId()){
                    case HOME:
                        showFragment(homeFragment);
                        currentPage = HOME;
                        break;
                    case ADD:
                        showFragment(fragmentAddManual);
                        currentPage = ADD;
                        if(page > 0)
                            page --;
                        if (web_page > 0)
                            web_page --;
                        break;
                    case FAVORITES:
                        showFragment(fragmentFavorites);
                        currentPage = FAVORITES;
                        if(page > 0)
                            page --;
                        if (web_page > 0)
                            web_page --;
                        break;
                    case SEARCH:
                        showFragment(fragmentSearch);
                        currentPage = SEARCH;
                        if(page > 0)
                            page --;
                        if (web_page > 0)
                            web_page --;
                        break;
                }
                return null;
            }
        });
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED){

        }else{
            requestStoragePermission();
        }


        webPageFragment = new WebPageFragment(this);
        fragmentAddImagePage = new FragmentAddImagePage(this);
        fragmentAddManual = new FragmentAddManual(this);
        homeFragment = new HomeFragment(this);
        fragmentSearch = new FragmentSearch(this);
        fragmentFavorites = new FragmentFavorites(this);
        fragmentRecipe = new FragmentRecipe(this);
        fragmentRecipeImage = new FragmentRecipeImage(this);

        webPageFragment.setCallBack(callBack_activity);
        fragmentAddImagePage.setCallBack(callBack_activity);
        fragmentAddManual.setCallBack(callBack_activity);
        homeFragment.setCallBack(callBack_activity);
        fragmentSearch.setCallBack(callBack_activity);
        fragmentFavorites.setCallBack(callBack_activity);
        fragmentRecipe.setCallBack(callBack_activity);
        fragmentRecipeImage.setCallBack(callBack_activity);

        showFragment(homeFragment);


    }

    private void requestStoragePermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.READ_EXTERNAL_STORAGE)){
            new AlertDialog.Builder(this).setTitle("Permission needed").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Storage_permission);
                }
            }).setNegativeButton("caancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        }else{
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},Storage_permission);
        }
    }

    private void showFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main_FL_base_page, fragment);
        transaction.commit();
    }

    private CallBack_Activity callBack_activity =  new CallBack_Activity() {

        @Override
        public void openWebPage(String url) {
            webPageFragment.setUrl(url);
            showFragment(webPageFragment);
            web_page++;
        }

        @Override
        public void openHomePage() {
            showFragment(homeFragment);
            bottomNavigation.show(HOME,true);
        }

        @Override
        public void openSearchPage() {
            showFragment(fragmentSearch);
        }

        @Override
        public void openFavoritesPage() {
            showFragment(fragmentFavorites);
        }

        @Override
        public void openAddManualPage() {
            showFragment(fragmentAddManual);
        }

        @Override
        public void openAddPicturePage() {
            showFragment(fragmentAddImagePage);
        }

        @Override
        public void openRecipeManualPage(Recipe recipe) {
            fragmentRecipe.setRecipe(recipe);
            showFragment(fragmentRecipe);
            page++;

        }

        @Override
        public void openRecipePicturePage(Recipe recipe) {
            fragmentRecipeImage.setRecipe(recipe);
            showFragment(fragmentRecipeImage);
            page++;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Storage_permission) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "success", Toast.LENGTH_LONG);
            }else{
                Toast.makeText(this, "failed", Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(page > 0){
            switch (currentPage){
                case HOME:
                    showFragment(homeFragment);
                    break;
                case ADD:
                    showFragment(fragmentAddManual);
                    break;
                case FAVORITES:
                    showFragment(fragmentFavorites);
                    break;
                case SEARCH:
                    showFragment(fragmentSearch);
                    break;
            }
            page--;
        }else if(web_page > 0 && !webPageFragment.isWebCanGoBack() ) {
            if(currentPage == FAVORITES){
                showFragment(fragmentFavorites);
            }else {
                super.onBackPressed();
            }
        }


    }
}