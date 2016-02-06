package fr.univ.lille1.car.ftp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class FTPDataChannel extends AbstractChannel {

	public FTPDataChannel(String hostname, int port) {
		try {
			this.socketChannel = new Socket(hostname,port);
			reader =  new BufferedReader(new InputStreamReader(this.socketChannel.getInputStream()));
			writer = new DataOutputStream(socketChannel.getOutputStream());
		} catch (UnknownHostException e) {
			System.err.println("Erruer pour ouvrir le channel data");
		} catch (IOException e) {
			System.err.println("Erruer pour ouvrir le channel data");
		}

	}
	public void close() {
		try {
			reader.close();
			socketChannel.close();
			writer.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	
	}
}
