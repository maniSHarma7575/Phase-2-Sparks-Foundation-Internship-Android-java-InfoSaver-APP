package com.example.myapplication.signUp;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ProfDetails extends AppCompatActivity {


    private EditText organisation,designation;
    private Spinner start,end;
    private Button save;
    TextView endDesc;
    CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prof_details);
        save=findViewById(R.id.save);
        organisation=findViewById(R.id.organisation);
        designation=findViewById(R.id.designation);
        start=findViewById(R.id.start);
        end=findViewById(R.id.end);
        checkBox=findViewById(R.id.currentlyWork);
        endDesc = findViewById(R.id.endDesc);
        final RequestQueue queue = Volley.newRequestQueue(this);
        Intent i = getIntent();
        final String EMAIL = i.getStringExtra("EMAIL");
        final String ID = i.getStringExtra("id");
        final String URL = "http://139.59.65.145:9090/user/professionaldetail/"+ID;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(organisation.getText().toString().trim().length()>0&&designation.getText().toString().trim().length()>0)
                    update(URL,EMAIL,queue);
                else
                    Toast.makeText(ProfDetails.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
            }
        });
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
    }

    private void update(String URL, final String EMAIL, RequestQueue queue) {
        final String Organisation,Designation,Start,End;
        Organisation = organisation.getText().toString();
        Designation = designation.getText().toString();
        if(checkBox.isChecked())
        {
            Start = start.getSelectedItem().toString();
            End = "-";
        }else
        {
            Start = start.getSelectedItem().toString();
            End = end.getSelectedItem().toString();
        }
        Map<String,String> params = new HashMap<>();
        params.put("end_date",End);
        params.put("organisation",Organisation);
        params.put("designation",Designation);
        params.put("start_date",Start);

        JSONObject parameters = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    String id = data.getString("id");
                    String uid = data.getString("uid");
                    Toast.makeText(ProfDetails.this,"UID  : "+uid+" ID : "+id ,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ProfDetails.this,Profile.class);
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
                Toast.makeText(ProfDetails.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);
    }
}
