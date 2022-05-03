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

import com.example.musicplayer.R;
import com.example.musicplayer.activity.PlayerActivity;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Song> mSongs;


    public SongAdapter(Context mContext, ArrayList<Song> mSongs) {
        this.mContext = mContext;
        this.mSongs = mSongs;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.song_items, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewSongTitle.setText(mSongs.get(position).getTitle());
        holder.textViewSongArtist.setText(mSongs.get(position).getArtist());

        // Load album art
//        byte[] image = getAlbumArt(mSongs.get(position).getPath());
//        if (image != null) {
//            Glide.with(mContext).asBitmap()
//                    .load(image)
//                    .into(holder.imageViewSongImage);
//        } else {
//            Glide.with(mContext)
//                    .load(R.drawable.menu_song_icon)
//                    .into(holder.imageViewSongImage);
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PlayerActivity.class);
                intent.putExtra("position", holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return mSongs.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewSongTitle, textViewSongArtist;
        ImageView imageViewSongImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSongTitle = itemView.findViewById(R.id.textViewSongTitle);
            textViewSongArtist = itemView.findViewById(R.id.textViewSongArtist);
            imageViewSongImage = itemView.findViewById(R.id.imageViewAlbumArtHolder);

        }
    }


    private byte[] getAlbumArt(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        byte[] art = retriever.getEmbeddedPicture();
        retriever.release();
        return art;
    }
}