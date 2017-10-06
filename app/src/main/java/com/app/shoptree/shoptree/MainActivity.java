package com.app.shoptree.shoptree;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.speech.RecognizerIntent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.app.shoptree.shoptree.Adapter.CategoryRecycle;
import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.Helper;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener, BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {


    private Toolbar toolbarMain;
    private MaterialSearchView searchView;
    private ListView gridView;
    private Button button;
    private TextView textView;
    private MenuItem search,cart;
    LinearLayout dynamicContent,bottonNavBar;
    LayerDrawable mCartMenuIcon;
    public static long countproductoncart=0;
    SharedPrefs sharedPrefs;
    private RecyclerView recyclerView;
    private CategoryRecycle categoryRecycle;
    private SliderLayout mDemoSlider;
    private ApiInterface apiInterface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        View wizard = getLayoutInflater().inflate(R.layout.activity_main, null);
        dynamicContent.addView(wizard);

        sharedPrefs = new SharedPrefs();
        //get the reference of RadioGroup.
        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        HashMap<String,String> url_maps = new HashMap<String, String>();
        url_maps.put("Market", "http://greatist.com/sites/default/files/Healthy-Grocery-Shopping.jpg");
        url_maps.put("Online", "http://goodearthmarket.coop/wp-content/themes/goodearth/images/grocery.jpg");
        url_maps.put("Frocery", "http://www.theloop.ca/wp-content/uploads/2015/02/grocery-shopping.jpg");
        url_maps.put("App", "http://cdn.modernfarmer.com/wp-content/uploads/2014/11/shoppinghero.jpg");


        for(String name : url_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(url_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(MainActivity.this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(MainActivity.this);

        countproductoncart =sharedPrefs.getCartCount(getBaseContext());
        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.matching);

        // Change the corresponding icon and text color on nav button click.

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_home_clicked, 0,0);
        rb.getBackground().setColorFilter(0xFFBBAA00, PorterDuff.Mode.MULTIPLY);
        rb.setTextColor(getResources().getColor(R.color.colorPrimaryDark));


        toolbarMain =  (Toolbar) findViewById(R.id.tool);
        setSupportActionBar(toolbarMain);
        getSupportActionBar().setTitle("");
        toolbarMain.setTitle("");
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        recyclerView = (RecyclerView) findViewById(R.id.categoryRecyc);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        gridView = (ListView) findViewById(R.id.cat_grid);
        Helper.setListViewHeightBasedOnChildren(gridView);
        textView =  (TextView) findViewById(R.id.samp);
        //gridView.setAdapter(new Category_Adapter(MainActivity.this,);
        searchView = (MaterialSearchView) findViewById(R.id.search_views);
        searchView.setVoiceSearch(false);
        searchView.setCursorDrawable(R.drawable.cursor);

       // button = (Button) findViewById(R.id.nextButton);
        if (isConnected()){
            loadCategory();
            //new HttpAsyncTask().execute("https://shopptree.com/api/Api_Categories");
        }

        /*button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MainActivity.this, MyAddressActivity.class);
                startActivity(intent);
            }
        });*/
        //searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //Do some magic
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });



    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }
    protected void loadCategory(){
        apiInterface = RetroFit.getClient().create(ApiInterface.class);

        apiInterface.getCategories().enqueue(new Callback<List<CategoryModel>>() {
            @Override
            public void onResponse(Call<List<CategoryModel>> call, Response<List<CategoryModel>> response) {
                //Log.d("match", String.valueOf(response.code()));
                Log.d("match", response.body().toString());
                categoryRecycle = new CategoryRecycle(getBaseContext(),response.body());
                recyclerView.setAdapter(categoryRecycle);
                for (CategoryModel categoryModel : response.body()){
                    Log.d("match", categoryModel.getCategoryId());

                }
            }

            @Override
            public void onFailure(Call<List<CategoryModel>> call, Throwable t) {

            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    @Override
    protected void onResume() {
        super.onResume();
        countproductoncart = sharedPrefs.getCartCount(getBaseContext());
        invalidateOptionsMenu();
        //setBadgeCount(this, mCartMenuIcon, String.valueOf(countproductoncart));
        Log.d("lifecycle","onResume invoked");
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, ArrayList<CategoryModel>> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute(){
            progressDialog = new ProgressDialog(MainActivity.this);
            progressDialog.setMessage("Loading...");
            progressDialog.show();
        }
        @Override
        protected ArrayList<CategoryModel> doInBackground(String... urls) {
            ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
            categoryModelArrayList.clear();
            String abc = JsonParser.getData(urls[0]);
            //Log.i("matching", abc.toString());

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

           // Category_Adapter categoryAdapter = new Category_Adapter(getBaseContext(),result);
            //gridView.setAdapter(categoryAdapter);
            categoryRecycle = new CategoryRecycle(getBaseContext(),result);
            recyclerView.setAdapter(categoryRecycle);
            for (CategoryModel categoryModel :result ){
                //textView.setText(categoryModel.getCatImage());
            }
            progressDialog.dismiss();

        }
    }


    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        mCartMenuIcon = (LayerDrawable) menu.findItem(R.id.action_cart).getIcon();
        search =(MenuItem) menu.findItem(R.id.action_search);
        searchView.setMenuItem(search);
        cart =(MenuItem) menu.findItem(R.id.action_cart);
        //BitmapDrawable iconBitmap = (BitmapDrawable) cart.getIcon();
        ArrayList<CartModel> cartModels = new ArrayList<>();
        //cartModels = sharedPrefs.getCart(getBaseContext());


        setBadgeCount(this, mCartMenuIcon, String.valueOf(countproductoncart));
        cart.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(MainActivity.this,CartActivity.class);
                startActivity(intent);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
    public static void setBadgeCount(Context context, LayerDrawable icon, String count) {

        BadgeDrawable badge;

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }

        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);
    }
    public void updatecart(){
        View view;
        countproductoncart=sharedPrefs.getCartCount(getBaseContext());
        setBadgeCount(this, mCartMenuIcon, String.valueOf(countproductoncart));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
