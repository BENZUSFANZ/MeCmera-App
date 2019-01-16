package com.example.benz.mecamera;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.support.v7.widget.Toolbar;


import com.example.benz.mecamera.MyStore.OtherFragment;
import com.luseen.simplepermission.permissions.PermissionFragment;


/**
 * A simple {@link Fragment} subclass.
 */
public class AboutFragment extends PermissionFragment {


    public AboutFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        /////////// button back in fragment ////////////////////
        final Toolbar tbAbout = (Toolbar) view.findViewById(R.id.tbAbout);
        AppCompatActivity appCompatActivity = (AppCompatActivity)getActivity();
        appCompatActivity.setSupportActionBar(tbAbout);

        appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appCompatActivity.getSupportActionBar().setTitle("");

        tbAbout.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction fr = getFragmentManager().beginTransaction();
                fr.replace(R.id.mainFrame, new OtherFragment()); //fragment class
                fr.commit();

            }
        });
        /////////////////////////////////////////////////////////


        return view;
    }

}
