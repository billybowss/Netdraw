package com.karamanis.netdraw;

import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;

public class MainActivity extends Activity  { 
	private MediaPlayer player;
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.activity_main);
        /* 
         BackgroundSound bs = new BackgroundSound();
         bs.execute();
         */
     }
     
     @Override
     public boolean onTouchEvent(MotionEvent event) {
    	 if (event.getAction() == MotionEvent.ACTION_DOWN) {
    		Intent intent = new Intent(MainActivity.this, ConnexionActivity.class);
    	  	startActivity(intent);	
		}
        return true;
     }
     
     @Override
    protected void onResume() {
    	super.onResume();
    	if(player!=null){
    		player.start();
    	}
    }
     @Override
    protected void onPause() {
    	super.onPause();
    	if(player!=null){
    		player.pause();
    	}
    }
     
     public class BackgroundSound extends AsyncTask<Void, Void, Void> {

    	    @Override
    	    protected Void doInBackground(Void... params) {
    	      /*  MediaPlayer player = MediaPlayer.create(MainActivity.this, R.raw.test_cbr); 
    	        player.setLooping(true); // Set looping 
    	        player.setVolume(100,100); 
    	        player.start(); 
    	        */
    	    	AssetFileDescriptor afd;
    	    	try {
	    	    	// Read the music file from the asset folder
	    	    	afd = getAssets().openFd("music.mp3");
	    	    	// Creation of new media player;
	    	    	player = new MediaPlayer();
	    	    	// Set the player music source.
	    	    	player.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(),afd.getLength());
	    	    	// Set the looping and play the music.
	    	    	player.setLooping(true);
	    	    	player.prepare();
	    	    	player.start();
    	    	} catch (IOException e) {
    	    		e.printStackTrace();
    	    	}

    	        return null;
    	    }

    }
}
