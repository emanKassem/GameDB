package com.example.l.gamedb.view;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import com.android.volley.VolleyError;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Company;
import com.example.l.gamedb.model.Franchise;
import com.example.l.gamedb.model.Page;
import com.example.l.gamedb.model.Review;
import com.example.l.gamedb.model.Theme;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Game;
import com.example.l.gamedb.model.Parameters;

import butterknife.BindView;
import butterknife.ButterKnife;


public class GamesFragment extends Fragment{

    private APIWrapper wrapper;
    private Gson gson;
    List<Game> games;
    List<Company> companies;
    List<Theme> theme;
    List<Franchise> franchises;
    List<Page> pages;
    List<Review> reviews;

    @BindView(R.id.games_recyclerview)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.adView)
    AdView mAdView;
    InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.games_fragment, container, false);
        ButterKnife.bind(this, view);
        String admobKey = BuildConfig.ADMOB_KEY;
        String key = BuildConfig.API_KEY;
        wrapper = new APIWrapper(getActivity(), key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mInterstitialAd = new InterstitialAd(getActivity());
        mInterstitialAd.setAdUnitId(admobKey);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener(){
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                getActivity().finish();
            }
        });
        mAdView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    Log.d("TAG", "The interstitial wasn't loaded yet.");
                }
            }
        });
        String argument = getArguments().getString("key");
        switch (argument){
            case "games":
                themes("1");
                break;
            case "companies":
                companies();
                break;
            case "franchises":
                franchises();
                break;
            case "pages":
                pages();
                break;
            case "reviews":
                reviews();
                break;
            case "action":
                themes("1");
                break;
            case "historical":
                themes("22");
                break;
            case "horror":
                themes("19");
                break;
            case "drama":
                themes("31");
                break;
            case "mystery":
                themes("43");
                break;
            case "singlePlayer":
                modes("1");
                break;
            case "multiPlayer":
                modes("2");
                break;
            default:
                search(argument);
        }

        return view;
    }

    private void modes(String mode) {
        Parameters parameters = new Parameters().addIds(mode);
        wrapper.gameModes(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                theme = Arrays.asList(gson.fromJson(resultString, Theme[].class));
                int count = theme.get(0).getGames().size();
                if(count>20)
                    count = 20;
                String ids = (theme.get(0).getGames().get(0)).toString();
                for(int i = 1; i<count; i++){
                    ids = ids+","+(theme.get(0).getGames().get(i)).toString();
                }
                games(ids);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void themes(final String themes) {
        Parameters parameters = new Parameters().addIds(themes);
        wrapper.themes(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                theme = Arrays.asList(gson.fromJson(resultString, Theme[].class));
                int count = theme.get(0).getGames().size();
                if(count>20)
                    count = 20;
                String ids = (theme.get(0).getGames().get(0)).toString();
                for(int i = 1; i<count; i++){
                    ids = ids+","+(theme.get(0).getGames().get(i)).toString();
                }
                games(ids);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    private void companies() {
        Parameters parameters = new Parameters();
        wrapper.companies(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                companies = Arrays.asList(gson.fromJson(resultString, Company[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                itemsRecyclerView.setLayoutManager(layoutManager);
                CompaniesAdapter companiesAdapter = new CompaniesAdapter(companies, getActivity());
                itemsRecyclerView.setAdapter(companiesAdapter);
                runLayoutAnimation(itemsRecyclerView);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void franchises(){
        Parameters parameters = new Parameters();
        wrapper.franchises(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                franchises = Arrays.asList(gson.fromJson(resultString, Franchise[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                itemsRecyclerView.setLayoutManager(layoutManager);
                FranchisesAdapter franchisesAdapter = new FranchisesAdapter(franchises, getActivity());
                itemsRecyclerView.setAdapter(franchisesAdapter);
                runLayoutAnimation(itemsRecyclerView);


            }

            @Override
            public void onError(VolleyError error) {

            }
        });

    }

    private void pages(){
        Parameters parameters = new Parameters();
        wrapper.pages(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                pages = Arrays.asList(gson.fromJson(resultString, Page[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                itemsRecyclerView.setLayoutManager(layoutManager);
                PagesAdapter pagesAdapter = new PagesAdapter(pages, getActivity());
                itemsRecyclerView.setAdapter(pagesAdapter);
                runLayoutAnimation(itemsRecyclerView);

            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    private void reviews(){
        Parameters parameters = new Parameters();
        wrapper.reviews(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                reviews = Arrays.asList(gson.fromJson(resultString, Review[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                itemsRecyclerView.setLayoutManager(layoutManager);
                ReviewsAdapter reviewsAdapter = new ReviewsAdapter(reviews, getActivity());
                itemsRecyclerView.setAdapter(reviewsAdapter);
                runLayoutAnimation(itemsRecyclerView);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

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
                itemsRecyclerView.setLayoutManager(layoutManager);
                GamesAdapter gamesAdapter = new GamesAdapter(getActivity(), games);
                itemsRecyclerView.setAdapter(gamesAdapter);
                runLayoutAnimation(itemsRecyclerView);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    public void games(String id) {
        Parameters parameters = new Parameters().addIds(id);
        wrapper.games(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                games = Arrays.asList(gson.fromJson(resultString, Game[].class));
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
                itemsRecyclerView.setLayoutManager(layoutManager);
                GamesAdapter gamesAdapter = new GamesAdapter(getActivity(), games);
                itemsRecyclerView.setAdapter(gamesAdapter);
                runLayoutAnimation(itemsRecyclerView);

            }

            @Override
            public void onError(VolleyError error) {
                error.printStackTrace();
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
