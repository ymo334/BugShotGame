package com.example.sf.bugshotgame;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.WindowManager;

import java.util.ArrayList;

/**
 * Created by USER on 2015-11-27.
 */
public class MyGameView extends SurfaceView implements Callback {
    Context mContext;
    SurfaceHolder mHolder;
    MyGameThread mThread;
    static int width, height;
    //생성자
    public MyGameView(Context context, AttributeSet attrs){
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mHolder = holder;
        mContext = context;
        mThread = new MyGameThread(holder, context);

        setFocusable(true);

        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        width = display.getWidth();
        height = display.getHeight();
    }
    // SurfaceView가 생성될 때 실행되는 부분
    @Override
    public void surfaceCreated(SurfaceHolder holder){
        try {
            mThread.start();
            System.out.println("처음 시작");
        } catch(Exception e){
            System.out.println("예외 발생 재시작");
            RestartGame();
        }
    }
    // SurfaceView가 바뀔 때 실행되는 부분
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

    }
    // SurfaceView가 해제될 때 실행되는 부분
    @Override
    public void surfaceDestroyed(SurfaceHolder holder){
        /*boolean done = true;
        while(done){
            try{
                System.out.println("파괴 준비");
                mThread.join(); // 스레드가 현재 step을 끝낼 때 까지 대기
                System.out.println("파괴 됨");
                done = false;
            }catch (InterruptedException e){ // 인터럽트 신호가 오면?
// 그 신호 무시 = 아무것도 하지 않음.
            }
        }*/
        //PauseGame();
        StopGame();
        System.gc();
    }
    // 스레드 완전 정지
    public void StopGame(){
        mThread.StopThread();
        System.out.println("스레드가 완전 정지됨");
    }
    // 스레드 일시 정지
    public void PauseGame(){
        mThread.PauseNResume(true);
        System.out.println("스레드가 일시 정지됨");
    }
    // 스레드 재기동
    public void ResumeGame() {
        System.out.println("스레드가 재기동 될 것임");
        mThread.PauseNResume(false);
        System.out.println("스레드가 재기동됨");
    }
    // 게임 초기화
    public void RestartGame(){
        mThread.StopThread(); // 스레드 중지
        mThread = null; // 현재의 스레드를 비우고
        mThread = new MyGameThread(mHolder, mContext);
        mThread.start();
        System.out.println("아예 처음부터 재시작");
    }
    // 스레드 영역
    class MyGameThread extends Thread{
        SurfaceHolder mHolder;
        Context mContext;
        boolean canRun = true;
        boolean isWait = false;

        private Bitmap back;
        private Bitmap back2;

        public Gun gun;
        public int bulletCount;
        public Joystick joy;

        Bitmap shootButton; // 슈팅 버튼 이미지 저장 변수
        public int shootx, shooty; // 슈팅 버튼 이미지 좌표
        public int shootw, shooth; // 슈팅 버튼 이미지 넓이,높이 관련
        public boolean shootIsTouch = false;

        int Tot = 0;
        Paint paint = new Paint();

        public ArrayList<Bug> mbug  = new ArrayList<Bug>();
        public ArrayList<Bug2> mbug2 = new ArrayList<Bug2>();
        public ArrayList<DieImg> mdie;
        public ArrayList<Score> mscore;
        public ArrayList<Bullet> bullets;
        public ArrayList<Block> mblock = new ArrayList<Block>();
        public ArrayList<BonusBullet> mbonus;

        int Stage1X[] = {0,1,2,3,4,5,0,1,2,3,4,5,0,1,2,3,4,5};
        int Stage1Y[] = {0,0,0,0,0,0,1,1,1,1,1,1,2,2,2,2,2,2};

        int Stage2X[] = {0,1,2,3,4,5,0,1,2,3,4,5};
        int Stage2Y[] = {0,0,0,0,0,0,1,1,1,1,1,1};

        int StageCount = 1; // 스테이지는 1에서 3번까지
        boolean StTF1 = false;
        boolean StTF2 = false;
        boolean StTF3 = false;
        // 스레드 생성자
        public MyGameThread(SurfaceHolder holder, Context context){
            mHolder = holder;
            mContext = context;

            Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            width = display.getWidth();
            height = display.getHeight();

            InitGame();
            Makestage();
        }
        // 게임 초기화
        public void InitGame(){
            paint.setTextSize(45);
            paint.setColor(Color.CYAN);
            paint.setTypeface(Typeface.DEFAULT_BOLD);
            paint.setStrokeJoin(Paint.Join.ROUND);
            paint.setAntiAlias(true);

            shootButton = BitmapFactory.decodeResource(getResources(), R.drawable.shootbutton);
            shootx = width - 250;
            shooty = height - 250;
            shootw = 200;
            shooth = 200;
            shootButton = Bitmap.createScaledBitmap(shootButton, shootw, shooth, false);

            gun = new Gun(mContext);
            joy = new Joystick(mContext);
            mdie = new ArrayList<DieImg>();
            mscore = new ArrayList<Score>();
            bullets = new ArrayList<Bullet>();
            mblock = new ArrayList<Block>();
            mbonus = new ArrayList<BonusBullet>();
        }
        // 스테이지 만들기
        public void Makestage(){
            if(StageCount == 1){ // 처음 스테이지
                for(int i = 0; i < 5; i++) {
                    mbug.add(new Bug(mContext));
                }
                for(int i = 0; i< Stage1X.length; i++){
                    mblock.add(new Block(mContext, Stage1X[i], Stage1Y[i]));
                }
                back = BitmapFactory.decodeResource(getResources(), R.drawable.back);
                back = Bitmap.createScaledBitmap(back, width, height - 300, false);
                back2 = BitmapFactory.decodeResource(getResources(), R.drawable.back2);
                back2 = Bitmap.createScaledBitmap(back2, width, 300, false);
                for(int i = 0; i < 5; i++){
                    mbonus.add(new BonusBullet(mContext));
                }
                bulletCount = 10;
            }else if(StageCount == 2){ // 두 번째 스테이지
                for(int i = 0; i < 8; i++){
                    mbug.add(new Bug(mContext));
                }
                for(int i = 0; i< Stage2X.length; i++){
                    mblock.add(new Block(mContext, Stage2X[i], Stage2Y[i]));
                }
                bulletCount = 10;

            }else if(StageCount == 3){ // 세 번째 스테이지
                for(int i = 0; i < 8; i++){
                    mbug2.add(new Bug2(mContext));
                }
                for(int i = 0; i< Stage2X.length; i++){
                    mblock.add(new Block(mContext, Stage2X[i], Stage2Y[i]));
                }
                bulletCount = 10;
            }
            else
                ;
        }
        // 게임 오버
        private void GameOver(){
            if((mblock.size() == 0) || ((StageCount == 3) && (mbug2.size() == 0))){ // 벽돌이 다 꺠짐 or 3라운드 벌레를 모두 처치 했을 경우 게임이 끝나고 점수 표시
                Context context = MyGameView.this.getContext();
                Intent intent = new Intent(context,OverActivity.class);
                intent.putExtra("stage",StageCount);
                intent.putExtra("score",Tot);
                context.startActivity(intent);
            }
        }
        // run
        public void run() {
            Canvas canvas = null;
            while(canRun){
                canvas = mHolder.lockCanvas();
                try{
                    synchronized (mHolder){ // 동기화 유지
                        update();
                        GameOver();
                        Cresh();
                        DrawImg(canvas);
                    }
                }finally {
                    if(canvas != null)
                        mHolder.unlockCanvasAndPost(canvas);
                }
                synchronized (this){
                    if(isWait){
                        try{
                            wait();
                        }catch (Exception e){

                        }
                    }
                }
            }
        }
        // 스레드 중지
        public void StopThread(){
            canRun = false;
            synchronized (this){
                this.notify();
            }
        }
        // 스레드 대기
        public void PauseNResume(boolean wait){
            isWait = wait;
            synchronized (this){
                this.notify();
            }
        }
        // view들 최신화
        public void update() {
            for(int i = mbug.size() - 1 ; i >= 0; i--){ // bug 업데이트
                mbug.get(i).BugMove();
                if(mbug.get(i).isdead == true) {
                    mdie.add(new DieImg(getContext(), mbug.get(i).x, mbug.get(i).y, System.currentTimeMillis()));
                    mscore.add(new Score(mbug.get(i).x, mbug.get(i).y, System.currentTimeMillis()));
                    mbug.remove(i);
                    System.gc();
                    Tot += 100;

                }
            }
            for(int i = mbug2.size() - 1 ; i >= 0; i--){ // bug 업데이트
                mbug2.get(i).BugMove();
                if(mbug2.get(i).isdead == true) {
                    mdie.add(new DieImg(getContext(), mbug2.get(i).getX(), mbug2.get(i).getY(), System.currentTimeMillis()));
                    mscore.add(new Score(mbug2.get(i).getX(), mbug2.get(i).getY(), System.currentTimeMillis()));
                    mbug2.remove(i);
                    System.gc();
                    Tot += 100;

                }
            }

            for(int i = mdie.size() - 1; i >= 0; i--){
                if(System.currentTimeMillis() - mdie.get(i).getCreateTime() >= 2000) // 벌레의 피가 생성된지(벌레가 죽은지) 2초가 지나면
                    mdie.remove(i); // 벌레의 피를 없애줌
            }
            for(int i = mscore.size() - 1; i >= 0; i--){
                if(System.currentTimeMillis() - mscore.get(i).getCreateTime() >= 2000)
                    mscore.remove(i);
            }
            if(shootIsTouch) { //슈팅 버튼의 터치가 일어나면 총알 오브젝트 생성
                if(bulletCount != 0) {
                    bullets.add(new Bullet(getContext(), gun.getGunX(), gun.getGunY()));
                    shootIsTouch = false;
                    bulletCount--;
                }
            }
            for(int i = bullets.size() - 1; i >= 0; i--){ // 맵을 나간 총알 제거함
                bullets.get(i).BulletMove(gun.getGunX(), gun.getGunY());
                if(bullets.get(i).y == 0) {
                    bullets.remove(i);
                    System.gc();
                }
            }
            for(int i = bullets.size() - 1; i >= 0; i--){ // 총알 isdead 검사해서 true인(충돌한) 총알은 제거
                if(bullets.get(i).isdead == true){
                    bullets.remove(i);
                    System.gc();
                }
            }
            for(int i = mblock.size() - 1; i >= 0; i--){ // 블럭 isCreash 검사해서 true인 블럭은 제거
                if(mblock.get(i).isCreash == true) {
                    mblock.remove(i);
                    //System.gc();
                }
            }
            for(int i = mbonus.size() - 1; i >= 0; i--){
                if(mbonus.get(i).isdead == true){
                    mbonus.remove(i);
                    System.gc();
                    bulletCount++;
                }
            }
        }
        // 충돌 감지 및 처리
        public void Cresh(){
            if(StTF1 == false) {
                if (mbug.size() == 0) {
                    StageCount = 2;
                    for (int i = mblock.size() - 1; i >= 0; i--) { // 블럭 isCreash 검사해서 true인 블럭은 제거
                        mblock.remove(i);
                        System.gc();
                    }
                    Makestage();
                    StTF1 = true;
                }
            }
            if(StTF2 == false) {
                if (StageCount == 2) {
                    if (mbug.size() == 0) {
                        StageCount = 3;
                        for (int i = mblock.size() - 1; i >= 0; i--) { // 블럭 isCreash 검사해서 true인 블럭은 제거
                            mblock.remove(i);
                            System.gc();
                        }
                        Makestage();
                        StTF2 = true;
                    }
                }
            }
            if(StTF3 == false) {
                if (StageCount == 3){
                    if (mbug2.size() == 0) {
                        StageCount = 4;
                        for (int i = mblock.size() - 1; i >= 0; i--) { // 블럭 isCreash 검사해서 true인 블럭은 제거
                            mblock.remove(i);
                            System.gc();
                        }
                        Makestage();
                        StTF1 = false;
                        StTF2 = false;
                    }
                }
            }
            for (int i = 0; i < mbug.size(); i++) { // 벌레와 총알의 충돌 감지
                for (int j = 0; j < bullets.size(); j++) {
                    if (mbug.get(i).bugBounds.intersect(bullets.get(j).bulletBounds)) {
                        mbug.get(i).isdead = true;
                        bullets.get(j).isdead = true;
                    }
                }
            }
            for (int i = 0; i < mbug2.size(); i++) { // 벌레2와 총알의 충돌 감지
                for (int j = 0; j < bullets.size(); j++) {
                    if (mbug2.get(i).bugBounds.intersect(bullets.get(j).bulletBounds)) {
                        mbug2.get(i).isdead = true;
                        bullets.get(j).isdead = true;
                    }
                }
            }
            for(int i = 0; i < mbonus.size(); i++){ // 총과 보너스 아이템 충돌 감지(아이템 획득)
                if(mbonus.get(i).BounusBulletbounds.intersect(gun.gunBounds)){
                    mbonus.get(i).isdead = true;
                }
            }
            for(int i = 0; i < mblock.size(); i++){ // 벌레와 블럭의 충돌 감지
                for(int j = 0; j < mbug.size(); j++){
                    // 충돌 없음
                    if(mbug.get(j).x + mbug.get(j).w < mblock.get(i).x1 || mbug.get(j).x - mbug.get(j).w > mblock.get(i).x2
                            || mbug.get(j).y + mbug.get(j).w < mblock.get(i).y1 || mbug.get(j).y - mbug.get(j).w > mblock.get(i).y2){
                        continue;
                    }
                    //
                    // 충돌 감지 x 좌표 축
                    if(mblock.get(i).x1 - mbug.get(j).x >= mbug.get(j).w || mbug.get(j).x - mblock.get(i).x2 >= mbug.get(j).w){
                        mblock.get(i).isCreash = true;
                        mbug.get(j).isCreash = 1;
                    }
                    // 충돌 감지 y 좌표 축
                    else{
                        mblock.get(i).isCreash = true;
                        mbug.get(j).isCreash = 2;
                    }
                }
                for(int j = 0; j < mbug2.size(); j++){
                    // 충돌 없음
                    if(mbug2.get(j).getX() + mbug2.get(j).getW() < mblock.get(i).x1 || mbug2.get(j).getX() - mbug2.get(j).getW() > mblock.get(i).x2
                            || mbug2.get(j).getY() + mbug2.get(j).getW() < mblock.get(i).y1 || mbug2.get(j).getY() - mbug2.get(j).getW() > mblock.get(i).y2){
                        continue;
                    }
                    //
                    // 충돌 감지 x 좌표 축
                    if(mblock.get(i).x1 - mbug2.get(j).getX() >= mbug2.get(j).getW() || mbug2.get(j).getX() - mblock.get(i).x2 >= mbug2.get(j).getW()){
                        mblock.get(i).isCreash = true;
                        mbug2.get(j).isCreash = 1;
                    }
                    // 충돌 감지 y 좌표 축
                    else{
                        mblock.get(i).isCreash = true;
                        mbug2.get(j).isCreash = 2;
                    }
                }
            }
        }
        // Draw
        public void DrawImg(Canvas canvas){
            // 배경
            canvas.drawBitmap(back, 0, 0, null);
            canvas.drawBitmap(back2, 0, height - 300, null);
            // 슈팅 버튼
            canvas.drawBitmap(shootButton, shootx, shooty, null);
            // 가상 조이스틱
            joy.draw(canvas);
            // 총
            gun.draw(canvas);
            // 벌레
            for(int i = 0; i < mbug.size(); i++){
                mbug.get(i).draw(canvas);
            }
            for(int i = 0; i < mbug2.size(); i++){
                mbug2.get(i).draw(canvas);
            }
            // 죽은 이미지
            for(int i = 0; i < mdie.size(); i++){
                mdie.get(i).draw(canvas);
            }
            // 점수
            for(int i = 0; i < mscore.size(); i++){
                canvas.drawText("+100점!", mscore.get(i).getX(), mscore.get(i).getY(), mscore.get(i).getPaint());
            }
            // 총알
            for(int i = 0; i < bullets.size(); i++){
                bullets.get(i).draw(canvas);
            }
            // 블럭
            for(int i = 0; i< mblock.size(); i++){
                mblock.get(i).draw(canvas);
            }
            // 보너스 아이템
            for(int i = 0; i< mbonus.size(); i++){
                mbonus.get(i).draw(canvas);
            }
            // 총 점수
            canvas.drawText("총점 : " + Tot, width/2 - 50, height - 150, paint);
            canvas.drawText("총알 : " + bulletCount, width/2 - 50, height - 200, paint);
            canvas.drawText("스테이지 : [ " + StageCount + " ]", width/2 - 50, height -100, paint);
        }
    }
    // 터치 구현
    public boolean onTouchEvent(MotionEvent event){
        int pointer_count = event.getPointerCount(); // 현재 터치가 발생한 포인트 수를 얻음
        if(pointer_count > 2)
            pointer_count = 2; // 3개 이상의 포인터를 터치했더라도 2개까지만 처리하도록 설정
        float currentX = event.getX(); //터치하고 있는 x좌표
        float currentY = event.getY(); //터치하고 있는 y좌표
        float currentArrayX[] = new float[2]; // 터치 이벤트가 두번 발생할 경우 x 좌표들
        float currentArrayY[] = new float[2]; // 터치 이벤트가 두번 발생할 경우 y 좌표들
        switch(event.getAction() & MotionEvent.ACTION_MASK){
            case MotionEvent.ACTION_MOVE: // 누른 상태에서 이동 하면
                float dis = (float)(Math.pow((currentX - (mThread.shootx + mThread.shootw/2)),2) + Math.pow((currentY - (mThread.shooty + mThread.shooth/2)),2));
                float powR = (float)Math.pow(mThread.shootw/2,2);
                if(powR >= dis){
                    //shootIsTouch = true;
                }
                else {
                    float degree = mThread.joy.joystickMove(currentX, currentY);
                    double distant = mThread.joy.distant(currentX, currentY);
                    mThread.gun.GunMove(distant, degree);
                }
                break;
            case MotionEvent.ACTION_DOWN: // 누르면
                dis = (float)(Math.pow((currentX - (mThread.shootx + mThread.shootw/2)),2) + Math.pow((currentY - (mThread.shooty + mThread.shooth/2)),2));
                powR = (float)Math.pow(mThread.shootw/2,2);
                if(powR >= dis){
                    mThread.shootIsTouch = true;
                }
                else {
                    float degree = mThread.joy.joystickMove(currentX, currentY);
                    double distant = mThread.joy.distant(currentX,currentY);
                    mThread.gun.GunMove(distant, degree);
                }
                break;
            case MotionEvent.ACTION_UP: // 떼면
                mThread.joy.joystickMove(200, height - 150);
                break;
            case MotionEvent.ACTION_POINTER_DOWN: // 터치 이벤트가 2번 발생 할 경우. 즉 조이스틱 Move 도중 미사일 발사를 할 경우
                for(int i = 0; i< 2; i++){
                    currentArrayX[i] = event.getX(i);
                    currentArrayY[i] = event.getY(i);
                }
                // 조이스틱 터치 이벤트
                float degree = mThread.joy.joystickMove(currentArrayX[0], currentArrayY[0]);
                double distant = mThread.joy.distant(currentArrayX[0],currentArrayY[0]);
                mThread.gun.GunMove(distant, degree);
                // 미사일 발사 터치 이벤트
                dis = (float)(Math.pow((currentArrayX[1] - (mThread.shootx + mThread.shootw/2)),2) + Math.pow((currentArrayY[0] - (mThread.shooty + mThread.shooth/2)),2));
                powR = (float)Math.pow(mThread.shootw/2,2);
                if(powR >= dis){
                    mThread.shootIsTouch = true;
                }
                //
                break;
        }
        return true;
    }
}
