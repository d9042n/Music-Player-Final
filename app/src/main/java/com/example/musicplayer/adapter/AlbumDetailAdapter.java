package com.example.musicplayer.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.activity.PlayerActivity;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {
    private Context mContext;
    public static ArrayList<Song> mAlbumSongs;

    public AlbumDetailAdapter(Context mContext, ArrayList<Song> mAlbumSongs) {
        this.mContext = mContext;
        this.mAlbumSongs = mAlbumSongs;
    }

    @NonNull
    @Override
    public AlbumDetailAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_items, parent, false);
        return new AlbumDetailAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumDetailAdapter.ViewHolder holder, int position) {
        holder.textViewSongTitle.setText(mAlbumSongs.get(position).getTitle());
        holder.textViewSongArtist.setText(mAlbumSongs.get(position).getArtist());

        byte[] image = getCoverArt(mAlbumSongs.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .circleCrop()
                    .into(holder.imageViewSongImage);
        } else {
            Glide.with(mContext).asBitmap()
                    .load(R.drawable.app_logo)
                    .circleCrop()
                    .into(holder.imageViewSongImage);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("sender", "albumDetails");
                intent.putExtra("position", holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbumSongs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSongTitle, textViewSongArtist;
        ImageView imageViewSongImage, imageViewMore;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSongTitle = itemView.findViewById(R.id.songItemTextViewSongTitle);
            textViewSongArtist = itemView.findViewById(R.id.songItemTextViewSongArtist);
            imageViewSongImage = itemView.findViewById(R.id.songItemImageViewAlbumArtHolder);
            imageViewMore = itemView.findViewById(R.id.songItemImageViewMoreButton);
        }
    }

    private byte[] getCoverArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}
