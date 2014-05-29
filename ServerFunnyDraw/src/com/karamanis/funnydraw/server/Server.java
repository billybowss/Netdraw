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

 public static void main(String[] args) throws IOException {
  // create socket
  ServerSocket servsock = new ServerSocket(50200);

  
   

  while (true) {
   System.out.println("Waiting...");

   Socket sock = servsock.accept();
   dataInputStream = new DataInputStream(sock.getInputStream());
   dataOutputStream = new DataOutputStream(sock.getOutputStream());
   System.out.println("Accepted connection : " + sock);

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
   		dataOutputStream.write(0);
	   dataOutputStream.flush();
	   sock.close();
	  }
  
	 }

	 public static String encodeImage(byte[] imageByteArray) {
	  return Base64.encodeBase64String(imageByteArray);
	 }
}

