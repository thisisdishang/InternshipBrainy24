package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class SignupActivity extends AppCompatActivity {

    EditText username, name, email, contact, password, confirmPassword;

    ImageView passwordHide, passwordShow, confirmpasswordHide, confirmpasswordShow;

    Button signup;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        username = findViewById(R.id.signup_username);
        name = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_emailid);
        contact = findViewById(R.id.signup_contactno);
        password = findViewById(R.id.signup_password);
        confirmPassword = findViewById(R.id.signup_confirm_password);
        passwordShow = findViewById(R.id.signup_password_show);
        passwordHide = findViewById(R.id.signup_password_hidden);
        confirmpasswordShow = findViewById(R.id.signup_confirm_password_show);
        confirmpasswordHide = findViewById(R.id.signup_confirm_password_hidden);
        signup = findViewById(R.id.signup_button);

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

        confirmpasswordShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmpasswordShow.setVisibility(View.GONE);
                confirmpasswordHide.setVisibility(View.VISIBLE);
                confirmPassword.setTransformationMethod(null);
            }
        });

        confirmpasswordHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confirmpasswordHide.setVisibility(View.GONE);
                confirmpasswordShow.setVisibility(View.VISIBLE);
                confirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().trim().equals("")) {
                    username.setError("Username Required");
                } else if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email ID Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email ID Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact Number Required");
                } else if (contact.getText().toString().trim().length()!=10) {
                    contact.setError("Contact Number Must Have 10 Digit");
                } else if (password.getText().toString().trim().equals("")) {
                    password.setError("Password Required");
                } else if (password.getText().toString().trim().length() < 6) {
                    password.setError("Minimum 6 Character Password Required");
                } else if (confirmPassword.getText().toString().trim().equals("")) {
                    confirmPassword.setError("Confirm Password Required");
                } else if (confirmPassword.getText().toString().trim().length() < 6) {
                    confirmPassword.setError("Minimum 6 Character Password Required");
                } else if (!password.getText().toString().trim().matches(confirmPassword.getText().toString().trim())) {
                    confirmPassword.setError("Password Does Not Match");
                } else {
                    new CommonMethod(SignupActivity.this, "Signup Successfully");
                    new CommonMethod(view, "Signup Successfully");
                    onBackPressed();
                }
            }
        });
    }
}