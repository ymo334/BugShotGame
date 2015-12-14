package com.example.sf.bugshotgame;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

public class GameActivity extends Activity {

    MyGameView mGameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN); //전체화면으로 설정

        setContentView(R.layout.activity_main);
        mGameView = (MyGameView)findViewById(R.id.mGameView);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        return true;

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        //return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause(){ // 홈버튼 누르면
        super.onPause();
        mGameView.PauseGame();
    }
    @Override
    protected  void onResume(){
        super.onResume();
        mGameView.ResumeGame();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                mGameView.PauseGame();
                new AlertDialog.Builder(GameActivity.this)
                        .setTitle("종료")
                        .setMessage("게임을 종료 하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                moveTaskToBack(true);
                                mGameView.surfaceDestroyed(mGameView.mHolder);
                                finish();
                                ActivityManager am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                                am.restartPackage(getPackageName());
                            }

                        })
                        .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                mGameView.ResumeGame();
                            }
                        })
                        .show();
        }
        return true;
    }



}
