package com.aje.onemenu.activities;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.aje.onemenu.CustomAdapterRestaurant;
import com.aje.onemenu.R;
import com.aje.onemenu.classes.Restaurant;
import com.aje.onemenu.classes.User;
import com.aje.onemenu.classes.UserId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RestaurantsList extends AppCompatActivity {

    private ListView listView;
    private FirebaseFirestore db;
    private ArrayList<Restaurant> restaurantsArray = new ArrayList<>();
    private HashMap<String, Uri> uriImages = new HashMap<>();
    private ArrayList<String> restaurantNames = new ArrayList<>();
    private ArrayList<String> restaurantDescriptions = new ArrayList<>();
    private String userID;
    private User user;
    private ArrayList<User> listOfUsers = new ArrayList<>();
    private ArrayList<String> userRecommendation = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_restaurants_list);
        userID = UserId.getInstance().getUserId();

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);
        listView = findViewById(R.id.list_view);

        db.collection("users").whereEqualTo("id", userID).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    int counter = 0;
                    for (DocumentSnapshot d : list) {

//                        Toast.makeText(RestaurantsList.this, "add user", Toast.LENGTH_SHORT).show();
                        User a = d.toObject(User.class);
                        userRecommendation = a.getProtein();
                        listOfUsers.add(a);
//                        Toast.makeText(RestaurantsList.this, ""+ listOfUsers.size(), Toast.LENGTH_SHORT).show();

//                        Toast.makeText(RestaurantsList.this, ""+a.getId()+" " +a.getName()+counter++, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


        final RelativeLayout rl = findViewById(R.id.list_Rlayout);
        final TextView nullHandler = findViewById(R.id.no_result);
        nullHandler.setVisibility(View.GONE);


        //need to add .whereequalto("cuisine", cuisine)
        db.collection("restaurants").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if(!queryDocumentSnapshots.isEmpty()){

                    List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: list){

                        Restaurant p = d.toObject(Restaurant.class);
                        restaurantNames.add(p.getName());
                        restaurantDescriptions.add(p.getDescription());
                        restaurantsArray.add(p);
                    }

                    loadUris();
                }

            }
        });

        SearchView simpleSearchView = findViewById(R.id.simpleSearchView);
        simpleSearchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {

                        nullHandler.setVisibility(View.GONE);
                        restaurantNames = new ArrayList<>();
                        restaurantDescriptions = new ArrayList<>();

                        db.collection("restaurants").whereGreaterThanOrEqualTo("name", s.toLowerCase())
                                .whereLessThanOrEqualTo("name", s.toLowerCase()+"\uF7FF").get()
                                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                    @Override
                                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                                        if(!queryDocumentSnapshots.isEmpty()){
                                            nullHandler.setVisibility(View.GONE);
                                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();
                                            restaurantsArray = new ArrayList<>();
                                            restaurantNames = new ArrayList<>();
                                            restaurantDescriptions = new ArrayList<>();

                                            for(DocumentSnapshot d: list){

                                                Restaurant p = d.toObject(Restaurant.class);
                                                restaurantsArray.add(p);
                                                restaurantNames.add(p.getName());
                                                restaurantDescriptions.add(p.getDescription());
                                            }

                                            updateUI();
                                        }
                                        else{
                                            restaurantsArray = new ArrayList<>();
                                            restaurantNames = new ArrayList<>();
                                            restaurantDescriptions = new ArrayList<>();
                                            updateUI();
                                            nullHandler.setVisibility(View.VISIBLE);
                                            //nullHandler.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                                        }
                                    }
                                });
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                }
        );

    }

    private void loadUris(){

        if(restaurantsArray.size() > 0){

            for(Restaurant r: restaurantsArray){

                FirebaseStorage storage = FirebaseStorage.getInstance();
                StorageReference storageReference = storage.getReference().child("restaurants/").child(r.getName() + "/").child("icon.png");

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Log.d("RestaurantList last path", uri.getLastPathSegment());

                        String[] s =  uri.getLastPathSegment().split("/");
                        String id = s[1];

                        Log.d("=================================== RestaurantList uri to string", id);

                        uriImages.put(id, uri);
                        updateUI();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        updateUI();
                    }
                });
            }
        }



    }

    private void updateUI(){

        CustomAdapterRestaurant customAdapterRestaurant =
                new CustomAdapterRestaurant(RestaurantsList.this, restaurantsArray, uriImages);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Restaurant restaurant = (Restaurant) listView.getItemAtPosition(position);

                Intent intent = new Intent(RestaurantsList.this, RestaurantMenuActivity.class);
                intent.putExtra("restaurant", restaurant.getName());
                intent.putStringArrayListExtra("userRecommendation", userRecommendation);
                startActivity(intent);
            }
        });

        listView.setAdapter(customAdapterRestaurant);


    }
}