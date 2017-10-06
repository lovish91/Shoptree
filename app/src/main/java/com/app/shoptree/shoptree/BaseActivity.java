package com.app.shoptree.shoptree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.UserInfo;

public class BaseActivity extends AppCompatActivity {

    RadioGroup radioGroup1;
    RadioButton deals;
    private UserInfo userInfo = new UserInfo();
    public static String USERID ="Userid";
    private String userid ="";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        radioGroup1=(RadioGroup)findViewById(R.id.radioGroup1);
        deals = (RadioButton)findViewById(R.id.Cart);
        SharedPreferences prfs = getSharedPreferences(USERID, Context.MODE_PRIVATE);
        userid = prfs.getString("cart_id", "");
        Log.d("fai", userid.toString()+"nmvm");
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                Intent in;
                Log.i("matching", "matching inside1 bro" + checkedId);
                switch (checkedId)
                {
                    case R.id.matching:
                        Log.i("matching", "matching inside1 matching" +  checkedId);
                        in=new Intent(getBaseContext(), MainActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.watchList:
                        Log.i("matching", "matching inside1 watchlistAdapter" + checkedId);

                        in = new Intent(getBaseContext(), OffersActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);

                        break;
                    case R.id.search:
                        Log.i("matching", "matching inside1 rate" + checkedId);

                        in = new Intent(getBaseContext(),SearchActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    case R.id.account:
                        Log.i("matching", "matching inside1 listing" + checkedId);

                        if (userid == " "){
                            in = new Intent(getBaseContext(),LoginActivity.class);
                            startActivity(in);
                            overridePendingTransition(0, 0);

                        }else {
                            in = new Intent(getBaseContext(), AccountActivity.class);
                            startActivity(in);
                            overridePendingTransition(0, 0);
                        }
                        break;
                    case R.id.Cart:
                        Log.i("matching", "matching inside1 deals" + checkedId);
                        in = new Intent(getBaseContext(), CartActivity.class);
                        startActivity(in);
                        overridePendingTransition(0, 0);
                        break;
                    default:
                        break;
                }
            }
        });
    }
}
