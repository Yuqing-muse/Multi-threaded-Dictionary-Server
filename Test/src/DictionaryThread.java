/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.file.Files;
import java.nio.file.Paths;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

/**
 * Implements the interface Runnable and override run() method
 * @author zys91
 *
 */
public class DictionaryThread implements Runnable {

    private Socket clientSocket;
    private int clientNum;
    private String filePath;
    private File file;
    private DictionaryServer server;
    private GenerateLog log;									// Declare a GenerateLog object to record logs

    /**
     * Constructor of DictionaryThread class
     * @param clientSocket
     * @param clientNum
     * @param filePath
     * @param server
     */
    public DictionaryThread (Socket clientSocket, int clientNum, String filePath,
    		DictionaryServer server, GenerateLog log) {
        this.clientSocket = clientSocket;
        this.clientNum = clientNum;
        this.filePath = filePath;
        this.server = server;
        this.file = new File(filePath);
        this.log = log;
        
    }
    

    /**
     * Define the basic operations and exception handling available to the client.
     * The case of the input word does not affect the operations.
     * Record key information and error description in log file.
     */
	public void run()
	{
		try {
            
			String input, command, word, meaning = "";
        	// Get the input/output streams for communicating to the client.
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream(), "UTF-8"));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream(), "UTF-8"));
            
            /*
             * Parse keyboard input to obtain command.
             */
            while (true && (input = in.readLine())!= null) {
	            String[] contentS = input.split(" ", 2); 
	            //Keep track of each client request.
	        	System.out.println("Message from client " + clientNum + ": " +
	                    input + "\n");
	        	String data;
            	command = contentS[0];
            	int len = lengthOfInput(input);
	            /*
	             * Perform different instructions according to the command
	             */
                switch (command) {
                    case "Query":
                    	if(len < 2) {
                    		out.write("Please enter correct number of parameters.\n");
                    		out.write("\n");
                    		out.flush();
                    	}
                    	else if(len > 2) {
                    		out.write("Please enter correct format of parameters.");
                    		out.write("\n");
                    		out.flush();
                    	}
                    	else {
	                        JSONObject wordsFile = readFromFile();
	                        word = contentS[1].split(" ", 2)[0];
	                        query(word, wordsFile, out);
                    	}
                        break;
                    case "Add":
                    	if(len < 2) {
                    		out.write("Please enter correct number of parameters.\n");
                    		out.write("\n");
                    		out.flush();
                    	}
                    	else {
	                        JSONObject wordsFile2 = readFromFile();
	                        String[] para = contentS[1].split(" ", 2);
	                        word = para[0];
	                        meaning = para[1];
	                        add(word, meaning, wordsFile2, out);
                    	}
                        break;
                    case "Remove":
                    	if(len < 2) {
                    		out.write("Please enter correct number of parameters.\n");
                    		out.write("\n");
                    		out.flush();
                    	}
                    	else if(len > 2) {
                    		out.write("Please enter correct format of parameters.");
                    		out.write("\n");
                    		out.flush();
                    	}
                    	else {
	                        JSONObject wordsFile3 = readFromFile();
	                        word = contentS[1].split(" ", 2)[0];
	                        remove(word, wordsFile3, out);
                    	}
                        break;
                    case "Update":
                    	if(len < 2) {
                    		out.write("Please enter correct number of parameters.\n");
                    		out.write("\n");
                    		out.flush();
                    	}
                    	else {
	                    	JSONObject wordsFile4 = readFromFile();
	                    	String[] para = contentS[1].split(" ", 2);
	                        word = para[0];
	                        meaning = para[1];
	                        /*
	                         * The meaning of the words entered by the client needs to be separated by ;
	                         * For instance: fruit; can eat; red
	                         */
	                        update(word, meaning, wordsFile4, out);
                    	}
                        break;
                    case "Exit":
                    	/*
                    	 * Close the socket when done.
                    	 * Record key information and error description in log file
                    	 */
                        if (clientSocket != null) {
                            try {
                            	server.service.closeWorker(this);
                                clientSocket.close();
                                log.logger.info("********************" + "\n" + "*********************");
                                log.logger.info("The Client connection number " + clientNum +
                                        " exits. "  + "\n");
                            }
                            catch (IOException e) {
                            	log.logger.info("Encounter error when close Client Socket." + "\n");
                            }
                        }
                    	break;
                }
                log.logger.info("Response sent" + "\n");
           }
        }
        catch(SocketException e) {
        	log.logger.info("Socket closed." + "\n");
        }
        catch(FileNotFoundException e) {
        	log.logger.info("Can not find file.");
        }
        catch(JSONException e) {
        	log.logger.info("Encounter error when parse the file." + "\n");
        }
        catch(IOException e) {
        	log.logger.info("Encounter error when send or receive data." + "\n");
        }
		catch(NoSuchElementException e) {
			log.logger.info("Lack of parameters." + "\n");
		}
	}
	
	/**
	 * This method defines the process of query in dictionary.
	 * Only if the word exists in the dictionary, we can query its meanings.
	 * @param word
	 * @param wordsFile
	 * @param output
	 * @throws IOException
	 * @throws JSONException
	 */
    private void query(String word, JSONObject wordsFile, BufferedWriter output) throws IOException, JSONException {
        // Display the meaning(s) of that word when it is in the dictionary
    	String value = word.toLowerCase();
        if (wordsFile.has(value)) {
        	org.json.JSONArray wordArray = wordsFile.getJSONArray(value);
            String meaning = meanings(wordArray);
            output.write(meaning);
            output.write('\n');
            output.flush();
        }
        // If the word is not found, display error message to client.
        else {
            output.write("Sorry, we can't find this word.");
            output.write('\n');
            output.flush();
        }
    }
    
    /**
     * This method is synchronized and adding a new word and its meanings into dictionary,
     * and store the updated JSONObject into JSON file.
     * The meaning of the words entered by the client needs to be separated by ;
     * @param word
     * @param means
     * @param wordsFile
     * @param output
     * @throws IOException
     * @throws JSONException
     */
    private synchronized void add(String word, String means, JSONObject wordsFile, BufferedWriter output) throws IOException, JSONException {
    	String value = word.toLowerCase();
    	try {
    		// Client isn't provide meanings lead to a custom exception
	    	if (means == null) {
	    		throw new LackParameterException();
	    	}
	    	else {
		    	// We can add the word and its meaning if the word hasn't already in the dictionary.
		    	if (wordsFile.has(value)) {
		    		output.write("Sorry, this word is already existed in the dictionary");
		    		output.write('\n');
			        output.flush();
		    	}
		    	else {
		    		JSONArray addedMeaning = new JSONArray();
		    		StringTokenizer meanings = new StringTokenizer(means, ";");
		    		while(meanings.hasMoreTokens()) {
						addedMeaning.put(meanings.nextToken());
                     }
			        // Add the word into dictionary with its meaning(s).
			        wordsFile.put(value, addedMeaning);
			        output.write("Successfully add");
			        output.write('\n');
			        output.flush();
			        // Updae the dictionary file
			        store(wordsFile);
		    	}
	    	}
    	}catch (LackParameterException e){
    		output.write("Please add a word with an associated meaning." + "\n");
    		output.flush();
    	}
    }
    
    /**
     * Remove the word and its meanings only when it exists in the dictionary;
     * and store the updated JSONObject into JSON file.
     * @param word
     * @param wordsFile
     * @param output
     * @throws IOException
     */
    private synchronized void remove(String word, JSONObject wordsFile, BufferedWriter output) throws IOException {
    	String value = word.toLowerCase();
    	// Check if dictionary has the word
    	if (wordsFile.has(value)) {
            wordsFile.remove(value);
            output.write("Success.");
            output.write('\n');
            output.flush();
            // Store in JSON file
            store(wordsFile);
        }
    	else {
    		output.write("Sorry, we can't find this word.");
    		output.write('\n');
            output.flush();
    	}
    }
    
    /**
     * Update associated meanings of a word from the dictionary;
     * and store the updated JSONObject into JSON file.
     * If multiple meanings exist, all of them should be replaced by new meaning(s) provided by user.
     * The meaning of the words entered by the client needs to be separated by ;
     * @param word
     * @param means
     * @param wordsFile
     * @param output
     * @throws IOException
     * @throws JSONException
     */
    private synchronized void update(String word, String means, JSONObject wordsFile, BufferedWriter output) throws IOException, JSONException {
    	String value = word.toLowerCase();
    	if (wordsFile.has(value)) {
    		/*
    		 * Check if the meanings provided by client are multiple
    		 */
    		if(means.contains(";")) {
    			String[] st = means.split(";");
    	    	JSONArray updatedMeaning = new JSONArray();
    	    	for(int i=0; i < st.length; i++) {
    	    		System.out.println(st[i]);
    	    		updatedMeaning.put(st[i]);
    	    	}
		    	wordsFile.put(value, updatedMeaning);
    		}
    		else {
    			// The number of meanings provided by client is one
    			JSONArray updatedMeaning = new JSONArray();
    			updatedMeaning.put(means);
    			wordsFile.put(value, updatedMeaning);
    		}
    		
            output.write("Success.");
            output.write('\n');
            output.flush();
            // Store in JSON file
            store(wordsFile);
        }
    	else {
    		output.write("Sorry, we can't find this word.");
    		output.write('\n');
            output.flush();
    	}
    }
    
    /**
     * Store the updated JSONObject into JSON file.
     * @param wordsFile
     */
    private void store(JSONObject wordsFile) {
    	FileWriter files = null;
    	try {
            // Create a FileWriter to write into JSON file
            files = new FileWriter(filePath);	
            files.write(wordsFile.toString());
            log.logger.info("Successfully store data in dictionary file.");
            log.logger.info("yyy");
            files.flush();
            files.close();
        } catch (IOException e) {
        	log.logger.info("Encounter error when store file.");
            e.printStackTrace();
        }
    }
    
    /**
     * Obtain all meanings of a word,
     * each meaning use ! to separate.
     * @param wordsArray
     * @return
     * @throws JSONException
     */
    private String meanings (JSONArray wordsArray) throws JSONException {
    	String means = "";
		for (int i = 0; i < wordsArray.length(); i++) {
			   means = means + wordsArray.getString(i) + "!";
		}
		return means;
    }
    
    /**
     * Load the dictionary data format (JSON).
     * @throws ParseException 
     * @throws IOException 
     */
    private JSONObject readFromFile() throws JSONException, IOException{
    	JSONObject wordsFile = parseJSONFile(filePath);
        return wordsFile;
    }
    
    /**
     * Parse JSON file.
     * @param filename
     * @return
     * @throws JSONException
     * @throws IOException
     */
    public static JSONObject parseJSONFile(String filename) throws JSONException, IOException {
        String content = new String(Files.readAllBytes(Paths.get(filename)));
        return new JSONObject(content);
    }
    
    /**
     * Obtain number of parameters to check if clients enter correct number of parameters.
     * @param input
     * @return length of instructions
     */
    public int lengthOfInput(String input) {
    	String[] para = input.split(" ");
    	return para.length;
    }
}
