package ru.sgu.csiit.sgu17;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class RssUtils {

    public static List<Article> parseRss(String rss) throws XmlPullParserException, IOException {
        final ArrayList<Article> res = new ArrayList<>();
        final XmlPullParser parser = Xml.newPullParser();
        parser.setInput(new ByteArrayInputStream(rss.trim().getBytes("UTF-8")), "UTF-8");

        while (!"channel".equals(parser.getName()))
            parser.next();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG)
                continue;
            String name = parser.getName();
            String urlImage = "";
            if ("item".equals(name)) {
                parser.require(XmlPullParser.START_TAG, null, "item");
                //Log.i("UriDataLoader", "Found item " + parser.getText());

                Article article = new Article();
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG)
                        continue;
                    String itemEntry = parser.getName();
                    if ("guid".equals(itemEntry)) {
                        article.guid = Long.parseLong(parser.nextText());
                    } else if ("title".equals(itemEntry)) {
                        article.title = parser.nextText();
                    } else if ("description".equals(itemEntry)) {
                        article.description = parser.nextText();
                    } else if ("pubDate".equals(itemEntry)) {
                        article.pubDate = parser.nextText();
                    } else if ("link".equals(itemEntry)) {
                        article.link = parser.nextText();
                    } else if("enclosure".equals(itemEntry)){
                        //article.urlImage = parser.getAttributeValue(0);
                        urlImage = parser.getAttributeValue(0);
                        article.link += " " + urlImage;
                        parser.nextText();
                    }
                    else {
                        skipTag(parser);
                    }
                }
                if(!existGuid(res, article.guid))
                    if(res.size() == 0){
                        article.isFirst = true;
                    }
                    else {
                        if (!res.get(res.size() - 1).pubDate.substring(0, 10).equals(article.pubDate.substring(0, 10))) {
                            article.isFirst = true;
                            res.get(res.size() - 1).isLast = true;
                        }
                    }
                    //Log.i("RssUtils", article.pubDate + " isFirst =" + article.isFirst + " isLast = " + article.isLast);
                    res.add(article);
            } else {
                skipTag(parser);
            }
        }

        return res;
    }

    private static boolean existGuid(List<Article> articles, long guid){
        for (int i = 0; i< articles.size(); i++){
            if(articles.get(i).guid == guid){
                return true;
            }
        }
        return false;
    }

    private static void skipTag(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
