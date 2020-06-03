package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.aje.onemenu.R;
import com.aje.onemenu.classes.FoodItem;
import com.aje.onemenu.classes.Restaurant;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantMenuActivity extends AppCompatActivity {

    private ArrayList<FoodItem> temp = new ArrayList<>();
    private ArrayList<FoodItem> recommendedList = new ArrayList<>();
    private ArrayList<FoodItem> mealList = new ArrayList<>();
    private ArrayList<FoodItem> beverageList = new ArrayList<>();
    private ArrayList<FoodItem> appetizerList = new ArrayList<>();
    private ArrayList<FoodItem> dessertList = new ArrayList<>();
    private ArrayList<FoodItem> tryList = new ArrayList<>();
    private FirebaseFirestore db;
    private String restaurant;
    private ArrayList<String> userPreference = new ArrayList<>();
    private HashMap<String, Uri> urisMenu = new HashMap<>();
    private HashMap<String, ImageView> imageViewHashMap= new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_food_menu);
        restaurant = getIntent().getStringExtra("restaurant");
        userPreference = getIntent().getStringArrayListExtra("userRecommendation");
        getUris();

        final LinearLayout recommendedLayout = findViewById(R.id.recommended_list);
        final LinearLayout mealLayout = findViewById(R.id.main_list);
        final LinearLayout beverageLayout = findViewById(R.id.beverage_list);
        final LinearLayout dessertLayout = findViewById(R.id.dessert_list);
        final LinearLayout appetizerLayout = findViewById(R.id.appetizer_list);
        final LinearLayout tryLayout = findViewById(R.id.try_list);

        final LinearLayout recommended_layout = findViewById(R.id.recommended_layout);
        final LinearLayout meal_layout = findViewById(R.id.meal_layout);
        final LinearLayout beverage_layout = findViewById(R.id.beverage_layout);
        final LinearLayout dessert_layout = findViewById(R.id.dessert_layout);
        final LinearLayout appetizer_layout = findViewById(R.id.appetizer_layout);
        final LinearLayout try_layout = findViewById(R.id.try_layout);


        //gather database
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);

        if(restaurant == null) restaurant = " ";

        populate(restaurant, "recommended",recommended_layout, recommendedLayout, recommendedList);
        populate(restaurant, "meal",meal_layout, mealLayout, mealList);
        populate(restaurant, "appetizers", appetizer_layout,appetizerLayout, appetizerList);
        populate(restaurant, "dessert",dessert_layout, dessertLayout, dessertList);
        populate(restaurant, "drink",beverage_layout, beverageLayout, beverageList);
        populate(restaurant, "try",try_layout, tryLayout, tryList);

        HorizontalScrollView sv = findViewById(R.id.meal_scroller);
        sv.scrollTo(0, sv.getBottom());
    }

    //populate arraylists
    public void populate(final String restaurant, final String foodGroup, final LinearLayout row, final LinearLayout layout, final ArrayList<FoodItem> foodList) {


        if (foodGroup.equals("recommended")) {
            if(!userPreference.isEmpty()){
                for(final String prefMeat: userPreference) {
                    db.collection("restaurants").document(restaurant).collection(restaurant + "_menu").whereArrayContains("meat", prefMeat).get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if (!queryDocumentSnapshots.isEmpty()) {
                                      //  Toast.makeText(RestaurantMenuActivity.this, "", Toast.LENGTH_SHORT).show();

                                        List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                                        ArrayList<FoodItem> temp1 = new ArrayList<>();
                                        for (DocumentSnapshot d : list) {
                                            int counter= 0;
                                            FoodItem p = d.toObject(FoodItem.class);
                                            //Toast.makeText(RestaurantMenuActivity.this, "recommeneded"+counter++, Toast.LENGTH_SHORT).show();
                                            p.setPath("restaurants" + "/" + restaurant + "/" + restaurant + "_menu" + "/" + d.getId());
//                                            boolean flag = true;
//                                            for(FoodItem f: foodList){
//                                                if(f.getName().contains(p.getName())) flag = false;
//                                                Log.d("Test", "onSuccess: "+ p.getName());
//                                            }
//                                            if(flag) {
                                                foodList.add(p);
//                                            }
//                                            temp.add(p);



                                        }
//                                        update(foodList, row, layout, foodGroup);
//


                                    }else{
                                        //Toast.makeText(RestaurantMenuActivity.this, "no recommendations", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            });
                }

            }
        }
        else{
            db.collection("restaurants").document(restaurant).collection(restaurant + "_menu").whereEqualTo("type", foodGroup).get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if (!queryDocumentSnapshots.isEmpty()) {
                                List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();
                                for (DocumentSnapshot d : list) {
                                    FoodItem p = d.toObject(FoodItem.class);
                                    p.setPath("restaurants" + "/" + restaurant + "/" + restaurant + "_menu" + "/" + d.getId());
                                    //menuList.add(p);
                                    foodList.add(p);
                                }
                                update(foodList, row, layout, foodGroup);
                                if (foodGroup.contains("meal")){
                                    ArrayList<FoodItem> temp2 = copyEliminator(recommendedList, temp);
                                    final LinearLayout recommended_layout2 = findViewById(R.id.recommended_layout);
                                    final LinearLayout recommendedLayout2 = findViewById(R.id.recommended_list);
                                    update(temp2, recommended_layout2,recommendedLayout2 ,"recommended");
                                }
                            } else {
                                Log.d("RestaurantMenuActivity", "There no such menu in the database");
                            }
                        }
                    });
        }
    }
    //consolidate items in recommended list
    //cannot sync db query so this is used to eliminate copies
    public ArrayList<FoodItem> copyEliminator(ArrayList<FoodItem> from, ArrayList<FoodItem> to){

        for(FoodItem fr: from){
            boolean flag = true;
            for(FoodItem t: to){
                if(t.getName().contains(fr.getName())){
                    flag = false;
                }
            }
            if(flag) to.add(fr);
        }
        return to;
    }
    //display whatever is on the arraylist
    //change appetizers to recommended
    public void update(ArrayList<FoodItem> list, LinearLayout row, LinearLayout layout, String foodGroup) {

        row.setVisibility(View.VISIBLE);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;
        int height = size.y;

//        if (foodGroup.contains("recommended")){
//            list = copyEliminator(temp, list);
//        }

        for (final FoodItem a : list) {

            final LinearLayout mealitem = new LinearLayout(this);
            mealitem.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams mealitemParams;
            if (foodGroup.contains("recommended")) {
                mealitemParams = new LinearLayout.LayoutParams((int) (width * 2 / 3), LinearLayout.LayoutParams.MATCH_PARENT);
            } else {
                mealitemParams = new LinearLayout.LayoutParams((int) (width * 2 / 5), LinearLayout.LayoutParams.MATCH_PARENT);
            }
            mealitemParams.setMargins(10, 0, 10, 0);
            mealitem.setLayoutParams(mealitemParams);
            mealitem.setBackgroundColor(Color.WHITE);

            //name
            if (!(foodGroup.contains("recommended"))) {
                TextView name = new TextView(this);
                name.setText(a.getName());
                name.setTextSize(13);
                mealitem.addView(name);
            }

            //second layout
            LinearLayout nameAndDescription = new LinearLayout(this);
            if (foodGroup.contains("meal") || foodGroup.contains("recommended")) {
                nameAndDescription.setOrientation(LinearLayout.VERTICAL);
            } else {
                nameAndDescription.setOrientation(LinearLayout.HORIZONTAL);
            }
            mealitem.addView(nameAndDescription);
            //            nameAndDescription.setBackgroundColor(Color.GRAY);

            //picture
            ImageView image = new ImageView(this);

            Log.i("==============================================",
                    a.getRestaurantMealPath());
            //restaurants/pinoybbq/pinoybbq_menu/Adobo

            String[] s =  a.getRestaurantMealPath().split("/");
            String id = s[s.length - 1];

            Log.e("=========", id);
            imageViewHashMap.put(id.replaceAll("\\s+|\\r|\\n|\\t", ""), image);

//            if(urisMenu.containsKey(id)){
//
//                Glide.with(RestaurantMenuActivity.this)
//                        .load(urisMenu.get(id))
//                        .into(image);
//
//            } else {
//
//                Drawable myDrawable = getResources().getDrawable(R.drawable.ic_restaurant_black_24dp);
//                image.setImageDrawable(myDrawable);
//
//            }

            Drawable myDrawable = getResources().getDrawable(R.drawable.ic_restaurant_black_24dp);
            image.setImageDrawable(myDrawable);


            if (foodGroup.contains("meal")) {
                image.setLayoutParams(new LinearLayout.LayoutParams((int) (width * 2 / 5), (int) (height / 8)));
            } else if (foodGroup.contains("recommended")) {
                image.setLayoutParams(new LinearLayout.LayoutParams((int) (width * 2 / 3), (int) (height/ 8)));

            } else {
                image.setLayoutParams(new LinearLayout.LayoutParams((int) (width / 5), (int) (height / 10)));
            }
            nameAndDescription.addView(image);

            //description
            TextView description = new TextView(this);
            if (foodGroup.contains("recommended")) {
                description.setText(a.getName());
                description.setText(a.getName());
                description.setTextSize(13);
            } else {
                description.setText(a.getDescription());
                description.setTextSize(7);
            }

            nameAndDescription.addView(description);



            mealitem.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                   // Toast.makeText(RestaurantMenuActivity.this, a.getRestaurantMealPath(), Toast.LENGTH_SHORT).show();
                    Log.d("RESTAURANT MENU ACTIVITY", a.getRestaurantMealPath());

                    if(!a.getRestaurantMealPath().isEmpty()){

                        Log.d("TEST", a.getRestaurantMealPath());

                        Intent intent = new Intent(RestaurantMenuActivity.this, MealActivity.class);
                        intent.putExtra("path", a.getRestaurantMealPath());
                        startActivity(intent);

                    }

                    else {

                        Log.d("Document Snapshop", "Error while loading restaurant from database");
                        finish();
                    }


                }
            });
            layout.addView(mealitem);
        }

        setImage();
    }

    private void getUris(){

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference listRef = storage.getReference().child("restaurants/" + restaurant);

        listRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for (StorageReference item : listResult.getItems()) {

                    ///restaurants/dintaifung/beef_soup.jpg

                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            String[] s =  uri.getLastPathSegment().split("/");
                            String temp = s[s.length - 1];
                            String id = temp.substring(0, temp.length() - 4);

                            Log.e("555555555555555555555555555555555555555", id);

                            urisMenu.put(id.replaceAll("\\s+|\\r|\\n|\\t", ""), uri);
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            setImage();
                        }
                    });
//                    /restaurants/dintaifung/beef_soup.jpg

                }
            }
        });

        setImage();
    }

    private void setImage(){

        for ( String id : urisMenu.keySet() ) {
            if (imageViewHashMap.containsKey(id)) {

                ImageView imageView = imageViewHashMap.get(id);

                Glide.with(RestaurantMenuActivity.this)
                        .load(urisMenu.get(id))
                        .into(imageView);
            }
        }
    }
}
