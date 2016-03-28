import java.util.Arrays;

import static java.lang.System.lineSeparator;

/**
 * Implementation of the Bank interface of the
 * Banker's Algorithm.
 *
 * @author Connor Grady
 * @version 1.0
 */
public class ImplBank implements Bank {
    private static final int MAX_REQUESTS = 20; // The maximum number of resource requests
    private int numOfRequests; // the number of resource requests

    private int numOfCustomers = Customer.COUNT; // The number of Customers
    int numOfResources; // The number of Resources

    // Array containing the amount of each resource available
    private int[] available;

    // 2d array containing the maximum demand of each Customer
    private int[][] maximum;

    // 2d array containing the amount currently allocated to each Customer
    private int[][] allocation;

    // 2d array containing the remaining needs of each Customer
    private int[][] need;

    /**
     * Initializes a new instance of ImplBank with the specified
     * resource availability.
     * @param avail The availability of resources.
     */
    public ImplBank(int[] avail) {
        this.available = avail;
        this.maximum = new int[numOfCustomers][available.length];
        this.allocation = new int[numOfCustomers][available.length];
    }

    /**
     * Adds a Customer to the Bank.
     *
     * @param custNum   The Number of the new Customer.
     * @param maxDemand The maximum demand for this Customer.
     */
    @Override
    public void addCustomer(int custNum, int[] maxDemand) {
        // Check if the specified Customer Number is greater
        // than the length of the maximum array
        if (custNum > maximum.length) {
            // Re-create the maximum array with an increased sizes
            maximum = resizeCustomerArray(maximum, custNum);
        }

        // Add the specified demand to the maximum array
        // at the specified customer number
        maximum[custNum] = maxDemand;
    }

    /**
     * Outputs the value of available, maximum, allocation, and need.
     */
    @Override
    public void getState() {
        /*StringBuilder sb = new StringBuilder("Outputting State..." + lineSeparator());

        sb.append("Resource Availability:").append(lineSeparator());
        for (int i = 0; i < available.length; i++) {
            sb.append("Resource ").append(i).append(": ").append(available[i]).append(lineSeparator());
        }

        sb.append(lineSeparator()).append("Customer Maximum Demand:").append(lineSeparator());
        writeCustomerArray(maximum, sb);

        sb.append(lineSeparator()).append("Customer Resource Allocation:").append(lineSeparator());
        writeCustomerArray(allocation, sb);

        sb.append(lineSeparator()).append("Customer Needs:").append(lineSeparator());
        writeCustomerArray(need, sb);

        sb.append(lineSeparator());

        System.out.println(sb);*/

        StringBuilder sb = new StringBuilder();

        // Append available
        sb.append("\n\nAvailable:\n----------\n");
        for(int i = 0; i < available.length; i++){
            sb.append("Resource " + i + ": " + available[i] + "\n");
        }

        // Append allocated
        sb.append("\n\nAllocated:\n----------\n");
        for(int i = 0; i < allocation[0].length; i++){
            // Sum of all allocated resources
            int sum = 0;

            for(int j = 0; j < allocation.length; j++){
                sum += allocation[j][i];
            }

            sb.append("Resource " + i + ": " + sum + "\n");
        }

        // Append max
        sb.append("\n\nMax demand:\n----------");
        for(int i = 0; i < maximum.length; i++){
            sb.append("\nCustomer " + i + ": ");

            // Append individual resources to line.
            for(int j = 0; j < maximum[i].length; j++){
                sb.append(" Resource " + j + ": " + maximum[i][j]);
            }
        }

        // Append need
        sb.append("\n\nNeed:\n----------");
        for(int i = 0; i < maximum.length; i++){
            sb.append("\nCustomer " + i + ": ");

            for(int j = 0; j < maximum[i].length; j++){
                sb.append(" Resource " + j + ": " + (maximum[i][j] - maximum[i][j]));
            }
        }

        // Print everything in one go.
        System.out.println(sb);
    }

