package edu.internship.brainy24;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyHolder> {
    Context context;
    ArrayList<CartList> arrayList;
    SharedPreferences sp;
    SQLiteDatabase db;

    public CartAdapter(Context context, ArrayList<CartList> arrayList, SharedPreferences sp, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.sp = sp;
        this.db = db;
    }

    @NonNull
    @Override
    public CartAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_cart, parent, false);
        return new CartAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView, plus, minus;
        TextView name, price, qty;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_cart_image);
            name = itemView.findViewById(R.id.custom_cart_name);
            price = itemView.findViewById(R.id.custom_cart_price);
            qty = itemView.findViewById(R.id.custom_cart_qty);
            plus = itemView.findViewById(R.id.custom_cart_plus);
            minus = itemView.findViewById(R.id.custom_cart_minus);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.name.setText(arrayList.get(position).getName());

        int cartQty = Integer.parseInt(arrayList.get(position).getQty());
        int proPrice = Integer.parseInt(arrayList.get(position).getPrice());
        int cartPrice = proPrice * cartQty;

        holder.price.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getPrice() + " * " + arrayList.get(position).getQty() + " = " + cartPrice);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCT_ID, arrayList.get(position).getProductId()).commit();
                new CommonMethod(context, ProductDetailsActivity.class);
            }
        });

        holder.qty.setText(arrayList.get(position).getQty());

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iQty = Integer.parseInt(arrayList.get(position).getQty()) + 1;
                String sCartid = arrayList.get(position).getCartId();
                updateCart(position, iQty, sCartid);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int iQty = Integer.parseInt(arrayList.get(position).getQty()) - 1;
                String sCartid = arrayList.get(position).getCartId();

                if (iQty <= 0) {
                    String deleteQuery = "DELETE FROM CART WHERE CARTID='" + sCartid + "'";
                    db.execSQL(deleteQuery);
                    arrayList.remove(position);
                    notifyDataSetChanged();
                } else {
                    updateCart(position, iQty, sCartid);
                }
            }
        });
    }

    private void updateCart(int position, int iQty, String sCartid) {
        String updateQuery = "UPDATE CART SET QTY='" + iQty + "' WHERE CARTID='" + sCartid + "'";
        db.execSQL(updateQuery);
        CartList list = new CartList();
        list.setCartId(arrayList.get(position).getCartId());
        list.setCategoryId(arrayList.get(position).getCategoryId());
        list.setSubcategoryId(arrayList.get(position).getSubcategoryId());
        list.setProductId(arrayList.get(position).getProductId());
        list.setName(arrayList.get(position).getName());
        list.setImage(arrayList.get(position).getImage());
        list.setPrice(arrayList.get(position).getPrice());
        list.setDescription(arrayList.get(position).getDescription());
        list.setQty(String.valueOf(iQty));
        arrayList.set(position, list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}