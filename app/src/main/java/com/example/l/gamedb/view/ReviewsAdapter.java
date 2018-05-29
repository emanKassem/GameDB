package com.example.l.gamedb.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.model.Parameters;
import com.example.l.gamedb.model.Review;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LENOVO on 29/05/2018.
 */

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsViewHolder>{

    List<Review> reviews;
    String key = BuildConfig.API_KEY;
    List<Game> games;
    Context context;
    private APIWrapper wrapper;
    private Gson gson;
    public ReviewsAdapter(List<Review> reviews, Context context){
        this.reviews = reviews;
        this.context = context;
        wrapper = new APIWrapper(context, key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }
    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_recyclerview_item, parent, false);
        return new ReviewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, final int position) {
        try {
            holder.reviewerNameTextView.setText(reviews.get(position).getUserName());
            holder.reviewContentTextView.setText(reviews.get(position).getContent());
            holder.reviewIntroductionTextView.setText(reviews.get(position).getTitle());
            games(reviews.get(position).getGame(), holder.reviewGameImageView, holder.reviewGameNameTextView);
            holder.floatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        Uri webpage = Uri.parse(reviews.get(position).getUrl());
                        Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                        context.startActivity(intent);
                    }catch (ActivityNotFoundException e){
                        Toast.makeText(context, "Page not found", Toast.LENGTH_LONG).show();
                        e.printStackTrace();
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void games(Integer game, final ImageView reviewGameImageView, final TextView reviewGameNameTextView) {
        String id = game.toString();
        Parameters parameters = new Parameters().addIds(id);
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                reviewGameNameTextView.setText(games.get(0).getName());
                try {
                    RequestQueue requestQueue = Volley.newRequestQueue(context);
                    ImageRequest request = new ImageRequest("http:"+games.get(0).getCover().getUrl(),
                            new Response.Listener<Bitmap>() {
                                @Override
                                public void onResponse(Bitmap bitmap) {
                                    reviewGameImageView.setImageBitmap(bitmap);
                                }
                            }, 0, 0, null,
                            new Response.ErrorListener() {
                                public void onErrorResponse(VolleyError error) {
                                    reviewGameImageView.setImageResource(R.drawable.ic_baseline_error_24px);
                                }
                            });
                    requestQueue.add(request);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.review_game_imageview)
        ImageView reviewGameImageView;
        @BindView(R.id.review_game_name_textView)
        TextView reviewGameNameTextView;
        @BindView(R.id.reviewer_name_textview)
        TextView reviewerNameTextView;
        @BindView(R.id.review_introduction_textView)
        TextView reviewIntroductionTextView;
        @BindView(R.id.review_content_textView)
        TextView reviewContentTextView;
        @BindView(R.id.floatingActionButton)
        FloatingActionButton floatingActionButton;
        public ReviewsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
