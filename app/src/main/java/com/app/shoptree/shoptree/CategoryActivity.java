package com.app.shoptree.shoptree;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.app.shoptree.shoptree.Adapter.Cat_Prodct_Recyclerview;
import com.app.shoptree.shoptree.Adapter.Category_grid_Recyclerview;
import com.app.shoptree.shoptree.Adapter.ProductList_RecycleAdapter;
import com.app.shoptree.shoptree.Adapter.Product_List_Adapter;
import com.app.shoptree.shoptree.Adapter.SubCat_Adapter;
import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.ExpandedGridView;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.database.MyDb;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.CategoryModel;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.app.shoptree.shoptree.model.Product;
import com.app.shoptree.shoptree.model.ProductModel;
import com.app.shoptree.shoptree.model.TestModel;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.shoptree.shoptree.MainActivity.setBadgeCount;

public class CategoryActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextView textView;
    private TextView textViewconnection;
    private ExpandedGridView gridView;
    LayerDrawable mCartMenuIcon;
    private MenuItem search,cart;
    private SharedPrefs sharedPrefs;
    private String catId;
    private RecyclerView product_grid;
    public static long countproductoncart=0;
    private ProductList_RecycleAdapter productList_recycleAdapter;
    private SubCat_Adapter categoryAdapter;
    private MyDb myDb;
    private List<ProductModel> products;
    private List<CategoryModel> categoryModels;
    public static CategoryActivity instance;
    private LinearLayoutManager manager;
    private ApiInterface apiInterface;
    private int pageNo = 1;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private NestedScrollView scrollView;
    private List<Object> objectList;
    private Cat_Prodct_Recyclerview prodctRecyclerview;
    private ProductList_RecycleAdapter adapter2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        instance = this;
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
        apiInterface = RetroFit.getClient ().create (ApiInterface.class);
        scrollView = (NestedScrollView) findViewById (R.id.scroll);
        objectList = new ArrayList<> ();
        categoryModels = new ArrayList<> ();
        products = new ArrayList<> ();

        myDb = new MyDb(getBaseContext());
        myDb.open();
        countproductoncart = myDb.countproduct();
        myDb.close();

        //textView1.setText(catId);
        //gridView = (ExpandedGridView) findViewById(R.id.sub_cat_grid);
        //categoryModels = new ArrayList<> ();
        //categoryAdapter= new SubCat_Adapter(getBaseContext(),categoryModels);
        //gridView.setAdapter(categoryAdapter);

        // check if you are connected or not
        if(isConnected()){
            //textViewconnection.setBackgroundColor(0xFF00CC00);
            //textViewconnection.setText("You are conncted");
            new aHttpAsyncTask().execute("https://shopptree.com/api/Api_Categories/"+catId);
            //new bHttpAsyncTask().execute("https://shopptree.com/api/Api_Products/"+catId);

           initProductRecyleview ();

        }
        else{
            //textViewconnection.setText("You are NOT conncted");
        }


    }
    private void initProductRecyleview(){
        product_grid = (RecyclerView) findViewById(R.id.product_grid2);
        manager = new LinearLayoutManager (this);
        product_grid.setLayoutManager(manager);
        product_grid.getLayoutManager();
        product_grid.setHasFixedSize(false);
        products = new ArrayList<> ();

        Category_grid_Recyclerview adapter1 = new Category_grid_Recyclerview (this,categoryModels);
        adapter2 = new ProductList_RecycleAdapter (this,products);

        prodctRecyclerview =  new Cat_Prodct_Recyclerview (getBaseContext (),objectList,adapter1,adapter2);

        product_grid.setAdapter(prodctRecyclerview);
        product_grid.addOnScrollListener (productgridScroll);
        loadProducts (catId, pageNo);//load query

    }
    private RecyclerView.OnScrollListener productgridScroll = new RecyclerView.OnScrollListener () {
        @Override
        public void onScrollStateChanged (RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged (recyclerView, newState);

        }
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            //Log.d("...", "Last Item Wow !");

            if(dy > 0) //check for scroll down
            {
                //Log.d("...", "Last scroling !");

                int visibleItemCount = manager.getChildCount();
                int totalItemCount = manager.getItemCount();
                int pastVisiblesItems = manager.findFirstVisibleItemPosition();

                if (!isLoading)
                {
                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)
                    {
                        isLoading = true;
                        Log.d("...", "Last Item Wow !"+ visibleItemCount+" " + pastVisiblesItems+" " +" "+ totalItemCount);
                        //Do pagination.. i.e. fetch new data
                        loadProducts (catId, ++pageNo);
                    }
                }
            }

        }
    };

    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked");
    }
    @Override
    protected void onResume() {
        super.onResume();
        myDb.open();
        countproductoncart = myDb.countproduct();
        myDb.close();
        prodctRecyclerview.notifyDataSetChanged ();
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

    public void update(){
        myDb.open();
        countproductoncart = myDb.countproduct();
        myDb.close();
        adapter2.notifyDataSetChanged ();
        invalidateOptionsMenu();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mCartMenuIcon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        //search =(MenuItem) menu.findItem(R.id.action_search);
        //searchView.setMenuItem(search);
        cart = (MenuItem) menu.findItem(R.id.action_cart);

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

    public void refre(){
        loadProducts (catId, ++pageNo);

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    private class aHttpAsyncTask extends AsyncTask<String, Void, List<CategoryModel>> {
        @Override
        protected List<CategoryModel> doInBackground(String... urls) {
            List<CategoryModel> categoryModelArrayList = new ArrayList<>();
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
        protected void onPostExecute(List<CategoryModel> result) {
            if (result.size ()>0) {
                loadProducts (catId, pageNo);//load query

                objectList.add (result.get (0));
                categoryModels.addAll (result);
                prodctRecyclerview.notifyItemInserted (result.size () - 1);
            }
        }
    }

    private void loadProducts(String id,int pageNo){
        final ProgressDialog progressDialog = new ProgressDialog (CategoryActivity.this);
        progressDialog.show ();
        apiInterface.getProducts (id,pageNo).enqueue (new Callback<ArrayList<ProductModel>> () {
            @Override
            public void onResponse (Call<ArrayList<ProductModel>> call, Response<ArrayList<ProductModel>> response) {
                if(response.isSuccessful ()){
                    ArrayList<ProductModel> productModels = response.body ();
                    if(productModels.size ()>0){
                        Log.d("page size", String.valueOf (response.code ()));
                        objectList.add (productModels.get (0));
                        products.addAll (productModels);

                        adapter2.notifyItemInserted (products.size ()-1);

                        //prodctRecyclerview.notifyDataSetChanged ();
                        isLoading = false;
                        progressDialog.dismiss ();
                    }else {
                        isLastPage = true;
                        progressDialog.dismiss ();
                    }
                    Log.d("response code", String.valueOf (response.body ()));


                }

                if (String.valueOf (response.body ()).equals ("404")){
                    Log.d("response code", String.valueOf (response.code ()));

                }

            }

            @Override
            public void onFailure (Call<ArrayList<ProductModel>> call, Throwable t) {

            }
        });
    }



}
