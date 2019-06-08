package com.example.myapplication.update;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.profile.Profile;
import com.example.myapplication.signUp.PersonalDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdatePersonalDetails extends AppCompatActivity {

    String ID,Name,Mobile,Location,Skills,Links;
    private EditText name,mobile,location,links,skills;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_personal_details);
        Intent i =getIntent();
        ID=i.getStringExtra("id");
        Name=i.getStringExtra("Name");
        Mobile=i.getStringExtra("Mobile");
        Location=i.getStringExtra("Location");
        Skills=i.getStringExtra("Skills");
        Links=i.getStringExtra("Links");

        name=findViewById(R.id.fullName);
        mobile=findViewById(R.id.mobile);
        location=findViewById(R.id.location);
        links=findViewById(R.id.links);
        skills=findViewById(R.id.skills);
        save=findViewById(R.id.save);

        name.setText(Name);
        mobile.setText(Mobile);
        location.setText(Location);
        skills.setText(Skills);
        links.setText(Links);

        final String URL = "http://139.59.65.145:9090/user/personaldetail/"+ID;
        final String EMAIL = i.getStringExtra("email");
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().trim().length()>0 && mobile.getText().toString().trim().length()>0&&location.getText().toString().trim().length()>0&&links.getText().toString().trim().length()>0&&skills.getText().toString().trim().length()>0)
                    update(URL,EMAIL,ID);
                else
                    Toast.makeText(UpdatePersonalDetails.this,"Please enter valid data",Toast.LENGTH_SHORT).show();            }
        });

    }
    private void update(final String URL, final String EMAIL,final String ID) {
        Name=name.getText().toString();
        Mobile=mobile.getText().toString();
        Location=location.getText().toString();
        Links=links.getText().toString();
        Skills=skills.getText().toString();
        Map<String,String> params = new HashMap<>();
        params.put("skills",Skills);
        params.put("mobile_no",Mobile);
        params.put("name",Name);
        params.put("links",Links);
        params.put("location",Location);
        JSONObject  parameters = new JSONObject(params);
        RequestQueue queue= Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    String uid=data.getString("uid");
                    String id = data.getString("id");
                    Intent i= new Intent(UpdatePersonalDetails.this, Profile.class);
                    Toast.makeText(UpdatePersonalDetails.this,"Updated",Toast.LENGTH_SHORT).show();
                    i.putExtra("EMAIL",EMAIL);
                    i.putExtra("id",uid);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(UpdatePersonalDetails.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
    }
