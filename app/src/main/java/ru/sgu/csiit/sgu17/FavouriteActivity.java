package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class FavouriteActivity extends AppCompatActivity implements FavouriteFragment.Listener{

    private static final String LOG_TAG = "FavouriteActivity";

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_favourite, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_newsBlog){
            OnNewsBlogClicked();
        }
        return super.onOptionsItemSelected(item);
    }

    private void OnNewsBlogClicked() {
        Log.i(LOG_TAG, "click on News Blog");
        Intent intent = new Intent(FavouriteActivity.this, NewsListActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_activity);
    }

    @Override
    public void OnArticleClicked(Article article) {
      Log.i(LOG_TAG, "click on article");
     if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
         PreviewFragment fragment = new PreviewFragment(article, true);

         getFragmentManager().beginTransaction()
                 .add(R.id.containerFavourite, fragment)
                 .addToBackStack(null)
                 .commit();
         Log.i(LOG_TAG, "add in container " + article.guid + " " + article.title);
     } else {
         WebFragment f = (WebFragment) getFragmentManager()
                 .findFragmentById(R.id.preview_fragment);
         f.getArguments().putString("url", article.link.split(" ")[0]);
         f.reload();
     }
    }
}
