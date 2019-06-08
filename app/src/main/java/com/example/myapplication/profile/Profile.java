package com.example.myapplication.profile;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.profile.fragments.EducationFragment;
import com.example.myapplication.signUp.ProfDetails;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Profile extends AppCompatActivity {


    private TextView name,email,organisation;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private String Name;
    Bundle  bundle;
    String urle,urlp,urlpro,UIDE,UIDP,UIDPRO;
    private String Mobile;
    private String Location;
    private String Links;
    private ImageView imageView;
    private String Skills;
    private String EOrganisation,Degree,EStartYear,EEndYear,ELocation;
    private String POrganisation,Designation,PStartYear,PEndYear;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        setTitle("Profile");
        tabLayout=findViewById(R.id.tabLayout);
        viewPager=findViewById(R.id.viewpager);
        imageView=findViewById(R.id.profilepic);
        Intent i = getIntent();
        final String EMAIL = i.getStringExtra("EMAIL");
        final String ID = i.getStringExtra("id");
        name=findViewById(R.id.fullName);
        email=findViewById(R.id.email);
        email.setText(EMAIL);
        organisation=findViewById(R.id.organisation);
        bundle=new Bundle();
        Log.d("IDLE",ID);
        final String profilePic = "http://139.59.65.145:9090/user/personaldetail/profilepic/"+ID;
        final String URLP = "http://139.59.65.145:9090/user/personaldetail/"+ID;
        final String  URLE="http://139.59.65.145:9090/user/educationdetail/"+ID;
        final String URLPro = "http://139.59.65.145:9090/user/professionaldetail/"+ID;
        urle="http://139.59.65.145:9090/user/educationdetail/";
        urlp="http://139.59.65.145:9090/user/personaldetail/";
        urlpro="http://139.59.65.145:9090/user/professionaldetail/";


        RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URLP, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                try{
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    Name = data.getString("name");
                    Mobile=data.getString("mobile_no");
                    Location=data.getString("location");
                    Links=data.getString("links");
                    Skills=data.getString("skills");
                    name.setText(Name);
                    bundle.putString("Name",Name);
                    bundle.putString("Mobile",Mobile);
                    bundle.putString("Location",Location);
                    bundle.putString("Links",Links);
                    bundle.putString("Skills",Skills);
                }catch (Exception e){

                }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(jsonObjectRequest);
            JsonObjectRequest jsonObjectRequest1 = new JsonObjectRequest(Request.Method.GET, URLE, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject data = jsonObject.getJSONObject("data");
                        EOrganisation=data.getString("organisation");
                        ELocation = data.getString("location");
                        Degree=data.getString("degree");
                        EStartYear= data.getString("start_year");
                        UIDE=data.getString("id");
                        urle=urle+UIDE;
                        EEndYear = data.getString("end_year");
                        Log.d("EOrganisation",EOrganisation);
                        organisation.setText(EOrganisation+" | "+ELocation);
                        bundle.putString("EOrganisation",data.getString("organisation"));
                        bundle.putString("ELocation",ELocation);
                        bundle.putString("Degree",Degree);
                        bundle.putString("EStartYear",EStartYear);
                        bundle.putString("EEndYear",EEndYear);
                        bundle.putString("id",ID);
                        bundle.putString("email",EMAIL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(jsonObjectRequest1);

            JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.GET, URLPro, null, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response.toString());
                        JSONObject  data = jsonObject.getJSONObject("data");
                        POrganisation = data.getString("organisation");
                        Designation = data.getString("designation");
                        PStartYear = data.getString("start_date");
                        PEndYear = data.getString("end_date");
                        UIDPRO=data.getString("id");
                        urlpro=urlpro+UIDPRO;
                        Log.d("urlpro",urlpro);
                        bundle.putString("POrganisation",POrganisation);
                        bundle.putString("Designation",Designation);
                        bundle.putString("PStartYear",PStartYear);
                        bundle.putString("PEndYear",PEndYear);
                        callAdapter();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });

        queue.add(jsonObjectRequest2);

        ImageRequest imageRequest = new ImageRequest(profilePic, new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                imageView.setImageBitmap(response);
            }
        }, 0, 0, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(imageRequest);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.example_menu,menu);
        return true;
    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                Profile.this);

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
                        Profile.this.finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                startActivity(new Intent(Profile.this, MainActivity.class));
                finish();
                return true;
            case R.id.item2: delete();
                return true;
            case R.id.item4:
                onBackPressed();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void delete() {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE, urle, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
        JsonObjectRequest jsonObjectRequest2 = new JsonObjectRequest(Request.Method.DELETE, urlpro, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    String status = jsonObject.getString("status_message");
                    Toast.makeText(Profile.this,status,Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(Profile.this,MainActivity.class));
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Profile.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest2);
    }


    private void callAdapter(){
        final MyAdapter adapter = new MyAdapter(this,getSupportFragmentManager(),tabLayout.getTabCount(),bundle);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


    }
}
