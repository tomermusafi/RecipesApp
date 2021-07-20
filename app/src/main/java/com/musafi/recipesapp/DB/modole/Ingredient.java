package com.musafi.recipesapp.DB.modole;

public class Ingredient {
    private static int id;
    private String name;
    private int amount;
    private String units;
    private String url_img;

    public Ingredient() {
    }

    public Ingredient(String name, int amount, String units, String url_img) {
        this.name = name;
        this.amount = amount;
        this.units = units;
        this.url_img = url_img;
        id++;
    }
    public Ingredient(String name) {
        this.name = name;
        id++;
    }

    public int getId() {
        return id;
    }

    public Ingredient setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Ingredient setName(String name) {
        this.name = name;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public Ingredient setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public String getUnits() {
        return units;
    }

    public Ingredient setUnits(String units) {
        this.units = units;
        return this;
    }

    public String getUrl_img() {
        return url_img;
    }

    public Ingredient setUrl_img(String url_img) {
        this.url_img = url_img;
        return this;
    }
}
