package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;


public class NewsListActivity extends AppCompatActivity
        implements NewsListFragment.Listener{

    private static final String LOG_TAG = "NewsListActivity";

    private Drawer.Result drawerResult = null;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //сам определяет горзонт. или верт.
        setContentView(R.layout.news_list_activity); //обертка над фрагментом

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(R.string.action_newsBlog);
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
                        InputMethodManager inputMethodManager = (InputMethodManager) NewsListActivity.this.getSystemService(Activity.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(NewsListActivity.this.getCurrentFocus().getWindowToken(), 0);
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
           PreviewFragment fragment = new PreviewFragment(article, false);

                getFragmentManager().beginTransaction()
                        .add(R.id.container, fragment) //добавляем в контейнер
                        .addToBackStack(null) //чтобы можно было нажать назад и вернуться обратно
                        .commit();
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
        PrefsFragment f = new PrefsFragment(false);
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

    @Override
    public void OnNewsListClicked() {
        NewsListFragment fragment = new NewsListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
