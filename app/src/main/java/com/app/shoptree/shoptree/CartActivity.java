package com.app.shoptree.shoptree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Adapter.Cart;
import com.app.shoptree.shoptree.Adapter.Cart_Adapter;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

import static com.app.shoptree.shoptree.MainActivity.setBadgeCount;

public class CartActivity extends AppCompatActivity {

    private ListView cartlist;
    private TextView emptycart,cartgrandTotol;
    private RecyclerView cart;
    private Cart cartadap;
    private Button checkout;
    private Toolbar cartToolbar;
    private RelativeLayout cartfooter;
    private DecimalFormat df = new DecimalFormat("0.#");
    private double grandtot = 0;
    private static SharedPrefs sharedPrefs = new SharedPrefs();
    private MenuItem search,carts;
    LayerDrawable mCartMenuIcon;
    UserInfo userInfo = new UserInfo();
    public static String USERID ="Userid";
    private String userid ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        cartlist = (ListView) findViewById(R.id.cartlist);
        cartlist.setDividerHeight(0);
        //Adding view to mlist header and footer
        //footer = (TextView) findViewById(R.id.footer);
        cartToolbar = (Toolbar) findViewById(R.id.cartToolbar);
        setSupportActionBar(cartToolbar);
        cartToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        cartToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        cartfooter = (RelativeLayout) findViewById(R.id.cartFooter);
        cartfooter.setVisibility(View.INVISIBLE);

        cart = (RecyclerView) findViewById(R.id.cart);
        cart.setHasFixedSize(false);
        checkout = (Button) findViewById(R.id.checkout);
        cartgrandTotol = (TextView) findViewById(R.id.grandTotalFooter);
        emptycart = (TextView) findViewById(R.id.emptyCart);
        //userInfo = sharedPrefs.getUserInfo(getBaseContext());

        SharedPreferences prfs = getSharedPreferences(USERID, Context.MODE_PRIVATE);
        userid = prfs.getString("cart_id", "");
        Log.d("fai", userid.toString()+"nmvm");

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cart.setLayoutManager(layoutManager);

        cart.setItemAnimator(new DefaultItemAnimator());


        new HttpAsyncTask().execute("https://shopptree.com/api/Api_Carts?Cartid=001");
    }


    private void addHeaderFooterView(ArrayList<CartModel> cartModels) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            double price = 0;
        int items = cartModels.size();
        for(CartModel cart :cartModels){
            price += Double.valueOf(cart.getCartQuantity())*cart.getCartRate();
        }

        //Header View
       // ViewGroup headerView = (ViewGroup) inflater.inflate(R.layout.product_item, null, false);
       // TextView headerTitle = (TextView) headerView.findViewById(R.id.product_tit);
       // headerTitle.setText(getResources().getString(R.string.app_name));//set the text to Header View
       // cartlist.addHeaderView(headerView);//Add view to list view as header view

        ViewGroup footerView = (ViewGroup) inflater.inflate(R.layout.grand_total, null, false);
        TextView priceTotal = (TextView) footerView.findViewById(R.id.Price);
        TextView priceText = (TextView) footerView.findViewById(R.id.Price_text);
        TextView delivery = (TextView) footerView.findViewById(R.id.delivery_charge);
        TextView grandtotal = (TextView) footerView.findViewById(R.id.TotalPayableAmount);
        priceText.setText("Price ("+items+")items");
        priceTotal.setText(getResources().getString(R.string.Rs) +" "+ df.format(price));
        if (price<500){
            delivery.setText(getResources().getString(R.string.Rs)+"49");
            grandtot = price + 49;
        }else {
            grandtot = price + 0;
            delivery.setText(getResources().getString(R.string.Rs)+"0");
        }
        grandtotal.setText(getResources().getString(R.string.Rs) +" "+ String.valueOf(df.format(grandtot)));
        cartgrandTotol.setText(getResources().getString(R.string.Rs) +" "+ String.valueOf(df.format(grandtot)));

        //set the text to Footer View
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userid == " "){
                   Intent in = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(in);
                }else {
                    Intent in = new Intent(getBaseContext(), MyAddressActivity.class)
                            //.putExtra("userid",userInfo.getUserId())
                            .putExtra("grandTotal",String.valueOf(grandtot));
                    startActivity(in);
                }

            }
        });
        cartlist.addFooterView(footerView);//Add view to list view as footer view

    }


    private class HttpAsyncTask extends AsyncTask<String, Void, ArrayList<CartModel>> {
        @Override
        protected ArrayList<CartModel> doInBackground(String... urls) {
            ArrayList<CartModel> categoryModelArrayList = new ArrayList<>();
            categoryModelArrayList.clear();
            String abc = JsonParser.getData(urls[0]);

            try {
                JSONArray jsonArray = new JSONArray(abc);
                if (jsonArray.length()>0){
                for (int i =0;i<jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String pmid = jsonObject.getString("PMID");
                    String name = jsonObject.getString("ProName");
                    String desc = jsonObject.getString("ProDescription");
                    String img = jsonObject.getString("ProPhotoMain");
                    String quantity = jsonObject.getString("CartQty");
                    Double cartRate = Double.valueOf(jsonObject.getString("CartRate"));
                    Double cartAmount = Double.valueOf(jsonObject.getString("CartAmount"));
                    //String unit = jsonObject.getString("")

                    CartModel categoryModel = new CartModel("1", pmid, name, "0", quantity, cartRate, cartAmount, img, "0");
                    categoryModelArrayList.add(categoryModel);
                }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return categoryModelArrayList;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<CartModel> result) {
            Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            //sharedPrefs.SaveCart(getBaseContext(),result);

            Cart_Adapter categoryAdapter = new Cart_Adapter(getBaseContext(),result);
            cartlist.setAdapter(categoryAdapter);

            if (result.size()>0){
                emptycart.setVisibility(View.INVISIBLE);
                cartfooter.setVisibility(View.VISIBLE);
                addHeaderFooterView(result);
            }
            for (CartModel categoryModel :result ){
                //textView.setText(categoryModel.getCatImage());
            }

        }
    }
}

