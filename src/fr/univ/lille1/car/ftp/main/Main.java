package fr.univ.lille1.car.ftp.main;

import java.net.Socket;

import fr.univ.lille1.car.ftp.FtpRequestChannel;
import fr.univ.lille1.car.ftp.Server;

public class Main {

	public static void main(String[] args) {
		try {
			Server server = Server.getInstance();
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
