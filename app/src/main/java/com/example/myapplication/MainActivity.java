package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ImageView imageplaypause;
    private TextView textcurrentime;
    private TextView texttotaltime;
    private SeekBar playerseekBar;
    private MediaPlayer mediaplayer;
    private final Handler handler = new Handler();


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textcurrentime = findViewById(R.id.textcurrenttime);
        texttotaltime = findViewById(R.id.texttotalduration);
        imageplaypause = findViewById(R.id.imageplaypause);
        playerseekBar = findViewById(R.id.playerseekbar);
        mediaplayer = new MediaPlayer();
        playerseekBar.setMax(100);
        imageplaypause.setOnClickListener(view -> {
            if(mediaplayer.isPlaying()){
                handler.removeCallbacks(updater);
                mediaplayer.pause();
                imageplaypause.setImageResource(R.drawable.ic_play);
            }
            else{
                mediaplayer.start();
                imageplaypause.setImageResource(R.drawable.ic_pause);
                updateseekbar();
            }

        });
        preparemediaplayer();
        playerseekBar.setOnTouchListener((view, event) -> {
            SeekBar seekBar=(SeekBar) view;
            int playposition=(mediaplayer.getDuration()/100)* seekBar.getProgress();
            mediaplayer.seekTo(playposition);
            textcurrentime.setText(millisecondstotimer(mediaplayer.getCurrentPosition()));
            return false;
        });

    }
    private void preparemediaplayer()
    {
        try {
            mediaplayer.setDataSource("https://www.android-examples.com/wp-content/uploads/2016/04/Thunder-rumble.mp3");
            mediaplayer.prepare();
            texttotaltime.setText(millisecondstotimer(mediaplayer.getDuration()));
        }
        catch (Exception exception){
            Toast.makeText(this,exception.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }



    private final Runnable updater=new Runnable() {
        @Override
        public void run() {
            updateseekbar();
            long currentduration=mediaplayer.getCurrentPosition();
            textcurrentime.setText(millisecondstotimer(currentduration));
        }
    };
    private void updateseekbar()
    {
        if(mediaplayer.isPlaying())
        {
            playerseekBar.setProgress((int )(((float)mediaplayer.getCurrentPosition()/mediaplayer.getDuration())*100));
            handler.postDelayed(updater,1000);
        }
    }
    private String millisecondstotimer(long milliseconds){
        String Timerstring="";
        String secondstring;
        int hours=(int) (milliseconds/(1000*60*60));
        int minutes=(int)(milliseconds%(1000*60*60))/(1000*60);
        int seconds=(int)(milliseconds%(1000*60*60))%(1000*60)/(1000);
        if (hours>0)
        {
            Timerstring=hours+":";

        }
        if(seconds<10)
        {
            secondstring="0"+seconds;
        }
        else
        {
            secondstring=""+seconds;
        }
        Timerstring= Timerstring+minutes+":"+secondstring;
        return Timerstring;
    }
}
