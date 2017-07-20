package ru.sgu.csiit.sgu17;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juli on 20.07.2017.
 */

public class FavouriteActivity extends AppCompatActivity   {

    private List<Article> favouriteArticles;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favouriteArticles = new ArrayList<Article>();
        setContentView(R.layout.favourite_activity);
    }

    public void addFavourit(Article article){
        favouriteArticles.add(article);
    }

  // @Override
  // public void OnArticleClicked(Article article) {
  //     if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

  //         PreviewFragment fragment = new PreviewFragment(article);
  //         getFragmentManager().beginTransaction()
  //                 .add(R.id.container, fragment)
  //                 .addToBackStack(null)
  //                 .commit();
  //     }
  //     else {
  //         WebFragment f = (WebFragment) getFragmentManager()
  //                 .findFragmentById(R.id.preview_fragment);
  //         f.getArguments().putString("url", article.link.split(" ")[0]);
  //         f.reload();
  //     }
  // }

  // @Override
  // public void OnPreferencesClicked() {
  //     PrefsFragment f = new PrefsFragment();
  //     getFragmentManager().beginTransaction()
  //             .add(R.id.container, f)
  //             .addToBackStack(null)
  //             .commit();
  // }

}