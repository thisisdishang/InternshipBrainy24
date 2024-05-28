package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences sp;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        imageView = findViewById(R.id.splash_image);

//        Picasso
//                .get()
//                .load("https://i.pinimg.com/originals/69/8a/66/698a6629921c7f080b9e6c48fd90a96d.gif")
//                .placeholder(R.mipmap.ic_launcher)
//                .into(imageView);

        Glide
                .with(SplashActivity.this)
                .asGif()
                .load("https://i.pinimg.com/originals/69/8a/66/698a6629921c7f080b9e6c48fd90a96d.gif")
                .placeholder(R.mipmap.ic_launcher)
                .into(imageView);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (sp.getString(ConstantSp.USERID, "").equalsIgnoreCase("")) {
                    new CommonMethod(SplashActivity.this, MainActivity.class);
                } else {
                    new CommonMethod(SplashActivity.this, DashboardActivity.class);
                }
            }
        }, 5000);
    }
}