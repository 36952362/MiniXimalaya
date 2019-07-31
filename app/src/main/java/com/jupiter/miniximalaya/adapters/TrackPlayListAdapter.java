package com.jupiter.miniximalaya.adapters;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.jupiter.miniximalaya.base.BaseApplication;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackPlayListAdapter extends RecyclerView.Adapter<TrackPlayListAdapter.InnerHolder>{

    private List<Track> tracks = new ArrayList<>();
    private int playingIndex = 0;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_play_list, parent, false);

        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        Track track = tracks.get(position);
        TextView playListTitle = holder.itemView.findViewById(R.id.tv_play_list_title);
        playListTitle.setText(track.getTrackTitle());

        ImageView playListIcon = holder.itemView.findViewById(R.id.iv_play_list_icon);

        if(position == playingIndex){
            playListIcon.setVisibility(View.VISIBLE);
            playListTitle.setTextColor(BaseApplication.getAppContext().getResources().getColor(R.color.subscribe_color));
        }
        else{
            playListIcon.setVisibility(View.GONE);
            playListTitle.setTextColor(BaseApplication.getAppContext().getResources().getColor(R.color.play_list_title_color));
        }

    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void setData(List<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll(tracks);
        notifyDataSetChanged();
    }

    public void setCurrentPlayIndex(int currentIndex) {

        playingIndex = currentIndex;
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
