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
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginFragment extends Fragment {

    private JSONObject paramsObj;
    private RequestQueue Queue;
    private Map<String, String> params;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final EditText loginUser = view.findViewById(R.id.editLoginUser);
        final EditText loginPassword = view.findViewById(R.id.editLoginPassword);

        final Button loginButton = view.findViewById(R.id.buttonLogin);

        final RadioGroup radioGroup = view.findViewById(R.id.radio_login_group);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = radioGroup.getCheckedRadioButtonId();
                RadioButton radioButton = view.findViewById(id);

                final String type;

                if(radioButton.getText().equals("Użytkownik")){
                    type = "user";
                } else {
                    type = "restaurant";
                }

                String url = "https://food--api.herokuapp.com/signin";
                paramsObj = new JSONObject();
                //Create JSON params object
                try {
                    paramsObj.put("nazwa", loginUser.getText().toString());
                    paramsObj.put("password", loginPassword.getText().toString());

                    params = new HashMap<>();
                    params.put("nazwa", loginUser.getText().toString());
                    params.put("password", loginPassword.getText().toString());
                    params.put("type", type);


                } catch(JSONException e){
                    e.printStackTrace();
                }

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>(){
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response", response.toString());
                        //Save token and username
                        try {
                            String token = response.getString("token");
                            String nazwa = response.getString("nazwa");
                            String adres = response.getString("adres");

                            SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();

                            editor.putString(getString(R.string.token), token);
                            editor.putString(getString(R.string.nazwa), nazwa);
                            editor.putString("adres", adres);
                            editor.putString("type", type);

                            editor.commit();

                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                        //Redirect user
                        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Error", "Errror???");
                        TextView loginError = view.findViewById(R.id.loginErrorText);
                        loginError.setText("Niepoprawne dane");

                        error.printStackTrace();
                    }
                });

                Queue.add(request);
            }
        });

        return view;
    }

    public Map<String, String> getParams() {
        return params;
    }
}
