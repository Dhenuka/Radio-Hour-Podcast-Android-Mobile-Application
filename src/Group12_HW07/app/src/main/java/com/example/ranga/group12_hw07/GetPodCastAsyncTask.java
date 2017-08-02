package com.example.ranga.group12_hw07;

import android.os.AsyncTask;
import android.util.Log;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by ranga on 3/10/2017.
 */

public class GetPodCastAsyncTask extends AsyncTask<String,Void,ArrayList<Podcast>> {
    IActivity activity;
    public GetPodCastAsyncTask(IActivity activity)
    {
        this.activity = activity;
    }




    @Override
    protected ArrayList<Podcast> doInBackground(String... strings) {
        try {
            Log.d("demo",strings[0]+"");
            URL url = new URL(strings[0]);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.connect();
            Log.d("demo","I entered GetPodCastAsyn");
            int statusCode = con.getResponseCode();
            boolean redirect = false;
           // con.setInstanceFollowRedirects(false); /* added line */
            if (statusCode != HttpURLConnection.HTTP_OK) {
                if (statusCode == HttpURLConnection.HTTP_MOVED_TEMP
                        || statusCode == HttpURLConnection.HTTP_MOVED_PERM
                        || statusCode == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }
            System.out.println("Response Code ... " + statusCode);

            if (redirect) {
                // get redirect url from "location" header field
                String newUrl = con.getHeaderField("Location");
                // get the cookie if need, for login
                //  String cookies = conn.getHeaderField("Set-Cookie");
                // open the new connnection again
                con = (HttpURLConnection) new URL(newUrl).openConnection();
                con.setRequestMethod("GET");
                con.connect();
                Log.d("demo","I entered GetPodCastAsyn");
                statusCode = con.getResponseCode();

                System.out.println("Redirect to URL : " + newUrl);
            }
            if(statusCode == HttpURLConnection.HTTP_OK) {
                InputStream in = con.getInputStream();
                return PodcastUtil.PullParser.parsePods(in);
            }
            else
            {
                Log.d("demo","status Not OK");
                Log.d("demo",""+statusCode);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

    @Override
    protected void onPostExecute(ArrayList<Podcast> podcasts) {
      super.onPostExecute(podcasts);
       if(podcasts!=null)
       {
           activity.setList(podcasts);

       }
    }


    public interface IActivity
    {
        public void setList(ArrayList<Podcast> podcasts);
    }


}
