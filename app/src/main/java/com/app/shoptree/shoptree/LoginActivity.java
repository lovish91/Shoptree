package com.app.shoptree.shoptree;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.app.shoptree.shoptree.Utilities.ApiInterface;
import com.app.shoptree.shoptree.Utilities.RetroFit;
import com.app.shoptree.shoptree.Utilities.SharedPrefs;
import com.app.shoptree.shoptree.model.LoginModel;
import com.app.shoptree.shoptree.model.UserInfo;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.JsonObject;

import org.apache.http.conn.ConnectTimeoutException;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.facebook.FacebookSdk.getApplicationContext;

public class LoginActivity extends AppCompatActivity {

    private TextView signup;
    private EditText email,password;
    private Button emaillogin;
    private LoginButton facebookLogin;
    private CallbackManager callbackManager;
    private String pass;
    private String emailID;
    private ApiInterface apiInterface;
    private SharedPrefs sharedPrefs;
    public static String USERID = "Userid";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_login);
        signup = (TextView) findViewById(R.id.link_signup);
        emaillogin = (Button) findViewById(R.id.btn_login);
        email = (EditText) findViewById(R.id.input_email);
        password = (EditText) findViewById(R.id.input_password);
        sharedPrefs =  new SharedPrefs();

        emaillogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 emailID = email.getText().toString();
                 pass = password.getText().toString();
                if (!isValidEmail(emailID)) {
                    email.setError("Invalid Email");
                }
                else
                if (!isValidPassword(pass)) {
                    password.setError("Invalid Password");
                }
                Login(emailID,pass);
                //new LoginManual().execute("https://shopptree.com/api/Api_SignIn");

            }
        });

        facebookLogin = (LoginButton)findViewById(R.id.login_button);
        facebookLogin.setReadPermissions("email", "public_profile", "user_friends");

        facebookLogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
               // signup.setText("User ID:  " +
                 //       loginResult.getAccessToken().getUserId() + "\n" +
                   //     "Auth Token: " + loginResult.getAccessToken().getToken());

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback(){

                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {
                                    // Application code
                                    String email = response.getJSONObject().getString("email");
                                    String profi = response.getJSONObject().getString("name");
                                    String id = response.getJSONObject().getString("id");
                                    signup.setText("Login Success \n" +profi +"\n" +"\n"+id + email+response.toString());
                                }catch(Exception e){
                                    e.printStackTrace();;
                                }
                            }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                signup.setText("Login attempt cancelled.");
            }

            @Override
            public void onError(FacebookException e) {
                signup.setText("Login attempt failed."+e.getMessage().toString());
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

    }
    public void Login(String emailID,String password) {
        apiInterface = RetroFit.getClient().create(ApiInterface.class);
        //LoginModel loginModel = new LoginModel(emailID,password);
        JsonObject paramObject = new JsonObject ();
        paramObject.addProperty ("UserEmailID", emailID);
        paramObject.addProperty ("UserPasswordHash",password);
        Log.d("success", paramObject.toString());

        Call <UserInfo> call = apiInterface.UserLogin(paramObject);
        call.enqueue(new Callback<UserInfo>() {

            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                Log.d("succ", String.valueOf(response.code()));


                if(response.isSuccessful ()){
                    //Log.d("fai", result.toString());
                    UserInfo userInfo = response.body ();
                    sharedPrefs.setUserInfo (userInfo,getBaseContext ());
                    Log.d("userinfo", userInfo.getUserID ());

                    updateCart ("001",userInfo.getUserID ());

                }else {

                    Toast.makeText(getBaseContext(),"Email or Password Incorrect" ,Toast.LENGTH_LONG).show();

                }

            }

            @Override
            public void onFailure(Call<UserInfo> call, Throwable t) {

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
    private boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 5) {
            return true;
        }
        return false;
    }
    private void printKeyHash(){
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.app.shoptree.shoptree",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.d("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("KeyHash:", e.toString());
        }
    }

        //signup.setText();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


    private void updateCart(String cartId,String userId){
        apiInterface = RetroFit.getClient().create(ApiInterface.class);
        String a = sharedPrefs.getTempCartID (getBaseContext ());
        apiInterface.updateCart (cartId,userId).enqueue (new Callback<ResponseBody> () {
                    @Override
                    public void onResponse (Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d("succes", String.valueOf (response.code ()));

                        if (response.isSuccessful ()) {
                            Intent intent = new Intent(LoginActivity.this ,MainActivity.class);
                            startActivity(intent);
                            finish ();
                        }
                    }

                    @Override
                    public void onFailure (Call<ResponseBody> call, Throwable t) {

                    }
                }
        );

    }
    private class LoginManual extends AsyncTask<String, Void, String> {
        String status = null;

        @Override
        protected String doInBackground(String... urls) {
            BufferedReader reader = null;
            String result = "";
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");
                conn.setRequestProperty("API_KEY", "MyApiKey");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                JSONObject jsonParam = new JSONObject();
                jsonParam.put("UserEmailId", emailID);
                jsonParam.put("UserPasswordHash", pass);

                Log.i("JSON", conn.toString ());
                DataOutputStream os = new DataOutputStream(conn.getOutputStream());
                //os.writeBytes(URLEncoder.encode(jsonParam.toString(), "UTF-8"));
                os.writeBytes(jsonParam.toString());

                os.flush();
                os.close();

                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { //success
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                            conn.getInputStream()));
                    String inputLine;
                    StringBuffer response = new StringBuffer();

                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    in.close();

                    // print result
                    result = response.toString();
                    System.out.println(result.toString());
                } else {
                    System.out.println("POST request not worked");
                }
                status = String.valueOf(conn.getResponseCode());
                Log.i("STATUS", String.valueOf(conn.getResponseCode()));
                Log.i("MSG", conn.getResponseMessage());

                conn.disconnect();
            } catch (SocketTimeoutException e){
                Toast.makeText(getApplicationContext(), "Socket Timeout", Toast.LENGTH_LONG).show();
            } catch (ConnectTimeoutException bug) {
                Toast.makeText(getApplicationContext(), "Connection Timeout", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }


            return result;
        }


        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            if(result.equals("404")){
                //Log.d("fai", result.toString());
                Toast.makeText(getBaseContext(),"Email or Password Incorrect" ,Toast.LENGTH_LONG).show();
            }else {
                SharedPreferences.Editor editor = getSharedPreferences(USERID, Context.MODE_PRIVATE).edit();
                String userid = result.toString().replace("\"", "").toString();
                editor.putString("Userid",userid);
                editor.commit ();

                Toast.makeText(getBaseContext(),result.replace("\"", "").toString(), Toast.LENGTH_LONG).show();

            }
        }
    }
}
