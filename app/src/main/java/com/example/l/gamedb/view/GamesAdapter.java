package com.example.l.gamedb.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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
import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Game;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GamesAdapter extends RecyclerView.Adapter<GamesAdapter.GamesViewHolder>{

    List<Game> games;
    Context context;
    public GamesAdapter(Context context, List<Game> games){
        this.games = games;
        this.context = context;
    }
    @NonNull
    @Override
    public GamesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_item_main, parent, false);
        return new GamesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final GamesViewHolder holder, int position) {

        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            ImageRequest request = new ImageRequest("http:"+games.get(position).getCover().getUrl(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            holder.gameImageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            holder.gameImageView.setImageResource(R.drawable.ic_baseline_error_24px);
                        }
                    });
            requestQueue.add(request);


            holder.gameTextView.setText(games.get(position).getName());
            holder.releaseDateTextView.setText(String.valueOf(games.get(position).getRelease_dates().get(0).getDate()));

            Double rate = games.get(position).getTotalRating();
            if(rate != null) {
                float rateCount = (float) (rate*5/100);
                holder.ratingBar.setRating(rateCount);
                holder.ratingBar.setVisibility(View.VISIBLE);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class GamesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.game_imageview)
        ImageView gameImageView;
        @BindView(R.id.gameNameTextView)
        TextView gameTextView;
        @BindView(R.id.releaseDateTextView)
        TextView releaseDateTextView;
        @BindView(R.id.ratingBar)
        RatingBar ratingBar;
        public GamesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
