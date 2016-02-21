package fr.univ.lille1.car.ftp;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class FtpRequestChannel extends AbstractChannel {
	protected String commandProcess;
	protected String pwd;
	protected AbstractChannel dataChannel;
	protected String adresseDataChannel;
	protected int portDataChannel;
	public FtpRequestChannel(Socket socketRequest) throws IOException {
		this.socketChannel = socketRequest;
		reader =  new BufferedReader(new InputStreamReader(this.socketChannel.getInputStream()));
		writer = new DataOutputStream(socketChannel.getOutputStream());
		pwd = System.getProperty("user.home");
	}
	public void processRequest() throws IOException {
		reply(220, "connected to ftp homemade");
		//reply(530, "Need user");
		reply(220, "Launching port command");
		// We listen on the socket ftp for a while...
		System.out.println("PWD ??"+pwd);
		while(true) {
			commandProcess = readLineFromSocket();
			if(commandProcess != null){
				System.out.println(commandProcess);
				switch(commandProcess.split(" ")[0]) {
				case "USER" :
					processUser(commandProcess.split(" ")[1]);
					break;
				case "PASS" :
					processPass(commandProcess.split(" ")[1]);
					break;
				case "RETR" :
					processRetr(commandProcess.split(" ")[1]);
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
				case "SYST" : 
					responseSystemCommand();
					break;
				case "PORT" :
					System.out.println("PORT COMMAND Processing....");
					processPort(commandProcess.split(" ")[1]);
				default :
					break;
					
				}
				
			}
		}
	}
	public void processPort(String arg){
		// We cast port.
		String[] argument = arg.split(",");
		int addr1 = Integer.parseInt(argument[0]);
		int addr2 = Integer.parseInt(argument[1]);
		int addr3 = Integer.parseInt(argument[2]);
		int addr4 = Integer.parseInt(argument[3]);
		int strongBitsPort = Integer.parseInt(argument[argument.length - 2]);
		int weightBitsPort = Integer.parseInt(argument[argument.length - 1]);
		int port = (strongBitsPort << 8) | weightBitsPort;
		System.out.println("DATA CHANNEL : " + addr1 + "." + addr2 + "." + addr3 + "." + addr4 + ":" + port);
		adresseDataChannel = addr1 + "." + addr2 + "." + addr3 + "." + addr4;
		portDataChannel = port;
		reply(200,"Data channel open");
		reply(150,"command port ready");
	}
	public void processUser(String user) throws IOException {
		if(user.equals("THOREZ")) {
			reply(331,"L'utilisateur est existant !");
		} else {
			reply(230,"L'utilisateur est anonymous !");
		}
	}
	public void processPass(String pass) {
		if(pass.equals("THOREZ")) 
			reply(230," Mot de passe correct. Bienvenue");
		else 
			reply(430," Mot de passe incorrect.");		
	}
	public void processStor() {
		
		
	}
	public String getPwd() {
		return pwd;
	}
	public void processRetr(String filepath) {
		new FTPDataChannelRetr(adresseDataChannel, portDataChannel,pwd + "/" + filepath).start();
		reply(226,"Transfert OK.");
	}
	public void processList() {
		new FTPDataChannelList(adresseDataChannel, portDataChannel,pwd).start();
		System.out.println("OKOK");
		reply(226,"LIST OK");
	}
	public void processQuit() {
		
	}
	@Override
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
	private void responseSystemCommand() {
		//writeToSocket("200 " + pwd);
		reply(200,"unix");
	}
	public String getAdresseDataChannel() {
		return adresseDataChannel;
	}
	public int getPortDataChannel() {
		return portDataChannel;
	}
}

