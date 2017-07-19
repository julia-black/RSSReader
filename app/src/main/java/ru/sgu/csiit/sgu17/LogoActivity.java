package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;


public class LogoActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logo_activity);
        final Thread logoTimer = new Thread(){
            public void run(){
                try{
                    Log.i("Logo", "Logo..." );
                   int logoTimer = 0;
                   while(logoTimer < 5000){
                       sleep(100);
                       logoTimer = logoTimer + 100;
                   };
                    toNewsListActivity();
                }
                catch (InterruptedException e){
                    e.printStackTrace();
                }
                finally {
                    finish();
                }
            }
        };
        logoTimer.start();
    }
    public void toNewsListActivity(){
        Intent intent = new Intent(LogoActivity.this, NewsListActivity.class);
        startActivity(intent);

    }
}
