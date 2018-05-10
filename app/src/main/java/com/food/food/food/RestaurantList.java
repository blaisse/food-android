package com.food.food.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RestaurantList extends AppCompatActivity implements RestaurantAdapter.onItemClickListener, Serializable {

    public static final String EXTRA_NAME = "name";
    public static final String EXTRA_ADDRESS = "address";
    public static final String EXTRA_MENU = "menu";

    private RecyclerView mRecyclerView;
    private RestaurantAdapter mRestaurantAdapter;
    private ArrayList<RestaurantItem> mRestaurantItems;
    private RequestQueue Queue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        mRecyclerView = findViewById(R.id.restauracje_lista);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mRestaurantItems = new ArrayList<>();

        Queue = Volley.newRequestQueue(this);

        jsonParse();
    }

    private void jsonParse(){
        String url = "https://food--api.herokuapp.com/restauracje";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //response => json array
                try {
                    for(int i=0; i<response.length(); i++){
                        Map<String, String> dostepneDania = new HashMap<>();
                        JSONObject restauracja = response.getJSONObject(i);

                        String nazwa = restauracja.getString("nazwa");
                        String adres = restauracja.getString("adres");
                        JSONObject menu = restauracja.getJSONObject("menu");
                        JSONArray menuKeys = menu.names();
//                        Log.d("NAMES", menuKeys.toString());

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

//                        mRestaurantItems.add(new RestaurantItem(nazwa, adres, dostepneDania));
                    }
                    //pass the data list to adapter
                    mRestaurantAdapter = new RestaurantAdapter(RestaurantList.this, mRestaurantItems);
                    mRecyclerView.setAdapter(mRestaurantAdapter);
                    mRestaurantAdapter.setOnItemClickListener(RestaurantList.this);

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
    }


    @Override
    public void onItemClick(int position) {
        Intent detail = new Intent(this, RestaurantItemDetail.class);
        RestaurantItem clickedItem = mRestaurantItems.get(position);

        detail.putExtra(EXTRA_NAME, clickedItem.getName());
        detail.putExtra(EXTRA_ADDRESS, clickedItem.getAddress());
//        detail.putExtra(EXTRA_MENU, (Serializable) clickedItem.getAvailableFood());
//        detail.putExtra(EXTRA_MENU, (Serializable) clickedItem.getAvailableFood());

        startActivity(detail);
    }
}
