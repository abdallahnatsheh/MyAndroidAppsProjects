package com.abdallahn_ameerh.ex1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;


public class GameActivity extends AppCompatActivity implements View.OnClickListener {

    MediaPlayer mediaPlayer;
    GameBoard gameBoard;
    private boolean gameOver = true;
    public TextView mov_count;
    private Button newGame;
    //setup for the countup timer
    private Chronometer chronometer;
    private long pauseOffset ;
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        //set the action bar name
        getSupportActionBar().setTitle("Puzzle 15");
        //setup the music
        mediaPlayer=MediaPlayer.create(GameActivity.this,R.raw.fez_music);
        //set up shared preferences and retrive the switch situation
        SharedPreferences sp = getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        boolean md_situation = sp.getBoolean("switch",false);
        //set up the countup timer
        chronometer = findViewById(R.id.sec_min);
        chronometer.setBase(SystemClock.elapsedRealtime());

        //create array of the puzzle blocks
        TextView blocks [] ={
                (findViewById(R.id.b0)),(findViewById(R.id.b1)),(findViewById(R.id.b2)), (findViewById(R.id.b3)),
                (findViewById(R.id.b4)),(findViewById(R.id.b5)), (findViewById(R.id.b6)),(findViewById(R.id.b7)),
                (findViewById(R.id.b8)), (findViewById(R.id.b9)),(findViewById(R.id.b10)),(findViewById(R.id.b11)),
                (findViewById(R.id.b12)),(findViewById(R.id.b13)),(findViewById(R.id.b14)),(findViewById(R.id.b15))};
        //create game object
        gameBoard =new GameBoard(blocks);
        //check the switch and play music
        if (md_situation) {
            mediaPlayer.start();
            mediaPlayer.setLooping(true); }
        else { mediaPlayer.pause(); }

        //setup the game
        mov_count=(TextView)findViewById(R.id.mov_count);
        mov_count.setText(Integer.toString(gameBoard.game_moves));
        newGame = (Button) findViewById(R.id.start_new_game);

        if(gameOver) {
            gameBoard.newGame();
            resetChronometer();
        }
        newGame.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                gameBoard.newGame();
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                resetChronometer();

            }
        });
        // here i will emplement the blocks actions
        for (int btn = 0 ; btn < 16 ; btn++) {
            String buttonID = "b" + btn;
            int resID = getResources().getIdentifier(buttonID, "id", getPackageName());
            gameBoard.blocks[btn] = findViewById(resID);
            gameBoard.blocks[btn].setOnClickListener((View.OnClickListener) this);
        }


    }


    @Override
    protected void onPause() {

        super.onPause();
        mediaPlayer.pause();
        pauseChronometer();
    }

    @Override
    protected void onStop() {

        super.onStop();
        mediaPlayer.pause();
        pauseChronometer();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mediaPlayer.reset();
        mediaPlayer.release();
        resetChronometer();
    }
    protected void onResume() {

        startChronometer();
        super.onResume();
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
    }
    public void startChronometer() {
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
            chronometer.start();
            running = true;
    }

    public void pauseChronometer() {
        if (running) {
            chronometer.stop();
            pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
            running = false;
        }
    }

    public void resetChronometer() {
        chronometer.setBase(SystemClock.elapsedRealtime());
        pauseOffset = 0;
    }
    public void gameSolved(){
        if(gameBoard.gameOver) {
            Toast.makeText(getApplicationContext(), "Game Over - Puzzle Solved!", Toast.LENGTH_LONG).show();
            pauseChronometer();
            for (int i = 0; i < 16; i++)
                gameBoard.blocks[i].setClickable(false);
        }
        gameBoard.gameOver=false;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.b1:
                gameBoard.movPuzzle(1);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b2:
                gameBoard.movPuzzle(2);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b3:
                gameBoard.movPuzzle(3);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b4:
                gameBoard.movPuzzle(4);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b5:
                gameBoard.movPuzzle(5);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b6:
                gameBoard.movPuzzle(6);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b7:
                gameBoard.movPuzzle(7);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b8:
                gameBoard.movPuzzle(8);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b9:
                gameBoard.movPuzzle(9);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b10:
                gameBoard.movPuzzle(10);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b11:
                gameBoard.movPuzzle(11);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b12:
                gameBoard.movPuzzle(12);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b13:
                gameBoard.movPuzzle(13);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b14:
                gameBoard.movPuzzle(14);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b15:
                gameBoard.movPuzzle(15);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
            case R.id.b0:
                gameBoard.movPuzzle(0);
                mov_count.setText(Integer.toString(gameBoard.game_moves));
                gameSolved();
                break;
        }
    }
}