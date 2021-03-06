package com.example.trontictactoe;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.*;

/*
An implementation of Minimax AI Algorithm in Tic Tac Toe,
using Java and Android Studio.
This software is available under GPL license.
Author: Kaden Carr
Year: 2022
License: GNU GENERAL PUBLIC LICENSE (GPL)
 */

public class PlayAI extends AppCompatActivity implements View.OnClickListener {


    private VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    int[][] board = new int[3][3]; // Tic tac toe board
    boolean edit = true;
    static int HUMAN = -1;
    static int HAL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) { // -------------------------
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Videoview to UI
        videoBG = findViewById(R.id.videoView);

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
        ImageButton home = findViewById(R.id.home); // home button


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

    // ------------------------------------------------------------- CREATION

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


    @Override
    public void onClick(View v) {
        /*
         * Take a click from a button and determines the action based on the button.
         * @param x The view value coming from the button.
         */


        if (v == findViewById(R.id.reset)) { // reset button
            reset();
            edit = true;
            return;
        }

        if (v == findViewById(R.id.home)) { // home button
            reset();
            openHome();
            return;
        }

        // game button

        if (edit) {
            String name = v.getTag().toString();

            int posx = name.charAt(0) - '0';
            int posy = name.charAt(1) - '0';

            if (board[posx][posy] == 0) {
                // place piece from person
                board[posx][posy] = HUMAN;
                ((ImageButton) v).setImageResource(R.drawable.x);


            if (checkWin(board, HUMAN)) {
                String text = "PLAYER 1 WINS!";
                edit = false;
                ((TextView) findViewById(R.id.winner)).setText(text);
                return;
            }

            // place piece from AIi

            halTurn();
            if (checkWin(board, HAL)) {
                String text = "HAL9000 WINS!";
                edit = false;
                ((TextView) findViewById(R.id.winner)).setText(text);

            }
            }
        } else {  // Already selected
            "".isEmpty(); // do nothing
        }

        }

    public boolean checkWin(int[][]state, int player) { // -1 = human and 1 = computer
        /*
         * Connects to the board and returns if the game is over.
         * @param takes if player is human or computer and the state of the game.
         * @return Returns a true or false if the game is over.
         */
        for (int i = 0; i <= 2; i++) {
            if (state[i][0] == player && (state[i][0] == state[i][1]) && (state[i][1] == state[i][2])) {
                return true; // checks vertical wins
            }
            if (state[0][i] == player && (state[0][i] == state[1][i]) && (state[1][i] == state[2][i])) {
                return true; // checks horizontal wins
            }
        }

        if (state[0][0] == player && (state[0][0] == state[1][1]) && (state[1][1] == state[2][2])) { // diagonal at 0, 0
            return true;
        }

        if (state[2][0] == player && (state[2][0] == state[1][1]) && (state[1][1] == state[0][2])) { // diagonal at 2, 0
            return true;
        }

        return false;
    }

    public boolean gameOver(int[][]state) {
        return checkWin(state,HAL) || checkWin(state, HUMAN);
    }

    public int evaluate(int[][]state) { // For AI Algorithm
        /*
         * Connects to the board and does calculations for the artificial intelligence.
         * @param take the state of the game (board)
         * @return Returs 1 if calculated computer wins, -1 if human wins, and 0 if neither.
         */
        if (checkWin(state, HAL)) {
            return 1;
        }
        else if (checkWin(state, HUMAN)) {
            return -1;
        }
        else {
            return 0;
        }

    }

    public int[] minimax(int[][]state, int depth, int player) {
        /*
         * Connects to the board and does calculations for the artificial intelligence.
         * @param take the state of the game (board)
         * @return Returs 1 if calculated computer wins, -1 if human wins, and 0 if neither.
         */

        int[] best = new int[3];

        if (player == HAL) {
            best[0] = -1; // best row
            best[1] = -1; // best col
            best[2] = -(int)Double.POSITIVE_INFINITY; // best score
            for (int x = 0; x < best.length; x++ ) {
                String beststr = x + " is " + best[x];
                Log.d("myTag", beststr);
            }
        }
        else {
            best[0] = 1; // best row
            best[1] = 1; // best col
            best[2] = (int)Double.POSITIVE_INFINITY; // best score
        }

        if (depth == 0 || gameOver(state)) {
            int score = evaluate(state);
            int[] returned =  {-1, -1, score};
            return returned;
        }


        for ( int i = 0; i < depth; i++) {
            int x = empty_cells(state).get(i)[0];
            int y = empty_cells(state).get(i)[1];
            state[x][y] = player;
            int[] score = minimax(state, depth - 1, -player);
            state[x][y] = 0;
            score[0] = x;
            score[1] = y;

            if (player == HAL) {
                if (score[2] > best[2]) {
                    best = score; // max
                }
            }
            else {
                if (score[2] < best[2]) {
                    best = score; // min
                }
            }

        }


        return best;
    }

    public Vector<Integer[]> empty_cells(int[][] state) {
        /*
         * Connects to the board to return the locations of all the empty spaces.
         * @param take the state of the game (board)
         * @return Returns a double array of x and y coordinates for empty spots.
         */

        Vector<Integer[]> empty_cells = new Vector<>();
        for (int x = 0; x <= 2; x++) {
            for (int y = 0; y <= 2; y++) {
                if (state[x][y] == 0) {
                    Integer []pos = {x, y};
                    empty_cells.add(pos);
                }
            }
        }



        return empty_cells;

    }

    public void halTurn() { // Will go after human
        /*
         * Will respond to the board to place the best possible move from HAL.
         */

        Log.d("myTag", " Hals turn");
        // calculates the number of empty cells.
        int depth = empty_cells(board).size();
        int x, y;
        if (depth == 0 || gameOver(board)) {
            return;
        } else if (empty_cells(board).size() == 8 && board[1][1] == 0) {
            x = 1;
            y = 1;
        } else if (empty_cells(board).size() == 8 && board[1][1] != 0) {
            x = 0;
            y = 2;
        }
        else {

            Log.d("myTag", " Pass depth defined");

            int[] move = minimax(board, depth, HAL);
            x = move[0];
            y = move[1];
        }

        board[x][y] = HAL;

        // Sets button to 0 based on the coordinates.
        if (x == 0 && y == 0) {
            ((ImageButton) findViewById(R.id.btn00)).setImageResource(R.drawable.o);
        }
        else if (x == 0 && y == 1) {
            ((ImageButton) findViewById(R.id.btn01)).setImageResource(R.drawable.o);
        }
        else if (x == 0 && y == 2) {
            ((ImageButton) findViewById(R.id.btn02)).setImageResource(R.drawable.o);
        }
        else if (x == 1 && y == 0) {
            ((ImageButton) findViewById(R.id.btn10)).setImageResource(R.drawable.o);
        }
        else if (x == 1 && y == 1) {
            ((ImageButton) findViewById(R.id.btn11)).setImageResource(R.drawable.o);
        }
        else if (x == 1 && y == 2) {
            ((ImageButton) findViewById(R.id.btn12)).setImageResource(R.drawable.o);
        }
        else if (x == 2 && y == 0) {
            ((ImageButton) findViewById(R.id.btn20)).setImageResource(R.drawable.o);
        }
        else if (x == 2 && y == 1) {
            ((ImageButton) findViewById(R.id.btn21)).setImageResource(R.drawable.o);
        }
        else if (x == 2 && y == 2) {
            ((ImageButton) findViewById(R.id.btn22)).setImageResource(R.drawable.o);
        }


    }


    public void reset() {
        /*
         * Function which resets the paremeters for the game to happen to default values.
         */
        for (int i = 0; i <= 2; i++) { // resets board
            for (int j = 0; j <= 2; j++) {
                board[i][j] = 0;
            }
        }

        ((TextView) findViewById(R.id.winner)).setText("");
        ((ImageButton) findViewById(R.id.btn00)).setImageResource(R.drawable.none); // (0, 0) 1
        ((ImageButton) findViewById(R.id.btn10)).setImageResource(R.drawable.none); // (1, 0) 2
        ((ImageButton) findViewById(R.id.btn20)).setImageResource(R.drawable.none); // (2, 0) 3
        ((ImageButton) findViewById(R.id.btn01)).setImageResource(R.drawable.none); // (0, 1) 4
        ((ImageButton) findViewById(R.id.btn11)).setImageResource(R.drawable.none); // (1, 1) 5
        ((ImageButton) findViewById(R.id.btn21)).setImageResource(R.drawable.none); // (2, 1) 6
        ((ImageButton) findViewById(R.id.btn02)).setImageResource(R.drawable.none); // (0, 2) 7
        ((ImageButton) findViewById(R.id.btn12)).setImageResource(R.drawable.none); // (1, 2) 8
        ((ImageButton) findViewById(R.id.btn22)).setImageResource(R.drawable.none); // (2, 2) 9

    }


    public void openHome() {
        /*
         * Function to bring the game to the HomeActivity.
         */
        Intent home = new Intent(this, HomeActivity.class );
        home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(home);
    }


}

