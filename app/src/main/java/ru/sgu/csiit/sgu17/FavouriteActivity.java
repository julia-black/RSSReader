package ru.sgu.csiit.sgu17;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Juli on 20.07.2017.
 */

public class FavouriteActivity extends AppCompatActivity implements NewsListFragment.Listener  {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favourite_activity);
    }

    @Override
    public void OnArticleClicked(Article article) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {

            boolean flagFavourite = true;
            PreviewFragment fragment = new PreviewFragment(article, flagFavourite);
            getFragmentManager().beginTransaction()
                    .add(R.id.containerFavourite, fragment) //добавляем в контейнер
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

    }

    @Override
    public void OnFavouriteListClicked() {

    }
}
