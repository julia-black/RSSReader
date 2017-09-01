package ru.sgu.csiit.sgu17;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableContainer;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

class NewsItemAdapter extends BaseAdapter {

    private final Context context;
    private final List<Article> data;
    private final LayoutInflater inflater;
    private boolean isFavouriteList;

    NewsItemAdapter(Context context, List<Article> data, boolean isFavouriteList) {
        this.context = context;
        this.data = data;
        this.isFavouriteList = isFavouriteList;

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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //for (int i = 0; i < data.size(); i++) {
        //    Log.i("Adapter", data.get(i).title + " " + data.get(i).isFirst + " " + data.get(i).isLast);
        //}
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
            holder.layoutItem = (RelativeLayout) v.findViewById(R.id.layout_item);
            holder.separator = (ImageView) v.findViewById(R.id.separator);
            v.setTag(holder);
        } else {
            v = convertView;
        }

        ViewHolder holder = (ViewHolder) v.getTag();
        holder.titleView.setText(art.title);
        holder.pubDateView.setText(art.pubDate.substring(11,art.pubDate.length()));

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String date = dateFormat.format(new Date());

        if(!isFavouriteList) {
            if (art.isFirst) {
                holder.topDate.setVisibility(View.VISIBLE);
                holder.layoutItem.setElevation(0);
                holder.layoutItem.setBackgroundResource(R.drawable.roundcorner_top);

                if (date.equals(art.pubDate.substring(0, 10))) {
                    holder.topDate.setText("Today");
                } else {
                    holder.topDate.setText(art.pubDate.substring(0, 10));
                }
            } else if (art.isLast) {
                holder.layoutItem.setBackgroundResource(R.drawable.roundcorner_bottom);
                holder.layoutItem.setElevation(5);
                holder.separator.setVisibility(View.INVISIBLE);
            }
        }
        else {
            holder.topDate.setVisibility(View.VISIBLE);
            holder.topDate.setText(art.pubDate.substring(0, 10));
            if(data.size() > 0) {
                if (art.title == data.get(0).title){
                    holder.layoutItem.setBackgroundResource(R.drawable.roundcorner_top);
                }
                if (art.title == data.get(data.size() - 1).title) {
                        holder.layoutItem.setBackgroundResource(R.drawable.roundcorner_bottom);
                        holder.separator.setVisibility(View.INVISIBLE);
                }
                if(art.title == data.get(data.size() - 1).title && art.title == data.get(0).title){
                    holder.layoutItem.setBackgroundResource(R.drawable.roundcorner);
                }

            }
        }
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
        public RelativeLayout layoutItem;
        public ImageView separator;
    }

}
