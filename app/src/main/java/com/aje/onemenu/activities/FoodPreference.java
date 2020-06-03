package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Ingredients;
import com.aje.onemenu.classes.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class FoodPreference extends AppCompatActivity {

    private ArrayList<String> meats = new ArrayList<>();
    private ArrayList<String> veggie = new ArrayList<>();
    private ArrayList<String> misc = new ArrayList<>();
    private ArrayList<String> currentSelectionMeats = new ArrayList<>();
    private ArrayList<String> currentSelectionVeg = new ArrayList<>();
    private ArrayList<String> currentSelectionMics = new ArrayList<>();
    private DocumentReference userPreference;
    private User user;
    private FirebaseFirestore db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extra = getIntent().getBundleExtra("extra");

        ArrayList<String> temp = (ArrayList<String>) extra.getSerializable("meats");
        if(temp != null) currentSelectionMeats = temp;

        temp = (ArrayList<String>) extra.getSerializable("vegetables");
        if(temp != null) currentSelectionVeg = temp;

        temp = (ArrayList<String>) extra.getSerializable("mics");
        if(temp != null) currentSelectionMics = temp;

        Log.d("SUCCESS",currentSelectionMeats.toString() + currentSelectionMeats.toString());

        db = FirebaseFirestore.getInstance();
        userPreference = db.collection("users").document(getIntent().getStringExtra("id"));

        user = new User();
        getUserPreferenceInstance();

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        float height = dm.widthPixels;

        addMeat();
        addVeggie();
        addMisc();
        this.setContentView(R.layout.activity_food_preference);
        RelativeLayout main = findViewById(R.id.main);

        Log.d("CORRECT","This is after DB");

        final LinearLayout preferenceScreen = findViewById(R.id.preferenceScreen);
        //main.addView(preferenceScreen);
        //preferenceScreen.setOrientation(LinearLayout.VERTICAL);
        final TabLayout tl = findViewById(R.id.tl);
        //preferenceScreen.addView(tl);
        final RelativeLayout rl = findViewById(R.id.rl);
        //preferenceScreen.addView(rl);
        final LinearLayout showMeat = showPreference(meats, currentSelectionMeats);
        showMeat.setHorizontalGravity(Gravity.CENTER);
        final LinearLayout showVeggie = showPreference(veggie, currentSelectionVeg);
        final LinearLayout showMisc = showPreference(misc, currentSelectionMics);
        rl.addView(showMeat);
        rl.addView(showVeggie);
        rl.addView(showMisc);

        showVeggie.setVisibility(View.GONE);
        showMisc.setVisibility(View.GONE);

        final LinearLayout ll2 = findViewById(R.id.ll2);
        //ll2.setOrientation(LinearLayout.VERTICAL);
        //preferenceScreen.addView(ll2);
//                LinearLayout ll3 = showPreferred(pmeats, "meat");
            //ll2.setOrientation(LinearLayout.VERTICAL);
//                ll2.addView(ll3);

        tl.addTab(tl.newTab().setText("Protein"));
        tl.addTab(tl.newTab().setText("Vegetable"));
        tl.addTab(tl.newTab().setText("Extra"));
        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            switch (tl.getSelectedTabPosition()){
                case 0:
                    showVeggie.setVisibility(View.GONE);
                    showMeat.setVisibility(View.VISIBLE);
                    showMisc.setVisibility(View.GONE);
                    break;
                case 1:
                    showMeat.setVisibility(View.GONE);
                    showVeggie.setVisibility(View.VISIBLE);
                    showMisc.setVisibility(View.GONE);
                    break;
                case 2:
                    showMisc.setVisibility(View.VISIBLE);
                    showVeggie.setVisibility(View.GONE);
                    showMeat.setVisibility(View.GONE);
            }
        }
        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
        }
        @Override
        public void onTabReselected(TabLayout.Tab tab) {
        }
        });

        Button next = new Button(this);
        next.setText(R.string.submit);
        preferenceScreen.addView(next);
        next.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                Toast.makeText(getApplicationContext(), "Saving Preferences, Please Wait", Toast.LENGTH_SHORT).show();
                submitForm();
                preferenceScreen.removeView(ll2);
                ll2.removeAllViews();
                LinearLayout list = ListOfPreferred();
                ll2.addView(list);
                preferenceScreen.addView(ll2);

                //Starts the python script

                if(!user.getId().isEmpty()){
                    Client connection = new Client();
                    connection.setUserID(user.getId());
                    Log.d("CLIENT SOCKET", "The client is being executed");
                    connection.execute();
                }

                try {
                    Thread.sleep(1500);

                }catch (Exception e){
                    e.printStackTrace();
                }
                finish();
            }
        });

        Button fd = new Button(this);
        fd.setText("FoodDescription");
        preferenceScreen.addView(fd);
        fd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodPreference.this, RestaurantMenuActivity.class);
                startActivity(intent);
            }
        });
        Button rList = new Button(this);
        rList.setText("List of Restaurants");
        preferenceScreen.addView(rList);
        rList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FoodPreference.this, RestaurantsList.class);

                startActivity(intent);
            }
        });

