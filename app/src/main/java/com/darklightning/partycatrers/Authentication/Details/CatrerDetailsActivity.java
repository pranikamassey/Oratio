package com.darklightning.partycatrers.Authentication.Details;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.darklightning.partycatrers.R;

public class CatrerDetailsActivity extends AppCompatActivity {

    private String catrerEmail,catrerNo,catrerDetails,catrerName,catrerPrice,catrerLocation;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catrer_details);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            catrerName = extras.getString("catrerName");
            catrerPrice = extras.getString("catrerPrice");
            catrerLocation = extras.getString("catrerLocation");
            catrerEmail = extras.getString("catrerEmail");
            catrerNo = extras.getString("catrerNo");
            catrerDetails = extras.getString("catrerDetails");
            setReqDetailsFields();
        }
    }

    private void setReqDetailsFields()
    {

    }
}
