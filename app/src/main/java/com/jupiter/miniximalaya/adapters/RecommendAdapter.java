package com.jupiter.miniximalaya.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jupiter.miniximalaya.R;
import com.squareup.picasso.Picasso;
import com.ximalaya.ting.android.opensdk.model.album.Album;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdapter  extends RecyclerView.Adapter<RecommendAdapter.InnerHolder> {

    private List<Album> albumList = new ArrayList<>();
    private OnRecommendItemClickListener onRecommendItemClickListener = null;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recommend, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final InnerHolder holder, int position) {
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(null != onRecommendItemClickListener){
                        int clickPosition = (int)view.getTag();
                        Album album = albumList.get(clickPosition);
                        onRecommendItemClickListener.onItemClick(clickPosition, album);
                    }
                }
            });
            holder.update(albumList.get(position));
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void updateData(List<Album> albumList) {
        this.albumList.clear();
        this.albumList.addAll(albumList);

        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void update(Album album) {
            ImageView albumCover = itemView.findViewById(R.id.iv_album_cover);
            TextView albumTitle = itemView.findViewById(R.id.tv_album_title);
            TextView albumDesc = itemView.findViewById(R.id.tv_album_desc);
            TextView albumPlayCount = itemView.findViewById(R.id.tv_album_play_count);
            TextView albumTrackCount = itemView.findViewById(R.id.tv_album_track_count);

            Picasso.with(itemView.getContext()).load(album.getCoverUrlLarge()).into(albumCover);
            albumTitle.setText(album.getAlbumTitle());
            albumDesc.setText(album.getAlbumIntro());
            albumPlayCount.setText(album.getPlayCount() + "");
            albumTrackCount.setText(album.getIncludeTrackCount() + "");
        }
    }

    public void setOnRecommendItemClickListener(OnRecommendItemClickListener listener){
        this.onRecommendItemClickListener = listener;
    }

    public interface OnRecommendItemClickListener {
        void onItemClick(int position, Album album);
    }
}
