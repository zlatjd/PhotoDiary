package kr.effrot.photoapp.util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import kr.effrot.photoapp.BuildConfig;
import kr.effrot.photoapp.MainActivity;
import kr.effrot.photoapp.splash.SplashActivity;

/**
 * Created by kimsungwoo on 2019. 7. 2..
 */

public class MarketVersionCheck extends AsyncTask<Void, Void, String> {

    private final String APP_VERSION_NAME = BuildConfig.VERSION_NAME;
    private final String STORE_URL = "https://play.google.com/store/apps/details?id=kr.effrot.photoapp";


    String packageName;
    Context context;



    AlertDialog.Builder mDialog;


    public MarketVersionCheck(Context context, String packageName) {
        this.context = context;
        this.packageName = packageName;

    }


    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {
        mDialog = new AlertDialog.Builder(context);

        String Version = null;

        try {
            Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=" + packageName).timeout(5000).get();

            Version = doc.select(".hAyfc .htlgb").get(7).ownText();
            Log.d("Version", Version);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return Version;

    }


    @Override
    protected void onPostExecute(String result) {
        try {

            if (result != null) {
                if (!result.equals(APP_VERSION_NAME)) { //APP_VERSION_NAME는 현재 앱의
                    mDialog.setMessage("최신 버전이 출시되었습니다. 업데이트 후 사용 가능합니다.")
                            .setCancelable(false)
                            .setPositiveButton("업데이트 바로가기",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int id) {
                                            Intent marketLaunch = new Intent(
                                                    Intent.ACTION_VIEW);
                                            marketLaunch.setData(Uri
                                                    .parse(STORE_URL));
                                            context.startActivity(marketLaunch);
                                            ((SplashActivity) context).finish();
                                        }
                                    });
                    AlertDialog alert = mDialog.create();
                    alert.setTitle("업데이트 알림");
                    alert.show();
                } else {
                    nextPage();
                }
            }
            super.onPostExecute(result);

        } catch (Exception e) {
            e.printStackTrace();
            nextPage();
        }


    }


    private void nextPage(){
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((SplashActivity) context).finish();
    }

}
        /*PackageInfo pi = null;

        try {
            pi = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        version = pi.versionName;
        marketVersion = result;

        if (!version.equals(marketVersion)) {
            mDialog.setMessage("업데이트 후 사용해주세요.")
                    .setCancelable(false)
                    .setPositiveButton("업데이트 바로가기",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    Intent marketLaunch = new Intent(
                                            Intent.ACTION_VIEW);
                                    marketLaunch.setData(Uri
                                            .parse("https://play.google.com/store/apps/details?id="+packageName));
                                    context.startActivity(marketLaunch);
                                    //finish();
                                }
                            });
            AlertDialog alert = mDialog.create();
            alert.setTitle("안 내");
            alert.show();
        }

        super.onPostExecute(result);
    }*/

