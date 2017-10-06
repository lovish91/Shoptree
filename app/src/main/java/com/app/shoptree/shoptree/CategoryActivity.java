package com.app.shoptree.shoptree;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.shoptree.shoptree.Adapter.Category_Adapter;
import com.app.shoptree.shoptree.Adapter.Product_List_Adapter;
import com.app.shoptree.shoptree.Adapter.SubCat_Adapter;
import com.app.shoptree.shoptree.Utilities.ExpandedGridView;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.CategoryModel;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.app.shoptree.shoptree.model.Product;
import com.app.shoptree.shoptree.model.TestModel;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static com.app.shoptree.shoptree.MainActivity.setBadgeCount;

public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private TextView textViewconnection;
    private ExpandedGridView gridView;
    private ExpandedGridView productgridview;
    LayerDrawable mCartMenuIcon;
    private MenuItem search,cart;
    SharedPrefs sharedPrefs;
    private Product_List_Adapter2 product_adapter;
    private String catId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        toolbar = (Toolbar) findViewById(R.id.toolbare);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        //textView = (TextView) findViewById(R.id.jsontext);
        //textViewconnection = (TextView) findViewById(R.id.apiconnected);
        //TextView textView1 = (TextView) findViewById(R.id.putextra);
        catId = getIntent().getStringExtra("categoryId");
        String catName = getIntent().getStringExtra("categoryname");
        toolbar.setTitle(catName);
        getSupportActionBar().setTitle(catName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        sharedPrefs = new SharedPrefs();

        //textView1.setText(catId);
        gridView = (ExpandedGridView) findViewById(R.id.sub_cat_grid);
        productgridview = (ExpandedGridView) findViewById(R.id.product_grid);
        // check if you are connected or not
        if(isConnected()){
            //textViewconnection.setBackgroundColor(0xFF00CC00);
            //textViewconnection.setText("You are conncted");
        }
        else{
            textViewconnection.setText("You are NOT conncted");
        }
        //APIClient();
        //APIService apiService = APIClient().get
        // call AsynTask to perform network operation on separate thread
        new aHttpAsyncTask().execute("https://shopptree.com/api/Api_Categories/"+catId);
        //new AddtocartTask().execute("http://shopptree.com/api/Api_Carts/?id=10002&qty=33&cartid=1111");
    }


    protected void onStart() {
        super.onStart();
        new bHttpAsyncTask().execute("https://shopptree.com/api/Api_Products/"+catId);

        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        MainActivity.countproductoncart = sharedPrefs.getCartCount(getBaseContext());
        invalidateOptionsMenu();
        Log.d("lifecycle","onResume invoked");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked");
    }



    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mCartMenuIcon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        //search =(MenuItem) menu.findItem(R.id.action_search);
        //searchView.setMenuItem(search);
        cart =(MenuItem) menu.findItem(R.id.action_cart);
        ArrayList<CartModel> cartModels = new ArrayList<>();
        MainActivity.countproductoncart = sharedPrefs.getCartCount(getBaseContext());


        setBadgeCount(this, mCartMenuIcon, String.valueOf(MainActivity.countproductoncart));
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(CategoryActivity.this,CartActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }



    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class aHttpAsyncTask extends AsyncTask<String, Void, ArrayList<CategoryModel>> {
        @Override
        protected ArrayList<CategoryModel> doInBackground(String... urls) {
            ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
            categoryModelArrayList.clear();
            String abc = JsonParser.getData(urls[0]);

            try {
                JSONArray jsonArray = new JSONArray(abc);
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("CategoryID");
                    String name = jsonObject.getString("CatName");
                    String desc = jsonObject.getString("CatDescription");
                    String img = jsonObject.getString("CatImage");

                    CategoryModel categoryModel = new CategoryModel(id,name,desc,img);
                    categoryModelArrayList.add(categoryModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return categoryModelArrayList;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<CategoryModel> result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            SubCat_Adapter categoryAdapter = new SubCat_Adapter(getBaseContext(),result);
            gridView.setAdapter(categoryAdapter);
            gridView.setExpanded(true);
            //textView.setText(result.toString());
        }
    }

    private class bHttpAsyncTask extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected ArrayList<Product> doInBackground(String... urls) {
            ArrayList<Product> productArrayList = new ArrayList<>();
            productArrayList.clear();
            String abc = JsonParser.getData(urls[0]);

            try {
                JSONArray jsonArray = new JSONArray(abc);
                for (int i =0;i<jsonArray.length();i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String pmid = jsonObject.getString("PMID");
                    //String prodctid = jsonObject.getString("ProductID");
                    String name = jsonObject.getString("ProName");
                    String desc = jsonObject.getString("ProDescription");
                    String img = jsonObject.getString("ProPhotoMain");
                    String unit = jsonObject.getString("UnitType");
                    Double oldprice = Double.valueOf(jsonObject.getString("ProMrp"));
                    Double newprice = Double.valueOf(jsonObject.getString("ProSellerPrice"));

                    Product product = new Product("1",pmid,name,"0","0",oldprice,newprice,img,unit,desc);
                    productArrayList.add(product);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return productArrayList;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<Product> result) {
            //Toast.makeText(getBaseContext(), "Received!", Toast.LENGTH_LONG).show();
            product_adapter = new Product_List_Adapter2(getBaseContext(),result);
            productgridview.setAdapter(product_adapter);
            productgridview.setExpanded(true);
            //textView.setText(result.toString());
        }
    }

    /*private class AddtocartTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            ArrayList<Product> productArrayList = new ArrayList<>();
            productArrayList.clear();
            String abc = JsonParser.getData(urls[0]);



            return abc;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();

        }
    }*/

    public class Product_List_Adapter2 extends BaseAdapter {

        private Context mcontext;
        private ArrayList<Product> products;
        private SharedPrefs sharedPrefs = new SharedPrefs();
        private ArrayList<TestModel> cartModels = new ArrayList<>();
        private  LayoutInflater inflater = null;
        private int count ;


        public Product_List_Adapter2(Context context, ArrayList<Product> products){
            this.products = products;
            this.mcontext = context;
            //this.imgId = ImageId;
            inflater = ( LayoutInflater )context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        public boolean checkAvailability(String pmid){


            for(TestModel a : cartModels){
                if(pmid.equals(a.getPMID().toString())){
                    Log.i("InputStream", String.valueOf(cartModels.size()));
                    return true;
                }

            }
            return false;
        }
        @Override
        public int getCount() {
            return products.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        public class Holder
        {
            TextView prodctName;
            ImageView prodctImg;
            TextView productunit;
            TextView productdisc;
            TextView productnewprice;
            TextView productoldprice;
            TextView Product_quantity;
            ImageButton add,minus;
            Button addToCart;
            LinearLayout Product_add_minus;
            RelativeLayout Productmain;
        }
        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            cartModels = sharedPrefs.getCart(mcontext);
            final Holder holder=new Holder();
            View rowView;
            final Product product = products.get(position);
            rowView = inflater.inflate(R.layout.product_item, null);
            holder.prodctName = (TextView) rowView.findViewById(R.id.product_tit);
            holder.prodctImg = (ImageView) rowView.findViewById(R.id.product_img);
            holder.productunit = (TextView) rowView.findViewById(R.id.product_unit);
            holder.productdisc = (TextView) rowView.findViewById(R.id.product_disc);
            holder.productnewprice = (TextView) rowView.findViewById(R.id.product_newPrc);
            holder.productoldprice = (TextView) rowView.findViewById(R.id.product_oldPrc);
            holder.productoldprice.setPaintFlags(holder.productoldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

            holder.addToCart = (Button) rowView.findViewById(R.id.add_product);
            holder.Productmain = (RelativeLayout) rowView.findViewById(R.id.mainProductView);
            holder.minus = (ImageButton) rowView.findViewById(R.id.Product_mns);
            holder.add = (ImageButton) rowView.findViewById(R.id.Product_pls);
            holder.Product_add_minus = (LinearLayout) rowView.findViewById(R.id.Product_add_minus);
            holder.Product_quantity = (TextView) rowView.findViewById(R.id.Product_quty);

            for(TestModel a : cartModels){
                if(product.getPmId().equals(a.getPMID())){
                    Log.i("InputStream", String.valueOf(cartModels.size()));
                    holder.Product_add_minus.setVisibility(View.VISIBLE);
                    holder.addToCart.setVisibility(View.INVISIBLE);
                    holder.Productmain.setBackgroundResource(R.drawable.productborder);
                    holder.Product_quantity.setText(String.valueOf(a.getCartQty()));
                    count = (a.getCartQty());
                }
            }
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        count = count + 1;
                        Log.i("button",String.valueOf(count));
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("CartID","001");
                        jsonParam.put("PMID",Integer.valueOf(product.getPmId()));
                        jsonParam.put("CartQty",count);
                        jsonParam.put("CartAmount",product.getProductNewPrice() * Double.valueOf(count));
                        new Addtocart(jsonParam).execute("https://shopptree.com/api/Api_Updatecart");
                        product.setProductQuantity(String.valueOf(count));
                        holder.Product_quantity.setText(String.valueOf(count));
                        sharedPrefs.SaveCart(mcontext,cartModels);
                        //notifyDataSetChanged();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
            holder.minus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (count > 1){
                            count = count-1;

                            JSONObject jsonParam = new JSONObject();
                            jsonParam.put("CartID","001");
                            jsonParam.put("PMID",Integer.valueOf(product.getPmId()));
                            jsonParam.put("CartQty",count);
                            jsonParam.put("CartAmount",product.getProductNewPrice() * Double.valueOf(count));
                            new Addtocart(jsonParam).execute("https://shopptree.com/api/Api_Updatecart");
                            product.setProductQuantity(String.valueOf(count));
                            holder.Product_quantity.setText(String.valueOf(count));
                            sharedPrefs.SaveCart(mcontext, cartModels);
                            //notifyDataSetChanged();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    //if(count ==0 || count ==1){
                    //  cartModels.remove(position);
                    //sharedPrefs.SaveCart(mcontext, cartModels);
                    //notifyDataSetChanged();
                    //}
                }
            });


            holder.prodctName.setText(product.getProductName());
            holder.productunit.setText(product.getUnit());
            double disc = Math.round(100 - ((double)product.getProductNewPrice())*100/(double)product.getProductOldPrice());
            DecimalFormat df = new DecimalFormat("0.#");
            if (disc==0.0){
                holder.productdisc.setVisibility(View.INVISIBLE);
                holder.productoldprice.setVisibility(View.INVISIBLE);
            }else {
                holder.productdisc.setText(String.valueOf(df.format(disc)) +" % OFF");
            }

            holder.productdisc.setText(String.valueOf(df.format(disc))+" OFF");
            holder.productoldprice.setText(mcontext.getResources().getString(R.string.Rs) + (df.format(product.getProductOldPrice())).toString());
            holder.productnewprice.setText(mcontext.getResources().getString(R.string.Rs) + (df.format(product.getProductNewPrice()).toString()));
            String a = product.getProductImg();

            Picasso.with(mcontext)
                    .load("https://shopptree.com"+a.substring(1))
                    //.placeholder(R.drawable.fruit)
                    //.error(R.drawable.fruit)
                    .into(holder.prodctImg);

            holder.addToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("CartID","001");
                        jsonObject.put("PMID",product.getPmId());
                        jsonObject.put("CartQty","1");


                        String abc = new Addtocart(jsonObject).execute("https://shopptree.com/api/Api_carts/").get();
                        if (abc.equals("Created")){
                            TestModel cartModel = new TestModel(0,"","","",product.getPmId(),"","",product.getProductId(),"","","","",1,0,0,"0",0,"0",0,0,0,0,0);
                            sharedPrefs.addCartItem(mcontext,cartModel);
                            holder.Product_add_minus.setVisibility(View.VISIBLE);
                            holder.addToCart.setVisibility(View.INVISIBLE);
                            MainActivity.countproductoncart =MainActivity.countproductoncart+1;
                            setBadgeCount(getBaseContext(), mCartMenuIcon, String.valueOf(MainActivity.countproductoncart));

                            notifyDataSetChanged();
                        }
                        Log.i("InputS", abc.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            });
            rowView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), ProductActivity.class).putExtra("productId",product.getPmId());
                    v.getContext().startActivity(intent);
                    // TODO Auto-generated method stub
                }
            });

            return rowView;
        }
        private class Addtocart extends AsyncTask<String, Void, String> {
            private JSONObject jsonObject;
            Addtocart(JSONObject jsonObject){
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
            protected void onPostExecute(String  abc) {
                Toast.makeText(mcontext, abc, Toast.LENGTH_SHORT).show();
                //Product_List_Adapter product_adapter = new Product_List_Adapter(getBaseContext(),result);
                //productgridview.setAdapter(product_adapter);
                //productgridview.setExpanded(true);
                //textView.setText(result.toString());

            }

        }

        public void refresh(){
            if (products.size()>0){
                notifyDataSetChanged();
            }

        }
    }


}
