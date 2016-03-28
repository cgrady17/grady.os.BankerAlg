import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Main/Driver used for testing the implementation of the Banker's Algorithm.
 *
 * @author Connor Grady
 * @version 1.0
 */
public class Driver {

    public static void main(String[] args) {
        System.out.println("Banker's Algorithm | Connor Grady | Version 1.0");
        System.out.println("This implementation uses a fixed set of resources, 1, 2, and 3. " +
        "You will need to provide the number of instances of each of these resources.");

        Scanner s = new Scanner(System.in);
        int[] resources = new int[3];
        for (int i = 0; i < resources.length; i++) {
            System.out.print("Please enter the number of instances of resource " + i + ": ");
            resources[i] = Integer.parseInt(s.nextLine());
        }

        Bank bank = new ImplBank(resources);

        int[] maximum = new int[resources.length];

        Thread[] customers = new Thread[Customer.COUNT];

        int custNum = 0;
        int resourceNum = 0;

        for (int i = 0; i < Customer.COUNT; i++) {
            for (int resource : resources) {
                maximum[resourceNum++] = getRandomInt(resource);
            }

            customers[custNum] = new Thread(new Customer(custNum, maximum, bank));
            bank.addCustomer(custNum, maximum);

            ++custNum;

            resourceNum = 0;
        }

        for (Thread thread : customers) {
            thread.start();
        }
    }

    /**
     * Returns a random Integer between 0 and the specified
     * maximum, inclusive.
     * @param max The higher bound of the range for the generated number.
     * @return Random integer between 0 and the max, inclusive.
     */
    private static int getRandomInt(int max) {
        return ThreadLocalRandom.current().nextInt(0, max + 1);
    }
}
