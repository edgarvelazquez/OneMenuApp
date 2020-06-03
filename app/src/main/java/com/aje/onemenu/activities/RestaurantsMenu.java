package com.aje.onemenu.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Restaurant;
import com.google.firebase.database.DatabaseReference; //database reference needed
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;


import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.firestore.DocumentSnapshot;

import android.widget.EditText; //EDIT TEXT NEEDED

import java.util.ArrayList;
import java.util.List;


public class RestaurantsMenu extends AppCompatActivity {
//EditText address,description,number;
TextView address,description,number;

Button btn;
//Button btnsave;
DatabaseReference reff;

//extra
    private FirebaseFirestore db;
    private List<Restaurant> restaurantList;

    private TextView in_n_out;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurants_menu);
        address=(TextView) findViewById(R.id.in_n_out);
        btn=(Button) findViewById(R.id.restaurant_one);
        //description=(TextView) findViewById(R.id.description)

                //Test conection
        Toast.makeText(RestaurantsMenu.this,  "Firebase connection success", Toast.LENGTH_LONG).show();
        EditText address,description,number;
        //Test connection

        restaurantList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder().setPersistenceEnabled(true).build();
        db.setFirestoreSettings(settings);



        //https://www.youtube.com/watch?v=NWEfGZeDuAY
        db.collection("restaurants").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){
                            List<DocumentSnapshot> list=queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot d: list){
                                Restaurant p = d.toObject(Restaurant.class);

                                restaurantList.add(p);

                                Log.d("test", "test + " +  d.getId()+" "+ p.getName());

                                in_n_out = findViewById(R.id.in_n_out);

                                in_n_out.setText(d.getId() + "      " + p.getName() );

                            }

                        }
                        else{
                            Log.d("fail", "fail");
                        }

                    }
                });




    //jose


        //in_n_out.setText(restaurantList.get(0).getName());
        //jose
        Log.d("hey","hey");
    }
}