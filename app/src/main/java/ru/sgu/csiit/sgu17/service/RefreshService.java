package ru.sgu.csiit.sgu17.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.sgu.csiit.sgu17.Article;
import ru.sgu.csiit.sgu17.NetUtils;
import ru.sgu.csiit.sgu17.NewsListActivity;
import ru.sgu.csiit.sgu17.NewsListFragment;
import ru.sgu.csiit.sgu17.R;
import ru.sgu.csiit.sgu17.RssUtils;
import ru.sgu.csiit.sgu17.db.SguDbContract;
import ru.sgu.csiit.sgu17.db.SguDbHelper;

public class RefreshService extends Service {

    public static final String REFRESH_ACTION = "ru.sgu.csiit.sgu17.service" +
            ".RefreshService.ACTION_REFRESH";

    private static final String LOG_TAG = "RefreshService";
    private static final String URL = "http://www.sgu.ru/news.xml";

    private Thread refreshThread;
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private final Runnable refreshRunnable = new Runnable() {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                try { //бесконечный цикл, чтобы если внешнй компонент запросит, мы не зависли

                    int period = 10_000;
                    if(!isRefresh()) { //если обновили пушем
                        period = getPeriodUpdate();
                        switch (period) {
                            case 15:
                                period = 15 * 60 * 1000; //переводим в милисекунды
                                break;
                            case 30:
                                period = 30 * 60 * 1000;
                                break;
                            case 1:
                                period = 3_600_000;
                                break;
                            case 0:
                                period = 0;
                                break;
                        }
                        Thread.sleep(period);
                    }

                    boolean loadAllowed;

                    if(period == 0)
                        loadAllowed = false;
                    else
                        loadAllowed = true;

                    loadAllowed = loadAllowed && (!isWiFiOnly() || isWifiConnected());
                    if (loadAllowed) {
                        Log.i(LOG_TAG, "load data...");
                        NewsListFragment.data = loadData();
                    }
                    //ставим обратно refresh = false
                    SharedPreferences prefs = getSharedPreferences(
                            NewsListActivity.class.getSimpleName(), MODE_PRIVATE);

                    prefs
                            .edit()
                            .putBoolean("refresh", false)
                            .apply();

                    //Период обновления
                    Thread.sleep(10_000);

               } catch (InterruptedException e) {
                   e.printStackTrace();
               }
            }
        }
    };

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(LOG_TAG, "onCreate()");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(LOG_TAG, "onStartCommand()");
        if (intent != null && refreshThread == null) {
            this.refreshThread = new Thread(refreshRunnable);
            refreshThread.start();
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy()");
    }

    private void onPostRefresh() {
        Log.d(LOG_TAG, "data refreshed");
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(REFRESH_ACTION);
        sendBroadcast(broadcastIntent);//отправляем broadcast
        sendDataRefreshedNotification();
    }

    private List<Article> loadData(){
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                showInProgressNotification();
            }
        });
        List<Article> netData = null;
        try {
            String httpResponse = NetUtils.httpGet(URL);
            netData = RssUtils.parseRss(httpResponse);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Failed to get HTTP response: " + e.getMessage(), e);
        } catch (XmlPullParserException e) {
            Log.e(LOG_TAG, "Failed to parse RSS: " + e.getMessage(), e);
        }
        try {
            Thread.sleep(5_000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mainHandler.post(new Runnable() {
            @Override
            public void run() {
                hideInProgressNotification();
                onPostRefresh();
            }
        });
        return netData;
    }

    private void sendDataRefreshedNotification() {
        //интент который нах-ся "в производстве", он используется, чтобы появлялся интент только по нажатию
        if (isNotificationsEnabled()) {
            Intent startIntent = new Intent(this, NewsListActivity.class);
            PendingIntent notificationIntent = PendingIntent.getActivity(
                    this, 0, startIntent, PendingIntent.FLAG_UPDATE_CURRENT);//флаг - старый интент удалиться и будет заменен на новый
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("SGU RSS data refreshed")
                    .setContentText("Press notification to open")
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                    .setContentIntent(notificationIntent)
                    .setAutoCancel(true)
                    .build();
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.notify(1, notification);
        }
    }

    private void showInProgressNotification() {
        if (isNotificationsEnabled()) {
            Notification notification = new Notification.Builder(this)
                    .setContentTitle("SGU RSS data is refreshing")
                    .setContentText("Wait until complete")
                    .setSmallIcon(R.drawable.icon)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.icon))
                    .setOngoing(true)
                    .build();
            startForeground(1, notification);//1 - id
        }
    }

    private void hideInProgressNotification() {
        stopForeground(false);
    }

    private boolean isNotificationsEnabled() {
        SharedPreferences prefs = getSharedPreferences(
                NewsListActivity.class.getSimpleName(), MODE_PRIVATE);
        return prefs.getBoolean("notifications", true);
    }

    private boolean isPeriodicUpdatesEnabled() {
        SharedPreferences prefs = getSharedPreferences(
                NewsListActivity.class.getSimpleName(), MODE_PRIVATE);
        return prefs.getBoolean("periodicUpdates", true);
    }

    private boolean isWiFiOnly() {
        SharedPreferences prefs = getSharedPreferences(
                NewsListActivity.class.getSimpleName(), MODE_PRIVATE);
        return prefs.getBoolean("wifiOnly", false);
    }

    private boolean isWifiConnected() {
        ConnectivityManager connManager = (ConnectivityManager)
                getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = connManager.getActiveNetworkInfo();
        return netInfo != null
                && netInfo.isConnected()
                && netInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    private int getPeriodUpdate(){
        SharedPreferences prefs = getSharedPreferences(
                NewsListActivity.class.getSimpleName(), MODE_PRIVATE);
        return prefs.getInt("periodUpdate", 0);
    }
    private boolean isUseLocation(){
        SharedPreferences prefs = getSharedPreferences(
                NewsListActivity.class.getSimpleName(), MODE_PRIVATE);
        return prefs.getBoolean("location", false);
    }
    private boolean isRefresh(){
        SharedPreferences prefs = getSharedPreferences(
                NewsListActivity.class.getSimpleName(), MODE_PRIVATE);
        return prefs.getBoolean("refresh", false);
    }
}
