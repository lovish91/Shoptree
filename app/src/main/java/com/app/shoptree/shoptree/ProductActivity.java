package com.app.shoptree.shoptree;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Adapter.Product_List_Adapter;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.app.shoptree.shoptree.MainActivity.setBadgeCount;

public class ProductActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView productTit,productUnit,proOldprice,proNewprice,productdesc,productdisc;
    private Button addtocart;
    private ImageView product_img;
    LayerDrawable mCartMenuIcon;
    private MenuItem search,cart;
    SharedPrefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        toolbar =  (Toolbar) findViewById(R.id.toolbare);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        productTit = (TextView) findViewById(R.id.product_titl);
        productUnit = (TextView) findViewById(R.id.product_uni);
        proOldprice = (TextView)  findViewById(R.id.product_secondPrice);
        proNewprice = (TextView) findViewById(R.id.product_firstPrice);
        productdesc = (TextView) findViewById(R.id.pro_disc_text);
        productdisc = (TextView) findViewById(R.id.product_disc);
        addtocart = (Button) findViewById(R.id.addtocart);
        product_img = (ImageView) findViewById(R.id.product_imag);

        sharedPrefs = new SharedPrefs();

        String x = getIntent().getStringExtra("productId");

        new cHttpAsyncTask().execute("https://shopptree.com/api/Api_productdetails/"+x);
    }

    private class cHttpAsyncTask extends AsyncTask<String, Void, Product> {
        @Override
        protected Product doInBackground(String... urls) {
            Product product = new Product();
            ArrayList<Product> productArrayList = new ArrayList<>();
            productArrayList.clear();
            String abc = JsonParser.getData(urls[0]);

            try {
                JSONArray jsonArray = new JSONArray(abc);
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String pmid = jsonObject.getString("PMID");
                    String prodctid = jsonObject.getString("ProductID");
                    String name = jsonObject.getString("ProName");
                    String desc = jsonObject.getString("ProDescription");
                    String img = jsonObject.getString("ProPhotoMain");
                    String unit = jsonObject.getString("UnitType");

                    //String proNewPrice = jsonObject.getString("ProSellerPrice");
                    //String proOldPrice = jsonObject.getString("ProMrp");
                    Double oldprice = Double.valueOf(jsonObject.getString("ProMrp"));
                    Double newprice = Double.valueOf(jsonObject.getString("ProSellerPrice"));

                     product = new Product(prodctid,pmid,name,"0","0",oldprice,newprice,img,unit,desc);
                    //product_two.add(product);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return product;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(final Product result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            productTit.setText(result.getProductName());
            productUnit.setText(result.getUnit());
            proNewprice.setText(getResources().getString(R.string.Rs) + result.getProductNewPrice().toString());
            proOldprice.setText(getResources().getString(R.string.Rs) + result.getProductOldPrice().toString());
            productdesc.setText(result.getDescription());
            proOldprice.setPaintFlags(proOldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            double disc = Math.round(100 - ((double)result.getProductNewPrice())*100/(double)result.getProductOldPrice());
            if (disc==0.0){
                productdisc.setVisibility(View.INVISIBLE);
                proOldprice.setVisibility(View.INVISIBLE);
            }else {
                productdisc.setText(String.valueOf(disc) +" % OFF");
            }

            String a = result.getProductImg();

            Picasso.with(getApplicationContext())
                    .load("https://shopptree.com"+ a.substring(1))
                    .centerInside()
                    .fit()
                    .into(product_img);
            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AddtocartTask().execute("https://shopptree.com/api/Api_carts/?id="+result.getPmId()+"&qty=1&cartid=001");
                }
            });

            //Product_List_Adapter product_adapter = new Product_List_Adapter(getBaseContext(),result);
            //productgridview.setAdapter(product_adapter);
            //productgridview.setExpanded(true);
            //textView.setText(result.toString());
        }
    }
    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mCartMenuIcon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        //search =(MenuItem) menu.findItem(R.id.action_search);
        //searchView.setMenuItem(search);
        cart =(MenuItem) menu.findItem(R.id.action_cart);
        //BitmapDrawable iconBitmap = (BitmapDrawable) cart.getIcon();
        ArrayList<CartModel> cartModels = new ArrayList<>();
        MainActivity.countproductoncart = sharedPrefs.getCartCount(getBaseContext());


        setBadgeCount(this, mCartMenuIcon, String.valueOf(MainActivity.countproductoncart));
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(ProductActivity.this,CartActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    private class AddtocartTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            ArrayList<Product> productArrayList = new ArrayList<>();
            productArrayList.clear();
            String abc = JsonParser.sendData(urls[0]);



            return abc;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();
            //Product_List_Adapter product_adapter = new Product_List_Adapter(getBaseContext(),result);
            //productgridview.setAdapter(product_adapter);
            //productgridview.setExpanded(true);
            //textView.setText(result.toString());
        }
    }
}
