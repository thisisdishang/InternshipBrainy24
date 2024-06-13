package edu.internship.brainy24;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

        ImageView imageView, wishlist, cart, plus, minus;
        TextView name, price, brand, qty;
        LinearLayout cartLayout;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.custom_product_image);
            name = itemView.findViewById(R.id.custom_product_name);
            price = itemView.findViewById(R.id.custom_product_price);
            wishlist = itemView.findViewById(R.id.custom_product_wishlist);
            cart = itemView.findViewById(R.id.custom_product_cart);
            brand = itemView.findViewById(R.id.custom_product_brand);
            cartLayout = itemView.findViewById(R.id.custom_product_cart_layout);
            plus = itemView.findViewById(R.id.custom_product_plus);
            minus = itemView.findViewById(R.id.custom_product_minus);
            qty = itemView.findViewById(R.id.custom_product_qty);
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

        holder.qty.setText(String.valueOf(arrayList.get(position).getiQty()));

        if (arrayList.get(position).getsCartId().equalsIgnoreCase("")) {
            holder.cart.setVisibility(View.VISIBLE);
            holder.cartLayout.setVisibility(View.GONE);
        } else {
            holder.cart.setVisibility(View.GONE);
            holder.cartLayout.setVisibility(View.VISIBLE);
        }

        holder.cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectCartQuery = "SELECT * FROM CART WHERE PRODUCTID='" + arrayList.get(position).getId() + "' AND USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND ORDERID='0'";
                Cursor cursor1 = db.rawQuery(selectCartQuery, null);
                if (cursor1.getCount() > 0) {
                    new CommonMethod(context, "Product Already Added In Cart");
                } else {
                    String cartQuery = "INSERT INTO CART VALUES (NULL,'" + sp.getString(ConstantSp.USERID, "") + "','0','" + arrayList.get(position).getId() + "','1')";
                    db.execSQL(cartQuery);

                    String selectCartQuery2 = "SELECT * FROM CART WHERE PRODUCTID='" + arrayList.get(position).getId() + "' AND USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND ORDERID='0'";
                    Cursor cursor2 = db.rawQuery(selectCartQuery2, null);
                    if (cursor2.getCount() > 0) {
                        while (cursor2.moveToNext()) {
                            ProductList list = new ProductList();
                            list.setId(arrayList.get(position).getId());
                            list.setCategoryId(arrayList.get(position).getCategoryId());
                            list.setSubcategoryId(arrayList.get(position).getSubcategoryId());
                            list.setName(arrayList.get(position).getName());
                            list.setImage(arrayList.get(position).getImage());
                            list.setPrice(arrayList.get(position).getPrice());
                            list.setDescription(arrayList.get(position).getDescription());
                            list.setWishlist(arrayList.get(position).isWishlist());
                            list.setBrand(arrayList.get(position).getBrand());
                            list.setsCartId(cursor2.getString(0));
                            list.setiQty(1);
                            arrayList.set(position, list);
                        }
                    }
                    notifyDataSetChanged();
                }
            }
        });

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateData(position, "Plus");
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arrayList.get(position).getiQty() <= 1) {
                    deleteData(position);
                } else {
                    updateData(position, "Minus");
                }
            }
        });

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
                    list.setBrand(arrayList.get(position).getBrand());
                    list.setWishlist(false);
                    list.setsCartId(arrayList.get(position).getsCartId());
                    list.setiQty(arrayList.get(position).getiQty());
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
                    list.setBrand(arrayList.get(position).getBrand());
                    list.setWishlist(true);
                    list.setsCartId(arrayList.get(position).getsCartId());
                    list.setiQty(arrayList.get(position).getiQty());
                    arrayList.set(position, list);

                    //holder.wishlist.setImageResource(R.drawable.wishlist_fill);
                }
                notifyDataSetChanged();
            }
        });
    }

    private void deleteData(int position) {
        String deleteCart = "DELETE FROM CART WHERE CARTID='" + arrayList.get(position).getsCartId() + "'";
        db.execSQL(deleteCart);

        ProductList list = new ProductList();
        list.setId(arrayList.get(position).getId());
        list.setCategoryId(arrayList.get(position).getCategoryId());
        list.setSubcategoryId(arrayList.get(position).getSubcategoryId());
        list.setName(arrayList.get(position).getName());
        list.setImage(arrayList.get(position).getImage());
        list.setPrice(arrayList.get(position).getPrice());
        list.setDescription(arrayList.get(position).getDescription());
        list.setBrand(arrayList.get(position).getBrand());
        list.setWishlist(arrayList.get(position).isWishlist());
        list.setsCartId("");
        list.setiQty(0);
        arrayList.set(position, list);
        notifyDataSetChanged();
    }

    private void updateData(int position, String sType) {
        int iQty = arrayList.get(position).getiQty();
        if (sType.equalsIgnoreCase("Plus")) {
            iQty += 1;
        } else {
            iQty -= 1;
        }
        String updateCart = "UPDATE CART SET QTY='" + iQty + "' WHERE CARTID='" + arrayList.get(position).getsCartId() + "'";
        db.execSQL(updateCart);

        ProductList list = new ProductList();
        list.setId(arrayList.get(position).getId());
        list.setCategoryId(arrayList.get(position).getCategoryId());
        list.setSubcategoryId(arrayList.get(position).getSubcategoryId());
        list.setName(arrayList.get(position).getName());
        list.setImage(arrayList.get(position).getImage());
        list.setPrice(arrayList.get(position).getPrice());
        list.setDescription(arrayList.get(position).getDescription());
        list.setBrand(arrayList.get(position).getBrand());
        list.setWishlist(arrayList.get(position).isWishlist());
        list.setsCartId(arrayList.get(position).getsCartId());
        list.setiQty(iQty);
        arrayList.set(position, list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}