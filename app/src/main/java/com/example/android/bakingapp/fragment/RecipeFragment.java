package com.example.android.bakingapp.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.DetailActivity;
import com.example.android.bakingapp.adapter.StepAdapter;
import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.widget.BakingWidgetService;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.bakingapp.model.Ingredient.ingredientsToString;

public class RecipeFragment extends Fragment {


    private Recipe mRecipe;
    LinearLayoutManager layoutManager;
    ArrayList<Ingredient> ingredients;
    ArrayList<Step> steps;
    String ingredientsString;

    @BindView(R.id.tv_ingredients)
    TextView ingredientsText;
    @BindView(R.id.rv_steps)
    RecyclerView recyclerView;

    Context mContext;
    Parcelable stepState;


    public RecipeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.recipe_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            mRecipe = savedInstanceState.getParcelable(getString(R.string.recipe_key));
            ingredientsString = savedInstanceState.getString(getString(R.string.ingredients_string_key));
            stepState = savedInstanceState.getParcelable(getString(R.string.rv_state_key));
        } else {
            if (getArguments() != null) {
                mRecipe = getArguments().getParcelable(getString(R.string.recipe_key));
            }
        }

        if (mRecipe != null) {
            ingredients = mRecipe.getIngredients();
            steps = mRecipe.getSteps();
        }

        ingredientsString = ingredientsToString(ingredients);
        ingredientsText.setText(ingredientsString);

        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);

        //This allows the entire screen to scroll, not just the recycler view
        recyclerView.setNestedScrollingEnabled(false);

        StepAdapter stepAdapter = new StepAdapter(getActivity(), steps, (DetailActivity) getActivity());
        recyclerView.setAdapter(stepAdapter);

        //Opening the recipe automatically adds the ingredients to the widget
        BakingWidgetService.startActionPopulateWidget(getContext(), ingredientsString);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(getString(R.string.recipe_key), mRecipe);

        outState.putString(getString(R.string.ingredients_string_key), ingredientsString);
        outState.putParcelable(getString(R.string.rv_state_key), recyclerView.getLayoutManager().onSaveInstanceState());
    }

    @Override
    public void onResume() {
        super.onResume();

        if (stepState != null) {
            recyclerView.getLayoutManager().onRestoreInstanceState(stepState);
            layoutManager.onRestoreInstanceState(stepState);
        }
    }

}
