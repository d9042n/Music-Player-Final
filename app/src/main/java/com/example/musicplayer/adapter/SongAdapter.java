package com.example.musicplayer.adapter;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicplayer.R;
import com.example.musicplayer.activity.PlayerActivity;
import com.example.musicplayer.model.Song;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
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

        byte[] image = getCoverArt(mSongs.get(position).getPath());
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
                intent.putExtra("position", holder.getAdapterPosition());
                mContext.startActivity(intent);
            }
        });

        holder.imageViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(mContext, view);
                popupMenu.getMenuInflater().inflate(R.menu.song_items_menu, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener((menuItem) -> {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_song_item_more:
                            Toast.makeText(mContext, "Delete Button Clicked!", Toast.LENGTH_SHORT).show();
                            deleteSong(holder.getAdapterPosition(), view);
                            break;
                    }
                    return true;
                });
            }
        });
    }


    private void deleteSong(int position, View view) {
        Uri contentUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                Long.parseLong(mSongs.get(position).getId()));
        File songFile = new File(mSongs.get(position).getPath());

        boolean isDeleted = songFile.delete();
        if (isDeleted) {
            mContext.getContentResolver().delete(contentUri, null, null);
            mSongs.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mSongs.size());
            Snackbar.make(view, "File Deleted!", Snackbar.LENGTH_SHORT).show();
        } else {
            Snackbar.make(view, "Can't delete this song!", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public int getItemCount() {
        return mSongs.size();
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
