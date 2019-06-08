package com.example.myapplication.update;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.profile.Profile;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateEducDetails extends AppCompatActivity {


    String Organisation,ID,Location,StartYear,EndYear,Degree;
    EditText organisation,degree,location;
    Spinner startYear,endYear;
    Button save;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_educ_details);

        final RequestQueue queue = Volley.newRequestQueue(this);

        Intent i = getIntent();
        ID = i.getStringExtra("id");
        Organisation = i.getStringExtra("Organisation");
        Degree = i.getStringExtra("Degree");
        Location = i.getStringExtra("Location");
        StartYear = i.getStringExtra("StartYear");
        EndYear = i.getStringExtra("EndYear");


        organisation=findViewById(R.id.organisation);
        degree = findViewById(R.id.degree);
        location = findViewById(R.id.location);
        startYear=findViewById(R.id.startYear);
        endYear=findViewById(R.id.endYear);
        save = findViewById(R.id.save);

        organisation.setText(Organisation);
        degree.setText(Degree);
        location.setText(Location);

        startYear.setSelection(getIndex(startYear,StartYear));
        endYear.setSelection(getIndex(endYear,EndYear));


        final String URL = "http://139.59.65.145:9090/user/educationdetail/"+ID;
        final String EMAIL = i.getStringExtra("email");

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(organisation.getText().toString().trim().length()>0&&degree.getText().toString().trim().length()>0&&location.getText().toString().trim().length()>0)
                        update(URL,EMAIL,queue);
                else
                    Toast.makeText(UpdateEducDetails.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void update(String URL, final String EMAIL, RequestQueue queue) {
        Organisation=organisation.getText().toString();
        Degree = degree.getText().toString();
        Location= location.getText().toString();
        StartYear = startYear.getSelectedItem().toString();
        EndYear = endYear.getSelectedItem().toString();

        Map<String,String> params = new HashMap<>();
        params.put("start_year",StartYear);
        params.put("degree",Degree);
        params.put("organisation",Organisation);
        params.put("location",Location);
        params.put("end_year",EndYear);
        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    String uid=data.getString("uid");
                    String id = data.getString("id");
                    Intent i= new Intent(UpdateEducDetails.this,Profile.class);
                    Toast.makeText(UpdateEducDetails.this,"Updated",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateEducDetails.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
        private int getIndex(Spinner spinner, String myString){
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }
}
