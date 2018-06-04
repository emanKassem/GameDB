package com.example.l.gamedb.widget;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Game;

import java.util.List;

public class ListViewWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        WidgetDataModel widgetDataModel = new WidgetDataModel(getApplicationContext());

        return new AppWidgetListView(this.getApplicationContext(), widgetDataModel.createSampleDataForWidget());
    }

    class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory{

        List<Game> gameList;
        Context context;
        public AppWidgetListView(Context context, List<Game> gameList){
            this.gameList = gameList;
            this.context = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            if (gameList != null) {
                return gameList.size();
            }
            return 0;
        }

        @Override
        public RemoteViews getViewAt(int i) {
            final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.list_item_widget);
            views.setTextViewText(R.id.gameNameTextViewWidget, gameList.get(i).getName());
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            String cover = "http:" + gameList.get(i).getCover().getUrl();
            String largeCover = cover.replaceAll("t_thumb", "t_logo_med");
            ImageRequest request = new ImageRequest(largeCover,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            views.setImageViewBitmap(R.id.gameImageViewWidget, bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            views.setImageViewResource(R.id.gameImageViewWidget, R.drawable.ic_baseline_error_24px);
                        }
                    });
            requestQueue.add(request);
            Intent fillInIntent = new Intent();
            fillInIntent.putExtra("id", gameList.get(i).getId());
            views.setOnClickFillInIntent(R.id.parentView, fillInIntent);
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