//        LinearLayout f = new LinearLayout(this);
//        f.setGravity(Gravity.BOTTOM);
//       // FrameLayout fl = new FrameLayout(this);
//        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        //f.setOrientation(LinearLayout.HORIZONTAL)
//        Fragment navbar = new NavBarFragment();
//
//        f.setId((Integer)12345);
//        ft.add(f.getId(), navbar, "hello").commit();
//        main.addView(f);
//        BottomNavigationView =


    }
    private void addMeat(){
        meats = Ingredients.getInstance().getMeats();
    }
    private void addVeggie(){
        veggie = Ingredients.getInstance().getVeggie();
    }
    private void addMisc(){
        misc = Ingredients.getInstance().getMisc();
    }

    /**
     *shows the user the list checkbox with preferred ingredients
     */
    private LinearLayout showPreference(ArrayList<String> ingredients, final ArrayList<String> pIngredients){

        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < ingredients.size(); i++) {
            final CheckBox cb = new CheckBox(getApplicationContext());
            cb.setText(ingredients.get(i));
            if(!pIngredients.equals(null) && pIngredients.contains(ingredients.get(i))){
                cb.setChecked(true);
            }

            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if(b) {
                        pIngredients.add(cb.getText().toString());
                    }
                    else{
                        pIngredients.remove(cb.getText().toString());
                    }
                }
            });
            ll.addView(cb);
        }

        return ll;
    }

    /**
     * shows the list of preferred meat
     */
    private LinearLayout showPreferred(ArrayList<String> list, String name){
        LinearLayout ll = new LinearLayout(this);
        ll.setOrientation(LinearLayout.HORIZONTAL);
        //LinearLayout row = new LinearLayout(this);

        if(list != null){
            for(int j = 0; j < list.size(); j++ ){
                if(j == 0) {
                    TextView group  = new TextView(this);
                    group.setText(new StringBuilder().append(name).append(":").toString());
                    group.setMaxLines(1);
                    group.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    ll.addView(group);
                    TextView tv = new TextView(this);
                    tv.setText(list.get(j));
                    tv.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    tv.setMaxLines(1);
                    ll.addView(tv);
                }else{
                    TextView tv = new TextView(this);
                    tv.setText(new StringBuilder().append(", ").append(list.get(j)).toString());
                    tv.setWidth(600);
                    ll.addView(tv);
                }

            }
        }

        return ll;
    }

    /**
     * creates linearlayout for the tabs in a relativelayout
     */
    private LinearLayout ListOfPreferred(){
        LinearLayout list = new LinearLayout(this);
        list.setOrientation(LinearLayout.VERTICAL);
        LinearLayout ll6 = showPreferred(currentSelectionMeats,"meat");
        LinearLayout ll7 = showPreferred(currentSelectionVeg,"vegetables");
        LinearLayout ll8 = showPreferred(currentSelectionMics, "extras");
        ll6.setOrientation(LinearLayout.VERTICAL);
        ll7.setOrientation(LinearLayout.VERTICAL);
        ll8.setOrientation(LinearLayout.VERTICAL);
        list.addView(ll6);
        list.addView(ll7);
        list.addView(ll8);
        return list;
    }

    private void getUserPreferenceInstance(){

        String id  = userPreference.getId();
        Log.d("Intent", "The id of the user is: " + id);
        if(id.equals(null) || userPreference == null) finish();

        userPreference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("Food Menu", "User info exists!");

                        user = document.toObject(User.class);
                        Log.d("database", "Load ingredients from DB");

                    } else {

                        Toast.makeText(FoodPreference.this
                                , "Please restart the application. There was an error with" +
                                        " the database connection.",
                                Toast.LENGTH_SHORT).show();
                        Log.d("Food Menu", "User does not exist!");
                        finish();
                    }

                } else {
                    Log.d("UserInfoActivity", "Failed with: ", task.getException());
                }
            }
        });

    }

    private void submitForm(){

        user.setProtein(currentSelectionMeats);
        user.setVegetables(currentSelectionVeg);
        user.setExtras(currentSelectionMics);
        userPreference.set(user);
    }


}