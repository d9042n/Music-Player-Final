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
import com.example.musicplayer.activity.AlbumActivity;
import com.example.musicplayer.model.Song;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Song> mAlbums;

    public AlbumAdapter(Context mContext, ArrayList<Song> mAlbums) {
        this.mContext = mContext;
        this.mAlbums = mAlbums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_items, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewAlbumName.setText(mAlbums.get(position).getAlbum());

        byte[] image = getAlbumArt(mAlbums.get(position).getPath());
        if (image != null) {
            Glide.with(mContext).asBitmap()
                    .load(image)
                    .circleCrop()
                    .into(holder.imageViewAlbumCover);
        } else {
            Glide.with(mContext).asBitmap()
                    .load(R.drawable.app_logo)
                    .circleCrop()
                    .into(holder.imageViewAlbumCover);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, AlbumActivity.class);
                intent.putExtra("album_name", mAlbums.get(holder.getAdapterPosition()).getAlbum());
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mAlbums.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewAlbumName;
        ImageView imageViewAlbumCover;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewAlbumName = itemView.findViewById(R.id.albumItemTextViewAlbumName);
            imageViewAlbumCover = itemView.findViewById(R.id.albumItemImageViewAlbumCover);
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
