package com.example.musicplayer.fragment;

import static com.example.musicplayer.activity.MainActivity.listAllSongs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.AlbumAdapter;

public class AlbumFragment extends Fragment {

    RecyclerView recyclerViewAlbumFragment;
    AlbumAdapter albumAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_album, container, false);

        recyclerViewAlbumFragment = view.findViewById(R.id.recyclerViewAlbumsFragment);
        recyclerViewAlbumFragment.setHasFixedSize(true);

        if (listAllSongs.size() > 0) {
            albumAdapter = new AlbumAdapter(getContext(), listAllSongs);
            recyclerViewAlbumFragment.setAdapter(albumAdapter);
            recyclerViewAlbumFragment.setLayoutManager(new GridLayoutManager(getContext(), 2));
        }

        return view;
    }
}