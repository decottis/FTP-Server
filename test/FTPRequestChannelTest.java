import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
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
	@Test
	public void processPortTest() throws IOException {
		channel.processPort("10,28,13,231,86,74");
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("200"));
		replyOfUserCommand = clientReader.readLine();
		assertTrue(replyOfUserCommand.startsWith("150"));
		assertEquals("10.28.13.231",channel.getAdresseDataChannel());
		assertEquals(22090,channel.getPortDataChannel());
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
