package com.example.android.bakingapp.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

public class BakingWidgetService extends IntentService {

    public static final String ACTION_POPULATE_WIDGET = "com.example.android.bakingapp.action.populate_widget";



    public BakingWidgetService() {
        super("BakingWidgetService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            String ingredients = intent.getStringExtra("ingredients");
            handleActionPopulateWidget(ingredients);
        }

    }

    public static void startActionPopulateWidget(Context context, String ingredients) {
        Intent intent = new Intent(context, BakingWidgetService.class);
        intent.setAction(ACTION_POPULATE_WIDGET);
        intent.putExtra("ingredients", ingredients);
        context.startService(intent);
    }

    private void handleActionPopulateWidget(String ingredients) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, BakingWidget.class));

        BakingWidget.updateBakingWidget(getApplicationContext(), appWidgetManager, appWidgetIds, ingredients);
    }

}
