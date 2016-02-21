import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import fr.univ.lille1.car.ftp.*;

public class FTPRequestChannelTest {

	private FtpRequestChannel channel;
	private Socket socket;
	private static Socket socketFromServer;
	private BufferedReader clientReader;
	private static Server server;
	private String replyOfUserCommand;
	private static Socket dataClientSocket;
	@BeforeClass
	public static void setUp() throws UnknownHostException, IOException, InterruptedException {
		new Thread(new Runnable() {
			public void run() {
				try {
					server = new Server();
					while(true){
						socketFromServer = server.waitConnection();
					}
				} catch (IOException e) {
					// The tests is finish stop this thread.
					// Nothing to do
				}
			}
		}).start();
	}
	@AfterClass
	public static void close() throws IOException {
		server.stopListening();
	}
	@Before
	public void setUpEachTest() throws IOException, InterruptedException {
		socket = new Socket("localhost",9009);
		// Wait for creating server connection;;
		Thread.sleep(100);
		clientReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		channel = new FtpRequestChannel(socketFromServer);
	}
	@After
	public void destroyAfterEachTest() throws IOException {
		clientReader.close();
		socket.close();
	}
	private void portCommand() throws IOException {
		channel.processPort("192,168,1,21,32,15");
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("200"));
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("150"));
		assertEquals("192.168.1.21",channel.getAdresseDataChannel());
		assertEquals(8207,channel.getPortDataChannel());
	}
	@Test
	public void processPortTest() throws IOException {
		portCommand();
	}
	private void startDataCommand() throws IOException {
		portCommand();
		// The client become server and wait data on port.
		System.out.println("DATAPORT " + channel.getPortDataChannel());
		final ServerSocket listenerClient = new ServerSocket(channel.getPortDataChannel());
		new Thread(new Runnable() {
			public void run() {
				try {
					dataClientSocket = listenerClient.accept();
				} catch (IOException e) {
					// The tests is finish stop this thread.
					// Nothing to do
				} finally {
					try {
						listenerClient.close();
					} catch (IOException e) {
						// Nothing to do here.
						e.printStackTrace();
					}

				}
			}
		}).start();
	}
	@Test
	public void processStorTest() throws IOException {
	//	startDataCommand();	
	//	channel.processStor();
	}
	@Test
	public void processRetrTest() throws IOException {
		startDataCommand();	
		channel.processRetr("test.txt");
		String dataLine = new BufferedReader(new InputStreamReader(dataClientSocket.getInputStream())).readLine();
		assertEquals("test de kirikou",dataLine);
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("226"));

	}
	@Test
	public void processListTest() throws InterruptedException, IOException {
		startDataCommand();
		channel.processList();
		String files="";
		String dataLine="";
		BufferedReader brData = new BufferedReader(new InputStreamReader(dataClientSocket.getInputStream()));
		while((dataLine = brData.readLine()) != null)
			files += dataLine + ":";
		assertEquals(files,getListFile());
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("226"));
		dataClientSocket.close();

	}
	private String getListFile() {
		String list = "";
		File[] files = new File(channel.getPwd()).listFiles();
		for (File file : files) {
			if (file.isFile()) {
				System.out.println("Send to dataChannel : " + file.getName());
				list += file.getName() + ":";
			}
		}
		return list;
	}
	@Test
	public void processRealUserTest() throws InterruptedException, IOException {
		channel.processUser("THOREZ");		
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("331"));
	}
	@Test
	public void processAnonymousUserTest() throws InterruptedException, IOException {
		channel.processUser("Anonymous");		
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("230"));
	}	
	@Test
	public void processGoodPassTest() throws IOException {
		channel.processPass("THOREZ");
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("230"));
	}
	@Test
	public void processBadPassTest() throws IOException {
		channel.processPass("WRONG_PASSWORD");
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("430"));
	}
}
