package com.example.l.gamedb.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by LENOVO on 27/05/2018.
 */

public class CompanyGamesAdapter extends RecyclerView.Adapter<CompanyGamesAdapter.CompanyGamesHolder>{


    @NonNull
    @Override
    public CompanyGamesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyGamesHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CompanyGamesHolder extends RecyclerView.ViewHolder{

        public CompanyGamesHolder(View itemView) {
            super(itemView);
        }
    }
}
