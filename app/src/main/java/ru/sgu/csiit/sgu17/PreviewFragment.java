package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class PreviewFragment extends Fragment {
    private static final String LOG_TAG = "PreviewFragment";

    private ImageView imageView;
    private TextView textTitle;
    private TextView textDescript;
    private TextView textDate;
    private Article article;


    public PreviewFragment(Article article) {
        this.article = article;
        setArguments(new Bundle());
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_preview, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_favorite){
            onAddFavouriteClicked();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.preview_article, container, false);


        this.textTitle = (TextView) v.findViewById(R.id.title_article);
        this.textDescript = (TextView) v.findViewById(R.id.description);
        this.textDate = (TextView) v.findViewById(R.id.pub_date);
        this.imageView = (ImageView) v.findViewById(R.id.imageNews);

        this.textTitle.setText(article.title);
        this.textDescript.setText(article.description);
        this.textDate.setText(article.pubDate);
        String urlImage = article.link.split(" ")[1];

        Glide.with(getActivity())
                .load(urlImage)
                .into(imageView);

        v.findViewById(R.id.linkReadMore).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onReadMoreClicked();
            }
        });

        return v;
    }
    public void onReadMoreClicked(){
        WebFragment webFragment = new WebFragment();
        webFragment.getArguments().putString("url", article.link.split(" ")[0]);
        getFragmentManager().beginTransaction()
                .add(R.id.container, webFragment)
                .addToBackStack(null)
                .commit();

    }

    public void onAddFavouriteClicked(){
        FavouriteFragment.addFavourite(article);
        //FavouriteFragment.favouriteArticles.add(article);
        Log.i(LOG_TAG, "Add favourite " + article.title);
    }
}
