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
import com.example.myapplication.update.UpdateProfDetails;

public class ProfessionalFragment extends Fragment {


    View view;
    TextView organisation,designation,startYear,endYear;
    Button b;

    public ProfessionalFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_professional, container, false);
        organisation=view.findViewById(R.id.organisation);
        designation=view.findViewById(R.id.designation);
        startYear=view.findViewById(R.id.startYear);
        endYear=view.findViewById(R.id.endYear);
        b= view.findViewById(R.id.button);

        final Bundle bundle = getArguments();
        final String Organisation = bundle.getString("POrganisation");
        final String Designation = bundle.getString("Designation");
        final String StartYear = bundle.getString("PStartYear");
        final String EndYear = bundle.getString("PEndYear");
        final String EMAIL = bundle.getString("email");


        organisation.setText(organisation.getText().toString()+" "+Organisation);
        designation.setText(designation.getText().toString()+" "+Designation);
        startYear.setText(startYear.getText().toString()+" "+StartYear);
        endYear.setText(endYear.getText().toString()+" "+EndYear);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID = bundle.getString("id");
                Intent i = new Intent(getActivity(), UpdateProfDetails.class);
                i.putExtra("Organisation",Organisation);
                i.putExtra("Designation",Designation);
                i.putExtra("StartYear",StartYear);
                i.putExtra("EndYear",EndYear);
                i.putExtra("email",EMAIL);
                i.putExtra("id",ID);
                startActivity(i);
                getActivity().finish();
            }
        });

        return view;
    }

}