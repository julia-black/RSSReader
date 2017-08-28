package ru.sgu.csiit.sgu17;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

class NewsItemAdapter extends BaseAdapter {

    private final Context context;
    private final List<Article> data;
    private final LayoutInflater inflater;

    NewsItemAdapter(Context context, List<Article> data) {
        this.context = context;
        this.data = data;
        this.inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public int getSizeArray(){
        return data.size();
    }


    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Article art = (Article) getItem(position);
        View v;
        //если эта View никогда не использовалась
        if (convertView == null) {
            v = inflater.inflate(R.layout.news_list_item, parent, false);
            ViewHolder holder = new ViewHolder();
            holder.topDate = (TextView) v.findViewById(R.id.topDate);
            holder.titleView = (TextView) v.findViewById(R.id.title);
            holder.pubDateView = (TextView) v.findViewById(R.id.pub_date);
            holder.imageNews = (ImageView) v.findViewById(R.id.imageNews);
            v.setTag(holder);
        } else {
            v = convertView;
        }

        ViewHolder holder = (ViewHolder) v.getTag();
        holder.titleView.setText(art.title);
        holder.pubDateView.setText(art.pubDate);

        String urlImage = art.link.split(" ")[1];
        Glide.with(context)
                .load(urlImage)
                .into(holder.imageNews);

        return v;
    }

    private static final class ViewHolder {
        private TextView titleView;
        private TextView pubDateView;
        private ImageView imageNews;
        private TextView topDate;
    }

}
