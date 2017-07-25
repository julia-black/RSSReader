package ru.sgu.csiit.sgu17;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import ru.sgu.csiit.sgu17.db.SguDbContract;
import ru.sgu.csiit.sgu17.db.SguDbHelper;

/**
 * Created by Juli on 21.07.2017.
 */

public class DataLoaderForAll extends AsyncTaskLoader<List<Article>> {

    private static final String URL = "http://www.sgu.ru/news.xml";
    private static final String LOG_TAG = "DataLoaderForAll";

    private List<Article> data;

    DataLoaderForAll(Context context) {
        super(context);
    }

    @Override
    protected void onStartLoading() {
        if (data != null) {
            deliverResult(data);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Article> loadInBackground() {

        data = null;
        try {
            URL url = new URL("http://sgu.ru/news.xml");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            try {
                InputStream istream = conn.getInputStream();
                try {
                    istream.read(new byte[1]);
                    String httpResponse = NetUtils.httpGet(URL);
                    data = RssUtils.parseRss(httpResponse);

                    //Log.i(LOG_TAG, "Data loaded. Count = " + data.size());
                } finally {
                    istream.close();
                }
            } finally {
                conn.disconnect();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return data;
      //  List<Article> res = null;
      //  SQLiteDatabase db = new SguDbHelper(getContext()).getReadableDatabase();
      //  Cursor cursor = db.query(SguDbContract.TABLE_NAME, new String[]{
      //          SguDbContract.COLUMN_TITLE,
      //          SguDbContract.COLUMN_DESCRIPTION,
      //          SguDbContract.COLUMN_PUBDATE,
      //          SguDbContract.COLUMN_LINK
      //  }, null, null, null, null, SguDbContract.COLUMN_PUBDATE + " DESC");
      //  try {
      //      res = new ArrayList<>();
      //      while (cursor.moveToNext()) {
      //          Article article = new Article();
      //          article.title = cursor.getString(0);
      //          article.description = cursor.getString(1);
      //          article.pubDate = cursor.getString(2);
      //          article.link = cursor.getString(3);
//
      //          res.add(article);
      //      }
      //  } finally {
      //      cursor.close();
      //      db.close();
      //  }
      //  Log.d(LOG_TAG, "load finished");
      //  return res;
    }

    @Override
    protected void onReset() {
        this.data = null;
    }
}