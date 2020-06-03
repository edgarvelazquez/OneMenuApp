package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.aje.onemenu.CustomAdapterRestaurant;
import com.aje.onemenu.R;
import com.aje.onemenu.classes.Recommendations;
import com.aje.onemenu.classes.Restaurant;
import com.aje.onemenu.classes.User;
import com.aje.onemenu.classes.UserId;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RecommendedRestaurantActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private ArrayList<Restaurant> restaurantList;
    private ListView listViewRecommendations;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private HashMap<String, Uri> uriImages;
    private ArrayList<String> recommendationArray;
    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommended_restaurant);

        listViewRecommendations = findViewById(R.id.recommendation_list_view);
        restaurantList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        recommendationArray = new ArrayList<>();
        resultTextView = findViewById(R.id.recommendation_textview_noresult);

        db.collection("recommendations").document(UserId.getInstance().getUserId()).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        if(documentSnapshot.exists()){

                            Recommendations recommendations = new Recommendations();
                            recommendations = documentSnapshot.toObject(Recommendations.class);

                            Log.d("===============================", recommendations.getA());
                            Log.d("==============================================", recommendations.getB());

                            getRecommendations(recommendations);
                            //loadRestaurants(recommendations);
                        }
                        else {
                            //Add about running scan
                            restaurantList.clear();
                        }

                        //updateUI();
                    }
                });
    }

    private void getRecommendationsHelper(String cuisine){

        db.collection("restaurants").whereEqualTo("cuisine", cuisine)
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: list){

                        Log.d("=====================RecommendedRestaurantActivity", "111111111111111111111111" );
                        restaurantList.add(d.toObject(Restaurant.class));
                    }
                }
                else {
                    Log.d("RecommendedRestaurantActivity", "getRecommendationsHelper no document" );
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                updateUI();
            }
        });
    }

    private void getRecommendations(Recommendations recommendations){

        final String recommendationB = recommendations.getB();
        restaurantList = new ArrayList<>();

        db.collection("restaurants").whereEqualTo("cuisine", recommendations.getA())
                .get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(!queryDocumentSnapshots.isEmpty()) {

                    List<DocumentSnapshot> list = queryDocumentSnapshots.getDocuments();

                    for(DocumentSnapshot d: list){

                        Log.d("=================  RecommendedRestaurantActivity", "000000000000000000000000000000" );
                        restaurantList.add(d.toObject(Restaurant.class));
                    }
                }
                else {
                    restaurantList = new ArrayList<>();
                    Log.d("RecommendedRestaurantActivity", "getRecommendations no document" );
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(!recommendationB.isEmpty()) getRecommendationsHelper(recommendationB);
                else updateUI();
            }
        });
    }

    private void updateUI(){

        if(restaurantList.size() > 0){

            resultTextView.setText("");

            uriImages = new HashMap<>();

            for(Restaurant r: restaurantList){

                storage = FirebaseStorage.getInstance();
                storageReference = storage.getReference().child("restaurants/").child(r.getName() + "/").child("icon.png");

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {

                        Log.d("FoodDescription last path", uri.getLastPathSegment());

                        String[] s =  uri.getLastPathSegment().split("/");
                        String id = s[1];

                        Log.d("=================================== FoodDescription uri to string", id);

                        uriImages.put(id, uri);
                        loadUI();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {

                        //add update
                        loadUI();
                    }
                });
            }
        } else {

            resultTextView.setText("There is no recommendations for you at this time. " +
                    " Please change your preferences to make a new analysis. ");

        }
    }

    private void loadUI(){

        assignRecommendations();

        CustomAdapterRestaurant customAdapterRestaurant =
                new CustomAdapterRestaurant(RecommendedRestaurantActivity.this, restaurantList, uriImages);

        listViewRecommendations.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Restaurant restaurant = (Restaurant) listViewRecommendations.getItemAtPosition(position);

                Intent intent = new Intent(RecommendedRestaurantActivity.this,RestaurantMenuActivity.class);
                intent.putExtra("restaurant", restaurant.getName());
                intent.putStringArrayListExtra("userRecommendation", recommendationArray);
                startActivity(intent);
            }
        });

        listViewRecommendations.setAdapter(customAdapterRestaurant);
    }

    private void assignRecommendations() {

        db.collection("users").document(UserId.getInstance().getUserId())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                if(documentSnapshot.exists()){

                    ArrayList<String> s = new ArrayList<>();
                    if(!documentSnapshot.toObject(User.class).getProtein().isEmpty()){

                        recommendationArray = documentSnapshot.toObject(User.class).getProtein();
                    }
                }
            }
        });


    }
}
