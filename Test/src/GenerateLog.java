import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

/**
 * A class to create log file and record errors or exceptions in the log file.
 * @author yuqchang
 *  *
 */
public class GenerateLog {
   public Logger logger;
   public GenerateLog () {
      // Simple file logging Handler and get logger file
      		FileHandler myFileHandler;
      		logger = Logger.getLogger("dictionaryLog");
      		
      		try {
       
      			// Append data to file
      			myFileHandler = new FileHandler("./dictionary-log.log", true);
      			logger.addHandler(myFileHandler);
       
      			// Print a brief summary of the Dictionary Server in a human readable format.
      			String summary = "This is a multi-threaded dictionary server that allows concurrent \r\n" + 
      					"clients to search the meaning(s) of a word, add a new word, and remove an existing word.\n"+
      					"It implements TCP sockets, threads in thread-per-connection mode.\n"+
      					"Author: Yuqing Chang\n" + "Student number: 1044862\n";
      			logger.info(summary);
      			
      		} catch (SecurityException e) {
      			e.printStackTrace();
      		} catch (IOException e) {
      			e.printStackTrace();
      		}
      	}
   
   }
