package com.example.musicplayer.animation;

import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

public class PLayerAnimation {
    public void startDiscTurnAnimation(ImageView coverArt) {
        Runnable animationRunnable = new Runnable() {
            @Override
            public void run() {
                coverArt.animate().rotationBy(360).withEndAction(this).setDuration(10000)
                        .setInterpolator(new LinearInterpolator()).start();
            }
        };
        coverArt.animate().rotationBy(360).withEndAction(animationRunnable).setDuration(10000)
                .setInterpolator(new LinearInterpolator()).start();
    }

    public void stopDiscTurnAnimation(ImageView coverArt) {
        coverArt.animate().cancel();
    }

}
