package com.app.shoptree.shoptree;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

public class PaymentsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RelativeLayout cartfooter;
    private TextView textgrandTotal;
    private DecimalFormat df = new DecimalFormat("0.#");
    private String paymentGrandTotal= "";
    private String addressid ="";
    private Button placeOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        cartfooter = (RelativeLayout) findViewById(R.id.paymentFooter);
        textgrandTotal = (TextView) findViewById(R.id.paymentGrandTotal);
        paymentGrandTotal = getIntent().getStringExtra("grandTotal");
        addressid =getIntent().getStringExtra("addressId");
        Log.i("JSON", paymentGrandTotal+addressid);

        textgrandTotal.setText(getResources().getString(R.string.Rs)+paymentGrandTotal);
        radioGroup = (RadioGroup) findViewById(R.id.paymentRadio);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.debitcard) {
                    Toast.makeText(getApplicationContext(), "choice: Silent",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.COD) {
                    Toast.makeText(getApplicationContext(), "choice: Sound",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });

    }
}
