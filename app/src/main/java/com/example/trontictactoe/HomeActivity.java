package com.example.trontictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Arrays;

/*
An implementation of Minimax AI Algorithm in Tic Tac Toe,
using Java and Android Studio.
This software is available under GPL license.
Author: Kaden Carr
Year: 2022
License: GNU GENERAL PUBLIC LICENSE (GPL)
 */

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    //Media Variables
    private VideoView videoBG;
    public MediaPlayer music;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /*
         * Creates the saved instance state. The constructor for our page.
         * @param Takes the state to construct our android page.
         */

        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_mode);

        // Starts theme music
        music = MediaPlayer.create(HomeActivity.this, R.raw.music);
        music.start();

        // Videoview to UI
        videoBG = findViewById(R.id.videoView);

        // Creates view
        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.video);

        // Set the new Uri to our VideoView and starts video view
        videoBG.setVideoURI(uri);
        videoBG.start();

        // Set an OnPreparedListener for our VideoView.
        videoBG.setOnPreparedListener(mediaPlayer -> {
            mMediaPlayer = mediaPlayer;
            // We set looping to true to allow the video to repeat
            mMediaPlayer.setLooping(true);

            // we then seek to the current position if it has been set and play the video
            if (mCurrentVideoPosition != 0) {
                mMediaPlayer.seekTo(mCurrentVideoPosition);
                mMediaPlayer.start();
            }
        });

        // Instantiates image buttons for board
        Button play = findViewById(R.id.play); // (0, 0) 1
        Button playai = findViewById(R.id.playai); // (1, 0) 2
        Button exit = findViewById(R.id.exit); // (2, 0) 3


        // Sets on click listeners
        play.setOnClickListener(this);
        for (Button button : Arrays.asList(playai, exit)) {
            button.setOnClickListener(this);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        videoBG.pause();
        music.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoBG.start();
        music.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        music.stop();
        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void onClick(View v) {
        /*
         * Takes the click and directs the button to change the page.
         * @param take the view of the button that clicks associated with the listener.
         */
        if (v == findViewById(R.id.exit)) { exit(); }
        else if (v == findViewById(R.id.play)) { openMainActivity(); }
        else if (v == findViewById(R.id.playai)) { openPlayAI(); }
    }


    public void openMainActivity() {
        /*
         * Opens 2 player mode!
         */
        Intent intent_main = new Intent(this, MainActivity.class );
        intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_main);
    }
    public void openPlayAI() {
        /*
         * Connects the play HAL to open the AI play mode.
         */
        Intent intent_ai = new Intent(this, PlayAI.class );
        intent_ai.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_ai);
    }

    public void exit() {
        /*
         * Exits the software
         */
        finish();
        System.exit(0);
    }




}
