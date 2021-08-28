/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
public class DictionaryServer{
	
	private static int port;																// Declare the port number
	private static ServerSocket serverSocket = null;
	private static Socket clientSocket;
	private static int counter = 0;															// Identifies the user number connected
	private static String filePath; 														// The path of dictionary's JSON file
	private static int threadPoolSize = 6;													// Set the default size of thread pool
	private static int queueSize = 15;
	private String clientInfo = "";
	public ServerGUI window;																// GUI of server
	public GenerateLog log;												
	public static OwnExecutorService service;
	
	/**
	 * Constructor of DictionaryServer class
	 * @throws IOException
	 */
	public DictionaryServer(int port, String filePath) throws IOException {
		 this.port = port;
		 this.filePath = filePath;
		 serverSocket = new ServerSocket(this.port);
		 this.log = new GenerateLog();
		 // Create a thread pool
		 service = MyExecutors.myFixedThreadPool(threadPoolSize, queueSize, log);
	 }
	
	/**
	 * Main function of the project.
	 * Use thread pool to manage the threads.
	 * @param args
	 * @throws IOException
	 */
    //public static void main(String args[]) throws IOException {
	public void runServer(DictionaryServer dic) {
    	//DictionaryServer dic = new DictionaryServer(port);
		try
		{
			// Wait for connections.
			while(true)
			{
				// Find a client and create a connection
				clientSocket = serverSocket.accept();
				counter++;
				clientInfo = clientInfo + "\n" + "Client conection number " + counter + " accepted:"+ clientSocket;
				log.logger.info("This is " + clientSocket);
				// Show information of clients on server GUI
				window.textArea.setText(null);
				window.textArea.append(clientInfo);
				/*
				 * Create a thread pool to manage and monitor all threads in the project,
				 * and start a new thread for each connection.
				 */
				
				try {
					service.submit(new DictionaryThread(clientSocket, counter, filePath, dic, log));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		} 
		catch (IOException e)
		{
			log.logger.info("Sorry, there's I/O error.");
			try {
				clientSocket.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
    }

}
   

	