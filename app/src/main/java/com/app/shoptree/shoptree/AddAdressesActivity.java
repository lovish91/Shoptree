package com.app.shoptree.shoptree;

import android.app.Dialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.model.Address;
import com.app.shoptree.shoptree.model.CityModel;
import com.app.shoptree.shoptree.model.Product;
import com.app.shoptree.shoptree.model.StateModel;
import com.google.gson.JsonElement;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import me.srodrigo.androidhintspinner.HintAdapter;
import me.srodrigo.androidhintspinner.HintSpinner;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddAdressesActivity extends AppCompatActivity {
    private EditText UserMobile,UserEmail,UserLocationName,UserAddreddLine1,UserAddressLine2,UserAddressLine3,UserPin;
    private Spinner UserState, UserCity;
    private HintSpinner <String> hintSpinner;
    private ApiInterface apiInterface;
    private String stateid = null;
    private String cityid = null;
    private String PhoneNo, Email, LocationName, AddressName1, AddressName2, AddressName3, Usercity, Userstate, userpinn = "";
    private int status = 0;
    private Button saveButton;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adresses);

        /*toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });*/
        toolbar = (Toolbar) findViewById(R.id.addAddresstoolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        UserMobile = (EditText) findViewById(R.id.UserMobile);
        UserEmail = (EditText) findViewById(R.id.UserEmail);
        UserLocationName = (EditText) findViewById(R.id.UserLocationName);
        UserAddreddLine1 = (EditText) findViewById(R.id.UserAddreddLine1);
        UserAddressLine2 = (EditText) findViewById(R.id.UserAddreddLine2);
        UserAddressLine3 = (EditText) findViewById(R.id.UserAddreddLine3);
        UserPin = (EditText) findViewById(R.id.UserPin);
        UserState = (Spinner) findViewById(R.id.UserState);
        UserCity = (Spinner) findViewById(R.id.UserCity);


        apiInterface = RetroFit.getClient().create(ApiInterface.class);
        states();

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addAddress();
            }
        });
    }
    private void states(){
        apiInterface.getStates().enqueue(new Callback<List<StateModel>>() {
            @Override
            public void onResponse(Call<List<StateModel>> call, Response<List<StateModel>> response) {
                ArrayList<StateModel> stateModels = new ArrayList<StateModel>();
                stateModels.add(new StateModel(1,"id","Select State",1));
                for (StateModel stateModel : response.body()){
                    stateModels.add(new StateModel(stateModel.getSNo(),stateModel.getStateId(),stateModel.getStateName(),stateModel.getStateStatus()));
                    Log.d("states", stateModel.toString());
                }
                final ArrayAdapter<StateModel> adapter = new ArrayAdapter<StateModel>(getBaseContext(),
                        android.R.layout.simple_spinner_item, stateModels){
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                UserState.setAdapter(adapter);
                UserState.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i>0) {
                            stateid  = adapter.getItem(i).getStateId();
                            Toast.makeText(getBaseContext(), stateid, Toast.LENGTH_LONG).show();
                            cities(stateid);
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                //cities(stateid);
            }

            @Override
            public void onFailure(Call<List<StateModel>> call, Throwable t) {

            }
        });

    }
    private void cities(String stateid){
        apiInterface.getCities(stateid).enqueue(new Callback<ArrayList<CityModel>>() {
            @Override
            public void onResponse(Call<ArrayList<CityModel>> call, Response<ArrayList<CityModel>> response) {
                ArrayList<CityModel> cityModels = new ArrayList<CityModel>();
                cityModels.add(new CityModel(1,"id","id","Select City",1));
                for (CityModel cityModel : response.body()){
                    cityModels.add(new CityModel(cityModel.getSNo(),cityModel.getCityId(),cityModel.getStateId(),cityModel.getCityName(),cityModel.getCityStatus()));
                    Log.d("states", cityModel.toString());
                }
                final ArrayAdapter<CityModel> cityadapter = new ArrayAdapter<CityModel>(getBaseContext(),
                        android.R.layout.simple_spinner_item, cityModels)
                {
                    @Override
                    public boolean isEnabled(int position){
                        if(position == 0)
                        {
                            // Disable the first item from Spinner
                            // First item will be use for hint
                            return false;
                        }
                        else
                        {
                            return true;
                        }
                    }
                    @Override
                    public View getDropDownView(int position, View convertView,
                                                ViewGroup parent) {
                        View view = super.getDropDownView(position, convertView, parent);
                        TextView tv = (TextView) view;
                        if(position == 0){
                            // Set the hint text color gray
                            tv.setTextColor(Color.GRAY);
                        }
                        else {
                            tv.setTextColor(Color.BLACK);
                        }
                        return view;
                    }
                };
                cityadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                UserCity.setAdapter(cityadapter);
                UserCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if (i>0) {
                            cityid  = cityadapter.getItem(i).getCityId();
                            Toast.makeText(getBaseContext(), cityid, Toast.LENGTH_LONG).show();
                            //addAddress();
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
            @Override
            public void onFailure(Call<ArrayList<CityModel>> call, Throwable t) {

            }
        });
    }
        private void addAddress(){


                PhoneNo = UserMobile.getText().toString();
                Email = UserEmail.getText().toString();
                LocationName = UserLocationName.getText().toString();
                AddressName1 = UserAddreddLine1.getText().toString();
                AddressName2 = UserAddressLine2.getText().toString();
                AddressName3 = UserAddressLine3.getText().toString();
                userpinn = UserPin.getText().toString();
                //if (stateid != "") {
                  //  Userstate = stateid;
                    //Toast.makeText(getBaseContext(), "Select a state", Toast.LENGTH_LONG).show();
                //} else if (cityid != "") {
                  //  Usercity = cityid;
                    //Toast.makeText(getBaseContext(), "Select a city", Toast.LENGTH_LONG).show();
                //}

            try {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("UserAddressID", "USER1025_1");
                jsonObject.put("UserID","USER1025");
                jsonObject.put("UserEmailid",Email);
                jsonObject.put("UserMobile",PhoneNo);
                jsonObject.put("UserLocationName",LocationName);
                jsonObject.put("UserAddressLine1",AddressName1);
                jsonObject.put("UserAddressLine2",AddressName2);
                jsonObject.put("UserAddressLine3",AddressName3);
                jsonObject.put("UserCity",cityid);
                jsonObject.put("UserState",stateid);
                jsonObject.put("UserPin",userpinn);
                jsonObject.put("Status",status);
                //Log.d("JSON json",jsonObject.toString());

                String abc = new AddtocartTask(jsonObject).execute("https://shopptree.com/api/api_useraddress").get();
                Toast.makeText(getBaseContext(), abc.toString(), Toast.LENGTH_LONG).show();

            } catch (JSONException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
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

            String abc = JsonParser.postData(urls[0],jsonObject);

            return abc;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  abc) {
            String a = abc.toString();
            Toast.makeText(getBaseContext(), abc, Toast.LENGTH_SHORT).show();
        }
    }

}
