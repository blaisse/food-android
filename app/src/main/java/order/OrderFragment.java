package order;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.food.food.food.DishAdapter;
import com.food.food.food.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class OrderFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrderAdapter mAdapter;
    private RequestQueue Queue;
    private ArrayList<OrderItem> ordersList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_orders, container, false);

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());
        ordersList = new ArrayList<>();

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final String tokenFromPreferences = sharedPref.getString("token", "");
        final String typeFromPreferences = sharedPref.getString("type", "");

        String url = "https://food--api.herokuapp.com/orders";
        JSONObject params = new JSONObject();
        try {
            params.put("type", typeFromPreferences);
        } catch(JSONException e){
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray orders = response.getJSONArray("orders");
                    Log.d("ORDERS", orders.toString());
                    for(int i=0; i<orders.length(); i++){
                        JSONObject order = orders.getJSONObject(i);
                        JSONObject restaurant = new JSONObject();
                        if(typeFromPreferences.equals("user")){
                            restaurant = order.getJSONObject("restaurant");
                        } else if(typeFromPreferences.equals("restaurant")){
                            restaurant = order.getJSONObject("user");
                        }
                        ordersList.add(new OrderItem(
                                order.getString("_id"),
                                order.getString("dishes"),
                                order.getString("price"),
                                order.getString("date"),
                                order.getString("user"),
                                restaurant.getString("_id"),
                                restaurant.getString("nazwa")
                        ));
                    }

                    mRecyclerView = view.findViewById(R.id.orders_list);
                    mLayoutManager = new LinearLayoutManager(getContext());
                    mAdapter = new OrderAdapter(ordersList);
                    mRecyclerView.setLayoutManager(mLayoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("auth", tokenFromPreferences);
                return params;
            }
        };

        Queue.add(request);

        return view;
    }
}
