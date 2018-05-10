package com.food.food.food;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RestaurantAdapter extends RecyclerView.Adapter<RestaurantAdapter.RestaurantViewHolder> {
    private Context mContext;
    private ArrayList<RestaurantItem> mRestaurantItems;
    private onItemClickListener mListener;

    public interface onItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public RestaurantAdapter(Context context, ArrayList<RestaurantItem> restaurantList){
        mContext = context;
        mRestaurantItems = restaurantList;
    }

    @NonNull
    @Override
    public RestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.restaurant_card, parent, false);
        return new RestaurantViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantViewHolder holder, int position) {
        RestaurantItem currentItem = mRestaurantItems.get(position);

        String restaurantName = currentItem.getName();
        String restaurantAddress = currentItem.getAddress();
        Log.d("REST IMGAGE", currentItem.getImg());

        holder.name.setText(restaurantName);
        holder.address.setText(restaurantAddress);
        if(currentItem.getImg().length() != 0){
            Picasso.get().load(currentItem.getImg()).into(holder.img);
        } else {
            holder.img.setVisibility(View.GONE);
            holder.name.setTextColor(Color.parseColor("#000000 "));
            holder.address.setTextColor(Color.parseColor("#000000"));
        }

    }

    @Override
    public int getItemCount() {
        return mRestaurantItems.size();
    }

    public class RestaurantViewHolder extends RecyclerView.ViewHolder {
        public TextView name;
        public TextView address;
        public ImageView img;

        public RestaurantViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.restaurant_list_name);
            address = itemView.findViewById(R.id.restaurant_list_address);
            img = itemView.findViewById(R.id.restaurant_list_img);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mListener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            mListener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

}
