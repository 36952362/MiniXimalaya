package com.jupiter.miniximalaya.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.AlbumDetailActivity;
import com.jupiter.miniximalaya.R;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.InnerHolder> {
    private List<Track> tracks = new ArrayList<>();

    private SimpleDateFormat updateDateFormat = new SimpleDateFormat("YYYY-MM-dd");
    private SimpleDateFormat playDurationFormat = new SimpleDateFormat("mm:ss");

    @NonNull
    @Override
    public AlbumDetailAdapter.InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_album_detail, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailAdapter.InnerHolder holder, int position) {
        holder.setData(tracks.get(position), position);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void setAlbumDetailData(List<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll(tracks);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setData(Track track, int position) {
            TextView itemIndex = itemView.findViewById(R.id.tv_album_item_detail_index);
            TextView itemTitle = itemView.findViewById(R.id.tv_album_item_detail_title);
            TextView itemDate = itemView.findViewById(R.id.tv_album_item_detail_date);
            TextView itemPlayCount = itemView.findViewById(R.id.tv_album_item_play_count);
            TextView itemPlayDuration = itemView.findViewById(R.id.tv_album_item_play_duration);

            itemIndex.setText(position + "");
            itemTitle.setText(track.getTrackTitle());
            String updateDate = updateDateFormat.format(track.getUpdatedAt());
            itemDate.setText(updateDate);
            itemPlayCount.setText(track.getPlayCount() + "");
            String playDuration = playDurationFormat.format(track.getDuration() * 1000);
            itemPlayDuration.setText(playDuration);
        }
    }
}
