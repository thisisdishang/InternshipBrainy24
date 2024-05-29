package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

public class CategoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    String[] nameArray = {"Shirts", "T-Shirts", "Jeans", "Shorts & Trousers", "Casual Shoes", "Infant Essentials"};
    int[] imageArray = {R.drawable.shirt, R.drawable.tshirt, R.drawable.jeans, R.drawable.trouser, R.drawable.shoe, R.drawable.infant};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        recyclerView = findViewById(R.id.category_recyclerview);

        //Set As List
        //recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));

        //Set As Grid
        //recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        //Set As Scroll
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.HORIZONTAL));

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        CategoryAdapter adapter = new CategoryAdapter(CategoryActivity.this, nameArray, imageArray);
        recyclerView.setAdapter(adapter);

    }
}