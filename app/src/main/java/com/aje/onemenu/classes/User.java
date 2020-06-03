package com.aje.onemenu.classes;

import java.util.ArrayList;

public class User {

    private String id;
    private String name;
    private String email;
    private ArrayList<String>  protein;
    private ArrayList<String> vegetables;
    private ArrayList<String> extras;

    public User(){
        protein = new ArrayList<>();
        vegetables = new ArrayList<>();
        extras = new ArrayList<>();
    }

    public User(String id) {
        this.id = id;
    }

    public User(String id, String name, String email, ArrayList<String> protein, ArrayList<String> vegetables, ArrayList<String> extras) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.protein = protein;
        this.vegetables = vegetables;
        this.extras = extras;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public ArrayList<String> getProtein() {
        return protein;
    }

    public void setProtein(ArrayList<String> protein) {
        this.protein = protein;
    }

    public ArrayList<String> getVegetables() {
        return vegetables;
    }

    public void setVegetables(ArrayList<String> vegetables) {
        this.vegetables = vegetables;
    }

    public ArrayList<String> getExtras() {
        return extras;
    }

    public void setExtras(ArrayList<String> extras) {
        this.extras = extras;
    }
}
