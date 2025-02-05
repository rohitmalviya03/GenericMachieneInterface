package Server;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadDemo {
    public static void main(String[] args) {
        // Create a fixed-size thread pool with 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(150);

        // Submit tasks to the executor
        for (int i = 0; i < 30; i++) {
            final int taskNumber = i;
            executor.execute(() -> {
                System.out.println("Task " + taskNumber + " executed by thread " + Thread.currentThread().getName());
            });
        }

        // Shutdown the executor when tasks are done
       // executor.shutdown();
    }
}