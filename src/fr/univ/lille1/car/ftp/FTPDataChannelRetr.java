package fr.univ.lille1.car.ftp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


	// DataOutPutStream / DataInputStream
	//Retr => Commande de téléchargement d'un fichier. Sens : Serveur -> Client
public class FTPDataChannelRetr extends FTPDataChannel {
	
	private String filepath;
	
	public FTPDataChannelRetr(String hostname, int port,String filepath) {
		super(hostname, port);
		this.filepath = filepath;
	}
	public void run() {
		BufferedReader br = null;
		try {
			br = new BufferedReader(
					   new InputStreamReader(
			                      new FileInputStream(filepath)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String sCurrentLine;
		try {
			while (( sCurrentLine = br.readLine()) != null) {
				writeToSocket(sCurrentLine + "\n");
			}
			br.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
