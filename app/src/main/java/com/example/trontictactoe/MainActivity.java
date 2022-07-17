package com.example.trontictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {



    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    int move = 0;
    int[][]board = new int[3][3]; // Tic tac toe board
    boolean edit = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        ImageButton btn00 = findViewById(R.id.btn00); // (0, 0) 1
        ImageButton btn10 = findViewById(R.id.btn10); // (1, 0) 2
        ImageButton btn20 = findViewById(R.id.btn20); // (2, 0) 3
        ImageButton btn01 = findViewById(R.id.btn01); // (0, 1) 4
        ImageButton btn11 = findViewById(R.id.btn11); // (1, 1) 5
        ImageButton btn21 = findViewById(R.id.btn21); // (2, 1) 6
        ImageButton btn02 = findViewById(R.id.btn02); // (0, 2) 7
        ImageButton btn12 = findViewById(R.id.btn12); // (1, 2) 8
        ImageButton btn22 = findViewById(R.id.btn22); // (2, 2) 9
        ImageButton reset = findViewById(R.id.reset); // reset button
        ImageButton home = findViewById(R.id.home); // reset button

        // Sets on click listeners
        btn00.setOnClickListener(this);
        btn10.setOnClickListener(this);
        btn20.setOnClickListener(this);
        btn01.setOnClickListener(this);
        btn11.setOnClickListener(this);
        btn21.setOnClickListener(this);
        btn02.setOnClickListener(this);
        btn12.setOnClickListener(this);
        btn22.setOnClickListener(this);
        reset.setOnClickListener(this);
        home.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == findViewById(R.id.reset)) {
            reset();
            edit = true;
            return;
        }

        if (v == findViewById(R.id.home)) {
            reset();
            openHome();
            return;
        }

            if (edit) {
                String name = ((ImageButton) findViewById(v.getId())).getTag().toString();

                int posx = name.charAt(0) - '0';
                int posy = name.charAt(1) - '0';

                System.out.print("X is " + posx + " and Y is " + posy);

                TextView announcement = ((TextView) findViewById(R.id.winner));
                //String text = "PLAYER " + (move % 2) + " WINS!";
                announcement.setText("");

                if (board[posx][posy] == 0) {
                    move++; // Increments move count


                    if (move % 2 == 1) { // x player 1
                        board[posx][posy] = 1;
                        ((ImageButton) v).setImageResource(R.drawable.x);
                    } else { // o player 2
                        board[posx][posy] = 2;
                        ((ImageButton) v).setImageResource(R.drawable.o);
                    }

                    if (checkWin()) {
                        String text = "PLAYER " + (((move + 1) % 2) + 1) + " WINS!";
                        edit = false;
                        announcement.setText(text);


                    }

                } else {  // Already selected
                    "".isEmpty(); // do nothing
                }
            }
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

    public boolean checkWin() {
        for (int i = 0; i <= 2; i++) {
            if (board[i][0] != 0 && (board[i][0] == board[i][1]) && (board[i][1] == board[i][2])) {
                return true; // checks vertical wins
            }
            if (board[0][i] != 0 && (board[0][i] == board[1][i]) && (board[1][i] == board[2][i])) {
                return true; // checks horizontal wins
            }
        }

        if (board[0][0] != 0 && (board[0][0] == board[1][1]) && (board[1][1] == board[2][2])) { // diagonal at 0, 0
            return true;
        }

        if (board[2][0] != 0 && (board[2][0] == board[1][1]) && (board[1][1] == board[0][2])) { // diagonal at 2, 0
            return true;
        }

        return false;
    }

    public void reset() {
        for (int i = 0; i <= 2; i++) { // resets board
            for (int j = 0; j <= 2; j++) {
                board[i][j] = 0;
            }
        } move = 0;

        ((TextView)findViewById(R.id.winner)).setText("");
        ((ImageButton)findViewById(R.id.btn00)).setImageResource(R.drawable.none); // (0, 0) 1
        ((ImageButton)findViewById(R.id.btn10)).setImageResource(R.drawable.none); // (1, 0) 2
        ((ImageButton)findViewById(R.id.btn20)).setImageResource(R.drawable.none); // (2, 0) 3
        ((ImageButton)findViewById(R.id.btn01)).setImageResource(R.drawable.none); // (0, 1) 4
        ((ImageButton)findViewById(R.id.btn11)).setImageResource(R.drawable.none); // (1, 1) 5
        ((ImageButton)findViewById(R.id.btn21)).setImageResource(R.drawable.none); // (2, 1) 6
        ((ImageButton)findViewById(R.id.btn02)).setImageResource(R.drawable.none); // (0, 2) 7
        ((ImageButton)findViewById(R.id.btn12)).setImageResource(R.drawable.none); // (1, 2) 8
        ((ImageButton)findViewById(R.id.btn22)).setImageResource(R.drawable.none); // (2, 2) 9

    }

    public static void wait(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(InterruptedException ex)
        {
            Thread.currentThread().interrupt();
        }
    }

    public void openHome() {
        Intent home = new Intent(this, HomeActivity.class );
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }

}