package com.karamanis.funnydraw.server;

	import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.commons.codec.binary.Base64;

public class Server {
	private static DataInputStream dataInputStream;

	private static DataOutputStream dataOutputStream;
	
	private static Socket sock1 = null;
	private static Socket sock2 = null;

	public static void main(String[] args) throws IOException {
	  // create socket
		ServerSocket servsock = new ServerSocket(50100);
		int players = 0;
		boolean twoPlayers =false;
	  
	   
	
	  	while (true) {
	
		   System.out.println("Waiting...");
		   if(players==0){
			   sock1 = servsock.accept();
			   dataInputStream = new DataInputStream(sock1.getInputStream());
			   dataOutputStream = new DataOutputStream(sock1.getOutputStream());
			   System.out.println("Player "+players+" - Accepted connection : " + sock1);
			   players++;
		   }else {
			   sock2 = servsock.accept();
			   dataInputStream = new DataInputStream(sock2.getInputStream());
			   dataOutputStream = new DataOutputStream(sock2.getOutputStream());
			   System.out.println("Player "+players+" - Accepted connection : " + sock2);
			   twoPlayers = true;
			   generateGame();
			   players = 0;
		   }
		   
		   
		  
	   /*
	   File myFile = new File("F:\\ic_launcher.png");
	   System.out.println("Sending...");
	   FileInputStream imageInFile = new FileInputStream(myFile);
	   byte imageData[] = new byte[(int) myFile.length()];
	   imageInFile.read(imageData);
	
	   /*
	    * Converting Image byte array into Base64 String
	    *
		   String imageDataString = encodeImage(imageData);
		   System.out.println(imageDataString.length());
	
		   System.out.println(imageDataString);
		   dataOutputStream.writeUTF(imageDataString);
	
	*/
	   		//dataOutputStream.write(0);
		   //dataOutputStream.flush();
		   //sock.close();
		  }
  
	 }
	public static void generateGame(){
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {
				System.out.println("Game Launched for two players");
				try {
					dataOutputStream.write(0);
					dataOutputStream.flush();	
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		});
		t.start();
	}
	 public static String encodeImage(byte[] imageByteArray) {
	  return Base64.encodeBase64String(imageByteArray);
	 }
}

