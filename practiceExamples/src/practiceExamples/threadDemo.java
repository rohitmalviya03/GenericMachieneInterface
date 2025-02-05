package practiceExamples;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class threadDemo {

	public static void main(String[] args) {
		ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
		executor.scheduleAtFixedRate(new RunnableTask(), 0, 10, TimeUnit.SECONDS);
		executor.shutdown();


	}
	
	
	

}
 class RunnableTask implements Runnable {
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " is executing the task.");
        try {
            Thread.sleep(1000); // Simulate work with sleep
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println(Thread.currentThread().getName() + " has finished executing the task.");
    }
}

