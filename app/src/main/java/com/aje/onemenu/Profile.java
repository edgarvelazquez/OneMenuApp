package com.aje.onemenu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.aje.onemenu.activities.FoodPreference;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }
    public void changeActivity(View view){
        Intent intent = new Intent(Profile.this, FoodPreference.class);
        startActivity(intent);
        finish();
    }
}
