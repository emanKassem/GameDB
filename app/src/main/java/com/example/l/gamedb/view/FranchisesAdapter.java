package com.example.l.gamedb.view;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Franchise;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.model.Parameters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FranchisesAdapter extends RecyclerView.Adapter<FranchisesAdapter.FranchisesViewHolder>{

    private List<Franchise> franchises;
    private List<Game> games;
    Context context;
    private APIWrapper wrapper;
    private Gson gson;

    FranchisesAdapter(List<Franchise> franchises, Context context){
        this.franchises = franchises;
        this.context = context;
        String key = BuildConfig.API_KEY;
        wrapper = new APIWrapper(context, key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }
    @NonNull
    @Override
    public FranchisesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.franchises_recyclerview_item, parent, false);
        return new FranchisesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FranchisesViewHolder holder, final int position) {
        holder.franchisesNameTextView.setText(franchises.get(position).getName());
        holder.franchisesLinkTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Uri webpage = Uri.parse(franchises.get(position).getUrl());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    context.startActivity(intent);
                }catch (ActivityNotFoundException e){
                    Toast.makeText(context, context.getString(R.string.page_not_found), Toast.LENGTH_LONG).show();
                    e.printStackTrace();
                }

            }
        });
        holder.franchisesItemCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(franchises.get(position).getGames() != null){
                    holder.franchisesGamesRecyclerview.setVisibility(View.VISIBLE);
                    games(franchises.get(position).getGames(), holder.franchisesGamesRecyclerview);
                }
            }
        });
    }

    private void games(List<Integer> game, final RecyclerView franchisesGamesRecyclerview) {
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
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                franchisesGamesRecyclerview.setLayoutManager(layoutManager);
                CompanyGamesAdapter companyGamesAdapter = new CompanyGamesAdapter(games, context);
                franchisesGamesRecyclerview.setAdapter(companyGamesAdapter);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return franchises.size();
    }

    public class FranchisesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.franchise_name_textView)
        TextView franchisesNameTextView;
        @BindView(R.id.franchise_link_textView)
        TextView franchisesLinkTextView;
        @BindView(R.id.franchise_games_recyclerview)
        RecyclerView franchisesGamesRecyclerview;
        @BindView(R.id.franchises_item_cardview)
        CardView franchisesItemCardView;
        public FranchisesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
