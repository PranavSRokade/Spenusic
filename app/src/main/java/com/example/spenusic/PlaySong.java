package com.example.spenusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

public class PlaySong extends AppCompatActivity {
    TextView textView, currentTime, duration;
    ImageView play_pause, next, previous;
    MediaPlayer mediaPlayer;
    SeekBar seekBar;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);

        play_pause = findViewById(R.id.play_pause);
        next = findViewById(R.id.next);
        previous = findViewById(R.id.previous);
        textView = findViewById(R.id.textView);
        currentTime = findViewById(R.id.currentTime);
        duration = findViewById(R.id.duration);
        seekBar = findViewById(R.id.seekBar);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String name = intent.getStringExtra("name");
        position = intent.getIntExtra("position", 0);
        ArrayList<Music> songs = (ArrayList) bundle.getParcelableArrayList("songList");

        Uri uri = Uri.parse(songs.get(position).path);
        mediaPlayer = MediaPlayer.create(this, uri);
        seekBar.setMax(mediaPlayer.getDuration());

        mediaPlayer.start();
        duration.setText(getTime(mediaPlayer.getDuration()));
        textView.setText(name);

        Handler handler = new Handler();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                currentTime.setText(getTime(mediaPlayer.getCurrentPosition()));
                seekBar.setProgress(mediaPlayer.getCurrentPosition());
                handler.postDelayed(this, 1);

                if(!mediaPlayer.isPlaying()){
                    play_pause.setImageResource(R.drawable.play);
                }
            }
        });


        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                    mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        play_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                else{
                    mediaPlayer.start();
                    play_pause.setImageResource(R.drawable.pause);
                }
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position != songs.size() - 1){
                    position++;
                }
                else{
                    position = 0;
                }
                mediaPlayer.stop();
                mediaPlayer.release();

                Uri uri = Uri.parse(songs.get(position).path);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(mediaPlayer.getDuration());

                mediaPlayer.start();
                textView.setText(songs.get(position).name);
                play_pause.setImageResource(R.drawable.pause);
                duration.setText(getTime(mediaPlayer.getDuration()));
            }
        });

        previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(position != 0){
                    position--;
                }
                else{
                    position = songs.size() - 1;
                }
                mediaPlayer.stop();
                mediaPlayer.release();

                Uri uri = Uri.parse(songs.get(position).path);
                mediaPlayer = MediaPlayer.create(getApplicationContext(), uri);
                seekBar.setMax(mediaPlayer.getDuration());

                mediaPlayer.start();
                textView.setText(songs.get(position).name);
                play_pause.setImageResource(R.drawable.pause);
                duration.setText(getTime(mediaPlayer.getDuration()));
            }
        });
    }

    public String getTime(int value){
        int seconds = (value/1000);
        int minute = seconds / 60;

        String time = minute + ":" + String.format("%02d",(seconds % 60));
        return time;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        seekBar.setMax(0);
    }
}