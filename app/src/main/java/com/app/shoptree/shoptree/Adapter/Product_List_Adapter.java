package com.app.shoptree.shoptree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
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

import com.app.shoptree.shoptree.CategoryActivity;
import com.app.shoptree.shoptree.JsonParser;
import com.app.shoptree.shoptree.MainActivity;
import com.app.shoptree.shoptree.ProductActivity;
import com.app.shoptree.shoptree.R;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.database.MyDb;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.app.shoptree.shoptree.model.Product;
import com.app.shoptree.shoptree.model.TestModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

/**
 * Created by lovishbajaj on 30/05/17.
 */

public class Product_List_Adapter extends BaseAdapter{

    private Context mcontext;
    private ArrayList<Product> products;
    private SharedPrefs sharedPrefs = new SharedPrefs();
    private ArrayList<TestModel> cartModels = new ArrayList<>();
    private static LayoutInflater inflater = null;
    private int count ;


    public Product_List_Adapter(Context context, ArrayList<Product> products){
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

    public  class Holder
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
                        new AddtocartTask(jsonParam).execute("https://shopptree.com/api/Api_Updatecart");
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
                        new AddtocartTask(jsonParam).execute("https://shopptree.com/api/Api_Updatecart");
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


                    String abc = new AddtocartTask(jsonObject).execute("https://shopptree.com/api/Api_carts/").get();
                    if (abc.equals("Created")){
                        TestModel cartModel = new TestModel(0,"","","",product.getPmId(),"","",product.getProductId(),"","","","",1,0,0,"0",0,"0",0,0,0,0,0);
                        sharedPrefs.addCartItem(mcontext,cartModel);
                        holder.Product_add_minus.setVisibility(View.VISIBLE);
                        holder.addToCart.setVisibility(View.INVISIBLE);
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
        protected void onPostExecute(String  abc) {
             Toast.makeText(mcontext, abc, Toast.LENGTH_SHORT).show();


        }

    }

}