    /**
     * Requests the specified resources for the specified Customer.
     *
     * @param custNum The number of the Customer requesting the resources.
     * @param request The resources being requested.
     * @return True/False indicating if the resources were granted.
     */
    @Override
    public boolean requestResources(int custNum, int[] request) {
        logRequest();

        for (int i = 0; i < request.length; i++) {
            if (request[i] > available[i]) return false;

            if (request[i] > maximum[custNum][i]) return false;
        }

        if (!safeState(custNum, request)) return false;

        for (int i = 0; i < request.length; i++) {
            available[i] -= request[i];
            allocation[custNum][i] += request[i];
        }

        return true;
    }

    /**
     * Release resources.
     * @param custNum The number of the customer being added.
     * @param release The resources to be released.
     */
    public synchronized void releaseResources(int custNum, int[] release){
        // Release resources
        for(int i = 0; i < release.length; i++){
            available[i] += release[i];
            allocation[custNum][i] -= release[i];
        }
        getState();
    }

    /**
     * Indicates whether a state is safe.
     * @param custNum The number of the Customer.
     * @param request The request to check.
     * @return Boolean indicating if the State is safe.
     */
    private boolean safeState(int custNum, int[] request) {
        int[] clonedResources = available.clone();
        int[][] clonedAllocation = allocation.clone();

        // First check if any part of the request requires more resources than are available (unsafe state)
        for (int i = 0; i < clonedResources.length; i++) {
            if (request[i] > clonedResources[i]) {
                return false;
            }
        }

        // If we reach this point, the first request was valid so we execute it on the simulated resources
        for (int i = 0; i < clonedResources.length; i++) {
            clonedResources[i] -= request[i];
            clonedAllocation[custNum][i] += request[i];
        }

        // Create new boolean array and set all to false
        boolean[] canFinish = new boolean[numOfCustomers];

        for (int i = 0; i < canFinish.length; i++) {
            canFinish[i] = false;
        }

        // Now check if there is an order wherein other customers can still finish after us
        for (int i = 0; i < numOfCustomers; i++) {
            // Find a customer that can finish a request. Loop through all resources per customer
            for (int j = 0; j < numOfCustomers; j++) {
                if (!canFinish[j]) {
                    for (int k = 0; k < clonedResources.length; k++) {
                        // If the need (maximum - allocation = need) is not greater than the amount of available resources, thread can finish
                        if (!((maximum[j][k] - clonedAllocation[j][k]) > clonedResources[k])) {
                            canFinish[j] = true;
                            for (int l = 0; l < clonedResources.length; l++) {
                                clonedResources[l] += clonedAllocation[j][l];
                            }
                        }
                    }
                }
            }
        }

        // restore the value of need and allocation for this thread
        for (int i = 0; i < available.length; i++) {
            clonedAllocation[custNum][i] -= request[i];
        }

        // After all the previous calculations. Loop through the array and see if every customer could complete the transaction for their maximum demand
        for (boolean aCanFinish : canFinish) {
            if (!aCanFinish) {
                return false;
            }
        }

        return true;
    }

    /**
     * Writes a two-dimensional array of Customer data to the
     * specified StringBuilder.
     * @param array 2D array containing the Customer data.
     * @param sb StringBuilder object in which to write the output.
     */
    private void writeCustomerArray(int[][] array, StringBuilder sb) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == null || array[i].length == 0) continue;

            sb.append("Customer ").append(i).append(":");

            for (int k : array[i]) {
                sb.append(" ").append(k);
            }
        }
    }

    /**
     * Logs that a resource request was made and checks
     * whether the request ceiling has been reached.
     */
    private void logRequest() {
        if (numOfRequests >= MAX_REQUESTS) {
            // @TODO Handle hitting request max
            Thread.currentThread().interrupt();
        }

        numOfRequests++;
    }

    /**
     * Creates a new 2d array from the specified original with
     * the specified new length. Useful for resizing 2d arrays, as
     * the Arrays utility does not support such a feature.
     * @param original The original 2d array to resize.
     * @param newLength The length of the new resized array.
     * @return A new, resized 2d array with the contents of the original.
     */
    private static int[][] resizeCustomerArray(int[][] original, int newLength) {
        if (original == null) return null;

        final int[][] result = new int[newLength][];
        for (int i = 0; i < original.length; i++) {
            result[i] = Arrays.copyOf(original[i], original[i].length);
        }

        return result;
    }
}
