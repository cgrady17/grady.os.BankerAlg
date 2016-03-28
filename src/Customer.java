import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Represents a Customer of a Bank in
 * the Banker's Algorithm.
 *
 * @author Connor Grady
 * @version 1.0
 */
public class Customer implements Runnable {
    public static final int COUNT = 5;

    private int numOfResources; // the number of different resources
    private int[] demand; // the maximum this Customer will demand
    private int custNum; // this Customer's number
    private int[] request; // this Customer's resource request

    private Random random; // Random number generator

    private Bank bank; // The Bank

    public Customer(int custNum, int[] demand, Bank bank) {
        this.custNum = custNum;
        this.demand = new int[demand.length];
        this.bank = bank;

        System.arraycopy(demand, 0, this.demand, 0, demand.length);
        numOfResources = demand.length;
        request = new int[numOfResources];
        random = new Random();
    }


    /**
     * When an object implementing interface <code>Runnable</code> is used
     * to create a thread, starting the thread causes the object's
     * <code>run</code> method to be called in that separately executing
     * thread.
     * <p>
     * The general contract of the method <code>run</code> is that it may
     * take any action whatsoever.
     *
     * @see Thread#run()
     */
    @Override
    public void run() {
        boolean canRun = true;

        while (canRun) {
            try {
                awaitCustomer();

                // make a resource request
                for (int i = 0; i < numOfResources; i++) {
                    request[i] = random.nextInt(demand[i] + 1);
                }

                if (bank.requestResources(custNum, request)) {
                    awaitCustomer();

                    bank.releaseResources(custNum, request);
                }
            } catch (InterruptedException ie) {
                canRun = false;
            }
        }

        System.out.println("Customer " + custNum + ": Interrupted.");
    }

    private static void awaitCustomer() throws InterruptedException {
        int time = (int)(5 * Math.random());

        Thread.sleep(time * 1000);
    }
}
