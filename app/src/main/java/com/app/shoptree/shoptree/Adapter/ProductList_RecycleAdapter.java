package com.app.shoptree.shoptree.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.JsonParser;
import com.app.shoptree.shoptree.ProductActivity;
import com.app.shoptree.shoptree.R;
import com.app.shoptree.shoptree.database.MyDb;
import com.app.shoptree.shoptree.model.Product;
import com.app.shoptree.shoptree.model.TestModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by lovishbajaj on 07/10/17.
 */

public class ProductList_RecycleAdapter extends RecyclerView.Adapter<ProductList_RecycleAdapter.ViewHolder> {
    private Context mcontext;
    private ArrayList<Product> products;
    private int count = 0;

    public ProductList_RecycleAdapter(Context mcontext,ArrayList<Product> products) {
        this.products = products;
        this.mcontext=mcontext;
//        imageLoader = AppController.getInstance().getImageLoader();

    }

    @Override
    public ProductList_RecycleAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_item,parent,false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final ProductList_RecycleAdapter.ViewHolder holder, final int position) {

        holder.productoldprice.setPaintFlags(holder.productoldprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        final MyDb myDb = new MyDb(mcontext);
        myDb.open();
        Log.d("not", String.valueOf(myDb.getQuantity(products.get(position).getPmId())));
        count = myDb.getQuantity(products.get(position).getPmId());
        int i=myDb.checkAvailable(products.get(position).getPmId());
        if (i==0) {
            holder.Product_add_minus.setVisibility(View.GONE);
            holder.addToCart.setVisibility(View.VISIBLE);
            Log.d("not avai",products.get(position).getPmId());
        }else {
            holder.Product_add_minus.setVisibility(View.VISIBLE);
            holder.addToCart.setVisibility(View.GONE);
            Log.d(" avai",products.get(position).getPmId());
            holder.Productmain.setBackgroundResource(R.drawable.productborder);
        }
        myDb.close();
        holder.Product_quantity.setText(String.valueOf(count));
        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    myDb.open();
                    int newcount =myDb.getQuantity(products.get(position).getPmId());
                    newcount = newcount + 1;
                    Log.i("button",String.valueOf(count));
                    JSONObject jsonParam = new JSONObject();
                    jsonParam.put("CartID","001");
                    jsonParam.put("PMID",Integer.valueOf(products.get(position).getPmId()));
                    jsonParam.put("CartQty",newcount);
                    jsonParam.put("CartAmount",products.get(position).getProductNewPrice() * Double.valueOf(newcount));
                    //new Product_List_Adapter.AddtocartTask(jsonParam).execute("https://shopptree.com/api/Api_Updatecart");
                    //product.setProductQuantity(String.valueOf(count));
                    myDb.updateQuantity(products.get(position).getPmId(),newcount);
                    holder.Product_quantity.setText(String.valueOf(newcount));
                    myDb.close();
                    //sharedPrefs.SaveCart(mcontext,cartModels);
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
                    myDb.open();
                    int newcount = myDb.getQuantity(products.get(position).getPmId());
                    if (newcount ==1){
                        String status = new delItemCart().execute("https://shopptree.com/api/Api_UpdateCart?CartID=001&PMID="+products.get(position).getPmId()).get();
                        Toast.makeText(mcontext, status, Toast.LENGTH_SHORT).show();
                        holder.Product_add_minus.setVisibility(View.GONE);
                        holder.addToCart.setVisibility(View.VISIBLE);
                        holder.Productmain.setBackgroundResource(R.color.my_awesome_color);
                        myDb.deleteproduct(products.get(position).getPmId());
                        myDb.close();

                        //sharedPrefs.SaveCart(mcontext, cartModels);
                        //notifyDataSetChanged();
                    }else {
                        newcount = newcount-1;
                        JSONObject jsonParam = new JSONObject();
                        jsonParam.put("CartID","001");
                        jsonParam.put("PMID",Integer.valueOf(products.get(position).getPmId()));
                        jsonParam.put("CartQty",count);
                        jsonParam.put("CartAmount",products.get(position).getProductNewPrice() * Double.valueOf(newcount));
                        //new Product_List_Adapter.AddtocartTask(jsonParam).execute("https://shopptree.com/api/Api_Updatecart");
                        //product.setProductQuantity(String.valueOf(count));
                        myDb.updateQuantity(products.get(position).getPmId(),newcount);
                        holder.Product_quantity.setText(String.valueOf(newcount));
                        myDb.close();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });


        holder.prodctName.setText(products.get(position).getProductName());
        holder.productunit.setText(products.get(position).getUnit());
        double disc = Math.round(100 - ((double)products.get(position).getProductNewPrice())*100/(double)products.get(position).getProductOldPrice());
        DecimalFormat df = new DecimalFormat("0.#");
        if (disc==0.0){
            holder.productdisc.setVisibility(View.INVISIBLE);
            holder.productoldprice.setVisibility(View.INVISIBLE);
        }else {
            holder.productdisc.setText(String.valueOf(df.format(disc)) +" % OFF");
        }

        holder.productdisc.setText(String.valueOf(df.format(disc))+" OFF");
        holder.productoldprice.setText(mcontext.getResources().getString(R.string.Rs) + (df.format(products.get(position).getProductOldPrice())).toString());
        holder.productnewprice.setText(mcontext.getResources().getString(R.string.Rs) + (df.format(products.get(position).getProductNewPrice()).toString()));
        String a = products.get(position).getProductImg();

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
                    jsonObject.put("PMID",products.get(position).getPmId());
                    jsonObject.put("CartQty","1");


                    String abc = new AddtocartTask(jsonObject).execute("https://shopptree.com/api/Api_carts/").get();
                    if (abc.equals("Created")){
                        myDb.open();
                        myDb.insertData(products.get(position).getProductId(),products.get(position).getPmId(),products.get(position).getProductName(),products.get(position).getProductImg(),String.valueOf(products.get(position).getProductNewPrice()),1);
                        //sharedPrefs.addCartItem(mcontext,cartModel);
                        holder.Product_add_minus.setVisibility(View.VISIBLE);
                        holder.addToCart.setVisibility(View.INVISIBLE);
                        notifyDataSetChanged();
                    }
                   // Log.i("InputS", abc.toString());

                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

            }
        });
        /*rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProductActivity.class).putExtra("productId",product.getPmId());
                v.getContext().startActivity(intent);
                // TODO Auto-generated method stub
            }
        });*/

    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
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

        public ViewHolder(View itemView) {
            super(itemView);
            prodctName = (TextView) itemView.findViewById(R.id.product_tit);
            prodctImg = (ImageView) itemView.findViewById(R.id.product_img);
            productunit = (TextView) itemView.findViewById(R.id.product_unit);
            productdisc = (TextView) itemView.findViewById(R.id.product_disc);
            productnewprice = (TextView) itemView.findViewById(R.id.product_newPrc);
            productoldprice = (TextView) itemView.findViewById(R.id.product_oldPrc);
            addToCart = (Button) itemView.findViewById(R.id.add_product);
            Productmain = (RelativeLayout) itemView.findViewById(R.id.mainProductView);
            minus = (ImageButton) itemView.findViewById(R.id.Product_mns);
            add = (ImageButton) itemView.findViewById(R.id.Product_pls);
            Product_add_minus = (LinearLayout) itemView.findViewById(R.id.Product_add_minus);
            Product_quantity = (TextView) itemView.findViewById(R.id.Product_quty);


        }
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
    private class delItemCart extends AsyncTask<String, Void, String> {
        //ProgressDialog progressDialog = new ProgressDialog(mcontext);
        @Override
        protected void onPreExecute() {
           // Log.i(TAG, "onPreExecute");
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

            //progressDialog.dismiss();
        }
    }

}
