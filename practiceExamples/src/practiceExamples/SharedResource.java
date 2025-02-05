package practiceExamples;

public class SharedResource {
    private boolean available = false;

    public synchronized void produce() throws InterruptedException {
        while (available) {
            wait();  // Wait until the resource is consumed
        }
        System.out.println("Produced an item");
        available = true;
        notify();  // Notify the consumer thread
    }

    public synchronized void consume() throws InterruptedException {
        while (!available) {
            wait();  // Wait until the resource is produced
        }
        System.out.println("Consumed an item");
        available = false;
        notify();  // Notify the producer thread
    }

    public static void main(String[] args) {
        SharedResource resource = new SharedResource();

        Runnable producerTask = () -> {
            try {
                while (true) {
                    resource.produce();
                    Thread.sleep(1000);  // Simulate time taken to produce
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Runnable consumerTask = () -> {
            try {
                while (true) {
                    resource.consume();
                    Thread.sleep(1500);  // Simulate time taken to consume
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        };

        Thread producerThread = new Thread(producerTask);
        Thread consumerThread = new Thread(consumerTask);

        producerThread.start();
        consumerThread.start();
    }
}
