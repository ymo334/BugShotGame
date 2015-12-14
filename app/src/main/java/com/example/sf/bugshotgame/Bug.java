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
public class Bug {
    int width, height;
    int centX, centY;
    int w, h;
    int x, y;
    int sx, sy;

    private Paint paint;

    Bitmap bug;
    RectF bugBounds;

    boolean isdead = false;
    public int isCreash = 0; // 블럭과 충돌안하면 0 , 블럭x축과 충돌하면 1, 블럭y축과 충돌하면 2

    int randomX, randomY;
    public void RandomN(int rangX,int rangY){
        randomX = (int)(Math.random() * rangX); // randomX -> 0 ~ rangX
        randomY = (int)(Math.random() * rangY); // randomY -> 0 ~ rangY
    }

    public Bug(Context context){
        width = MyGameView.width;
        height = MyGameView.height;
        centX = MyGameView.width / 2;
        centY = MyGameView.height / 2;
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);

        Resources res;
        res = context.getResources();

        RandomN(5,0);
        if(randomX == 0)
            bug = BitmapFactory.decodeResource(res, R.drawable.bug1);
        else if(randomX == 1)
            bug = BitmapFactory.decodeResource(res, R.drawable.bug2);
        else if(randomX == 2)
            bug = BitmapFactory.decodeResource(res, R.drawable.bug3);
        else if(randomX == 3)
            bug = BitmapFactory.decodeResource(res, R.drawable.bug6);
        else if(randomX == 4)
            bug = BitmapFactory.decodeResource(res, R.drawable.bug7);
        else
            bug = BitmapFactory.decodeResource(res, R.drawable.bug1);

        bugBounds = new RectF();

        RandomN(width, height - 300 - 3*(width / 6));
        x = randomX; // 초기 좌표 설정
        y = randomY + 3*(width/6);

        w = bug.getWidth() / 2;
        h = bug.getHeight() / 2;

        Random rnd = new Random();
        int k = rnd.nextInt(2) == 0 ? -1 : 1; // -1 , 1
        sx = (rnd.nextInt(4) + 2) * k; // +- 2 ~ 5
        sy = (rnd.nextInt(4) + 2) * k; // +- 2 ~ 5
    }

    public void BugMove(){
        x += sx;
        y += sy;

        if(isCreash == 1) {
            sx++;
            sx = -sx;
        }
        else if(isCreash == 2) {
            sy++;
            sy = -sy;
        }
        else
            ;
        isCreash = 0;

        if( x < w) {
            x = w;
            sx++;
            sx = -sx;
        }
        if( x > width - w) {
            x = width - w;
            sx++;
            sx = -sx;
        }
        if(y < h) {
            y = h;
            sy++;
            sy = -sy;
        }
        if(y > height - 300 - h) {
            y = height - 300 - h;
            sy++;
            sy = -sy;
        }

    }
    public void draw(Canvas canvas){
        bugBounds.set(x - w/2 , y - h/2 , x + w/2 , y + h/2 );
        canvas.drawRect(bugBounds, paint);
        canvas.drawBitmap(bug, x - w , y - h , null);
    }
}
