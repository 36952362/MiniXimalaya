package com.jupiter.miniximalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.jupiter.miniximalaya.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.track.Track;

import java.util.ArrayList;
import java.util.List;

public class TrackPlayPageAdapter extends PagerAdapter {
    private List<Track> tracks = new ArrayList<>();


    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.item_play_cover, container, false);
        container.addView(view);
        ImageView imageView = view.findViewById(R.id.iv_play_cover);
        Track track = tracks.get(position);
        String playCoverImage = track.getCoverUrlLarge();
        Picasso.with(view.getContext()).load(playCoverImage).into(imageView);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return tracks.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    public void setData(List<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll(tracks);
        notifyDataSetChanged();
    }
}
