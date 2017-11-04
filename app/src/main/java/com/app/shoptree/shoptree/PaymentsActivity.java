package com.app.shoptree.shoptree;

import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.UserInfo;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PaymentsActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private RelativeLayout cartfooter;
    private TextView textgrandTotal;
    private DecimalFormat df = new DecimalFormat("0.#");
    private String paymentGrandTotal= "";
    private String addressid ="";
    private Button placeOrder;
    private String paymentOption = "null";
    private SharedPrefs sharedPrefs;
    private UserInfo userInfo;
    private ApiInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payments);

        cartfooter = (RelativeLayout) findViewById(R.id.paymentFooter);
        textgrandTotal = (TextView) findViewById(R.id.paymentGrandTotal);
        sharedPrefs = new SharedPrefs ();
        userInfo = new UserInfo ();
        userInfo = sharedPrefs.getUserInfo (getBaseContext ());
        paymentGrandTotal = getIntent().getStringExtra("grandTotal");
        addressid = getIntent().getStringExtra("addressId");
        Log.i("JSON", paymentGrandTotal+addressid);
        apiInterface = RetroFit.getClient ().create (ApiInterface.class);
        placeOrder = (Button) findViewById (R.id.Place_Order);
        textgrandTotal.setText(getResources().getString(R.string.Rs)+paymentGrandTotal);
        radioGroup = (RadioGroup) findViewById(R.id.paymentRadio);
        int id = radioGroup.getCheckedRadioButtonId ();
        if(id == R.id.debitcard){
            paymentOption = "debitcard";
        }else if(id == R.id.COD){
            paymentOption = "COD";
        }
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                if(checkedId == R.id.debitcard) {
                    paymentOption = "null";
                    Toast.makeText(getApplicationContext(), "choice: Silent",
                            Toast.LENGTH_SHORT).show();
                } else if(checkedId == R.id.COD) {
                    paymentOption = "COD";
                    Toast.makeText(getApplicationContext(), "choice: Sound",
                            Toast.LENGTH_SHORT).show();

                }
            }
        });
        placeOrder.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View view) {
                Log.i("payment", paymentGrandTotal+addressid +paymentOption);
                if (paymentOption.equals ("COD")){
                    JsonObject jsonObject =  new JsonObject ();
                    jsonObject.addProperty ("UserID",userInfo.getUserID ());
                    jsonObject.addProperty ("UserAddressID",addressid);
                    jsonObject.addProperty ("PaymentType",paymentOption);
                    apiInterface.placeOrder (jsonObject).enqueue (new Callback<ResponseBody> () {
                        @Override
                        public void onResponse (Call<ResponseBody> call, Response<ResponseBody> response) {
                            if(response.isSuccessful ()){
                                Toast.makeText(getBaseContext(),"Order is Place" ,Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onFailure (Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }else {
                    Toast.makeText(getBaseContext(),paymentOption +"Under Process" ,Toast.LENGTH_LONG).show();

                }

            }
        });

    }
}
