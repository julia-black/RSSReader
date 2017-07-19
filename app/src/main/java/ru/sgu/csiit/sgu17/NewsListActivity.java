package ru.sgu.csiit.sgu17;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class NewsListActivity extends AppCompatActivity
        implements NewsListFragment.Listener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_list_activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            OnPreferencesClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void OnArticleClicked(Article article) {
        //if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            PreviewFragment previewFragment = new PreviewFragment(article);
            getFragmentManager().beginTransaction()
                    .add(R.id.container, previewFragment)
                    .addToBackStack(null)
                    .commit();
       // }
       // else {
          //PreviewFragment previewFragment = (PreviewFragment) getFragmentManager()
          //        .findFragmentById(R.id.preview_fragment);
       // }
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
