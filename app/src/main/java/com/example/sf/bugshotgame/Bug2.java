package com.example.sf.bugshotgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;

import java.util.Random;

/**
 * Created by USER on 2015-11-27.
 */
public class Bug2
{
    private int width, height;
    private int centX, centY;
    private int w, h;
    private int x, y; // 벌레 중심 좌표
    private int sx, sy;
    private int rx, ry; // 벌레가 회전하는 원의 중심 좌표
    private int r; // 원의 반지름
    private double angle = 0; // 벌레좌표의 원에 대한 초기 각도
    int n = 10; // 각도 증가

    private Paint paint;

    Bitmap bug;
    RectF bugBounds;

    boolean isdead = false;
    public int isCreash = 0;

    int randomX, randomY;
    public void RandomN(int rangX,int rangY){
        randomX = (int)(Math.random() * rangX); // randomX -> 0 ~ rangX
        randomY = (int)(Math.random() * rangY); // randomY -> 0 ~ rangY
    }
    public Bug2(Context context){
        width = MyGameView.width;
        height = MyGameView.height;
        centX = MyGameView.width / 2;
        centY = MyGameView.height / 2;
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);

        Resources res;
        res = context.getResources();

        bug = BitmapFactory.decodeResource(res, R.drawable.bug5);
        bugBounds = new RectF();

        RandomN(width, height - 300 - 3*(width / 6));
        rx = randomX; // 초기 좌표 설정
        ry = randomY + 3*(width/6);

        w = bug.getWidth() / 2;
        h = bug.getHeight() / 2;

        Random rnd = new Random();
        int k = rnd.nextInt(2) == 0 ? -1 : 1; // -1 , 1
        r = (rnd.nextInt(10) + 20); // 20 ~ 29
        sx = (rnd.nextInt(4) + 2) * k; // +- 2 ~ 5
        sy = (rnd.nextInt(4) + 2) * k;

        x = (int) (rx + Math.cos(angle * Math.PI / 180) * r);
        y = (int) (ry - Math.sin(angle * Math.PI / 180) * r);
    }

    public void BugMove(){
        angle += n;
        rx += sx;
        ry += sy;
        x = (int) (rx + Math.cos(angle * Math.PI / 180) * r);
        y = (int) (ry + Math.sin(angle * Math.PI / 180) * r);

        if(isCreash == 1) {
            sx++; // 충돌하면 속도 증가
            sx = -sx;
        }
        else if(isCreash == 2) {
            sy++;
            sy = -sy;
        }
        else
        ;
        isCreash = 0;
        if( x < w) { // 왼쪽 벽에 충돌
            x = w;
            sx++;
            sx = -sx;
            n=-10;
        }
        if( x > width - w) { // 오른족 벽에 충돌
            x = width - w;
            sx++;
            sx = -sx;
            n = 10;
        }
        if(y < h) { // 위의 벽에 충돌
            y = h;
            sy++;
            sy = -sy;
            n = -10;
        }
        if(y > height - 300 - h) { // 아래의 벽에 충돌
            y = height - 300 - h;
            sy++;
            sy = -sy;
            n = 10;
        }

    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
    public int getW(){
        return w;
    }
    public int getH() {
        return h;
    }
    public void draw(Canvas canvas){
        bugBounds.set(x - w/2 , y - h/2 , x + w/2 , y + h/2 );
        canvas.drawRect(bugBounds, paint);
        canvas.drawBitmap(bug, x - w , y - h , null);
    }

}
