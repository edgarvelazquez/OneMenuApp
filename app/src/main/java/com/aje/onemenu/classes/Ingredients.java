package com.aje.onemenu.classes;

import java.util.ArrayList;
import java.util.Collections;

public class Ingredients {

    private ArrayList<String> meats;
    private ArrayList<String> veggie;
    private ArrayList<String> misc;

    private void addMeat(){
        meats.add("pork");
        meats.add("chicken");
        meats.add("beef");
        meats.add("fish");
        meats.add("crab");
        meats.add("tofu");
        meats.add("shrimp");
        meats.add("duck");
        meats.add("lamb");
        meats.add("goat");
        meats.add("lobster");
        meats.add("bison");
        meats.add("frog");
        meats.add("turkey");
        meats.add("eggs");
        meats.add("deer");
        meats.add("catfish");

        meats.add("sausage");
        meats.add("salmon");
        meats.add("bacon");
        meats.add("octopus");

        Collections.sort(meats);
    }
    private void addVeggie(){
        veggie.add("lettuce");
        veggie.add("onion");
        veggie.add("potato");
        veggie.add("tomato");
        veggie.add("edamame");
        veggie.add("cilantro");
        veggie.add("celery");
        veggie.add("cucumber");
        veggie.add("peas");
        veggie.add("pepper");
        veggie.add("mushroom");
        veggie.add("avocado");
        veggie.add("basil");
        veggie.add("arugula");
        veggie.add("asparagus");
        veggie.add("carrot");
        veggie.add("tulipe");
        veggie.add("spinach");
        veggie.add("bell pepper");
        veggie.add("orange");
        veggie.add("lentil");
        veggie.add("parsley");
        veggie.add("chickpeas");
        veggie.add("couscous");
        veggie.add("shallots");
        veggie.add("zucchini");
        veggie.add("scallion");
        veggie.add("olives");
        veggie.add("bitter melon");
        veggie.add("okra");
        veggie.add("eggplant");
        veggie.add("calamansi");

        Collections.sort(veggie);
    }
    private void addMisc(){
        misc.add("ketchup");
        misc.add("mustard");
        misc.add("bun");
        misc.add("mayonnaise");
        misc.add("sesame oil");
        misc.add("beancurd");
        misc.add("chili");
        misc.add("bean");
        misc.add("pasta");

        misc.add("black tea");
        misc.add("pancake");
        misc.add("cheese");
        misc.add("bread");
        misc.add("pickle");
        misc.add("herb");
        misc.add("tortilla");
        misc.add("rice");
        misc.add("sour cream");
        misc.add("worcestershire sauce");
        misc.add("butter");
        misc.add("pecan");
        misc.add("chocolate");
        misc.add("grits");
        misc.add("walnut");
        misc.add("peanut");
        misc.add("truffle");
        misc.add("cinnamon");
        misc.add("ginger");
        misc.add("turmeric");
        misc.add("coriander");
        misc.add("cumin");
        misc.add("cayenne");
        misc.add("honey");
        misc.add("paprika");
        misc.add("soy sauce");
        misc.add("vinegar");
        misc.add("fish sauce");
        misc.add("oregano");



        Collections.sort(misc);
    }

    private static Ingredients instance = null;

    protected Ingredients() {
        // Exists only to defeat instantiation.

        meats = new ArrayList<>();
        misc = new ArrayList<>();
        veggie = new ArrayList<>();

        addMeat();
        addMisc();
        addVeggie();
    }
    public static Ingredients getInstance() {

        if(instance == null) {
            instance = new Ingredients();
        }
        return instance;
    }

    public ArrayList<String> getMeats() {
        return meats;
    }

    public ArrayList<String> getVeggie() {
        return veggie;
    }

    public ArrayList<String> getMisc() {
        return misc;
    }
}
