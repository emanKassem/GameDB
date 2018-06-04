package com.example.l.gamedb.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {
    Cursor cursor;
    String key = BuildConfig.API_KEY;
    List<Game> games;
    Context context;
    private APIWrapper wrapper;
    private Gson gson;

    public FavoriteAdapter(Cursor cursor, Context context) {
        this.cursor = cursor;
        this.context = context;
        wrapper = new APIWrapper(context, key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item_main, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        cursor.moveToPosition(position);
        int id = cursor.getInt(
                cursor.getColumnIndex(GameContract.GameEntry.COLUMN_ID));
        game(String.valueOf(id), holder.gameImageView, holder.gameTextView, holder.releaseDateTextView,
                holder.ratingBar,holder.gameCardView);
    }

    private void game(String id, final ImageView gameImageView, final TextView gameTextView,
                      final TextView releaseDateTextView, final RatingBar ratingBar, final CardView gameCardView) {
        Parameters parameters = new Parameters().addIds(id);
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                final Game game = games.get(0);
                gameTextView.setText(game.getName());
                releaseDateTextView.setText(String.valueOf(game.getRelease_dates().get(0).getDate()));
                Double rate = game.getTotalRating();
                if (rate != null) {
                    float rateCount = (float) (rate * 5 / 100);
                    ratingBar.setRating(rateCount);
                    ratingBar.setVisibility(View.VISIBLE);
                }

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                String cover = "http:" + game.getCover().getUrl();
                String largeCover = cover.replaceAll("t_thumb", "t_logo_med");
                ImageRequest request = new ImageRequest(largeCover,
                        new Response.Listener<Bitmap>() {
                            @Override
                            public void onResponse(Bitmap bitmap) {
                                gameImageView.setImageBitmap(bitmap);
                            }
                        }, 0, 0, null,
                        new Response.ErrorListener() {
                            public void onErrorResponse(VolleyError error) {
                                gameImageView.setImageResource(R.drawable.ic_baseline_error_24px);
                            }
                        });
                requestQueue.add(request);

                gameCardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, GameProfileActivity.class);
                        intent.putExtra("id", game.getId().toString());
                        ActivityOptionsCompat options = ActivityOptionsCompat.
                                makeSceneTransitionAnimation((Activity) context, gameImageView, "profile");
                        context.startActivity(intent, options.toBundle());
                    }
                });
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }


    @Override
    public int getItemCount() {
        if (cursor == null) {
            return 0;
        }
        return cursor.getCount();
    }

    public class FavoriteViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.game_imageview)
        ImageView gameImageView;
        @BindView(R.id.gameNameTextView)
        TextView gameTextView;
        @BindView(R.id.releaseDateTextView)
        TextView releaseDateTextView;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        @BindView(R.id.gameCardView)
        CardView gameCardView;
        public FavoriteViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
