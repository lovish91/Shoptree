package com.app.shoptree.shoptree;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.shoptree.shoptree.Adapter.Account_Adapter;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.AccountModel;
import com.app.shoptree.shoptree.model.UserInfo;

import java.util.ArrayList;

public class AccountActivity extends BaseActivity {
    LinearLayout dynamicContent,bottonNavBar;
    private Toolbar AcctountToolbar;
    private TextView logout,hisory,myorders,mydeliveryaddress,username,emailid;
    public static String USERID = "Userid";
    private ListView accountlist;
    private Account_Adapter account_adapter;
    private SharedPrefs sharedPrefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_account);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_account, null);
        dynamicContent.addView(wizard);


        String id[] = {"1","2","3","4"};
        String name[] = {"My Orders","My Addresses","Change Password","Logout"};

        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.account);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_person_clicked, 0,0);
        rb.getBackground().setColorFilter(0xFFBBAA00, PorterDuff.Mode.MULTIPLY);
        rb.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        sharedPrefs = new SharedPrefs ();
        AcctountToolbar = (Toolbar) findViewById(R.id.AcctountToolbar);
        setSupportActionBar(AcctountToolbar);
        AcctountToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        AcctountToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        accountlist = (ListView) findViewById (R.id.accountlist);

        ArrayList<AccountModel> accountModels = new ArrayList<> ();
        for(int i = 0; i <id.length; i++){
            AccountModel accountModel = new AccountModel (id[i],name[i]);
            accountModels.add (accountModel);
        }
        account_adapter  = new Account_Adapter (this,accountModels);
        accountlist.setAdapter (account_adapter);

        UserInfo  userInfo =  sharedPrefs.getUserInfo (getBaseContext ());
        username = (TextView) findViewById (R.id.Username);
        emailid = (TextView) findViewById (R.id.UserEmailid);
        username.setText (userInfo.getUserFirstName () +" "+userInfo.getUserLastName ());
        emailid.setText (userInfo.getUserEmailID ());

        /*myorders = (TextView) findViewById (R.id.myorder);
        myorders.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (AccountActivity.this, MyOrdersActivity.class);
                startActivity (intent);
            }
        });
        hisory = (TextView) findViewById (R.id.myorderhistory);
        hisory.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (AccountActivity.this, OrderDetail_Activity.class);
                startActivity (intent);
            }
        });
        logout = (TextView) findViewById (R.id.logout);
        logout.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                sharedPrefs =  new SharedPrefs ();
                sharedPrefs.userLogout (getBaseContext ());

                Intent intent = new Intent (AccountActivity.this,MainActivity.class);
                startActivity (intent);
                finish ();
            }
        });
        mydeliveryaddress = (TextView) findViewById (R.id.mydeliveryaddress);
        mydeliveryaddress.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Intent intent = new Intent (AccountActivity.this, OTP_PhoneNo_Activity.class);
                startActivity (intent);
            }
        });*/
    }
}
