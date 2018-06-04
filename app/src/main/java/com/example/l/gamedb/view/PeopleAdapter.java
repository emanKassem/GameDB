package com.example.l.gamedb.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.l.gamedb.BuildConfig;
import com.example.l.gamedb.R;
import com.example.l.gamedb.callback.onSuccessCallback;
import com.example.l.gamedb.model.APIWrapper;
import com.example.l.gamedb.model.Parameters;
import com.example.l.gamedb.model.Person;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LENOVO on 01/06/2018.
 */

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>{

    List<Integer> people;
    Integer resource;
    String key = BuildConfig.API_KEY;
    List<Person> personList;
    Context context;
    private APIWrapper wrapper;
    private Gson gson;

    public PeopleAdapter(List<Integer> people, Integer resource, Context context){
        this.people = people;
        this.resource = resource;
        this.context = context;
        wrapper = new APIWrapper(context, key);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setDateFormat("M/d/yy hh:mm a");
        gson = gsonBuilder.create();
    }
    @NonNull
    @Override
    public PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new PeopleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleViewHolder holder, int position) {
        people(people.get(position), holder.peopleTextView);
    }

    private void people(Integer integer, final TextView peopleTextView) {
        Parameters parameters = new Parameters().addIds(integer.toString());
        wrapper.people(parameters, new onSuccessCallback() {
            @Override
            public void onSuccess(JSONArray result) {
                String resultString = result.toString();
                personList = Arrays.asList(gson.fromJson(resultString, Person[].class));
                peopleTextView.setText(personList.get(0).getName());
            }

            @Override
            public void onError(VolleyError error) {

            }
        });
    }

    @Override
    public int getItemCount() {
        if(people == null){
            return 0;
        }
        return people.size();
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.people_textView)
        TextView peopleTextView;
        public PeopleViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
