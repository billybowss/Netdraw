package com.karamanis.funnydraw.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import org.apache.commons.codec.binary.Base64;

public class Server {
	private static DataInputStream dataInputStream1;
	private static DataOutputStream dataOutputStream1;
	private static DataInputStream dataInputStream2;
	private static DataOutputStream dataOutputStream2;
	private static Socket sock1 = null;
	private static Socket sock2 = null;
	private static ArrayList<String> listWords ;

	public static void main(String[] args) throws IOException {
	  // create socket
		ServerSocket servsock = new ServerSocket(50100);
		int players = 0;
		boolean twoPlayers =false;
		initializeListWords();
	
	  	while (true) {
	
		   System.out.println("Waiting...");
		   if(players==0){
			   sock1 = servsock.accept();
			   dataInputStream1 = new DataInputStream(sock1.getInputStream());
			   dataOutputStream1 = new DataOutputStream(sock1.getOutputStream());
			   System.out.println("Player "+players+" - Accepted connection : " + sock1);
			   players++;
		   }else {
			   sock2 = servsock.accept();
			   dataInputStream2 = new DataInputStream(sock2.getInputStream());
			   dataOutputStream2 = new DataOutputStream(sock2.getOutputStream());
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
					dataOutputStream1.write(0);
					dataOutputStream1.flush();	
					dataOutputStream2.write(1);
					dataOutputStream2.flush();	
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
	private static void initializeListWords(){
		listWords = new ArrayList<String>();
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			InputStream in_s = Server.class.getClassLoader().getResourceAsStream("assets/dictionnay.xml");
			if(in_s == null){
				System.out.println("LE IN EST NULL");
			}
		    parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
	        parser.setInput(in_s, null);

	        parseXML(parser);

		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	 private static void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
		{
		 	String word="";
	        int eventType = parser.getEventType();
	        while (eventType != XmlPullParser.END_DOCUMENT){
	            String name = null;
	            switch (eventType){
	                case XmlPullParser.START_DOCUMENT:
	                	listWords = new ArrayList<String>();
	                    break;
	                case XmlPullParser.START_TAG:
	                    name = parser.getName();
	                    if (name.equalsIgnoreCase("word")){
	                    	word = parser.nextText();
	                    }
	                    break;
	                case XmlPullParser.END_TAG:
	                    name = parser.getName();
	                    if (name.equalsIgnoreCase("word")){
	                    	listWords.add(word);
	                    } 
	            }
	            eventType = parser.next();
	        }
	        System.out.println("Fichier dictionnary.xml chargé !");
	        System.out.println(listWords.size()+" mots ont été chargé ");      
		}
}

