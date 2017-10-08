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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.app.shoptree.shoptree.Adapter.ProductList_RecycleAdapter;
import com.app.shoptree.shoptree.Adapter.Product_List_Adapter;
import com.app.shoptree.shoptree.Adapter.SubCat_Adapter;
import com.app.shoptree.shoptree.Utilities.ExpandedGridView;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.database.MyDb;
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
    private String catId;
    private RecyclerView product_grid;
    private static long countproductoncart=0;
    private ProductList_RecycleAdapter productList_recycleAdapter;


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
        catId = getIntent().getStringExtra("categoryId");
        String catName = getIntent().getStringExtra("categoryname");
        toolbar.setTitle(catName);
        getSupportActionBar().setTitle(catName);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimaryDark));
        sharedPrefs = new SharedPrefs();

        product_grid = (RecyclerView) findViewById(R.id.product_grid2);
        int numberOfColumns = 3;
        GridLayoutManager manager = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        product_grid.setLayoutManager(manager);
        product_grid.getLayoutManager().setAutoMeasureEnabled(true);
        product_grid.setNestedScrollingEnabled(false);
        product_grid.setHasFixedSize(false);

        MyDb myDb = new MyDb(getBaseContext());
        myDb.open();
        countproductoncart = myDb.countproduct();
        myDb.close();

        //textView1.setText(catId);
        gridView = (ExpandedGridView) findViewById(R.id.sub_cat_grid);
        productgridview = (ExpandedGridView) findViewById(R.id.product_grid);
        // check if you are connected or not
        if(isConnected()){
            //textViewconnection.setBackgroundColor(0xFF00CC00);
            //textViewconnection.setText("You are conncted");
            new aHttpAsyncTask().execute("https://shopptree.com/api/Api_Categories/"+catId);
            new bHttpAsyncTask().execute("https://shopptree.com/api/Api_Products/"+catId);

        }
        else{
            textViewconnection.setText("You are NOT conncted");
        }
        //APIClient();
        //APIService apiService = APIClient().get
        // call AsynTask to perform network operation on separate thread

        //new AddtocartTask().execute("http://shopptree.com/api/Api_Carts/?id=10002&qty=33&cartid=1111");
    }


    protected void onStart() {
        super.onStart();

        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        MyDb myDb = new MyDb(getBaseContext());
        myDb.open();
        countproductoncart = myDb.countproduct();
        myDb.close();
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


        setBadgeCount(this, mCartMenuIcon, String.valueOf(countproductoncart));
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
            //product_adapter = new Product_List_Adapter2(getBaseContext(),result);
            //productgridview.setAdapter(product_adapter);
            //productgridview.setExpanded(true);
            //textView.setText(result.toString());
            productList_recycleAdapter = new ProductList_RecycleAdapter(getBaseContext(), result);
            product_grid.setAdapter(productList_recycleAdapter);


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





}
