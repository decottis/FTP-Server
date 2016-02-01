package fr.univ.lille1.car.ftp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class FtpRequest extends Thread {
	protected Socket socketRequest;
	protected String commandProcess;
	protected String currentUser;
	protected BufferedReader reader;
	protected DataOutputStream writer;
	
	public FtpRequest(Socket socketRequest) throws IOException {
		this.socketRequest = socketRequest;
		reader =  new BufferedReader(new InputStreamReader(this.socketRequest.getInputStream()));
		writer = new DataOutputStream(socketRequest.getOutputStream());
	}
	public void processRequest() throws IOException {
		reply(220, "connected to ftp homemade");
		//reply(530, "Need user");
		// We listen on the socket ftp for a while...
		while(true) {
			commandProcess = readLineFromSocket();
			if(commandProcess != null){
				System.out.println(commandProcess);
				System.out.println(commandProcess.split(" ")[0]);
				switch(commandProcess.split(" ")[0]) {
				case "USER" :
					processUser();
					break;
				case "PASS" :
					processPass();
					break;
				case "RETR" :
					processRetr();
					break;
				case "STOR" :
					processStor();
					break;
				case "LIST" :
					processList();
					break;
				case "QUIT" :
					processQuit();
					break;
				default :
					break;
					
				}
			}
		}
	}
	public void processUser() throws IOException {
		if(commandProcess.split(" ")[1].equals("THOREZ")) {
			currentUser = commandProcess.split(" ")[1];
			reply(331,"L'utilisateur selectionné est existant !");
		} else {
			reply(230,"L'utilisateur selectionné est anonymous!");
		}
	}
	public void processPass() {
		
	}
	public void processStor() {
		
	}
	public void processRetr() {
		
	}
	public void processList() {
		
	}
	public void processQuit() {
		
	}
	public void run() {
		try {
			processRequest();
		} catch (IOException e) {
			reply(500," error for parsing your request");
		} finally {
			try {
				writer.close();
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private String readLineFromSocket() throws IOException {
		return reader.readLine();
	}
	private void writeToSocket(String buffer){
		try {
			writer.writeBytes(buffer);
			writer.flush();
		} catch(Exception e) {
			System.err.println("Problem for write to ftp socket...");
		}
	}
	private void reply(int code, String text){
		writeToSocket(code + " " + text + " \r\n");
	}
}
