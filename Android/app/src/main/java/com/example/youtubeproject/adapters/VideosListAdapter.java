package com.example.youtubeproject.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtubeproject.R;
import com.example.youtubeproject.entities.Video;
import com.example.youtubeproject.pages.VideoViewPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VideosListAdapter extends RecyclerView.Adapter<VideosListAdapter.VideoViewHolder> {

    class VideoViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView uploader;
        private final TextView views;

        private final TextView timePassed;
        private final ImageButton videoPic;


        private VideoViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.videoTitle);
            uploader = itemView.findViewById(R.id.videoUploader);
            views = itemView.findViewById(R.id.videoViews);
            timePassed = itemView.findViewById(R.id.videoPassedTime);
            videoPic = itemView.findViewById(R.id.videoPic);
        }


    }

    private final LayoutInflater mInflater;

    private List<Video> videos;

    public VideosListAdapter(Context context) {mInflater = LayoutInflater.from(context);}

    @Override
    public VideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.video_layout, parent, false);
        return new VideoViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(VideoViewHolder holder, int position) {
        if (videos != null) {
            final Video current = videos.get(position);
            holder.title.setText(current.getTitle());
            holder.uploader.setText(current.getPublisher().getUsername() + " . ");
            holder.views.setText(current.get__v() + " views. ");

            String formattedDate = convertDate(current.getUploadDate());
            holder.timePassed.setText(formattedDate);

            String fullThumbnailUrl = "http://10.0.2.2:5001/uploads/" + current.getThumbnailName();
            Glide.with(holder.videoPic.getContext())
                    .load(fullThumbnailUrl)
                    .into(holder.videoPic);
            holder.videoPic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Create an Intent to start ActivityVideoViewPage
                    Context context = v.getContext();
                    Intent intent = new Intent(context, VideoViewPage.class);
                    // Pass additional data if needed
                    String [] data = new String[2];
                    data[0] = current.getId();
                    data[1] = current.getPublisher().getUsername();
                    intent.putExtra("data", data);
                    context.startActivity(intent);
                }
            });
        }
    }


    private String convertDate(String dateString) {
        try {
            // Parse the input date string
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            Date date = inputFormat.parse(dateString);

            // Format the date to the desired output format
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (ParseException e) {
            // Handle the parsing error
            e.printStackTrace();
            // Return the original date string in case of error
            return dateString;
        }
    }


    public void setVideos(List<Video> s){
        videos = s;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(videos != null){
            return videos.size();
        }
        else return 0;
    }

    public List<Video> getVideos() {
        return videos;
    }


}