package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.UserId;
import com.aje.onemenu.reviews.CustomAdapterReview;
import com.aje.onemenu.reviews.Review;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class FoodDescription extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private EditText textReview;
    private float foodRating;
    private RelativeLayout reviewsLayout;
    private RelativeLayout addReviewLayout;
    private Button addPhotoButton;
    private Button submitReviewButton;
    private ImageView imageView;
    private static int RESULT_LOAD_IMAGE = 1;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private Uri selectedImageUri;
    private String userID;
    private String restaurantID;
    private String imageReviewPath;
    private ArrayList<String> reviewArray;
    private ArrayList<String> urlArray;
    private HashMap<String, StorageReference> hashMapImages;
    private ListView listViewReview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_description);

        mAuth = FirebaseAuth.getInstance();

        Log.d("FOODDESCRIPTION", "On create loaded");

        db = FirebaseFirestore.getInstance();
        userID = UserId.getInstance().getUserId();
        restaurantID = getIntent().getStringExtra("restaurantId");

        Log.d("Review Activity", userID);
        Log.d("Review Activity", restaurantID);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reviewsLayout = findViewById(R.id.reviews_layout);
        addReviewLayout = findViewById(R.id.add_review_layout);
        addPhotoButton = findViewById(R.id.choose_image_button);
        submitReviewButton = findViewById(R.id.submit_review_button);
        imageView = findViewById(R.id.food_image_view);
        textReview = findViewById(R.id.get_text_review);
        listViewReview = findViewById(R.id.list_view_reviews);
        foodRating = 6;

        Log.d("FOODDESCRIPTION", "variables asigned");

        updateReviewList();

        RatingBar rate = findViewById(R.id.ratingBar);
        rate.setOnRatingBarChangeListener( new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                foodRating = v;
            }
        });

        addPhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

                startActivityForResult(i, RESULT_LOAD_IMAGE);
            }
        });

        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(FoodDescription.this, "" + foodRating, Toast.LENGTH_SHORT).show();

                try {
                    uploadImage();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                setReviewLayoutVisible();
            }
        });

        Button addReviewB = findViewById(R.id.add_review_button);

        addReviewB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setReviewLayoutInvisible();
            }
        });

    }

    private void updateReviewList(){

//        String rest[] = restaurantID.split("/");
//        String restaurantName = rest[0];
//        String mealName = rest[rest.length - 1];

//        db.collection("reviews").document(restaurantName).collection(mealName).get()

        String[] path = restaurantID.split("/");

        final ArrayList<Review> list = new ArrayList<>();
        final HashMap<String, Uri> mapReviews = new HashMap<>();
        final ArrayList<String> ids = new ArrayList<>();

        db.collection("reviews").document(path[0]).collection(path[1]).get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                        for(DocumentSnapshot d: queryDocumentSnapshots){

                            Log.d("99999999999999999999999999999999999", d.getId());

                            list.add(d.toObject(Review.class));
                            ids.add(d.getId());
                        }
                    }
                });

//        final ArrayList<StorageReference> storageList = new ArrayList<>();
        final ArrayList<Uri> uriList = new ArrayList<>();

        storageReference = storageReference.child("reviews/" + restaurantID);

        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {

                    item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {

                            Log.d("FoodDescription", "We are downloading the images");
                            Log.d("FoodDescription uri to string", uri.toString());
                            Log.d("FoodDescription last path", uri.getLastPathSegment());

                            String[] s =  uri.getLastPathSegment().split("/");
                            String id = s[s.length-1].substring(0, 21);
                            //id = id[id.length-1].split(".");

                            Log.d("FoodDescription ID", id);

                            mapReviews.put(id, uri);
                            uriList.add(uri);
                            updateUI(list, mapReviews, ids);
                        }
                    });

