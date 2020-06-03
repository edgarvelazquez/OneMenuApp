package com.aje.onemenu.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.aje.onemenu.R;
import com.aje.onemenu.activities.FoodPreference;
import com.aje.onemenu.activities.MainActivity;
import com.aje.onemenu.activities.MealActivity;
import com.aje.onemenu.classes.Meal;
import com.aje.onemenu.classes.User;
import com.aje.onemenu.classes.UserId;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserInfoActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener  {

    private ImageView profile_image;
    private TextView name;
    private TextView email;
    private Button signOutButton;
    private FirebaseFirestore db;
    private User currentUser;
    private DocumentReference usersInfo;
    private GoogleSignInAccount account;
    private Button edithPreferences;
    private boolean loaded; // Checks is the list of ingredients is already loaded from Database
    private Button edgar;

    private GoogleApiClient googleApiClient;
    private GoogleSignInOptions gso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        loaded = false;
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        profile_image = findViewById(R.id.profile_image);
        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        signOutButton = findViewById(R.id.signOutButton);
        edithPreferences = findViewById(R.id.edith_preferences_button);

        edithPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(loaded) {

                    Bundle extra = new Bundle();

                    extra.putSerializable("meats", currentUser.getProtein());
                    extra.putSerializable("vegetables", currentUser.getVegetables());
                    extra.putSerializable("mics", currentUser.getExtras());

                    Log.d("successful", currentUser.getProtein().toString());

                    Intent intent = new Intent(UserInfoActivity.this, FoodPreference.class);

                    intent.putExtra("extra", extra);

                    intent.putExtra("id", currentUser.getId());
                    startActivity(intent);
                }
                else {
                    Toast.makeText(UserInfoActivity.this
                            , "Please wait while we update the data from the database."
                            , Toast.LENGTH_SHORT).show();
                }
            }
        });

        signOutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        if(status.isSuccess())
                            gotoMainActivity();
                        else
                            Toast.makeText(getApplicationContext(), "Log Out Failed.", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        edgar = findViewById(R.id.edgarButton);

        edgar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Meal meal = new Meal();

                db = FirebaseFirestore.getInstance();
                final DocumentReference mealDocument = db.collection("restaurants").document("dennys")
                        .collection("dennys_menu").document("meatloaf");

                mealDocument.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull final Task<DocumentSnapshot> task) {

                        if(task.isSuccessful()){
                            DocumentSnapshot mealSnap = task.getResult();

                            if(mealSnap.exists()){

                                Log.d("TEST", mealDocument.getPath());

                                Intent intent = new Intent(UserInfoActivity.this, MealActivity.class);
                                intent.putExtra("path", mealDocument.getPath());
                                startActivity(intent);

                            }

                            else {

                                Log.d("Document Snapshop", "Error while loading restaurant from database");
                                finish();
                            }
                        }
                    }
                });
            }
        });

    }

    private void gotoMainActivity() {

        startActivity(new Intent(getApplicationContext(), MainActivity.class));
        fileList();

    }

    private void setUserInfoFromDatabase(User account){

        currentUser = account;

        Log.d("Error", currentUser.getProtein().toString());
        updateUI();
    }

    private void setUserInfoFromGoogleSignIn(GoogleSignInAccount account){

        currentUser = new User();
        currentUser.setEmail(account.getEmail());
        currentUser.setId(account.getId());
        currentUser.setName(account.getDisplayName());

        updateUI();
    }

    private void updateUI(){

        loaded = true;
        name.setText(currentUser.getName());
        email.setText(currentUser.getEmail());
        Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profile_image);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()) {

          account = result.getSignInAccount();
          UserId.getInstance().setUserId(account.getId());

          db = FirebaseFirestore.getInstance();
          usersInfo = db.collection("users").document(account.getId());
          usersInfo.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            Log.d("UserInfoActivity", "Document exists!");

                            setUserInfoFromDatabase(document.toObject(User.class));

                        } else {

                            setUserInfoFromGoogleSignIn(account);
                            Log.d("UserInfoActivity", "Document does not exist!");

                            usersInfo.set(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(Task<Void> task) {
                                    Log.d("successful", "We pushed to db");

                                    String text;
                                    if(task.isSuccessful()){
                                        text = "Account created";
                                    } else {
                                        text = "Error, fail to connect to database";
                                    }

                                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                    } else {
                        Log.d("UserInfoActivity", "Failed with: ", task.getException());
                    }
                }
            });
        }
        else {
            Toast.makeText(getApplicationContext(), "Error sign in with Google account", Toast.LENGTH_SHORT).show();
            gotoMainActivity();
        }
    }

    private void pushUserDatatoDB(){

        usersInfo = db.collection("users").document(currentUser.getId());
        usersInfo.set(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {
                Log.d("successful", "We pushed to db");
                if(task.isSuccessful()){
                    String test = "Account created";
                    Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Error, fail to connect to database", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        loaded = false;
        OptionalPendingResult<GoogleSignInResult> opt = Auth.GoogleSignInApi.silentSignIn(googleApiClient);

        if(opt.isDone()) {
            GoogleSignInResult result = opt.get();
            handleSignInResult(result);
        }

        else {
            opt.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(@NonNull GoogleSignInResult result) {
                    handleSignInResult(result);

                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
