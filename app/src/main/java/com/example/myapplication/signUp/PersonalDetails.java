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

public class PersonalDetails extends AppCompatActivity {


    Uri f;
    public static final int PICK_IMAGE = 1;
    private Button save;
    private ImageView image;
    private String UID="";
    private EditText name,mobile,location,links,skills;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        save=findViewById(R.id.save);
        image=findViewById(R.id.image);
        name=findViewById(R.id.fullName);
        mobile=findViewById(R.id.mobile);
        location=findViewById(R.id.location);
        links=findViewById(R.id.links);
        skills=findViewById(R.id.skills);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        Intent i = getIntent();
        final String EMAIL=i.getStringExtra("EMAIL");
        final String ID = i.getStringExtra("ID");
        final String URL = "http://139.59.65.145:9090/user/personaldetail/"+ID;
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(name.getText().toString().trim().length()>0 && mobile.getText().toString().trim().length()>0&&location.getText().toString().trim().length()>0&&links.getText().toString().trim().length()>0&&skills.getText().toString().trim().length()>0)
                    update(URL,EMAIL,ID);
                else
                    Toast.makeText(PersonalDetails.this,"Please enter valid data",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void update(final String URL, final String EMAIL,final String ID) {
        final String Name,Mobile,Location,Links,Skills;
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
        JSONObject parameters=new JSONObject(params);
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL, parameters, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONObject data = jsonObject.getJSONObject("data");
                    String uid = data.getString("uid");
                    String id =data.getString("id");
                    Toast.makeText(PersonalDetails.this,"UID :"+uid +" ID : "+id,Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(PersonalDetails.this,EducDetails.class);
                    i.putExtra("id",uid);
                    i.putExtra("uid",id);
                    i.putExtra("EMAIL",EMAIL);
                    startActivity(i);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PersonalDetails.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(jsonObjectRequest);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),f);
            Bitmap lastBitmap = bitmap;
            String image = getStringImage(lastBitmap);
            SendImage(image,ID);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private void SendImage(final String image, String ID) {
        Map<String,String> params = new HashMap<>();
        params.put("photo",image);
        params.put("uid",ID);
        final String URL1 = "http://139.59.65.145:9090/user/personaldetail/pp/post";
        JSONObject jsonObject = new JSONObject(params);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, URL1, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject1 = new JSONObject(response.toString());
                    Toast.makeText(PersonalDetails.this, jsonObject1.getString("status_message"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(PersonalDetails.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private String getStringImage(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }


    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"Select Picture"),PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri filePath = data.getData();
            f=filePath;
            try{
                final Uri uriImage = data.getData();
                final InputStream inputStream = getContentResolver().openInputStream(uriImage);
                final Bitmap imageMap = BitmapFactory.decodeStream(inputStream);
                image.setImageBitmap(imageMap);
            }
            catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(this, "Image was not found", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
