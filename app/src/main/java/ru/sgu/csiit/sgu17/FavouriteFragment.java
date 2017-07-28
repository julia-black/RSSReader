package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.Loader;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import ru.sgu.csiit.sgu17.db.SguDbContract;
import ru.sgu.csiit.sgu17.db.SguDbHelper;


public class FavouriteFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Article>> {

    private static final String LOG_TAG = "FavouriteFragment";

    public interface Listener {
        void OnArticleClicked(Article article);
        void OnPreferencesClicked();
        void OnFavouriteListClicked();
        void OnNewsListClicked();
    }
    public static List<Article> favouriteArticles = new ArrayList<>();

    private NewsItemAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dataAdapter = new NewsItemAdapter(getActivity(), favouriteArticles);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.favourite_list_fragment, container, false);
        ListView newsList = (ListView) v.findViewById(R.id.favourite_list);
        newsList.setAdapter(dataAdapter);

        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(R.string.action_favoriteList);
        }
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Article article = (Article) parent.getItemAtPosition(position);
                if (isResumed()) {
                    Listener l = (Listener) getActivity();
                    l.OnArticleClicked(article);
                }
            }
        });
        getLoaderManager().initLoader(0, null, this);
        return v;
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
         Log.d(LOG_TAG, "onCreateLoader");
         return new SguRssLoader(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {
       Log.d(LOG_TAG, "onLoadFinished " + loader.hashCode());
       favouriteArticles.clear();
       favouriteArticles.addAll(data);
       Log.i(LOG_TAG, favouriteArticles.size() + "");

       if(favouriteArticles.size() > 0 ){
           getView().findViewById(R.id.textNotFavourite).setVisibility(View.GONE);
       }
       else {
           getView().findViewById(R.id.textNotFavourite).setVisibility(View.VISIBLE);
       }
       dataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.d(LOG_TAG, "onResetLoader");
    }
}
