/** 
 * name: Yuqing Chang
 * student number: 1044862
 * username: yuqchang
 */
import java.util.LinkedList;
import java.util.Queue;


/**
 * MyBlockingQueue<Type> class implements blocking queue based on LinkedList structure.
 * It includes the synchronized function: addTask and removeTask.
 * @author yuqchang
 *
 * @param <Type>
 */
public class MyBlockingQueue<DictionaryThread> {
	private Queue<DictionaryThread> queue = new LinkedList<DictionaryThread>();
	// Final variables: empty state of queue, default size of queue
    private int EMPTY = 0;
    private int MAX_SIZE_OF_QUEUE = -1;
    
    /**
     * Constructor of MyBlockingQueue class
     * @param size
     */
    public MyBlockingQueue(int size){
        this.MAX_SIZE_OF_QUEUE = size;
    }
    
    /**
     * Obtain current size of queue.
     * @return
     */
    public int getCurrentSizeOfQueue() {
    	return this.queue.size();
    }
    
    /**
     * Add task to waiting Blocking Queue.
     * @param t
     * @throws InterruptedException
     */
    public synchronized void addTask(DictionaryThread t) throws InterruptedException  {
    	// The blocking queue is already full
        while(this.queue.size() == this.MAX_SIZE_OF_QUEUE) {
            wait();
        }
        // The blocking queue is empty
        if(this.queue.size() == getEMPTY()) {
            notifyAll();
        }
        // Insert the specified element into this queue
        this.queue.offer(t);
    }
    
    /**
     * Remove task in front of waiting Blocking Queue.
     * @return
     * @throws InterruptedException
     */
    public synchronized DictionaryThread removeTask() throws InterruptedException{
        while(this.queue.size() == getEMPTY()){
            wait();
        }
        if(this.queue.size() == this.MAX_SIZE_OF_QUEUE){
            notifyAll();
        }
        // Remove the element at the front the queue
        return this.queue.poll();
    }
    
    /**
     * Obtain empty state of blocking queue.
     * @return
     */
	public int getEMPTY() {
		return EMPTY;
	}

}
