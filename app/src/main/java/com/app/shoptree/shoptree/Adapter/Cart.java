package com.app.shoptree.shoptree.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.JsonParser;
import com.app.shoptree.shoptree.ProductActivity;
import com.app.shoptree.shoptree.R;
import com.app.shoptree.shoptree.model.CartModel;
import com.app.shoptree.shoptree.model.Product;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lovishbajaj on 06/08/17.
 */

public class Cart extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;

    private String TAG = "testing";

    private ArrayList<CartModel> Cartlist;
    private Context context;



    public Cart(Context context, ArrayList<CartModel> Cartlist) {
        this.context = context;
        this.Cartlist = Cartlist;

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


      if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
            return new ItemViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            //Inflating header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grand_total, parent, false);
            return new FooterViewHolder(itemView);
        } else return null;



    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        try {
            final CartModel cartModel = Cartlist.get(position);


            if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText("Header V");
            headerHolder.headerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You clicked at Header View!", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (holder instanceof FooterViewHolder) {

            FooterViewHolder footerHolder = (FooterViewHolder) holder;
            //footerHolder.footerText.setText("Footer View" );
            footerHolder.priceItem.setText("Price() items");

        } else if (holder instanceof ItemViewHolder) {
            final ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            itemViewHolder.itemText.setText(cartModel.getProductName());
            itemViewHolder.prodctName.setText(cartModel.getProductName());
            itemViewHolder.productunit.setText(cartModel.getUnit());

            itemViewHolder.productQuantity.setText(cartModel.getCartQuantity());
            itemViewHolder.cartRate.setText(context.getResources().getString(R.string.Rs) + cartModel.getCartRate().toString());
            final String total = String.valueOf (cartModel.getCartRate() * Double.valueOf(cartModel.getCartQuantity()));
            itemViewHolder.cartAmount.setText(context.getResources().getString(R.string.Rs) +  total);
            Log.i(TAG, cartModel.getPmId().toString());
                String a = cartModel.getProductImg();

                itemViewHolder.add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        try {
                            int a = Integer.parseInt(cartModel.getCartQuantity());
                            if (a > 0){
                                int b = a+1;

                                JSONObject jsonParam = new JSONObject();
                                jsonParam.put("CartID","001");
                                jsonParam.put("PMID",Integer.valueOf(cartModel.getPmId()));
                                jsonParam.put("CartQty",b);
                                jsonParam.put("CartAmount",cartModel.getCartRate() * Double.valueOf(cartModel.getCartQuantity()));
                                Log.i("JSON",jsonParam.toString());
                                itemViewHolder.productQuantity.setText(String.valueOf(b));
                                new AddtocartTask(jsonParam).execute("http://192.168.1.9/api/Api_Carts");
                                Cartlist.get(position).setCartQuantity(String.valueOf(b));
                                notifyDataSetChanged();


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                Picasso.with(context)
                        .load("http://shopptree.com"+a.substring(1))
                        .into(itemViewHolder.prodctImg);
                itemViewHolder.prodctName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        //Intent intent = new Intent(v.getContext(), ProductActivity.class).putExtra("productId",cartModel.getPmId());
                        //v.getContext().startActivity(intent);
                        // TODO Auto-generated method stub
                    }
                });
            itemViewHolder.itemText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Toast.makeText(context, "You clicked at item " + position, Toast.LENGTH_SHORT).show();
                }
            });
        }
        }catch (Exception e){
            Log.i(TAG, "knkn "+e.getMessage().toString());

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == Cartlist.size() + 2) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }


    private class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView headerTitle;

        public HeaderViewHolder(View view) {
            super(view);
            headerTitle = (TextView) view.findViewById(R.id.product_tit);
        }
    }

    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView footerText;
        TextView priceItem;
        TextView price;
        TextView delivery;
        TextView grandTotal;

        public FooterViewHolder(View view) {
            super(view);
            footerText = (TextView) view.findViewById(R.id.product_tit);
            priceItem = (TextView) view.findViewById(R.id.Price_text);
            price = (TextView) view.findViewById(R.id.Price);
            delivery = (TextView) view.findViewById(R.id.delivery_charge);
            grandTotal = (TextView) view.findViewById(R.id.TotalPayableAmount);
        }
    }

    private class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemText;
        TextView prodctName;
        ImageView prodctImg;
        TextView productunit;
        TextView productdisc;
        TextView productQuantity;
        TextView cartRate;
        TextView cartAmount;
        ImageButton add;
        ImageView minus;

        public ItemViewHolder(View itemView) {
            super(itemView);
            itemText = (TextView) itemView.findViewById(R.id.Product_Title);
            prodctName = (TextView) itemView.findViewById(R.id.Product_Title);
            prodctImg = (ImageView) itemView.findViewById(R.id.Product_Img);
            productunit = (TextView) itemView.findViewById(R.id.Product_Unit);
            productQuantity = (TextView) itemView.findViewById(R.id.Product_quantity);
            cartRate = (TextView) itemView.findViewById(R.id.Product_price);
            cartAmount = (TextView) itemView.findViewById(R.id.TotalAmount);
            add = (ImageButton) itemView.findViewById(R.id.Product_plus);
            minus = (ImageButton) itemView.findViewById(R.id.Product_minus);
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
            Log.i("JSON",jsonObject.toString()+urls[0]);

            String abc = JsonParser.PutData(urls[0],jsonObject);

            return abc;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  abc) {
            String a = abc.toString();
            Toast.makeText(context, abc, Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return Cartlist.size()+2;
    }
}
