package com.food.food.food;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialogFragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import comments.AddCommentDialog;
import comments.CommentFragment;

public class DishAdapter extends RecyclerView.Adapter<DishAdapter.DishViewHolder> {

    private ArrayList<DishItem> dishList;
    private Context context;

    public  class DishViewHolder extends RecyclerView.ViewHolder {

        public TextView name;
        public TextView price;
        public TextView time;
        public ImageView img;
        public Button comments;
        public Button addComment;
        public Button dishOrder;
        public EditText amountEdit;
        public RelativeLayout gradeLayout;

        public DishViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.dish_name);
            price = itemView.findViewById(R.id.dish_price);
            time = itemView.findViewById(R.id.dish_time);
            img = itemView.findViewById(R.id.dish_img);
            comments = itemView.findViewById(R.id.dish_comments_button);
            addComment = itemView.findViewById(R.id.dish_add_comments_button);
            dishOrder= itemView.findViewById(R.id.dish_order_button);
            context = itemView.getContext();
            gradeLayout = itemView.findViewById(R.id.dish_average_layout);
            amountEdit = itemView.findViewById(R.id.dish_amount);

        }
    }

    public DishAdapter(ArrayList<DishItem> dishList){
        this.dishList = dishList;
    }

    @NonNull
    @Override
    public DishViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dish_card, parent, false);
        return new DishViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DishViewHolder holder, int position) {
        DishItem dishItem = dishList.get(position);

        final String dishName = dishItem.getNazwa();
        final String dishPrice = dishItem.getCena();
        final String dishTime = dishItem.getCzas();
        final String dishImg = dishItem.getImg();
        final String dishId = dishItem.getId();
        final String dishRestaurantId = dishItem.getRestaurantId();
        Double dishAverage = dishItem.getAverage();
        final JSONArray dishComments = dishItem.getComments();

        String priceSt = "Cena: " + dishPrice + " zł";
        String timeSt = "Czas przygotowania: " + dishTime + " minut/-y";

        //Display stars
        for(int i=0; i<5; i++){

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.leftMargin = i * 150;
            TextView gradeItem = new TextView(holder.gradeLayout.getContext());
            // -1 because it's 0 based and grades are from 1 to 5
            if(i <= dishAverage - 1){
                gradeItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_black_24dp, 0, 0, 0);
            } else if(i < dishAverage && dishAverage >= i + 0.45 && dishAverage < i+1){
                //Half star
                //Example: average = 3.5, 3.5 > 3 and is more than 3.45 and is less than 4
                gradeItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_half_black_24dp, 0, 0, 0);
            } else {
                gradeItem.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_star_border_black_24dp, 0, 0, 0);
            }
            gradeItem.setId(i);
            gradeItem.setLayoutParams(params);
            holder.gradeLayout.addView(gradeItem);

        }

