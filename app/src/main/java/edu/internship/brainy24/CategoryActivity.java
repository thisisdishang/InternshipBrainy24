package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] nameArray = {"Shirts", "T-Shirts", "Jeans", "Shorts & Trousers", "Shoes", "Infant Essentials"};
    int[] imageArray = {R.drawable.shirt, R.drawable.tshirt, R.drawable.jeans, R.drawable.trouser, R.drawable.shoe, R.drawable.infant};

    ArrayList<CategoryList> arrayList;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        db = openOrCreateDatabase("InternshipBrainy24.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY,USERNAME VARCHAR(100),NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTableQuery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categoryTableQuery);

        for (int i = 0; i < nameArray.length; i++) {
            String selectQuery = "SELECT * FROM CATEGORY WHERE NAME='" + nameArray[i] + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
            } else {
                String insertQuery = "INSERT INTO CATEGORY VALUES(NULL,'" + nameArray[i] + "','" + imageArray[i] + "')";
                db.execSQL(insertQuery);
            }
        }

        recyclerView = findViewById(R.id.category_recyclerview);

        //Set As List
        //recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

        //Set As Grid
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //Set As Scroll
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM CATEGORY";
        Cursor cursor = db.rawQuery(selectQuery, null);
        arrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            CategoryList list = new CategoryList();
            list.setCategoryId(cursor.getString(0));
            list.setName(cursor.getString(1));
            list.setImage(cursor.getString(2));
            arrayList.add(list);
        }

        //CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, nameArray, imageArray);
        CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, arrayList);
        recyclerView.setAdapter(adapter);

    }
}