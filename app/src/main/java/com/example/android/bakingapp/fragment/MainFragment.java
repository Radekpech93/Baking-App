package com.example.android.bakingapp.fragment;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.adapter.RecipeAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Recipe>> {

    private static final String TAG = MainFragment.class.getSimpleName();
    private static final int RECIPE_SEARCH_LOADER = 7;

    @BindView(R.id.rv_recipes)
    RecyclerView recyclerView;
    @BindView(R.id.tv_error_no_internet)
    TextView noInternetTextView;

    private RecipeAdapter recipeAdapter;
    Context mContext;
    private boolean isTablet;

    public MainFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.main_fragment, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ButterKnife.bind(this, view);

        if (getArguments() != null) {
            isTablet = getArguments().getBoolean(getString(R.string.is_tablet_key));
        }

        if (isTablet) {
            GridLayoutManager gridLayoutManager = new GridLayoutManager(mContext, 2);
            recyclerView.setLayoutManager(gridLayoutManager);
        } else {
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext);
            recyclerView.setLayoutManager(linearLayoutManager);
        }

        ArrayList<Recipe> mRecipes = new ArrayList<>();
        recipeAdapter = new RecipeAdapter(getActivity(), mRecipes);
        recyclerView.setAdapter(recipeAdapter);

        boolean internetConnected = checkInternetConnection();

        if (internetConnected) {
            getLoaderManager().initLoader(RECIPE_SEARCH_LOADER, null, this);
        }

    }

    @Override
    public Loader<List<Recipe>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<Recipe>>(getContext()) {

            @Override
            protected void onStartLoading() {
                super.onStartLoading();
                forceLoad();
            }

            @Override
            public List<Recipe> loadInBackground() {
                URL recipeUrl = NetworkUtils.buildQueryUrl();

                try {
                    String recipeJsonResponse = NetworkUtils.getResponseFromHttpUrl(recipeUrl);

                    return NetworkUtils.parseJson(recipeJsonResponse);

                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<Recipe>> loader, List<Recipe> recipes) {
        if (recipes != null) {
            recipeAdapter.setRecipeData(recipes);
        } else {
            Log.d(TAG, "Unable to get recipes (onLoadFinished recipes == null)");
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<List<Recipe>> loader) {

    }

    private boolean checkInternetConnection() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        if (cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnectedOrConnecting()) {
            showRecipes();
            return true;
        } else {
            showErrorNoInternet();
            return false;
        }
    }

    private void showRecipes() {
        noInternetTextView.setVisibility(View.INVISIBLE);
        recyclerView.setVisibility(View.VISIBLE);
    }

    private void showErrorNoInternet() {
        recyclerView.setVisibility(View.INVISIBLE);
        noInternetTextView.setVisibility(View.VISIBLE);
    }

}
