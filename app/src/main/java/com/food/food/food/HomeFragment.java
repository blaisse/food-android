package com.food.food.food;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_home, container, false);

        ImageView image = v.findViewById(R.id.home_image);

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        //Probably default value of an empty string "" might be better
        String tokenFromPreferences = sharedPref.getString(getString(R.string.token), null);
        String nazwaFromPreferences = sharedPref.getString(getString(R.string.nazwa), null);

        //User is logged in
        if(nazwaFromPreferences != null && tokenFromPreferences != null){
            Log.d("token from MainActivity", tokenFromPreferences);
            Log.d("nazwa from Main", nazwaFromPreferences);

            //Set toolbar username
            TextView toolbarUsername = getActivity().findViewById(R.id.toolbarUsername);
            toolbarUsername.setText(nazwaFromPreferences);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RestaurantListFragment()).addToBackStack(null).commit();
            }
        });

        return v;
    }

}
