package com.musafi.recipesapp.DB.modole;

import java.util.ArrayList;
import java.util.List;


public class Recipe {
    private static int id;

    private String name;

    private List<Ingredient> ingredients;

    private String instructions;

    private List<String> categories;

    private int type;

    private String dish_url;

    private String recipe_url;

    private String web_url;

    private boolean active;

    public Recipe() {
    }

    public Recipe(String name, ArrayList<Ingredient> ingredients, String instructions, ArrayList<String> categories, String dish_url) {
        this.name = name;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.categories = categories;
        this.type = 0;
        this.dish_url = dish_url;
        id ++;
    }

    public Recipe(String name, ArrayList<String> categories, String dish_url, String recipe_url) {
        this.name = name;
        this.categories = categories;
        this.dish_url = dish_url;
        this.recipe_url = recipe_url;
        this.ingredients = new ArrayList<>();
        this.type = 1;
        id ++;
    }

    public Recipe(String name, String web_url) {
        this.name = name;
        this.web_url = web_url;
        this.type = 2;
        this.ingredients = new ArrayList<>();
        this.categories  = new ArrayList<>();
        id ++;
    }

    public int getId() {
        return id;
    }

    public Recipe setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Recipe setName(String name) {
        this.name = name;
        return this;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public Recipe setIngredients(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
        return this;
    }

    public String getInstructions() {
        return instructions;
    }

    public Recipe setInstructions(String instructions) {
        this.instructions = instructions;
        return this;
    }

    public List<String> getCategories() {
        return categories;
    }

    public Recipe setCategories(ArrayList<String> categories) {
        this.categories = categories;
        return this;
    }

    public int getType() {
        return type;
    }

    public Recipe setType(int type) {
        this.type = type;
        return this;
    }

    public String getDish_url() {
        return dish_url;
    }

    public Recipe setDish_url(String dish_url) {
        this.dish_url = dish_url;
        return this;
    }

    public String getRecipe_url() {
        return recipe_url;
    }

    public Recipe setRecipe_url(String recipe_url) {
        this.recipe_url = recipe_url;
        return this;
    }

    public String getWeb_url() {
        return web_url;
    }

    public Recipe setWeb_url(String web_url) {
        this.web_url = web_url;
        return this;
    }

    public boolean isActive() {
        return active;
    }

    public Recipe setActive(boolean active) {
        this.active = active;
        return this;
    }
}
