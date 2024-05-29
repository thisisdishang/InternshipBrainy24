package edu.internship.brainy24;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

public class ProfileActivity extends AppCompatActivity {
    EditText username, name, email, contact, password, confirmPassword;

    ImageView passwordHide, passwordShow, confirmpasswordHide, confirmpasswordShow;

    Button editProfile, submit;

    RadioButton male, female;
    RadioGroup gender;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    Spinner city;
    String[] cityArray = {"Select City", "Surat", "Vadodara", "Somnath", "Ahmedabad", "Bhuj", "Bhavnagar", "Rajkot", "Valsad"};

    String sCity = "";

    String sGender = "";

    FrameLayout passwordLayout, confirmpasswordLayout;

    SQLiteDatabase db;

    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = openOrCreateDatabase("InternshipBrainy24.db", MODE_PRIVATE, null);
        String tableQuery = "CREATE TABLE IF NOT EXISTS USERS(USERID INTEGER PRIMARY KEY,USERNAME VARCHAR(100),NAME VARCHAR(100),EMAIL VARCHAR(100),CONTACT BIGINT(10),PASSWORD VARCHAR(20),GENDER VARCHAR(6),CITY VARCHAR(20))";
        db.execSQL(tableQuery);

        sp = getSharedPreferences(ConstantSp.PREF, MODE_PRIVATE);

        username = findViewById(R.id.profile_username);
        name = findViewById(R.id.profile_name);
        email = findViewById(R.id.profile_emailid);
        contact = findViewById(R.id.profile_contactno);
        password = findViewById(R.id.profile_password);
        confirmPassword = findViewById(R.id.profile_confirm_password);
        passwordShow = findViewById(R.id.profile_password_show);
        passwordHide = findViewById(R.id.profile_password_hidden);
        confirmpasswordShow = findViewById(R.id.profile_confirm_password_show);
        confirmpasswordHide = findViewById(R.id.profile_confirm_password_hidden);
        submit = findViewById(R.id.profile_submit);
        editProfile = findViewById(R.id.profile_edit);
        male = findViewById(R.id.profile_male);
        female = findViewById(R.id.profile_female);
        gender = findViewById(R.id.profile_gender);
        city = findViewById(R.id.profile_city);

        passwordLayout = findViewById(R.id.profile_password_frame);
        confirmpasswordLayout = findViewById(R.id.profile_confirm_password_frame);


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

        submit.setOnClickListener(new View.OnClickListener() {
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
                } else if (contact.getText().toString().trim().length() != 10) {
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
                } else if (gender.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(ProfileActivity.this, "Please Select Gender");
                } else if (sCity.equals("")) {
                    new CommonMethod(ProfileActivity.this, "Please Select City");
                } else {
                    String selectQuery = "SELECT * FROM USERS WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "'";
                    Cursor cursor = db.rawQuery(selectQuery, null);
                    if (cursor.getCount() > 0) {
                        String updateQuery = "UPDATE USERS SET USERNAME='" + username.getText().toString() + "',NAME='" + name.getText().toString() + "',EMAIL='" + email.getText().toString() + "',CONTACT='" + contact.getText().toString() + "',PASSWORD='" + password.getText().toString() + "',GENDER='" + sGender + "',CITY='" + sCity + "' WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "'";
                        db.execSQL(updateQuery);
                        new CommonMethod(ProfileActivity.this, "Profile Update Successfully");
                        new CommonMethod(view, "Profile Update Successfully");

                        sp.edit().putString(ConstantSp.USERNAME, username.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.NAME, name.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.EMAIL, email.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.CONTACT, contact.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.PASSWORD, password.getText().toString()).commit();
                        sp.edit().putString(ConstantSp.GENDER, sGender).commit();
                        sp.edit().putString(ConstantSp.CITY, sCity).commit();

                        setData(false);
                    } else {
                        new CommonMethod(ProfileActivity.this, "Invalid User");
                        new CommonMethod(view, "Invalid User");
                    }
                }
            }
        });

//      Improve coding standard
        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sGender = radioButton.getText().toString();
                new CommonMethod(ProfileActivity.this, sGender);
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(ProfileActivity.this, android.R.layout.simple_list_item_checked, cityArray);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = cityArray[i];
                    new CommonMethod(ProfileActivity.this, cityArray[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setData(true);
            }
        });

        setData(false);
    }

    private void setData(boolean b) {
        username.setText(sp.getString(ConstantSp.USERNAME, ""));
        name.setText(sp.getString(ConstantSp.NAME, ""));
        email.setText(sp.getString(ConstantSp.EMAIL, ""));
        contact.setText(sp.getString(ConstantSp.CONTACT, ""));
        password.setText(sp.getString(ConstantSp.PASSWORD, ""));
        confirmPassword.setText(sp.getString(ConstantSp.PASSWORD, ""));

        sGender = sp.getString(ConstantSp.GENDER, "");
        if (sGender.equalsIgnoreCase("Male")) {
            male.setChecked(true);
            female.setChecked(false);
        } else if (sGender.equalsIgnoreCase("Female")) {
            male.setChecked(false);
            female.setChecked(true);
        } else {
            male.setChecked(false);
            female.setChecked(false);
        }

        sCity = sp.getString(ConstantSp.CITY, "");
        int iCityPosition = 0;
        for (int i = 0; i < cityArray.length; i++) {
            if (cityArray[i].equals(sCity)) {
                iCityPosition = i;
                break;
            }
        }
        city.setSelection(iCityPosition);

        username.setEnabled(b);
        name.setEnabled(b);
        email.setEnabled(b);
        contact.setEnabled(b);
        male.setEnabled(b);
        female.setEnabled(b);
        city.setEnabled(b);

        if (b) {
            editProfile.setVisibility(View.GONE);
            submit.setVisibility(View.VISIBLE);
            passwordLayout.setVisibility(View.VISIBLE);
            confirmpasswordLayout.setVisibility(View.VISIBLE);
        } else {
            editProfile.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            passwordLayout.setVisibility(View.GONE);
            confirmpasswordLayout.setVisibility(View.GONE);
        }
    }
}