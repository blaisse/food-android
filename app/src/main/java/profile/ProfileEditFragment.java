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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.food.food.food.HomeFragment;
import com.food.food.food.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ProfileEditFragment extends Fragment {

    private RequestQueue Queue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_profile_edit_data, container, false);

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final EditText name = view.findViewById(R.id.profile_edit_data_name);
        final EditText address = view.findViewById(R.id.profile_edit_data_address);
        final EditText img = view.findViewById(R.id.profile_edit_data_img);

        Button submit = view.findViewById(R.id.profile_edit_data_button);

        final SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);

        final String tokenFromPreferences = sharedPref.getString("token", "");
        final String typeFromPreferences = sharedPref.getString("type", "");
        final String nazwaFromPreferences = sharedPref.getString(getString(R.string.nazwa), "");
        final String adresFromPreferences = sharedPref.getString("adres", "");

        if(typeFromPreferences.equals("user")){
            img.setVisibility(View.GONE);
        } else {
//            Log.d("img from pref", sharedPref.getString("img", ""));
            img.setText(sharedPref.getString("img", ""));
//            img.setText();
        }

        if(!nazwaFromPreferences.equals("") && !adresFromPreferences.equals("")){
            name.setText(nazwaFromPreferences);
            address.setText(adresFromPreferences);
        }

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameText = name.getText().toString();
                String addressText = address.getText().toString();
                String imgText = img.getText().toString();

                if(nameText.equals(nazwaFromPreferences) && addressText.equals(adresFromPreferences) && imgText.length() == 0){

                    //Text didn't change, don't allow the update
                    //Maybe should a warning message
                } else {
                    if(name.getText().length() == 0 || address.getText().length() == 0){

                    } else {
                        //Server call to update fields
                        Log.d("Update", "Call server to update fields");
                        String url = "https://food--api.herokuapp.com/edycja";

                        Map<String, String> params = new HashMap<>();
                        params.put("type", typeFromPreferences);
                        params.put("nazwa", nameText);
                        params.put("adres", addressText);

                        if(img.length() != 0){
                            params.put("img", imgText);
                        }

                        JSONObject parameters = new JSONObject(params);

                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //Update SharedPreferences
                                SharedPreferences.Editor editor = sharedPref.edit();
                                Log.d("xd?", response.toString());
                                try {
                                    editor.putString(getString(R.string.nazwa), response.getString("nazwa"));
                                    editor.putString("adres", response.getString("adres"));

                                    if(response.getString("img").length() != 0){
                                        editor.putString("img", response.getString("img"));
                                    }

                                    //Update username in toolbar - it doesn't change automatically
                                    TextView toolbarUsername = getActivity().findViewById(R.id.toolbarUsername);
                                    toolbarUsername.setText(response.getString("nazwa"));

                                } catch(JSONException e){
                                    e.printStackTrace();
                                }

                                editor.commit();

                                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
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
                    }
                }
            }
        });

        return view;
    }
}
