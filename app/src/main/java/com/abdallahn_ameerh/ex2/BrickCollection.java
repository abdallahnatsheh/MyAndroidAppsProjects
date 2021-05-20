package com.abdallahn_ameerh.ex2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class BrickCollection {
    float getHeight, getWidth;
    int row, col;
    Brick[] AllOfbricks;
    int counter = 0;
    //create bricks to be drawn on the fifth row for 250 px
    public BrickCollection(float getHeight, float getWidth, int row, int col) {
        counter = 0;
        this.getHeight = getHeight;
        this.getWidth = getWidth;
        this.row = row;
        this.col = col;
        AllOfbricks = new Brick[row * col];
        float padding = 5;
        Paint p = new Paint();
        p.setColor(Color.RED);
        p.setStyle(Paint.Style.FILL);

        for (int i = 5; i < col+5; i++) {
            for (int j = 0; j < row; j++) {
                if (j == 0 && (i -5) >= 0)
                    AllOfbricks[counter] = new Brick(j * getWidth, (i * getHeight) + padding, (j + 1) * getWidth, (i + 1) * getHeight);
                else
                    AllOfbricks[counter] = new Brick((j * getWidth) + padding, (i * getHeight) + padding, (j + 1) * getWidth, (i + 1) * getHeight);
                counter++;
                if (counter == row * col)
                    break;
            }
        }
        counter = 0;
    }
    //draw all bricks
    public void draw(Canvas canvas) {
        counter = 0;
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                AllOfbricks[counter].draw(canvas);
                counter++;
                if (counter == row * col)
                    break;
            }
        }
        counter = 0;
    }
    //check ball collision on brick depend on the direction
public boolean checkCollision(Ball ball) {//to check collision
    String result;

    for (int i = 0; i < row * col; i++) {
        result = AllOfbricks[i].didCollide(ball);
        if (!result.equalsIgnoreCase("none")) {
            if (result.equalsIgnoreCase("top")) {
                AllOfbricks[i].isDisabled = true;
                    ball.movY = ball.movY * -1;
                return true;

            } else if (result.equalsIgnoreCase("bottom")) {
                AllOfbricks[i].isDisabled = true;
                    ball.movY = ball.movY * -1;
                return true;

            } else if (result.equalsIgnoreCase("left")) {
                AllOfbricks[i].isDisabled = true;
                    ball.movX = ball.movX * -1;
                return true;

            } else if (result.equalsIgnoreCase("right")) {
                AllOfbricks[i].isDisabled = true;
                    ball.movX = ball.movX * -1;
                return true;
            }
        }
    }
    return false;
}
    //check is all bricks are disabled
    public boolean checkWin() {//check if all the bricks destroyed
        int counter = 0;
        boolean res = true;
        for (int i = 0; i < row * col; i++) {
            if (AllOfbricks[i].isDisabled) {
                counter++;
            }
        }
            if (counter != row * col) {
                res= false;
            }
        return res;
    }
    //enable drawing for all bricks
    public void newGame() {//in case of game-over set isDisabled to false.
        for (int i = 0; i < row * col; i++) {
            AllOfbricks[i].isDisabled = false;
            AllOfbricks[i].p.setColor(Color.RED);
            AllOfbricks[i].p.setStyle(Paint.Style.FILL);
        }
    }
}
