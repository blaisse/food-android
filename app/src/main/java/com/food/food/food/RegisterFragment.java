package com.food.food.food;

import android.content.Context;
import android.content.Intent;
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
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterFragment extends Fragment {

    private Map<String, String> params;
    private RequestQueue Queue;
    private View v;

    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        v = inflater.inflate(R.layout.fragment_register, container, false);

        Queue = Volley.newRequestQueue(getActivity().getApplicationContext());

        final EditText registerUser = v.findViewById(R.id.editRegisterUser);
        final EditText registerPassword = v.findViewById(R.id.editRegisterPassword);
        final EditText registerAddress = v.findViewById(R.id.editRegisterAddress);

        final TextView linkToLogin = v.findViewById(R.id.linkToLogin);

        Button registerButton = v.findViewById(R.id.buttonRegister);

        radioGroup = v.findViewById(R.id.radio_register_group);
//        radioButton = v.findViewById();

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton btn = radioGroup.findViewById(checkedId);
//                int i = radioGroup.indexOfChild(btn);
                Log.d("Button selected", btn.getText().toString());
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View vi) {

//                RadioButton selectedAccountType = radioGroup.findViewById()

                //Get selected account type User / Restaurant
                int id = radioGroup.getCheckedRadioButtonId();
                radioButton = v.findViewById(id);

                final String type;

                if(radioButton.getText().equals("Użytkownik")){
                    Log.d("WORK", "IT BE WORK USER");
                    type = "user";
                } else {
                    type = "restaurant";
                }

                String url = "https://food--api.herokuapp.com/signupRestaurant";
                String user = registerUser.getText().toString();
                String address = registerAddress.getText().toString();
                String password = registerPassword.getText().toString();

                params = new HashMap<>();
                params.put("nazwa", user);
                params.put("adres", address);
                params.put("password", password);
                params.put("type", type);

                Log.d("MAPA", params.toString());

                JSONObject parameters = new JSONObject(params);

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, parameters, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        try {
                            Log.d("REGISTER RESPONSE", response.toString());
                            //Store token
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

                            //get saved token - testing
//                            String tokenFromPreferences = sharedPref.getString(getString(R.string.token), "");
//                            Log.d("token from preferences", tokenFromPreferences);
//                            String tokenFromPreferences = sharedPref.getString("type", "");
//                            Log.d("token from preferences", tokenFromPreferences);
                            //Redirect to home page
                            getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();


                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //API returns 422 on incorrect information provided
                        //Volley handles such errors here
                        //Even though API returns a JSON Object, Volley gets it as a byte array - turn into string then JSON object
                        final TextView registerError = v.findViewById(R.id.registerErrorText);
                        try {
                            JSONObject er = new JSONObject(new String(error.networkResponse.data));
                            registerError.setText(er.getString("error"));
                        } catch(JSONException responseErrorToJSON){
                            error.printStackTrace();
                        }
                        Log.d("API error", new String(error.networkResponse.data));
                        error.printStackTrace();
                    }
                });

                Queue.add(request);

            }
        });


        linkToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
            }
        });


        return v;
    }

    public void selectRegister(View v){
        int id = radioGroup.getCheckedRadioButtonId();
        radioButton = v.findViewById(id);

//        Log.d("SELECTED", radioButton.toString());
    }

    public Map<String, String> getParams() {
        return params;
    }
}
