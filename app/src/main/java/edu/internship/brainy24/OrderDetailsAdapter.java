package edu.internship.brainy24;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class OrderDetailsAdapter extends RecyclerView.Adapter<OrderDetailsAdapter.MyHolder> {

    Context context;
    ArrayList<OrderProductList> arrayList;

    public OrderDetailsAdapter(Context context, ArrayList<OrderProductList> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name, price;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.custom_order_product_image);
            name = itemView.findViewById(R.id.custom_order_product_name);
            price = itemView.findViewById(R.id.custom_order_product_price);
        }
    }

    @NonNull
    @Override
    public OrderDetailsAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_order_product, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailsAdapter.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.name.setText(arrayList.get(position).getName());

        int cartQty = Integer.parseInt(arrayList.get(position).getQty());
        int proPrice = Integer.parseInt(arrayList.get(position).getPrice());
        int cartPrice = proPrice * cartQty;

        holder.price.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getPrice() + " * " + arrayList.get(position).getQty() + " = " + cartPrice);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

}
