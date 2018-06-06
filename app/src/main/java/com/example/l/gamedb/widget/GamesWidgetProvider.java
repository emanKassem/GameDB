package com.example.l.gamedb.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.gamedb.MainActivity;
import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.view.GameProfileActivity;

/**
 * Implementation of App Widget functionality.
 */
public class GamesWidgetProvider extends AppWidgetProvider {

    public static Game latestOpenedGame;
    public static Bitmap gameImage;
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.games_widget_provider);
        try {
            views.setImageViewBitmap(R.id.gameImageViewWidget, gameImage);
            views.setTextViewText(R.id.gameNameTextViewWidget, latestOpenedGame.getName());
            Intent appIntent = new Intent(context, GameProfileActivity.class);
            appIntent.putExtra("id", String.valueOf(latestOpenedGame.getId()));
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.gameImageViewWidget, appPendingIntent);
        }catch (Exception e){
            e.printStackTrace();
        }

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    public static void updateGamesWidget(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

