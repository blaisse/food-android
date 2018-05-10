package com.food.food.food;

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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;

import static com.food.food.food.RestaurantList.EXTRA_ADDRESS;
import static com.food.food.food.RestaurantList.EXTRA_MENU;
import static com.food.food.food.RestaurantList.EXTRA_NAME;

public class RestaurantItemDetailFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RestaurantMenuAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_restaurant_list_detail, container, false);

        TextView nameView = view.findViewById(R.id.restaurant_list_detail_name);
        TextView addressView = view.findViewById(R.id.restaurant_list_detail_address);
        final LinearLayout layout = view.findViewById(R.id.restaurant_list_detail_layout);

        Bundle bundle = getArguments();
        if(bundle != null){
            String name = bundle.getString(EXTRA_NAME);
            String address = bundle.getString(EXTRA_ADDRESS);
            String img = bundle.getString("img");

            ImageView imgView = view.findViewById(R.id.restaurant_list_bg_img);
            Picasso.get().load(img).into(imgView);

//            Picasso.get().load(img).into(imgView, new Callback() {
//                @Override
//                public void onSuccess() {
////                    layout.setBackground(imgView.getDrawable());
//                }
//
//                @Override
//                public void onError(Exception e) {
//
//                }
//            });

            final String id = bundle.getString("id");
//            String menu = bundle.getString(EXTRA_MENU);
            final Map<String, String> menu = (Map<String, String>)bundle.getSerializable(EXTRA_MENU);

            //Set restaurant name and its address
            nameView.setText(name);
            addressView.setText(address);

            final ArrayList<RestaurantMenuItem> menuList = new ArrayList<>();

            for(String menuDanie : menu.keySet()){
                String potrawa = menu.get(menuDanie).toString();
                menuList.add(new RestaurantMenuItem(img, menuDanie, potrawa));
            }

            mRecyclerView = view.findViewById(R.id.restauracje_lista_menu_recycle);
            mLayoutManager = new LinearLayoutManager(getContext());
            mAdapter = new RestaurantMenuAdapter(menuList);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            mAdapter.setOnItemClickListener(new RestaurantMenuAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    DishFragment dishFragment = new DishFragment();
                    String clickedDish = menuList.get(position).getText();
                    Log.d("Clicked dish", clickedDish);
                    Log.d("Restaurant id:", id);
//                mAdapter.notifyItemChanged(position);

                    //Create bundle to send to Detail Fragment
                    //"dishes": [{}, {}]
                    Bundle dishBundle = new Bundle();
                    dishBundle.putString("dishes", menu.get(clickedDish));
                    //Restaurant id
                    dishBundle.putString("id", id);
                    //Dish type clicked "desery"
                    dishBundle.putString("dishType", clickedDish);
                    dishFragment.setArguments(dishBundle);

                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, dishFragment).addToBackStack(null).commit();

                }
            });

        }

        return view;
    }
}
