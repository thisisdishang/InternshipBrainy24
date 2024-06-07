package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class ProductActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] categoryIdArray = {"1", "1", "2"};
    String[] subcategoryIdArray = {"1", "1", "7"};
    String[] nameArray = {"U.S. Polo Assn. Engineered Oxford White Shirt", "U S Polo Assn Men Blue Casual Shirt", "Allen Solly Solid Men Polo Neck Navy Blue T-Shirt"};
    int[] imageArray = {R.drawable.uspoloshirt1, R.drawable.uspoloshirt2, R.drawable.allensollytshirt1};
    String[] descriptionArray = {"Off white solid opaque casual shirt ,has a spread collar, button placket, long regular sleeves, curved hem", "Blue solid opaque Casual shirt, has a spread collar, button placket, 1 patch pocket, long regular sleeves, curved hem", "Mustard navy blue T-shirt for men Brand logo print Regular length Polo collar Short, regular sleeves Knitted cotton fabric"};
    String[] priceArray = {"1999", "1499", "699"};
    ArrayList<ProductList> arrayList;
    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

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


        for (int i = 0; i < nameArray.length; i++) {
            String selectQuery = "SELECT * FROM PRODUCT WHERE NAME='" + nameArray[i] + "'AND SUBCATEGORYID='" + subcategoryIdArray[i] + "' AND CATEGORYID='" + categoryIdArray[i] + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
            } else {
                String insertQuery = "INSERT INTO PRODUCT VALUES(NULL,'" + subcategoryIdArray[i] + "','" + categoryIdArray[i] + "','" + nameArray[i] + "','" + imageArray[i] + "','" + priceArray[i] + "','" + descriptionArray[i] + "')";
                db.execSQL(insertQuery);
            }
        }

        recyclerView = findViewById(R.id.product_recyclerview);

        //Set As List
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductActivity.this));

        //Set As Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //Set As Scroll
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM PRODUCT WHERE SUBCATEGORYID='" + sp.getString(ConstantSp.SUB_CATEGORY_ID, "") + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                ProductList list = new ProductList();
                list.setId(cursor.getString(0));
                list.setSubcategoryId(cursor.getString(1));
                list.setCategoryId(cursor.getString(2));
                list.setName(cursor.getString(3));
                list.setImage(cursor.getString(4));
                list.setPrice(cursor.getString(5));
                list.setDescription(cursor.getString(6));

                String selectQueryWishlist = "SELECT * FROM WISHLIST WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND PRODUCTID='" + cursor.getString(0) + "'";
                Cursor cursorWishlist = db.rawQuery(selectQueryWishlist, null);
                if (cursorWishlist.getCount() > 0) {
                    list.setWishlist(true);
                    while (cursorWishlist.moveToNext()) {
                        list.setWishlistid(cursorWishlist.getString(0));
                    }
                } else {
                    list.setWishlist(false);
                    list.setWishlistid("0");
                }
                arrayList.add(list);
            }
            //CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, nameArray, imageArray);
            ProductAdapter adapter = new ProductAdapter(ProductActivity.this, arrayList, sp, db);
            recyclerView.setAdapter(adapter);
        } else {
            new CommonMethod(ProductActivity.this, "Product Not Found");
        }
    }
}