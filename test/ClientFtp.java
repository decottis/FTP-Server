import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;


public class ClientFtp {

	public static void main(String[] args) throws IOException, InterruptedException {
		int port = 9009;
		InetAddress host = InetAddress.getByName("localhost"); 
		System.out.println("Connecting to server on port " + port); 

		Socket socket = new Socket(host,port); 
		Thread.sleep(100000);
		socket.close();
	}

}
