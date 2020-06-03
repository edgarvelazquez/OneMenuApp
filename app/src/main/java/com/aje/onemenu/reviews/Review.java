package com.aje.onemenu.reviews;

import android.net.Uri;

import com.google.firebase.storage.StorageReference;

public class Review {

    private float rating;
    private String review;
    private String url;

    public Review(){}

    public double getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
