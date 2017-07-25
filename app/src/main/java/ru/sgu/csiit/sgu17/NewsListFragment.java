package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.Loader;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import ru.sgu.csiit.sgu17.service.RefreshService;

public class NewsListFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Article>>, SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout swipeRefreshLayout;

    private static final String LOG_TAG = "NewsListActivity";

    private final RefreshBroadcastReceiver refreshBroadcastReceiver = new RefreshBroadcastReceiver();
    public static List<Article> data = new ArrayList<>();
    private NewsItemAdapter dataAdapter;


    @Override
    public void onRefresh() {
       Intent serviceIntent = new Intent(getActivity(), RefreshService.class);
       getActivity().startService(serviceIntent);
       swipeRefreshLayout.setRefreshing(false);
    }

    private final class RefreshBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isResumed()) {
                getActivity().getLoaderManager().restartLoader(0, null, NewsListFragment.this);
                Toast.makeText(getActivity(), "Data refreshed", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    public interface Listener {
        void OnArticleClicked(Article article);
        void OnPreferencesClicked();
        void OnFavouriteListClicked();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.dataAdapter = new NewsItemAdapter(getActivity(), data);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.news_list_fragment, container, false);

        ListView newsList = (ListView) v.findViewById(R.id.news_list);
        newsList.setAdapter(dataAdapter);

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
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        swipeRefreshLayout.setColorSchemeColors(Color.RED, Color.GREEN, Color.BLUE, Color.CYAN);
        getLoaderManager().initLoader(0, null, this);
        return v;
    }



    @Override
    public void onStart() {
        super.onStart();
        IntentFilter intentFilter = new IntentFilter(RefreshService.REFRESH_ACTION);
        getActivity().registerReceiver(refreshBroadcastReceiver, intentFilter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(refreshBroadcastReceiver);
    }

    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader");
        //return new SguRssLoader(getActivity());
        return new DataLoaderForAll(getActivity());
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> loaderData) {
        Log.d(LOG_TAG, "onLoadFinished " + loader.hashCode());
        data.clear();
        data.addAll(loaderData);
        Log.i(LOG_TAG, dataAdapter.getSizeArray() + " count");
        if(dataAdapter.getSizeArray() > 0 ){
            getView().findViewById(R.id.textNotArticles).setVisibility(View.GONE);
        }
        else {
                getView().findViewById(R.id.textNotArticles).setVisibility(View.VISIBLE);
        }
        dataAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {
        Log.d(LOG_TAG, "onLoaderReset " + loader.hashCode());
    }

}
