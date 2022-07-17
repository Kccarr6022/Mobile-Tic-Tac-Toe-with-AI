package com.example.trontictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {


    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_mode);

        // Videoview to UI
        videoBG = (VideoView) findViewById(R.id.videoView);

        Uri uri = Uri.parse("android.resource://" // First start with this,
                + getPackageName() // then retrieve your package name,
                + "/" // add a slash,
                + R.raw.video);

        // Set the new Uri to our VideoView and starts video view
        videoBG.setVideoURI(uri);
        videoBG.start();

        // Set an OnPreparedListener for our VideoView.
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                // We set looping to true to allow the video to repeat
                mMediaPlayer.setLooping(true);

                // we then seek to the current position if it has been set and play the video
                if (mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });

        // Instantiates image buttons for board
        Button play = (Button) findViewById(R.id.play); // (0, 0) 1
        Button playai = ( Button) findViewById(R.id.playai); // (1, 0) 2
        Button exit = (Button) findViewById(R.id.exit); // (2, 0) 3


        // Sets on click listeners
        play.setOnClickListener((View.OnClickListener) this);
        playai.setOnClickListener((View.OnClickListener) this);
        exit.setOnClickListener((View.OnClickListener) this);

    }

    public void onClick(View v) {
        if (v == findViewById(R.id.exit)) { exit(); }
        else if (v == findViewById(R.id.play)) { openMainActivity(); }
        else if (v == findViewById(R.id.playai)) { openPlayAI(); }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoBG.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        videoBG.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        mMediaPlayer.release();
        mMediaPlayer = null;
    }

    public void openMainActivity() {
        Intent intent_main = new Intent(this, MainActivity.class );
        intent_main.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_main);
    }
    public void openPlayAI() {
        Intent intent_ai = new Intent(this, PlayAI.class );
        intent_ai.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_ai);
    }

    public void exit() {
        finish();
        System.exit(0);
    }




}
