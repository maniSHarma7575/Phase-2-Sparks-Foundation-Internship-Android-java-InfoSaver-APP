package com.example.myapplication.signUp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class EducDetails extends AppCompatActivity {

    private Button save;
    private EditText organisation,degree,location;
    private Spinner startYear,endYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_educ_details);
        Intent intent = getIntent();
        final RequestQueue queue = Volley.newRequestQueue(this);
        final String EMAIL=intent.getStringExtra("EMAIL");
        final String ID = intent.getStringExtra("id");
        String UID = intent.getStringExtra("uid");
        final String URL = "http://139.59.65.145:9090/user/educationdetail/"+ID;
        save=findViewById(R.id.save);
        organisation=findViewById(R.id.organisation);
        degree=findViewById(R.id.degree);
        location=findViewById(R.id.location);
        startYear=findViewById(R.id.startYear);
        endYear=findViewById(R.id.endYear);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(organisation.getText().toString().trim().length()>0&&degree.getText().toString().trim().length()>0&&location.getText().toString().trim().length()>0)
                    update(URL,EMAIL,queue,ID);
                else
                    Toast.makeText(EducDetails.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void update(String URL, final String EMAIL, RequestQueue queue,final String ID) {
        final String Organisation,Degree,Location,StartYear,EndYear;
        Organisation = organisation.getText().toString();
        Degree = degree.getText().toString();
        Location = location.getText().toString();
        StartYear = startYear.getSelectedItem().toString();
        EndYear = endYear.getSelectedItem().toString();

        Map<String,String> params = new HashMap<>();
        params.put("start_year",StartYear);
        params.put("degree",Degree);
        params.put("organisation",Organisation);
        params.put("location",Location);
        params.put("end_year",EndYear);
        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    String uid=data.getString("uid");
                    String id = data.getString("id");
                    Toast.makeText(EducDetails.this,"UID : "+uid+" ID : "+id,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(EducDetails.this,ProfDetails.class);
                    i.putExtra("id",uid);
                    i.putExtra("EMAIL",EMAIL);
                    i.putExtra("uid",id);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(EducDetails.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }




}
