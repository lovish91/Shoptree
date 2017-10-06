package com.app.shoptree.shoptree;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Adapter.SearchAdapter;
import com.app.shoptree.shoptree.model.CategoryModel;
import com.app.shoptree.shoptree.model.SugesstionM;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchbox;
    private ListView searchlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchbox = (SearchView) findViewById(R.id.searchbox);
        searchlist = (ListView) findViewById(R.id.searchList);

        searchbox.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length()>2){
                    new searchAsync().execute("https://shopptree.com/api/api_search/?searchkeyword="+newText);
                }


                return false;
            }
        });
    }
    private class searchAsync extends AsyncTask<String,Void,ArrayList<String>>{
        //private String searchKeyword;
        //searchAsync(String searchKeyword){
          //  this.searchKeyword = searchKeyword;
       // }

        @Override
        protected ArrayList<String> doInBackground(String... urls) {
            ArrayList<String> sugesstionMs = new ArrayList<>();
            sugesstionMs.clear();
            String reply = JsonParser.getData(urls[0]);

            try {
                JSONArray jsonArray = new JSONArray(reply);
                for (int i =0;i<jsonArray.length();i++){
                    //JSONObject jsonObject = jsonArray.getJSONObject(i);
                    sugesstionMs.add(jsonArray.getString(i));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return sugesstionMs;
        }
        @Override
        protected void onPostExecute(final ArrayList<String> reply) {
            Toast.makeText(getBaseContext(), reply.toString(), Toast.LENGTH_SHORT).show();
            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1,reply);
            searchlist.setAdapter(arrayAdapter);
          searchlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
              @Override
              public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                  int itemPosition     = position;
                  String  itemValue    = (String) searchlist.getItemAtPosition(position);
                  Toast.makeText(getApplicationContext(),itemValue , Toast.LENGTH_LONG).show();
                  Intent in = new Intent(getBaseContext(), QueryActivity.class);
                  in.putExtra("searchkeyword", itemValue);
                  startActivity(in);
              }
          });
            //SearchAdapter searchAdapter = new SearchAdapter(getBaseContext(),reply);
            //searchlist.setAdapter(searchAdapter);
        }
    }

}
