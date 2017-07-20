package ru.sgu.csiit.sgu17;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PreviewFragment extends Fragment {

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
        LoadImage loadImage = new LoadImage(article.link.split(" ")[1], this.imageView);
        loadImage.execute();

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
}
