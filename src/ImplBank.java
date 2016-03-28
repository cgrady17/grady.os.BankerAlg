/**
 * Implementation of the Bank interface of the
 * Banker's Algorithm.
 *
 * @author Connor Grady
 * @version 1.0
 */
public class ImplBank implements Bank {
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

    }

    /**
     * Outputs the value of available, maximum, allocation, and need.
     */
    @Override
    public void getState() {

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
        return false;
    }
}
