package fr.univ.lille1.car.ftp;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class Server {

	protected final int PORT = 9009;
	private ServerSocket commandSocket;
	private static Server singleton;
	private Server() throws UnknownHostException, IOException {
		//commandeSocket = new MySocket(InetAddress.getLocalHost(), PORT);
		commandSocket = new ServerSocket(PORT);
	}
	public static Server getInstance() {
		try {
			return ((singleton == null) ?  new Server() : singleton);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Socket waitConnection() throws IOException {
		return commandSocket.accept();
	}
	public void stopListening() throws IOException {
		commandSocket.close();
	}
	public ServerSocket getCommandSocket() {
		return commandSocket;
	}
}
