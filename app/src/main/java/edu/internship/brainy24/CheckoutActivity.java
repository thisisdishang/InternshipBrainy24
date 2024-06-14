package edu.internship.brainy24;

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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class CheckoutActivity extends AppCompatActivity {
    EditText name, email, contact, address, pincode;
    Button checkout;
    RadioGroup payvia;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    Spinner city;
    String[] cityArray = {"Select City", "Surat", "Vadodara", "Somnath", "Ahmedabad", "Bhuj", "Bhavnagar", "Rajkot", "Valsad"};
    String sCity = "";
    String sPayvia = "";
    SQLiteDatabase db;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

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

        String cartTableQuery = "CREATE TABLE IF NOT EXISTS CART(CARTID INTEGER PRIMARY KEY,USERID VARCHAR(10),ORDERID VARCHAR(10),PRODUCTID VARCHAR(10),QTY VARCHAR(10))";
        db.execSQL(cartTableQuery);

        String orderTableQuery = "CREATE TABLE IF NOT EXISTS TBL_ORDER (ORDERID INTEGER PRIMARY KEY,USERID VARCHAR(10),NAME VARCHAR(10),EMAIL VARCHAR(100),CONTACT VARCHAR(10),ADDRESS TEXT,CITY VARCHAR(50),PINCODE BIGINT(6),PAYVIA VARCHAR(10),TRANSACTIONID VARCHAR(100),TOTALAMOUNT VARCHAR(20))";
        db.execSQL(orderTableQuery);

        name = findViewById(R.id.checkout_name);
        email = findViewById(R.id.checkout_emailid);
        contact = findViewById(R.id.checkout_contactno);
        address = findViewById(R.id.checkout_address);
        pincode = findViewById(R.id.checkout_pincode);
        checkout = findViewById(R.id.checkout_button);
        payvia = findViewById(R.id.checkout_payvia);
        city = findViewById(R.id.checkout_city);

        name.setText(sp.getString(ConstantSp.NAME, ""));
        email.setText(sp.getString(ConstantSp.EMAIL, ""));
        contact.setText(sp.getString(ConstantSp.CONTACT, ""));

        checkout.setText("Pay Now: " + ConstantSp.PRICE_SYMBOL + sp.getString(ConstantSp.TOTAL_AMOUNT, ""));
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (name.getText().toString().trim().equals("")) {
                    name.setError("Name Required");
                } else if (email.getText().toString().trim().equals("")) {
                    email.setError("Email ID Required");
                } else if (!email.getText().toString().trim().matches(emailPattern)) {
                    email.setError("Valid Email ID Required");
                } else if (contact.getText().toString().trim().equals("")) {
                    contact.setError("Contact Number Required");
                } else if (contact.getText().toString().trim().length() != 10) {
                    contact.setError("Contact Number Must Have 10 Digit");
                } else if (address.getText().toString().trim().equals("")) {
                    address.setError("Address Required");
                } else if (pincode.getText().toString().trim().equals("")) {
                    pincode.setError("Pincode Required");
                } else if (pincode.getText().toString().trim().length() < 6) {
                    pincode.setError("6 Character Pincode Required");
                } else if (sCity.equals("")) {
                    new CommonMethod(CheckoutActivity.this, "Please Select City");
                } else if (payvia.getCheckedRadioButtonId() == -1) {
                    new CommonMethod(CheckoutActivity.this, "Please Select Payvia");
                } else {
                    if (sPayvia.equalsIgnoreCase("COD")) {
                        String insertQuery = "INSERT INTO TBL_ORDER VALUES (NULL,'" + sp.getString(ConstantSp.USERID, "") + "','" + name.getText().toString() + "','" + email.getText().toString() + "','" + contact.getText().toString() + "','" + address.getText().toString() + "','" + sCity + "','" + pincode.getText().toString() + "','" + sPayvia + "','','" + sp.getString(ConstantSp.TOTAL_AMOUNT, "") + "')";
                        db.execSQL(insertQuery);

                        String selectQuery = "SELECT MAX(ORDERID) FROM TBL_ORDER LIMIT 1";
                        Cursor cursor = db.rawQuery(selectQuery, null);
                        if (cursor.getCount() > 0) {
                            while (cursor.moveToNext()) {
                                String updateCartQuery = "UPDATE CART SET ORDERID='" + cursor.getString(0) + "' WHERE USERID='" + sp.getString(ConstantSp.USERID, "") + "' AND ORDERID='0'";
                                db.execSQL(updateCartQuery);
                            }
                        }

                        new CommonMethod(CheckoutActivity.this, "Order Placed Successfully");
                        new CommonMethod(view, "Order Placed Successfully");
                        new CommonMethod(CheckoutActivity.this, DashboardActivity.class);
                        finish();
                    }
                }
            }
        });


//      Improve coding standard
        payvia.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton radioButton = findViewById(i);
                sPayvia = radioButton.getText().toString();
                new CommonMethod(CheckoutActivity.this, sPayvia);
            }
        });

        ArrayAdapter adapter = new ArrayAdapter(CheckoutActivity.this, android.R.layout.simple_list_item_checked, cityArray);
        city.setAdapter(adapter);

        city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    sCity = "";
                } else {
                    sCity = cityArray[i];
                    new CommonMethod(CheckoutActivity.this, cityArray[i]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        sCity = sp.getString(ConstantSp.CITY, "");
        int iCityPosition = 0;
        for (int i = 0; i < cityArray.length; i++) {
            if (cityArray[i].equals(sCity)) {
                iCityPosition = i;
                break;
            }
        }
        city.setSelection(iCityPosition);

    }
}