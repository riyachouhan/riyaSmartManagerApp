package com.ddkcommunity.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ddkcommunity.BuildConfig;
import com.ddkcommunity.R;
import com.ddkcommunity.UserModel;
import com.ddkcommunity.activities.YoutubeActivity;
import com.ddkcommunity.model.Tutorial;
import com.ddkcommunity.utilies.AppConfig;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubeThumbnailLoader;
import com.google.android.youtube.player.YouTubeThumbnailView;

import java.util.List;

public class TutorialAdapter extends RecyclerView.Adapter<TutorialAdapter.MyViewHolder> {

    List<Tutorial> data;
    Activity activity;

    public TutorialAdapter(List<Tutorial> data, Activity activity) {
        this.activity = activity;
        this.data = data;
    }

    public void updateData(List<Tutorial> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_tutorials, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        holder.title.setText(data.get(position).getTutorialTitle());
        holder.date.setText(AppConfig.changeDateInMonth(data.get(position).getCreatedAt()));

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements YouTubeThumbnailView.OnInitializedListener {
        public TextView title, date;
        public ImageView share;

        public YouTubeThumbnailView youTubeThumbnailView;

//        public YouTubeThumbnailLoader youTubeThumbnailLoader;

        public MyViewHolder(View view) {
            super(view);

            title = view.findViewById(R.id.title_TV);
            date = view.findViewById(R.id.time_TV);
            share = view.findViewById(R.id.share_IV);
            youTubeThumbnailView = view.findViewById(R.id.image_view);
            youTubeThumbnailView.initialize(BuildConfig.YOUTUBE_API_KEY, this);

            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AppConfig.copyAndShareLink("Follow the link to become a part of DDK Community ", data.get(getAdapterPosition()).getTutorialVideo(), "Share Video", activity);
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String string = data.get(getAdapterPosition()).getTutorialVideo();
                    UserModel.getInstance().setYoutubeChannel(AppConfig.getYoutubeVideoIdFromUrl(string));
                    activity.startActivity(new Intent(activity, YoutubeActivity.class));
                }
            });
        }

        @Override
        public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
//            this.youTubeThumbnailLoader = youTubeThumbnailLoader;
            youTubeThumbnailLoader.setOnThumbnailLoadedListener(new ThumbnailLoadedListener());
            if (data.size() > getAdapterPosition()) {
                if (getAdapterPosition() > -1) {
                    String url = data.get(getAdapterPosition()).getTutorialVideo();
                    String changeUrl = AppConfig.getYoutubeVideoIdFromUrl(url);
                    youTubeThumbnailLoader.setVideo(changeUrl);
                }
            }
        }

        @Override
        public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {

        }

        private final class ThumbnailLoadedListener implements
                YouTubeThumbnailLoader.OnThumbnailLoadedListener {

            @Override
            public void onThumbnailError(YouTubeThumbnailView arg0, YouTubeThumbnailLoader.ErrorReason arg1) {
                /*AppConfig.showToast(
                        "ThumbnailLoadedListener.onThumbnailError()");*/
            }

            @Override
            public void onThumbnailLoaded(YouTubeThumbnailView arg0, String arg1) {
                /*AppConfig.showToast(
                        "ThumbnailLoadedListener.onThumbnailLoaded()");*/

            }

        }
    }
}
