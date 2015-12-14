package com.example.sf.bugshotgame;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;


/**
 * Created by USER on 2015-11-27.
 */
public class DieImg {
    private int x, y;
    private int alpha = 255; // 이미지의 투명도
    private long createTime;
    Paint paint;

    Bitmap dieimg;

    public DieImg(Context context, int _x, int _y, long Time)
    {
        x = _x;
        y = _y;
        createTime = Time;
        paint = new Paint();
        paint.setColor(Color.TRANSPARENT);
        Resources res;
        res = context.getResources();
        dieimg = BitmapFactory.decodeResource(res, R.drawable.dead);
    }
    public long getCreateTime(){
        return createTime;
    }

    public void draw(Canvas canvas){
        alpha -= 2;
        paint.setAlpha(alpha);
        canvas.drawBitmap(dieimg, x, y, paint);
    }
}
