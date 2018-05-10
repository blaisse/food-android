package order;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.food.food.food.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private ArrayList<OrderItem> orderItems;
    private String[] months = {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private OrderHorizontalAdapter mAdapter;
    private ArrayList<OrderDishItem> dishesList;

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        //Fields declarations
        public TextView restaurantName;
        public TextView dateView;
        public TextView priceView;
//        public LinearLayout dishesLayout;
        public SharedPreferences sharedPref;
        public Context context;

        public OrderViewHolder(View itemView) {
            super(itemView);
            //Find fields
            restaurantName = itemView.findViewById(R.id.order_restaurant);
            dateView = itemView.findViewById(R.id.order_date);
            priceView = itemView.findViewById(R.id.order_price);
//            dishesLayout = itemView.findViewById(R.id.order_dishes_layout);
            context = itemView.getContext();
        }
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order, parent, false);
        dishesList = new ArrayList<>();
        mRecyclerView = v.findViewById(R.id.order_dishes_layout);
        mLayoutManager = new LinearLayoutManager(v.getContext(), LinearLayoutManager.HORIZONTAL, false);
        return new OrderViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {

        OrderItem orderItem = orderItems.get(position);
        SharedPreferences sharedPref = ((AppCompatActivity)holder.context).getPreferences(Context.MODE_PRIVATE);
        String typeFromPreferences = sharedPref.getString("type", "");

        String restaurantName = orderItem.getRestaurantName();
        String user = orderItem.getUser();

        holder.restaurantName.setText(restaurantName);

//        if(typeFromPreferences.equals("user")){
//            holder.restaurantName.setText(restaurantName);
//        } else {
//            holder.restaurantName.setText(user);
//        }

        try {
            JSONArray orders = new JSONArray(orderItem.getDishes());
            for(int i=0; i<orders.length(); i++){
                JSONObject temp = orders.getJSONObject(i);
                Log.d("STRIG NAME", temp.getString("name"));
                dishesList.add(new OrderDishItem(
                        temp.getString("name"),
                        temp.getString("price"),
                        temp.getString("img")
                ));
            }
            mAdapter = new OrderHorizontalAdapter(dishesList);
            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
            Log.d("HELLO ORDERED DISHES", orders.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }


        String price = orderItem.getPrice() + " zł";
        String dishes = orderItem.getDishes();
//           Integer.toString(calendar.get(Calendar.DAY_OF_MONTH))
        Calendar calendar = Calendar.getInstance(new Locale("pl", "pl_PL"));
        calendar.setTimeInMillis(Long.parseLong(orderItem.getDate()));
//        GregorianCalendar cal = new GregorianCalendar();
        Integer month = calendar.get(Calendar.MONTH);
        String hour = Integer.toString(calendar.get(Calendar.HOUR_OF_DAY));
        String minute = Integer.toString(calendar.get(Calendar.MINUTE));
        SimpleDateFormat formatDay = new SimpleDateFormat("hh:mm");
//        Log.d("Formattedf", formatDay.format(calendar.get(Calendar.DAY_OF_MONTH)));
//        System.out.println(cal.getTime());
        if(hour.substring(0, 1).equals("0")){
            hour = "0" + hour;
        }
        if(Integer.parseInt(minute) <= 9){
            minute = "0" + minute;
        }
        String dateString = hour + ":" + minute + "   " +
                            Integer.toString(calendar.get(Calendar.DAY_OF_MONTH)) + " " +
                            months[month] + " " +
                            Integer.toString(calendar.get(Calendar.YEAR));

        //Doesn't do anything - create a recyclerview
//        try {
//            JSONArray ar = new JSONArray(dishes);
//            for(int i=0; i<ar.length(); i++){
//                JSONObject dish = ar.getJSONObject(i);
//                TextView tx = new TextView(holder.dishesLayout.getContext());
//                tx.setLeft(200);
////                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
////                params.leftMargin = i * 180;
////                tx.setLayoutParams(params);
//                tx.setText(dish.getString("name"));
//                //holder.dishesLayout.addView(tx);
//            }
//
//        } catch(JSONException e){
//            e.printStackTrace();
//        }

        holder.priceView.setText(price);
        holder.dateView.setText(dateString);

    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public OrderAdapter(ArrayList<OrderItem> orderItems){ this.orderItems = orderItems; }

}
