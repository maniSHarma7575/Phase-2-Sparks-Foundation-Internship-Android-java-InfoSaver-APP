package com.example.myapplication.profile.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.update.UpdatePersonalDetails;

public class PersonalFragment extends Fragment {


    TextView name,mobile,location,links,skills;
    View view;
    Button b;

    public PersonalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_personal, container, false);
        name=view.findViewById(R.id.name);
        mobile=view.findViewById(R.id.mobile);
        location=view.findViewById(R.id.location);
        links=view.findViewById(R.id.links);
        skills=view.findViewById(R.id.skills);
        b=view.findViewById(R.id.button);

        final Bundle args=getArguments();
        final String Name = args.getString("Name");
        final String Mobile = args.getString("Mobile");
        final String Location = args.getString("Location");
        final String Links = args.getString("Links");
        final String Skills = args.getString("Skills");
        final String EMAIL = args.getString("email");
        name.setText(name.getText().toString()+" "+Name);
        mobile.setText(mobile.getText().toString()+" "+Mobile);
        location.setText(location.getText().toString()+" "+Location);
        links.setText(links.getText().toString()+" "+Links);
        skills.setText(skills.getText().toString()+" "+Skills);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = args.getString("id");
                Intent i = new Intent(getActivity(), UpdatePersonalDetails.class);
                i.putExtra("Name",Name);
                i.putExtra("Mobile",Mobile);
                i.putExtra("Links",Links);
                i.putExtra("Skills",Skills);
                i.putExtra("Location",Location);
                i.putExtra("email",EMAIL);
                i.putExtra("id",ID);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }

}