package profile;

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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;
import java.util.Map;

public class ProfileAddDishFragment extends Fragment {

    private RequestQueue Queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_add_dish, container, false);

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        final String tokenFromPreferences = sharedPref.getString("token", "");

        final AutoCompleteTextView dishType = view.findViewById(R.id.profile_add_dish_type_field);

        Button addDishButton = view.findViewById(R.id.profile_add_dish_button);

        final EditText dishName = view.findViewById(R.id.profile_add_dish_name_field);
        final EditText dishPrice = view.findViewById(R.id.profile_add_dish_price_field);
        final EditText dishTime = view.findViewById(R.id.profile_add_dish_time_field);
        final EditText dishImg = view.findViewById(R.id.profile_add_dish_img_field);

        String url = "https://food--api.herokuapp.com/listadan";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                Log.d("ADD DISH RESPONSE", response.toString());
                if(response.length() != 0){

                    List<String> x = new ArrayList<>();

                    try {

                        for(int i=0; i<response.length(); i++){
                            x.add(response.getString(i));
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, x);
                    dishType.setAdapter(adapter);
                }

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

        addDishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Hey kciued", "lolxd");

                String name = dishName.getText().toString();
                String type = dishType.getText().toString();
                String price = dishPrice.getText().toString();
                String time = dishTime.getText().toString();
                String img = dishImg.getText().toString();

                if(name.length() != 0 && type.length() != 0 && price.length() != 0 && time.length() != 0 && img.length() != 0){
                    Log.d("NO empty field", "xd");
                    String url = "https://food--api.herokuapp.com/dodajdanie";
                    Map<String, String> params = new HashMap<>();
                    params.put("nazwa", name);
                    params.put("rodzaj", type);
                    params.put("cena", price);
                    params.put("czas", time);
                    params.put("img", img);

                    JSONObject parameters = new JSONObject(params);

                    JsonObjectRequest request2 = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            dishName.setText("");
                            dishType.setText("");
                            dishPrice.setText("");
                            dishTime.setText("");
                            Toast.makeText(getContext(), "Dodano danie", Toast.LENGTH_SHORT).show();

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

                    Queue.add(request2);
                }

            }
        });

        return view;
    }
}
