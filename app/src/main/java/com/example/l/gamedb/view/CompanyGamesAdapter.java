package com.example.l.gamedb.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Game;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LENOVO on 27/05/2018.
 */

public class CompanyGamesAdapter extends RecyclerView.Adapter<CompanyGamesAdapter.CompanyGamesHolder>{

    List<Game> games;
    Context context;
    public CompanyGamesAdapter(List<Game> games, Context context){
        this.games = games;
        this.context = context;
    }

    @NonNull
    @Override
    public CompanyGamesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.company_games_item, parent, false);
        return new CompanyGamesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CompanyGamesHolder holder, int position) {
        holder.companyGameName.setText(games.get(position).getName());
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        ImageRequest request = new ImageRequest("http:"+games.get(position).getCover().getUrl(),
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.companyGameImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        holder.companyGameImageView.setImageResource(R.drawable.ic_baseline_error_24px);
                    }
                });
        requestQueue.add(request);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    public class CompanyGamesHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.company_game_imageView)
        ImageView companyGameImageView;
        @BindView(R.id.company_game_textView)
        TextView companyGameName;
        public CompanyGamesHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
