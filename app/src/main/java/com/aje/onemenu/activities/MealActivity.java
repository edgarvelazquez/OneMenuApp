package com.aje.onemenu.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.ScrollingMovementMethod;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.aje.onemenu.R;
import com.aje.onemenu.classes.Meal;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.util.Arrays;


public class MealActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private DocumentReference mealInformationReference;
    private Meal mealInformation;
    private Button reviewButton;
    private TextView textViewName;
    private TextView allInfo;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        imageView = findViewById(R.id.meal_image);
        db = FirebaseFirestore.getInstance();
        final String path = getIntent().getStringExtra("path");

        String[] idFireStorage = path.split("/");
        getUriImage(idFireStorage[1] + "/" + idFireStorage[3]);
        mealInformationReference = db.document(path);

        reviewButton = findViewById(R.id.review_button);

        mealInformationReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){

                    mealInformation = documentSnapshot.toObject(Meal.class);
                    textViewName=findViewById(R.id.item_name);
                    textViewName.setText(mealInformation.getName());

                    allInfo= findViewById(R.id.item_description);
                    StringBuilder meatList= new StringBuilder();
                    for (String a: mealInformation.getMeat()){
                        meatList.append(a + "\n");
                    }
                    StringBuilder vegetableList= new StringBuilder();
                    for (String a: mealInformation.getVegetable()){
                        vegetableList.append(a + "\n");
                    }
                    StringBuilder extrasList= new StringBuilder();
                    for (String a: mealInformation.getExtras()){
                        extrasList.append(a + "\n");
                    }
                    SpannableString description = new SpannableString("Description");
                    SpannableString meat = new SpannableString("Meat");
                    SpannableString vegetable = new SpannableString("Vegetable");
                    SpannableString extras = new SpannableString("Extras");
                    StyleSpan bold = new StyleSpan(Typeface.BOLD);
                    StyleSpan bold1 = new StyleSpan(Typeface.BOLD);
                    StyleSpan bold2 = new StyleSpan(Typeface.BOLD);
                    StyleSpan bold3 = new StyleSpan(Typeface.BOLD);
                    description.setSpan(bold, 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    meat.setSpan(bold1, 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    vegetable.setSpan(bold2, 0, 9, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    extras.setSpan(bold3, 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    allInfo.setMovementMethod(new ScrollingMovementMethod());

                    SpannableStringBuilder completeInfo= new SpannableStringBuilder();
                    completeInfo.append(description);
                    completeInfo.append("\n" + mealInformation.getDescription()+ "\n\n");
                    if(meatList.length()>1){
                        completeInfo.append(meat);
                        completeInfo.append("\n"+ meatList+"\n");
                    }
                    if(vegetableList.length()>1){
                        completeInfo.append(vegetable);
                        completeInfo.append("\n"+vegetableList+"\n");
                    }
                    if(extrasList.length()>1){
                        completeInfo.append(extras);
                        completeInfo.append("\n"+ extrasList);
                    }

                    allInfo.setText( completeInfo);


                }else {
                    Log.d("Connection Failed", "We were unable to get the document Meal from db");
                }
            }
        });

//        mealInformationReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
//                if (task.isSuccessful()){
//
////                    Log.d("Document Meal Description", task.getResult().toObject(Meal.class).getDescription());
//
//                    if( task.getResult().exists()){
//                        mealInformation = task.getResult().toObject(Meal.class);
//                        textViewName=findViewById(R.id.item_name);
//                        textViewName.setText(mealInformation.getName());
//                        textViewDescription=findViewById(R.id.item_description);
//                        textViewDescription.setText("Description\n" + mealInformation.getDescription());
//                        textViewMeat=findViewById(R.id.item_meat);
//                        textViewMeat.setText("Meat\n"+Arrays.toString(mealInformation.getMeat().toArray()));
//                        textViewVegetable=findViewById(R.id.item_vegetable);
//                        textViewVegetable.setText("Vegetables\n"+Arrays.toString(mealInformation.getVegetable().toArray()));
//                        textViewExtras=findViewById(R.id.item_extras);
//                        textViewExtras.setText("Extras\n"+Arrays.toString(mealInformation.getMeat().toArray()));
//
//                    }
//                }
//                else {
//                    Log.d("Connection Failed", "We were unable to get the document Meal from db");
//                }
//            }
//        });

        reviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MealActivity.this, FoodDescription.class);

                String restId[] = path.split("/");

//                Log.d("ID NOOOOOOOOOOOOOOOOOOOOOOOOOOOOOO", restId[1]);
//                Log.d("MEAL NOOOOOOOOOOOOOOOOOOOOOOOOOOOO", restId[restId.length - 1]);

                intent.putExtra("restaurantId", restId[1] + "/" + restId[restId.length - 1]);
                startActivity(intent);

            }
        });
    }

    private void getUriImage(String id){

        Log.e("================================================",
                id);

        final String[] s = id.split("/");

        StorageReference sReference = FirebaseStorage.getInstance().getReference()
                .child("restaurants/" + s[0] + "/");

        Log.e("99999999999999999999999999999999999999999999999999",
                s[0]);

        sReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {

                for(StorageReference item : listResult.getItems()){

                    String[] a =  item.getPath().split("/");
                    String aa = a[3];
                    String aaa = aa.substring(0, aa.length() - 4);

                    if(s[1].equals(aaa)){

                        item.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(MealActivity.this)
                                        .load(uri)
                                        .into(imageView);


                            }
                        });
                    }
                }
            }
        });
    }
}
