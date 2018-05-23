package com.example.l.gamedb.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Company;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CompaniesAdapter extends RecyclerView.Adapter<CompaniesAdapter.CompaniesViewHolder>{

    List<Company> companies;

    public CompaniesAdapter(List<Company> companies){
        this.companies = companies;
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
