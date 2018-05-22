package com.example.l.gamedb.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.android.volley.VolleyError;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.model.Parameters;

import butterknife.BindView;
import butterknife.ButterKnife;

import static junit.framework.Assert.fail;

public class GamesFragment extends Fragment{

    private APIWrapper wrapper;
    private Gson gson;
    List<Game> games;

    @BindView(R.id.games_recyclerview)
    RecyclerView gameRecyclerView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.games_fragment, container, false);
        ButterKnife.bind(this, view);
        String key = BuildConfig.API_KEY;;
        wrapper = new APIWrapper(getActivity(), key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        String argument = getArguments().getString("key");
        if(argument.equals("games")) {
            try {
                games();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                search(argument);
            }catch (Exception e){
                e.printStackTrace();
            }

        }

        return view;
    }

    private void search(String argument) {
        Parameters parameters = new Parameters()
                .addSearch(argument)
                .addFields("*");
        wrapper.search(APIWrapper.Endpoint.GAMES, parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                gameRecyclerView.setLayoutManager(layoutManager);
                GamesAdapter gamesAdapter = new GamesAdapter(getActivity(), games);
                gameRecyclerView.setAdapter(gamesAdapter);
                runLayoutAnimation(gameRecyclerView);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void games() {
        Parameters parameters = new Parameters();
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                gameRecyclerView.setLayoutManager(layoutManager);
                GamesAdapter gamesAdapter = new GamesAdapter(getActivity(), games);
                gameRecyclerView.setAdapter(gamesAdapter);
                runLayoutAnimation(gameRecyclerView);
            }

            @Override
            public void onError(VolleyError error) {

            }

        });
    }

    private void runLayoutAnimation(final RecyclerView recyclerView) {
        final Context context = recyclerView.getContext();
        final LayoutAnimationController controller =
                AnimationUtils.loadLayoutAnimation(context, R.anim.layout_animation_fall_down);

        recyclerView.setLayoutAnimation(controller);
        recyclerView.getAdapter().notifyDataSetChanged();
        recyclerView.scheduleLayoutAnimation();
    }
}
