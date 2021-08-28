/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.util.HashSet;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

interface OwnExecutorService{
	void submit(Runnable r) throws InterruptedException;
	void closeWorker(DictionaryThread dictionaryThread);
}

/**
 * Implement my own thread pool to control threads.
 * It reduces the consumption of system resources, facilitates the control of the number of concurrent threads,
 *  and improves system responsiveness.
 * @author yuqchang
 */
public class OwnThreadPool implements OwnExecutorService {
	static int size;											// Declare the size of thread pool.
	static int inUse;											// Declare the number of threads in use.
	static int queueSize;
	static MyBlockingQueue<Runnable> queue;						// Declare the queue to store waiting tasks. 
	private Perform p;
	final static HashSet<Thread> workers = new HashSet<>();		// Declare a set of running threads
	static boolean isEmpty;										// Check if thread pool is empty
	public static GenerateLog log;								// Obtain log file to record interactions and error messages
	
	/**
	 * Constructor of OwnThreadPool class
	 * @param poolSize
	 * @param queueSize
	 * @param log 
	 */
	public OwnThreadPool(int poolSize, int queueSize, GenerateLog log) {
		OwnThreadPool.size = poolSize;
		inUse = 0;
		this.queueSize = queueSize;
		queue = new MyBlockingQueue<Runnable>(queueSize);
		p = new Perform();
		this.log = log;
	}
	
	@Override
	/**
	 * Add task into waiting queue and submit it to thread pool.
	 */
	public void submit(Runnable r) throws InterruptedException{
		queue.addTask(r);
		p.performFunction();
	}
	
	/**
	 * Check if thread pool is empty.
	 * @return (boolean) isEmpty
	 */
	public boolean empty() {
		getActiveCount();
		return this.isEmpty;
	}
	
	/**
	 * Obtain number of active threads in thread pool.
	 * @return
	 */
	public synchronized static int getActiveCount() {
		int s = workers.size();
		if(s == 0)
			isEmpty = true;
		else
			isEmpty = false;
		return s;
	}
	
	/**
	 * Shut down a thread in thread pool.
	 */
	public synchronized void closeWorker(DictionaryThread t) {
		workers.remove(t);
	}
}


/**
 * Control the number of threads being executed to not exceed the size of the thread pool and manage the thread queue.
 * @author yuqchang
 *
 */
class Perform implements Runnable {
	/**
	 * Check if thread pool has extra free threads, allocate a thread to the task in waiting queue.
	 * @throws InterruptedException
	 */
	void performFunction() throws InterruptedException {
		// Number of active threads is smaller than size of thread pool 
		if (OwnThreadPool.getActiveCount() < OwnThreadPool.size) {
			OwnThreadPool.log.logger.info("A task in waiting queue can enter thread pool to execute.\n");
			OwnThreadPool.inUse++;
			Thread t = new Thread(new Perform());
			// Allocate a thread in the thread pool to the task
			OwnThreadPool.workers.add(t);
			t.start();
		}
	}
		
	
	@Override
	/**
	 * Keep monitoring the state of waiting queue in thread pool;
	 * check if it has tasks that wait to execute.
	 */
	public void run() {
		while (true) {
			// Queue has waiting tasks
			if (OwnThreadPool.queue.getCurrentSizeOfQueue() != 0) {
				try {
					// Get task in front of the waiting queue and run this thread
					OwnThreadPool.queue.removeTask().run();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					OwnThreadPool.log.logger.info("Encounter an interrupted exception when obtain task from queue.\n");
					e.printStackTrace();
				}
			}
		}
}
}
	
/**
 * Set the size of my thread pool.
 * @author yuqchang
 *
 */
class MyExecutors {
	int size;
	
	public static OwnExecutorService myFixedThreadPool(int poolSize, int queueSize, GenerateLog log) {
		return new OwnThreadPool(poolSize, queueSize, log);
	}
}	



	

	
