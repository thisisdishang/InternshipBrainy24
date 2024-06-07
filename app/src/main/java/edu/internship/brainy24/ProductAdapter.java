package edu.internship.brainy24;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyHolder> {
    Context context;
    SharedPreferences sp;
    SQLiteDatabase db;
    ArrayList<ProductList> arrayList;
    boolean isWishlist = false;


    public ProductAdapter(Context context, ArrayList<ProductList> arrayList, SharedPreferences sp, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.sp = sp;
        this.db = db;
    }

    @NonNull
    @Override
    public ProductAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product, parent, false);
        return new ProductAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView, wishlist;
        TextView name, price;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            price = itemView.findViewById(R.id.custom_product_price);
            wishlist = itemView.findViewById(R.id.custom_product_wishlist);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.name.setText(arrayList.get(position).getName());
        holder.price.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getPrice());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCT_ID, arrayList.get(position).getId()).commit();
                new CommonMethod(context, ProductDetailsActivity.class);
            }
        });

        if (arrayList.get(position).isWishlist()) {
            holder.wishlist.setImageResource(R.drawable.wishlist_fill);
            isWishlist = true;
        } else {
            holder.wishlist.setImageResource(R.drawable.wishlist_empty);
            isWishlist = false;
        }

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isWishlist) {
                    String deleteQuery = "DELETE FROM WISHLIST WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND PRODUCTID='" + arrayList.get(position).getId() + "'";
                    db.execSQL(deleteQuery);
                    holder.wishlist.setImageResource(R.drawable.wishlist_empty);
                    isWishlist = false;
                } else {
                    String insertQuery = "INSERT INTO WISHLIST VALUES (NULL,'" + sp.getString(ConstantSp.USERID, "") + "','" + arrayList.get(position).getId() + "')";
                    db.execSQL(insertQuery);
                    holder.wishlist.setImageResource(R.drawable.wishlist_fill);
                    isWishlist = true;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}