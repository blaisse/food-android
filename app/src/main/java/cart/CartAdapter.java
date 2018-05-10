package cart;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.food.food.food.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private ArrayList<CartItem> cartList;

    public static class CartViewHolder extends RecyclerView.ViewHolder{

        public TextView nameView;
        public TextView priceView;
        public TextView timeView;
        public EditText amountView;
        public TextView totalPriceView;
        public ImageView imgView;
        public LinearLayout linearLayout;
        public LinearLayout summaryLayout;
        public Context context;

        public Button deleteButton;
        public Button orderButton;
        public Button editButton;

        public CartViewHolder(View itemView) {
            super(itemView);
            //Find by id here
            nameView = itemView.findViewById(R.id.cart_name);
            priceView = itemView.findViewById(R.id.cart_price);
            timeView = itemView.findViewById(R.id.cart_time);
            amountView = itemView.findViewById(R.id.cart_amount);
            imgView = itemView.findViewById(R.id.cart_img);
            linearLayout = itemView.findViewById(R.id.cart_linear_img_bg);
            deleteButton = itemView.findViewById(R.id.cart_delete);
            orderButton = itemView.findViewById(R.id.cart_order);
            editButton = itemView.findViewById(R.id.cart_edit);
            context = itemView.getContext();
            totalPriceView = itemView.findViewById(R.id.cart_total_price);

            summaryLayout = itemView.findViewById(R.id.cart_summary_layout);

        }
    }

    public CartAdapter(ArrayList<CartItem> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_cart, parent, false);
        return new CartViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, int position) {

        final CartItem cartItem = cartList.get(position);

        String restaurantId = cartItem.getRestaurantId();
        String amountStr = cartItem.getAmount();

        Integer amount = Integer.parseInt(amountStr);
        Integer time = Integer.parseInt(cartItem.getTime()) * amount;
        Integer price = Integer.parseInt(cartItem.getPrice());

        Double priceDouble = (double)price * amount;

        String finalPrice = Double.toString(priceDouble) + " zł";
        String finalTime = Integer.toString(time) + " min";

        holder.nameView.setText(cartItem.getName());
        holder.priceView.setText(finalPrice);
        holder.amountView.setText(cartItem.getAmount());
        holder.timeView.setText(finalTime);
        Picasso.get().load(cartItem.getImg()).into(holder.imgView);

        final SharedPreferences sharedPref = ((AppCompatActivity)holder.context).getPreferences(Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Remove item from Set
                Set<String> set = sharedPref.getStringSet("shoppingCart", new HashSet<String>());
                String cartForce = "deleted" + cartItem.getId();
                for(Iterator<String> iterator = set.iterator(); iterator.hasNext();){
                    String item = iterator.next();
                    try {

                        JSONObject temp = new JSONObject(item);
                        if(cartItem.getId().equals(temp.getString("id"))){
                            //Remove this item
                            iterator.remove();
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }

                editor.putStringSet("shoppingCart", set);
                editor.putString("cartForce", cartForce);
                editor.commit();

                AppCompatActivity activity = (AppCompatActivity) v.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CartFragment()).addToBackStack(null).commit();
            }
        });

//        holder.amountView.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if(s.length() != 0 && !s.toString().equals("0")){
////                    Log.d("IT OK  is ", "ok");
//                    cartItem.setAmount(s.toString());
////                    notifyItemChanged(holder.getAdapterPosition());
//                }
//                Log.d("TEXT CHANGED", s.toString());
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                Log.d("WTF IS THIS", s.toString());
//                if(s.length() != 0 && !s.toString().equals("0")){
////                    Log.d("IT OK  is ", "ok");
////                    cartItem.setAmount(s.toString());
//                    notifyItemChanged(holder.getAdapterPosition());
//                }
//            }
//        });

        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder.amountView.setText("2");
//                notifyDataSetChanged();
//                cartItem.setAmount("2");
//                notifyItemChanged(holder.getAdapterPosition());
                String inputValue = holder.amountView.getText().toString();

                //Update so that it cannot start with 0

                JSONObject updatedAmount = new JSONObject();
                Set<String> set = sharedPref.getStringSet("shoppingCart", new HashSet<String>());
                for(Iterator<String> iterator = set.iterator(); iterator.hasNext();){
                    String item = iterator.next();
                    try {

                        JSONObject temp = new JSONObject(item);
                        if(cartItem.getId().equals(temp.getString("id"))){
                            if(inputValue.equals("") || inputValue.equals("0")){
                                //Old value
                                inputValue = temp.getString("amount");
                            }
                            temp.put("amount", inputValue);
                            updatedAmount = temp;
                            iterator.remove();
                            //Update this item
                        }

                    } catch(JSONException e){
                        e.printStackTrace();
                    }
                }
                set.add(updatedAmount.toString());
                editor.putStringSet("shoppingCart", set);
                editor.apply();

                Double updatedItemPrice = Double.parseDouble(cartItem.getPrice()) * Integer.parseInt(cartItem.getAmount());
                Double newPrice = Double.parseDouble(cartItem.getPrice()) * Integer.parseInt(inputValue);
//                holder.totalPriceView.setText(Double.toString(finalTotalPrice));
//                Log.d("UPDATED OLD PRICE XD", Double.toString(updatedItemPrice));
//                Log.d("WTF TO PARENT?", holder.amountView.getParent()..toString());
                View p = (View)v.getRootView();
                TextView qq = p.findViewById(R.id.cart_total_price);
                Double finalTotalPrice = Double.parseDouble(qq.getText().toString()) - updatedItemPrice + newPrice;

                Log.d("HELP", qq.toString());
                qq.setText(Double.toString(finalTotalPrice));
                cartItem.setAmount(inputValue);
                notifyItemChanged(holder.getAdapterPosition());
//                holder.summaryLayout.setVisibility(View.VISIBLE);
//                Log.d("Total price", Double.toString(totalPrice));
//                String totalPriceString = Double.toString(totalPrice) + " zł";
//                holder.totalPriceView.setText(totalPriceString);

            }
        });

    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }
}
