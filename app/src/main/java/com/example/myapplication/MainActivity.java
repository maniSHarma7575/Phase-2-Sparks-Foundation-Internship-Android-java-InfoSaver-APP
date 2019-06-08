package com.example.myapplication;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.profile.Profile;
import com.example.myapplication.signUp.PersonalDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private String test="http://139.59.65.145:9090/test";
    private String URL = "http://139.59.65.145:9090/user/signup";
    private String URL1 = "http://139.59.65.145:9090/user/login";
    private Button signUp,login;
    String ID="";
    int count=0;
    private EditText eMail,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        eMail=findViewById(R.id.email);
        password=findViewById(R.id.password);
        signUp = findViewById(R.id.signup);
        login=findViewById(R.id.login);
        final RequestQueue queue = Volley.newRequestQueue(this);

        //showStatus(queue);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eMail.getText().toString().equals("") && password.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Please Enter valid Data",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final String email,passWord;
                    email=eMail.getText().toString();
                    passWord=password.getText().toString();
                    loginProcess(email,passWord,queue);
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eMail.getText().toString().equals("") && password.getText().toString().equals(""))
                {
                    Toast.makeText(MainActivity.this,"Please Enter valid Data",Toast.LENGTH_SHORT).show();
                }else
                {
                    final String email,passWord;
                    email=eMail.getText().toString();
                    passWord=password.getText().toString();
                    update(email,passWord,queue);
                }
            }
        });
    }

    private void loginProcess(String email, String password, RequestQueue queue) {
        Map<String,String> params = new HashMap<>();
        params.put("email",email);
        params.put("password",password);
        final JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL1, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response.toString());
                    JSONObject data = jsonObject1.getJSONObject("data");
                    String id = data.getString("id");
                    String EMAIl = data.getString("email");
                    Intent i= new Intent(MainActivity.this, Profile.class);
                    i.putExtra("id",id);
                    i.putExtra("EMAIL",EMAIl);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
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
                    Toast.makeText(MainActivity.this,"Success \n UID : "+id,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this,PersonalDetails.class);
                    i.putExtra("ID",id);
                    i.putExtra("EMAIL",eMail.getText().toString());
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
                Toast.makeText(MainActivity.this,error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        queue.add(jsonObjectRequest);
    }

    private void showStatus(RequestQueue queue) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, test,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String success = jsonObject.getString("status");
                                Toast.makeText(MainActivity.this,"Status : "+success,Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                MainActivity.this);

        // set title
        alertDialogBuilder.setTitle("Exit");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you really want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, close
                        // current activity
                        MainActivity.this.finish();
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    public void dofb(View view) {
        Intent intent=new Intent(MainActivity.this, FacebookActivity.class);
        startActivity(intent);
    }

    public void dotweet(View view) {
        Intent intent=new Intent(MainActivity.this,TweetActivity.class);
        startActivity(intent);
    }
}
