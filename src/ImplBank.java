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

        System.out.println(sb.toString());
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

        return false;
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
}
