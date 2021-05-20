package com.abdallahn_ameerh.ex2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Brick {

    private float top, bottom, right, left;
    public Paint p;
    boolean isDisabled;
    //brick constructor
    public Brick(float left, float top, float right, float bottom) {
        this.left = left;
        this.top = top;
        this.bottom = bottom;
        this.right = right;
        this.p = new Paint(Paint.ANTI_ALIAS_FLAG);
        this.p.setColor(Color.RED);
        this.isDisabled = false;
    }
  //check collision depending on cordinates
   public String didCollide(Ball ball) {//to check collision
        if (isDisabled)
            return "none";
        float marginY = (bottom - top) ;
       Log.d("gg", "marginY: "+marginY);
        float marginX = (right - left) ;
       Log.d("gg", "marginX: "+marginY);

       if (ball.getX() + ball.getRadius() > left && ball.getX() - ball.getRadius() < right) {
            if (ball.getY() - ball.getRadius() < bottom && ball.getY() - ball.getRadius() > bottom - marginY) {
                return "bottom";
            } else if (ball.getY() + ball.getRadius() > top && ball.getY() + ball.getRadius() < top + marginY) {
                return "top";
            }
        }

        if (ball.getY() + ball.getRadius() > top && ball.getY() - ball.getRadius() < bottom) {
            if (ball.getX() + ball.getRadius() > left && ball.getX() + ball.getRadius() < left + marginX) {
                return "Left";
            } else if (ball.getX() - ball.getRadius() < right && ball.getX() - ball.getRadius() > right - marginX) {
                return "right";
            }
        }
        return "none";
    }
    //draw non destroyed bricks
    public void draw(Canvas canvas) {
        if (!isDisabled)
            canvas.drawRect(left, top, right, bottom, p);
    }

}
