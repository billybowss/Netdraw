package com.karamanis.netdraw;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Toast;

public class MainActivity extends Activity  { 
	 Socket socket = null;
	 DataOutputStream dataOutputStream = null;
	 DataInputStream dataInputStream = null;
	 int typeClient;
 
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.activity_main);
         doConnect();
         Log.d("EM", "Create");
     }
     
     @Override
     public boolean onTouchEvent(MotionEvent event) {
         if (event.getAction() == MotionEvent.ACTION_DOWN) {
  			  
  			if(typeClient==0){
  				//Intent intent = new Intent(MainActivity.this, DrawActivity.class);
  				//startActivity(intent);	
  				Toast.makeText(getApplicationContext(), "Vous êtes Dessinateur : "+typeClient, Toast.LENGTH_LONG);
  			}else {
  				Toast.makeText(getApplicationContext(), "Vous êtes Devineur : "+typeClient, Toast.LENGTH_LONG);
			}
  			   
             //Commentaire de test
         }
         return true;
     }
     
     public void doConnect (){
    	 Toast.makeText(this, "TEST SERVER", Toast.LENGTH_LONG);
    	 Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					socket = new Socket("192.168.0.19", 50100);
					dataOutputStream = new DataOutputStream(socket.getOutputStream());
		  			dataInputStream = new DataInputStream(socket.getInputStream());
		  			typeClient = dataInputStream.readInt();
				} catch (UnknownHostException uhe){
					Log.d("EM", "UnknownHostException "+uhe.getMessage());
				}catch (IOException ioe) {
					Log.d("EM", "IOException "+ioe.getMessage());
				}
				
			}
		});
    	 t.start();
    	 
     }
}
