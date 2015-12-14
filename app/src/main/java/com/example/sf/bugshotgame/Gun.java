package com.example.sf.bugshotgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

/**
 * Created by USER on 2015-11-27.
 */
public class Gun {
    private int width, height;
    private int centX, centY;
    private int w, h;
    private int x, y;
    private Bitmap gun;
    RectF gunBounds;

    Paint paint;

    boolean isdead = false;
    private int randomX, randomY;

    public void RandomN(int rangX,int rangY){
        randomX = (int)(Math.random() * rangX); // randomX -> 0 ~ rangX
        randomY = (int)(Math.random() * rangY); // randomY -> 0 ~ rangY
    }

    public Gun(Context context){
        width = MyGameView.width;
        height = MyGameView.height;
        centX = MyGameView.width / 2;
        centY = MyGameView.height / 2;
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);

        Resources res;
        res = context.getResources();

        gun = BitmapFactory.decodeResource(res, R.drawable.gun);
        gunBounds = new RectF();

        RandomN(width, height);
        x = centX;
        y = height - 400;

        w = gun.getWidth() / 2;
        h = gun.getHeight() / 2;

    }
    public int getGunX(){
        return x;
    }
    public int getGunY(){
        return y;
    }


    public void GunMove(double distant, float degree){
        // 조이스틱 움직인 거리, 각도 등을 받아 위치 변경
        if(distant > 120) {
            x = x + (int)(Math.sin(degree)*10);
            y = y + (int)(Math.cos(degree)*10);
        }
        else{
            x = x + (int)(Math.sin(degree)*10);
            y = y + (int)(Math.cos(degree)*10);
        }
        // 벽에 충돌 할 경우 범위를 넘어가지 않도록 조정
        if(x < w)
            x = w;
        if(x > width - w)
            x = width - w;
        if(y < h)
            y = h;
        if(y > height - 300 - h)
            y = height - 300 -h;
    }


    public void draw(Canvas canvas){
        canvas.drawBitmap(gun, x - w , y - h , null);
        gunBounds.set(x - w/2 , y - h/2 , x + w/2 , y + h/2 );
        canvas.drawRect(gunBounds, paint);
    }
}
