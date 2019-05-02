package com.example.inigo.aplicaciomusicainigoreal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class SongActivity extends AppCompatActivity {

    private TextView nameSongTV,autorSongTV;
    private ImageButton playButton;
    private ImageView fotoImage;
    private boolean playing = false;
    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);
        final String imageUrl = getIntent().getExtras().getString("imageUrl");
        final String songURL = getIntent().getExtras().getString("songUrl");
        String nameSong = getIntent().getExtras().getString("nameSong");
        String autorSong = getIntent().getExtras().getString("autorSong");

        nameSongTV = findViewById(R.id.nameSong);
        autorSongTV = findViewById(R.id.autorSong);
        playButton = findViewById(R.id.imageButton);
        fotoImage = findViewById(R.id.imageView);

        MiHilo thread = new MiHilo();
        thread.execute(imageUrl);

        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateImage(imageUrl,1);
                playing = true;
                playSong(songURL);
            }
        });

        fotoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (playing) {
                    mediaPlayer.pause();
                    animateImage(imageUrl,2);
                    playing = false;
                }
            }
        });




        nameSongTV.setText(nameSong);
        autorSongTV.setText(autorSong);

    }
    public void animateImage(String url,int mode){
        if(mode==1) {
            fotoImage.animate().scaleXBy((float) 1.1);
            fotoImage.animate().scaleYBy((float) 1.1);
            playButton.animate().alpha(0);
        }else{
            fotoImage.animate().scaleX((float) 1);
            fotoImage.animate().scaleY((float) 1);
            playButton.animate().alpha(1);
        }
    }
    public void playSong(String url){
        mediaPlayer = new MediaPlayer();
        try{
            mediaPlayer.setDataSource(url);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mp.start();
                }
            });
            mediaPlayer.prepare();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public class MiHilo extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... strings) {

            URL url;
            HttpURLConnection connection;
            Bitmap bitmap = null;

            try {
                url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                InputStream in = connection.getInputStream();

                bitmap = BitmapFactory.decodeStream(in);

            } catch (Exception e) {
                e.printStackTrace();
            }

            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);

            fotoImage.setImageBitmap(bitmap);

        }
    }
}
