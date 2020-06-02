package com.example.android.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.activity.DetailActivity;
import com.example.android.bakingapp.model.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {

    private static final String TAG = RecipeAdapter.class.getSimpleName();

    private List<Recipe> mRecipes;
    private final Context mContext;

    public RecipeAdapter(Context context, List<Recipe> recipes) {
        mContext = context;
        mRecipes = recipes;
    }


    @NonNull
    @Override
    public RecipeAdapter.RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        int layoutIdForListItem = R.layout.recipe_list_item;
        LayoutInflater inflater = LayoutInflater.from(mContext);

        View view = inflater.inflate(layoutIdForListItem, parent, false);

        return new RecipeHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.RecipeHolder holder, int position) {
        Recipe recipe = mRecipes.get(position);

        holder.recipeName.setText(recipe.getName());
        holder.recipeServings.setText(recipe.getServings() + " servings");
    }

    @Override
    public int getItemCount() {
        if(mRecipes != null) {
            return mRecipes.size();
        } else {
            return 0;
        }
    }

    public void setRecipeData(List<Recipe> recipeData) {
        mRecipes = recipeData;
        notifyDataSetChanged();
    }

    class RecipeHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        private TextView recipeName;
        private TextView recipeServings;

        RecipeHolder(View view) {
            super(view);

            recipeName = view.findViewById(R.id.tv_recipe_name);
            recipeServings = view.findViewById(R.id.tv_recipe_servings);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(mContext, DetailActivity.class);
            int position = getAdapterPosition();

            Recipe clickedRecipe;
            clickedRecipe = mRecipes.get(position);
            intent.putExtra("RecipeKey", clickedRecipe);

            mContext.startActivity(intent);
        }
    }
}
