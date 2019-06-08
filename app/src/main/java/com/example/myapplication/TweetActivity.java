package com.example.myapplication;

import android.content.Intent;
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
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.Twitter;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class TweetActivity extends AppCompatActivity {
    TwitterLoginButton loginButton;
    String ID;
    RequestQueue queue;
    private String test="http://139.59.65.145:9090/test";
    private String URL = "http://139.59.65.145:9090/user/signup";
    private String URL1 = "http://139.59.65.145:9090/user/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        queue = Volley.newRequestQueue(this);
        Twitter.initialize(this);
        setContentView(R.layout.activity_tweet);

        loginButton = (TwitterLoginButton) findViewById(R.id.tlogin_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                // Do something with result, which provides a TwitterSession for making API calls
                TwitterSession session = TwitterCore.getInstance().getSessionManager().getActiveSession();
                TwitterAuthToken authToken = session.getAuthToken();
                String token = authToken.token;
                String secret = authToken.secret;

                login(session);
            }

            @Override
            public void failure(TwitterException exception) {
                // Do something on failure
                Toast.makeText(TweetActivity.this,"Authentication failed",Toast.LENGTH_LONG).show();
            }
        });
    }

    public void login(TwitterSession session)
    {

        String emailid="sharma.manish7575@gmail.com";
        String password="manish@tweet";


            update(emailid,password,queue);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
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
                    Toast.makeText(TweetActivity.this,"Success \n UID : "+id,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(TweetActivity.this, PersonalDetails.class);
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
                Toast.makeText(TweetActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
}
