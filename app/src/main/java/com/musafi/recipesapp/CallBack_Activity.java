package com.musafi.recipesapp;

import com.musafi.recipesapp.DB.modole.Recipe;

public interface CallBack_Activity {
    void openWebPage(String url);
    void openHomePage();
    void openSearchPage();
    void openFavoritesPage();
    void openAddManualPage();
    void openAddPicturePage();
    void openRecipeManualPage(Recipe recipe);
    void openRecipePicturePage(Recipe recipe);
}
