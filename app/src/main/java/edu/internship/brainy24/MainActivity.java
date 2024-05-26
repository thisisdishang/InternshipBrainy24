package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity {

    EditText username, password;
    Button login;
    TextView signup, forgotPassword;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ImageView passwordHide, passwordShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = findViewById(R.id.main_username);
        password = findViewById(R.id.main_password);
        login = findViewById(R.id.main_login);
        signup = findViewById(R.id.main_signup);
        forgotPassword = findViewById(R.id.main_forgot);

        forgotPassword.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        passwordHide = findViewById(R.id.main_password_hidden);
        passwordShow = findViewById(R.id.main_password_show);

        passwordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordShow.setVisibility(View.GONE);
                passwordHide.setVisibility(View.VISIBLE);
                password.setTransformationMethod(null);
            }
        });

        passwordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordHide.setVisibility(View.GONE);
                passwordShow.setVisibility(View.VISIBLE);
                password.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().trim().equals("")) {
                    username.setError("Username Required");
                } else if (!username.getText().toString().trim().matches(emailPattern)) {
                    username.setError("Valid Email ID Required");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (password.getText().toString().trim().length() < 6) {
                    password.setError("Minimum 6 Char Password Required");
                } else {
                    System.out.println("Login Successfully");
                    Log.d("My Response", "Login Successfully");
                    //Toast.makeText(MainActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                    new CommonMethod(MainActivity.this, "Login Successfully");
                    //Snackbar.make(view, "Login Successfully", Snackbar.LENGTH_LONG).show();
                    new CommonMethod(view, "Login Successfully");
                    //Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
                    //startActivity(intent);
                    new CommonMethod(MainActivity.this, DashboardActivity.class);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, SignupActivity.class);
                //startActivity(intent);
                new CommonMethod(MainActivity.this,SignupActivity.class);
            }
        });

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(MainActivity.this, ForgotPasswordActivity.class);
                //startActivity(intent);
                new CommonMethod(MainActivity.this,ForgotPasswordActivity.class);
            }
        });
    }
}