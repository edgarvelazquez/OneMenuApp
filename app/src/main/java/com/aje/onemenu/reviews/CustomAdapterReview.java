package com.aje.onemenu.reviews;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.aje.onemenu.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapterReview extends BaseAdapter {

    private Context context;
    private ArrayList<Review> reviews;
    private HashMap<String, Uri> uriReviews;
    private ArrayList<String> ids;

    public CustomAdapterReview(Context context, ArrayList<Review> reviews, HashMap<String, Uri> uriReviews, ArrayList<String> ids) {
        this.context = context;
        this.reviews = reviews;
        this.uriReviews = uriReviews;
        this.ids = ids;
    }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.activity_fragment_review, parent, false);
        }

        final Review review = (Review) this.getItem(position);

        TextView reviewText = convertView.findViewById(R.id.review_fragment_textview);
        ImageView reviewImage = convertView.findViewById(R.id.review_fragment_imageview);
        RatingBar reviewRatingBar = convertView.findViewById(R.id.review_fragment_rating_bar);

        reviewText.setText(review.getReview());
        reviewRatingBar.setRating((float)review.getRating());

//        reviewImage.setImageURI(review.getStorageReference());

//        Glide.with(this.context)
//                .load(storageReferences.get(position))
//                .into(reviewImage);

        if(uriReviews.containsKey(ids.get(position)) ) {

            Glide.with(context)
                    .load(uriReviews.get(ids.get(position)))
                    .into(reviewImage);
        }

        //    reviewImage.setImageURI(uriReview.get(position));

//        final ScrollView scrollView = convertView.findViewById(R.id.scroll_review_text);

        reviewText.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {
                // Disallow the touch request for parent scroll on touch of child view
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        reviewText.setMovementMethod(new ScrollingMovementMethod());


//        convertView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Toast.makeText(context, review.getReview(), Toast.LENGTH_LONG).show();
//            }
//        });


        return convertView;
    }
}
