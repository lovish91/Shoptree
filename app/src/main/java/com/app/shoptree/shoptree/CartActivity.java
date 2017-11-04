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
import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.UserInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.app.shoptree.shoptree.MainActivity.setBadgeCount;

public class CartActivity extends AppCompatActivity {

    private ListView cartlist;
    private TextView emptycart,cartgrandTotol,priceTotal,priceText,delivery;
    private RecyclerView cart;
    private Button checkout;
    private Toolbar cartToolbar;
    private   RelativeLayout cartfooter;
    private DecimalFormat df = new DecimalFormat("0.#");
    private double grandtot = 0;
    private static SharedPrefs sharedPrefs ;
    private MenuItem search,carts;
    LayerDrawable mCartMenuIcon;
    UserInfo userInfo = new UserInfo();
    public static String USERID ="Userid";
    private String userid ="";
    private ArrayList<CartModel> cartModels = new  ArrayList<CartModel>();
    private ApiInterface apiInterface;
    private Cart_Adapter categoryAdapter;
    private ViewGroup footerView;
    public static CartActivity instance;
    private TextView grandtotal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        instance =this;
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
        //Log.d("fai", userid.toString()+"nmvm");
        sharedPrefs = new SharedPrefs();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        cart.setLayoutManager(layoutManager);

        cart.setItemAnimator(new DefaultItemAnimator());

        new HttpAsyncTask().execute("https://shopptree.com/api/Api_Carts?Cartid=001");
        categoryAdapter = new Cart_Adapter(getBaseContext(),cartModels);
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

         footerView = (ViewGroup) inflater.inflate(R.layout.grand_total, null, false);
        priceTotal = (TextView) footerView.findViewById(R.id.Price);
        priceText = (TextView) footerView.findViewById(R.id.Price_text);
        delivery = (TextView) footerView.findViewById(R.id.delivery_charge);
        grandtotal = (TextView) footerView.findViewById(R.id.TotalPayableAmount);
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
        cartlist.addFooterView(footerView);//Add view to list view as footer view

        //set the text to Footer View
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserInfo userInfo = sharedPrefs.getUserInfo (getBaseContext ());
                if (userInfo == null){
                   Intent in = new Intent(getBaseContext(),LoginActivity.class);
                    startActivity(in);
                }else {
                    updateCart ("001",userInfo.getUserID ());
                    //Intent in = new Intent(getBaseContext(), MyAddressActivity.class)
                            //.putExtra("userid",userInfo.getUserId())
                      //      .putExtra("grandTotal",String.valueOf(grandtot));
                    //startActivity(in);
                }

            }
        });

    }
    private void updateCart(String cartId,String userId){
        apiInterface = RetroFit.getClient().create(ApiInterface.class);
        String a = sharedPrefs.getTempCartID (getBaseContext ());
        apiInterface.updateCart (cartId,userId).enqueue (new Callback<ResponseBody> () {
          @Override
          public void onResponse (Call<ResponseBody> call, Response<ResponseBody> response) {
          Log.d("succes", String.valueOf (response.code ()));

          if (response.isSuccessful ()) {
              Intent in = new Intent(CartActivity.this, MyAddressActivity.class)
                      //.putExtra("userid",userInfo.getUserId())
                      .putExtra("grandTotal",String.valueOf(grandtot));
              startActivity(in);
           }
           }
            @Override
            public void onFailure (Call<ResponseBody> call, Throwable t) {

            }
          }
        );

    }


    public void refresh(int cartsize,double cartamount){
        //Toast.makeText(getBaseContext (),  "is removed ", Toast.LENGTH_SHORT).show();
        //grandtotal.setText ("zero");
        //cartlist.removeFooterView (footerView);
        if (cartsize>0){
            priceText.setText("Price ("+cartsize+")items");
            priceTotal.setText(getResources().getString(R.string.Rs) +" "+ df.format(cartamount));
            if (cartamount<500){
                delivery.setText(getResources().getString(R.string.Rs)+"49");
                grandtot = cartamount + 49;
            }else {
                grandtot = cartamount + 0;
                delivery.setText(getResources().getString(R.string.Rs)+"0");
            }
            grandtotal.setText(getResources().getString(R.string.Rs) +" "+ String.valueOf(df.format(grandtot)));
            cartgrandTotol.setText(getResources().getString(R.string.Rs) +" "+ String.valueOf(df.format(grandtot)));
        }else {
            cartfooter.setVisibility (View.GONE);
            emptycart.setVisibility (View.VISIBLE);
            cartlist.removeFooterView (footerView);
        }
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
            //sharedPrefs.SaveCart(getBaseContext(),result);
            cartModels.addAll (result);
            cartlist.setAdapter(categoryAdapter);
            categoryAdapter.notifyDataSetChanged ();

            if (result.size()>0){
                emptycart.setVisibility(View.INVISIBLE);
                cartfooter.setVisibility(View.VISIBLE);
                addHeaderFooterView(result);
            }else{
                //cartlist.removeAllViews ();


            }

        }
    }
}

