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
    private static final int MAX_REQUESTS = 20; // The maxinum number of resource requests
    int numOfRequests; // the number of resource requests

    int numOfCustomers; // The number of Customers
    int numOfResources; // The number of Resources

    // Array containing the amount of each resource available
    int[] available;

    // 2d array containing the maximum demand of each Customer
    int[][] maximum;

    // 2d array containing the amount currently allocated to each Customer
    int[][] allocation;

    // 2d array containing the remaining needs of each Customer
    int[][] need;

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
        StringBuilder sb = new StringBuilder("Outputting State..." + lineSeparator());

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
                        // If the need (maxdemand - allocated = need) is noot bigger than the amount of available resources, thread can finish
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
        numOfRequests++;

        if (numOfRequests >= MAX_REQUESTS) {
            // @TODO Handle hitting request max
        }
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
