package com.aje.onemenu;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.aje.onemenu.classes.Restaurant;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

public class CustomAdapterRestaurant extends BaseAdapter {


    private Context context;
    private HashMap<String, Uri> uriPictures;
    private ArrayList<Restaurant> listRestaurants;

    public CustomAdapterRestaurant(Context context, ArrayList<Restaurant> list, HashMap<String, Uri> uris) {

        this.listRestaurants = list;
        this.context = context;
        this.uriPictures = uris;
    }

    @Override
    public int getCount() {
        return listRestaurants.size();
    }

    @Override
    public Object getItem(int position) {
        return listRestaurants.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {

            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_restaurant_info, parent, false);
        }

        final Restaurant restaurant = (Restaurant) this.getItem(position);

        TextView titleText = convertView.findViewById(R.id.titleRestaurant);
        TextView descriptionText = convertView.findViewById(R.id.descRestaurant);
        ImageView foodImage = convertView.findViewById(R.id.iconRestaurant);

        titleText.setText(restaurant.getName());
        descriptionText.setText(restaurant.getDescription());

        if(uriPictures.containsKey(listRestaurants.get(position).getName()) ) {

            Glide.with(context)
                    .load(uriPictures.get(listRestaurants.get(position).getName()))
                    .into(foodImage);
        }
        return convertView;
    }
}
