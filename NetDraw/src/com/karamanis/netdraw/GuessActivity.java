package com.karamanis.netdraw;

import java.io.IOException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


public class GuessActivity extends Activity{
	private Button btValidate ;
	private LinearLayout drawLayout;
	private ImageView imView; 
	private ImageView imvwinorlose; 
	private String errorMessage=null;
	private NetworkTask nt = null;
	private EditText etPropositon;
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_guess);
        etPropositon = (EditText)findViewById(R.id.etProposition);
        btValidate = (Button)findViewById(R.id.btValidateProposition);
        btValidate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				PropositionTask pt = new PropositionTask();
				pt.execute();
				btValidate.setEnabled(false);
				etPropositon.setEnabled(false);
			}
		});
        btValidate.setEnabled(false);
		etPropositon.setEnabled(false);
        
		drawLayout = (LinearLayout) findViewById(R.id.layoutDrawPanelGuess);
		imView = new ImageView(getApplicationContext());
		imView.setBackgroundColor(Color.WHITE);
		drawLayout.addView(imView);
		imvwinorlose = (ImageView)findViewById(R.id.imvwinorlose);
		imvwinorlose.setVisibility(ImageView.GONE);
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out); 	
    }
	
	@Override
	protected void onResume() {
		super.onResume();
		if(nt == null){
			nt = new NetworkTask();
			nt.execute();
		}
	}
	
	 private class NetworkTask extends AsyncTask<Void,Integer,Void> {
		 private Bitmap bitmap;
		 private  String base64Code;
		 private byte[] decodedString = null;
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             Toast.makeText(getApplicationContext(), "Waiting for draw...", Toast.LENGTH_LONG).show();
         }
  
         @Override
         protected Void doInBackground(Void... arg0) {
        		try {
					base64Code = ConnexionActivity.iss.readLine();
	        		decodedString = Base64.decode(base64Code);
	        		bitmap = BitmapFactory.decodeByteArray(decodedString, 0,decodedString.length);
				} catch (IOException e) {
					errorMessage= e.getMessage();
				}
             return null;
         }
  
         @Override
         protected void onPostExecute(Void result) {
        	 if(errorMessage!=null){
        		 Toast.makeText(getApplicationContext(),"Error "+ errorMessage, Toast.LENGTH_SHORT).show();
        	 }else{
        		 btValidate.setEnabled(true);
        		etPropositon.setEnabled(true);
            	 imView.setImageBitmap(bitmap);
            	 imView.invalidate();
            	 drawLayout.invalidate();
        	 }
         }
     }

	 private class PropositionTask extends AsyncTask<Void,Integer,Void> {
		 String winOrLose="";
		 String proposition="";
		 @Override
		protected void onPreExecute() {
			super.onPreExecute();
			proposition = etPropositon.getText().toString();
			Toast.makeText(getApplicationContext(),"Sending proposition : "+ proposition+"...", Toast.LENGTH_SHORT).show();
			
		}
         @Override
         protected Void doInBackground(Void... arg0) {
        	 	ConnexionActivity.oss.println(proposition);
        		ConnexionActivity.oss.flush();
        	 	try {
					winOrLose= ConnexionActivity.iss.readLine();
				} catch (IOException e) {
					errorMessage = e.getMessage();
				}
             return null;
         }
  
         @Override
         protected void onPostExecute(Void result) {
        	 if(errorMessage!=null){
        		 Toast.makeText(getApplicationContext(),"Error "+ errorMessage, Toast.LENGTH_SHORT).show();
        	 }else{
        		 Toast.makeText(getApplicationContext(),"Win Or Lose  : "+winOrLose, Toast.LENGTH_SHORT).show();
            	 if(winOrLose.compareToIgnoreCase("WIN")==0){
            		 imvwinorlose.setImageResource(R.drawable.youwin);
        			 imvwinorlose.setVisibility(ImageView.VISIBLE);
        			 Handler handler = new Handler();
        			 handler.postDelayed(new Runnable() {
        			     @Override
        			     public void run() {
        			         // Hide your View after 3 seconds
        			    	 imvwinorlose.setVisibility(View.GONE);
        			     }
        			 }, 5000);
            		 NewGameTask ngtask = new NewGameTask();
            		 ngtask.execute(); 
            	 }else{
            		 etPropositon.setEnabled(true);
            		 btValidate.setEnabled(true);
            		 if (winOrLose.compareToIgnoreCase("LOSE1")==0) {
            			 imvwinorlose.setVisibility(ImageView.VISIBLE);
            			 Handler handler = new Handler();
            			 handler.postDelayed(new Runnable() {

            			     @Override
            			     public void run() {
            			         // Hide your View after 3 seconds
            			    	 imvwinorlose.setVisibility(View.GONE);
            			     }
            			 }, 3000);
            		 }
            		 if (winOrLose.compareToIgnoreCase("LOSE2")==0) {
            			 imvwinorlose.setImageResource(R.drawable.oneleft);
            			 imvwinorlose.setVisibility(ImageView.VISIBLE);
            			 Handler handler = new Handler();
            			 handler.postDelayed(new Runnable() {

            			     @Override
            			     public void run() {
            			         // Hide your View after 3 seconds
            			    	 imvwinorlose.setVisibility(View.GONE);
            			     }
            			 }, 3000);
 					}
            		 if (winOrLose.compareToIgnoreCase("LOSE3")==0) {
            			 imvwinorlose.setImageResource(R.drawable.youlouse);
            			 imvwinorlose.setVisibility(ImageView.VISIBLE);
            			 etPropositon.setEnabled(false);
            			 btValidate.setEnabled(false);
            			 Handler handler = new Handler();
            			 handler.postDelayed(new Runnable() {
            			     @Override
            			     public void run() {
            			         // Hide your View after 3 seconds
            			    	 imvwinorlose.setVisibility(View.GONE);
            			     }
            			 }, 5000);
                		 NewGameTask ngtask = new NewGameTask();
                		 ngtask.execute(); 
 					}
            		
            	 }
        	 }
         }
     }
	 private class NewGameTask extends AsyncTask<Void,Integer,Void> {
		 String typeClient =null;
		 String word =null;
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             Toast.makeText(getApplicationContext(), "Starting new game...", Toast.LENGTH_LONG).show();
         }
  
         @Override
         protected Void doInBackground(Void... arg0) {
        	 
        	 try {
	                typeClient = ConnexionActivity.iss.readLine(); 
	                if(typeClient.compareTo("0")==0){
	                	word = ConnexionActivity.iss.readLine();
	                }
				} catch (UnknownHostException uhe){
					errorMessage = uhe.getMessage();
				}catch (IOException ioe) {
					errorMessage = ioe.getMessage();
				}
             return null;
         }
  
         // Méthode exécutée à la fin de l'execution de la tâche asynchrone
         @Override
         protected void onPostExecute(Void result) {
        	 if(errorMessage!=null){
        		 Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        	 }else{
        		 GuessActivity.this.finish();
        		 if(typeClient.compareTo("0")==0){
                 	Intent intent = new Intent(getApplicationContext(), DrawActivity.class);
                 	intent.putExtra("word", word);
              	  	startActivity(intent);	
                  }
                  if(typeClient.compareTo("1")==0){
                 	Intent intent = new Intent(getApplicationContext(), GuessActivity.class);
               	  	startActivity(intent);	
                  }
        	 }          
         }
     }
}
