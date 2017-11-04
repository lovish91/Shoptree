package com.app.shoptree.shoptree;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Adapter.Product_List_Adapter;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.database.MyDb;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.app.shoptree.shoptree.MainActivity.setBadgeCount;

public class ProductActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TextView productTit,productUnit,proOldprice,proNewprice,productdesc,productdisc,productqty;
    private Button addtocart;
    private ImageView product_img;
    LayerDrawable mCartMenuIcon;
    private MenuItem search,cart;
    SharedPrefs sharedPrefs;
    private long countproductoncart = 0;
    private  MyDb myDb;
    private LinearLayout addminLayout;
    private ImageButton productAdd,productMns;

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

        addminLayout = (LinearLayout) findViewById (R.id.Productaddminus);
        productMns = (ImageButton) findViewById (R.id.Productmns);
        productAdd = (ImageButton) findViewById (R.id.Productpls);
        productqty = (TextView) findViewById (R.id.Productquty);

        sharedPrefs = new SharedPrefs();
        myDb= new MyDb(getBaseContext());
        myDb.open();
        countproductoncart = myDb.countproduct();
        myDb.close();
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
            myDb.open ();
            int i = myDb.checkAvailable (result.getPmId ());
            if (i == 1){
                addminLayout.setVisibility (View.VISIBLE);
                addtocart.setVisibility (View.GONE);
                int qty = myDb.getQuantity (result.getPmId ());
                productqty.setText (String.valueOf (qty));
            }
            myDb.close ();

            String a = result.getProductImg();

            Picasso.with(getApplicationContext())
                    .load("https://shopptree.com"+ a.substring(1))
                    .centerInside()
                    .fit()
                    .into(product_img);
            productAdd.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    try {
                    myDb.open ();
                    int count = myDb.getQuantity (result.getPmId ());
                    count = count + 1;

                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("CartID","001");
                    jsonParam.put("PMID",Integer.valueOf(result.getPmId ()));
                    jsonParam.put("CartQty",count);
                    jsonParam.put("CartAmount",result.getProductNewPrice() * Double.valueOf(count));
                        String status = new AddtocartTask(jsonParam).execute("https://shopptree.com/api/Api_Updatecart/").get();
                        if(status.equals ("Accepted")){
                            myDb.updateQuantity (result.getPmId (),count);
                            productqty.setText (String.valueOf (count));
                            myDb.close ();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    } catch (ExecutionException e) {
                        e.printStackTrace ();
                    }
                }
            });
            productMns.setOnClickListener (new View.OnClickListener () {
                @Override
                public void onClick (View view) {
                    try {
                        myDb.open ();
                        int count = myDb.getQuantity (result.getPmId ());
                        if (count == 1) {
                            String status = new delItemCart ().execute("https://shopptree.com/api/Api_UpdateCart?CartID=001&PMID="+result.getPmId ()).get();
                            if (status.equals ("202")){
                                myDb.deleteproduct(result.getPmId());
                                addtocart.setVisibility (View.VISIBLE);
                                addminLayout.setVisibility (View.GONE);
                                myDb.close ();

                            }
                        } else {

                            count = count - 1;
                            JSONObject jsonParam = new JSONObject ();
                            jsonParam.put ("CartID", "001");
                            jsonParam.put ("PMID", Integer.valueOf (result.getPmId ()));
                            jsonParam.put ("CartQty", count);
                            jsonParam.put ("CartAmount", result.getProductNewPrice () * Double.valueOf (count));
                            String status = new AddtocartTask (jsonParam).execute ("https://shopptree.com/api/Api_Updatecart/").get ();
                            if (status.equals ("Accepted")) {
                                myDb.updateQuantity (result.getPmId (), count);
                                productqty.setText (String.valueOf (count));
                                myDb.close ();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace ();
                    } catch (InterruptedException e) {
                        e.printStackTrace ();
                    } catch (ExecutionException e) {
                        e.printStackTrace ();
                    }
                }
            });

            addtocart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    myDb.open ();
                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CartID","001");
                        jsonObject.put("PMID",result.getPmId());
                        jsonObject.put("CartQty","1");
                        String status =  new AddtocartTask(jsonObject).execute("https://shopptree.com/api/Api_carts/").get();
                    if(status .equals ("Created")){
                        Toast.makeText(getBaseContext(), result.getProductName() + "is added to cart", Toast.LENGTH_SHORT).show();
                        myDb.insertData(result.getProductId (),result.getPmId (),result.getProductName (),result.getProductImg (),String.valueOf(result.getProductNewPrice ()),1);
                        productqty.setText ("1");
                        addtocart.setVisibility (View.GONE);
                        addminLayout.setVisibility (View.VISIBLE);
                        myDb.close ();
                        setBadgeCount(ProductActivity.this, mCartMenuIcon, String.valueOf(countproductoncart +1));

                    }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace ();
                    }
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


        setBadgeCount(this, mCartMenuIcon, String.valueOf(countproductoncart));
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
        private JSONObject jsonObject;
        AddtocartTask(JSONObject jsonObject){
            this.jsonObject = jsonObject;
        }
        @Override
        protected String doInBackground(String... urls) {
            ArrayList<Product> productArrayList = new ArrayList<>();
            productArrayList.clear();
            String abc = JsonParser.postData(urls[0],jsonObject);


            return abc;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_SHORT).show();

        }
    }
    private class delItemCart extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            Log.i("TAG", "onPreExecute");
        }
        @Override
        protected String doInBackground(String... urls) {

            String abc = JsonParser.delData(urls[0]);

            return abc;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  abc) {
            //String a = abc.toString();
            Toast.makeText(getBaseContext (), abc, Toast.LENGTH_SHORT).show();

        }
    }
}
