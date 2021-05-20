package com.abdallahn_ameerh.ex2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;


public class Paddle {
    private float top, bottom, right, left;
    public Paint p;
    private int moveRight = 0, moveLeft = 0;
    //paddle constructor
    public Paddle(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.p = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.p.setColor(Color.BLUE);
    }

    public void moveRight(float w) {//to make the paddle move right

        if (this.getRight() <= w) { //check if touch the screen side
            this.right +=10;//= moveRight;
            this.left +=10;//= moveRight;
        }
    }

    public void moveLeft() {//to make the paddle move left
        if (this.getLeft() > 0) { // check if touch the screen side
            this.left -=10;//= moveLeft;
            this.right -=10;//= moveLeft;
        }
    }

    public void setLeft(float left) {
        this.left = left;
    }

    public float getLeft() {
        return left;
    }

    public void setRight(float right) {
        this.right = right;
    }

    public float getRight() {
        return right;
    }

    public void draw(Canvas canvas) {
        canvas.drawRect(left, top, right, bottom, p);
    }

    void didCollide(Ball ball) {//to check collision from top
        //check if the ball on the same x cordinate of paddle
        if (ball.getX() + ball.getRadius() > left && ball.getX() - ball.getRadius() < right) {
            //check if its touch the paddle
            if (ball.getY() + ball.getRadius() < top && ball.getY() + ball.getRadius() > bottom ) {

                if (ball.getY() >= 0) {
                    if (ball.movY < 0)
                        ball.movY = (ball.movY * -1);
                }

            }
        }

    }
}