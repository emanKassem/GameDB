package com.example.l.gamedb.view;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.l.gamedb.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class FavoriteFragment extends Fragment{
    private Cursor mCursor;
    @BindView(R.id.games_recyclerview)
    RecyclerView itemsRecyclerView;

    public static FavoriteFragment createYourFragmentWithCursor( Cursor cursor ) {
        FavoriteFragment fragment = new FavoriteFragment();
        fragment.setCursor( cursor );
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.games_fragment, container, false);
        ButterKnife.bind(this, view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        itemsRecyclerView.setLayoutManager(layoutManager);
        FavoriteAdapter favoriteAdapter = new FavoriteAdapter(mCursor, getActivity());
        itemsRecyclerView.setAdapter(favoriteAdapter);
        return view;
    }
    protected Cursor getCursor() {
        return mCursor;
    }

    private void setCursor( Cursor cursor ) {
        mCursor = cursor;
    }

}
