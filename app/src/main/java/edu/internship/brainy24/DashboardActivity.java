package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    TextView name;
    SharedPreferences sp;
    Button profile, logout, category, wishlist, cart, myorders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        name = findViewById(R.id.dashboard_id);
        name.setText("Welcome " + sp.getString(ConstantSp.NAME, ""));
        profile = findViewById(R.id.dashboard_profile);
        logout = findViewById(R.id.dashboard_logout);
        category = findViewById(R.id.dashboard_category);
        wishlist = findViewById(R.id.dashboard_wishlist);
        cart = findViewById(R.id.dashboard_cart);
        myorders = findViewById(R.id.dashboard_my_orders);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(DashboardActivity.this);
                builder.setTitle("Logout!");
                builder.setMessage("Are You Sure Want To Logout?");
                builder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //sp.edit().remove(ConstantSp.USERID).commit();
                        sp.edit().clear().commit();
                        new CommonMethod(DashboardActivity.this, "Logout Successfully");
                        new CommonMethod(DashboardActivity.this, MainActivity.class);
                        finish();
                    }
                });
                builder.show();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, ProfileActivity.class);
            }
        });

        category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, CategoryActivity.class);
            }
        });

        wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, WishlistActivity.class);
            }
        });

        cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, CartActivity.class);
            }
        });

        myorders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new CommonMethod(DashboardActivity.this, MyOrdersActivity.class);
            }
        });
    }

}