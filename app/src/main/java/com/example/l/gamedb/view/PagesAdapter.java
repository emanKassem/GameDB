package com.example.l.gamedb.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Page;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.PagesViewHolder>{

    List<Page>pages;
    Context context;
    public PagesAdapter(List<Page>pages, Context context){
        this.pages = pages;
        this.context = context;
    }
    @NonNull
    @Override
    public PagesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pages_recyclerview_item, parent, false);
        return new PagesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final PagesViewHolder holder, final int position) {
        try {
            RequestQueue requestQueue = Volley.newRequestQueue(context);
            ImageRequest request = new ImageRequest("http:"+pages.get(position).getLogo().getUrl(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            holder.pageGameImageView.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            holder.pageGameImageView.setImageResource(R.drawable.ic_baseline_error_24px);
                        }
                    });
            requestQueue.add(request);
            holder.pageGameTextView.setText(pages.get(position).getName());
            holder.descriptionTextView.setText(pages.get(position).getDescription());
            holder.youtubeFloatingActionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Uri webpage = Uri.parse(pages.get(position).getYoutube());
                    Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
                    context.startActivity(intent);
                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public class PagesViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.page_game_imageview)
        ImageView pageGameImageView;
        @BindView(R.id.page_game_name_textView)
        TextView pageGameTextView;
        @BindView(R.id.youtube_fab)
        ImageView youtubeFloatingActionButton;
        @BindView(R.id.description_textView)
        TextView descriptionTextView;
        public PagesViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
