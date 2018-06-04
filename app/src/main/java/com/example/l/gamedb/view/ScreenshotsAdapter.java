package com.example.l.gamedb.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.l.gamedb.R;
import com.example.l.gamedb.model.Url;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by LENOVO on 01/06/2018.
 */

public class ScreenshotsAdapter extends RecyclerView.Adapter<ScreenshotsAdapter.ScreenshotsViewHolder>{
    List<Url> screenshots;
    Context context;
    public ScreenshotsAdapter(List<Url> screenshots, Context context){
        this.screenshots = screenshots;
        this.context = context;
    }
    @NonNull
    @Override
    public ScreenshotsAdapter.ScreenshotsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_screenshots, parent, false);
        return new ScreenshotsViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull final ScreenshotsViewHolder holder, int position) {
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String cover = "http:"+screenshots.get(position).getUrl();
        String largeCover = cover.replaceAll("t_thumb", "t_screenshot_big");
        ImageRequest request = new ImageRequest(largeCover,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        holder.screenshotsImageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        holder.screenshotsImageView.setImageResource(R.drawable.ic_baseline_error_24px);
                    }
                });
        requestQueue.add(request);

    }

    @Override
    public int getItemCount() {
        if(screenshots == null){
            return 0;
        }
        return screenshots.size();
    }

    public class ScreenshotsViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.screenshots_imageView)
        ImageView screenshotsImageView;
        public ScreenshotsViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
