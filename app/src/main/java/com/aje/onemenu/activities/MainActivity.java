package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.aje.onemenu.R;
import com.aje.onemenu.settings.UserInfoActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

    public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

        SignInButton signInButton;
        private GoogleApiClient googleApiClient;
        private static final int SIGN_IN = 1;
        SharedPreferences preferences;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            preferences = getSharedPreferences("myPrefs", MODE_PRIVATE);

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

            googleApiClient = new GoogleApiClient.Builder(this).enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

            signInButton = findViewById(R.id.google_sign_inB);
            signInButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                    startActivityForResult(intent, SIGN_IN);
                }
            });
        }

        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(requestCode, resultCode, data);

            if(requestCode == SIGN_IN){
                GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                int statusCode = result.getStatus().getStatusCode();

                Toast.makeText(this, "Code: " + result.getSignInAccount(), Toast.LENGTH_LONG).show();

                if(result.isSuccess()){
                    //firebaseAuthWithGoogle(result.getSignInAccount());
                    startActivity(new Intent(MainActivity.this, UserInfoActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(this, R.string.LoginFail, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }