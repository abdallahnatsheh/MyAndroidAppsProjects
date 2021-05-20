package com.abdallahn_ameerh.ex2;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.Random;

public class GameView extends View {
    private int score = 0;
    private ArrayList  lives = new ArrayList();
    ArrayList<String> temp = new ArrayList<>();
    private float getHeight, getWidth, Bwidth,Bheight ;
    private static  int ROWS;
    private static  int COLS;
    private Paint pGameInfo, pGameMessage;
    Paddle paddle;
    BrickCollection B;
    private int movePaddle = 0;
    int gamesit=0;
    private Ball ball;
    private boolean didColide = false;
    enum State {GET_READY, PLAYING, GAME_OVER};
    public State state;
    private MediaPlayer breakit;//for the sound
    Thread task;


    public GameView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //a thread to check collidation
    private void threadForCollide() {
         task = new Thread() {
            @Override
            public void run() {
                didColide = B.checkCollision(ball);
                paddle.didCollide(ball);
            }
        };

        task.start();
    }
    private void gameMessage() {
        //this a setup to print the game messages
        pGameMessage = new Paint(Paint.ANTI_ALIAS_FLAG);
        pGameMessage.setTextAlign(Paint.Align.CENTER);
        pGameMessage.setColor(Color.GREEN);
        pGameMessage.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
        pGameMessage.setTextSize(55);
    }
        private void gameInfo() {
        //to print the game score and lives setup
            pGameInfo = new Paint(Paint.ANTI_ALIAS_FLAG);
            pGameInfo.setColor(Color.GREEN);
            pGameInfo.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            pGameInfo.setTextSize(60);
    }


    private void startGame() {
        state = State.GET_READY;
        temp.removeAll(temp);
        temp.add("O");temp.add("O");temp.add("O");
        lives = (ArrayList)temp.clone();
        ROWS = new Random().nextInt((6 - 2) + 1) + 2;
        COLS = new Random().nextInt((7 - 3) + 1) + 3;
        getWidth = getWidth();//2088
        getHeight = getHeight();//1080
        Bwidth = (getWidth-(5*(ROWS+1))) / ROWS;  //brick width
        Bheight =(getHeight/20); //brick height
        gameInfo(); // score and life settings
        gameMessage(); // middle words settings
        B = new BrickCollection(Bheight, Bwidth, ROWS, COLS);
        paddle = new Paddle((getWidth -Bwidth)/2, getHeight-(Bheight*1.6f),
                            (getWidth +Bwidth)/2, getHeight - 120f);
        ball = new Ball((getWidth / 2) , getHeight - 160f, (Bheight/2));
        breakit = MediaPlayer.create(getContext(), R.raw.explode);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (gamesit == 0) {
            startGame();
            gamesit++;
        }
        B.draw(canvas);
        paddle.draw(canvas);
        threadForCollide();
        ball.draw(canvas);
        pGameInfo.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("Lives: " + lives.toString()
                .replace("[","").replace("]","").replace(",",""),
                getWidth - 50, 70, pGameInfo);
        pGameInfo.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("Scores: " + score, 50, 70, pGameInfo);
        ball.ballMov(getWidth, getHeight);
        gamesit = 1; //this check if the game started so it dont randomize the screen all the time

    switch (state) {
        case GET_READY:
            if (B.checkWin())
                startGame();
            canvas.drawText("Click to PLAY!", getWidth / 2, getHeight / 2, pGameMessage);
            break;

        case PLAYING:

            // move the jumpingBall
            ball.ballMov(getWidth, getHeight);
            if (didColide) {
                score += lives.size() * 5;
                breakit.start();

            }
            if (B.checkWin()){//in case of winning
                lives = (ArrayList)temp.clone();
                score = 0;
                state = State.GAME_OVER;}

            // check if the ball dropped under the paddle
            if (ball.outOfHeight) {
                if (lives.size() > 1) {
                    lives.remove(lives.size()-1);
                    ball.outOfHeight = false;
                    ball.setX(((getWidth / 2) - 200f) + 200f);
                    ball.setY(getHeight - 160f);
                    paddle.setLeft((getWidth -Bwidth)/2);
                    paddle.setRight(((getWidth +Bwidth)/2));
                    state = State.GET_READY;
                    ball.movX = 0;
                    ball.movY = 0;
                } else {
                    state = State.GAME_OVER;
                }
            }
            break;
        case GAME_OVER:
            if (B.checkWin()){
                canvas.drawText("You Win! Score: "+score, getWidth / 2, getHeight / 2, pGameMessage);
                state = State.GET_READY;
            }
            else {
                canvas.drawText("GAME OVER - You Loss!", getWidth / 2, getHeight / 2, pGameMessage);
                ball.movX = 0;
                ball.movY = 0;
            }
            break;
    }
    invalidate();
}

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (state == State.GET_READY) {
                    ball.movX = 2;
                    ball.movY = 2;
                    state = State.PLAYING;
                } else {
                    if (state == State.PLAYING) {
                    if (motionEvent.getX() > getWidth / 2) {
                        paddle.moveRight(getWidth);
                    } else {
                        paddle.moveLeft();
                    }

                } else if (state == state.GAME_OVER) {
                        lives = (ArrayList)temp.clone();
                        score = 0;
                        ball.setX((getWidth / 2));
                        ball.setY(getHeight - 160f);
                        paddle.setLeft((getWidth - Bwidth) / 2);
                        paddle.setRight((getWidth + Bwidth) / 2);
                        ball.outOfHeight = false;
                        B.newGame();
                        state = state.GET_READY;
                } else {
                    startGame();
                }
        }
                break;
            case MotionEvent.ACTION_DOWN:
                if (state == State.GET_READY) {
                    ball.movX = 2;
                    ball.movY = 2;
                    state = State.PLAYING;
                }
                else if (state == state.GAME_OVER) {
                    lives = (ArrayList)temp.clone();
                    score = 0;
                    ball.setX((getWidth / 2));
                    ball.setY(getHeight - 160f);
                    paddle.setLeft((getWidth - Bwidth) / 2);
                    paddle.setRight((getWidth + Bwidth) / 2);
                    ball.outOfHeight = false;
                    B.newGame();
                    state = state.GET_READY;
                }
                break;

            case MotionEvent.ACTION_UP:
                if (state == State.PLAYING)
                    movePaddle = 0;
                break;
        }
        invalidate();
        return true;
    }
    @Override
    protected void onSizeChanged(int width, int height, int oldw, int oldh) {
        super.onSizeChanged(width, height, oldw, oldh);
        getWidth = width;
        getHeight = height;

    }
}