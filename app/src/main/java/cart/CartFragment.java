package cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.food.food.food.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import comments.CommentAdapter;
import order.OrderFragment;

public class CartFragment extends Fragment {

    private ArrayList<CartItem> cartList;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private CartAdapter mAdapter;
    private RequestQueue Queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        mRecyclerView = view.findViewById(R.id.cart_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        cartList = new ArrayList<>();

        final TextView totalPriceView = view.findViewById(R.id.cart_total_price);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        final String tokenFromPreferences = sharedPref.getString("token", "");
        final String typeFromPreferences = sharedPref.getString("type", "");
        final Set<String> setFromPreferences = sharedPref.getStringSet("shoppingCart", new HashSet<String>());

        Double totalPrice = 0.0;

        if(setFromPreferences.size() != 0){
            for(String item : setFromPreferences){
                try {

                    JSONObject temp = new JSONObject(item);

                    cartList.add(new CartItem(
                            temp.getString("id"),
                            temp.getString("name"),
                            temp.getString("price"),
                            temp.getString("time"),
                            temp.getString("img"),
                            temp.getString("amount"),
                            temp.getString("restaurantId")
                    ));

                    totalPrice += Integer.parseInt(temp.getString("amount")) * (double)Double.parseDouble(temp.getString("price"));

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }

            mRecyclerView = view.findViewById(R.id.cart_list);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new CartAdapter(cartList);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);

            //Total price
            LinearLayout summaryLayout = view.findViewById(R.id.cart_summary_layout);
            summaryLayout.setVisibility(View.VISIBLE);

            Log.d("Total price", Double.toString(totalPrice));
            String totalPriceString = Double.toString(totalPrice);
            totalPriceView.setText(totalPriceString);
        } else {
            //Show "Empty cart" message
            TextView emptyMessageView = view.findViewById(R.id.cart_empty_message);
            emptyMessageView.setVisibility(View.VISIBLE);
        }

        view.findViewById(R.id.cart_order).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!tokenFromPreferences.equals("")){
                    Log.d("SET ORDER", setFromPreferences.toString());
                    JSONObject params = new JSONObject();
                    JSONArray dishesArray = new JSONArray();
                    try {
//                    params.put("dishes", setFromPreferences);
                        for(Iterator<String> iterator = setFromPreferences.iterator(); iterator.hasNext();){
                            String item = iterator.next();
                            JSONObject temp = new JSONObject(item);
                            dishesArray.put(temp);
                        }
                        params.put("dishes", dishesArray);
//                    Log.d("Why", dishesArray.toString());
                        params.put("totalPrice", totalPriceView.getText().toString());
                        params.put("type", typeFromPreferences);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    String url = "https://food--api.herokuapp.com/order";

                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, params, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Response", response.toString());
                            //Go to confirmation page?
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).addToBackStack(null).commit();
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        public Map<String, String> getHeaders() throws AuthFailureError {
                            Map<String, String>  params = new HashMap<String, String>();
                            params.put("auth", tokenFromPreferences);
                            return params;
                        }
                    };
                    Queue.add(request);
                    editor.remove("shoppingCart");
                    editor.apply();
                } else {
                    //Log in to order
                    Toast.makeText(getContext(), "Zaloguj się aby dokonać zakupu", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return view;
    }
}
