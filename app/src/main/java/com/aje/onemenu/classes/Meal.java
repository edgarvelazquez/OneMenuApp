package com.aje.onemenu.classes;

import java.util.ArrayList;

public class Meal {

    private String name;
    private String description;
    private String type;
    private ArrayList<String> meat;
    private ArrayList<String> vegetable;
    private ArrayList<String> extras;

    public Meal(){
        meat = new ArrayList<>();
        vegetable = new ArrayList<>();
        extras = new ArrayList<>();
    }

    public Meal(String name, String description, String type, ArrayList<String> meat, ArrayList<String> vegetable, ArrayList<String> extras) {
        this.name = name;
        this.description = description;
        this.type = type;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ArrayList<String> getMeat() {
        return meat;
    }

    public void setMeat(ArrayList<String> meat) {
        this.meat = meat;
    }

    public ArrayList<String> getVegetable() {
        return vegetable;
    }

    public void setVegetable(ArrayList<String> vegetable) {
        this.vegetable = vegetable;
    }

    public ArrayList<String> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<String> extras) {
        this.extras = extras;
    }
}
