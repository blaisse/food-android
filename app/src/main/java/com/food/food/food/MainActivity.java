package com.food.food.food;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cart.CartFragment;
import order.OrderFragment;
import profile.ProfileFragment;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SharedPreferences.OnSharedPreferenceChangeListener {
    private TextView TextViewRestauracje;
    private RequestQueue Queue;
    private DrawerLayout drawer;

//    private Fragment registerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Open Profile fragment when username in toolbar is clicked
        toolbar.findViewById(R.id.toolbarUsername).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
            }
        });

        toolbar.findViewById(R.id.toolbar_cart_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();
            }
        });

        drawer = findViewById(R.id.drawer_layout);
        //Clear shared preferences, most moved to listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

        sharedPref.registerOnSharedPreferenceChangeListener(this);

        String tokenFromPreferences = sharedPref.getString(getString(R.string.token), null);
        String nazwaFromPreferences = sharedPref.getString(getString(R.string.nazwa), null);

        Set<String> cartItems = sharedPref.getStringSet("shoppingCart", new HashSet<String>());

        TextView shoppingCartAmount = findViewById(R.id.toolbar_cart_items);
        Integer size = cartItems.size();
        if(size > 0){
            shoppingCartAmount.setText(Integer.toString(size));
        }

        //If user is logged in use different menu
        if(tokenFromPreferences != null && nazwaFromPreferences != null){
            navigationView.inflateMenu(R.menu.drawer_menu_logged_in);
        } else {
            navigationView.inflateMenu(R.menu.drawer_menu);
        }

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

//        TextViewRestauracje = findViewById(R.id.main_text);
        //Button buttonRestauracje = findViewById(R.id.button_restauracje);

        Queue = Volley.newRequestQueue(this);

//        buttonRestauracje.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //jsonParse();
//                openRestaurantList();
//            }
//        });

        //it won't override default behaviour when rotating phone etc. | rotating phone sets savedInstanceState not to null
        //if will pass if we open the app
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.drawer_home);
        }

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.drawer_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;
            case R.id.drawer_restaurant_list:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RestaurantListFragment()).addToBackStack(null).commit();
                break;
            case R.id.drawer_restaurant_fragment:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RestaurantFragment()).addToBackStack(null).commit();
                break;
            case R.id.drawer_orders:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new OrderFragment()).addToBackStack(null).commit();
                break;
            case R.id.drawer_profile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
                break;
            case R.id.drawer_login:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new LoginFragment()).addToBackStack(null).commit();
                break;
            case R.id.drawer_register:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RegisterFragment()).addToBackStack(null).commit();
                break;
            case R.id.drawer_logout:

                SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.token));
                editor.remove(getString(R.string.nazwa));
                editor.remove("img");
                editor.commit();//or apply()?

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

        }
        drawer.closeDrawer(GravityCompat.START);
        return true;//item selected
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
//            if (getFragmentManager().getBackStackEntryCount() > 0 ){
//                getFragmentManager().popBackStack();
//            } else {
//                super.onBackPressed();
//            }
        }

    }

    public void openProfile(){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).addToBackStack(null).commit();
    }

    public void openRestaurantList(){
        Intent intent = new Intent(this, RestaurantList.class);
        startActivity(intent);
    }

    private void jsonParse(){
        String url = "https://food--api.herokuapp.com/restauracje";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //response => json array
                try {
                    for(int i=0; i<response.length(); i++){
                        JSONObject restauracja = response.getJSONObject(i);

                        String nazwa = restauracja.getString("nazwa");
                        JSONObject menu = restauracja.getJSONObject("menu");

                        TextViewRestauracje.append(nazwa + "!");
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

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals("shoppingCart") || key.equals("cartForce")){
            Log.d("CART", "updated boy");
            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);
            Set<String> cartItems = sharedPref.getStringSet("shoppingCart", new HashSet<String>());

            TextView shoppingCartAmount = findViewById(R.id.toolbar_cart_items);
            Integer size = cartItems.size();
            if(size > 0){
                Log.d("SIZE?", shoppingCartAmount.toString());
                shoppingCartAmount.setVisibility(View.VISIBLE);
                shoppingCartAmount.setText(Integer.toString(size));
            } else {
                shoppingCartAmount.setVisibility(View.GONE);
            }
//            Log.d("U p d a t e", key);
//            Log.d("Shopping cart needs", "an update");
        }
        if(key.equals(getString(R.string.token))){
            //Change menu
            Log.d("TOKEN CHANGED", "TOKEEEEEN");
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().clear();
//            navigationView.inflateMenu(R.menu.drawer_menu);

            SharedPreferences sharedPref = this.getPreferences(Context.MODE_PRIVATE);

            String tokenFromPreferences = sharedPref.getString(getString(R.string.token), null);
            String nazwaFromPreferences = sharedPref.getString(getString(R.string.nazwa), null);

            //Change toolbar username
            TextView toolbarUsername = findViewById(R.id.toolbarUsername);

            //If user is logged in use different menu
            if(tokenFromPreferences != null && nazwaFromPreferences != null){
                navigationView.inflateMenu(R.menu.drawer_menu_logged_in);
                toolbarUsername.setText(nazwaFromPreferences);
            } else {
                navigationView.inflateMenu(R.menu.drawer_menu);
                toolbarUsername.setText("");
            }

        }
    }
}
