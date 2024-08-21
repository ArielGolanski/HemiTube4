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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.youtubeproject.R;
import com.example.youtubeproject.entities.SessionManager;
import com.example.youtubeproject.entities.UserVideo;
import com.example.youtubeproject.pages.VideoViewPage;

import com.example.youtubeproject.pages.YouPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserVideoListAdapter extends RecyclerView.Adapter<UserVideoListAdapter.UserVideoViewHolder>{

    class UserVideoViewHolder extends RecyclerView.ViewHolder {

        private final TextView title;
        private final TextView uploader;
        private final TextView views;

        private final TextView timePassed;
        private final ImageButton videoPic;



        private UserVideoViewHolder(View itemView){
            super(itemView);
            title = itemView.findViewById(R.id.videoTitle);
            uploader = itemView.findViewById(R.id.videoUploader);
            views = itemView.findViewById(R.id.videoViews);
            timePassed = itemView.findViewById(R.id.videoPassedTime);
            videoPic = itemView.findViewById(R.id.videoPic);
        }


    }

    private final LayoutInflater mInflater;

    private List<UserVideo> videos;
    private final SessionManager sessionManager = SessionManager.getInstance(); //made it not finale


    public UserVideoListAdapter(Context context) {mInflater = LayoutInflater.from(context);}

    @Override
    public UserVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = mInflater.inflate(R.layout.video_layout, parent, false);
        return new UserVideoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(UserVideoListAdapter.UserVideoViewHolder holder, int position){
        if(videos != null){
            final UserVideo current = videos.get(position);
            holder.title.setText(current.getTitle());
            holder.uploader.setText(sessionManager.getUsernameInPage() + " . ");
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
                    data[1] = sessionManager.getUsernameInPage();
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



    public void setVideos(List<UserVideo> s){
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

    public List<UserVideo> getVideos() {
        return videos;
    }


}
