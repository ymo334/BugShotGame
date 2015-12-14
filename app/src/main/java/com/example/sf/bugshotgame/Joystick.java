package com.example.sf.bugshotgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by USER on 2015-11-29.
 */
public class Joystick {
    int width, height;
    float x, y; // 큰 원이 그려질 좌표
    float _x, _y; // 작은 원이 그려질 좌표
    int bigR; // 큰 원 반지름
    int smallR; // 작은 원 반지름

    private Paint paint;
    RectF bigCircle;
    RectF smallCircle;
    Bitmap big;
    Bitmap small;

    public Joystick(Context context) {
        width = MyGameView.width;
        height = MyGameView.height;

        x = 200;
        y = height - 150;
        _x = 200;
        _y = height - 150;

        bigR = 120;
        smallR = 60;

        bigCircle = new RectF();
        smallCircle = new RectF();
        big = BitmapFactory.decodeResource(context.getResources(), R.drawable.shootbutton);
        big = Bitmap.createScaledBitmap(big, 260, 260, true);
        small = BitmapFactory.decodeResource(context.getResources(), R.drawable.joy);
        small = Bitmap.createScaledBitmap(small,160,130,true);

        paint = new Paint();
        paint.setColor(Color.TRANSPARENT); // 투명한 색
    }

    public float joystickMove(float touchX, float touchY){ // 조이스틱 작은 원의 움직인 구현 -> 큰 원과 작은 원 각도 반환
        float dx = touchX - x;
        float dy = touchY - y;

        float degree = (float)Math.atan2(dx, dy);
        double dis = distant(touchX, touchY);

        if(dis > 120){
            _x = (float)( x + Math.sin(degree) * 120);
            _y = (float)( y + Math.cos(degree) * 120);
        }
        else
        {
            _x = touchX;
            _y = touchY;
        }
        return degree;
    }
    public double distant(float touchX, float touchY){
        return  Math.sqrt(Math.pow(touchX - x, 2) + Math.pow(touchY - y, 2));
    }


    public void draw(Canvas canvas){
        bigCircle.set(x - bigR, y - bigR, x + bigR, y + bigR);
        canvas.drawOval(bigCircle, paint);
        canvas.drawBitmap(big, x - bigR - 10, y - bigR - 10, null);

        smallCircle.set(_x - smallR, _y - smallR, _x + smallR, _y + smallR);
        canvas.drawOval(smallCircle, paint);
        canvas.drawBitmap(small, _x - smallR - 20, _y - smallR - 5, null);
    }
}