//        holder.gradeView.setText(Double.toString(dishAverage));
        holder.name.setText(dishName);
        holder.price.setText(priceSt);
        holder.time.setText(timeSt);
        Picasso.get().load(dishImg).into(holder.img);

        String buttonText = "Komentarze (" + dishComments.length() + ")";
        holder.comments.setText(buttonText);

        final SharedPreferences sharedPref = ((AppCompatActivity)context).getPreferences(Context.MODE_PRIVATE);
        final String tokenFromPreferences = sharedPref.getString("token", "");
        Set<String> setFromPreferences = sharedPref.getStringSet("shoppingCart", new HashSet<String>());
        String typeFromPreference = sharedPref.getString("type", "");

        if(!tokenFromPreferences.equals("") && typeFromPreference.equals("user")){
            //User is logged in
            holder.addComment.setVisibility(View.VISIBLE);
        }
        if(setFromPreferences.size() != 0){
            for(String item : setFromPreferences){
                try {

                    JSONObject temp = new JSONObject(item);
                    if(temp.getString("id").equals(dishId)){
                        String text = "W koszyku (" + temp.getString("amount") + ")";
                        holder.dishOrder.setText(text);
                        holder.dishOrder.setBackgroundColor(Color.parseColor("#FF5C80"));
                    }

                } catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }

        holder.dishOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add this dish to sharedPreferences
                SharedPreferences.Editor editor = sharedPref.edit();
//                String mapFromPreferences = sharedPref.getString("shoppingCart", "");
                Set<String> setFromPreferences = sharedPref.getStringSet("shoppingCart", new HashSet<String>());
//                editor.remove("shoppingCart");
//                editor.commit();
                String inputAmountString = holder.amountEdit.getText().toString();
                Integer inputAmount = 1;
                if(inputAmountString.equals("") || inputAmountString.equals("0")){
                    //Amount = 1
                } else {
                    inputAmount = Integer.parseInt(inputAmountString);
                }
//                inputAmount = Integer.parseInt(holder.amountEdit.getText().toString());
//                Log.d("input amount", Integer.toString(inputAmount));

                String cartForce = dishId;
                JSONObject obj = new JSONObject();
                try {

                    obj.put("id", dishId);
                    obj.put("name", dishName);
                    obj.put("price", dishPrice);
                    obj.put("time", dishTime);
                    obj.put("img", dishImg);
                    obj.put("restaurantId", dishRestaurantId);

                } catch(JSONException e){
                    e.printStackTrace();
                }
                if(setFromPreferences.size() != 0){

                    try {
                        Boolean hasBeenModified = false;

                        for(Iterator<String> iterator = setFromPreferences.iterator(); iterator.hasNext();){
                            String item = iterator.next();
                            JSONObject temp = new JSONObject(item);

                            if(dishId.equals(temp.getString("id"))){
                                hasBeenModified = true;
                                Integer amount = Integer.parseInt(temp.getString("amount")) + inputAmount;
                                cartForce += Integer.toString(amount);//1
                                obj.put("amount", amount);
                                iterator.remove();
                            }
                        }

                        if(!hasBeenModified){
                            obj.put("amount", inputAmount);
                        }

                        setFromPreferences.add(obj.toString());
//                        Set<String> ss = new HashSet<String>(setFromPreferences);
                        editor.putStringSet("shoppingCart", setFromPreferences);
                        if(cartForce.equals("")){
                            cartForce += "1";
                        }
                        editor.putString("cartForce", cartForce);
//                        Log.d("ADDED", obj.toString());
                        editor.commit();
                        //Add img too
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                } else {
                    //Create map
                    try {
                        cartForce += "firstelement";
                        obj.put("amount", inputAmount);
//                        Log.d("First element", obj.toString());
                        setFromPreferences.add(obj.toString());
                        editor.putString("cartForce", cartForce);
                        editor.putStringSet("shoppingCart", setFromPreferences);
                        editor.commit();
                        //Add img too
                    } catch(JSONException e){
                        e.printStackTrace();
                    }

                }
                holder.dishOrder.setText("Dodano do koszyka");
                holder.dishOrder.setBackgroundColor(Color.parseColor("#FF5C80"));

            }
        });

        holder.comments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentFragment commentFragment = new CommentFragment();
                //Create a bundle to send over?
                //add dish name etc to know what those comments are about
                Bundle bundle = new Bundle();
                bundle.putString("comments", dishComments.toString());
                bundle.putString("dishName", dishName);

                commentFragment.setArguments(bundle);

                if(dishComments.length() == 0){
                    //Add a toast "Brak komentarzy"?
                } else {
                    //Get Activity to open fragment from adapter
                    AppCompatActivity activity = (AppCompatActivity) v.getContext();
                    activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, commentFragment).addToBackStack(null).commit();
                }
            }
        });

        holder.addComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d("ADD COMMENT", tokenFromPreferences);
//                Log.d("Add comment2", typeFromPreference);
                AddCommentDialog addCommentDialog = new AddCommentDialog();

                Bundle bundle = new Bundle();
                bundle.putString("dishId", dishId);
                bundle.putString("dishName", dishName);
                bundle.putString("comments", dishComments.toString());
                addCommentDialog.setArguments(bundle);

                addCommentDialog.setTargetFragment(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.dish_fragment), 1);
                addCommentDialog.show(((AppCompatActivity)context).getSupportFragmentManager() , "add comments dialog work pls");

                InputMethodManager imm = (InputMethodManager) ((AppCompatActivity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                //openDialog();
            }
        });
//        holder.img.set();
    }

    public void openDialog(){
        AddCommentDialog addCommentDialog = new AddCommentDialog();
//        android.app.FragmentManager fm = ((AppCompatActivity) context).getFragmentManager();
//        addCommentDialog.show(((Activity) context).getFragmentManager(), 1);

        addCommentDialog.setTargetFragment(((AppCompatActivity) context).getSupportFragmentManager().findFragmentById(R.id.dish_fragment), 1);
        addCommentDialog.show(((AppCompatActivity)context).getSupportFragmentManager() , "add comments dialog work pls");
//            addCommentDialog.show(,"dialog pls work");
//        addCommentDialog.show(((AppCompatActivity) context).getFragmentManager().beginTransaction(), "dialog");
        InputMethodManager imm = (InputMethodManager) ((AppCompatActivity)context).getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public int getItemCount() {
        return dishList.size();
    }
}
