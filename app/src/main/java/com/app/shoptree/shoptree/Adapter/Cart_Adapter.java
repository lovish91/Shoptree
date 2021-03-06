package com.app.shoptree.shoptree.Adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.CartActivity;
import com.app.shoptree.shoptree.CategoryActivity;
import com.app.shoptree.shoptree.JsonParser;
import com.app.shoptree.shoptree.ProductActivity;
import com.app.shoptree.shoptree.R;
import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.database.MyDb;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by lovishbajaj on 29/07/17.
 */

public class Cart_Adapter extends BaseAdapter {
    private Context mcontext;
    private ArrayList<CartModel> products;
    private static LayoutInflater inflater = null;
    private String TAG = "testing";
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    private double total = 0;
    double grandtotal = 0.0;
    private String Status="";
    DecimalFormat df = new DecimalFormat("0.#");
    private ApiInterface apiInterface;
    private ProgressDialog progressDialog ;







    public Cart_Adapter(Context context, ArrayList<CartModel> products){
        this.products = products;
        this.mcontext = context;

        inflater = (LayoutInflater)mcontext.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == products.size() + 1) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
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

    public  class Holder
    {
        TextView prodctName;
        ImageView prodctImg;
        TextView productunit;
        TextView productdisc;
        ImageButton remove;
        TextView productQuantity;
        TextView cartRate;
        TextView cartAmount;
        ImageButton add;
        ImageView minus;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        int type = getItemViewType(position);
        View rowView = convertView;
        final MyDb myDb = new MyDb (mcontext);
        final Cart_Adapter.Holder holder = new Cart_Adapter.Holder();
        final CartModel product = products.get(position);
        rowView = inflater.inflate(R.layout.cart_item, null);
        holder.prodctName = (TextView) rowView.findViewById(R.id.Product_Title);
        holder.prodctImg = (ImageView) rowView.findViewById(R.id.Product_Img);
        //holder.productunit = (TextView) rowView.findViewById(R.id.Product_Unit);
        holder.productQuantity = (TextView) rowView.findViewById(R.id.Product_quantity);
        holder.cartRate = (TextView) rowView.findViewById(R.id.Product_price);
        holder.cartAmount = (TextView) rowView.findViewById(R.id.TotalAmount);
        holder.add = (ImageButton) rowView.findViewById(R.id.Product_plus);
        holder.minus = (ImageButton) rowView.findViewById(R.id.Product_minus);
        holder.remove = (ImageButton) rowView.findViewById(R.id.Product_remove);

        holder.prodctName.setText(product.getProductName());
        holder.productQuantity.setText(product.getCartQuantity());
        holder.cartRate.setText(mcontext.getResources().getString(R.string.Rs) + df.format(product.getCartRate()).toString());

        total = (product.getCartRate() * Double.valueOf(product.getCartQuantity()));
        holder.cartAmount.setText(mcontext.getResources().getString(R.string.Rs) + String.valueOf(df.format(total)));
        Log.i(TAG, product.getPmId().toString());
        String a = product.getProductImg();


