package com.example.l.gamedb.view;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.data.GameContract;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.model.Parameters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.yarolegovich.discretescrollview.DiscreteScrollView;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GameProfileActivity extends AppCompatActivity {

    private APIWrapper wrapper;
    private Gson gson;
    List<Game> games;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.backdrop)
    ImageView backDropImageView;
    @BindView(R.id.fab)
    ImageView fabImageView;
    @BindView(R.id.summery_content_textView)
    ExpandableTextView summeryContentTextView;
    @BindView(R.id.visit_link_textView)
    TextView visitTextView;
    @BindView(R.id.picker)
    DiscreteScrollView pickerScrollView;
    @BindView(R.id.publisher_recyclerview)
    RecyclerView publisherRecyclerView;
    @BindView(R.id.developers_recyclerview)
    RecyclerView developerRecyclerView;
    @BindView(R.id.recommended_games_recyclerView)
    RecyclerView recommendedGamesRecyclerView;
    @BindView(R.id.shareFloatingActionButton)
    FloatingActionButton shareFloatingActionButton;
    @BindView(R.id.GameRatingBar)
    RatingBar gameRatingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_profile);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        String key = BuildConfig.API_KEY;
        wrapper = new APIWrapper(this, key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        final String id = getIntent().getStringExtra("id");
        if(checkIfGameInDb(Integer.valueOf(id))){
            fabImageView.setImageResource(R.drawable.ic_baseline_favorite_24px);
        }
        fabImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onclickAddGame(Integer.valueOf(id));
            }
        });
        game(id);
    }

    private void game(String id) {
        Parameters parameters = new Parameters().addIds(id);
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                final Game game = games.get(0);
                RequestQueue requestQueue = Volley.newRequestQueue(GameProfileActivity.this);
                String cover = "http:"+game.getCover().getUrl();
                String largeCover = cover.replaceAll("t_thumb", "t_screenshot_big");
                ImageRequest request = new ImageRequest(largeCover,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                backDropImageView.setImageBitmap(bitmap);
                            }
                        }, 0, 0, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                backDropImageView.setImageResource(R.drawable.ic_baseline_error_24px);
                            }
                        });
                requestQueue.add(request);
                summeryContentTextView.setText(game.getSummary());
                visitTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Uri webpage = Uri.parse(game.getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        startActivity(intent);
                    }
                });
                ScreenshotsAdapter screenshotsAdapter = new ScreenshotsAdapter(game.getScreenshots(), GameProfileActivity.this);
                pickerScrollView.setAdapter(screenshotsAdapter);
                Double rate = game.getTotalRating();
                if(rate != null) {
                    float rateCount = (float) (rate*5/100);
                    gameRatingBar.setRating(rateCount);
                    gameRatingBar.setVisibility(View.VISIBLE);
                }
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GameProfileActivity.this, LinearLayoutManager.VERTICAL, false);
                developerRecyclerView.setLayoutManager(layoutManager);
                PeopleAdapter developers = new PeopleAdapter(game.getDevelopers(), R.layout.game_people, GameProfileActivity.this);
                developerRecyclerView.setAdapter(developers);
                RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(GameProfileActivity.this, LinearLayoutManager.VERTICAL, false);
                PeopleAdapter developers2 = new PeopleAdapter(game.getPublishers(), R.layout.game_people, GameProfileActivity.this);
                publisherRecyclerView.setLayoutManager(layoutManager2);
                publisherRecyclerView.setAdapter(developers2);
                shareFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, game.getUrl());
                        sendIntent.setType("text/plain");
                        startActivity(sendIntent);
                    }
                });
                game2(game.getGames());
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void game2(List<Integer> game) {
        String ids = game.get(0).toString();
        for (int i = 1; i < game.size(); i++) {
            ids = ids + "," + game.get(i);
        }
        Parameters parameters = new Parameters().addIds(ids);
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(GameProfileActivity.this, LinearLayoutManager.HORIZONTAL, false);
                recommendedGamesRecyclerView.setLayoutManager(layoutManager);
                CompanyGamesAdapter companyGamesAdapter = new CompanyGamesAdapter(games, GameProfileActivity.this);
                recommendedGamesRecyclerView.setAdapter(companyGamesAdapter);

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }
    private boolean checkIfGameInDb(int id) {
        Cursor cursor = getContentResolver().query(
                GameContract.GameEntry.CONTENT_URI, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                int gameId = cursor.getInt(
                        cursor.getColumnIndex(GameContract.GameEntry.COLUMN_ID));
                if (gameId == id) {
                    return true;
                }
            }
        }
        if (cursor != null) {
            cursor.close();
        }
        return false;
    }
    private void onclickAddGame(int id){
        boolean b = checkIfGameInDb(id);
        if(b != true) {
            insertGameIntoDb(id);
            fabImageView.setImageResource(R.drawable.ic_baseline_favorite_24px);
        }
        else{
            fabImageView.setImageResource(R.drawable.ic_baseline_favorite_border_24px);
            deleteGameFromDb(id);
        }
    }

    private void deleteGameFromDb(int id) {
        String[] selectionArg = {String.valueOf(id)};
        getContentResolver().delete(GameContract.GameEntry.CONTENT_URI, GameContract.GameEntry.COLUMN_ID+"=?", selectionArg);
    }

    private void insertGameIntoDb(int id) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(GameContract.GameEntry.COLUMN_ID, id);
        getContentResolver().insert(GameContract.GameEntry.CONTENT_URI, contentValues);
    }

}
