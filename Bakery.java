
public class Bakery extends Thread {

	// Variables for the threads. 
	public int thread_id; // The id of the current thread.
	public static final int countToThis = 200;
	public static final int numberOfThreads = 5;
	public static volatile int count = 0; // A simple counter for the testing.

	// Global variables for the bakery's algorithm.
	private static volatile boolean[] choosing = new boolean[numberOfThreads]; // Array that contains boolean values for each thread and it means that thread i wants to get into the critical area or not.
																				 
	private static volatile int[] ticket = new int[numberOfThreads]; // The ticket is used to define the priority.
	
	/*
	 * Thread constructor.
	 */
	public Bakery(int id) {
		thread_id = id;
	}

	// Simple test of a global counter. 
	public void run() {
		int scale = 2;

		for (int i = 0; i < countToThis; i++) {

			lock(thread_id);
				// Start of critical section.
				count = count + 1;
				System.out.println("I am " + thread_id + " and count is: " + count);
				
				// Wait, in order to cause a race condition among the threads.
				try {
					sleep((int) (Math.random() * scale));
				} catch (InterruptedException e) { /* nothing */ }
				// End of critical section.
			unlock(thread_id);
			
		} // for

	} // run method

	/*
	 * Method that does the lock of the bakery's algorithm.
	 */
	public void lock(int id) {
		// That means that the current thread (with id = id), is interested in getting into the critical section.
		choosing[id] = true;

		// Find the max value and add 1 to get the next available ticket.
		ticket[id] = findMax() + 1;
		choosing[id] = false;

		// System.out.println("Thread " + id + " got ticket in Lock");

		for (int j = 0; j < numberOfThreads; j++) {

			// If the thread j is the current thread go the next thread.
			if (j == id)
				continue;
			
			// Wait if thread j is choosing right now.
			while (choosing[j]) { /* nothing */ }

			
			while (ticket[j] != 0 && (ticket[id] > ticket[j] || (ticket[id] == ticket[j] && id > j))) { /* nothing */ }
						 
		} // for
	}

	/*
	 * Method that leaves the lock.
	 */
	private void unlock(int id) {
		ticket[id] = 0;
		// System.out.println("Thread " + id + " unlock");
	}

	/*
	 * Method that finds the max value inside the ticket array.
	 */
	private int findMax() {
		
		int m = ticket[0];

		for (int i = 1; i < ticket.length; i++) {
			if (ticket[i] > m)
				m = ticket[i];
		}
		return m;
	}

	public static void main(String[] args) {

		// Initialization of the global variables (it is not necessary at all).
		for (int i = 0; i < numberOfThreads; i++) {
			choosing[i] = false;
			ticket[i] = 0;
		}

		Bakery[] threads = new Bakery[numberOfThreads]; // Array of threads.

		// Initialize the threads.
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new Bakery(i);
			threads[i].start();
		}

		// Wait all threads to finish.
		for (int i = 0; i < threads.length; i++) {
			try {
				threads[i].join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		System.out.println("\nCount is: " + count);
		System.out.println("\nExpected was: " + (countToThis * numberOfThreads));
	} // main

}
