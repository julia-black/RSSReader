package ru.sgu.csiit.sgu17;

import android.app.Activity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
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

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;


public class FavouriteActivity extends AppCompatActivity implements FavouriteFragment.Listener{

    private static final String LOG_TAG = "FavouriteActivity";

    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;
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
        else if (id == android.R.id.home) {
            onBackPressed();
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle(R.string.action_favoriteList);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
            OnFavouriteListClicked();
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
     showUpButton(true);
    }
    @Override
    public void OnPreferencesClicked() {
        PrefsFragment f = new PrefsFragment(true);
        getFragmentManager().beginTransaction()
                .add(R.id.containerFavourite, f)
                .addToBackStack(null)
                .commit();
        showUpButton(true);
    }

    @Override
    public void OnFavouriteListClicked(){
        FavouriteFragment fragment = new FavouriteFragment();
        getFragmentManager().beginTransaction()
                .replace(R.id.containerFavourite, fragment)
                .addToBackStack(null)
                .commit();
        showUpButton(false);
    }

    @Override
    public void OnNewsListClicked() {
        Intent intent = new Intent(FavouriteActivity.this, NewsListActivity.class);
        startActivity(intent);
        showUpButton(false);
    }
}
