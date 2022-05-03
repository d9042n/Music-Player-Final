package com.example.musicplayer.fragment;

import static com.example.musicplayer.activity.MainActivity.localSongs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.adapter.SongAdapter;

public class SongsFragment extends Fragment {

    RecyclerView recyclerViewSongsFragment;
    SongAdapter songAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_songs, container, false);

        recyclerViewSongsFragment = view.findViewById(R.id.recyclerViewSongsFragment);
        recyclerViewSongsFragment.setHasFixedSize(true);

        if (localSongs.size() > 0) {
            songAdapter = new SongAdapter(getContext(), localSongs);
            recyclerViewSongsFragment.setAdapter(songAdapter);
            recyclerViewSongsFragment.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        }

        return view;
    }
}