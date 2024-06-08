package edu.internship.brainy24;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    SQLiteDatabase db;
    SharedPreferences sp;
    RecyclerView recyclerView;
    ArrayList<CartList> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        db = openOrCreateDatabase("InternshipBrainy24.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY,USERNAME VARCHAR(100),NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTableQuery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categoryTableQuery);

        String subcategoryTableQuery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subcategoryTableQuery);

        String productTableQuery = "CREATE TABLE IF NOT EXISTS PRODUCT(PRODUCTID INTEGER PRIMARY KEY,SUBCATEGORYID VARCHAR(10),CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100),PRICE VARCHAR(20),DESCRIPTION TEXT)";
        db.execSQL(productTableQuery);

        String wishlistTableQuery = "CREATE TABLE IF NOT EXISTS WISHLIST(WISHLISTID INTEGER PRIMARY KEY,USERID VARCHAR(10),PRODUCTID VARCHAR(10))";
        db.execSQL(wishlistTableQuery);

        String cartTableQyery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY,USERID VARCHAR(10),ORDERID VARCHAR(10),PRODUCTID VARCHAR(10),QTY VARCHAR(10))";
        db.execSQL(cartTableQyery);

        recyclerView = findViewById(R.id.cart_recyclerview);

        recyclerView.setLayoutManager(new LinearLayoutManager(CartActivity.this));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM CART WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND ORDERID='0'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                CartList list = new CartList();
                list.setCartId(cursor.getString(0));

                String productQuery = "SELECT * FROM PRODUCT WHERE PRODUCTID='" + cursor.getString(3) + "'";
                Cursor cursorProduct = db.rawQuery(productQuery, null);
                while (cursorProduct.moveToNext()) {
                    list.setProductId(cursorProduct.getString(0));
                    list.setSubcategoryId(cursorProduct.getString(1));
                    list.setCategoryId(cursorProduct.getString(2));
                    list.setName(cursorProduct.getString(3));
                    list.setImage(cursorProduct.getString(4));
                    list.setPrice(cursorProduct.getString(5));
                    list.setDescription(cursorProduct.getString(6));
                }
                list.setQty(cursor.getString(4));
                arrayList.add(list);
            }

            CartAdapter adapter = new CartAdapter(CartActivity.this, arrayList, sp, db);
            recyclerView.setAdapter(adapter);
        } else {
            new CommonMethod(CartActivity.this, "No Any Product Added In Wishlist");
        }

    }
}