package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

public class EmptyActivity extends AppCompatActivity implements DetailsFragment.OnFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.empty_activity);
        Bundle dataToPass = getIntent().getExtras();
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(dataToPass);
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout, detailsFragment).commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
