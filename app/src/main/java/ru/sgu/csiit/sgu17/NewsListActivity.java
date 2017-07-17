package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

import java.util.concurrent.TimeUnit;

public class NewsListActivity extends Activity
        implements NewsListFragment.Listener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.news_list_activity);
        //setContentView(R.layout.logo_fragment);
       //try {
       //    TimeUnit.SECONDS.sleep(5);
       //    setContentView(R.layout.news_list_activity);
       //} catch (InterruptedException e) {
       //    e.printStackTrace();
       //}
        setContentView(R.layout.news_list_activity);

    }

    @Override
    public void OnArticleClicked(Article article) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            PreviewFragment fragment = new PreviewFragment();
            fragment.getArguments().putString("url", article.link);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, fragment)
                    .addToBackStack(null)
                    .commit();
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
