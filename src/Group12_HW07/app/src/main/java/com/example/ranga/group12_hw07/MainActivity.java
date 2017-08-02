package com.example.ranga.group12_hw07;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;

import static java.security.AccessController.getContext;

public class MainActivity extends AppCompatActivity implements GetPodCastAsyncTask.IActivity,MyAdapter.IDATA{

    private ProgressDialog pg;
    private ArrayList<Podcast> myPodsArrayList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;



    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp!=null){
            mp.reset();
        }
        handler.removeCallbacks(runnable);
    }

    private RecyclerView.LayoutManager mLayoutManager;
    private GridLayoutManager gridLayoutManager;
    public static boolean isViewStateLOG = true;
    MediaPlayer mp;
    Handler handler;
    Runnable runnable;
    ImageButton ib;
    SeekBar seekBar;


    @Override
    protected void onResume() {
        super.onResume();
        ib.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);

    }
    @Override
    public void setList(ArrayList<Podcast> podcasts) {
        if(podcasts!=null) {
            Collections.sort(podcasts, new Comparator<Podcast>() {
                @Override
                public int compare(Podcast t1, Podcast t2) {
                   String date1 =  t1.getPdate();
                   String date2 = t2.getPdate();
                    SimpleDateFormat parser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                    Date dateN1 = null;
                    Date dateN2 = null;
                    try {
                        dateN1 = parser.parse(date1);
                        dateN2 = parser.parse(date2);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    return dateN2.compareTo(dateN1);
                }
            });
            myPodsArrayList = podcasts;
            mAdapter = new MyAdapter(this,myPodsArrayList, new MyAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Podcast pod) {
                    if(mp!=null){
                        mp.reset();
                    }
                  Intent intent = new Intent(MainActivity.this,PlayActivity.class);
                  intent.putExtra("podName",pod);
                    startActivity(intent);
                }
            });
            mRecyclerView.setAdapter(mAdapter);
            pg.dismiss();
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //action.setDisplayShowHomeEnabled(false);
     //   action.setDisplayShowTitleEnabled(false);
        setContentView(R.layout.activity_main);
        handler=new Handler();
        seekBar= (SeekBar) findViewById(R.id.seekBar);
        ib= (ImageButton) findViewById(R.id.imageButton);
        ib.setVisibility(View.INVISIBLE);
        seekBar.setVisibility(View.INVISIBLE);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean input) {
                if(input){
                    mp.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        ib.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp.isPlaying()){
                    mp.pause();
                    ib.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                }else{
                    mp.start();
                    ib.setImageResource(R.drawable.ic_pause_black_24dp);
                }
            }
        });

        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        setTitle("TED Radio Hour Podcast");
        getSupportActionBar().setIcon(R.drawable.ted_i);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        myPodsArrayList = new ArrayList<Podcast>();
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mRecyclerView.setLayoutManager(isViewStateLOG ? new LinearLayoutManager(this) : new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
      /*  gridLayoutManager = new GridLayoutManager(this,2);
                mRecyclerView.setLayoutManager(mLayoutManager);
*/
        // specify an adapter (see also next example)

        pg = new ProgressDialog(this);
        pg.setMessage("Loading Episodes...");
        pg.setCancelable(false);
        pg.show();
        new GetPodCastAsyncTask(MainActivity.this).execute("http://www.npr.org/rss/podcast.php?id=510298");

    }


    private void changeLayoutManager() {
        if (mRecyclerView.getLayoutManager().equals(mLayoutManager)) {
            //1
            mRecyclerView.setLayoutManager(gridLayoutManager);
            //2

        } else {
            //3
            mRecyclerView.setLayoutManager(mLayoutManager);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflator = getMenuInflater();
        inflator.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId())
        {
            case R.id.action_switch:
                isViewStateLOG = !isViewStateLOG;
                supportInvalidateOptionsMenu();
                //loading = false;
                mRecyclerView.setLayoutManager(isViewStateLOG ? new LinearLayoutManager(this) : new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                mRecyclerView.setAdapter(mAdapter);
                //    changeLayoutManager();
                 break;

            /*case R.id.action_settings:
                Log.d("demo","Refresh");
                break;*/
        }
        return true;
    }

    @Override
    public void onReturn(int id) throws IOException {

        if(mp==null){
            mp=new MediaPlayer();
        }else{
            mp.reset();
            mp=new MediaPlayer();
        }
        Podcast podcast=myPodsArrayList.get(id);
        mp.setDataSource(podcast.getMp3());
        ib.setImageResource(R.drawable.ic_pause_black_24dp);
        mp.prepareAsync();
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                ib.setVisibility(View.VISIBLE);
                seekBar.setMax(mp.getDuration());
                seekBar.setVisibility(View.VISIBLE);
                mp.start();
                PlayCycle();
            }
        });
    }
    public void PlayCycle(){
        seekBar.setProgress(mp.getCurrentPosition());
        runnable=new Runnable() {
            @Override
            public void run() {
                PlayCycle();
            }
        };
        handler.postDelayed(runnable,1000);
    }
}
