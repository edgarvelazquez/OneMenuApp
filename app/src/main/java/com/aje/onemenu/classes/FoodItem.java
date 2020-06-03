package com.aje.onemenu.classes;

import java.util.ArrayList;

public class FoodItem {

    private String name;
    private String type;
    private String description;
    private ArrayList<String> meat;
    private ArrayList<String> vegetable;
    private ArrayList<String> extras;
    private String documentPath;

    public void setPath(String path){
        documentPath = path;
    }

    public String getRestaurantMealPath(){
        return documentPath;
    }

    public FoodItem(){}

    public FoodItem(String name, String type, String description, ArrayList<String> meat, ArrayList<String> vegetable, ArrayList<String> extras) {
        this.name = name;
        this.type = type;
        this.description = description;
        this.meat = meat;
        this.vegetable = vegetable;
        this.extras = extras;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getMeat() { return meat; }

    public void setMeat(ArrayList<String> meat) {
        this.meat = meat;
    }

    public ArrayList<String> getVegetable() { return vegetable; }

    public void setVegetable(ArrayList<String> vegetable) {
        this.vegetable = vegetable;
    }

    public ArrayList<String> getExtras() { return extras; }

    public void setExtras(ArrayList<String> extras) {
        this.extras = extras;
    }

}
