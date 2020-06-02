package com.example.android.bakingapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.fragment.RecipeFragment;
import com.example.android.bakingapp.fragment.StepFragment;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import java.util.List;

public class DetailActivity extends AppCompatActivity implements StepAdapter.OnStepClickListener{

    Recipe recipe;
    Boolean isTablet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        recipe = intent.getParcelableExtra(getString(R.string.recipe_key));

        isTablet = findViewById(R.id.fragment_step_container_tablet) != null;

        if (savedInstanceState == null) {

            RecipeFragment recipeFragment = new RecipeFragment();

            Bundle args = new Bundle();
            args.putParcelable(getString(R.string.recipe_key), recipe);
            recipeFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_recipe_container, recipeFragment)
                    .commit();

            if (isTablet) {

                StepFragment stepFragment = new StepFragment();

                Step firstStep = recipe.getSteps().get(0);

                Bundle argsTablet = new Bundle();
                argsTablet.putParcelable(getString(R.string.step_key), firstStep);
                stepFragment.setArguments(argsTablet);

                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_step_container_tablet, stepFragment)
                        .commit();
            }

        }
    }

    public void onListItemClick(List<Step> steps, int position) {
        Step clickedStep = steps.get(position);

        StepFragment stepFragment = new StepFragment();

        Bundle args = new Bundle();
        args.putParcelable(getString(R.string.step_key), clickedStep);
        stepFragment.setArguments(args);

        if(isTablet) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_step_container_tablet, stepFragment)
                    .commit();
        } else {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_recipe_container, stepFragment)
                    .addToBackStack(null)
                    .commit();
        }
    }


}
