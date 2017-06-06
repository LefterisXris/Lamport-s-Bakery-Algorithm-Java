## Lamport's Bakery Algorithm. 

This is a Java implementation of Bakery Algorithm. For more information about this algorithm please visit [Wikipedia](https://en.wikipedia.org/wiki/Lamport%27s_bakery_algorithm "Lamport's Bakery Algorithm in Wikipedia").

How to use it:

Just compile the code and run it. By default, the test is to create 5 threads that each has to loop 200 times and increase a global counter (same for all threads). The expected result should be 200 * 5 = 1000. You can change those values from the variables "numberOfThreads" and "countToThis".

The main "work" of the algorithm is inside the method "lock". There, only one thread will manage to exit the loops and it will keep the lock until it runs the unlock method. 
