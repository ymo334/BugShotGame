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
 * Created by USER on 2015-12-03.
 */
public class BonusBullet {
    int width, height;
    int centX, centY;
    int w, h;
    int x, y;
    int sx, sy;

    private Paint paint;

    Bitmap BonusBullet;
    RectF BounusBulletbounds;

    boolean isdead = false;

    int randomX, randomY;
    public void RandomN(int rangX,int rangY){
        randomX = (int)(Math.random() * rangX); // randomX -> 0 ~ rangX
        randomY = (int)(Math.random() * rangY); // randomY -> 0 ~ rangY
    }

    public BonusBullet(Context context){
        width = MyGameView.width;
        height = MyGameView.height;
        centX = MyGameView.width / 2;
        centY = MyGameView.height / 2;
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);

        Resources res;
        res = context.getResources();

        BonusBullet = BitmapFactory.decodeResource(res, R.drawable.bonus);
        BounusBulletbounds = new RectF();

        RandomN(width, height - 300 - 4*(width / 6));
        x = randomX; // 초기 좌표 설정
        y = randomY + 3 * ( width / 6 );

        w = BonusBullet.getWidth() / 2;
        h = BonusBullet.getHeight() / 2;
    }

    public void draw(Canvas canvas){
        BounusBulletbounds.set(x - w/2 , y - h/2 , x + w/2 , y + h/2 );
        canvas.drawRect(BounusBulletbounds, paint);
        canvas.drawBitmap(BonusBullet, x - w , y - h , null);
    }
}
