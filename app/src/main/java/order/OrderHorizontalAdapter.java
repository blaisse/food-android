package order;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.food.food.food.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderHorizontalAdapter extends RecyclerView.Adapter<OrderHorizontalAdapter.OrderHorizontalViewHolder> {//<OrderHorizontalAdapter>

    private ArrayList<OrderDishItem> dishesList;

    private TextView nameView;
    private TextView priceView;

    private ImageView imgView;

    public class OrderHorizontalViewHolder  extends RecyclerView.ViewHolder {
        public OrderHorizontalViewHolder(View itemView) {
            super(itemView);

            nameView = itemView.findViewById(R.id.order_horizontal_name);
            priceView = itemView.findViewById(R.id.order_horizontal_price);
            imgView = itemView.findViewById(R.id.order_horizontal_img);
        }
    }

    public OrderHorizontalAdapter(ArrayList<OrderDishItem> dishesList){ this.dishesList = dishesList; };

    @NonNull
    @Override
    public OrderHorizontalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_order_horizontal, parent, false);
        return new OrderHorizontalViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHorizontalViewHolder holder, int position) {

        OrderDishItem dishItem = dishesList.get(position);
        nameView.setText(dishItem.getName());
//        priceView.setText(dishItem.getPrice());
        Picasso.get().load(dishItem.getImg()).into(imgView);

    }

    @Override
    public int getItemCount() {
        return dishesList.size();
    }

}
