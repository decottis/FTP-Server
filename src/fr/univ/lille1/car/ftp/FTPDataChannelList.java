package fr.univ.lille1.car.ftp;

import java.io.File;
import java.io.IOException;

public class FTPDataChannelList extends FTPDataChannel {

	protected String pwd;
	
	public FTPDataChannelList(String hostname, int port,String pwd) {
		super(hostname, port);
		this.pwd = pwd;
	}
	public void run() {
		File[] files = new File(pwd).listFiles();
		for (File file : files) {
		    if (file.isFile()) {
		    	System.out.println("Send to dataChannel : " + file.getName());
		        writeToSocket(file.getName() + "\r\n");
		    }
		}
		System.out.println("FIN DU RUN LIST");
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
