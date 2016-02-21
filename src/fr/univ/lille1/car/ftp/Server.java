package fr.univ.lille1.car.ftp;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

	protected final int PORT = 9009;
	public ServerSocket commandSocket;
	public Server() throws UnknownHostException, IOException {
		//commandeSocket = new MySocket(InetAddress.getLocalHost(), PORT);
		commandSocket = new ServerSocket(PORT);
	}
	public Socket waitConnection() throws IOException {
		return commandSocket.accept();
	}
	public void stopListening() throws IOException {
		commandSocket.close();
	}
		/**
	 * @param args
		 * @throws IOException 
		 * @throws UnknownHostException 
	 */
	public static void main(String[] args) {
		try {
			Server server = new Server();
			Socket currentSocket;
			FtpRequestChannel request;
			// Listening on the port 9009 => Waiting some clients...
			while(true) {
				currentSocket = server.waitConnection();
				request = new FtpRequestChannel(currentSocket);
				request.start();
			}
		} catch(Exception e) {
			System.out.println("An error occurs.");
			e.printStackTrace();
			
		}
	}

}
