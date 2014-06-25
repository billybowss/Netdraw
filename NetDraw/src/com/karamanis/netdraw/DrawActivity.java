package com.karamanis.netdraw;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.karamanis.netdraw.dialog.ColorPickerDialog;


public class DrawActivity extends Activity implements ColorPickerDialog.OnColorChangedListener { 

	//DrawablePanel paintPanel;
	private DrawingView dw;
	private Paint mPaint;
	private ImageButton ibt_Color;
	private ImageButton ibt_Send;
	private ImageButton ibt_Erase;
	private TextView tvWord;
	private String word;
	private String error = null;

	@Override
	public void onCreate(Bundle savedInstanceState) 
	{	
		super.onCreate(savedInstanceState);	
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_draw);	
		word = getIntent().getStringExtra("word");
		mPaint = new Paint(); 			 
		tvWord = (TextView)findViewById(R.id.tvWordDraw);
		tvWord.setText(word);
		ibt_Color = (ImageButton) findViewById(R.id.ibt_Color);
		ibt_Color.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				ColorPickerDialog c = new ColorPickerDialog(DrawActivity.this, DrawActivity.this,mPaint.getColor());
            	c.show();				
			}
		});
		ibt_Send = (ImageButton) findViewById(R.id.ibt_Send);
		ibt_Send.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				sendImage();
			}
		});
		ibt_Erase = (ImageButton)findViewById(R.id.ibtErase);
		ibt_Erase.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dw.setPaintColor(Color.WHITE);
			}
		});
		dw = new DrawingView(this);
		dw.setBackgroundColor(Color.WHITE);
		LinearLayout drawLayout = (LinearLayout) findViewById(R.id.layoutDrawPanel);
		drawLayout.addView(dw,LayoutParams.FILL_PARENT);
		Toast.makeText(getApplicationContext(), "Drawing word : "+word, Toast.LENGTH_LONG).show();
		
		overridePendingTransition(R.anim.slide_in, R.anim.slide_out); 
		   
	}
	private void sendImage(){
		NetworkTask nt = new NetworkTask();
		nt.execute();
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out); 
	}
		
	public void OnResume()
	{
		super.onResume();
        setContentView(R.layout.activity_draw);		       
	}

	@Override
	public void colorChanged(int color) {
		dw.setPaintColor(color);
	}
	public void listenproposition(){
		PropositionTask pt = new PropositionTask();
		pt.execute();	
	}
	private class NetworkTask extends AsyncTask<Void,Integer,Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Toast.makeText(getApplicationContext(), "Sending draw...", Toast.LENGTH_SHORT).show();
        }
 
        @Override
        protected Void doInBackground(Void... arg0) {
        	Bitmap bitmap = dw.getCanvasBitmap();
        	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();  
        	bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
    		byte[] byteArray = byteArrayOutputStream.toByteArray();
    		String encoded = null;
    		try {
    			encoded = Base64.encodeBytes(byteArray);
            	ConnexionActivity.oss.println(encoded);
            	ConnexionActivity.oss.flush();
    		} catch (Exception e) {
    			error = e.getMessage();
    		}
            return null;
        }
 
        @Override
        protected void onPostExecute(Void result) {
        	if(error!=null){
        		Log.d("ERROR", error);
        		Toast.makeText(getApplicationContext(),"Error "+ error, Toast.LENGTH_LONG).show();
        	}
        	else {
        		Toast.makeText(getApplicationContext(), "Draw send !", Toast.LENGTH_SHORT).show();
        		listenproposition();
        	}
        }
    }

	private class PropositionTask extends AsyncTask<Void,Integer,Void> {
		 String winOrLose="";
		 String errorMessage=null;
         @Override
         protected Void doInBackground(Void... arg0) {
        	 	try {
        	 		winOrLose = ConnexionActivity.iss.readLine();
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
            	 if(winOrLose.compareToIgnoreCase("WIN")==0){
            		 Toast.makeText(getApplicationContext(),"Player2 found the word !!", Toast.LENGTH_LONG).show();
            		 Handler handler = new Handler();
            		 handler.postDelayed(new Runnable() {
        			     @Override
        			     public void run() {
        			     }
        			 }, 5000);
            		 NewGameTask ngtask = new NewGameTask();
            		 ngtask.execute(); 
            	 }else{
            		 if (winOrLose.compareToIgnoreCase("LOSE1")==0) {
            			 Toast.makeText(getApplicationContext(),"Player2 First Try unsuccessful!", Toast.LENGTH_LONG).show();
            			 listenproposition();
            		 }
            		 if (winOrLose.compareToIgnoreCase("LOSE2")==0) {
            			 Toast.makeText(getApplicationContext(),"Player2 Second Try unsuccessful!", Toast.LENGTH_LONG).show();
            			 listenproposition();
 					}
            		 if (winOrLose.compareToIgnoreCase("LOSE3")==0) {
            			 Toast.makeText(getApplicationContext(),"Player2 Loses !!", Toast.LENGTH_LONG).show();
            			 Handler handler = new Handler();
            			 handler.postDelayed(new Runnable() {
            			     @Override
            			     public void run() {
 
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
		 String errorMessage=null;
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
  
         @Override
         protected void onPostExecute(Void result) {
        	 if(errorMessage!=null){
        		 Toast.makeText(getApplicationContext(), errorMessage, Toast.LENGTH_SHORT).show();
        	 }else{
        		 DrawActivity.this.finish();
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

