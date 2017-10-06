package com.app.shoptree.shoptree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

public class AddAdressesActivity extends AppCompatActivity {
    private EditText UserMobile,UserLocationName,UserAddreddLine1,UserAddressLine2,UserAddressLine3,UserPin;
    private Spinner UserState, UserCity;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adresses);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        UserMobile = (EditText) findViewById(R.id.UserMobile);
        UserLocationName = (EditText) findViewById(R.id.UserLocationName);
        UserAddreddLine1 = (EditText) findViewById(R.id.UserAddreddLine1);
        UserAddressLine2 = (EditText) findViewById(R.id.UserAddreddLine2);
        UserAddressLine3 = (EditText) findViewById(R.id.UserAddreddLine3);
        UserPin = (EditText) findViewById(R.id.UserPin);
        UserState = (Spinner) findViewById(R.id.UserState);
        UserCity = (Spinner) findViewById(R.id.UserCity);
    }
}
