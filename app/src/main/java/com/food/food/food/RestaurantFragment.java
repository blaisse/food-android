package com.food.food.food;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

public class RestaurantFragment extends Fragment {

    private RequestQueue Queue;


    final RestaurantItemDetailFragment detailFragment = new RestaurantItemDetailFragment();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.restaurant_fragment, container, false);

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        //Końcówka url to ID restauracji
        String url = "https://food--api.herokuapp.com/restauracja/5af1ea9b75e9040014401ff2";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray responseMenu = response.getJSONArray("menu");
                    JSONObject menu = new JSONObject();
                    for(int i=0;i<responseMenu.length(); i++){
                        JSONObject singleDish = responseMenu.getJSONObject(i);
                        String type = singleDish.getString("rodzaj");

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
                        bundle.putString("name", response.getString("nazwa"));
                        bundle.putString("address", response.getString("adres"));
                        bundle.putString("id", response.getString("_id"));
                        bundle.putString("img", response.getString("img"));
                        bundle.putSerializable("menu", (Serializable) dostepneDania);

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


        return v;
    }


}
