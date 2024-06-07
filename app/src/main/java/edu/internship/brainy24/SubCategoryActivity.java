package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import java.util.ArrayList;

public class SubCategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] categoryIdArray = {"1", "1", "1", "1", "2", "2", "2", "3", "3", "5", "5", "5"};
    String[] nameArray = {"U.S.Polo", "H&M", "Louis Phillipe", "Allen Solly", "U.S.Polo", "Louis Phillipe", "Allen Solly", "Levis", "Mufti", "Casual Shoes", "Sports Shoes", "Sneakers Shoes"};
    int[] imageArray = {R.drawable.uspolo, R.drawable.handm, R.drawable.louisphillipe, R.drawable.allensolly, R.drawable.uspolo, R.drawable.louisphillipe, R.drawable.allensolly, R.drawable.levis, R.drawable.mufti, R.drawable.casual_shoes, R.drawable.sports_shoes, R.drawable.sneaker_shoes};

    ArrayList<SubCategoryList> arrayList;

    SQLiteDatabase db;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        db = openOrCreateDatabase("InternshipBrainy24.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY,USERNAME VARCHAR(100),NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        String categoryTableQuery = "CREATE TABLE IF NOT EXISTS CATEGORY(CATEGORYID INTEGER PRIMARY KEY,NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(categoryTableQuery);

        String subcategoryTableQuery = "CREATE TABLE IF NOT EXISTS SUBCATEGORY(SUBCATEGORYID INTEGER PRIMARY KEY,CATEGORYID VARCHAR(10),NAME VARCHAR(100),IMAGE VARCHAR(100))";
        db.execSQL(subcategoryTableQuery);


        for (int i = 0; i < nameArray.length; i++) {
            String selectQuery = "SELECT * FROM SUBCATEGORY WHERE NAME='" + nameArray[i] + "' AND CATEGORYID='" + categoryIdArray[i] + "'";
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.getCount() > 0) {
            } else {
                String insertQuery = "INSERT INTO SUBCATEGORY VALUES(NULL,'" + categoryIdArray[i] + "','" + nameArray[i] + "','" + imageArray[i] + "')";
                db.execSQL(insertQuery);
            }
        }

        recyclerView = findViewById(R.id.sub_category_recyclerview);

        //Set As List
        //recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

        //Set As Grid
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //Set As Scroll
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        String selectQuery = "SELECT * FROM SUBCATEGORY WHERE CATEGORYID='" + sp.getString(ConstantSp.CATEGORY_ID, "") + "'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.getCount() > 0) {
            arrayList = new ArrayList<>();
            while (cursor.moveToNext()) {
                SubCategoryList list = new SubCategoryList();
                list.setId(cursor.getString(0));
                list.setCategoryid(cursor.getString(1));
                list.setName(cursor.getString(2));
                list.setImage(cursor.getString(3));
                arrayList.add(list);
            }
            //CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, nameArray, imageArray);
            SubCategoryAdapter adapter = new SubCategoryAdapter(SubCategoryActivity.this, arrayList);
            recyclerView.setAdapter(adapter);
        } else {
            new CommonMethod(SubCategoryActivity.this, "Sub Category Not Found");
        }

    }
}