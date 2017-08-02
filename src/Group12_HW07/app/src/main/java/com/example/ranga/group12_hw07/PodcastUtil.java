package com.example.ranga.group12_hw07;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by ranga on 3/10/2017.
 */
public class PodcastUtil {
    static public class PullParser {
        static ArrayList<Podcast> parsePods(InputStream in) throws Exception {
            Log.d("demo","entered parse");
            String checkInput = "";
            XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
            parser.setInput(in, "UTF-8");
            Podcast pod = null;
            ArrayList<Podcast> PodcastArrayList = new ArrayList<>();
            int event = parser.getEventType();
            Log.d("demo","I entered parsePods");

            while (event != XmlPullParser.END_DOCUMENT) {
                switch (event) {
                    case XmlPullParser.START_TAG:
                        if (parser.getName().equals("item")) {
                            Log.d("demo", "strat");
                            checkInput = "Item";
                            Log.d("demo",checkInput+"checkInput");
                            pod = new Podcast();
                        }
                        if (!(checkInput.equals(""))) {
                            if (parser.getName().equals("title")) {
                                String title =parser.nextText();
                                Log.d("demo",title+"title");
                                pod.setTitle(title);
                            } else if (parser.getName().equals("description")) {
                                Log.d("demo","description");
                                String descr =parser.nextText();
                                Log.d("demo",descr+"descr");
                                pod.setDes(descr);
                            } else if (parser.getName().equals("pubDate")) {
                                Log.d("demo","description");
                                String pubDate =parser.nextText();
                                Log.d("demo",pubDate+"pubDate");
                                pod.setPdate(pubDate);
                            } else if (parser.getName().equals("itunes:image")) {
                                String href =parser.getAttributeValue(null, "href");
                                Log.d("demo",href+"href");
                                pod.setImage(href);
                            } else if (parser.getName().equals("itunes:duration")) {
                                String duration =parser.nextText();
                                Log.d("demo",duration+"duration");
                                pod.setDuration(duration);
                            } else if (parser.getName().equals("enclosure")) {
                                String enclosure =parser.getAttributeValue(null, "url");
                                Log.d("demo",enclosure+"enclosure");
                                pod.setMp3(enclosure);
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (parser.getName().equals("item")) {
                            Log.d("demo", "end");
                            PodcastArrayList.add(pod);
                            pod = null;
                        }
                        break;
                }
                event = parser.next();
            }
            return PodcastArrayList;
        }
    }
}
