package fr.univ.lille1.car.ftp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public abstract class AbstractChannel extends Thread {

	protected BufferedReader reader;
	protected DataOutputStream writer;
	protected Socket socketChannel;
	
	public String readLineFromSocket() {
		try {
		return reader.readLine();
		} catch(IOException e) {
			System.err.println("Impossible de lire dans la socket");
		}
		return null;
	}
	public void writeToSocket(String buffer){
		try {
			writer.writeBytes(buffer);
		} catch(Exception e) {
			System.err.println("Problem for write to ftp socket...");
		}
	}
	protected void reply(int code, String text){
		System.out.println("REPLY // " + code + " " + text);
		writeToSocket(code + " " + text + "\n");
	}
}
