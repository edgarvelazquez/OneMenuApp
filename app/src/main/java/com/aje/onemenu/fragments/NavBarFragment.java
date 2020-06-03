package com.aje.onemenu.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.aje.onemenu.Profile;
import com.aje.onemenu.R;
import com.aje.onemenu.activities.MainActivity;
import com.aje.onemenu.activities.RecommendedRestaurantActivity;
import com.aje.onemenu.activities.RestaurantMenuActivity;
import com.aje.onemenu.activities.RestaurantsList;
import com.aje.onemenu.settings.UserInfoActivity;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class NavBarFragment extends Fragment implements TabLayout.OnTabSelectedListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NavBarFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
//    public static NavBarFragment newInstance(String param1) {
//        NavBarFragment fragment = new NavBarFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        fragment.setArguments(args);
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nav_bar_fragment, container, false);

        TabLayout tl =view.findViewById(R.id.navBar);
        tl.addOnTabSelectedListener(this);



        return view;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        switch (tab.getPosition()) {
            case 0:

                Intent intent = new Intent(getActivity(), RestaurantsList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case 1:
                Intent intent3 = new Intent(getActivity(), RecommendedRestaurantActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);

                break;
            case 2:
                Intent intent2 = new Intent(getActivity(), UserInfoActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivityIfNeeded(intent2, 0);
//                startActivity(intent2,0);
                startActivity(intent2);
                break;

        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

        switch (tab.getPosition()) {
            case 0:

                Intent intent = new Intent(getActivity(), RestaurantsList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case 1:
                Intent intent3 = new Intent(getActivity(), RecommendedRestaurantActivity.class);
                intent3.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent3);

                break;
            case 2:
                Intent intent2 = new Intent(getActivity(), UserInfoActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivityIfNeeded(intent2, 0);
//                startActivity(intent2,0);
                startActivity(intent2);
                break;

        }


    }

}

