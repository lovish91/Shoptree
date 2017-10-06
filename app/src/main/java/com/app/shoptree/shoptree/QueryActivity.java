package com.app.shoptree.shoptree;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.app.shoptree.shoptree.Adapter.Product_List_Adapter;
import com.app.shoptree.shoptree.Utilities.ExpandedGridView;
import com.app.shoptree.shoptree.model.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QueryActivity extends AppCompatActivity {

    private ExpandedGridView Querygrid;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);

        Querygrid = (ExpandedGridView) findViewById(R.id.Query_grid);

        String searhkeyword = getIntent().getStringExtra("searchkeyword");


        new bHttpAsyncTask().execute("https://shopptree.com/api/api_search/getresult?searchstring="+searhkeyword);


    }
    private class bHttpAsyncTask extends AsyncTask<String, Void, ArrayList<Product>> {
        @Override
        protected ArrayList<Product> doInBackground(String... urls) {
            ArrayList<Product> productArrayList = new ArrayList<>();
            productArrayList.clear();
            String abc = JsonParser.getData(urls[0]);
            Log.i("jsondata ",abc);

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
                    //Double oldprice = Double.valueOf(jsonObject.getString("ProMrp"));
                    Double newprice = Double.valueOf(jsonObject.getString("ProSellerPrice"));

                    Product product = new Product("1",pmid,name,"0","0",0.0,newprice,img,unit,desc);
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
            Product_List_Adapter product_adapter = new Product_List_Adapter(getBaseContext(),result);
            Querygrid.setAdapter(product_adapter);
            Querygrid.setExpanded(true);
            //textView.setText(result.toString());
        }
    }
}
