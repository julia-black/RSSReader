package ru.sgu.csiit.sgu17;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class NewsListActivity extends AppCompatActivity
        implements NewsListFragment.Listener {

    private static final String LOG_TAG = "NewsListActivity";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //сам определяет горзонт. или верт.
        setContentView(R.layout.news_list_activity); //обертка над фрагментом
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.news_list_activity);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_settings){
            OnPreferencesClicked();
        }
        else
            if(id == R.id.action_favoriteList){
                OnFavouriteListClicked();
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
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            Log.i(LOG_TAG, "click on Article");
           //boolean flagFavourite = false;
           PreviewFragment fragment = new PreviewFragment(article, false);

           // if(flagFavourite){
           //     getFragmentManager().beginTransaction()
           //             .add(R.id.containerFavourite, fragment)
           //             .addToBackStack(null)
           //             .commit();
           // } else {
                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment) //добавляем в контейнер
                        .addToBackStack(null) //чтобы можно было нажать назад и вернуться обратно
                        .commit();
           // }
        }
        else {
            WebFragment f = (WebFragment) getFragmentManager()
                    .findFragmentById(R.id.preview_fragment);
            f.getArguments().putString("url", article.link.split(" ")[0]);
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

    @Override
    public void OnFavouriteListClicked(){
        Log.i(LOG_TAG, "click on favouriteList");
        Intent intent = new Intent(NewsListActivity.this, FavouriteActivity.class);
        startActivity(intent);
    }
}
