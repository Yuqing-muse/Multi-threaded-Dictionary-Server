package DictionaryClient;
/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * The connection to the server is always maintained until the client logs out. 
 * Also monitor the status of messages sent and replies received
 * @author yuqchang
 *
 */
public class DicClient {

	// IP and port
	private static String ip;
	private static int port;
	private static Socket socket;

	/**
	 * Constructor of DicClient class
	 */
	public DicClient(int port, String ip) {
		try {
			this.port = port;
			// Connect to the server.
			this.ip = ip;
			socket = new Socket(this.ip, this.port);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			System.out.println("Sorry, there's a unknown host error.");
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("Sorry, there's I/O error in reading your input.");
			e.printStackTrace();
		}
	}

	/*
	 * Send data to server and obtain the data from server.
	 * Return the received data from server.
	 */
	public String send(String sendData) {
		String received = "";
		String data;
		try
		{ 
			// Get the input/output streams for reading/writing data from/to the socket
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
			BufferedWriter output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
	    	// Send the data to the server by writing to the socket output stream
	    	output.write(sendData);
	    	output.write('\n');
	    	System.out.println("Data sent to Server--> " + sendData);
	    	output.flush();
			
	    	// Read all lines of data from server
			while(true && ((data = input.readLine())!=null)) {
				received = data;
				return received;
			}
		}
		catch (UnknownHostException e)
		{
			System.out.println("Sorry, there's a unknown host error.");
			e.printStackTrace();
		}
		catch (IOException e) 
		{
			System.out.println("Sorry, there's I/O error in reading your input.");
			e.printStackTrace();
		}
		return received;
	}
}
