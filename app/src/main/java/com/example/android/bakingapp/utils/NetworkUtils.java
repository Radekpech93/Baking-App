package com.example.android.bakingapp.utils;

import android.net.Uri;

import com.example.android.bakingapp.model.Ingredient;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String COMPLETE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/baking.json";

    private static final String JSON_NAME = "name";
    private static final String JSON_QUANTITY = "quantity";
    private static final String JSON_MEASURE = "measure";
    private static final String JSON_INGREDIENT = "ingredient";
    private static final String JSON_ID = "id";
    private static final String JSON_SHORT_DESCRIPTION = "shortDescription";
    private static final String JSON_DESCRIPTION = "description";
    private static final String JSON_VIDEO_URL = "videoURL";
    private static final String JSON_THUMBNAIL_URL = "thumbnailURL";
    private static final String JSON_SERVINGS = "servings";

    private static final String JSON_ARRAY_INGREDIENTS = "ingredients";
    private static final String JSON_ARRAY_STEPS = "steps";

    public static URL buildQueryUrl(){
        Uri builtUri = Uri.parse(COMPLETE_URL).buildUpon().build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    public static List<Recipe> parseJson(String jsonString)
            throws JSONException {

        JSONArray recipeArray;

        if (jsonString != null) {
            recipeArray = new JSONArray(jsonString);
        } else {
            return null;
        }

        ArrayList<Recipe> recipes = new ArrayList<>();

        for (int r = 0; r < recipeArray.length(); r++) {

            JSONObject recipeObject = recipeArray.getJSONObject(r);

            ArrayList<Ingredient> ingredients = new ArrayList<>();
            ArrayList<Step> steps = new ArrayList<>();

            String name = recipeObject.getString(JSON_NAME);
            String servings = recipeObject.getString(JSON_SERVINGS);

            JSONArray ingredientArray = new JSONArray(recipeObject.getString(JSON_ARRAY_INGREDIENTS));
            JSONArray stepArray = new JSONArray(recipeObject.getString(JSON_ARRAY_STEPS));

            for (int i = 0; i < ingredientArray.length(); i++) {
                Ingredient ingredient = new Ingredient();

                JSONObject ingredientObject = ingredientArray.getJSONObject(i);

                ingredient.setQuantity(ingredientObject.getDouble(JSON_QUANTITY));
                ingredient.setMeasure(ingredientObject.getString(JSON_MEASURE));
                ingredient.setIngredient(ingredientObject.getString(JSON_INGREDIENT));

                ingredients.add(ingredient);
            }

            for (int s = 0; s < stepArray.length(); s++) {
                Step step = new Step();

                JSONObject stepObject = stepArray.getJSONObject(s);

                step.setId(stepObject.getInt(JSON_ID));
                step.setShortDescription(stepObject.getString(JSON_SHORT_DESCRIPTION));
                step.setDescription(stepObject.getString(JSON_DESCRIPTION));
                step.setVideoUrl(stepObject.getString(JSON_VIDEO_URL));
                step.setThumbnailUrl(stepObject.getString(JSON_THUMBNAIL_URL));

                steps.add(step);
            }

            recipes.add(new Recipe(name, ingredients, steps, servings));
        }

        return recipes;
    }


}
