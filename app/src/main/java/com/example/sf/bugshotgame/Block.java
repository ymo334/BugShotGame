package com.example.sf.bugshotgame;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

/**
 * Created by USER on 2015-11-29.
 */
public class Block {
    public int x1, y1, x2, y2; // 블록 좌표
    public Bitmap imgBlock;
    public boolean isCreash = false; // 0이면 충돌 안한 상태 , 1이면 x축 충돌, 2이면 y축 충돌

    //생성자
    public Block(Context context, float x, float y){
        x1 = (int)(MyGameView.width / 6 * x);
        y1 = (int)(MyGameView.width / 6 * y);

        x2 = x1 + MyGameView.width / 6;
        y2 = y1 + MyGameView.width / 6;

        imgBlock = BitmapFactory.decodeResource(context.getResources(), R.drawable.block, null);
        imgBlock = Bitmap.createScaledBitmap(imgBlock, MyGameView.width / 6, MyGameView.width / 6, true);
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(imgBlock,x1,y1,null);

    }


}
