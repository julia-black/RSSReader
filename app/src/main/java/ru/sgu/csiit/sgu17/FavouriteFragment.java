package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;

import java.util.List;

/**
 * Created by Juli on 20.07.2017.
 */

public class FavouriteFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<Article>> {
    @Override
    public Loader<List<Article>> onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<Article>> loader, List<Article> data) {

    }

    @Override
    public void onLoaderReset(Loader<List<Article>> loader) {

    }
}
