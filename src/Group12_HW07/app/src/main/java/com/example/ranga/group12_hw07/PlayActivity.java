package com.example.ranga.group12_hw07;

import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.example.ranga.group12_hw07.R.id.seekBar;

public class PlayActivity extends AppCompatActivity {

    private ImageView iv;
    private TextView title,desc,pubDate,duration;
    private ImageButton playButton;
    private SeekBar sb;
    MediaPlayer mp;
    Podcast pod;
    Handler handler;
    Runnable runnable;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp!=null){
            mp.reset();
        }
        handler.removeCallbacks(runnable);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);
        handler=new Handler();



        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar1);
        setSupportActionBar(myToolbar);
        setTitle("Play!");
        getSupportActionBar().setIcon(R.drawable.ted_i);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        iv = (ImageView) findViewById(R.id.playActivityIV);
        title = (TextView) findViewById(R.id.playEpisodeTitleID);
        desc = (TextView) findViewById(R.id.playdescripID);
        duration = (TextView) findViewById(R.id.playdurationID);
        pubDate = (TextView) findViewById(R.id.playPubDateID);
        playButton = (ImageButton) findViewById(R.id.playButtonPlayActID);
        sb = (SeekBar) findViewById(R.id.seekBarID);
        sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mp.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




       if( getIntent().getExtras().containsKey("podName"))
       {
           if(getIntent().getExtras().getSerializable("podName")!=null)
           {
               pod = (Podcast) getIntent().getExtras().getSerializable("podName");
               Picasso.with(iv.getContext()).load(pod.getImage()).into(iv);
               title.setText(pod.getTitle());
               desc.setText(pod.getDes());
               String input = pod.getPdate();
               input = input.substring(0,input.length()-6);
               Log.d("demo",input+"");
               SimpleDateFormat parser = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
               Date date = null;
               try {
                   date = parser.parse(input);
               } catch (ParseException e) {
                   e.printStackTrace();
               }
               SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");
               String formattedDate = formatter.format(date);
               pubDate.setText(formattedDate);

               duration.setText(setDuration(Integer.parseInt(pod.getDuration())));


           }
       }

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mp==null){
                    mp=new MediaPlayer();
                    try {
                        mp.setDataSource(pod.getMp3());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    mp.prepareAsync();
                    mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mediaPlayer) {
                            playButton.setVisibility(View.VISIBLE);
                            sb.setMax(mp.getDuration());
                            sb.setVisibility(View.VISIBLE);
                            mp.start();
                            PlayCycle();
                        }
                    });
                }else{
                    if(mp.isPlaying()){
                        mp.pause();
                        playButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                    }else{
                        mp.start();
                        playButton.setImageResource(R.drawable.ic_pause_black_24dp);
                    }
                }


            }
        });
    }

    public void PlayCycle(){
        sb.setProgress(mp.getCurrentPosition());
        runnable=new Runnable() {
            @Override
            public void run() {
                PlayCycle();
            }
        };
        handler.postDelayed(runnable,1000);
    }


    public String setDuration(int duration)
    {
        duration = duration*1000;
        StringBuilder finalTimerString = new StringBuilder();

// Convert total duration into time
        int hours = (int)( duration / (1000*60*60));
        int minutes = (int)(duration % (1000*60*60)) / (1000*60);
        int seconds = (int) ((duration % (1000*60*60)) % (1000*60) / 1000);
// Add hours if there
        if(hours > 0){
            finalTimerString.append( hours + ":");
        }

// Prepending 0 to seconds if it is one digit
        if(minutes < 10) {
            finalTimerString.append("0" + minutes+":");
        }else {
            finalTimerString.append("" + minutes+":");
        }
        if(seconds < 10) {
            finalTimerString.append("0" + seconds);
        }else {
            finalTimerString.append("" + seconds);
        }

        return finalTimerString.toString();
    }

}
