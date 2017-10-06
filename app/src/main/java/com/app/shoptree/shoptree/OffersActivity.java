package com.app.shoptree.shoptree;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

import static com.app.shoptree.shoptree.R.id.dynamicContent;

public class OffersActivity extends BaseActivity {

    LinearLayout dynamicContent,bottonNavBar;
    private Toolbar offerToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_offers);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_offers, null);
        dynamicContent.addView(wizard);

        offerToolbar = (Toolbar) findViewById(R.id.OfferToolbar);
        setSupportActionBar(offerToolbar);
    }
}
