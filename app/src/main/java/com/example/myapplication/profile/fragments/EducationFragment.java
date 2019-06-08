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
import com.example.myapplication.update.UpdateEducDetails;

public class EducationFragment extends Fragment {

    TextView organisation,degree,location,startYear,endYear;
    View view;
    Button b;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_education, container, false);
        organisation=view.findViewById(R.id.organisation);
        degree=view.findViewById(R.id.degree);
        location=view.findViewById(R.id.location);
        startYear=view.findViewById(R.id.startYear);
        endYear=view.findViewById(R.id.endYear);
        b=view.findViewById(R.id.button);
        final Bundle args = getArguments();
        final String Organisation = args.getString("EOrganisation");
        final String Degree =args.getString("Degree");
        final String Location = args.getString("ELocation");
        final String StartYear = args.getString("EStartYear");
        final String EndYear = args.getString("EEndYear");
        final String EMAIL = args.getString("email");
        organisation.setText(organisation.getText()+" "+Organisation);
        degree.setText(degree.getText().toString()+" "+Degree);
        location.setText(location.getText().toString()+" "+Location);
        startYear.setText(startYear.getText().toString()+" "+StartYear);
        endYear.setText(endYear.getText().toString()+" "+EndYear);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ID= args.getString("id");
                Intent i = new Intent(getActivity(), UpdateEducDetails.class);
                i.putExtra("id",ID);
                i.putExtra("Organisation",Organisation);
                i.putExtra("Degree",Degree);
                i.putExtra("Location",Location);
                i.putExtra("StartYear",StartYear);
                i.putExtra("EndYear",EndYear);
                i.putExtra("email",EMAIL);
                startActivity(i);
                getActivity().finish();
            }
        });
        return view;
    }

}