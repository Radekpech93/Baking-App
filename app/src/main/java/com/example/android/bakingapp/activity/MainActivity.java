package com.example.android.bakingapp.activity;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {


    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        boolean isTablet = findViewById(R.id.fragment_main_container_tablet) != null;

        MainFragment mainFragment = new MainFragment();

        if (savedInstanceState == null) {

            if (isTablet) {
                Bundle args = new Bundle();
                args.putBoolean(getString(R.string.is_tablet_key), true);
                mainFragment.setArguments(args);

                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_main_container_tablet, mainFragment)
                        .commit();
            } else {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_main_container, mainFragment)
                        .commit();
            }

        }

        getIdlingResource();

    }
}


// TODO: TESTS

