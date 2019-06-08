package com.example.myapplication.update;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.R;
import com.example.myapplication.profile.Profile;
import com.example.myapplication.signUp.ProfDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UpdateProfDetails extends AppCompatActivity {


    private EditText organisation,designation;
    private Spinner start,end;
    TextView endDesc;
    CheckBox checkBox;
    String Organisation,Designation,StartYear,EndYear,ID;
    private Button save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_prof_details);
        save=findViewById(R.id.save);
        organisation=findViewById(R.id.organisation);
        designation=findViewById(R.id.designation);
        checkBox=findViewById(R.id.currentlyWork);
        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        endDesc=findViewById(R.id.endDesc);
        Intent i =getIntent();
        final String EMAIL = i.getStringExtra("email");
        Organisation=i.getStringExtra("Organisation");
        Designation=i.getStringExtra("Designation");
        StartYear=i.getStringExtra("StartYear");
        EndYear=i.getStringExtra("EndYear");
        ID=i.getStringExtra("id");


        organisation.setText(Organisation);
        designation.setText(Designation);

        start.setSelection(getIndex(start,StartYear));
        end.setSelection(getIndex(end,EndYear));

        final RequestQueue queue = Volley.newRequestQueue(this);
        final String URL = "http://139.59.65.145:9090/user/professionaldetail/"+ID;
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(checkBox.isChecked())
                {
                    end.setVisibility(View.GONE);
                    endDesc.setVisibility(View.GONE);
                }
                else
                {
                    end.setVisibility(View.VISIBLE);
                    endDesc.setVisibility(View.VISIBLE);
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(organisation.getText().toString().trim().length()>0&&designation.getText().toString().trim().length()>0)
                    update(URL,EMAIL,queue);
                else
                    Toast.makeText(UpdateProfDetails.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void update(String URL, final String EMAIL, RequestQueue queue) {
        Organisation = organisation.getText().toString();
        Designation = designation.getText().toString();
        StartYear = start.getSelectedItem().toString();
        EndYear = end.getSelectedItem().toString();

        Map<String,String> params = new HashMap<>();
        params.put("end_date",EndYear);
        params.put("organisation",Organisation);
        params.put("designation",Designation);
        params.put("start_date",StartYear);

        JSONObject parameters = new JSONObject(params);

        JsonObjectRequest jsonObjectRequest  = new JsonObjectRequest(Request.Method.PUT, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    String uid=data.getString("uid");
                    String id = data.getString("id");
                    Intent i= new Intent(UpdateProfDetails.this, Profile.class);
                    Toast.makeText(UpdateProfDetails.this,"Updated",Toast.LENGTH_SHORT).show();
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
                Toast.makeText(UpdateProfDetails.this,error.toString(),Toast.LENGTH_SHORT).show();
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
