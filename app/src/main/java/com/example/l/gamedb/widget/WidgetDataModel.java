package com.example.l.gamedb.widget;


import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;

import com.android.volley.VolleyError;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.data.GameContract;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.model.Parameters;
import com.example.l.gamedb.view.FavoriteFragment;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

public class WidgetDataModel {

    static List<Game> gameList;
    Context context;
    static Cursor cursor;
    String key = BuildConfig.API_KEY;
    APIWrapper wrapper;
    private Gson gson;

    public WidgetDataModel(Context context){
        this.context =context;
        wrapper = new APIWrapper(context, key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        new FavoriteQueryTask().execute();
    }

    public class FavoriteQueryTask extends AsyncTask<Void, Void, Cursor> {

        @Override
        protected Cursor doInBackground(Void... voids) {
            try {
                return context.getContentResolver().query(GameContract.GameEntry.CONTENT_URI,
                        null,
                        null,
                        null,
                        null);

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        @Override
        protected void onPostExecute(Cursor cursor1) {
            cursor = cursor1;
        }
    }

    public  List<Game> createSampleDataForWidget() {
        if(cursor != null){
            int size = cursor.getCount();
            cursor.moveToPosition(0);
            int id = cursor.getInt(
                    cursor.getColumnIndex(GameContract.GameEntry.COLUMN_ID));
            String ids = String.valueOf(id);
            for (int i = 1; i<size; i++){
                cursor.moveToPosition(i);
                id = cursor.getInt(
                        cursor.getColumnIndex(GameContract.GameEntry.COLUMN_ID));
                ids += String.valueOf(id);
            }
            games(ids);
        }
        return gameList;
    }

    private void games(String ids) {
        Parameters parameters = new Parameters().addIds(ids);
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                gameList = Arrays.asList(gson.fromJson(resultString, Game[].class));
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
}
