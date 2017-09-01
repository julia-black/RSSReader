package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.widget.Toolbar;
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

import ru.sgu.csiit.sgu17.db.SguDbContract;
import ru.sgu.csiit.sgu17.db.SguDbHelper;

public class PreviewFragment extends Fragment{
    private static final String LOG_TAG = "PreviewFragment";

    private ImageView imageView;
    private TextView textTitle;
    private TextView textDescript;
    private TextView textDate;
    private Article article;
    private MenuItem item;
    private boolean flagFavourite;

   public PreviewFragment(Article article, boolean flagFavourite) {
       this.flagFavourite = flagFavourite;
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
        inflater.inflate(R.menu.menu_main, menu);
        item = menu.findItem(R.id.action_favorite_main);
        item.setVisible(true);

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_favorite_main){
            onFavouriteClicked();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.preview_article, container, false);

        Log.i(LOG_TAG, "article = " + article.guid);

        Toolbar toolbar = (Toolbar)getActivity().findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle("Article");
        }

        this.textTitle = (TextView) v.findViewById(R.id.title_article);
        this.textDescript = (TextView) v.findViewById(R.id.description);
        this.textDate = (TextView) v.findViewById(R.id.pub_date);
        this.imageView = (ImageView) v.findViewById(R.id.imageNews);

        this.textTitle.setText(article.title);
        this.textDescript.setText(article.description);
        this.textDate.setText(article.pubDate);
        String urlImage = article.link.split(" ")[1];

        Log.i(LOG_TAG, article.title + " " + article.isFirst + " " + article.isLast);
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

        if(flagFavourite){
           getFragmentManager().beginTransaction()
                   .add(R.id.containerFavourite, webFragment)
                   .addToBackStack(null)
                   .commit();
       }
        else {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, webFragment)
                    .addToBackStack(null)
                    .commit();
       }
    }


    public void onFavouriteClicked() {
        //если мы находимся просто в превью, а не в превью фаворита, то нажатие на сердечко вызывает добавление фаворита
        //иначе - удаление из фаворитов
       SQLiteDatabase db = new SguDbHelper(getActivity()).getWritableDatabase();
       db.beginTransaction();
       if(!flagFavourite)
       {
           try {
               ContentValues cv = new ContentValues();
               cv.put(SguDbContract.COLUMN_GUID, article.guid);
               cv.put(SguDbContract.COLUMN_TITLE, article.title);
               cv.put(SguDbContract.COLUMN_DESCRIPTION, article.description);
               cv.put(SguDbContract.COLUMN_LINK, article.link);
               cv.put(SguDbContract.COLUMN_PUBDATE, article.pubDate);

               long insertedId = db.insertWithOnConflict(SguDbContract.TABLE_NAME,
                       null, cv, SQLiteDatabase.CONFLICT_IGNORE);
               if (insertedId == -1L)
                   Log.i(LOG_TAG, "skipped article guid = " + article.guid);
               db.setTransactionSuccessful();
               Log.i(LOG_TAG, "Success adding in DB");
           } finally {
               db.endTransaction();
               db.close();
           }
           Log.i(LOG_TAG, "Add favourite " + article.guid);
        }
        else {
            try {
                int delCount = db.delete(SguDbContract.TABLE_NAME, SguDbContract.COLUMN_LINK + "='" + article.link + "'", null);
                db.setTransactionSuccessful();
                Log.i(LOG_TAG, "Count deleting strings = " + delCount);
            } finally {
                db.endTransaction();
                db.close();
            }
           FavouriteFragment fragment = new FavouriteFragment();
           getFragmentManager().beginTransaction()
                   .remove(this)
                   .add(R.id.containerFavourite, fragment)
                   //.addToBackStack(null)
                   .commit();
        }
    }
}
