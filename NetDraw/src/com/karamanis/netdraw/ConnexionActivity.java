package com.karamanis.netdraw;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

public class ConnexionActivity extends Activity  { 
	private NetworkTask nt = null;
	public static Socket socket = null;
	public static BufferedReader iss = null;
	public static PrintWriter oss = null;
	private String typeClient;
	private String word;
	private String ipAddress;
	private ImageButton btConnect;
	private EditText etIp;
	private ProgressBar pb;
	private String errorConnection = null;
	
 
     @Override
     public void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         this.requestWindowFeature(Window.FEATURE_NO_TITLE);
         setContentView(R.layout.connexion);
         etIp = (EditText)findViewById(R.id.etIp);
         etIp.setText("192.168.0.115");
         pb = (ProgressBar)findViewById(R.id.progressBar1);
         pb.setVisibility(View.GONE);
         btConnect = (ImageButton) findViewById(R.id.btConnect);
         
         btConnect.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ipAddress = etIp.getText().toString();
				btConnect.setEnabled(false);
				etIp.setEnabled(false);
				doConnect();
				
				
			}
		});
         overridePendingTransition(R.anim.slide_in, R.anim.slide_out); 
     }
     
     @Override
    protected void onResume() {
    	super.onResume();
    	reinitializeConnection();
    }
     
     @Override
    public void onBackPressed() {
    	super.onBackPressed();
    	reinitializeConnection();
    	overridePendingTransition(R.anim.back_slide_in, R.anim.back_slide_out);
    }
    private void reinitializeConnection(){
    	if(socket != null){
        	try {
				socket.close();
				socket = null;
			} catch (IOException e) {
				e.printStackTrace();
			}
    	}
    	if(nt !=null){
    		nt.cancel(true);
    		nt = null;
    	}
    	iss = null;
    	oss = null;
    	typeClient="";
    	word ="";
    	ipAddress="";
    	errorConnection=null;
    	btConnect.setEnabled(true);
    	etIp.setEnabled(true);
    }
   
     public void doConnect (){
    	if(nt==null){
    		nt = new NetworkTask();
    		nt.execute();
    	}
     }
     private class NetworkTask extends AsyncTask<Void,Integer,Void> {
         // Méthode exécutée au début de l'execution de la tâche asynchrone
         @Override
         protected void onPreExecute() {
             super.onPreExecute();
             pb.setVisibility(View.VISIBLE);
             Toast.makeText(getApplicationContext(), "Connection...", Toast.LENGTH_LONG).show();
         }
  
         @Override
         protected Void doInBackground(Void... arg0) {
        	 
        	 try {
					socket = new Socket(ipAddress, 55555);
					DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());
					DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
		  			iss = new BufferedReader(new InputStreamReader(dataInputStream));
		  			oss = new PrintWriter( new BufferedWriter( new OutputStreamWriter(dataOutputStream)));
	                typeClient = iss.readLine(); 
	                if(typeClient.compareTo("0")==0){
	                	word = iss.readLine();
	                }
				} catch (UnknownHostException uhe){
					errorConnection = uhe.getMessage();
				}catch (IOException ioe) {
					errorConnection = ioe.getMessage();
				}
             return null;
         }
  
         // Méthode exécutée à la fin de l'execution de la tâche asynchrone
         @Override
         protected void onPostExecute(Void result) {
        	 if(errorConnection!=null){
        		 Toast.makeText(getApplicationContext(), errorConnection, Toast.LENGTH_SHORT).show();
        		 reinitializeConnection();
        	 }else{
        		 if(typeClient.compareTo("0")==0){
                 	Intent intent = new Intent(ConnexionActivity.this, DrawActivity.class);
                 	intent.putExtra("word", word);
              	  	startActivity(intent);	
                  }
                  if(typeClient.compareTo("1")==0){
                 	Intent intent = new Intent(ConnexionActivity.this, GuessActivity.class);
               	  	startActivity(intent);	
                  }
        	 }
        	 pb.setVisibility(View.GONE);
             
         }
     }
     
}
