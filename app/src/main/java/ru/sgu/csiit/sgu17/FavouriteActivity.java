package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class FavouriteActivity extends AppCompatActivity implements FavouriteFragment.Listener{

    private static final String LOG_TAG = "FavouriteActivity";

    private Drawer.Result drawerResult = null;

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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(R.string.action_favoriteList);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        drawerResult = new Drawer()
                .withActivity(this)
                .withActionBarDrawerToggle(true)
                .withToolbar(toolbar)
                .withHeader(R.layout.drawer_header)
                .addDrawerItems(new PrimaryDrawerItem().withName(R.string.action_newsBlog).withIcon(FontAwesome.Icon.faw_rss).withIdentifier(0),
                        new PrimaryDrawerItem().withName(R.string.action_favoriteList).withIcon(FontAwesome.Icon.faw_heart).withIdentifier(1),
                        new PrimaryDrawerItem().withName(R.string.action_prefs).withIcon(FontAwesome.Icon.faw_cog).withIdentifier(2),
                        new PrimaryDrawerItem().withName(""),
                        new PrimaryDrawerItem().withName(""),
                        new PrimaryDrawerItem().withName(""),
                        new PrimaryDrawerItem().withName(""),
                        new PrimaryDrawerItem().withName(""),
                        new CustomPrimaryDrawerItem()
                )
                .withOnDrawerListener(new Drawer.OnDrawerListener() {
                    @Override
                    public void onDrawerOpened(View drawerView) {
                        //Скрытие клавиатуры при открытие drawer'a
                        InputMethodManager inputMethodManager = (InputMethodManager) FavouriteActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(FavouriteActivity.this.getCurrentFocus().getWindowToken(), 0);
                    }

                    @Override
                    public void onDrawerClosed(View drawerView) {

                    }
                })
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener(){

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id, IDrawerItem drawerItem) {
                        //News Blog
                        if(drawerItem.getIdentifier() == 0){
                            OnNewsListClicked();
                        }
                        else //Favourite
                            if (drawerItem.getIdentifier() == 1){
                                OnFavouriteListClicked();
                            }
                            else //Setting
                                if(drawerItem.getIdentifier() == 2){
                                    OnPreferencesClicked();
                                }
                    }
                })
                .build();
    }

    @Override
    public void onBackPressed() {
        // Закрываем Navigation Drawer по нажатию системной кнопки "Назад" если он открыт
        if (drawerResult.isDrawerOpen()) {
            drawerResult.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
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
    @Override
    public void OnPreferencesClicked() {
        PrefsFragment f = new PrefsFragment(true);
        getFragmentManager().beginTransaction()
                .add(R.id.containerFavourite, f)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnFavouriteListClicked(){
        FavouriteFragment fragment = new FavouriteFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.containerFavourite, fragment)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void OnNewsListClicked() {
        Intent intent = new Intent(FavouriteActivity.this, NewsListActivity.class);
        startActivity(intent);

    }
}
