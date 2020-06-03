//package com.aje.onemenu.activities;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.aje.onemenu.R;
//import com.aje.onemenu.classes.User;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.OptionalPendingResult;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.firestore.DocumentReference;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.FirebaseFirestoreSettings;
//import com.squareup.picasso.Picasso;
//
//
//public class ProfileActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {
//
//    private ImageView profile_image;
//    private TextView name;
//    private TextView email;
//    private Button signOutButton;
//    private Button EdgarButton;
//    private Button AlexButton;
//    private Button pushUserFirebase;
//    private FirebaseFirestore db;
//    private User currentUser;
//    private DocumentReference usersInfo;
//
//    private GoogleApiClient googleApiClient;
//    private GoogleSignInOptions gso;
//
////    https://www.youtube.com/watch?v=uPg1ydmnzpk
////    17:30
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_profile);
//
//        db = FirebaseFirestore.getInstance();
//
//        profile_image = findViewById(R.id.profile_image);
//        name = findViewById(R.id.name);
//        email = findViewById(R.id.email);
//        signOutButton = findViewById(R.id.signOutButton);
//        pushUserFirebase = findViewById(R.id.pushUser);
//
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//
//        googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this,this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();
//
//        signOutButton.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//                Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
//                    @Override
//                    public void onResult(@NonNull Status status) {
//                        if(status.isSuccess())
//                            gotoMainActivity();
//                        else
//                            Toast.makeText(ProfileActivity.this, "Log Out Failed.", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });
//
//        //Restaurants_menu Button
//        EdgarButton = findViewById(R.id.edgarButton);
//        EdgarButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, RestaurantsMenu.class);
//                startActivity(intent);
//            }
//        });
//
//        AlexButton = findViewById(R.id.alexButton);
//        AlexButton.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ProfileActivity.this, FoodPreference.class);
//                startActivity(intent);
//            }
//        });
//
//        pushUserFirebase.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                if(!currentUser.getName().equals(null)){
//                    Log.d("successful", "pushed to database");
//
//                    usersInfo = db.collection("users").document(currentUser.getId());
//                    usersInfo.set(currentUser).addOnCompleteListener(new OnCompleteListener<Void>() {
//                        @Override
//                        public void onComplete(Task<Void> task) {
//                            Log.d("successful", "We pushed to db");
//                            if(task.isSuccessful()){
//                                String test = "Account created";
//                                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
//                            }
//                            else {
//                                Toast.makeText(getApplicationContext(), "Error, fail to connect to database", Toast.LENGTH_SHORT).show();
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void gotoMainActivity() {
//
//        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
//        fileList();
//
//    }
//
//    @Override
//    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
//
//    }
//
//    private void setUserInfo(GoogleSignInAccount account){
//
//        currentUser = new User();
//
//        currentUser.setEmail(account.getEmail());
//        currentUser.setId(account.getId());
//        currentUser.setName(account.getDisplayName());
//
//        name.setText(account.getDisplayName());
//        email.setText(account.getEmail());
//        Picasso.get().load(account.getPhotoUrl()).placeholder(R.mipmap.ic_launcher).into(profile_image);
//    }
//
//    private void handleSignInResult(GoogleSignInResult result) {
//        if(result.isSuccess()) {
//
////          GoogleSignInAccount account = result.getSignInAccount();
//            setUserInfo(result.getSignInAccount());
//        }
//        else {
//            gotoMainActivity();
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        OptionalPendingResult<GoogleSignInResult> opt = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
//
//        if(opt.isDone()) {
//            GoogleSignInResult result = opt.get();
//            handleSignInResult(result);
//        }
//
//        else {
//            opt.setResultCallback(new ResultCallback<GoogleSignInResult>() {
//                @Override
//                public void onResult(@NonNull GoogleSignInResult result) {
//                    handleSignInResult(result);
//
//                }
//            });
//        }
//    }
//}