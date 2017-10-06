package com.app.shoptree.shoptree;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.shoptree.shoptree.model.Product;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText emailId;
    private EditText password;
    private EditText phoneNo;
    private Button Signup;
    private String firstname;
    private String lastname;
    private String phoneno;
    private String email;
    private String pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firstName = (EditText) findViewById(R.id.first_name);
        lastName = (EditText) findViewById(R.id.last_name);
        emailId = (EditText) findViewById(R.id.input_email);
        phoneNo = (EditText) findViewById(R.id.input_phoneNo);
        password = (EditText) findViewById(R.id.input_password);
        Signup = (Button) findViewById(R.id.btn_signup);


        findViewById(R.id.btn_signup).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                firstname = firstName.getText().toString();
                lastname = lastName.getText().toString();
                phoneno = phoneNo.getText().toString();
                email = emailId.getText().toString();
                pass = password.getText().toString();

                if (isNotNull(firstname)){
                    firstName.setError("Empty");
                }
                else
                if (isNotNull(lastname)){
                    lastName.setError("Empty");
                }
                else
                if (isValidMobile(phoneno)){
                    phoneNo.setError("Error");
                }
                else
                if (!isValidEmail(email)) {
                    emailId.setError("Invalid Email");
                }
                else
                if (!isValidPassword(pass)) {
                    password.setError("Invalid Password");
                }


                new SignUp().execute("https://shopptree.com/api/Api_UserAccount");
            }
        });



    }
    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // validating password with retype password
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }
    private boolean isNotNull(String name){
        if (name!=null && name.length()>0){
            return true;
        }
        return false;
    }
    private boolean isValidMobile(String mobile) {
        if (!TextUtils.isEmpty(mobile)) {
            return Patterns.PHONE.matcher(mobile).matches();
        }
        return false;
    }


    private class SignUp extends AsyncTask<String, Void, String> {
        String status = null;

        @Override
        protected String doInBackground(String... urls ){
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept","application/json");
                conn.setRequestProperty("API_KEY", "MyApiKey");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                    jsonParam.put("UserFirstName",firstname);
                    jsonParam.put("UserLastName",lastname);
                    jsonParam.put("UserEmailId",email);
                    jsonParam.put("UserPhone",phoneno);
                    jsonParam.put("UserPasswordHash",pass);

                Log.i("JSON","");
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                status = String.valueOf(conn.getResponseCode());
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG" , conn.getResponseMessage());

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return status;
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String  result) {
            Toast.makeText(getBaseContext(), result, Toast.LENGTH_LONG).show();
            //Product_List_Adapter product_adapter = new Product_List_Adapter(getBaseContext(),result);
            //productgridview.setAdapter(product_adapter);
            //productgridview.setExpanded(true);
            //textView.setText(result.toString());
        }


    }
}
