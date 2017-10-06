package com.app.shoptree.shoptree;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Adapter.AddressAdapter;
import com.app.shoptree.shoptree.model.Address;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.app.shoptree.shoptree.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyAddressActivity extends AppCompatActivity {
    private ListView addresslist;
    private Toolbar myAddressToolbar;
    private Button continueToCart;
    private RelativeLayout myaddressfooter;
    private String grandTotal;
    private TextView myaddressgrnadtotal;
    public static int chooseaddr = 0;
    public static String addressid = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_address);

        chooseaddr = 0;

        addresslist = (ListView) findViewById(R.id.addresslist);
        myAddressToolbar = (Toolbar) findViewById(R.id.myaddresstoolbar);
        setSupportActionBar(myAddressToolbar);
        getSupportActionBar().setTitle("");
        myAddressToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        myAddressToolbar.setTitle("");
        myaddressgrnadtotal = (TextView) findViewById(R.id.myaddressGrandTotal);
        continueToCart = (Button) findViewById(R.id.continueToCart);
        myaddressfooter = (RelativeLayout) findViewById(R.id.myaddressfooter);
        grandTotal = getIntent().getStringExtra("grandTotal");
        if (grandTotal.equals(null)) {
            myaddressfooter.setVisibility(View.INVISIBLE);
        }
        myaddressgrnadtotal.setText(getResources().getString(R.string.Rs) + grandTotal);
        new MyAddress().execute("https://shopptree.com/api/api_useraddress/?userid=USER1001");
    }

    private class MyAddress extends AsyncTask<String, Void, ArrayList<Address>> {
        //String userAddId = "USER1001_1";

        @Override
        protected ArrayList<Address> doInBackground(String... urls) {
            ArrayList<Address> addressArrayList = new ArrayList<>();
            addressArrayList.clear();
            String abc = JsonParser.getData(urls[0]);
            try {
                JSONArray jsonArray = new JSONArray(abc);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String userAddId = jsonObject.getString("UserAddressID");
                    String userID = jsonObject.getString("UserID");
                    String UserEmailID = jsonObject.getString("UserEmailid");
                    String UserMobile = jsonObject.getString("UserMobile");
                    String UserLocationName = jsonObject.getString("UserLocationName");
                    String userAddreddLine1 = jsonObject.getString("UserAddressLine1");
                    String userAddressLine2 = jsonObject.getString("UserAddressLine2");
                    String userAddressLine3 = jsonObject.getString("UserAddressLine3");
                    String userCity = jsonObject.getString("UserCity");
                    String userState = jsonObject.getString("UserState");
                    String userPin = jsonObject.getString("UserPin");
                    int Status = Integer.valueOf(jsonObject.getString("Status"));


                    Address categoryModel = new Address(userAddId, userID, UserEmailID, UserMobile, UserLocationName, userAddreddLine1,
                            userAddressLine2, userAddressLine3, userCity, userState, userPin, Status);
                    addressArrayList.add(categoryModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return addressArrayList;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<Address> result) {
            Log.i("JSON", result.toString());

            //Toast.makeText(getBaseContext(), result.toString(), Toast.LENGTH_SHORT).show();
            final AddressAdapter addressAdapter = new AddressAdapter(getBaseContext(), R.layout.address_item_layout, result);
            addresslist.setAdapter(addressAdapter);
            continueToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (chooseaddr != 0) {
                        Intent in = new Intent(getBaseContext(), PaymentsActivity.class)
                                .putExtra("grandTotal", grandTotal)
                                .putExtra("addressId", addressid);
                        startActivity(in);
                    } else {
                        Toast.makeText(getBaseContext(), "Please Choose address first!", Toast.LENGTH_SHORT).show();
                    }
                }

                //productgridview.setExpanded(true);
                //textView.setText(result.toString());
            });

        }

    }
}