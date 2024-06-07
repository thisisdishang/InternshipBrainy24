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

    public ProductAdapter(Context context, ArrayList<ProductList> arrayList, SQLiteDatabase db) {
        this.context = context;
        this.arrayList = arrayList;
        this.db = db;
        sp = context.getSharedPreferences(ConstantSp.PREF, Context.MODE_PRIVATE);
    }

    @NonNull
    @Override
    public ProductAdapter.MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_product, parent, false);
        return new ProductAdapter.MyHolder(view);
    }

    public class MyHolder extends RecyclerView.ViewHolder {

        ImageView imageView, wishlist, cart;
        TextView name, price, brand;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            price = itemView.findViewById(R.id.custom_product_price);
            wishlist = itemView.findViewById(R.id.custom_product_wishlist);
            cart = itemView.findViewById(R.id.custom_product_cart);
            brand = itemView.findViewById(R.id.custom_product_brand);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.MyHolder holder, int position) {
        holder.imageView.setImageResource(Integer.parseInt(arrayList.get(position).getImage()));
        holder.name.setText(arrayList.get(position).getName());
        holder.price.setText(ConstantSp.PRICE_SYMBOL + arrayList.get(position).getPrice());
        holder.brand.setText(arrayList.get(position).getBrand());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sp.edit().putString(ConstantSp.PRODUCT_ID, arrayList.get(position).getId()).commit();
                new CommonMethod(context, ProductDetailsActivity.class);
            }
        });

        if (arrayList.get(position).isWishlist()) {
            holder.wishlist.setImageResource(R.drawable.wishlist_fill);
        } else {
            holder.wishlist.setImageResource(R.drawable.wishlist_empty);
        }

        holder.wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(position).isWishlist) {
                    String deleteQuery = "DELETE FROM WISHLIST WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND PRODUCTID='" + sp.getString(ConstantSp.PRODUCT_ID, "") + "'";
                    db.execSQL(deleteQuery);

                    ProductList list = new ProductList();
                    list.setId(arrayList.get(position).getId());
                    list.setCategoryId(arrayList.get(position).getCategoryId());
                    list.setSubcategoryId(arrayList.get(position).getSubcategoryId());
                    list.setName(arrayList.get(position).getName());
                    list.setImage(arrayList.get(position).getImage());
                    list.setPrice(arrayList.get(position).getPrice());
                    list.setDescription(arrayList.get(position).getDescription());
                    list.setWishlist(false);
                    arrayList.set(position, list);

                    // holder.wishlist.setImageResource(R.drawable.wishlist_empty);
                } else {
                    String insertQuery = "INSERT INTO WISHLIST VALUES(NULL,'" + sp.getString(ConstantSp.USERID, "") + "','" + sp.getString(ConstantSp.PRODUCT_ID, "") + "')";
                    db.execSQL(insertQuery);

                    ProductList list = new ProductList();
                    list.setId(arrayList.get(position).getId());
                    list.setCategoryId(arrayList.get(position).getCategoryId());
                    list.setSubcategoryId(arrayList.get(position).getSubcategoryId());
                    list.setName(arrayList.get(position).getName());
                    list.setImage(arrayList.get(position).getImage());
                    list.setPrice(arrayList.get(position).getPrice());
                    list.setDescription(arrayList.get(position).getDescription());
                    list.setWishlist(true);
                    arrayList.set(position, list);

                    //holder.wishlist.setImageResource(R.drawable.wishlist_fill);
                }
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}