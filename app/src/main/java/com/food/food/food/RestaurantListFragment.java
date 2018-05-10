package com.food.food.food;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantListFragment extends Fragment implements RestaurantAdapter.onItemClickListener {

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_MENU = "menu";

    private RecyclerView mRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private ArrayList<RestaurantItem> mRestaurantItems;
    private RequestQueue Queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    //cannot find this view
        View v = inflater.inflate(R.layout.fragment_restaurant_list, container, false);

        mRecyclerView = v.findViewById(R.id.restauracje_lista);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRestaurantItems = new ArrayList<>();

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        jsonParse();

        return v;
    }

    private void jsonParse(){
        String url = "https://food--api.herokuapp.com/restauracje";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //response => json array
                try {
                    for(int i=0; i<response.length(); i++){
//                        Map<String, String> dostepneDania = new HashMap<>();
                        JSONObject restauracja = response.getJSONObject(i);
                        Log.d("ID", restauracja.toString());
                        String nazwa = restauracja.getString("nazwa");
                        String adres = restauracja.getString("adres");
                        String id = restauracja.getString("_id");
                        String img = "";
                        if(restauracja.has("img")){
                            img = restauracja.getString("img");
                        }
//                        JSONObject menu = restauracja.getJSONObject("menu");
//                        JSONArray menuKeys = menu.names();
//                        Log.d("NAMES", menuKeys.toString());

//                        for (int j = 0; j < menuKeys.length(); j++) {
//                            String key = menuKeys.getString(j);
//                            JSONArray dania = menu.getJSONArray(key);
//                            if(dania.length() != 0){
//                                Log.d("ARRAY", "NOT EMPTY!");
//                                dostepneDania.put(key, dania.toString());
//                            }
//                        }

                        mRestaurantItems.add(new RestaurantItem(id, nazwa, adres, img));
                    }
                    //pass the data list to adapter
                    mRestaurantAdapter = new RestaurantAdapter(getActivity(), mRestaurantItems);
                    mRecyclerView.setAdapter(mRestaurantAdapter);
                    mRestaurantAdapter.setOnItemClickListener(RestaurantListFragment.this);

                } catch(JSONException e){
                    e.printStackTrace();
                }

//                try {
//                    for(int i=0; i<response.length(); i++){
//                        Map<String, String> dostepneDania = new HashMap<>();
//                        JSONObject restauracja = response.getJSONObject(i);
//
//                        String nazwa = restauracja.getString("nazwa");
//                        String adres = restauracja.getString("adres");
//                        JSONObject menu = restauracja.getJSONObject("menu");
//                        JSONArray menuKeys = menu.names();
////                        Log.d("NAMES", menuKeys.toString());
//
//                        for (int j = 0; j < menuKeys.length(); j++) {
//                            String key = menuKeys.getString(j);
//                            JSONArray dania = menu.getJSONArray(key);
//                            if(dania.length() != 0){
//                                Log.d("ARRAY", "NOT EMPTY!");
//                                dostepneDania.put(key, dania.toString());
//                            }
////                            Log.d("Klucz", key + " " + nazwa);
////                            Log.d("J:", Integer.toString(j));
////                            Log.d("Dania", dania.toString());
////                            Log.d("MENU ITEM", menu.names()(i))
//                            //Log.d("WTF?", menu.names().toString(i));
////                            JSONArray danie = menu.getJSONArray(menu.names().getString(i));
////                            Log.d("----0----Send help..", danie.toString());
//                        }
//
//                        mRestaurantItems.add(new RestaurantItem(nazwa, adres, dostepneDania));
//                    }
//                    //pass the data list to adapter
//                    mRestaurantAdapter = new RestaurantAdapter(getActivity(), mRestaurantItems);
//                    mRecyclerView.setAdapter(mRestaurantAdapter);
//                    mRestaurantAdapter.setOnItemClickListener(RestaurantListFragment.this);
//
//                } catch(JSONException e){
//                    e.printStackTrace();
//                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Queue.add(request);
    }

    @Override
    public void onItemClick(int position) {
        //Fetch selected restaurant data

        final RestaurantItem clickedItem = mRestaurantItems.get(position);
        final RestaurantItemDetailFragment detailFragment = new RestaurantItemDetailFragment();

        String url = "https://food--api.herokuapp.com/restauracja/" + clickedItem.getId();
        Log.d("url:---", url);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray responseMenu = response.getJSONArray("menu");
                    JSONObject menu = new JSONObject();
                    for(int i=0;i<responseMenu.length(); i++){
                        JSONObject singleDish = responseMenu.getJSONObject(i);
                        String type = singleDish.getString("rodzaj");

//                        Log.d("AR", menu.getJSONObject(type).toString());
                        if(menu.has(type)){
                            menu.getJSONArray(type).put(singleDish);
                        } else {
                            JSONArray ar = new JSONArray();
                            ar.put(singleDish);
                            menu.put(type, ar);
                        }
                    }
                    Map<String, String> dostepneDania = new HashMap<>();
                    JSONArray menuKeys = menu.names();

                    //Check if restaurant has menu, throws an error if it doesn't
                    if(menuKeys != null){

                        for (int j = 0; j < menuKeys.length(); j++) {
                            String key = menuKeys.getString(j);
                            JSONArray dania = menu.getJSONArray(key);
                            dostepneDania.put(key, dania.toString());
                        }

                        Bundle bundle = new Bundle();
                        bundle.putString(EXTRA_NAME, clickedItem.getName());
                        bundle.putString(EXTRA_ADDRESS, clickedItem.getAddress());
                        bundle.putString("id", clickedItem.getId());
                        bundle.putString("img", clickedItem.getImg());
                        bundle.putSerializable(EXTRA_MENU, (Serializable) dostepneDania);

                        detailFragment.setArguments(bundle);

                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).addToBackStack(null).commit();

                    } else {
                        Toast.makeText(getContext(), "Restauracja nie ma uzupełnionych danych", Toast.LENGTH_SHORT).show();
                    }


                } catch(JSONException e){
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        Queue.add(request);

//        Bundle bundle = new Bundle();
//        bundle.putString(EXTRA_NAME, clickedItem.getName());
//        bundle.putString(EXTRA_ADDRESS, clickedItem.getAddress());
//        bundle.putSerializable(EXTRA_MENU, (Serializable) clickedItem.getAvailableFood());

//        detailFragment.setArguments(bundle);

//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, detailFragment).addToBackStack(null).commit();

//        Intent detail = new Intent(getActivity(), RestaurantItemDetail.class);
//
//        detail.putExtra(EXTRA_NAME, clickedItem.getName());
//        detail.putExtra(EXTRA_ADDRESS, clickedItem.getAddress());
////        detail.putExtra(EXTRA_MENU, (Serializable) clickedItem.getAvailableFood());
//        detail.putExtra(EXTRA_MENU, (Serializable) clickedItem.getAvailableFood());
//
//        startActivity(detail);
    }
}
