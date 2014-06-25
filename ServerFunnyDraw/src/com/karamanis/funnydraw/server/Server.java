package com.karamanis.funnydraw.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Server {
	private static Socket sock1 = null;
	private static Socket sock2 = null;
	private static ArrayList<String> listWords ;

	public static void main(String[] args) throws IOException {
	  // create socket
		ServerSocket servsock = new ServerSocket(55555);
		int players = 0;
		initializeListWords();
	
	  	while (true) {
		   System.out.println("Waiting...");
		   if(players==0){
			   sock1 = servsock.accept();
			  
			   System.out.println("Player "+players+" - Accepted connection : " + sock1);
			   players++;
		   }else {
			   sock2 = servsock.accept();
			   
			   
			   System.out.println("Player "+players+" - Accepted connection : " + sock2);
			   generateGame(sock1,sock2);
			   players = 0;
		   }
		}
	 }
	
	public static void generateGame(Socket p1, Socket p2) throws IOException{
		System.out.println("Game Launched for two players");
		final Socket player1 = p1;
		final Socket player2 = p2;
		DataInputStream dataInputStream1 = new DataInputStream(player1.getInputStream());
		DataOutputStream dataOutputStream1 = new DataOutputStream(player1.getOutputStream());
		DataInputStream dataInputStream2 = new DataInputStream(player2.getInputStream());
		DataOutputStream dataOutputStream2 = new DataOutputStream(player2.getOutputStream());
		
		final PrintWriter oss1 = new PrintWriter( new BufferedWriter( new OutputStreamWriter(dataOutputStream1)));
		final BufferedReader iss1 = new BufferedReader(new InputStreamReader(dataInputStream1));
		
		final PrintWriter oss2 = new PrintWriter( new BufferedWriter( new OutputStreamWriter(dataOutputStream2)));
		final BufferedReader iss2 = new BufferedReader(new InputStreamReader(dataInputStream2));
		
		Thread t = new Thread(new Runnable() {
			
			@Override
			public void run() {			
				oss1.println("0");
				oss1.flush();
				System.out.println("-- Role Attribution P1 : CHECK !");
				oss2.println("1");
				oss2.flush();
				System.out.println("-- Role Attribution P2 : CHECK !");
				System.out.println("======================================");
				Random rand = new Random();
				int intRand = rand.nextInt(listWords.size());
				String word = listWords.get(intRand);
				System.out.println("-- Generate Word : CHECK !");
				oss1.println(word);
				oss1.flush();
				System.out.println("-- Send Word : CHECK !");
				
				String image="";
				
				System.out.println("======================================");
				
				System.out.println("Waiting for Draw from Player 1...");
				try {
					image = iss1.readLine();
				} catch (IOException e) {
					e.printStackTrace();
				}
				System.out.println("-- Reception of the draw : CHECK !");
				
				oss2.println(image);
				oss2.flush();
				System.out.println("-- Send Draw : CHECK !");
				System.out.println("======================================");
				int cptProposition =1;
				Boolean win = false;
				while(cptProposition <4 && win==false){
					System.out.println("Waiting for a proposition...");
					String wordProposition="";
					try {
						wordProposition = iss2.readLine();
						System.out.println("-- Player2 made a proposition ! #"+cptProposition+"       : "+wordProposition);
					} catch (IOException e) {
						e.printStackTrace();
					}
					if(word.compareToIgnoreCase(wordProposition)==0){
						System.out.println("------ Word Matches !!");
						oss1.println("WIN");
						oss1.flush();
						oss2.println("WIN");
						oss2.flush();
						win = true;
					}else{
						oss1.println("LOSE"+cptProposition);
						oss1.flush();
						oss2.println("LOSE"+cptProposition);
						oss2.flush();
						System.out.println("------ Word doesn't match !!");
						System.out.println("======================================");
						cptProposition++;
					}
				}
				if(win){
					try {
						generateGame(player2, player1);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}else {
					try {
						generateGame(player1, player2);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		});
		t.start();
	}

	private static void initializeListWords(){
		listWords = new ArrayList<String>();
		listWords.add("maison");
		listWords.add("Chat");
		listWords.add("chaise");
		listWords.add("telephone");
		listWords.add("ordinateur");
		listWords.add("chaussette");
		listWords.add("ballon");
		listWords.add("voiture");
		
		/*
		XmlPullParserFactory pullParserFactory;
		try {
			pullParserFactory = XmlPullParserFactory.newInstance();
			XmlPullParser parser = pullParserFactory.newPullParser();

			InputStream in_s = Server.class.getClassLoader().getResourceAsStream("dictionnay.xml");
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
		}*/
	}
	 /*private static void parseXML(XmlPullParser parser) throws XmlPullParserException,IOException
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
		}*/
}

