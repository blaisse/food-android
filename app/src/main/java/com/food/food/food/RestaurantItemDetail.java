package com.food.food.food;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.food.food.food.RestaurantList.EXTRA_ADDRESS;
import static com.food.food.food.RestaurantList.EXTRA_MENU;
import static com.food.food.food.RestaurantList.EXTRA_NAME;

public class RestaurantItemDetail extends AppCompatActivity implements Serializable {

    private RecyclerView mRecyclerView;
    private RestaurantMenuAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_item_detail);

        //getting data from RestaurantList
        Intent intent = getIntent();
        String name = intent.getStringExtra(EXTRA_NAME);
        String address = intent.getStringExtra(EXTRA_ADDRESS);
//        Map<String, JSONArray> menu = intent.get(EXTRA_MENU);
        Map<String, String> menu = (Map<String, String>)intent.getSerializableExtra(EXTRA_MENU);
//        Log.d("pls work", menu.get("zupy"));
        TextView nameView = findViewById(R.id.restaurant_list_detail_name);
        TextView addressView = findViewById(R.id.restaurant_list_detail_address);
        TextView menuView = findViewById(R.id.restaurant_list_detail_menu);

        Map<String, String> dania = new HashMap<>();

        nameView.setText(name);
        addressView.setText(address);

        //Create a new Map for list
        Map<String, String> restauracjaMenu = new HashMap<>();
        JSONObject menuObject = new JSONObject(menu);
        Log.d("menuObject", menuObject.toString());
        Log.d("meu", menu.toString());

        final ArrayList<RestaurantMenuItem> menuList = new ArrayList<>();

        for(String menuDanie : menu.keySet()){
            String potrawa = menu.get(menuDanie).toString();
//            menuList.add(new RestaurantMenuItem(0, menuDanie, potrawa));
        }

        mRecyclerView = findViewById(R.id.restauracje_lista_menu_recycle);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new RestaurantMenuAdapter(menuList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RestaurantMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                menuList.get(position).setText("LMFAO XD");
//                mAdapter.notifyItemChanged(position);
                //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();

            }
        });

//        try {
//            JSONArray array = new JSONArray(menu.get("zupy"));
//            for (int i = 0; i < array.length(); i++) {
//                JSONObject danie = array.getJSONObject(i);
//                //Make another list for it..
//                //display: cena, czas
//                menuView.append(danie.getString("nazwa") + "\n");
//            }
//        } catch(JSONException e){
//            e.printStackTrace();
//        }



    }

}
