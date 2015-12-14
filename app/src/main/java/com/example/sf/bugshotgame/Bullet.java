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
public class Bullet {
    int width, height;
    int centX, centY;
    int w, h;
    int x, y;

    private Paint paint;

    Bitmap bullet;
    RectF bulletBounds;

    boolean isdead = false;


    public Bullet(Context context,int gunX, int gunY){
        width = MyGameView.width;
        height = MyGameView.height;
        centX = MyGameView.width / 2;
        centY = MyGameView.height / 2;
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);

        Resources res;
        res = context.getResources();

        bullet = BitmapFactory.decodeResource(res, R.drawable.bullet2);
        bullet = Bitmap.createScaledBitmap(bullet, 33, 60, true);
        bulletBounds = new RectF();

        x = gunX;
        y = gunY;

        w = 33 / 2;
        h = 27 / 2;

    }
    public void BulletMove(int gunX, int gunY){
        if(y < 0)
            isdead = true;
        else
            y -= 15;
    }

    public void draw(Canvas canvas){
        bulletBounds.set(x - 3 * w, y - 3 * h, x - 2 * w, y - 2 * h);
        canvas.drawRect(bulletBounds, paint);
        canvas.drawBitmap(bullet, x - 3 * w , y - 3 * h , null);
    }

}