        for (int i=0; i<=products.size();i++){
           grandtotal = grandtotal + total;
        }
        //Log.i(TAG, String.valueOf(grandtotal));
        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(CartActivity.instance);
                progressDialog.show ();
                try {
                   String abc = new delItemCart ().execute("https://shopptree.com/api/Api_UpdateCart?CartID=001&PMID="+product.getPmId()).get();
                if (abc.equals ("202")){
                    Toast.makeText(mcontext, product.getProductName()+ "is removed ", Toast.LENGTH_SHORT).show();
                    myDb.open ();
                    myDb.deleteproduct (product.getPmId ());
                    myDb.close ();
                    products.remove(product);
                    notifyDataSetChanged ();
                    int cartsize = 0;
                    cartsize = products.size ();
                    double cartamount = 0;
                    for (CartModel cartModel:products){
                        cartamount += Double.valueOf(cartModel.getCartQuantity())*cartModel.getCartRate();
                    }
                    CartActivity.instance.refresh (cartsize,cartamount);

                }
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                } catch (ExecutionException e) {
                    e.printStackTrace ();
                }
                //CartActivity cartActivity = CartActivity.instance;
                //cartActivity.refresh ();
                progressDialog.dismiss ();
            }
        });

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(CartActivity.instance);
                progressDialog.show ();
                try {
                    myDb.open ();
                int a = myDb.getQuantity (product.getPmId ());
                if (a >0){
                    int b = a+1;
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("CartID","001");
                    jsonParam.put("PMID",Integer.valueOf(product.getPmId()));
                    jsonParam.put("CartQty",b);
                    jsonParam.put("CartAmount",product.getCartRate() * Double.valueOf(product.getCartQuantity()));
                    Log.i("add",jsonParam.toString());

                    String status = new AddtocartTask(jsonParam).execute("https://shopptree.com/api/Api_Updatecart").get ();
                    if (status.equals ("Accepted")){
                        myDb.updateQuantity(product.getPmId (),b);
                        product.setCartQuantity(String.valueOf(b));
                        total = (product.getCartRate() * Double.valueOf(product.getCartQuantity()));
                        holder.cartAmount.setText(mcontext.getResources().getString(R.string.Rs) + String.valueOf(df.format(total)));
                        myDb.close ();
                        notifyDataSetChanged ();
                        int cartsize = 0;
                        cartsize = products.size ();
                        double cartamount = 0;
                        for (CartModel cartModel:products){
                            cartamount += Double.valueOf(cartModel.getCartQuantity())*cartModel.getCartRate();
                        }
                        CartActivity.instance.refresh (cartsize,cartamount);
                    }
                }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace ();
                } catch (ExecutionException e) {
                    e.printStackTrace ();
                }
                progressDialog.dismiss ();

            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(CartActivity.instance);
                progressDialog.show ();
                try{
                    myDb.open ();
                    int a = myDb.getQuantity (product.getPmId ());
                    if (a ==1){
                        String status = new delItemCart().execute("https://shopptree.com/api/Api_UpdateCart?CartID=001&PMID="+product.getPmId ()).get();
                            if (status .equals ("202")) {
                                Toast.makeText (mcontext, product.getProductName () + "is removed ", Toast.LENGTH_SHORT).show ();
                                myDb.deleteproduct (product.getPmId ());
                                myDb.close ();
                                products.remove (product);
                                notifyDataSetChanged ();
                                int cartsize = 0;
                                cartsize = products.size ();
                                double cartamount = 0;
                                for (CartModel cartModel:products){
                                    cartamount += Double.valueOf(cartModel.getCartQuantity())*cartModel.getCartRate();
                                }
                                CartActivity.instance.refresh (cartsize,cartamount);
                                progressDialog.dismiss ();
                            }
                    }else {
                        int b = a - 1;
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("CartID","001");
                        jsonParam.put("PMID",Integer.valueOf(product.getPmId()));
                        jsonParam.put("CartQty",b);
                        jsonParam.put("CartAmount",product.getCartRate() * Double.valueOf(product.getCartQuantity()));
                        Log.i("minus",jsonParam.toString());
                        String status = new AddtocartTask(jsonParam).execute("https://shopptree.com/api/Api_Updatecart").get ();
                        if (status.equals ("Accepted")){
                            holder.productQuantity.setText(String.valueOf(b));
                            myDb.updateQuantity(product.getPmId (),b);
                            product.setCartQuantity(String.valueOf(b));
                            total = (product.getCartRate() * Double.valueOf(product.getCartQuantity()));
                            holder.cartAmount.setText(mcontext.getResources().getString(R.string.Rs) + String.valueOf(df.format(total)));
                            myDb.close ();
                            //notifyDataSetChanged ();
                            int cartsize = 0;
                            cartsize = products.size ();
                            double cartamount = 0;
                            for (CartModel cartModel:products){
                                cartamount += Double.valueOf(cartModel.getCartQuantity())*cartModel.getCartRate();
                            }
                            CartActivity.instance.refresh (cartsize,cartamount);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss ();

            }
        });
        Picasso.with(mcontext)
                .load("https://shopptree.com"+a.substring(1))
                .into(holder.prodctImg);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), ProductActivity.class).putExtra("productId",product.getPmId());
                //v.getContext().startActivity(intent);
                // TODO Auto-generated method stub
            }
        });
        return rowView;
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
            Log.i("JSON",jsonObject.toString()+urls[0]);
            String abc = JsonParser.postData(urls[0],jsonObject);
            return abc;
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  abc) {
            Toast.makeText(mcontext, abc, Toast.LENGTH_SHORT).show();
        }
    }


    private class delItemCart extends AsyncTask<String, Void, String> {
        //ProgressDialog progressDialog = new ProgressDialog(CartActivity.instance);
        @Override
        protected void onPreExecute() {
            Log.i(TAG, "onPreExecute");
           //progressDialog.show();
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
           Toast.makeText(mcontext, abc, Toast.LENGTH_SHORT).show();

            //progressDialog.dismiss();
        }
    }

}


