package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class DashboardActivity extends AppCompatActivity {

    TextView name;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);
        name = findViewById(R.id.dashboard_id);
        name.setText("Welcome " + sp.getString(ConstantSp.NAME, ""));
    }

}