package com.app.shoptree.shoptree;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class AccountActivity extends BaseActivity {
    LinearLayout dynamicContent,bottonNavBar;
    private Toolbar AcctountToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_account);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_account, null);
        dynamicContent.addView(wizard);



        //get the reference of RadioGroup.

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.account);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_person_clicked, 0,0);
        rb.getBackground().setColorFilter(0xFFBBAA00, PorterDuff.Mode.MULTIPLY);
        rb.setTextColor(getResources().getColor(R.color.colorPrimaryDark));

        AcctountToolbar = (Toolbar) findViewById(R.id.AcctountToolbar);
        setSupportActionBar(AcctountToolbar);
        AcctountToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        AcctountToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}
