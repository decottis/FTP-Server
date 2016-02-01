package fr.univ.lille1.car.ftp;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MySocket extends Socket {
	
	
	public MySocket(InetAddress adress, int port) throws IOException {
		super(adress,port);
	}

	public String readLineFromSocket() throws IOException {
		InputStream inputStream = this.getInputStream();
		InputStreamReader rs = new InputStreamReader(inputStream);
		BufferedReader buffer = new BufferedReader(rs);	
		String s = buffer.readLine();
		buffer.close();
		rs.close();
		inputStream.close();
		return s;
	}
	public void writeToSocket(String buffer) throws IOException {
		OutputStream os = this.getOutputStream();
		DataOutputStream dos = new DataOutputStream(os);
		dos.writeBytes(buffer);
		dos.close();
		os.close();
	}
}
