package com.abdallahn_ameerh.ex2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

public class Ball {

    private float x;
    private float y;
    private float radius;
    private Paint p;
    float movX = 0;
    float movY = 0;
    boolean outOfHeight; // bool to check if the ball under the paddle
    //ball constructor
    public Ball(float x, float y, float radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.p = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.p.setColor(Color.WHITE);
        this.outOfHeight = false;
    }


    public void ballMov(float w, float h){//to move the ball in an animated way
        this.x -= movX;
        this.y -= movY;

        // check if ball out of left or right side
        if((x-radius)<=0 || (x+radius)>=w)
        {
            movX = -movX;
        }

        // check if ball out of bottom or up side
        if((y+radius)>=h || (y-radius)<=0)
        {
            if((y-radius)<=0)
                movY = -movY;
            else
                outOfHeight=true;
        }

    }

    public float getX() {
        return x;
    }
    public float setX(float x) {
        return this.x=x;
    }
    public float getY() {
        return y;
    }
    public float setY(float y) {
        return this.y = y;
    }

    public float getRadius() {
        return radius;
    }

    public void draw(Canvas canvas) {
        canvas.drawCircle(x, y, radius, p);
    }
}
