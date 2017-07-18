package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;

import java.util.concurrent.TimeUnit;

public class NewsListActivity extends Activity
        implements NewsListFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity);

    }

    @Override
    public void OnArticleClicked(Article article) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
           // WebFragment webFragment = new WebFragment();
           // webFragment.getArguments().putString("url", article.link.split(" ")[0]);


          // getFragmentManager().beginTransaction()
          //         .add(R.id.container, webFragment)
          //         .addToBackStack(null)
          //         .commit();

            PreviewFragment previewFragment = new PreviewFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.container, previewFragment)
                    .addToBackStack(null)
                    .commit();
           // previewFragment.getArguments().putString("url", article.link.split(" ")[0]);
        } else {
            PreviewFragment f = (PreviewFragment) getFragmentManager()
                    .findFragmentById(R.id.preview_fragment);
            f.getArguments().putString("url", article.link);
            f.reload();
        }
    }

    @Override
    public void OnPreferencesClicked() {
        PrefsFragment f = new PrefsFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.container, f)
                .addToBackStack(null)
                .commit();
    }
}
