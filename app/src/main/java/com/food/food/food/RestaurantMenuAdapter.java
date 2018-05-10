package com.food.food.food;

import android.content.Context;
import android.support.annotation.NonNull;
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

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RestaurantMenuAdapter extends RecyclerView.Adapter<RestaurantMenuAdapter.RestaurantMenuViewHolder> {

    private OnItemClickListener mListener;
    private ArrayList<RestaurantMenuItem> menuList;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public static class RestaurantMenuViewHolder extends RecyclerView.ViewHolder {

        public TextView text;
        public LinearLayout layout;
        public Context context;

        public RestaurantMenuViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            text = itemView.findViewById(R.id.restaurant_menu_card_text);
            context = itemView.getContext();

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    public RestaurantMenuAdapter(ArrayList<RestaurantMenuItem> menuList){
        this.menuList = menuList;
    }

    @NonNull
    @Override
    public RestaurantMenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.restaurant_menu_card, parent, false);
        return new RestaurantMenuViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final RestaurantMenuViewHolder holder, int position) {
        RestaurantMenuItem currentItem = menuList.get(position);
//        final ImageView img = new ImageView(holder.context);
//
//        Picasso.get().load(currentItem.getImage()).into(img, new Callback() {
//            @Override
//            public void onSuccess() {
//                holder.layout.setBackground(img.getDrawable());
//            }
//
//            @Override
//            public void onError(Exception e) {
//
//            }
//        });
        holder.text.setText(currentItem.getText());
    }

    @Override
    public int getItemCount() {
        return menuList.size();
    }
}
