package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;


import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.signUp.PersonalDetails;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FacebookActivity extends AppCompatActivity {
    private LoginButton loginButton;
    private CallbackManager callbackManager;
    String name="";
    String ID="";
    String emailid;
    RequestQueue queue;
    private String test="http://139.59.65.145:9090/test";
    private String URL = "http://139.59.65.145:9090/user/signup";
    private String URL1 = "http://139.59.65.145:9090/user/login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        queue = Volley.newRequestQueue(this);
        loginButton=findViewById(R.id.login_button);
        callbackManager=CallbackManager.Factory.create();
        loginButton.setReadPermissions(Arrays.asList("email","public_profile"));
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {


        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }
    AccessTokenTracker tokenTracker=new AccessTokenTracker() {
        @Override
        protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
            if(currentAccessToken==null)
            {

                Toast.makeText(FacebookActivity.this, "User logged out", Toast.LENGTH_SHORT).show();
            }
            else
            {
                loaduserProfile(currentAccessToken);
            }
        }
    };

    private void loaduserProfile(AccessToken newAccessToken)
    {

        GraphRequest request=GraphRequest.newMeRequest(newAccessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {

                try {
                    String first_name=object.getString("first_name");
                    String last_name=object.getString("last_name");
                    String email=object.getString("email");
                    String id=object.getString("id");


                    name=first_name+" "+last_name;






                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        });


        emailid="ms90051@gmail.com";
        String password="manish@fb";


            update(emailid,password,queue);




    }
    private void update(final String email, final String password, RequestQueue queue) {
        Map<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject object = new JSONObject(response.toString());
                    JSONObject data = object.getJSONObject("data");
                    String id = data.getString("id");
                    ID=id;
                    Toast.makeText(FacebookActivity.this,"Success \n UID : "+id,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(FacebookActivity.this, PersonalDetails.class);
                    i.putExtra("ID",id);
                    i.putExtra("EMAIL",email);
                    Log.d("ID",id);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(FacebookActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

}
