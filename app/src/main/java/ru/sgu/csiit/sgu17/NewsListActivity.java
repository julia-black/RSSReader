package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
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

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    public ActionBarDrawerToggle drawerToggle;
    private boolean mToolBarNavigationListenerIsRegistered = false;

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        try {
                            selectDrawerItem(menuItem);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                        return true;
                    }
                });
    }

    public void selectDrawerItem(MenuItem menuItem) throws IllegalAccessException, InstantiationException {
        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment: {
                OnNewsListClicked();
                break;
            }
            case R.id.nav_second_fragment:
                OnFavouriteListClicked();
                break;
            case R.id.nav_third_fragment:
                OnPreferencesClicked();
                break;
            default:
                break;
        }
        menuItem.setChecked(true);
        mDrawer.closeDrawers();
    }

    private static final String LOG_TAG = "NewsListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //сам определяет горзонт. или верт.
        setContentView(R.layout.news_list_activity); //обертка над фрагментом

       toolbar = (Toolbar) findViewById(R.id.toolbar);
       if(toolbar != null) {
           toolbar.setTitle(R.string.action_newsBlog);
           setSupportActionBar(toolbar);
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
           if (savedInstanceState != null) {
               resolveUpButtonWithFragmentStack();
           }
       }
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        nvDrawer.setItemTextColor(ColorStateList.valueOf(Color.DKGRAY));
        setupDrawerContent(nvDrawer);
        drawerToggle = setupDrawerToggle();
        mDrawer.addDrawerListener(drawerToggle);
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            OnNewsListClicked();
        }
    }

    private void resolveUpButtonWithFragmentStack() {
        showUpButton(getSupportFragmentManager().getBackStackEntryCount() > 0);
    }

    private void showUpButton(boolean show) {
        if(show){
            drawerToggle.setDrawerIndicatorEnabled(false);
            if(toolbar != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }
            if(!mToolBarNavigationListenerIsRegistered) {
                drawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();

                    }
                });
                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {
            // Remove back button
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            // Show hamburger
            drawerToggle.setDrawerIndicatorEnabled(true);
            // Remove the/any drawer toggle listener
            drawerToggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
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
            else if (id == android.R.id.home) {
                onBackPressed();
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
        showUpButton(true);
    }

    @Override
    public void OnPreferencesClicked() {
        PrefsFragment f = new PrefsFragment(false);
        getFragmentManager().beginTransaction()
                .add(R.id.container, f)
                .addToBackStack(null)
                .commit();
        showUpButton(true);
    }

    @Override
    public void OnFavouriteListClicked(){
        Log.i(LOG_TAG, "click on favouriteList");
        Intent intent = new Intent(NewsListActivity.this, FavouriteActivity.class);
        startActivity(intent);
        showUpButton(false);
    }

    @Override
    public void OnNewsListClicked() {
        drawerToggle.setDrawerIndicatorEnabled(true);
        NewsListFragment fragment = new NewsListFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

}