//                    storageList.add(item);
//                    hashMapImages.put(item.getName(), item);
                }
            }
        }).addOnCompleteListener(new OnCompleteListener<ListResult>() {
            @Override
            public void onComplete(@NonNull Task<ListResult> task) {

                updateUI(list, mapReviews, ids);
            }
        });
    }

    private void updateUI(ArrayList<Review> list, HashMap<String, Uri> mapReviewsURI, ArrayList<String> ids){

        Log.d("FoodDescription", "We are updating UI");

        listViewReview.setAdapter(new CustomAdapterReview(FoodDescription.this, list, mapReviewsURI, ids));
    }


    private void setReviewLayoutInvisible() {

        addReviewLayout.setVisibility(View.VISIBLE);
        reviewsLayout.setVisibility(View.INVISIBLE);
    }
    private void setReviewLayoutVisible() {
        reviewsLayout.setVisibility(View.VISIBLE);
        addReviewLayout.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            selectedImageUri = data.getData();

//            imageView.setImageURI(selectedImageUri); //Picasso.get().load(selectedImage).into(imageView);

            Glide.with(FoodDescription.this)
                    .load(selectedImageUri)
                    .into(imageView);
        }
    }

    private void uploadImage() throws FileNotFoundException {

        final Review reviewInfo = new Review();
        reviewInfo.setRating(foodRating);
        reviewInfo.setReview(textReview.getText().toString());

        if(selectedImageUri != null && !selectedImageUri.equals(Uri.EMPTY)){

            Log.d("PATH test", selectedImageUri.getPath());
            Log.d("LASTPATH test", selectedImageUri.getLastPathSegment());

            if(userID.equals(null) || restaurantID.equals(null)) {
                userID = "error";
                restaurantID = "error";
            }

            Log.d("ERROR FROMINTENT", restaurantID);
            Log.d("ERROR FROMINTENT", userID);

            String reviewPathFINAL= "reviews/" + restaurantID;


//            storageReference = storageReference.child( reviewPathFINAL+ "/" + userID + ".jpg");

            storageReference = storageReference.child(userID + ".jpg");

            Bitmap bitmap  = decodeUri(selectedImageUri);

//            UploadTask uploadTask = storageReference.putFile(Uri.fromFile(image));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            UploadTask uploadTask = storageReference.putBytes(data);

            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    imageReviewPath = taskSnapshot.getMetadata().getPath();
                    reviewInfo.setUrl(imageReviewPath);
                    reviewInfo.setRating(foodRating);
                    Log.d("IMAGE PATH", imageReviewPath);

                    String[] restID = restaurantID.split("/");

                    db.collection("reviews").document(restID[0]).collection(restID[1]).document(userID)
                            .set(reviewInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Log.d("REVIEW", "We pushed the review to db");
                            if(task.isSuccessful()){
                                String test = "Review Added";
                                Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Log.d("REVIEW", "There was an error with the completition of the review");
//                    Toast.makeText(getApplicationContext(), "Error, fail to connect to database", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            });
        }
        else {

            String[] restID = restaurantID.split("/");

            db.collection("reviews").document(restID[0]).collection(restID[1]).document(userID)
                    .set(reviewInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Log.d("REVIEW", "We pushed the review to db");
                    if(task.isSuccessful()){
                        String test = "Review Added";
                        Toast.makeText(getApplicationContext(), test, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("REVIEW", "There was an error with the completition of the review");
//                    Toast.makeText(getApplicationContext(), "Error, fail to connect to database", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private Bitmap decodeUri(Uri uri)
            throws FileNotFoundException {

        Context c = (FoodDescription.this);

        int requiredSize = 1000;

        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o);

        int width_tmp = o.outWidth
                , height_tmp = o.outHeight;
        int scale = 1;

        while(true) {
            if(width_tmp / 2 < requiredSize || height_tmp / 2 < requiredSize)
                break;
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(c.getContentResolver().openInputStream(uri), null, o2);
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        signInAnonymously();
    }

    private void signInAnonymously() {

        mAuth.signInAnonymously()
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("FoodDescription", "signInAnonymously:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("FoodDescription", "signInAnonymously:failure", task.getException());
                        }

                        // ...
                    }
                });
    }
}

