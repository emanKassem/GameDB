package com.example.l.gamedb.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Company;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.model.Parameters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder>{

    List<Company> companies;
    String key = BuildConfig.API_KEY;
    private APIWrapper wrapper;
    private Gson gson;
    List<Game> games;
    Context context;

    public CompaniesAdapter(List<Company> companies, Context context){
        this.companies = companies;
        this.context = context;
        wrapper = new APIWrapper(context, key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }
    @NonNull
    @Override
    public CompaniesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.companies_recyclerview_item, parent,false);
        return new CompaniesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CompaniesViewHolder holder, int position) {
        holder.companyNameTextView.setText(companies.get(position).getName());
        games(companies.get(position).getDeveloped(), holder.companyGamesRecyclerView);

    }

    private void games(List<Integer> developed, final RecyclerView companyGamesRecyclerView) {
        String ids = developed.get(0).toString();
        for (int i = 1; i<developed.size(); i++){
            ids = ids + ","+developed.get(i);
        }
        Parameters parameters = new Parameters().addIds(ids);
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
                companyGamesRecyclerView.setLayoutManager(layoutManager);
                CompanyGamesAdapter companyGamesAdapter = new CompanyGamesAdapter();
                companyGamesRecyclerView.setAdapter(companyGamesAdapter);

            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return companies.size();
    }

    public class CompaniesViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.company_name_textView)
        TextView companyNameTextView;
        @BindView(R.id.company_games_recyclerview)
        RecyclerView companyGamesRecyclerView;
        @BindView(R.id.expandImageView)
        ImageView expandImageView;
        public CompaniesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
