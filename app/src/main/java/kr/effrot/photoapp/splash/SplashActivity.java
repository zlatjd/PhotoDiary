package kr.effrot.photoapp.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import kr.effrot.photoapp.MainActivity;
import kr.effrot.photoapp.R;

public class SplashActivity extends AppCompatActivity {


    //String device_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        try {


            /*new Thread(new Runnable() {
                @Override
                public void run() {
                    //getDeviceVersion();

                    MarketVersionCheck marketVersionCheck = new MarketVersionCheck(SplashActivity.this, getPackageName());
                    marketVersionCheck.execute();

                }
            }).start();*/


            /*DisplayMetrics displayMetrics = getApplicationContext().getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            int height = displayMetrics.heightPixels;*/


            Handler handler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);

                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                }
            };

            handler.sendEmptyMessageDelayed(0, 2500);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /*private void getDeviceVersion() {

        try {
            String device_version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            this.device_version = device_version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

    }*/

}
