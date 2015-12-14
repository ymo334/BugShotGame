package com.example.sf.bugshotgame;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by USER on 2015-11-27.
 */
public class Score {
    public int x, y;
    public Paint paint;
    private int loop = 0;
    private int color = Color.WHITE;
    private long createTime;

    //생성자
    public Score(int _x, int _y, long Time) { // 충돌 좌표
        x = _x;
        y = _y;
        createTime = Time;
        loop = 0;
        paint = new Paint();
        paint.setColor(color);
        paint.setTextSize(40);
        paint.setAntiAlias(true);
        Move();
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public Paint getPaint(){
        return paint;
    }

    public long getCreateTime(){
        return createTime;
    }

    public boolean Move(){
        y -= 4;
        if(y < -20) return false;
        loop++;
        if(loop % 4 == 0){
            color = (Color.WHITE + Color.YELLOW) - color;
            paint.setColor(color);
        }
        return true;
    }
}
